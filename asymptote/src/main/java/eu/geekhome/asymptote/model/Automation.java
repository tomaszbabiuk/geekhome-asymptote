package eu.geekhome.asymptote.model;

public class Automation<T, V> {
    private final int _index;
    private final T _trigger;
    private final V _value;

    public Automation(int index, T trigger, V value) {
        _index = index;
        _trigger = trigger;
        _value = value;
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
}
