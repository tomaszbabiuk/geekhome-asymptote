package eu.geekhome.asymptote.model;

public class DeviceSnapshot {
    private final String _chipId;
    private final String _deviceToken;
    private final String _restoreToken;

    public DeviceSnapshot(String chipId, String deviceToken, String restoreToken) {
        _chipId = chipId;
        _deviceToken = deviceToken;
        _restoreToken = restoreToken;
    }

    public DeviceSnapshot(String serialized) throws Exception {
        String[] splited = serialized.split("\\|");
        if (splited.length == 3) {
            _chipId = splited[0];
            _deviceToken = splited[1];
            _restoreToken = splited[2];
        } else {
            throw new Exception("Invalid device snapshot format");
        }
    }

    public String getChipId() {
        return _chipId;
    }

    public String getDeviceToken() {
        return _deviceToken;
    }

    public String getRestoreToken() {
        return _restoreToken;
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s", getChipId(), getDeviceToken(), getRestoreToken());
    }
}
