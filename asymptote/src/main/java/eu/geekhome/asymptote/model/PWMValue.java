package eu.geekhome.asymptote.model;


public class PWMValue {
    public int getDuty() {
        return _duty;
    }

    private int _duty;
    private int _channel;

    public PWMValue(int channel, int duty) {
        _channel = channel;
        _duty = duty;
    }

    public int getChannel() {
        return _channel;
    }

    public void setDuty(int duty) {
        _duty = duty;
    }
}
