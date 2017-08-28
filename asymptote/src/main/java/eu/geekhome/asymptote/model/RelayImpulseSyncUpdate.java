package eu.geekhome.asymptote.model;

public class RelayImpulseSyncUpdate extends SyncUpdateBase<ImpulseValue> {
    public RelayImpulseSyncUpdate(ImpulseValue value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
