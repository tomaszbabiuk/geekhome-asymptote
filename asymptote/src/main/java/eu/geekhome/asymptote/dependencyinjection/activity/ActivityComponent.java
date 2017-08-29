package eu.geekhome.asymptote.dependencyinjection.activity;


import dagger.Subcomponent;
import eu.geekhome.asymptote.MainActivity;
import eu.geekhome.asymptote.SplashActivity;
import eu.geekhome.asymptote.viewmodel.CMSViewModel;
import eu.geekhome.asymptote.viewmodel.ChangeEmailViewModel;
import eu.geekhome.asymptote.viewmodel.ChangeFirmwareViewModel;
import eu.geekhome.asymptote.viewmodel.ChangePasswordViewModel;
import eu.geekhome.asymptote.viewmodel.ControlRGBItemViewModel;
import eu.geekhome.asymptote.viewmodel.EditSensorViewModel;
import eu.geekhome.asymptote.viewmodel.FirmwareItemViewModel;
import eu.geekhome.asymptote.viewmodel.HelpActionBarViewModel;
import eu.geekhome.asymptote.viewmodel.HygrostatRoleDetailsViewModel;
import eu.geekhome.asymptote.viewmodel.LockViewModel;
import eu.geekhome.asymptote.viewmodel.LoginViewModel;
import eu.geekhome.asymptote.viewmodel.MainActionBarViewModel;
import eu.geekhome.asymptote.viewmodel.MainViewModel;
import eu.geekhome.asymptote.viewmodel.MainsAdvancedRoleDetailsViewModel;
import eu.geekhome.asymptote.viewmodel.NoWiFiViewModel;
import eu.geekhome.asymptote.viewmodel.OtaViewModel;
import eu.geekhome.asymptote.viewmodel.PrivacyViewModel;
import eu.geekhome.asymptote.viewmodel.ProfileViewModel;
import eu.geekhome.asymptote.viewmodel.RelayWorkingModeItemViewModel;
import eu.geekhome.asymptote.viewmodel.ResetPasswordDarkViewModel;
import eu.geekhome.asymptote.viewmodel.ResultViewModel;
import eu.geekhome.asymptote.viewmodel.SecuredDevicesFoundViewModel;
import eu.geekhome.asymptote.viewmodel.SensorItemViewModel;
import eu.geekhome.asymptote.viewmodel.SetEmergencyPasswordViewModel;
import eu.geekhome.asymptote.viewmodel.SignInViewModel;
import eu.geekhome.asymptote.viewmodel.SignUpDarkViewModel;
import eu.geekhome.asymptote.viewmodel.ThermostatRoleDetailsViewModel;
import eu.geekhome.asymptote.viewmodel.TouchConfigurationViewModel;
import eu.geekhome.asymptote.viewmodel.TouchPressViewModel;
import eu.geekhome.asymptote.viewmodel.TouchProgressViewModel;
import eu.geekhome.asymptote.viewmodel.TroubleshootingViewModel;
import eu.geekhome.asymptote.viewmodel.VerifyEmailDarkViewModel;
import eu.geekhome.asymptote.viewmodel.VerifyEmailLightViewModel;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(SplashActivity splashActivity);
    void inject(PrivacyViewModel privacyViewModel);
    void inject(SignInViewModel signInViewModel);
    void inject(LoginViewModel loginViewModel);
    void inject(SignUpDarkViewModel signUpDarkViewModel);
    void inject(ResetPasswordDarkViewModel resetPasswordDarkViewModel);
    void inject(VerifyEmailDarkViewModel verifyEmailDarkViewModel);
    void inject(NoWiFiViewModel noWiFiViewModel);
    void inject(HelpActionBarViewModel helpActionBarViewModel);
    void inject(ChangeEmailViewModel changeEmailViewModel);
    void inject(ChangeFirmwareViewModel changeFirmwareViewModel);
    void inject(SensorItemViewModel sensorItemViewModel);
    void inject(ControlRGBItemViewModel controlRGBItemViewModel);
    void inject(EditSensorViewModel editSensorViewModel);
    void inject(OtaViewModel otaViewModel);
    void inject(ChangePasswordViewModel changePasswordViewModel);
    void inject(CMSViewModel cmsViewModel);
    void inject(FirmwareItemViewModel firmwareItemViewModel);
    void inject(HygrostatRoleDetailsViewModel hygrostatRoleDetailsViewModel);
    void inject(ThermostatRoleDetailsViewModel thermostatRoleDetailsViewModel);
    void inject(MainViewModel mainViewModel);
    void inject(MainActionBarViewModel mainActionBarViewModel);
    void inject(SecuredDevicesFoundViewModel securedDevicesFoundViewModel);
    void inject(SetEmergencyPasswordViewModel setEmergencyPasswordViewModel);
    void inject(ProfileViewModel profileViewModel);
    void inject(TouchConfigurationViewModel touchConfigurationViewModel);
    void inject(TroubleshootingViewModel troubleshootingViewModel);
    void inject(MainActivity mainActivity);
    void inject(LockViewModel lockViewModel);
    void inject(MainsAdvancedRoleDetailsViewModel mainsAdvancedRoleDetailsViewModel);
    void inject(ResultViewModel resultViewModel);
    void inject(TouchPressViewModel touchPressViewModel);
    void inject(TouchProgressViewModel touchProgressViewModel);
    void inject(VerifyEmailLightViewModel verifyEmailLightViewModel);
    void inject(RelayWorkingModeItemViewModel relayWorkingModeItemViewModel);
}
