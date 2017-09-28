package eu.geekhome.asymptote.model;


import java.util.Date;

public class DateTimeTriggerValue<T> {

    private final Date _date;
    private final T _value;

    public DateTimeTriggerValue(Date date, T value) {
        _date = date;
        _value = value;
    }

    public Date getDate() {
        return _date;
    }

    public T getValue() {
        return _value;
    }
}
