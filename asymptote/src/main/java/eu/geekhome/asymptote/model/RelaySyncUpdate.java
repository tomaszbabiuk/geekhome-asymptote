package eu.geekhome.asymptote.model;

public class RelaySyncUpdate extends SyncUpdateBase<RelayValue> {
    public RelaySyncUpdate(RelayValue value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RelaySyncUpdate) {
            RelaySyncUpdate objAsRelayUpdate = (RelaySyncUpdate)obj;
            return objAsRelayUpdate.getValue().getChannel() == getValue().getChannel();
        }
        return false;
    }
}
