package eu.geekhome.asymptote.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import eu.geekhome.asymptote.BR;

public class WiFiParameters extends BaseObservable {
    private String _ssid;
    private String _bssid;
    private boolean _hidden;
    private String _password;

    public WiFiParameters(String ssid, String password, String bssid, boolean hidden) {
        _ssid = ssid;
        _bssid = bssid;
        _hidden = hidden;
        _password = password;
    }

    @Bindable
    public String getSsid() {
        return _ssid;
    }

    public void setSsid(String ssid) {
        _ssid = ssid;
        notifyPropertyChanged(BR.ssid);
    }

    @Bindable
    public String getBssid() {
        return _bssid;
    }

    public void setBssid(String bssid) {
        _bssid = bssid;
        notifyPropertyChanged(BR.bssid);
    }

    @Bindable
    public boolean isHidden() {
        return _hidden;
    }

    public void setHidden(boolean ssidHidden) {
        _hidden = ssidHidden;
        notifyPropertyChanged(BR.hidden);
    }

    @Bindable
    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
        notifyPropertyChanged(BR.password);
    }
}