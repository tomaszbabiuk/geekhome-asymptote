package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import javax.inject.Inject;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.NavigationService;

public class SecuredDevicesFoundViewModel extends BaseObservable implements LayoutHolder {

    private final MainViewModel _mainViewModel;
    private final ActivityComponent _activityComponent;
    private String _userId;
    private String _addresses;

    @Inject
    NavigationService _navigationService;

    public SecuredDevicesFoundViewModel(ActivityComponent activityComponent, MainViewModel mainViewModel, String userId) {
        activityComponent.inject(this);
        _activityComponent = activityComponent;
        _mainViewModel = mainViewModel;
        _userId = userId;
    }

    public void enterEmergencyPassword() {
        SetEmergencyPasswordViewModel setEmergencyPasswordViewModel =
                new SetEmergencyPasswordViewModel(_activityComponent, _mainViewModel, _userId);
        _navigationService.showOverlayViewModel(setEmergencyPasswordViewModel);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_secured_devices_found;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    @Bindable
    public String getAddresses() {
        return _addresses;
    }

    public void setAddresses(String addresses) {
        _addresses = addresses;
        notifyPropertyChanged(BR.addresses);
    }
}
