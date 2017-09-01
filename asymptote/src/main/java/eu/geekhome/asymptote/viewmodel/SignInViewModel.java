package eu.geekhome.asymptote.viewmodel;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentSignInBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.SplashViewModelsFactory;

public class SignInViewModel extends ViewModel<FragmentSignInBinding> {
    private final SplashViewModel _splashViewModel;

    private final NavigationService _navigationService;
    private final SplashViewModelsFactory _factory;

    public SignInViewModel(NavigationService navigationService, SplashViewModelsFactory factory, SplashViewModel splashViewModel) {
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
        LoginViewModel loginViewModel = _factory.createLoginViewModel(_splashViewModel);
        _navigationService.showViewModel(loginViewModel);
    }

    public void signUp() {
        _splashViewModel.setErrorMessage(null);
        SignUpDarkViewModel signUpDarkViewModel = _factory.createSignUpDarkViewModel(_splashViewModel);
        _navigationService.showViewModel(signUpDarkViewModel, new ShowBackButtonInToolbarViewParam());
    }

    public void signInEmergency() {
        _navigationService.startMainPresentation(null, true, null);
    }

    public void resetPassword() {
        ResetPasswordDarkViewModel resetPasswordViewModel = _factory.createResetPasswordDarkViewModel(_splashViewModel);
        _navigationService.showViewModel(resetPasswordViewModel);
    }
}