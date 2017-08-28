package eu.geekhome.asymptote.services;

import java.net.InetAddress;

import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.Variant;

public interface LocalDiscoveryService {
    interface Listener {
        void onFound(InetAddress address, Variant firmwareVariant, BoardId boardId, Byte[] restoreTokenPart);
    }

    void setListener(Listener listener);
    void startDiscovery();
    void stopDiscovery();
}
