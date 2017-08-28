package eu.geekhome.asymptote.model;

public class PWMSyncUpdate extends SyncUpdateBase<PWMValue> {
    public PWMSyncUpdate(PWMValue value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
