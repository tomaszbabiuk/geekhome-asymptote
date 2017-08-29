package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import javax.inject.Inject;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.controls.SimpleHeaderDecoration;
import eu.geekhome.asymptote.databinding.FragmentMainBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.DeviceSnapshot;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudCertificateChecker;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.SyncListener;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.utils.ByteUtils;
import eu.geekhome.asymptote.utils.Ticker;

public class MainViewModel extends ViewModel<FragmentMainBinding> implements SyncListener, SensorItemViewModel.SensorLifecycleListener {
    private ArrayList<DeviceSnapshot> _deviceSnapshots;
    private final Hashtable<InetAddress, Byte[]> _firebaseDevices;
    private ObservableArrayList<LayoutHolder> _sensors = new ObservableArrayList<>();
    private ArrayList<InetAddress> _securedDevices = new ArrayList<>();
    private DiscoveryViewModel _discoveryViewModel;
    private SecuredDevicesFoundViewModel _securedDevicesFoundViewModel;
    private Ticker _aliveTicker;
    private Ticker _discoveryTicker;
    private byte _discoveryTick;
    private MainActionBarViewModel _actionBarModel;
    private String _errorMessage;
    private String _userId;
    private boolean _notAuthorizedDialogShown;
    private boolean _reloadDevices = false;
    private byte[] _cloudFingerPrint;

    @Inject
    NavigationService _navigationService;
    @Inject
    ThreadRunner _threadRunner;
    @Inject
    SyncManager _syncManager;
    @Inject
    WiFiHelper _wifiHelper;
    @Inject
    EmergencyManager _emergencyManager;
    @Inject
    Context _context;
    @Inject
    CloudDeviceService _cloudDeviceService;
    @Inject
    CloudCertificateChecker _cloudCertificateChecker;

    private interface SensorFoundDelegate {
        void sensorFound(SensorItemViewModel sensor);
    }

