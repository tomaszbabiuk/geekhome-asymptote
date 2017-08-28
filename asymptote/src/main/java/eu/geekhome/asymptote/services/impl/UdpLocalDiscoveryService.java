package eu.geekhome.asymptote.services.impl;

import java.net.DatagramPacket;

import eu.geekhome.asymptote.services.LocalDiscoveryService;
import eu.geekhome.asymptote.services.UdpService;
import eu.geekhome.asymptote.utils.DatagramProcessor;
import eu.geekhome.asymptote.utils.Ticker;

public class UdpLocalDiscoveryService implements LocalDiscoveryService, UdpService.StartedListener, UdpService.DatagramListener {
    private final UdpService _udpService;
    private Listener _listener;
    private Ticker _discoveryTicker;

    public UdpLocalDiscoveryService(UdpService udpService) {
        _udpService = udpService;
    }

    @Override
    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void startDiscovery() {
        _udpService.setStartedListener(this);
        _udpService.setDatagramListener(this);
        _udpService.startServer();
    }

    @Override
    public void stopDiscovery() {
        if (_discoveryTicker != null) {
            _discoveryTicker.stop();
            _discoveryTicker = null;
        }
        _udpService.setDatagramListener(null);
        _udpService.setStartedListener(null);
        _udpService.stopServer();
    }

    @Override
    public void started() {
        _discoveryTicker = createRediscoverTicker();
    }

    private Ticker createRediscoverTicker() {
        return new Ticker(new Ticker.Listener() {
            @Override
            public void onTick() {
                _udpService.discover();
            }

            @Override
            public void onElapsed() {
            }
        }, 4, 5000);
    }

    @Override
    public void datagramReceived(final DatagramPacket datagram) {
        if (_listener != null && DatagramProcessor.isMeDatagram(datagram)) {
            _listener.onFound(datagram.getAddress(), DatagramProcessor.getFirmwareVariant(datagram),
                    DatagramProcessor.getBoardId(datagram), DatagramProcessor.getRestoreTokenPart(datagram));
        }
    }
}
