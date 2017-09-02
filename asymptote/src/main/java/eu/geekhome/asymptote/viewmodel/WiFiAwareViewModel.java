package eu.geekhome.asymptote.viewmodel;

import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.WiFiChangedListener;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public abstract class WiFiAwareViewModel<T extends ViewDataBinding> extends ViewModel<T> implements WiFiChangedListener {
    private final MainViewModelsFactory _factory;
    private final WiFiHelper _wifiHelper;
    private final NavigationService _navigationService;

    protected abstract boolean isCloudOnlyAllowed();
    protected abstract String getNoWiFiRationale();

    public WiFiAwareViewModel(MainViewModelsFactory factory, WiFiHelper wiFiHelper, NavigationService navigationService) {
        _factory = factory;
        _wifiHelper = wiFiHelper;
        _navigationService = navigationService;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!_wifiHelper.isWifiConnected() && !_wifiHelper.isCloudOnly() && !isCloudOnlyAllowed()) {
            showNoWiFiOverlay();
        }
        _wifiHelper.registerCallback(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        _wifiHelper.unregisterCallback();
    }

    private void showNoWiFiOverlay() {
        NoWiFiViewModel noWiFiViewModel =  _factory.createNoWiFiViewModel(isCloudOnlyAllowed(), getNoWiFiRationale());
        _navigationService.showOverlayViewModel(noWiFiViewModel);
    }

    @Override
    public void wifiChanged(boolean connected) {
        if (connected) {
            _navigationService.clearOverlay();
        } else {
            showNoWiFiOverlay();
        }
    }
}
