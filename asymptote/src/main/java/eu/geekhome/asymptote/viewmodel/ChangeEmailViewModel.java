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
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentChangeEmailBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.utils.KeyboardHelper;
import eu.geekhome.asymptote.validation.ValidationContext;

public class ChangeEmailViewModel extends HelpViewModelBase<FragmentChangeEmailBinding> {
    private final ValidationContext _validation = new ValidationContext();
    private String _password;
    private String _email;
    private String _errorMessage;

    @Inject
    CloudUserService _cloudUserService;
    @Inject
    Context _context;
    @Inject
    NavigationService _navigationService;

    @Bindable
    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        _errorMessage = errorMessage;
        notifyPropertyChanged(eu.geekhome.asymptote.BR.errorMessage);
    }

    @Override
    protected boolean isCloudOnlyAllowed() {
        return true;
    }

    @Override
    protected String getNoWiFiRationale() {
        return null;
    }

    public ChangeEmailViewModel(ActivityComponent activityComponent) {
        super(activityComponent);
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public void next(@NonNull final View view) {
        setErrorMessage(null);
        if (getValidation().validate()) {
            KeyboardHelper.hideKeyboard(view);

            _cloudUserService.changeEmail(getPassword(), getEmail(), new CloudActionCallback<Void>() {
                @Override
                public void success(Void data) {
                    VerifyEmailLightViewModel verifyEmailLightViewModel = new VerifyEmailLightViewModel(getActivityComponent(), getEmail());
                    _navigationService.showViewModel(verifyEmailLightViewModel, new ShowBackButtonInToolbarViewParam());
                }

                @Override
                public void failure(CloudException exception) {
                    String failureMessage = _context.getString(R.string.error_changing_email, exception.getLocalizedMessage());
                    setErrorMessage(failureMessage);
                }
            });
        }
    }

    @Override
    public FragmentChangeEmailBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentChangeEmailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_email, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public ValidationContext getValidation() {
        return _validation;
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
    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
        notifyPropertyChanged(BR.email);
    }
}