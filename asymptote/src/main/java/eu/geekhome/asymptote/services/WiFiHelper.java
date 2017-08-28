package eu.geekhome.asymptote.services;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

public interface WiFiHelper {
    WifiInfo getConnectionInfo();
    boolean isWifiConnected();
    boolean isCloudOnly();
    NetworkInfo getWifiNetworkInfo();
    void registerCallback(WiFiChangedListener listener);
    void unregisterCallback();
    void setCloudOnly(boolean cloudOnly);
    String getIP();
}
