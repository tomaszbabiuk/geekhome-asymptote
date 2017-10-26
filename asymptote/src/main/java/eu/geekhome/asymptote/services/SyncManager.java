package eu.geekhome.asymptote.services;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.SyncUpdate;
import eu.geekhome.asymptote.model.Variant;

public interface SyncManager {
    void setSyncListener(SyncListener listener);
    void pushUpdates(DeviceSyncData syncData, List<SyncUpdate> updates,
                     final InetAddress address, final SyncCallback syncCallback);
    void start() throws IOException;
    void stop();

    void pushUpdatesAtOnce(DeviceSyncData syncData, List<SyncUpdate> updates,
                     final InetAddress address, final SyncCallback syncCallback);


    void listAutomations(Variant variant, InetAddress address, SyncCallback syncCallback);

    void sync(Variant variant, InetAddress address, SyncCallback syncCallback);
    void lock(Variant variant, InetAddress address, SyncCallback callback, String userId);

    interface SyncCallback {
        void success();
        void failure(Exception exception);
    }
}
