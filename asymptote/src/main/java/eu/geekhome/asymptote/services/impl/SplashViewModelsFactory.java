package eu.geekhome.asymptote.services.impl;

import android.app.Activity;
import android.content.Context;

import javax.inject.Inject;

import eu.geekhome.asymptote.SplashActivity;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.PasswordService;
import eu.geekhome.asymptote.services.PrivacyService;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.viewmodel.LoginWithEmailViewModel;
import eu.geekhome.asymptote.viewmodel.LoginWithEmergencyViewModel;
import eu.geekhome.asymptote.viewmodel.PrivacyViewModel;
import eu.geekhome.asymptote.viewmodel.ResetPasswordDarkViewModel;
import eu.geekhome.asymptote.viewmodel.SignInViewModel;
import eu.geekhome.asymptote.viewmodel.SignUpDarkViewModel;
import eu.geekhome.asymptote.viewmodel.SplashViewModel;
import eu.geekhome.asymptote.viewmodel.VerifyEmailDarkViewModel;

public class SplashViewModelsFactory {

    private final CloudUserService _cloudUserService;
    private final ToastService _toastService;
    private final NavigationService _navigationService;
    private final CloudDeviceService _cloudDeviceService;
    private final Context _context;
    private final PrivacyService _privacyService;
    private final Activity _activity;
    private final PasswordService _passwordService;

    @Inject
    SplashViewModelsFactory(SplashActivity activity, CloudUserService cloudUserService, CloudDeviceService cloudDeviceService,
                            ToastService toastService, NavigationService navigationService, PrivacyService privacyService,
                            PasswordService passwordService) {
        _context = activity.getApplicationContext();
        _activity = activity;
        _cloudUserService = cloudUserService;
        _cloudDeviceService = cloudDeviceService;
        _toastService = toastService;
        _navigationService = navigationService;
        _privacyService = privacyService;
        _passwordService = passwordService;
    }

    public VerifyEmailDarkViewModel createVerifyEmailDarkViewModel(String email, String password,
                                                                   String message, SplashViewModel splashViewModel) {
        return new VerifyEmailDarkViewModel(_context, _cloudUserService, _cloudDeviceService, _toastService, _navigationService,
                email, password, message, splashViewModel);
    }

    public SignUpDarkViewModel createSignUpDarkViewModel(SplashViewModel splashViewModel) {
        return new SignUpDarkViewModel(this, _context, _navigationService, _cloudUserService, splashViewModel);
    }

    public ResetPasswordDarkViewModel createResetPasswordDarkViewModel(SplashViewModel splashViewModel) {
        return new ResetPasswordDarkViewModel(_context, _cloudUserService, _toastService, _navigationService, splashViewModel);
    }

    public SplashViewModel createSplashViewModel() {
        return new SplashViewModel(_navigationService);
    }

    public SignInViewModel createSignInViewModel(SplashViewModel splashViewModel) {
        return new SignInViewModel(_navigationService, this, _passwordService, _cloudUserService,
                _context, _cloudDeviceService, splashViewModel);
    }

    public PrivacyViewModel createPrivacyViewModel(boolean showAgreement) {
        return new PrivacyViewModel(_privacyService, _activity, showAgreement);
    }

    public LoginWithEmailViewModel createLoginWithEmailViewModel(SplashViewModel splashViewModel) {
        return new LoginWithEmailViewModel(this, _context, _navigationService, _cloudUserService,
                _cloudDeviceService, _passwordService, splashViewModel);
    }

    public LoginWithEmergencyViewModel createLoginWithEmergencyViewModel(SplashViewModel splashViewModel) {
        return new LoginWithEmergencyViewModel(_navigationService, _passwordService, splashViewModel);
    }
}
