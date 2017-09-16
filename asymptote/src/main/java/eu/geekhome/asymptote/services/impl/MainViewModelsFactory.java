package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.databinding.ObservableArrayList;

import java.net.InetAddress;

import javax.inject.Inject;

import eu.geekhome.asymptote.MainActivity;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.model.WiFiParameters;
import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.services.CloudCertificateChecker;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FirmwareRepository;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.OtaServer;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.services.UdpService;
import eu.geekhome.asymptote.services.UserMessageAcknowledgeService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.WiFiParamsResolver;
import eu.geekhome.asymptote.viewmodel.CMSViewModel;
import eu.geekhome.asymptote.viewmodel.ChangeEmailViewModel;
import eu.geekhome.asymptote.viewmodel.ChangeFirmwareViewModel;
import eu.geekhome.asymptote.viewmodel.ChangePasswordViewModel;
import eu.geekhome.asymptote.viewmodel.ControlsCreator;
import eu.geekhome.asymptote.viewmodel.DeviceLockedViewModel;
import eu.geekhome.asymptote.viewmodel.EditSensorViewModel;
import eu.geekhome.asymptote.viewmodel.FirmwareItemViewModel;
import eu.geekhome.asymptote.viewmodel.HelpActionBarViewModel;
import eu.geekhome.asymptote.viewmodel.HygrostatRoleDetailsViewModel;
import eu.geekhome.asymptote.viewmodel.LockViewModel;
import eu.geekhome.asymptote.viewmodel.MainActionBarViewModel;
import eu.geekhome.asymptote.viewmodel.MainViewModel;
import eu.geekhome.asymptote.viewmodel.MainsAdvancedRoleDetailsViewModel;
import eu.geekhome.asymptote.viewmodel.NoWiFiViewModel;
import eu.geekhome.asymptote.viewmodel.OtaViewModel;
import eu.geekhome.asymptote.viewmodel.ProfileViewModel;
import eu.geekhome.asymptote.viewmodel.RelayWorkingModeItemViewModel;
import eu.geekhome.asymptote.viewmodel.ResultViewModel;
import eu.geekhome.asymptote.viewmodel.SecuredDevicesFoundViewModel;
import eu.geekhome.asymptote.viewmodel.SensorItemViewModel;
import eu.geekhome.asymptote.viewmodel.SetEmergencyPasswordViewModel;
import eu.geekhome.asymptote.viewmodel.ThermostatRoleDetailsViewModel;
import eu.geekhome.asymptote.viewmodel.TouchConfigurationViewModel;
import eu.geekhome.asymptote.viewmodel.TouchPressViewModel;
import eu.geekhome.asymptote.viewmodel.TouchProgressViewModel;
import eu.geekhome.asymptote.viewmodel.TroubleshootingViewModel;
import eu.geekhome.asymptote.viewmodel.VerifyEmailLightViewModel;

public class MainViewModelsFactory {

    private final CloudUserService _cloudUserService;
    private final ToastService _toastService;
    private final NavigationService _navigationService;
    private final CloudDeviceService _cloudDeviceService;
    private final MainActivity _activity;
    private final Context _context;
    private final WiFiHelper _wifiHelper;
    private final AddressesPersistenceService _addressesPersistenceService;
    private final WiFiParamsResolver _wifiParamsResolver;
    private final OtaServer _otaServer;
    private CloudCertificateChecker _cloudCertificateChecker;
    private SyncManager _syncManager;
    private FirmwareRepository _firmwareRepository;
    private UdpService _udpService;
    private ThreadRunner _threadRunner;
    private GeneralDialogService _generalDialogService;
    private EmergencyManager _emergencyManager;
    private ControlsCreator _controlsCreator;
    private UserMessageAcknowledgeService _userMessageAcknowledgeService;

