package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.DialogSetEmergencyPasswordBinding;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.utils.KeyboardHelper;
import eu.geekhome.asymptote.validation.ValidationContext;

public class SetEmergencyPasswordViewModel extends ViewModel<DialogSetEmergencyPasswordBinding> {
    private final NavigationService _navigationService;
    private final EmergencyManager _emergencyManager;
    private final CloudUserService _cloudUserService;
    private final ToastService _toastService;

    private boolean _rememberPassword;
    private String _password;
    private final MainViewModel _mainViewModel;
    private final ValidationContext _validation = new ValidationContext();
    private String _userId;
    private boolean _emergency;


    public SetEmergencyPasswordViewModel(NavigationService navigationService, EmergencyManager emergencyManager,
                                         CloudUserService cloudUserService, ToastService toastService,
                                         MainViewModel mainViewModel, String userId) {
        _navigationService = navigationService;
        _emergencyManager = emergencyManager;
        _cloudUserService = cloudUserService;
        _toastService = toastService;
        _mainViewModel = mainViewModel;
        _userId = userId;
        _emergency = _emergencyManager.isEmergency();
    }

    @Bindable
    public ValidationContext getValidation() {
        return _validation;
    }

    @Override
    public DialogSetEmergencyPasswordBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        DialogSetEmergencyPasswordBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.dialog_set_emergency_password, container, false);
        binding.setVm(this);
        return binding;
    }

    public void close() {
        KeyboardHelper.hideKeyboard(getBinding().getRoot());
        _navigationService.goBack();
    }

    public void ok() {
        if (_validation.validate()) {
            _navigationService.goBackTo(MainViewModel.class);
            _emergencyManager.setPassword(getPassword());
            _mainViewModel.rediscover();
            KeyboardHelper.hideKeyboard(getBinding().getRoot());

            if (isRememberPassword()) {
                _cloudUserService.rememberEmergencyPassword(_userId, getPassword(), new CloudActionCallback<Void>() {
                    @Override
                    public void success(Void data) {
                        _navigationService.goBackTo(MainViewModel.class);
                    }

                    @Override
                    public void failure(CloudException exception) {
                        _toastService.makeToast(exception.getLocalizedMessage(), true);
                    }
                });
            }
        }
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
    public boolean isRememberPassword() {
        return _rememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword) {
        _rememberPassword = rememberPassword;
        notifyPropertyChanged(BR.rememberPassword);
    }

    @Bindable
    public boolean isEmergency() {
        return _emergency;
    }
}
