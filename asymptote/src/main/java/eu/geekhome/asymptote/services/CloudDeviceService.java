package eu.geekhome.asymptote.services;

import java.util.ArrayList;

import eu.geekhome.asymptote.model.DeviceKey;
import eu.geekhome.asymptote.model.DeviceSnapshot;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.SyncUpdate;
import eu.geekhome.asymptote.model.UserSnapshot;

public interface CloudDeviceService {

    void removeDevice(String userId, DeviceKey key, CloudActionCallback<Void> callback);

    void registerForDeviceSyncEvents(String userId, DeviceSnapshot deviceSnapshot);

    void unregisterFromDeviceSyncEvents(String userId);

    void setSyncListener(SyncListener syncListener);

    void registerDevice(String userId, DeviceSnapshot deviceSnapshot, CloudActionCallback<Void> callback);

    void getUserSnapshot(String userId, CloudActionCallback<UserSnapshot> callback);

    void pushUpdates(String userId, String token, DeviceSyncData syncData, ArrayList<SyncUpdate> updates, CloudActionCallback<Void> callback);
}