    @Inject
    MainViewModelsFactory(MainActivity activity, Context context, CloudUserService cloudUserService, CloudDeviceService cloudDeviceService,
                          ToastService toastService, NavigationService navigationService,
                          WiFiHelper wifiHelper, AddressesPersistenceService addressesPersistenceService,
                          WiFiParamsResolver wiFiParamsResolver, OtaServer otaServer,
                          CloudCertificateChecker cloudCertificateChecker, SyncManager syncManager,
                          FirmwareRepository firmwareRepository, UdpService udpService,
                          ThreadRunner threadRunner, GeneralDialogService generalDialogService,
                          EmergencyManager emergencyManager, ControlsCreator controlsCreator,
                          UserMessageAcknowledgeService userMessageAcknowledgeService) {
        _activity = activity;
        _context = context;
        _cloudUserService = cloudUserService;
        _cloudDeviceService = cloudDeviceService;
        _toastService = toastService;
        _navigationService = navigationService;
        _wifiHelper = wifiHelper;
        _addressesPersistenceService = addressesPersistenceService;
        _wifiParamsResolver = wiFiParamsResolver;
        _otaServer = otaServer;
        _cloudCertificateChecker = cloudCertificateChecker;
        _syncManager = syncManager;
        _firmwareRepository = firmwareRepository;
        _udpService = udpService;
        _threadRunner = threadRunner;
        _generalDialogService = generalDialogService;
        _emergencyManager = emergencyManager;
        _controlsCreator = controlsCreator;
        _userMessageAcknowledgeService = userMessageAcknowledgeService;
    }

    public HelpActionBarViewModel createHelpActionBarModel() {
        return new HelpActionBarViewModel(_activity, _toastService, _cloudUserService);
    }

    public NoWiFiViewModel createNoWiFiViewModel(boolean cloudOnlyAllowed, String rationale) {
        return new NoWiFiViewModel(_wifiHelper, _navigationService, cloudOnlyAllowed, rationale);
    }

    public VerifyEmailLightViewModel createVerifyEmailLightViewModel(String email) {
        return new VerifyEmailLightViewModel(_context, this, _navigationService, _cloudUserService, email);
    }

    public ChangeEmailViewModel createChangeEmailViewModel() {
        return new ChangeEmailViewModel(_context, this, _wifiHelper, _navigationService, _cloudUserService);
    }

    public ChangePasswordViewModel createChangePasswordViewModel() {
        return new ChangePasswordViewModel(_context, this, _wifiHelper, _navigationService, _toastService, _cloudUserService);
    }

    public ResultViewModel createResultViewModel(String title, String status, boolean success) {
        return new ResultViewModel(this, _wifiHelper, _navigationService, title, status , success);
    }

    public TouchProgressViewModel createTouchProgressViewModel(WiFiParameters wiFiParameters) {
        return new TouchProgressViewModel(_context, this, _navigationService, _wifiHelper,
                _addressesPersistenceService, wiFiParameters);
    }

    public TouchPressViewModel createTouchPressViewModel(WiFiParameters wifiParameters) {
        return new TouchPressViewModel(_context, this, _wifiHelper, _navigationService, wifiParameters);
    }

    public TouchConfigurationViewModel createTouchConfigurationViewModel() {
        return new TouchConfigurationViewModel(_context, this, _wifiHelper, _wifiParamsResolver, _navigationService);
    }

    public OtaViewModel createOtaViewModel(SensorItemViewModel sensor, Firmware firmware) {
        return new OtaViewModel(_context, this, _otaServer, _navigationService,
                _syncManager, _wifiHelper, _cloudDeviceService, _cloudCertificateChecker, sensor, firmware);
    }

    public ProfileViewModel createProfileViewModel() {
        return new ProfileViewModel(this, _navigationService, _cloudUserService);
    }

    public TroubleshootingViewModel createTroubleshootingViewModel() {
        return new TroubleshootingViewModel(_context, this, _navigationService);
    }

