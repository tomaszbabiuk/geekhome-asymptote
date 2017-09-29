package eu.geekhome.asymptote.model;

public class DateTimeTriggerValue<T> {

    private final long _dateMark;
    private final long _timeMark;
    private final T _value;
    private final int _index;

    public DateTimeTriggerValue(int index, long dateMark, long timeMark, T value) {
        _index = index;
        _dateMark = dateMark;
        _timeMark = timeMark;
        _value = value;
    }

    public long getDateMark() {
        return _dateMark;
    }

    public long getTimeMark() {
        return _timeMark;
    }

    public T getValue() {
        return _value;
    }

    public int getIndex() {
        return _index;
    }
}
