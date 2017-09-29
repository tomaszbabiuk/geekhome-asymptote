package eu.geekhome.asymptote.model;

public class DateTimeTrigger {
    private final long _dateMark;
    private final long _timeMark;

    public DateTimeTrigger(long dateMark, long timeMark) {
        _dateMark = dateMark;
        _timeMark = timeMark;
    }

    public long getDateMark() {
        return _dateMark;
    }

    public long getTimeMark() {
        return _timeMark;
    }
}
