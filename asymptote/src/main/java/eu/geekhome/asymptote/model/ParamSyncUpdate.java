package eu.geekhome.asymptote.model;

public class ParamSyncUpdate extends SyncUpdateBase<ParamValue> {
    public ParamSyncUpdate(ParamValue value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
