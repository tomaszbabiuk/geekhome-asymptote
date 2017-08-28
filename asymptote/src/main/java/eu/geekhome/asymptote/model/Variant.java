package eu.geekhome.asymptote.model;

import android.support.annotation.RawRes;

import eu.geekhome.asymptote.R;

public enum Variant {
    WiFi((byte)0x00, R.raw.wifi, true, false),
    Firebase((byte)0x01, R.raw.cloud, false, true),
    Hybrid((byte)0x02, R.raw.hybrid, true, true),
    Unknown((byte)0xFF, R.raw.at, false, false);


    private byte _id;
    private final int _iconId;
    private boolean _wifi;
    private boolean _cloud;


    Variant(byte id, @RawRes int iconId, boolean wifi, boolean cloud) {
        _id = id;
        _iconId = iconId;
        _wifi = wifi;
        _cloud = cloud;
    }

    public int getId() {
        return _id;
    }

    public static Variant fromInt(int variantAsInt) {
        for(Variant value : values()) {
            if (value.getId() == variantAsInt) {
                return value;
            }
        }

        return Unknown;
    }

    public int getIconId() {
        return _iconId;
    }

    public boolean isWifi() {
        return _wifi;
    }

    public boolean isCloud() {
        return _cloud;
    }
}