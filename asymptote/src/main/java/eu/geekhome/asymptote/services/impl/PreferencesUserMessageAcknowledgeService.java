package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import eu.geekhome.asymptote.services.PrivacyService;
import eu.geekhome.asymptote.services.UserMessageAcknowledgeService;

public class PreferencesUserMessageAcknowledgeService implements UserMessageAcknowledgeService {
    private final String KEY_ACKNOWLEDGE = "User message";
    private SharedPreferences _settings;

    public PreferencesUserMessageAcknowledgeService(Context context) {
        _settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void setAcknowledged() {
        _settings.edit().putBoolean(KEY_ACKNOWLEDGE, true).apply();
    }

    @Override
    public boolean isAcknowledge() {
        return _settings.getBoolean(KEY_ACKNOWLEDGE, false);
    }
}