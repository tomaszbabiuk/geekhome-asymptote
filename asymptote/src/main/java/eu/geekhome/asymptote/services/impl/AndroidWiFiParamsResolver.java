package eu.geekhome.asymptote.services.impl;

import android.net.wifi.WifiInfo;

import eu.geekhome.asymptote.model.WiFiParameters;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.WiFiParamsResolver;

public class AndroidWiFiParamsResolver implements WiFiParamsResolver {
	private final WiFiHelper _wifiHelper;

	public AndroidWiFiParamsResolver(WiFiHelper wiFiHelper) {
		_wifiHelper = wiFiHelper;
	}

	@Override
	public WiFiParameters resolve() {
		WifiInfo info = _wifiHelper.getConnectionInfo();

		if (info != null && _wifiHelper.isWifiConnected()) {
			String ssid;
			boolean hidden = info.getHiddenSSID();
			String bssid = info.getBSSID();
			int len = info.getSSID().length();
			if (info.getSSID().startsWith("\"")
					&& info.getSSID().endsWith("\"")) {
				ssid = info.getSSID().substring(1, len - 1);
			} else {
				ssid = info.getSSID();
			}


			return new WiFiParameters(ssid, null, bssid, hidden);
		}

		return null;
	}
}
