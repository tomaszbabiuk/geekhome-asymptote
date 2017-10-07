package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentSignInBinding;
import eu.geekhome.asymptote.model.CloudUser;
import eu.geekhome.asymptote.model.DeviceSnapshot;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.PasswordService;
import eu.geekhome.asymptote.services.impl.SplashViewModelsFactory;

public class SignInViewModel extends ViewModel<FragmentSignInBinding> {
    private final PasswordService _passwordService;
    private final CloudUserService _cloudUserService;
    private final Context _context;
    private final CloudDeviceService _cloudDeviceService;
    private final SplashViewModel _splashViewModel;
    private final NavigationService _navigationService;
    private final SplashViewModelsFactory _factory;

    public SignInViewModel(NavigationService navigationService, SplashViewModelsFactory factory,
                           PasswordService passwordService, CloudUserService cloudUserService,
                           Context context, CloudDeviceService cloudDeviceService,
                           SplashViewModel splashViewModel) {
        _passwordService = passwordService;
        _cloudUserService = cloudUserService;
        _context = context;
        _cloudDeviceService = cloudDeviceService;
        _splashViewModel = splashViewModel;
        _navigationService = navigationService;
        _factory = factory;
    }

    @Override
    public FragmentSignInBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentSignInBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        binding.setVm(this);
        return binding;
    }

    public void signInWithEmail() {
        if (_passwordService.isRememberCloudCredentials()) {
            final String email = _passwordService.getCloudEmail();
            final String password = _passwordService.getCloudPassword();
            _splashViewModel.setBusy(true);
            _splashViewModel.setErrorMessage(null);
            _cloudUserService.signInWithEmailAndPassword(email, password, new CloudUserService.UserCallback() {
                @Override
                public void success(CloudUser user) {
                    if (!user.isEmailVerified()) {
                        _splashViewModel.setBusy(false);
                        String verificationMessage = _context.getString(R.string.finish_registration_process, email);

                        VerifyEmailDarkViewModel verifyViewModel = _factory.createVerifyEmailDarkViewModel(email,
                                password, verificationMessage, _splashViewModel);

                        _navigationService.showViewModel(verifyViewModel);
                    } else {
                        loadUserSnapshotAndProceed(user);
                    }
                }

                @Override
                public void failure(CloudException exception) {
                    _passwordService.setRememberCloudCredentials(false);
                    showError(exception);
                }
            });
        } else {
            _splashViewModel.setErrorMessage(null);
            LoginWithEmailViewModel model = _factory.createLoginWithEmailViewModel(_splashViewModel);
            _navigationService.showViewModel(model);
        }
    }

    public void signUp() {
        _splashViewModel.setErrorMessage(null);
        SignUpDarkViewModel signUpDarkViewModel = _factory.createSignUpDarkViewModel(_splashViewModel);
        _navigationService.showViewModel(signUpDarkViewModel, new ShowBackButtonInToolbarViewParam());
    }

    public void signInEmergency() {
        if (_passwordService.isRememberEmergencyPassword()) {
            String password = _passwordService.getEmergencyPassword();
            UserSnapshot snapshot = new UserSnapshot(new ArrayList<DeviceSnapshot>(), password);
            _navigationService.startMainPresentation(null, true, snapshot);
        } else {
            _splashViewModel.setErrorMessage(null);
            LoginWithEmergencyViewModel model = _factory.createLoginWithEmergencyViewModel(_splashViewModel);
            _navigationService.showViewModel(model);
        }
    }

    public void resetPassword() {
        ResetPasswordDarkViewModel resetPasswordViewModel = _factory.createResetPasswordDarkViewModel(_splashViewModel);
        _navigationService.showViewModel(resetPasswordViewModel);
    }

    private void showError(CloudException exception) {
        _splashViewModel.setBusy(false);
        String failureMessage = _context.getString(R.string.unable_to_signin, exception.getLocalizedMessage());
        _splashViewModel.setErrorMessage(failureMessage);
    }

    private void loadUserSnapshotAndProceed(final CloudUser user) {
        _cloudDeviceService.getUserSnapshot(user.getId(), new CloudActionCallback<UserSnapshot>() {
            @Override
            public void success(UserSnapshot data) {
                _navigationService.startMainPresentation(user.getId(), false, data);
            }

            @Override
            public void failure(CloudException exception) {
                showError(exception);
            }
        });
    }
}