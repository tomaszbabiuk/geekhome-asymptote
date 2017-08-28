package eu.geekhome.asymptote.utils;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import eu.geekhome.asymptote.R;

public enum SensorColorMatcher {
    Floors(0, R.drawable.rectangle_floors, R.drawable.rectangle_floors_selected, R.color.section_floors, R.color.section_floors_light),
    Favorites(1, R.drawable.rectangle_favorites, R.drawable.rectangle_favorites_selected, R.color.section_favorites, R.color.section_favorites_light),
    Heating(2, R.drawable.rectangle_heating, R.drawable.rectangle_heating_selected, R.color.section_heating, R.color.section_heating_light),
    HotWater(3, R.drawable.rectangle_hotwater, R.drawable.rectangle_hotwater_selected, R.color.section_hotwater, R.color.section_hotwater_light),
    Lights(4, R.drawable.rectangle_lights, R.drawable.rectangle_lights_selected, R.color.section_lights, R.color.section_lights_light),
    Locks(5, R.drawable.rectangle_locks, R.drawable.rectangle_locks_selected, R.color.section_locks, R.color.section_locks_light),
    Map(6, R.drawable.rectangle_map, R.drawable.rectangle_map_selected, R.color.section_map, R.color.section_map_light),
    Monitoring(7, R.drawable.rectangle_monitoring, R.drawable.rectangle_monitoring_selected, R.color.section_monitoring, R.color.section_monitoring_light),
    Ventilation(8, R.drawable.rectangle_ventilation, R.drawable.rectangle_ventilation_selected, R.color.section_ventilation, R.color.section_ventilation_light);

    private int _value;
    private int _normalDrawableId;
    private int _selectedDrawableId;
    private int _sectionColor;
    private int _sectionLightColor;

    SensorColorMatcher(int value, @DrawableRes int normalDrawableId, @DrawableRes int selectedDrawableId,
                       @ColorRes int sectionColor, @ColorRes int sectionLightColor) {
        _value = value;
        _normalDrawableId = normalDrawableId;
        _selectedDrawableId = selectedDrawableId;
        _sectionColor = sectionColor;
        _sectionLightColor = sectionLightColor;
    }

    public static SensorColorMatcher fromInt(int value) {
        for (SensorColorMatcher i : values()) {
            if (i.toInt() == value) {
                return i;
            }
        }

        return SensorColorMatcher.Floors;
    }

    public int resolveDrawableForValue(int value) {
        if (_value == value) {
            return _selectedDrawableId;
        }

        return _normalDrawableId;
    }


    public int toInt() {
        return _value;
    }

    public int getSectionColor() {
        return _sectionColor;
    }

    public int getSectionLightColor() {
        return _sectionLightColor;
    }
}

