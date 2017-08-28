package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import eu.geekhome.asymptote.services.PrivacyService;

public class PreferencesPrivacyService implements PrivacyService {
    private final String HARDCODED_VERSION = "1.0";
    private final String KEY_ACCEPTED_PRIVACY_POLICY_VERSION = "Accepted privacy policy version";
    private SharedPreferences _settings;

    public PreferencesPrivacyService(Context context) {
        _settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void setAgreed() {
        _settings.edit().putString(KEY_ACCEPTED_PRIVACY_POLICY_VERSION, HARDCODED_VERSION).apply();
    }

    @Override
    public boolean isAccepted() {
        String version = _settings.getString(KEY_ACCEPTED_PRIVACY_POLICY_VERSION, null);
        return version != null && version.equals(HARDCODED_VERSION);
    }
}