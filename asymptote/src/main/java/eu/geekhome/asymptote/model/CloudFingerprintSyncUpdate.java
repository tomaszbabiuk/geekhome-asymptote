package eu.geekhome.asymptote.model;

public class CloudFingerprintSyncUpdate extends SyncUpdateBase<String> {
    public CloudFingerprintSyncUpdate(String value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
