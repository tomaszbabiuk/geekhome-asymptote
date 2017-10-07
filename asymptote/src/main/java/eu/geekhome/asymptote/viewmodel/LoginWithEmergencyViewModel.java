package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentLoginWithEmergencyBinding;
import eu.geekhome.asymptote.model.DeviceSnapshot;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.PasswordService;
import eu.geekhome.asymptote.utils.KeyboardHelper;
import eu.geekhome.asymptote.validation.ValidationContext;

public class LoginWithEmergencyViewModel extends ViewModel<FragmentLoginWithEmergencyBinding> {
    private final ValidationContext _validation = new ValidationContext();
    private final NavigationService _navigationService;
    private final PasswordService _passwordService;
    private SplashViewModel _splashViewModel;
    private String _password;
    private View _rootView;
    private boolean _rememberPassword;

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

    public LoginWithEmergencyViewModel(NavigationService navigationService, PasswordService passwordService, SplashViewModel splashViewModel) {
        _navigationService = navigationService;
        _passwordService = passwordService;
        _splashViewModel = splashViewModel;
    }

    @Override
    public FragmentLoginWithEmergencyBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentLoginWithEmergencyBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_with_emergency, container, false);
        binding.setVm(this);
        _rootView = binding.getRoot();
        return binding;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPassword(null);
    }

    public void signInWithEmergency() {
        KeyboardHelper.hideKeyboard(_rootView);
        if (_validation.validate()) {
            _splashViewModel.setBusy(true);
            _splashViewModel.setErrorMessage(null);

            if (isRememberPassword()) {
                _passwordService.setRememberEmergencyPassword(true);
                _passwordService.setEmergencyPassword(getPassword());
            }

            UserSnapshot snapshot = new UserSnapshot(new ArrayList<DeviceSnapshot>(), getPassword());
            _navigationService.startMainPresentation(null, true, snapshot);
        }
    }

    @Bindable
    public boolean isRememberPassword() {
        return _rememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword) {
        _rememberPassword = rememberPassword;
        notifyPropertyChanged(BR.rememberPassword);
    }
}