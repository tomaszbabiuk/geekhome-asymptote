package eu.geekhome.asymptote.services.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.geekhome.asymptote.services.WiFiChangedListener;
import eu.geekhome.asymptote.services.WiFiHelper;

public class AndroidWiFiHelper implements WiFiHelper {
	private static final Logger _logger = LoggerFactory.getLogger(AndroidWiFiHelper.class);

	private final Context _context;
    private WiFiChangedListener _listener;
    private BroadcastReceiver _wifiBroadcastReceiver;
	private boolean _cloudOnly;

    public AndroidWiFiHelper(Context context) {
		_context = context;
        _wifiBroadcastReceiver = new WifiReceiver();
	}

	@Override
	public WifiInfo getConnectionInfo() {
		WifiManager mWifiManager = (WifiManager) _context.getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		return mWifiManager.getConnectionInfo();
	}

	@Override
	public boolean isWifiConnected() {
		NetworkInfo mWiFiNetworkInfo = getWifiNetworkInfo();
		boolean isWifiConnected = false;
		if (mWiFiNetworkInfo != null) {
			isWifiConnected = mWiFiNetworkInfo.isConnected();
		}
		return isWifiConnected;
	}

	@Override
	public NetworkInfo getWifiNetworkInfo() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	}

    @Override
	public void registerCallback(WiFiChangedListener listener) {
        _listener = listener;

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

		_context.registerReceiver(_wifiBroadcastReceiver, intentFilter);
	}

    public void unregisterCallback() {
        _listener = null;
		try {
			_context.unregisterReceiver(_wifiBroadcastReceiver);
		} catch (Exception ex) {
			_logger.info("Problem unregistering receiver: ", ex);
		}
    }

	public boolean isCloudOnly() {
		return _cloudOnly;
	}

	public void setCloudOnly(boolean cloudOnly) {
		_cloudOnly = cloudOnly;
	}

	@Override
	public String getIP() {
		WifiManager mWifiManager = (WifiManager) _context.getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		return Formatter.formatIpAddress(mWifiManager.getConnectionInfo().getIpAddress());
	}

	public class WifiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if(info != null) {
                onWiFiChanged(info.isConnected());
			}
		}

        private void onWiFiChanged(boolean connected) {
            if (_listener != null) {
                _listener.wifiChanged(connected);
            }
        }
    }
}
