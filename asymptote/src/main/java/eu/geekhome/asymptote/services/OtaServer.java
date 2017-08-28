package eu.geekhome.asymptote.services;

import java.io.IOException;

import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.Firmware;

public interface OtaServer {
    interface Listener {
        void downloadStarted();
    }

    void setListener(Listener listener);
    void stop();
    void start(DeviceSyncData syncData, Firmware firmware) throws IOException;

    boolean isRunning();
}
