package eu.geekhome.asymptote.services.impl;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HttpDateTimeAutomationResponse {
    @SerializedName("idx")
    private int _index;

    @SerializedName("tme")
    private int _time;

    @SerializedName("unt")
    private int _unit;

    @SerializedName("val")
    private long _value;

    @SerializedName("prm")
    private int _param;

    @SerializedName("end")
    private int _enabled;

    public int getIndex() {
        return _index;
    }

    public int getTime() {
        return _time;
    }

    public int getUnit() {
        return _unit;
    }

    public long getValue() {
        return _value;
    }

    public int getParam() {
        return _param;
    }

    public int getEnabled() {
        return _enabled;
    }
}
