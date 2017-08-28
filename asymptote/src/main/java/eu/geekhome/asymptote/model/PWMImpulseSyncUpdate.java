package eu.geekhome.asymptote.model;

public class PWMImpulseSyncUpdate extends SyncUpdateBase<ImpulseValue> {
    public PWMImpulseSyncUpdate(ImpulseValue value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
