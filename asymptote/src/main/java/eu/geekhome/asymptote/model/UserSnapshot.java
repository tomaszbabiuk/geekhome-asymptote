package eu.geekhome.asymptote.model;

import java.util.ArrayList;

public class UserSnapshot {
    private final ArrayList<DeviceSnapshot> _deviceSnapshots;
    private final String _emergencyPassword;

    public UserSnapshot(ArrayList<DeviceSnapshot> devices, String emergencyPassword) {

        _deviceSnapshots = devices;
        _emergencyPassword = emergencyPassword;
    }

    public ArrayList<DeviceSnapshot> getDeviceSnapshots() {
        return _deviceSnapshots;
    }

    public String getEmergencyPassword() {
        return _emergencyPassword;
    }
}
