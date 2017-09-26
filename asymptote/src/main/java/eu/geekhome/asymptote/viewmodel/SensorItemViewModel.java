package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.net.InetAddress;
import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.ListitemSensorBinding;
import eu.geekhome.asymptote.model.BoardRole;
import eu.geekhome.asymptote.model.ColorSyncUpdate;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.NameSyncUpdate;
import eu.geekhome.asymptote.model.PWMSyncUpdate;
import eu.geekhome.asymptote.model.ParamSyncUpdate;
import eu.geekhome.asymptote.model.RGBSyncUpdate;
import eu.geekhome.asymptote.model.RelayImpulseSyncUpdate;
import eu.geekhome.asymptote.model.RelaySyncUpdate;
import eu.geekhome.asymptote.model.RoleSyncUpdate;
import eu.geekhome.asymptote.model.StateSyncUpdate;
import eu.geekhome.asymptote.model.SyncUpdate;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FirmwareRepository;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.SyncSource;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.UdpService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class SensorItemViewModel extends BaseObservable implements LayoutHolder {
    private String _restoreToken;
    private boolean _syncDelayed;
    private final SensorLifecycleListener _sensorLifecycleListener;
    private InetAddress _address;
    private DeviceSyncData _syncData;
    private boolean _active;
    private boolean _blocked;
    private boolean _newFirmwareAvailable;
    private ArrayList<SyncUpdate> _updates = new ArrayList<>();
    private long _lastSeenOnWiFi;
    private long _lastSeenOnCloud;
    private boolean _hasWiFiSignal;
    private boolean _hasCloudSignal;
    private boolean _isAlive;
    private ObservableArrayList<LayoutHolder> _controls;
    private BoardRole _lastRole;
    private final String _userId;
    private String _token;

    public interface SensorLifecycleListener {
        void recreated(SensorItemViewModel sender);
        void locking(SensorItemViewModel sender);

    }

    private final FirmwareRepository _firmwareRepository;
    private final WiFiHelper _wifiHelper;
    private final UdpService _udpService;
    private final Context _context;
    private final ThreadRunner _threadRunner;
    private final GeneralDialogService _generalDialogService;
    private final NavigationService _navigationService;
    private final SyncManager _syncManager;
    private final CloudDeviceService _cloudDeviceService;
    private final EmergencyManager _emergencyManager;
    private final ControlsCreator _controlsCreator;
    private final MainViewModelsFactory _factory;

    public SensorItemViewModel(Context context, MainViewModelsFactory factory, FirmwareRepository firmwareRepository,
                               WiFiHelper wifiHelper, UdpService udpService, ThreadRunner threadRunner,
                               GeneralDialogService generalDialogService, NavigationService navigationService,
                               SyncManager syncManager, CloudDeviceService cloudDeviceService,
                               EmergencyManager emergencyManager, ControlsCreator controlsCreator,
                               SensorLifecycleListener sensorLifecycleListener,
                               InetAddress address, DeviceSyncData syncData,
                               long timestamp, String userId, String token) {
        _context = context;
        _factory = factory;
        _firmwareRepository = firmwareRepository;
        _wifiHelper = wifiHelper;
        _udpService = udpService;
        _threadRunner = threadRunner;
        _generalDialogService = generalDialogService;
        _navigationService = navigationService;
        _syncManager = syncManager;
        _cloudDeviceService = cloudDeviceService;
        _emergencyManager = emergencyManager;
        _controlsCreator = controlsCreator;
        _sensorLifecycleListener = sensorLifecycleListener;
        _syncData = syncData;
        _token = token;
        _lastRole = syncData.getRole();
        _controls = _controlsCreator.createControls(syncData, this);
        _address = address;
        _userId = userId;
        setSyncData(syncData, timestamp);
    }

    public String getUserId() {
        return _userId;
    }

    @Bindable
    public String getRestoreToken() {
        return _restoreToken;
    }

    public void setRestoreToken(String restoreToken) {
        _restoreToken = restoreToken;
        notifyPropertyChanged(BR.restoreToken);
    }

    @Bindable
    public boolean isEmergency() {
        return _emergencyManager.isEmergency();
    }

    @Bindable
    public boolean isHasWiFiSignal() {
        return _hasWiFiSignal;
    }

    private void setHasWiFiSignal(boolean hasWiFiSignal) {
        _hasWiFiSignal = hasWiFiSignal;
        notifyPropertyChanged(BR.hasWiFiSignal);
    }

    @Bindable
    public boolean isHasCloudSignal() {
        return _hasCloudSignal;
    }

    private void setHasCloudSignal(boolean hasCloudSignal) {
        _hasCloudSignal = hasCloudSignal;
        notifyPropertyChanged(BR.hasCloudSignal);
    }

    @Bindable
    public boolean isAlive() {
        return _isAlive;
    }

    @Bindable
    public boolean isBlocked() {
        return _blocked;
    }

    void setBlocked(boolean blocked) {
        _blocked = blocked;
        notifyPropertyChanged(BR.blocked);
    }

    @Bindable
    public boolean isNewFirmwareAvailable() {
        return _newFirmwareAvailable;
    }

    private void setNewFirmwareAvailable(boolean value) {
        _newFirmwareAvailable = value;
        notifyPropertyChanged(BR.newFirmwareAvailable);
    }

    @Bindable
    public boolean isActive() {
        return _active;
    }

    private void setActive(boolean active) {
        _active = active;
        notifyPropertyChanged(BR.active);
    }

    private void setAlive(boolean value) {
        _isAlive = value;
        notifyPropertyChanged(BR.alive);
    }

    private void calculateActive() {
        for (boolean relayState : _syncData.getRelayStates()) {
            if (relayState) {
                setActive(true);
                return;
            }
        }

        for (int pwmDuty : _syncData.getPwmDuties()) {
            if (pwmDuty > 0) {
                setActive(true);
                return;
            }
        }

        if (getSyncData().getRole() == BoardRole.GATE && !getSyncData().getState().equals("closed")) {
            setActive(true);
            return;
        }

        setActive(false);
    }


    @Bindable
    public DeviceSyncData getSyncData() {
        return _syncData;
    }

    void setSyncData(DeviceSyncData data, long timestamp) {
        _syncDelayed = false;
        if (isHasWiFiSignal() && data.getSystemInfo().getVariant() == Variant.Firebase) {
            updateLastSeen(data.getSource(), timestamp);
            _cloudDeviceService.removeDevice(getUserId(), data.getDeviceKey(), null);
            return;
        }

        if (data.getRole() != _lastRole) {
            _lastRole = data.getRole();
            _controls = _controlsCreator.createControls(data, this);
            recreateSensor();
        }

        updateLastSeen(data.getSource(), timestamp);

        //merge
        //TODO PWMIMPULSESYNCUPDATE
        ArrayList<SyncUpdate> toDelete = new ArrayList<>();
        for (SyncUpdate update : getUpdates()) {
            if (update instanceof RelaySyncUpdate) {
                RelaySyncUpdate relayUpdate = (RelaySyncUpdate) update;
                int channel = relayUpdate.getValue().getChannel();
                boolean reportedState = data.getRelayStates()[channel];
                boolean updateState = relayUpdate.getValue().getState();
                if (reportedState == updateState) {
                    toDelete.add(update);
                } else {
                    data.getRelayStates()[channel] = updateState;
                }
            } else if (update instanceof PWMSyncUpdate) {
                PWMSyncUpdate pwmUpdate = (PWMSyncUpdate) update;
                int channel = pwmUpdate.getValue().getChannel();
                int reportedDuty = data.getPwmDuties()[channel];
                int updateDuty = pwmUpdate.getValue().getDuty();
                if (reportedDuty == updateDuty) {
                    toDelete.add(update);
                } else {
                    data.getPwmDuties()[channel] = updateDuty;
                }
            } else if (update instanceof RGBSyncUpdate) {
                RGBSyncUpdate pwmUpdate = (RGBSyncUpdate) update;
                int redChannel = pwmUpdate.getValue().getRed().getChannel();
                int greenChannel = pwmUpdate.getValue().getGreen().getChannel();
                int blueChannel = pwmUpdate.getValue().getBlue().getChannel();
                int reportedRedDuty = data.getPwmDuties()[redChannel];
                int reportedGreenDuty = data.getPwmDuties()[greenChannel];
                int reportedBlueDuty = data.getPwmDuties()[blueChannel];
                int updateRedDuty = pwmUpdate.getValue().getRed().getDuty();
                int updateGreenDuty = pwmUpdate.getValue().getGreen().getDuty();
                int updateBlueDuty = pwmUpdate.getValue().getBlue().getDuty();
                if (reportedRedDuty == updateRedDuty &&
                    reportedGreenDuty == updateGreenDuty &&
                    reportedBlueDuty == updateBlueDuty) {
                    toDelete.add(update);
                } else {
                    data.getPwmDuties()[redChannel] = updateRedDuty;
                    data.getPwmDuties()[greenChannel] = updateGreenDuty;
                    data.getPwmDuties()[blueChannel] = updateBlueDuty;
                }
            } else if (update instanceof ParamSyncUpdate) {
                ParamSyncUpdate paramUpdate = (ParamSyncUpdate) update;
                int index = paramUpdate.getValue().getIndex();
                long reportedValue = data.getParams()[index];
                long updateValue = paramUpdate.getValue().getValue();
                if (reportedValue == updateValue) {
                    toDelete.add(update);
                } else {
                    data.getParams()[index] = updateValue;
                }
            } else if (update instanceof RoleSyncUpdate) {
                RoleSyncUpdate roleUpdate = (RoleSyncUpdate) update;
                BoardRole reportedRole = data.getRole();
                BoardRole updateRole = roleUpdate.getValue();
                if (reportedRole == updateRole) {
                    toDelete.add(update);
                } else {
                    data.setRole(updateRole);
                }
            } else if (update instanceof NameSyncUpdate) {
                NameSyncUpdate nameUpdate = (NameSyncUpdate) update;
                String reportedName = data.getName();
                String updateName = nameUpdate.getValue();
                if (reportedName.equals(updateName)) {
                    toDelete.add(update);
                } else {
                    data.setName(updateName);
                }
            } else if (update instanceof ColorSyncUpdate) {
                ColorSyncUpdate colorUpdate = (ColorSyncUpdate) update;
                int reportedColor = data.getColor();
                int updateColor = colorUpdate.getValue();
                if (reportedColor == updateColor) {
                    toDelete.add(update);
                } else {
                    data.setColor(updateColor);
                }
            } else if (update instanceof RelayImpulseSyncUpdate) {
                RelayImpulseSyncUpdate relayImpulseUpdate = (RelayImpulseSyncUpdate) update;
                int channel = relayImpulseUpdate.getValue().getChannel();
                long reportedImpulse = data.getRelayImpulses()[channel];
                long updateImpulse = relayImpulseUpdate.getValue().getImpulse();
                if (reportedImpulse == updateImpulse) {
                    toDelete.add(update);
                } else {
                    data.getRelayImpulses()[channel] = updateImpulse;
                }
            } else if (update instanceof StateSyncUpdate) {
                StateSyncUpdate stateSyncUpdate = (StateSyncUpdate) update;
                String reportedState = data.getState();
                String updateState = stateSyncUpdate.getValue();
                boolean stateChanged = updateState.startsWith("*") && !reportedState.startsWith("*");
                if (stateChanged) {
                    toDelete.add(update);
                } else {
                    data.setState(updateState);
                }
            } else {
                //delete the rest of the updates
                toDelete.add(update);
            }
        }

        getUpdates().removeAll(toDelete);
        _syncData = data;

        setNewFirmwareAvailable(!_firmwareRepository.isLatest(data.getSystemInfo()));
        calculateActive();
        notifyPropertyChanged(BR.syncData);
        syncControls(data);
        setBlocked(hasBlockedUpdates());
    }

    private boolean hasBlockedUpdates() {
        for (SyncUpdate update : getUpdates()) {
            if (update.isBlocking()) {
                return true;
            }
        }

        return false;
    }

    private void syncControls(DeviceSyncData data) {
        for (LayoutHolder holder : _controls) {
            if (holder instanceof ControlItemViewModelBase) {
                ((ControlItemViewModelBase) holder).sync(data);
            }
        }
    }

    private void updateLastSeen(SyncSource source, long timestamp) {
        if (source == SyncSource.LAN) {
            _lastSeenOnWiFi = timestamp;
            setHasWiFiSignal(true);
        }

        if (source == SyncSource.CLOUD) {
            _lastSeenOnCloud = timestamp;
            setHasCloudSignal(true);
        }

        setAlive(true);
    }

    void checkAlive(long now) {
        setHasWiFiSignal(now - _lastSeenOnWiFi < 45000);
        setHasCloudSignal(now - _lastSeenOnCloud < 125000);
        if (!isHasWiFiSignal() && !isHasCloudSignal()) {
            setAlive(false);
            setBlocked(false);
        }
    }

    public void edit() {
        EditSensorViewModel renameModel = _factory.createEditSensorViewModel(this);
        _navigationService.showViewModel(renameModel, new ShowBackButtonInToolbarViewParam());
    }

    public void triggers() {
        EditTriggersViewModel triggersModel = _factory.createEditTriggersViewModel(this);
        _navigationService.showViewModel(triggersModel, new ShowBackButtonInToolbarViewParam());
    }

    public void changeFirmware() {
        if (isEmergency()) {
            _generalDialogService.showOKDialog(R.string.sign_in_to_install, null);
        } else {
            if (!getSyncData().isLocked() && getSyncData().getSystemInfo().getVariant().isWifi()) {
                _generalDialogService.showOKDialog(R.string.device_locked_not_upgradable, null);
            } else {
                ChangeFirmwareViewModel changeFirmwareModel = _factory.createChangeFirmwareViewModel(this);
                _navigationService.showViewModel(changeFirmwareModel, new ShowBackButtonInToolbarViewParam());
            }
        }
    }

    public void lock() {
        if (_sensorLifecycleListener != null) {
            _sensorLifecycleListener.locking(this);
        }
    }

    public void unlock() {
        DeviceLockedViewModel uvm = _factory.createDeviceLockedViewModel();
        _navigationService.showOverlayViewModel(uvm);
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_sensor;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
        ListitemSensorBinding sensorBinding = (ListitemSensorBinding) binding;
        Animation rotateAnimation = AnimationUtils.loadAnimation(_context, R.anim.rotate_around_center_point_linear);
        sensorBinding.gearImage.startAnimation(rotateAnimation);
    }

    public InetAddress getAddress() {
        return _address;
    }

    public void setAddress(InetAddress address) {
        _address = address;
    }

    public ArrayList<SyncUpdate> getUpdates() {
        return _updates;
    }

    protected void onRequestFullSync() {
        setBlocked(hasBlockedUpdates());

        boolean syncWiFi = _wifiHelper.isWifiConnected() &&
                isHasWiFiSignal() &&
                getSyncData().getSystemInfo().getVariant().isWifi();

        if (syncWiFi) {
            _syncManager.pushUpdates(getSyncData(), getUpdates(), getAddress(), new SyncManager.SyncCallback() {
                @Override
                public void success() {
                    getUpdates().clear();
                }

                @Override
                public void failure(Exception exception) {
                    _syncManager.pushUpdates(getSyncData(), getUpdates(), getAddress(), new SyncManager.SyncCallback() {
                        @Override
                        public void success() {
                            getUpdates().clear();
                        }

                        @Override
                        public void failure(Exception exception) {
                            setBlocked(true);
                            //waiting for successful sync to unblock the state
                        }
                    });
                }
            });

            return;
        }

        if (getSyncData().getSystemInfo().getVariant().isCloud() && getToken() != null) {
            ArrayList<SyncUpdate> updatesCopy = new ArrayList<>(getUpdates());
            _cloudDeviceService.pushUpdates(_userId, getToken(), getSyncData(), updatesCopy, new CloudActionCallback<Void>() {
                @Override
                public void success(Void data) {
                    _udpService.sync(getAddress());
                }

                @Override
                public void failure(CloudException exception) {
                    //waiting for successful sync to unblock the state
                }
            });
        }

    }

    private void recreateSensor() {
        if (_sensorLifecycleListener != null) {
            _sensorLifecycleListener.recreated(this);
        }
    }

    void requestSyncDelayed() {
        _syncDelayed = true;
        _threadRunner.removeCallbacks(requestSyncRunnable);
        _threadRunner.postDelayed(requestSyncRunnable, 1000);
    }

    private Runnable requestSyncRunnable = new Runnable() {
        @Override
        public void run() {
            onRequestFullSync();
        }
    };

    void requestOtaRestore(String md5) {
        _udpService.requestOtaRestore(getAddress(), getRestoreToken(), md5);
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getControls() {
        return _controls;
    }

    public void setControls(ObservableArrayList<LayoutHolder> controls) {
        _controls = controls;
        notifyPropertyChanged(BR.controls);
    }

    boolean isSyncDelayed() {
        return _syncDelayed;
    }

    @Bindable
    public String getToken() {
        return _token;
    }

    public void setToken(String token) {
        _token = token;
        notifyPropertyChanged(BR.token);
    }
}
