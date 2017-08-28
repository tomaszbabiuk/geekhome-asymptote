package eu.geekhome.asymptote.model;

public class RoleSyncUpdate extends SyncUpdateBase<BoardRole> {
    public RoleSyncUpdate(BoardRole value) {
        super(value);
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
