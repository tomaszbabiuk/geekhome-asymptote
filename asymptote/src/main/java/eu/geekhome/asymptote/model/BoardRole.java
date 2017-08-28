package eu.geekhome.asymptote.model;


import android.support.annotation.RawRes;

import eu.geekhome.asymptote.R;

public enum BoardRole {
    NONE(0, R.raw.power_off, R.raw.power_on, false, false, false),
    RGB_2PWM(1, R.raw.lightbulb_off, R.raw.lightbulb_on_dimmable, false, false, false),
    MULTI_PWM(2, R.raw.lightbulb_off, R.raw.lightbulb_on_dimmable, false, false, false),
    TOUCH1(3, R.raw.sonofftouch_off, R.raw.sonofftouch_on, false, false, false),
    MAINS1(4, R.raw.power_off, R.raw.power_on, false, false, false),
    HEATING_THERMOSTAT(5, R.raw.heating_all, R.raw.heating_sun, true, true, false),
    COOLING_THERMOSTAT(6, R.raw.heating_all, R.raw.heating_snow, true, true, false),
    HEATING_COMFORTSTAT(7, R.raw.heating_all, R.raw.heating_sun, true, true, false),
    COOLING_COMFORTSTAT(8, R.raw.heating_all, R.raw.heating_snow, true, true, false),
    DRYING_HYGROSTAT(9, R.raw.drying_off, R.raw.drying_on, true, true, false),
    HUMIDIFICATION_HYGROSTAT(10, R.raw.humidification_off, R.raw.humidification_on, true, true, false),
    MULTISENSOR(11, R.raw.smokedetector, R.raw.smokedetector, false, false, false),
    RGBW(12, R.raw.lightbulb_off, R.raw.lightbulb_on_dimmable, false, false, false),
    RGB_1PWM(13, R.raw.lightbulb_off, R.raw.lightbulb_on_dimmable, false, false, false),
    MAINS2(14, R.raw.power_off, R.raw.power_on, false, false, false),
    MAINS4(15, R.raw.power_off, R.raw.power_on, false, false, false),
    MAINS1_ADV(16, R.raw.power_off, R.raw.power_on, true, false, true),
    MAINS2_ADV(17, R.raw.power_off, R.raw.power_on, true, false, true),
    MAINS4_ADV(18, R.raw.power_off, R.raw.power_on, true, false, true),
    GATE(19, R.raw.garagedoor, R.raw.garagedoor_open, false, true, false),

    GeekHOME(255, R.raw.robot, R.raw.robot, false, false, false);

    private final int _id;
    private final boolean _advanced;
    private int _inactiveIconId;
    private int _activeIconId;
    private boolean _hasDetails;
    private boolean _automatic;

    BoardRole(int id, @RawRes int inactiveIconId, @RawRes int activeIconId, boolean hasDetails, boolean automatic, boolean advanced) {
        _id = id;
        _inactiveIconId = inactiveIconId;
        _activeIconId = activeIconId;
        _hasDetails = hasDetails;
        _automatic = automatic;
        _advanced = advanced;
    }

    public int getId() {
        return _id;
    }

    public static BoardRole fromInt(int roleAsInt) {
        for(BoardRole value : values()) {
            if (value.getId() == roleAsInt) {
                return value;
            }
        }

        return NONE;
    }

    public int getInactiveIconId() {
        return _inactiveIconId;
    }

    public int getActiveIconId() {
        return _activeIconId;
    }

    public boolean isHasDetails() {
        return _hasDetails;
    }

    public boolean isAutomatic() {
        return _automatic;
    }

    public boolean isAdvanced() {
        return _advanced;
    }
}
