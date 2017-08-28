package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.services.NavigationService;

public class SplashViewModel extends BaseObservable {
    private final NavigationService _navigationService;
    private String _errorMessage;
    private boolean _busy;

    @Bindable
    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        _errorMessage = errorMessage;
        notifyPropertyChanged(BR.errorMessage);
    }

    @Bindable
    public boolean isBusy() {
        return _busy;
    }

    public void setBusy(boolean busy) {
        _busy = busy;
        notifyPropertyChanged(BR.busy);
    }

    public SplashViewModel(NavigationService navigationService) {
        _navigationService = navigationService;
    }

    public void showTermsAndPrivacy() {
        _navigationService.showTermsAndPrivacy();
    }
}
