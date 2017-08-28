package eu.geekhome.asymptote.model;

public class ParamValue {

    private int _index;
    private long _value;

    public ParamValue(int index, long value) {
        _index = index;
        _value = value;
    }

    public int getIndex() {
        return _index;
    }

    public long getValue() {
        return _value;
    }

    public void setValue(int value) {
        _value = value;
    }
}
