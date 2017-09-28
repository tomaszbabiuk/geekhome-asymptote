package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.Calendar;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentOtaBinding;
import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.CloudFingerprintSyncUpdate;
import eu.geekhome.asymptote.model.CloudPasswordSyncUpdate;
import eu.geekhome.asymptote.model.CloudUsernameSyncUpdate;
import eu.geekhome.asymptote.model.DeviceSnapshot;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.model.OtaHashSyncUpdate;
import eu.geekhome.asymptote.model.OtaHostSyncUpdate;
import eu.geekhome.asymptote.model.RestoreTokenSyncUpdate;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudCertificateChecker;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.OtaServer;
import eu.geekhome.asymptote.services.SyncListener;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;
import eu.geekhome.asymptote.utils.ByteUtils;
import eu.geekhome.asymptote.utils.Ticker;

public class OtaViewModel extends WiFiAwareViewModel<FragmentOtaBinding> implements SyncListener, OtaServer.Listener {
    private final Variant _previousVariant;
    private long _serverStartedAt;
    private OtaPhase _otaPhase;
    private final SensorItemViewModel _sensor;
    private Ticker _conductTicker;
    private String _errorMessage;
    private final ThreadRunner _threadRunner;
    private Firmware _firmware;
    private boolean _restoreTokenUsed;
    private final HelpActionBarViewModel _actionBarModel;
    private final OtaServer _otaServer;
    private final NavigationService _navigationService;
    private final Context _context;
    private final SyncManager _syncManager;
    private final WiFiHelper _wifiHelper;
    private final CloudDeviceService _cloudDeviceService;
    private final CloudCertificateChecker _certificateChecker;
    private final MainViewModelsFactory _factory;

