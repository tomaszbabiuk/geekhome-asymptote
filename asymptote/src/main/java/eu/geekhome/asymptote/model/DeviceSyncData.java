package eu.geekhome.asymptote.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.services.SyncSource;

public class DeviceSyncData extends BaseObservable {
    private boolean _locked;
    private SystemInformation _systemInformation;
    private Integer _temperature;
    private Integer _humidity;
    private Integer _dust;
    private Integer _luminosity;
    private Integer _noise;
    private boolean[] _relayStates;
    private int[] _pwmDuties;
    private long[] _params;
    private long[] _relayImpulses;
    private long[] _pwmImpulses;
    private Double _comfortTemperature;
    private DeviceKey _deviceKey;
    private String _name;
    private int _color;
    private BoardRole _role;
    private BoardMode _mode;
    private SyncSource _source;
    private boolean _isSensor;
    private OtaState _ota;
    private String _state;


    public DeviceSyncData(boolean locked, SystemInformation systemInformation,
                          DeviceKey deviceKey, String name, int color, BoardRole role,
                          OtaState ota, BoardMode mode, String state, SyncSource source) {
        _locked = locked;
        _systemInformation = systemInformation;
        _deviceKey = deviceKey;
        _name = name;
        _color = color;
        _role = role;
        _mode = mode;
        _source = source;
        _isSensor = false;
        _ota = ota;
        _state = state;
    }

    @Bindable
    public boolean isSensor() {
        return _isSensor;
    }

    public void setSensor(boolean sensor) {
        _isSensor = sensor;
        notifyPropertyChanged(BR.sensor);
    }

    @Bindable
    public int getColor() {
        return _color;
    }

    public void setColor(int color) {
        _color = color;
        notifyPropertyChanged(BR.color);
    }

    @Bindable
    public BoardRole getRole() {
        return _role;
    }

    public void setRole(BoardRole role) {
        _role = role;
        notifyPropertyChanged(BR.role);
    }

    @Bindable
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public Integer getTemperature() {
        if (_temperature != null) {
            return _temperature;
        }

        return null;
    }

    public void setTemperature(Integer temperature) {
        _temperature = temperature;
        notifyPropertyChanged(BR.temperature);
        calculateComfortTemperature();
        setSensor(true);
    }

    @Bindable
    public Double getComfortTemperature() {
        return _comfortTemperature;
    }

    public void setComfortTemperature(Double temperature) {
        _comfortTemperature = temperature;
        notifyPropertyChanged(BR.comfortTemperature);
    }

    @Bindable
    public Integer getHumidity() {
        return _humidity;
    }

    public void setHumidity(Integer humidity) {
        _humidity = humidity;
        notifyPropertyChanged(BR.humidity);
        calculateComfortTemperature();
        setSensor(true);
    }

    @Bindable
    public Integer getLuminosity() {
        return _luminosity;
    }

    public void setLuminosity(Integer luminosity) {
        _luminosity = luminosity;
        notifyPropertyChanged(BR.luminosity);
        setSensor(true);
    }

    @Bindable
    public Integer getNoise() {
        return _noise;
    }

    public void setNoise(Integer noise) {
        _noise = noise;
        notifyPropertyChanged(BR.noise);
        setSensor(true);
    }

    @Bindable
    public Integer getDust() {
        return _dust;
    }

    public void setDust(Integer dust) {
        _dust = dust;
        notifyPropertyChanged(BR.dust);
        setSensor(true);
    }

    @Bindable
    public SystemInformation getSystemInfo() {
        return _systemInformation;
    }

    public void setInfo(SystemInformation info) {
        _systemInformation = info;
        notifyPropertyChanged(BR.systemInfo);
    }

    @Bindable
    public boolean[] getRelayStates() {
        return _relayStates;
    }

    public void setRelayStates(boolean[] states) {
        _relayStates = states;
        notifyPropertyChanged(BR.relayStates);
    }

    @Bindable
    public int[] getPwmDuties() {
        return _pwmDuties;
    }

    public void setPwmDuties(int[] duties) {
        _pwmDuties = duties;
        notifyPropertyChanged(BR.pwmDuties);
    }

    @Bindable
    public long[] getParams() {
        return _params;
    }

    public void setParams(long[] params) {
        _params = params;
        notifyPropertyChanged(BR.params);
    }


    private void calculateComfortTemperature() {
        if (_temperature != null && _humidity != null) {
            double wind = 0;
            double temperatureInC = _temperature - 273.15;
            double result = 37 - ((37 - temperatureInC) / (0.68 - 0.0014 * _humidity + (1 / (1.76 + 1.4 * Math.pow(wind, 0.75))))) - 0.29 * temperatureInC * (1 - (_humidity / 100));
            setComfortTemperature(result);
        } else {
            setComfortTemperature(0.0);
        }
    }

    @Bindable
    public DeviceKey getDeviceKey() {
        return _deviceKey;
    }

    @Bindable
    public boolean isLocked() {
        return _locked;
    }

    public void setLocked(boolean locked) {
        _locked = locked;
        notifyPropertyChanged(BR.locked);
    }

    public OtaState getOta() {
        return _ota;
    }

    @Bindable
    public BoardMode getMode() {
        return _mode;
    }

    public void setMode(BoardMode mode) {
        _mode = mode;
        notifyPropertyChanged(BR.mode);
    }

    @Bindable
    public long[] getRelayImpulses() {
        return _relayImpulses;
    }

    public void setRelayImpulses(long[] relayImpulses) {
        _relayImpulses = relayImpulses;
        notifyPropertyChanged(BR.relayImpulses);
    }

    @Bindable
    public long[] getPwmImpulses() {
        return _pwmImpulses;
    }

    public void setPwmImpulses(long[] pwmImpulses) {
        _pwmImpulses = pwmImpulses;
        notifyPropertyChanged(BR.pwmImpulses);
    }

    public SyncSource getSource() {
        return _source;
    }

    @Bindable
    public String getState() {
        return _state;
    }

    public void setState(String state) {
        _state = state;
        notifyPropertyChanged(BR.state);
    }
}
