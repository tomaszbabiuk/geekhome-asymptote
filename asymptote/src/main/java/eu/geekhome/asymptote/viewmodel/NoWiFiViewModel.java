package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentOverlayNowifiBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.WiFiHelper;

public class NoWiFiViewModel extends ViewModel<FragmentOverlayNowifiBinding> {
    private final WiFiHelper _wifiHelper;
    private final NavigationService _navigationService;
    private boolean _cloudOnlyAllowed;
    private String _rationale;

    public NoWiFiViewModel(WiFiHelper wiFiHelper, NavigationService navigationService, boolean cloudOnlyAllowed, String rationale) {
        _wifiHelper = wiFiHelper;
        _navigationService = navigationService;
        setCloudOnlyAllowed(cloudOnlyAllowed);
        setRationale(rationale);
    }

    @Override
    public FragmentOverlayNowifiBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentOverlayNowifiBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overlay_nowifi, container, false);
        binding.setModel(this);
        return binding;
    }

    public void refresh() {
        if (_wifiHelper.isWifiConnected()) {
            _navigationService.clearOverlay();
        }
    }

    public void cancel() {
        _navigationService.clearOverlay();
        _navigationService.goBackTo(MainViewModel.class);
    }

    public void switchToCloudOnly() {
        _wifiHelper.setCloudOnly(true);
        _navigationService.clearOverlay();
    }

    @Bindable
    public boolean isCloudOnlyAllowed() {
        return _cloudOnlyAllowed;
    }

    private void setCloudOnlyAllowed(boolean cloudOnlyAllowed) {
        _cloudOnlyAllowed = cloudOnlyAllowed;
        notifyPropertyChanged(BR.cloudOnlyAllowed);
    }


    @Override
    public boolean goingBack() {
        return false;
    }

    @Bindable
    public String getRationale() {
        return _rationale;
    }

    private void setRationale(String rationale) {
        _rationale = rationale;
        notifyPropertyChanged(BR.rationale);
    }
}
