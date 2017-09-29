package eu.geekhome.asymptote.model;

public class AutomationSyncUpdate<T,V> extends SyncUpdateBase<Automation<T,V>> {

    public AutomationSyncUpdate(Automation<T,V> value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
