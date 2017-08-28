package eu.geekhome.asymptote.model;

public class OtaHashSyncUpdate extends SyncUpdateBase<String> {
    public OtaHashSyncUpdate(String value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
