package eu.geekhome.asymptote.services.impl;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NanoDeviceDataSnapshot {
    @SerializedName("role")
    private int _role;

    @SerializedName("mode")
    private int _mode;

    @SerializedName("color")
    private int _color;

    @SerializedName("temp")
    private Integer _temp;

    @SerializedName("hum")
    private Integer _hum;

    @SerializedName("dust")
    private Integer _dust;

    @SerializedName("lum")
    private Integer _lum;

    @SerializedName("noise")
    private Integer _noise;

    @SerializedName("inputs")
    private ArrayList<Byte> _inputs;

    @SerializedName("pwms")
    private ArrayList<Integer> _pwms;

    @SerializedName("relayImpulses")
    private ArrayList<Long> _relayImpulses;

    @SerializedName("pwmImpulses")
    private ArrayList<Long> _pwmImpulses;

    @SerializedName("vmajor")
    private int _vmajor;

    @SerializedName("vminor")
    private int _vminor;

    @SerializedName("fvariant")
    private int _fvariant;

    @SerializedName("boardid")
    private int _boardId;

    @SerializedName("name")
    private String _name;

    @SerializedName("chipid")
    private String _chipId;

    @SerializedName("relays")
    private ArrayList<Byte> _relays;

    @SerializedName("params")
    private ArrayList<Long> _params;

    @SerializedName("locked")
    private int _locked;

    @SerializedName("ota")
    private byte _ota;

    @SerializedName("state")
    private  String _state;

    public Integer getTemp() {
        return _temp;
    }

    public Integer getHum() {
        return _hum;
    }

    public Integer getDust() {
        return _dust;
    }

    public Integer getLum() {
        return _lum;
    }

    public Integer getNoise() {
        return _noise;
    }

    public int getVmajor() {
        return _vmajor;
    }

    public int getVminor() {
        return _vminor;
    }

    public int getFvariant() {
        return _fvariant;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public ArrayList<Byte> getRelays() {
        return _relays;
    }

    public ArrayList<Integer> getPwms() {
        return _pwms;
    }

    public ArrayList<Byte> getInputs() {
        return _inputs;
    }

    public ArrayList<Long> getParams() {
        return _params;
    }

    public int getRole() {
        return _role;
    }

    public int getColor() {
        return _color;
    }

    public int isLocked() {
        return _locked;
    }

    public byte getOta() {
        return _ota;
    }

    public int getBoardId() {
        return _boardId;
    }

    public ArrayList<Long> getRelayImpulses() {
        return _relayImpulses;
    }

    public ArrayList<Long> getPwmImpulses() {
        return _pwmImpulses;
    }

    public String getChipId() {
        return _chipId;
    }

    public String getState() {
        return _state;
    }
}
