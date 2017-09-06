package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableArrayList;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import eu.geekhome.asymptote.model.DeviceKey;


public class PreferencesFavoriteColorsService implements eu.geekhome.asymptote.services.FavoriteColorsService {
    private final String KEY_FAVORITE_COLORS = "Favorite colors";
    private final Gson _gson;
    private SharedPreferences _settings;

    private HashSet<ColorsSet> _colorsSets;

    public PreferencesFavoriteColorsService(Context context) {
        _settings = PreferenceManager.getDefaultSharedPreferences(context);
        _gson = new Gson();
        load();
    }

    @Override
    public void colorPicked(String setId, int color) {
        for (ColorsSet set : _colorsSets) {
            if (set.getKey().equals(setId)) {
                set.rankColor(color);
                save();
                return;
            }
        }
        ColorsSet newSet = new ColorsSet(setId);
        newSet.rankColor(color);
        _colorsSets.add(newSet);
        save();
    }

    public void colorRemoved(String setId, int color) {
        for (ColorsSet set : _colorsSets) {
            if (set.getKey().equals(setId)) {
                set.remove(color);
                save();
                return;
            }
        }
    }

    @Override
    public ArrayList<RankedColor> getFavoriteColors(String setId) {
        for (ColorsSet set : _colorsSets) {
            if (set.getKey().equals(setId)) {
                return set.getRankedColors();
            }
        }

        return new ObservableArrayList<>();
    }

    private void save() {
        HashSet<String> toSave = new HashSet<>();
        if (_colorsSets != null) {
            for (ColorsSet set : _colorsSets) {
                String json = _gson.toJson(set);
                toSave.add(json);
            }
        }

        _settings.edit().putStringSet(KEY_FAVORITE_COLORS, toSave).apply();
    }

    private void load() {
        Set<String> saved = _settings.getStringSet(KEY_FAVORITE_COLORS, null);
        _colorsSets = new HashSet<>();
        if (saved != null) {
            for (String savedItem : saved) {
                ColorsSet set = _gson.fromJson(savedItem, ColorsSet.class);
                set.sort();
                _colorsSets.add(set);
            }
        }
    }
}