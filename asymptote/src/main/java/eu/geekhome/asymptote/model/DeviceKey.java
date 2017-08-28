package eu.geekhome.asymptote.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class DeviceKey extends BaseObservable {
    private final String _deviceId;
    private final BoardId _boardId;
    private String _chipId;

    public DeviceKey(String chipId, BoardId boardId) {
        _chipId = chipId;
        _deviceId = buildDeviceId(chipId, boardId.toInt());
        _boardId = boardId;
    }

    public static String buildDeviceId(String chipId, int boardIdAsInt) {
        return String.format("%s-%02X", chipId, boardIdAsInt).toLowerCase();
    }

    @Bindable
    public String getDeviceId() {
        return _deviceId;
    }

    public BoardId getBoardId() {
        return _boardId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceKey)) {
            return false;
        }

        DeviceKey toEqual = (DeviceKey)obj;
        return toEqual.getDeviceId().equals(getDeviceId()) &&
               toEqual.getBoardId().equals(getBoardId());
    }


    public String getChipId() {
        return _chipId;
    }
}
