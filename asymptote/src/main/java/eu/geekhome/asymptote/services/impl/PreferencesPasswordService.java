package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import eu.geekhome.asymptote.services.ObfuscatorService;
import eu.geekhome.asymptote.services.PasswordService;

public class PreferencesPasswordService implements PasswordService {
    private final String KEY_REMEMBER_CLOUD_CREDENTIALS = "Remember email and password";
    private final String KEY_REMEMBER_EMERGENCY_PASSWORD = "Remember emergency password";
    private final String KEY_EMERGENCY_PASSWORD = "Emergency password";
    private final String KEY_CLOUD_PASSWORD = "Cloud password";
    private final String KEY_CLOUD_EMAIL = "Cloud email";

    private SharedPreferences _settings;
    private final ObfuscatorService _obfuscatorService;

    public PreferencesPasswordService(Context context, ObfuscatorService obfuscatorService) {
        _settings = PreferenceManager.getDefaultSharedPreferences(context);
        _obfuscatorService = obfuscatorService;
    }

    @Override
    public void setRememberEmergencyPassword(boolean remember) {
        _settings.edit().putBoolean(KEY_REMEMBER_EMERGENCY_PASSWORD, remember).apply();
    }

    @Override
    public void setRememberCloudCredentials(boolean remember) {
        _settings.edit().putBoolean(KEY_REMEMBER_CLOUD_CREDENTIALS, remember).apply();
    }

    @Override
    public void setEmergencyPassword(String emergencyPassword) {
        String obfuscated = _obfuscatorService.obfuscate(emergencyPassword);
        _settings.edit().putString(KEY_EMERGENCY_PASSWORD, obfuscated).apply();
    }

    @Override
    public void setCloudPassword(String cloudPassword) {
        String obfuscated = _obfuscatorService.obfuscate(cloudPassword);
        _settings.edit().putString(KEY_CLOUD_PASSWORD, obfuscated).apply();
    }

    @Override
    public void setCloudEmail(String cloudUsername) {
        String obfuscated = _obfuscatorService.obfuscate(cloudUsername);
        _settings.edit().putString(KEY_CLOUD_EMAIL, obfuscated).apply();
    }

    @Override
    public boolean isRememberEmergencyPassword() {
        return _settings.getBoolean(KEY_REMEMBER_EMERGENCY_PASSWORD, false);
    }

    @Override
    public boolean isRememberCloudCredentials() {
        return _settings.getBoolean(KEY_REMEMBER_CLOUD_CREDENTIALS, false);
    }

    @Override
    public String getEmergencyPassword() {
        String obfuscated = _settings.getString(KEY_EMERGENCY_PASSWORD, null);
        return _obfuscatorService.deobfuscate(obfuscated);
    }

    @Override
    public String getCloudPassword() {
        String obfuscated = _settings.getString(KEY_CLOUD_PASSWORD, null);
        return _obfuscatorService.deobfuscate(obfuscated);
    }

    @Override
    public String getCloudEmail() {
        String obfuscated = _settings.getString(KEY_CLOUD_EMAIL, null);
        return _obfuscatorService.deobfuscate(obfuscated);
    }
}