package eu.geekhome.asymptote.model;

public class RGBSyncUpdate extends SyncUpdateBase<RGBValue> {
    public RGBSyncUpdate(RGBValue value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