    public SensorItemViewModel createSensorItemViewModel(SensorItemViewModel.SensorLifecycleListener sensorLifecycleListener,
                                                         InetAddress address, DeviceSyncData syncData,
                                                         long timestamp, String userId, String token) {
        return new SensorItemViewModel(_context, this, _firmwareRepository, _wifiHelper, _udpService,
                _threadRunner, _generalDialogService, _navigationService, _syncManager, _cloudDeviceService,
                _emergencyManager, _controlsCreator, sensorLifecycleListener, address, syncData,
                timestamp, userId, token);

    }

    public MainActionBarViewModel createMainActionBarViewModel() {
        return new MainActionBarViewModel(this, _navigationService, _generalDialogService, _emergencyManager);
    }

    public CMSViewModel createCmsViewModel(ObservableArrayList<LayoutHolder> sections) {
        return new CMSViewModel(this, _navigationService, sections);
    }

    public MainViewModel createMainViewModel(String userId, UserSnapshot userSnapshot) {
        return new MainViewModel(_context, this, _navigationService, _threadRunner, _syncManager,
                _wifiHelper, _emergencyManager, _cloudDeviceService, _cloudCertificateChecker,
                _userMessageAcknowledgeService, _generalDialogService, userId, userSnapshot);
    }

    public SetEmergencyPasswordViewModel createSetEmergencyPasswordViewModel(MainViewModel mainViewModel, String userId) {
        return new SetEmergencyPasswordViewModel(_navigationService, _emergencyManager, _cloudUserService,
                _toastService, mainViewModel, userId);
    }

    public SecuredDevicesFoundViewModel createSecuredDevicesFoundViewModel(MainViewModel mainViewModel, String userId) {
        return new SecuredDevicesFoundViewModel(this, _navigationService, mainViewModel, userId);
    }

    public LockViewModel createLockViewModel(MainViewModel mainViewModel, SensorItemViewModel sensor) {
        return new LockViewModel(_navigationService, _syncManager, _toastService, _cloudUserService,
                _context, _threadRunner, _emergencyManager, mainViewModel, sensor);
    }

    public DeviceLockedViewModel createDeviceLockedViewModel() {
        return new DeviceLockedViewModel(_navigationService);
    }

    public ChangeFirmwareViewModel createChangeFirmwareViewModel(SensorItemViewModel sensor) {
        return new ChangeFirmwareViewModel(_context, this, _firmwareRepository, _emergencyManager, sensor);
    }

    public EditSensorViewModel createEditSensorViewModel(SensorItemViewModel sensor) {
        return new EditSensorViewModel(_context, this, _navigationService, sensor);
    }

    public FirmwareItemViewModel createFirmwareItemViewModel(Firmware firmware, SensorItemViewModel sensor,
                                                             String name, String description,
                                                             FirmwareItemViewModel.Context firmwareContext, boolean available) {
        return new FirmwareItemViewModel(this, _navigationService, firmware, sensor, name, description, firmwareContext, available);
    }

    public ThermostatRoleDetailsViewModel createThermostatRoleDetailsViewModel(EditSensorViewModel editSensorViewModel,
                                                                               SensorItemViewModel sensor, String title, String instruction, boolean reset) {
        return new ThermostatRoleDetailsViewModel(this, _navigationService, editSensorViewModel, sensor,
                title, instruction, reset);
    }

    public HygrostatRoleDetailsViewModel createHygrostatRoleDetailsViewModel(EditSensorViewModel editSensorViewModel,
                                                                             SensorItemViewModel sensor, String title, String instruction, boolean reset) {
        return new HygrostatRoleDetailsViewModel(this, _navigationService, editSensorViewModel, sensor,
                title, instruction, reset);
    }

    public MainsAdvancedRoleDetailsViewModel createMainsAdvancedViewModel(EditSensorViewModel editSensorViewModel, SensorItemViewModel sensor) {
        return new MainsAdvancedRoleDetailsViewModel(this, _navigationService, editSensorViewModel, sensor);
    }

    public RelayWorkingModeItemViewModel createRelayWorkingModeItem(long impulse, int channel) {
        return new RelayWorkingModeItemViewModel(_generalDialogService, impulse, channel);
    }
}
