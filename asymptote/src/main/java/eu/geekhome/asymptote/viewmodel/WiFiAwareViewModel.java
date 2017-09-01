package eu.geekhome.asymptote.viewmodel;

import android.databinding.ViewDataBinding;

import javax.inject.Inject;

import eu.geekhome.asymptote.bindingutils.InjectedViewModel;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.WiFiChangedListener;
import eu.geekhome.asymptote.services.WiFiHelper;

public abstract class WiFiAwareViewModel<T extends ViewDataBinding> extends InjectedViewModel<T> implements WiFiChangedListener {
    @Inject WiFiHelper _wifiHelper;
    @Inject NavigationService _navigationService;

    protected abstract boolean isCloudOnlyAllowed();
    protected abstract String getNoWiFiRationale();

    public WiFiAwareViewModel(ActivityComponent activityComponent) {
        super(activityComponent);
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
        NoWiFiViewModel noWiFiViewModel = new NoWiFiViewModel(getActivityComponent(), isCloudOnlyAllowed(),
                getNoWiFiRationale());
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
