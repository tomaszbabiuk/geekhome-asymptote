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

    @SerializedName("chn")
    private int _channel;

    public int getIndex() {
        return _index;
    }

    public void setIndex(int index) {
        _index = index;
    }

    public int getTime() {
        return _time;
    }

    public void setTime(int time) {
        _time = time;
    }

    public int getUnit() {
        return _unit;
    }

    public void setUnit(int unit) {
        _unit = unit;
    }

    public long getValue() {
        return _value;
    }

    public void setValue(long value) {
        _value = value;
    }

    public int getChannel() {
        return _channel;
    }

    public void setChannel(int channel) {
        _channel = channel;
    }
}