    @Bindable
    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        _errorMessage = errorMessage;
        notifyPropertyChanged(BR.errorMessage);
    }

    @Bindable
    public MainActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getSensors() {
        return _sensors;
    }

    public MainViewModel(ActivityComponent activityComponent, String userId, UserSnapshot userSnapshot) {
        super(activityComponent);
        _actionBarModel = new MainActionBarViewModel(activityComponent);
        _userId = userId;
        _notAuthorizedDialogShown = false;
        _discoveryViewModel = new DiscoveryViewModel();
        _securedDevicesFoundViewModel = new SecuredDevicesFoundViewModel(activityComponent, this, userId);
        _deviceSnapshots = userSnapshot.getDeviceSnapshots();
        _firebaseDevices = new Hashtable<>();
    }

    @NonNull
    private Ticker createDiscoveryTicker() {
        _discoveryTick = 60;

        return new Ticker(new Ticker.Listener() {
            @Override
            public void onTick() {
                if (_discoveryTick == 50) {
                    checkIfAllSnapshotsReport();
                }

                if (_discoveryTick < 0) {
                    _discoveryViewModel.setTimingMessage(_context.getString(_sensors.size() > 1 ? R.string.sensors_found : R.string.no_lan_sensors_found));
                } else {
                    _discoveryViewModel.setTimingMessage(_context.getString(R.string.discovery_elapsed, _discoveryTick));
                }

                _discoveryTick--;

                if (_discoveryTick < -10) {
                    _discoveryTicker.stop();
                }
            }

            @Override
            public void onElapsed() {
                if (_sensors.size() > 1) {
                    _sensors.remove(_discoveryViewModel);
                }
            }
        }, 70, 1000);
    }

    private void checkIfAllSnapshotsReport() {
        if (_deviceSnapshots != null) {
            final boolean[] snaphotReports = {false};
            for (final DeviceSnapshot snapshot : _deviceSnapshots) {
                iterateSensors(new SensorFoundDelegate() {
                    @Override
                    public void sensorFound(SensorItemViewModel sensor) {
                        if (sensor.getSyncData().getDeviceKey().getChipId().equals(snapshot.getChipId())) {
                            snaphotReports[0] = true;
                        }
                    }
                });
            }

            if (!snaphotReports[0]) {
                scheduleCertificateUpdate();
            }
        }
    }

    @NonNull
    private Ticker createAliveTicker() {
        return new Ticker(new Ticker.Listener() {
            @Override
            public void onTick() {
                final long now = Calendar.getInstance().getTimeInMillis();
                iterateSensors(new SensorFoundDelegate() {
                    @Override
                    public void sensorFound(final SensorItemViewModel sensor) {
                        sensor.checkAlive(now);

                        Variant variant = sensor.getSyncData().getSystemInfo().getVariant();
                        boolean sync = variant.isWifi() &&
                                !sensor.isSyncDelayed() &&
                                _wifiHelper.isWifiConnected();

                        if (sync) {
                            _syncManager.sync(variant, sensor.getAddress(), new SyncManager.SyncCallback() {
                                @Override
                                public void success() {
                                    //great!
                                }

                                @Override
                                public void failure(Exception exception) {
                                    if (sensor.isAlive()) {
                                        if (_wifiHelper.isWifiConnected()) {
                                            sensor.setBlocked(true);
                                        }
                                    }
                                }
                            });
                        }

                        if (variant.isCloud() && !sensor.isHasCloudSignal()) {
                            scheduleCertificateUpdate();
                        }
                    }
                });
            }

            @Override
            public void onElapsed() {
            }
        }, -1, 5000);
    }

    private void scheduleCertificateUpdate() {
        if (_cloudFingerPrint == null) {
            _cloudCertificateChecker.check(new CloudCertificateChecker.Listener() {
                @Override
                public void onChecked(byte[] sha1Thumbprint) {
                    _cloudFingerPrint = sha1Thumbprint;
                    updateCloudFingerprints();
                }

                @Override
                public void onError(IOException e) {
                }
            });
        } else {
            updateCloudFingerprints();
        }
    }

    private void updateCloudFingerprints() {
        if (_deviceSnapshots != null) {
            for (DeviceSnapshot snapshot : _deviceSnapshots) {
                snapshot.getRestoreToken();
                String fingerprint = ByteUtils.bytesToHex(_cloudFingerPrint).toLowerCase();
                byte[] hashBytes = ByteUtils.createHmacSHA256(snapshot.getRestoreToken(), fingerprint);
                String hash = ByteUtils.bytesToHex(hashBytes).toLowerCase();
                _cloudDeviceService.updateCertificateFingerprint(_userId, snapshot.getDeviceToken(), fingerprint, hash, new CloudActionCallback<Void>() {
                    @Override
                    public void success(Void data) {

                    }

                    @Override
                    public void failure(CloudException exception) {
                        setErrorMessage(exception.getLocalizedMessage());
                    }
                });
            }
        }
    }

    @Override
    public FragmentMainBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.setVm(this);
        RecyclerView view = binding.recycler;
        int footerSize = (int) _context.getResources().getDimension(R.dimen.sensorlist_fake_footer_size);
        view.addItemDecoration(new SimpleHeaderDecoration(0, footerSize));
        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    void rediscover() {
        _sensors.clear();
        onPause();
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!_sensors.contains(_discoveryViewModel)) {
            _sensors.add(0, _discoveryViewModel);
        }
        _discoveryTicker = createDiscoveryTicker();

        try {
            _syncManager.setSyncListener(this);
            _syncManager.start();
        } catch (Exception e) {
            setErrorMessage(e.getLocalizedMessage());
        }

        if (!_emergencyManager.isEmergency()) {
            if (_deviceSnapshots == null || _reloadDevices) {
                _reloadDevices = false;
                _deviceSnapshots = new ArrayList<>();
                _cloudDeviceService.getUserSnapshot(_userId, new CloudActionCallback<UserSnapshot>() {
                    @Override
                    public void success(UserSnapshot userSnapshot) {
                        for (final DeviceSnapshot deviceSnapshot : userSnapshot.getDeviceSnapshots()) {
                            _cloudDeviceService.registerForDeviceSyncEvents(_userId, deviceSnapshot);
                            _deviceSnapshots.add(deviceSnapshot);
                        }
                    }

                    @Override
                    public void failure(CloudException exception) {
                        setErrorMessage(exception.getLocalizedMessage());
                    }
                });
            } else {
                for (final DeviceSnapshot deviceSnapshot : _deviceSnapshots) {
                    _cloudDeviceService.registerForDeviceSyncEvents(_userId, deviceSnapshot);
                }
            }
        }

        _aliveTicker = createAliveTicker();
        _cloudDeviceService.setSyncListener(this);
    }

    @Override
    public void onPause() {
        _reloadDevices = true;
        _sensors.remove(_discoveryViewModel);

        if (_aliveTicker != null) {
            _aliveTicker.stop();
        }

        if (_discoveryTicker != null) {
            _discoveryTicker.stop();
        }

        _cloudDeviceService.setSyncListener(null);
        _cloudDeviceService.unregisterFromDeviceSyncEvents(_userId);
        _syncManager.setSyncListener(null);
        _syncManager.stop();

        super.onPause();
    }

    @Override
    public boolean goingBack() {
        return false;
    }

    private SensorItemViewModel findSensorOnTheList(String deviceId) {
        if (_sensors.size() > 0) {
            for (LayoutHolder item : _sensors) {
                if (item instanceof SensorItemViewModel) {
                    SensorItemViewModel sensor = (SensorItemViewModel) item;
                    if (deviceId.equals(sensor.getSyncData().getDeviceKey().getDeviceId())) {
                        return sensor;
                    }
                }
            }
        }

        return null;
    }

    private void iterateSensors(SensorFoundDelegate foundDelegate) {
        if (_sensors.size() > 0) {
            for (LayoutHolder item : _sensors) {
                if (item instanceof SensorItemViewModel) {
                    SensorItemViewModel sensor = (SensorItemViewModel) item;
                    foundDelegate.sensorFound(sensor);
                }
            }
        }
    }

    private boolean hasUnsupportedBoardItem(String deviceId) {
        if (_sensors.size() > 0) {
            for (LayoutHolder item : _sensors) {
                if (item instanceof BoardUnsupportedViewModel) {
                    BoardUnsupportedViewModel sensor = (BoardUnsupportedViewModel) item;
                    if (deviceId.equals(sensor.getDeviceId())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    @Override
    public void onAfterSync(final InetAddress from, final DeviceSyncData syncData, final long timestamp,
                            final String token) {
        if (_securedDevices.contains(from)) {
            _securedDevices.remove(from);
            updateSecuredDevicesUI();
        }

        _threadRunner.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SensorItemViewModel sensor = findSensorOnTheList(syncData.getDeviceKey().getDeviceId());
                if (sensor == null) {
                    final SensorItemViewModel newSensor = new SensorItemViewModel(getActivityComponent(),
                            MainViewModel.this, from, syncData, timestamp, _userId, token);
                    _sensors.add(newSensor);
                } else {
                    sensor.setAddress(from);
                    sensor.setSyncData(syncData, timestamp);
                    if (sensor.getUpdates().size() > 0) {
                        sensor.onRequestFullSync();
                    }
                    if (token != null) {
                        sensor.setToken(token);
                    }
                }
            }
        });
    }

    @Override
    public void onUnsupportedBoard(InetAddress from, String deviceId) {
        boolean hasUnsupportedItem = hasUnsupportedBoardItem(deviceId);
        if (!hasUnsupportedItem) {
            BoardUnsupportedViewModel model = new BoardUnsupportedViewModel(deviceId);
            _sensors.add(model);
        }
    }

    @Override
    public void onSecuredDeviceFound(InetAddress from) {
        if (!_securedDevices.contains(from)) {
            _securedDevices.add(from);
        }
        updateSecuredDevicesUI();
    }

    @Override
    public void onCloudDeviceFound(InetAddress from, Variant variant, BoardId boardId, Byte[] restoreTokenPart) {
        if (!_firebaseDevices.containsKey(from)) {
            _firebaseDevices.put(from, restoreTokenPart);
        }
        updateFirebaseDevicesStatus();
    }

    private void updateFirebaseDevicesStatus() {
        if (_deviceSnapshots != null && _firebaseDevices != null) {
            for (Map.Entry<InetAddress, Byte[]> deviceRecord : _firebaseDevices.entrySet()) {
                for (final DeviceSnapshot snapshot : _deviceSnapshots) {
                    String token = new String(new byte[]{
                            deviceRecord.getValue()[0],
                            deviceRecord.getValue()[1],
                            deviceRecord.getValue()[2],
                            deviceRecord.getValue()[3],
                            deviceRecord.getValue()[4],
                            deviceRecord.getValue()[5],
                            deviceRecord.getValue()[6],
                            deviceRecord.getValue()[7]
                    });

                    if (snapshot.getRestoreToken().startsWith(token)) {
                        final String chipId = snapshot.getChipId();
                        iterateSensors(new SensorFoundDelegate() {
                            @Override
                            public void sensorFound(SensorItemViewModel sensor) {
                                if (sensor.getSyncData().getDeviceKey().getChipId().equals(chipId)) {
                                    sensor.setRestoreToken(snapshot.getRestoreToken());
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private void updateSecuredDevicesUI() {
        StringBuilder addresses = new StringBuilder();
        for (InetAddress address : _securedDevices) {
            addresses.append(_context.getString(R.string.device_invalid_password, address));
        }
        _securedDevicesFoundViewModel.setAddresses(addresses.toString());

        if (_securedDevices.size() > 0) {
            _threadRunner.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!_sensors.contains(_securedDevicesFoundViewModel)) {
                        if (_sensors.size() > 0) {
                            _sensors.add(1, _securedDevicesFoundViewModel);
                        } else {
                            _sensors.add(_securedDevicesFoundViewModel);
                        }
                    }
                    if (!_notAuthorizedDialogShown) {
                        _notAuthorizedDialogShown = true;
                        SetEmergencyPasswordViewModel setEmergencyPasswordViewModel =
                                new SetEmergencyPasswordViewModel(getActivityComponent(), MainViewModel.this, _userId);
                        _navigationService.showOverlayViewModel(setEmergencyPasswordViewModel);
                    }
                }
            });
        } else {
            _sensors.remove(_securedDevicesFoundViewModel);
        }


    }

    @Override
    public void recreated(final SensorItemViewModel recreated) {
        final int index = _sensors.indexOf(recreated);
        _sensors.remove(recreated);
        _sensors.add(index, recreated);
    }

    @Override
    public void locking(final SensorItemViewModel sender) {
        if (_emergencyManager.getPassword() == null) {
            LockViewModel lockViewModel = new LockViewModel(getActivityComponent(), this, sender);
            _navigationService.showOverlayViewModel(lockViewModel);
        } else {
            _syncManager.lock(sender.getSyncData().getSystemInfo().getVariant(), sender.getAddress(), new SyncManager.SyncCallback() {
                @Override
                public void success() {
                    sender.getSyncData().setLocked(true);
                    sender.requestSyncDelayed();
                }

                @Override
                public void failure(Exception exception) {
                    setErrorMessage(exception.getLocalizedMessage());
                }
            }, _emergencyManager.getPassword());
        }
    }
}