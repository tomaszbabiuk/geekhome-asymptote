package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentChangePasswordBinding;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;
import eu.geekhome.asymptote.utils.KeyboardHelper;
import eu.geekhome.asymptote.validation.ValidationContext;

public class ChangePasswordViewModel extends HelpViewModelBase<FragmentChangePasswordBinding> {
    private final NavigationService _navigationService;
    private String _oldPassword;
    private String _newPassword;
    private String _confirmNewPassword;
    private String _errorMessage;
    private final ValidationContext _validation = new ValidationContext();
    private final CloudUserService _cloudUserService;
    private final ToastService _toastService;
    private final Context _context;

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

    public ChangePasswordViewModel(Context context, MainViewModelsFactory factory, WiFiHelper wifiHelper,
                                   NavigationService navigationService, ToastService toastService,
                                   CloudUserService cloudUserService) {
        super(factory, wifiHelper, navigationService);
        _context = context;
        _toastService = toastService;
        _cloudUserService = cloudUserService;
        _navigationService = navigationService;
    }

    public void done(@NonNull final View view) {
        setErrorMessage(null);

        if (getValidation().validate()) {
            KeyboardHelper.hideKeyboard(view);

            _cloudUserService.changePassword(getOldPassword(), getNewPassword(), new CloudActionCallback<Void>() {
                @Override
                public void success(Void data) {
                    String successMessage = _context.getString(R.string.password_has_been_changed);
                    _toastService.makeToast(successMessage, true);
                    _navigationService.goBack();
                }

                @Override
                public void failure(CloudException exception) {
                    String failureMessage = _context.getString(R.string.error_changing_password, exception.getLocalizedMessage());
                    setErrorMessage(failureMessage);
                }
            });
        }
    }

    @Override
    public FragmentChangePasswordBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentChangePasswordBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public ValidationContext getValidation() {
        return _validation;
    }

    @Bindable
    public String getOldPassword() {
        return _oldPassword;
    }

    public void setOldPassword(String value) {
        _oldPassword = value;
        notifyPropertyChanged(BR.oldPassword);
    }

    @Bindable
    public String getNewPassword() {
        return _newPassword;
    }

    public void setNewPassword(String value) {
        _newPassword = value;
        notifyPropertyChanged(BR.newPassword);
    }

    @Bindable
    public String getConfirmNewPassword() {
        return _confirmNewPassword;
    }

    public void setConfirmNewPassword(String value) {
        _confirmNewPassword = value;
        notifyPropertyChanged(BR.confirmNewPassword);
    }
}