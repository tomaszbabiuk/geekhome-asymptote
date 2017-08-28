package eu.geekhome.asymptote.model;

public class StateSyncUpdate extends SyncUpdateBase<String> {
    public StateSyncUpdate(String value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StateSyncUpdate) {
            StateSyncUpdate objAsStateSyncUpdate = (StateSyncUpdate)obj;
            return objAsStateSyncUpdate.getValue().equals(getValue());
        }
        return false;
    }
}
