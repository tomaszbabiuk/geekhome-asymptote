package eu.geekhome.asymptote.model;

public class SchedulerTrigger {
    private final int _days;
    private final long _timeMark;

    public SchedulerTrigger(int days, long timeMark) {
        _days = days;
        _timeMark = timeMark;
    }

    public long getTimeMark() {
        return _timeMark;
    }

    public int getDays() {
        return _days;
    }
}
