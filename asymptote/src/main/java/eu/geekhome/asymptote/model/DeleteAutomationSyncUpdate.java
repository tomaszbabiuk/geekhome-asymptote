package eu.geekhome.asymptote.model;

public class DeleteAutomationSyncUpdate<T,V> extends SyncUpdateBase<Automation<T,V>> {

    public DeleteAutomationSyncUpdate(Automation<T,V> value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
