package eu.geekhome.asymptote.model;

public class CloudPasswordSyncUpdate extends SyncUpdateBase<String> {
    public CloudPasswordSyncUpdate(String value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