    @Bindable
    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        notifyPropertyChanged(BR.errorMessage);
        _errorMessage = errorMessage;
    }

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    public OtaViewModel(Context context, MainViewModelsFactory factory, OtaServer otaServer,
                        NavigationService navigationService, SyncManager syncManager,
                        WiFiHelper wifiHelper, CloudDeviceService cloudDeviceService,
                        CloudCertificateChecker cloudCertificateChecker,
                        ThreadRunner threadRunner,
                        SensorItemViewModel sensor, Firmware firmware) {
        super(factory, wifiHelper, navigationService);
        _context = context;
        _navigationService = navigationService;
        _wifiHelper = wifiHelper;
        _certificateChecker = cloudCertificateChecker;
        _factory = factory;
        _otaServer = otaServer;
        _syncManager = syncManager;
        _cloudDeviceService = cloudDeviceService;
        _threadRunner = threadRunner;
        _firmware = firmware;
        _sensor = sensor;
        _previousVariant = _sensor.getSyncData().getSystemInfo().getVariant();
        _actionBarModel = _factory.createHelpActionBarModel();
        _serverStartedAt = 0;
        setOtaPhase(OtaPhase.ServerStarting);
    }

    @NonNull
    private Ticker createConductTicker() {
        return new Ticker(new Ticker.Listener() {
            @Override
            public void onTick() {
                Variant variant = (getOtaPhase() == OtaPhase.InProgress || getOtaPhase() == OtaPhase.Finished)
                        ? _firmware.getVariant()
                        : _sensor.getSyncData().getSystemInfo().getVariant();
                _syncManager.sync(variant, _sensor.getAddress(), new SyncManager.SyncCallback() {
                    @Override
                    public void success() {
                    }

                    @Override
                    public void failure(Exception exception) {
                    }
                });

                boolean isProgressPhase = _otaPhase == OtaPhase.WaitingForDevice || _otaPhase == OtaPhase.InProgress;
                boolean timeElapsed = Calendar.getInstance().getTimeInMillis() > _serverStartedAt + 160 * 1000;
                if (isProgressPhase && timeElapsed) {
                    processTimeElapsed();
                }
            }

            @Override
            public void onElapsed() {
            }
        }, -1, 5000);
    }

    @Bindable
    public OtaPhase getOtaPhase() {
        return _otaPhase;
    }

    private void setOtaPhase(final OtaPhase value) {
        _otaPhase = value;
        notifyPropertyChanged(BR.otaPhase);
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    @Override
    protected boolean isCloudOnlyAllowed() {
        return false;
    }

    @Override
    protected String getNoWiFiRationale() {
        return _context.getString(R.string.rationale_nowifi_ota_impossible);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_firmware != null) {
            try {
                _syncManager.start();
                _syncManager.setSyncListener(this);
            } catch (IOException e) {
                setErrorMessage(e.getLocalizedMessage());
            }
            startOtaServer();
            _conductTicker = createConductTicker();

            _cloudDeviceService.setSyncListener(this);

            if (_sensor.getUserId() != null) {
                _cloudDeviceService.getUserSnapshot(_sensor.getUserId(), new CloudActionCallback<UserSnapshot>() {
                    @Override
                    public void success(UserSnapshot userSnapshot) {
                        for (final DeviceSnapshot deviceSnapshot : userSnapshot.getDeviceSnapshots()) {
                            _cloudDeviceService.registerForDeviceSyncEvents(_sensor.getUserId(), deviceSnapshot);
                        }
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
    public void onPause() {
        if (_conductTicker != null) {
            _conductTicker.stop();
        }
        _otaServer.stop();
        _syncManager.stop();
        _syncManager.setSyncListener(null);

        _cloudDeviceService.setSyncListener(null);
        if (_sensor.getUserId() != null) {
            _cloudDeviceService.unregisterFromDeviceSyncEvents(_sensor.getUserId());
        }

        super.onPause();
    }


    private void started() {
        _serverStartedAt = Calendar.getInstance().getTimeInMillis();
        _restoreTokenUsed = false;
        switch (_firmware.getVariant()) {
            case WiFi:
                sendOtaInformationToDevice();
                break;
            case Hybrid:
            case Firebase:
                if (_firmware.getVariant().isCloud()) {
                    promoteDeviceToFirebase();
                } else {
                    sendOtaInformationToDevice();
                }
                break;
        }
    }

    private void sendOtaInformationToDevice() {
        String md5 = _firmware.getMd5();
        String host = String.format("http://%s:8890/", _wifiHelper.getIP());
        OtaHashSyncUpdate hashSyncUpdate = new OtaHashSyncUpdate(md5);
        OtaHostSyncUpdate hostSyncUpdate = new OtaHostSyncUpdate(host);
        _sensor.getUpdates().add(hashSyncUpdate);
        _sensor.getUpdates().add(hostSyncUpdate);
        _sensor.requestFullSync();
        _sensor.getUpdates().clear();
        setOtaPhase(OtaPhase.WaitingForDevice);
    }

    private void promoteDeviceToFirebase() {
        SecureRandom random = new SecureRandom();
        String chipId = _sensor.getSyncData().getDeviceKey().getChipId();
        final String deviceToken = new BigInteger(160, random).toString(32);
        final String restoreToken = new BigInteger(80, random).toString(32);
        final DeviceSnapshot deviceSnapshot = new DeviceSnapshot(chipId, deviceToken, restoreToken);
        _certificateChecker.check(new CloudCertificateChecker.Listener() {
            @Override
            public void onChecked(final byte[] sha1Thumbprint) {
                _cloudDeviceService.registerDevice(_sensor.getUserId(), deviceSnapshot, new CloudActionCallback<Void>() {
                    @Override
                    public void success(Void data) {
                        String md5 = _firmware.getMd5();
                        String host = String.format("http://%s:8890/", _wifiHelper.getIP());
                        OtaHashSyncUpdate hashSyncUpdate = new OtaHashSyncUpdate(md5);
                        OtaHostSyncUpdate hostSyncUpdate = new OtaHostSyncUpdate(host);
                        RestoreTokenSyncUpdate restoreTokenSyncUpdate = new RestoreTokenSyncUpdate(restoreToken);
                        CloudUsernameSyncUpdate usernameSyncUpdate = new CloudUsernameSyncUpdate(_sensor.getUserId());
                        CloudPasswordSyncUpdate passwordSyncUpdate = new CloudPasswordSyncUpdate(deviceToken);
                        String fingerprint = ByteUtils.bytesToHex(sha1Thumbprint).toLowerCase();
                        CloudFingerprintSyncUpdate cloudFingerprintSyncUpdate = new CloudFingerprintSyncUpdate(fingerprint);
                        _sensor.getUpdates().add(hashSyncUpdate);
                        _sensor.getUpdates().add(hostSyncUpdate);
                        _sensor.getUpdates().add(restoreTokenSyncUpdate);
                        _sensor.getUpdates().add(usernameSyncUpdate);
                        _sensor.getUpdates().add(passwordSyncUpdate);
                        _sensor.getUpdates().add(cloudFingerprintSyncUpdate);
                        _sensor.requestFullSync();
                        _sensor.getUpdates().clear();
                        _cloudDeviceService.registerForDeviceSyncEvents(_sensor.getUserId(), deviceSnapshot);
                        setOtaPhase(OtaPhase.WaitingForDevice);
                    }

                    @Override
                    public void failure(CloudException exception) {
                        setErrorMessage(exception.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onError(IOException e) {
                setErrorMessage(e.getLocalizedMessage());
            }
        });

    }

    private void startOtaServer() {
        if (!_otaServer.isRunning()) {
            try {
                setOtaPhase(OtaPhase.ServerStarting);
                _otaServer.setListener(this);
                startOtaServerInternal();
            } catch (IllegalThreadStateException ise) {
                _otaServer.stop();
                try {
                    startOtaServerInternal();
                } catch (IOException e) {
                    setOtaPhase(OtaPhase.ServerError);
                }
            } catch (IOException e) {
                setOtaPhase(OtaPhase.ServerError);
            }
        }
    }

    private void startOtaServerInternal() throws IOException {
        _otaServer.start(_sensor.getSyncData(), _firmware);
        started();
    }

    @Override
    public FragmentOtaBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentOtaBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ota, container, false);
        binding.setVm(this);
        return binding;
    }

    @Override
    public void onAfterSync(InetAddress from, DeviceSyncData syncData, long timestamp, String token) {
        if (getOtaPhase() == OtaPhase.InProgress && _sensor.getSyncData().getDeviceKey().getDeviceId().equals(syncData.getDeviceKey().getDeviceId())) {
            int newVersionMajor = syncData.getSystemInfo().getVersionMajor();
            int newVersionMinor = syncData.getSystemInfo().getVersionMinor();
            Variant newVariant = syncData.getSystemInfo().getVariant();
            int prevVersionMajor = _sensor.getSyncData().getSystemInfo().getVersionMajor();
            int prevVersionMinor = _sensor.getSyncData().getSystemInfo().getVersionMinor();
            boolean differentMajorVersion = newVersionMajor != prevVersionMajor;
            boolean differentMinorVersion = newVersionMinor != prevVersionMinor;
            boolean differentVariant = newVariant != _previousVariant;
            if (differentMajorVersion || differentMinorVersion || differentVariant) {
                setOtaPhase(OtaPhase.Finished);
                if (newVariant == Variant.WiFi && _previousVariant.isCloud()) {
                    removeDeviceFromFirebaseAndCallSuccess(syncData);
                } else {
                    processSuccess(syncData);
                }
            } else if (syncData.getOta().isError()) {
                setOtaPhase(OtaPhase.Finished);
                processFailure(syncData);
            }
        }
    }

    @Override
    public void onUnsupportedBoard(InetAddress from, String deviceId) {
        //we shouldn't start OTA when board is unsupported
    }

    @Override
    public void onSecuredDeviceFound(InetAddress from) {

    }

    @Override
    public void onCloudDeviceFound(InetAddress from, Variant variant, BoardId boardId, Byte[] restoreTokenPart) {
    }

    private void processTimeElapsed() {
        if (!_restoreTokenUsed && _sensor.getRestoreToken() != null) {
            _restoreTokenUsed = true;
            _serverStartedAt = Calendar.getInstance().getTimeInMillis();
            tryWithRestoreToken();
        } else {
            String title = _context.getString(R.string.ota_failed);
            String status = _context.getString(R.string.ota_elapsed);
            ResultViewModel result = _factory.createResultViewModel(title, status, false);
            _navigationService.goBackTo(MainViewModel.class);
            _navigationService.showViewModel(result);
        }
    }

    private void tryWithRestoreToken() {
        String md5 = _firmware.getMd5();
        _sensor.requestOtaRestore(md5);
    }

    private void processFailure(final DeviceSyncData syncData) {
        _threadRunner.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String title = _context.getString(R.string.ota_failed);
                String status = syncData.getOta().getErrorCode();
                ResultViewModel result = _factory.createResultViewModel(title, status, false);
                _navigationService.goBackTo(MainViewModel.class);
                _navigationService.showViewModel(result);
            }
        });
    }

    private void processSuccess(final DeviceSyncData syncData) {
        _threadRunner.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String title = _context.getString(R.string.ota_done);
                String status = _context.getString(R.string.ota_device_updated, _sensor.getSyncData().getDeviceKey().getDeviceId(),
                        syncData.getSystemInfo().getVersionMajor(), syncData.getSystemInfo().getVersionMinor());
                ResultViewModel result = _factory.createResultViewModel(title, status, true);
                _navigationService.goBackTo(MainViewModel.class);
                _navigationService.showViewModel(result);
            }
        });
    }

    private void removeDeviceFromFirebaseAndCallSuccess(final DeviceSyncData syncData) {
        _cloudDeviceService.removeDevice(_sensor.getUserId(), _sensor.getSyncData().getDeviceKey(), new CloudActionCallback<Void>() {
            @Override
            public void success(Void data) {
                processSuccess(syncData);
            }

            @Override
            public void failure(CloudException exception) {
                setErrorMessage(exception.getLocalizedMessage());
            }
        });
    }

    @Override
    public void downloadStarted() {
        setOtaPhase(OtaPhase.InProgress);
        _serverStartedAt = Calendar.getInstance().getTimeInMillis();
    }
}