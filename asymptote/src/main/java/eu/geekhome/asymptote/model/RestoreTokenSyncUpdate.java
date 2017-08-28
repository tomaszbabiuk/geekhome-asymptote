package eu.geekhome.asymptote.model;

public class RestoreTokenSyncUpdate extends SyncUpdateBase<String> {
    public RestoreTokenSyncUpdate(String value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
