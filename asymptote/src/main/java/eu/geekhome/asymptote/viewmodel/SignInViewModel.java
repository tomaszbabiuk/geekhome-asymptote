package eu.geekhome.asymptote.viewmodel;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentSignInBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.NavigationService;

public class SignInViewModel extends ViewModel<FragmentSignInBinding> {
    private final SplashViewModel _splashViewModel;

    @Inject NavigationService _navigationService;

    public SignInViewModel(ActivityComponent activityComponent, SplashViewModel splashViewModel) {
        super(activityComponent);
        _splashViewModel = splashViewModel;
    }

    @Override
    public FragmentSignInBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentSignInBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        binding.setVm(this);
        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public void signInWithEmail() {
        LoginViewModel loginViewModel = new LoginViewModel(getActivityComponent(), _splashViewModel);
        _navigationService.showViewModel(loginViewModel);
    }

    public void signUp() {
        _splashViewModel.setErrorMessage(null);
        _navigationService.showViewModel(new SignUpDarkViewModel(getActivityComponent(), _splashViewModel),
                new ShowBackButtonInToolbarViewParam());
    }

    public void signInEmergency() {
        _navigationService.startMainPresentation(null, true, null);
    }

    public void resetPassword() {
        ResetPasswordDarkViewModel resetPasswordViewModel = new ResetPasswordDarkViewModel(
                getActivityComponent(), _splashViewModel);
        _navigationService.showViewModel(resetPasswordViewModel);
    }
}