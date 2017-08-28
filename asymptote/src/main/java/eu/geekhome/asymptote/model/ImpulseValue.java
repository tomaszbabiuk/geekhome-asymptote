package eu.geekhome.asymptote.model;


public class ImpulseValue  {
    private int _channel;
    private long _impulse;

    public ImpulseValue(int channel, long impulse) {
        _channel = channel;
        _impulse = impulse;
    }

    public int getChannel() {
        return _channel;
    }

    public long getImpulse() {
        return _impulse;
    }

    public void setImpulse(long impulse) {
        _impulse = impulse;
    }
}
