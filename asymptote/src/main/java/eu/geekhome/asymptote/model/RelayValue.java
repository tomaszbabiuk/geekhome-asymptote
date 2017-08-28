package eu.geekhome.asymptote.model;

public class RelayValue {
    private boolean _state;
    private int _channel;

    public RelayValue(int channel, boolean state) {
        _state = state;
        _channel = channel;
    }

    public boolean getState() {
        return _state;
    }

    public int getChannel() {
        return _channel;
    }

    public void setState(boolean state) {
        _state = state;
    }
}
