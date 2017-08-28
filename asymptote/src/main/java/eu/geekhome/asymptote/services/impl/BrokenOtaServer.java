package eu.geekhome.asymptote.services.impl;

import java.io.IOException;

import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.services.OtaServer;

public class BrokenOtaServer implements OtaServer {
    @Override
    public void setListener(Listener listener) {

    }

    @Override
    public void start(DeviceSyncData syncData, Firmware firmware) throws IOException {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
