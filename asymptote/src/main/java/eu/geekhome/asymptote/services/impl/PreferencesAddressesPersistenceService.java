package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.utils.ByteUtils;


public class PreferencesAddressesPersistenceService implements AddressesPersistenceService {
    private final String KEY_LOCAL_DISCOVERY = "Local discovery";
    private SharedPreferences _settings;
    private Hashtable<String, InetAddress> _addresses = new Hashtable<>();

    public PreferencesAddressesPersistenceService(Context context) {
        _settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void load() {
        Set<String> saved = _settings.getStringSet(KEY_LOCAL_DISCOVERY, null);
        if (saved != null) {
            for (String address : saved) {
                try {

                    byte[] bytes = {
                            (byte) Integer.parseInt(address.substring(0, 2), 16),
                            (byte) Integer.parseInt(address.substring(2, 4), 16),
                            (byte) Integer.parseInt(address.substring(4, 6), 16),
                            (byte) Integer.parseInt(address.substring(6, 8), 16)
                    };
                    _addresses.put(address, InetAddress.getByAddress(bytes));
                } catch (UnknownHostException ignored) {
                }
            }
        }
    }


    @Override
    public void save() {
        _settings.edit().putStringSet(KEY_LOCAL_DISCOVERY, _addresses.keySet()).apply();
    }

    @Override
    public void clear() {
        _addresses.clear();
    }

    @Override
    public void add(InetAddress address) {
        if (!_addresses.containsValue(address)) {
            _addresses.put(ByteUtils.bytesToHex(address.getAddress()), address);
        }
    }

    @Override
    public Enumeration<InetAddress> getAddresses() {
        return _addresses.elements();
    }

    @Override
    public boolean isEmpty() {
        return _addresses.isEmpty();
    }

    @Override
    public boolean contains(InetAddress address) {
        return _addresses.containsValue(address);
    }
}