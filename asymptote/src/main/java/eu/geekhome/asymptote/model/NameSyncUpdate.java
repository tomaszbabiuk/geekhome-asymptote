package eu.geekhome.asymptote.model;

public class NameSyncUpdate extends SyncUpdateBase<String> {
    public NameSyncUpdate(String value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
