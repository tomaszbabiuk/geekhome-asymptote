package eu.geekhome.asymptote.model;

public class DateTimeTrigger {
    private final long _utcTimestamp;

    public DateTimeTrigger(long utcTimestamp) {
        _utcTimestamp = utcTimestamp;
    }

    public long getUtcTimestamp() {
        return _utcTimestamp;
    }
}
