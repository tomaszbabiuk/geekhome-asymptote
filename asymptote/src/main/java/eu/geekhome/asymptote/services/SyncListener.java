package eu.geekhome.asymptote.services;

import java.net.InetAddress;
import java.util.ArrayList;

import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.SyncUpdate;
import eu.geekhome.asymptote.model.Variant;

public interface SyncListener {
    void onAfterSync(InetAddress from, DeviceSyncData syncData, long timestamp, String token);
    void onUnsupportedBoard(InetAddress from, String deviceId);
    void onSecuredDeviceFound(InetAddress from);
    void onCloudDeviceFound(InetAddress from, Variant variant, BoardId boardId, Byte[] restoreTokenPart);
}
