package eu.geekhome.asymptote.model;

public class OtaHostSyncUpdate extends SyncUpdateBase<String> {
    public OtaHostSyncUpdate(String value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
