package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.services.LocalDiscoveryService;

class DnsSdService implements LocalDiscoveryService {
    private static final Logger _logger = LoggerFactory.getLogger(LocalDiscoveryService.class);
    private static final String SERVICE_TYPE = "_http._tcp.";
    private final NsdManager _manager;
    private NsdManager.DiscoveryListener _discoveryListener;
    private Listener _listener;

    DnsSdService(Context context) {
        _manager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    @Override
    public void startDiscovery() {
        _discoveryListener = initializeDiscoveryListener();
        _manager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, _discoveryListener);
    }

    @Override
    public void stopDiscovery() {
        if (_discoveryListener != null) {
            _manager.stopServiceDiscovery(_discoveryListener);
            _discoveryListener = null;
        }
    }

    private NsdManager.DiscoveryListener initializeDiscoveryListener() {
        return new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                _logger.debug("Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                _logger.debug("Service discovery success" + service);
                if (service.getServiceName().startsWith("GH_")) {
                    _manager.resolveService(service, new NsdManager.ResolveListener() {
                        @Override
                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                            _logger.debug("Service resolving failed");
                        }

                        @Override
                        public void onServiceResolved(NsdServiceInfo serviceInfo) {
                            _logger.debug("Service resolved");
                            if (_listener != null) {
                                _listener.onFound(serviceInfo.getHost(), Variant.Unknown, null,  new Byte[0]);
                            }
                        }
                    });
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                _logger.debug("service lost" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                _logger.debug("Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                _logger.debug("Discovery failed: Error code:" + errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                _logger.debug("Discovery failed: Error code:" + errorCode);
            }
        };
    }

    public Listener getListener() {
        return _listener;
    }

    @Override
    public void setListener(Listener listener) {
        _listener = listener;
    }
}

