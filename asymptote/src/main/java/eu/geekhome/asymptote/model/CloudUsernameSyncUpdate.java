package eu.geekhome.asymptote.model;

public class CloudUsernameSyncUpdate extends SyncUpdateBase<String> {
    public CloudUsernameSyncUpdate(String value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
