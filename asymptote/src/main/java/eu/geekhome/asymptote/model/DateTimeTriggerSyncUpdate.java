package eu.geekhome.asymptote.model;

public class DateTimeTriggerSyncUpdate<T> extends SyncUpdateBase<DateTimeTriggerValue<T>> {

    public DateTimeTriggerSyncUpdate(DateTimeTriggerValue<T> value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
