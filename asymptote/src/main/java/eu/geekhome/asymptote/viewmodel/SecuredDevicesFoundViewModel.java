package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class SecuredDevicesFoundViewModel extends BaseObservable implements LayoutHolder {

    private final MainViewModel _mainViewModel;
    private final MainViewModelsFactory _factory;
    private final NavigationService _navigationService;
    private String _userId;
    private String _addresses;

    public SecuredDevicesFoundViewModel(MainViewModelsFactory factory, NavigationService navigationService, MainViewModel mainViewModel, String userId) {
        _factory = factory;
        _navigationService = navigationService;
        _mainViewModel = mainViewModel;
        _userId = userId;
    }

    public void enterEmergencyPassword() {
        SetEmergencyPasswordViewModel setEmergencyPasswordViewModel =
                _factory.createSetEmergencyPasswordViewModel(_mainViewModel, _userId);
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

    void setAddresses(String addresses) {
        _addresses = addresses;
        notifyPropertyChanged(BR.addresses);
    }
}
