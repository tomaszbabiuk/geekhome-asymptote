package eu.geekhome.asymptote.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class SystemInformation extends BaseObservable{
    private final Variant _variant;
    private final BoardId _boardId;
    private final int _versionMajor;
    private final int _versionMinor;

    public SystemInformation(BoardId boardId, int versionMajor, int versionMinor, Variant variant) {
        _boardId = boardId;
        _versionMajor = versionMajor;
        _versionMinor = versionMinor;
        _variant = variant;
    }

    @Bindable
    public int getVersionMajor() {
        return _versionMajor;
    }

    @Bindable
    public int getVersionMinor() {
        return _versionMinor;
    }

    @Bindable
    public Variant getVariant() {
        return _variant;
    }

    @Bindable
    public BoardId getBoardId() {
        return _boardId;
    }
}
