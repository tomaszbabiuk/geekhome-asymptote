package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.WiFiParamsResolver;
import eu.geekhome.asymptote.utils.KeyboardHelper;
import eu.geekhome.asymptote.validation.ValidationContext;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentTouchConfigurationBinding;
import eu.geekhome.asymptote.model.WiFiParameters;
import eu.geekhome.asymptote.services.NavigationService;

public class TouchConfigurationViewModel extends HelpViewModelBase {
    private static String Password;
    private WiFiParameters _params;
    private boolean _rememberPassword;
    private final ValidationContext _validation = new ValidationContext();

    @Inject
    NavigationService _navigationService;
    @Inject
    Context _context;
    @Inject
    WiFiParamsResolver _wiFiParamsResolver;


    @Override
    protected String getNoWiFiRationale() {
        return _context.getString(R.string.rationale_nowifi_adding_devices);
    }

    public TouchConfigurationViewModel(ActivityComponent activityComponent) {
        super(activityComponent);
        _params = _wiFiParamsResolver.resolve();
        if (_params == null) {
            _params = new WiFiParameters("emu_ssid", null, "emu_bssid", false);
        }

        loadPassword();
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    private void loadPassword() {
        _params.setPassword(Password);
        setRememberPassword(Password != null);
    }

    @Bindable
    public WiFiParameters getParams() {
        return _params;
    }

    @SuppressWarnings("unused")
    public void onNext(@NonNull final View view) {
        if (getValidation().validate()) {
            KeyboardHelper.hideKeyboard(view);

            savePasswordIfChosen();
            TouchPressViewModel touchPressViewModel = new TouchPressViewModel(getActivityComponent(), getParams());
            _navigationService.showViewModel(touchPressViewModel, new ShowBackButtonInToolbarViewParam());
        }
    }

    private void savePasswordIfChosen() {
        if (getRememberPassword()) {
            Password = _params.getPassword();
        }
    }

    @Override
    public ViewDataBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentTouchConfigurationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_touch_configuration, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public boolean getRememberPassword() {
        return _rememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword) {
        _rememberPassword = rememberPassword;
    }

    @Bindable
    public ValidationContext getValidation() {
        return _validation;
    }
}