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
import eu.geekhome.asymptote.databinding.FragmentResetPasswordDarkBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.utils.KeyboardHelper;
import eu.geekhome.asymptote.validation.ValidationContext;

public class ResetPasswordDarkViewModel extends ViewModel<FragmentResetPasswordDarkBinding> {
    private final ValidationContext _validation = new ValidationContext();
    @Inject CloudUserService _cloudUserService;
    @Inject Context _context;
    @Inject ToastService _toastService;
    private String _email;
    @Inject NavigationService _navigationService;
    private SplashViewModel _splashViewModel;

    public ResetPasswordDarkViewModel(ActivityComponent activityComponent, SplashViewModel splashViewModel) {
        super(activityComponent);
        _splashViewModel = splashViewModel;
    }

    public void resetPassword(@NonNull final View view) {
        _splashViewModel.setErrorMessage(null);

        if (getValidation().validate()) {
            KeyboardHelper.hideKeyboard(view);
            _splashViewModel.setBusy(true);

            _cloudUserService.resetPassword(getEmail(), new CloudActionCallback<Void>() {
                @Override
                public void success(Void data) {
                    resetSuccess();
                }

                private void resetSuccess() {
                    String verificationMessage = _context.getString(R.string.reset_password_success, getEmail());
                    _toastService.makeToast(verificationMessage, true);
                    _navigationService.goBackTo(SignInViewModel.class);
                }

                @Override
                public void failure(CloudException exception) {
                    if (exception.isImplicatesSecurity()) {
                        resetSuccess();
                    } else {
                        _splashViewModel.setBusy(false);
                        String failureMessage = _context.getString(R.string.error_resetting_password, exception.getLocalizedMessage());
                        _splashViewModel.setErrorMessage(failureMessage);
                    }
                }
            });
        }
    }


    @Override
    public FragmentResetPasswordDarkBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentResetPasswordDarkBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password_dark, container, false);
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

    @Override
    public void onResume() {
        super.onResume();
        _splashViewModel.setErrorMessage(null);
    }
}