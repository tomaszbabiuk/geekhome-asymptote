package eu.geekhome.asymptote.model;

public class ColorSyncUpdate extends SyncUpdateBase<Integer> {
    public ColorSyncUpdate(Integer value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
