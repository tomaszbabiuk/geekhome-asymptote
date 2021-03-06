package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.databinding.ObservableArrayList;

import java.net.InetAddress;

import javax.inject.Inject;

import eu.geekhome.asymptote.MainActivity;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.AutomationDateTimeHumidity;
import eu.geekhome.asymptote.model.AutomationDateTimeImpulse;
import eu.geekhome.asymptote.model.AutomationDateTimePWM;
import eu.geekhome.asymptote.model.AutomationDateTimeRGB;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.model.AutomationDateTimeTemperature;
import eu.geekhome.asymptote.model.AutomationSchedulerHumidity;
import eu.geekhome.asymptote.model.AutomationSchedulerImpulse;
import eu.geekhome.asymptote.model.AutomationSchedulerPWM;
import eu.geekhome.asymptote.model.AutomationSchedulerRGB;
import eu.geekhome.asymptote.model.AutomationSchedulerRelay;
import eu.geekhome.asymptote.model.AutomationSchedulerTemperature;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.model.WiFiParameters;
import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.services.CloudCertificateChecker;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.ColorDialogService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FirmwareRepository;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.LogoutService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.OtaServer;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.UdpService;
import eu.geekhome.asymptote.services.UserMessageAcknowledgeService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.WiFiParamsResolver;
import eu.geekhome.asymptote.viewmodel.CMSViewModel;
import eu.geekhome.asymptote.viewmodel.ChangeEmailViewModel;
import eu.geekhome.asymptote.viewmodel.ChangeFirmwareViewModel;
import eu.geekhome.asymptote.viewmodel.ChangePasswordViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationDateTimeImpulseViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationDateTimePWMViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationDateTimeRGBViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationSchedulerImpulseViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationSchedulerPWMViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationSchedulerRGBViewModel;
import eu.geekhome.asymptote.viewmodel.EditImpulseValueViewModel;
import eu.geekhome.asymptote.viewmodel.EditPWMValueViewModel;
import eu.geekhome.asymptote.viewmodel.EditRGBValueViewModel;
import eu.geekhome.asymptote.viewmodel.ManageViewModel;
import eu.geekhome.asymptote.viewmodel.ChooseAutomationViewModel;
import eu.geekhome.asymptote.viewmodel.ControlsCreator;
import eu.geekhome.asymptote.viewmodel.EditAutomationDateTimeHumidityViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationDateTimeRelayViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationDateTimeTemperatureViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationSchedulerHumidityViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationSchedulerRelayViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationSchedulerTemperatureViewModel;
import eu.geekhome.asymptote.viewmodel.EditAutomationViewModel;
import eu.geekhome.asymptote.viewmodel.EditDateTimeViewModel;
import eu.geekhome.asymptote.viewmodel.EditHumidityValueViewModel;
import eu.geekhome.asymptote.viewmodel.EditRelayValueViewModel;
import eu.geekhome.asymptote.viewmodel.EditSchedulerViewModel;
import eu.geekhome.asymptote.viewmodel.EditSensorViewModel;
import eu.geekhome.asymptote.viewmodel.EditTemperatureValueViewModel;
import eu.geekhome.asymptote.viewmodel.FirmwareItemViewModel;
import eu.geekhome.asymptote.viewmodel.HelpActionBarViewModel;
import eu.geekhome.asymptote.viewmodel.HygrostatRoleDetailsViewModel;
import eu.geekhome.asymptote.viewmodel.LightsSwitchTraditionalRoleDetailsViewModel;
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
import eu.geekhome.asymptote.viewmodel.SwitchModeItemViewModel;
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
    private LogoutService _logoutService;
    private ColorDialogService _colorDialogService;

    @Inject
    MainViewModelsFactory(MainActivity activity, Context context, CloudUserService cloudUserService, CloudDeviceService cloudDeviceService,
                          ToastService toastService, NavigationService navigationService,
                          WiFiHelper wifiHelper, AddressesPersistenceService addressesPersistenceService,
                          WiFiParamsResolver wiFiParamsResolver, OtaServer otaServer,
                          CloudCertificateChecker cloudCertificateChecker, SyncManager syncManager,
                          FirmwareRepository firmwareRepository, UdpService udpService,
                          ThreadRunner threadRunner, GeneralDialogService generalDialogService,
                          EmergencyManager emergencyManager, ControlsCreator controlsCreator,
                          UserMessageAcknowledgeService userMessageAcknowledgeService,
                          LogoutService logoutService, ColorDialogService colorDialogService) {
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
        _logoutService = logoutService;
        _colorDialogService = colorDialogService;
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

    public ResultViewModel createResultViewModel(String title, String status, boolean success, boolean enableRetry) {
        return new ResultViewModel(this, _wifiHelper, _navigationService, title, status , success, enableRetry);
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
                _syncManager, _wifiHelper, _cloudDeviceService, _cloudCertificateChecker, _threadRunner,
                sensor, firmware);
    }

    public ProfileViewModel createProfileViewModel() {
        return new ProfileViewModel(this, _navigationService, _cloudUserService, _logoutService);
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
        return new MainActionBarViewModel(this, _navigationService, _generalDialogService, _emergencyManager,
                 _logoutService);
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

    public LockViewModel createLockViewModel(SensorItemViewModel sensor) {
        return new LockViewModel(this, _navigationService, _syncManager, _toastService, _cloudUserService,
                _threadRunner, _emergencyManager, sensor);
    }

    public ChangeFirmwareViewModel createChangeFirmwareViewModel(SensorItemViewModel sensor) {
        return new ChangeFirmwareViewModel(_context, this, _firmwareRepository, sensor);
    }

    public EditSensorViewModel createEditSensorViewModel(SensorItemViewModel sensor) {
        return new EditSensorViewModel(_context, this, _navigationService, sensor);
    }

    public FirmwareItemViewModel createFirmwareItemViewModel(Firmware firmware, SensorItemViewModel sensor,
                                                             String name, String description,
                                                             FirmwareItemViewModel.Context firmwareContext) {
        return new FirmwareItemViewModel(this, _navigationService, firmware, sensor, name, description, firmwareContext);
    }

    public ThermostatRoleDetailsViewModel createThermostatRoleDetailsViewModel(
            EditSensorViewModel editSensorViewModel, SensorItemViewModel sensor, String title, String instruction, boolean reset) {
        return new ThermostatRoleDetailsViewModel(this, _navigationService, editSensorViewModel, sensor,
                title, instruction, reset);
    }

    public HygrostatRoleDetailsViewModel createHygrostatRoleDetailsViewModel(
            EditSensorViewModel editSensorViewModel, SensorItemViewModel sensor, String title, String instruction, boolean reset) {
        return new HygrostatRoleDetailsViewModel(this, _navigationService, editSensorViewModel, sensor,
                title, instruction, reset);
    }

    public MainsAdvancedRoleDetailsViewModel createMainsAdvancedViewModel(
            EditSensorViewModel editSensorViewModel, SensorItemViewModel sensor) {
        return new MainsAdvancedRoleDetailsViewModel(this, _navigationService, editSensorViewModel, sensor);
    }

    public RelayWorkingModeItemViewModel createRelayWorkingModeItem(long impulse, int channel) {
        return new RelayWorkingModeItemViewModel(_generalDialogService, impulse, channel);
    }

    public SwitchModeItemViewModel createSwitchModeItemViewModel(long mode, int channel) {
        return new SwitchModeItemViewModel(channel, mode);
    }

    public LightsSwitchTraditionalRoleDetailsViewModel createLightsSwitchTraditionalRoleDetailsViewModel(
            EditSensorViewModel editSensorViewModel, SensorItemViewModel sensor) {
        return new LightsSwitchTraditionalRoleDetailsViewModel(this, _navigationService, editSensorViewModel, sensor);
    }

    public EditAutomationViewModel createEditAutomationViewModel(SensorItemViewModel sensor) {
        return new EditAutomationViewModel(this, _context, _navigationService, _syncManager, _threadRunner,
                _cloudDeviceService, sensor);
    }

    public ChooseAutomationViewModel createChooseTriggerViewModel(AutomationAddedListener listener,
                                                                  int index, SensorItemViewModel sensor) {
        return new ChooseAutomationViewModel(this, _navigationService, listener, index, sensor);
    }

    public EditAutomationDateTimeRelayViewModel createEditAutomationDateTimeRelayViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationDateTimeRelayViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationDateTimeRelayViewModel createEditAutomationDateTimeRelayViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationDateTimeRelay automation) {
        return new EditAutomationDateTimeRelayViewModel(_context, this, _navigationService, listener,
                sensor, automation);
    }

    public EditAutomationDateTimeImpulseViewModel createEditAutomationDateTimeImpulseViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationDateTimeImpulseViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationDateTimeImpulseViewModel createEditAutomationDateTimeImpulseViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationDateTimeImpulse automation) {
        return new EditAutomationDateTimeImpulseViewModel(_context, this, _navigationService, listener,
                sensor, automation);
    }

    public EditAutomationDateTimePWMViewModel createEditAutomationDateTimePWMViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationDateTimePWMViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationDateTimePWMViewModel createEditAutomationDateTimePWMViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationDateTimePWM automation) {
        return new EditAutomationDateTimePWMViewModel(_context, this, _navigationService, listener,
                sensor, automation);
    }

    public EditAutomationDateTimeTemperatureViewModel createEditAutomationDateTimeTemperatureViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationDateTimeTemperatureViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationDateTimeTemperatureViewModel createEditAutomationDateTimeTemperatureViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationDateTimeTemperature automation) {
        return new EditAutomationDateTimeTemperatureViewModel(_context, this, _navigationService, listener,
                sensor, automation);
    }

    public EditAutomationDateTimeHumidityViewModel createEditAutomationDateTimeHumidityViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationDateTimeHumidityViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationDateTimeHumidityViewModel createEditAutomationDateTimeHumidityViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationDateTimeHumidity automation) {
        return new EditAutomationDateTimeHumidityViewModel(_context, this, _navigationService, listener,
                sensor, automation);
    }

    public EditAutomationDateTimeRGBViewModel createEditAutomationDateTimeRGBViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationDateTimeRGBViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationDateTimeRGBViewModel createEditAutomationDateTimeRGBViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationDateTimeRGB automation) {
        return new EditAutomationDateTimeRGBViewModel(_context, this, _navigationService, listener,
                sensor, automation);
    }

    public EditAutomationSchedulerRelayViewModel createEditAutomationSchedulerRelayViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationSchedulerRelayViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationSchedulerRelayViewModel createEditAutomationSchedulerRelayViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationSchedulerRelay automation) {
        return new EditAutomationSchedulerRelayViewModel(_context, this, _navigationService, listener,
                sensor, automation);

    }

    public EditAutomationSchedulerImpulseViewModel createEditAutomationSchedulerImpulseViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationSchedulerImpulseViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationSchedulerImpulseViewModel createEditAutomationSchedulerImpulseViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationSchedulerImpulse automation) {
        return new EditAutomationSchedulerImpulseViewModel(_context, this, _navigationService, listener,
                sensor, automation);

    }

    public EditAutomationSchedulerPWMViewModel createEditAutomationSchedulerPWMViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationSchedulerPWMViewModel(_context, this, _navigationService, listener,
                sensor, index);
    }

    public EditAutomationSchedulerPWMViewModel createEditAutomationSchedulerPWMViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationSchedulerPWM automation) {
        return new EditAutomationSchedulerPWMViewModel(_context, this, _navigationService, listener,
                sensor, automation);
    }

    public EditAutomationSchedulerTemperatureViewModel createEditAutomationSchedulerTemperatureViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationSchedulerTemperatureViewModel(_context, this, _navigationService, listener, sensor, index);
    }

    public EditAutomationSchedulerTemperatureViewModel createEditAutomationSchedulerTemperatureViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationSchedulerTemperature automation) {
        return new EditAutomationSchedulerTemperatureViewModel(_context, this, _navigationService, listener, sensor, automation);
    }

    public EditAutomationSchedulerHumidityViewModel createEditAutomationSchedulerHumidityViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationSchedulerHumidityViewModel(_context, this, _navigationService, listener, sensor, index);
    }

    public EditAutomationSchedulerHumidityViewModel createEditAutomationSchedulerHumidityViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationSchedulerHumidity automation) {
        return new EditAutomationSchedulerHumidityViewModel(_context, this, _navigationService, listener, sensor, automation);
    }

    public EditAutomationSchedulerRGBViewModel createEditAutomationSchedulerRGBViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, int index) {
        return new EditAutomationSchedulerRGBViewModel(_context, this, _navigationService, listener, sensor, index);
    }

    public EditAutomationSchedulerRGBViewModel createEditAutomationSchedulerRGBViewModel(
            AutomationAddedListener listener, SensorItemViewModel sensor, AutomationSchedulerRGB automation) {
        return new EditAutomationSchedulerRGBViewModel(_context, this, _navigationService, listener, sensor, automation);
    }

    public EditRelayValueViewModel createEditRelayValueViewModel(SensorItemViewModel sensor) {
        return new EditRelayValueViewModel(_context, sensor);
    }

    public EditPWMValueViewModel createEditPWMValueViewModel(SensorItemViewModel sensor) {
        return new EditPWMValueViewModel(_context, sensor);
    }

    public EditImpulseValueViewModel createEditImpulseValueViewModel(SensorItemViewModel sensor) {
        return new EditImpulseValueViewModel(_context, sensor);
    }

    public EditTemperatureValueViewModel createEditTemperatureValueViewModel(SensorItemViewModel sensor) {
        return new EditTemperatureValueViewModel(sensor);
    }

    public EditHumidityValueViewModel createEditHumidityValueViewModel(SensorItemViewModel sensor) {
        return new EditHumidityValueViewModel(sensor);
    }

    public EditRGBValueViewModel createEditRGBValueViewModel(SensorItemViewModel sensor) {
        return new EditRGBValueViewModel(_context, sensor, _colorDialogService);
    }

    public EditDateTimeViewModel createEditDateTimeViewModel(SensorItemViewModel sensor) {
        return new EditDateTimeViewModel(_generalDialogService, sensor);
    }

    public ManageViewModel createChooseActionViewModel(SensorItemViewModel sensor) {
        return new ManageViewModel(this, _context, _navigationService, _emergencyManager, _syncManager,
                _toastService, _threadRunner,sensor);
    }

    public EditSchedulerViewModel createEditSchedulerViewModel(SensorItemViewModel sensor) {
        return new EditSchedulerViewModel(_generalDialogService, sensor);
    }
}
