package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentSignUpDarkBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.model.CloudUser;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.utils.KeyboardHelper;
import eu.geekhome.asymptote.validation.ValidationContext;

public class SignUpDarkViewModel extends ViewModel<FragmentSignUpDarkBinding> {
    @Inject
    CloudUserService _cloudUserService;

    @Inject
    Context _context;

    @Inject
    NavigationService _navigationService;

    private String _email;
    private final ValidationContext _validation = new ValidationContext();
    private String _password;
    private String _confirmPassword;
    private SplashViewModel _splashViewModel;

    public SignUpDarkViewModel(ActivityComponent activityComponent, SplashViewModel splashViewModel) {
        super(activityComponent);
        _splashViewModel = splashViewModel;
    }

    public void next(@NonNull final View view) {
        _splashViewModel.setErrorMessage(null);

        if (getValidation().validate()) {
            KeyboardHelper.hideKeyboard(view);
            _splashViewModel.setBusy(true);

            _cloudUserService.signUpWithEmailAndPassword(getEmail(), getPassword(), new CloudUserService.UserCallback() {
                @Override
                public void success(CloudUser user) {
                    String verificationMessage = _context.getString(R.string.email_verify_success, getEmail());
                    VerifyEmailDarkViewModel verifyViewModel = new VerifyEmailDarkViewModel(getActivityComponent(), getEmail(),
                            getPassword(), verificationMessage, _splashViewModel);
                    _navigationService.showViewModel(verifyViewModel);
                }

                @Override
                public void failure(CloudException exception) {
                    _splashViewModel.setBusy(false);
                    String failureMessage = _context.getString(R.string.error_signing_up, exception.getLocalizedMessage());
                    _splashViewModel.setErrorMessage(failureMessage);
                }
            });
        }
    }


    @Override
    public FragmentSignUpDarkBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentSignUpDarkBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_dark, container, false);
        binding.setVm(this);
        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Bindable
    public ValidationContext getValidation() {
        return _validation;
    }

    @Bindable
    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getConfirmPassword() {
        return _confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        _confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    @Override
    public void onResume() {
        super.onResume();
        _splashViewModel.setErrorMessage(null);
    }
}