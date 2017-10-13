package eu.geekhome.asymptote.model;

import android.content.Context;

public abstract class Automation<T, V> {
    private final int _index;
    private final T _trigger;
    private final V _value;
    private boolean _enabled;

    public Automation(int index, T trigger, V value, boolean enabled) {
        _index = index;
        _trigger = trigger;
        _value = value;
        _enabled = enabled;
    }

    public T getTrigger() {
        return _trigger;
    }

    public V getValue() {
        return _value;
    }

    public int getIndex() {
        return _index;
    }

    public abstract String composeMessage(Context context);

    public boolean isEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean enabled) {
        _enabled = enabled;
    }

    public abstract  boolean supportsRole(BoardRole role);

}
