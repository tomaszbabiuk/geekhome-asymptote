package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.UdpService;
import eu.geekhome.asymptote.utils.DatagramProcessor;
import eu.geekhome.asymptote.utils.UdpUtils;

public class AndroidUdpService implements UdpService {
    private static final Logger _logger = LoggerFactory.getLogger(AndroidUdpService.class);
    private final DatagramProcessor _datagramProcessor;
    private ThreadRunner _threadRunner;
    private InetAddress _broadcastAddress;
    private Thread _serverThread;
    private UdpService.StartedListener _startedListener;
    private UdpService.DatagramListener _datagramListener;
    private DatagramSocket _socket;
    private boolean _stopped;

    public AndroidUdpService(Context context, ThreadRunner threadRunner) {
        _datagramProcessor = new DatagramProcessor();
        _threadRunner = threadRunner;
        try {
            _broadcastAddress = getBroadcastAddress(context);
        } catch (IOException ignored) {
        }
    }

    public void sendDatagram(byte[] data, InetAddress address) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        try {
            socket.setBroadcast(false);
            DatagramPacket datagram = new DatagramPacket(data, data.length, address, 8888);
            _logger.info("Sending datagram: " + UdpUtils.datagramToString(datagram));
            socket.send(datagram);
        } finally {
            socket.close();
        }
    }

    public void sendBroadcast(byte[] data) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        try {
            socket.setBroadcast(true);
            DatagramPacket datagram = new DatagramPacket(data, data.length, _broadcastAddress, 8888);
            _logger.info("Sending broadcast: " + UdpUtils.datagramToString(datagram));
            socket.send(datagram);
        } finally {
            socket.close();
        }
    }

    private InetAddress getBroadcastAddress(Context context) throws IOException {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null) {
            throw new IOException("Cannot get DHCP info!");
        }

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));
        return InetAddress.getByAddress(quads);
    }

    @Override
    public void discover() {
        _threadRunner.runInBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] reportBroadcast = _datagramProcessor.createDiscoverCommand();
                    sendBroadcast(reportBroadcast);
                } catch (Exception e) {
                    _logger.error(e.toString());
                }
            }
        });
    }

    @Override
    public void sync(final InetAddress address) {
        _threadRunner.runInBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] reportBroadcast = _datagramProcessor.createSyncCommand();
                    sendDatagramToDevice(address, reportBroadcast);
                } catch (Exception e) {
                    _logger.error(e.toString());
                }
            }
        });
    }

    @Override
    public void requestOtaRestore(final InetAddress address, final String restoreToken, final String md5) {
        _threadRunner.runInBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] datagram = _datagramProcessor.createOtaRestoreDatagram(restoreToken, md5);
                    sendDatagramToDevice(address, datagram);
                } catch (Exception e) {
                    _logger.error(e.toString());
                }
            }
        });
    }

    @Override
    public void requestCertRestore(final InetAddress address, final String restoreToken, final String fingerprint) {
        _threadRunner.runInBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] datagram = _datagramProcessor.createCertificateUpdateDatagram(restoreToken, fingerprint);
                    sendBroadcast(datagram);
                } catch (Exception e) {
                    _logger.error(e.toString());
                }
            }
        });
    }

    private void sendDatagramToDevice(final InetAddress address, final byte[] datagram) {
        _threadRunner.runInBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    sendDatagram(datagram, address);
                } catch (Exception e) {
                    _logger.error(e.toString());
                }
            }
        });
    }

    private void recreateServerThread() {
        _serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
                    _socket.setBroadcast(true);
                    onStarted();
                    while (!_stopped) {
                        try {
                            byte[] buffer = new byte[32];
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                            _socket.setSoTimeout(1000);
                            _socket.receive(packet);
                            _logger.info("Datagram received: " + UdpUtils.datagramToString(packet));
                            onDatagramReceived(packet);
                        } catch (SocketTimeoutException ignored) {
                        }
                    }
                    _socket.close();
                } catch (IOException ex) {
                    _logger.error("Error creating UDP Server", ex);
                }
                _serverThread = null;
            }
        });
    }

    private void onDatagramReceived(DatagramPacket packet) {
        if (_datagramListener != null) {
            _datagramListener.datagramReceived(packet);
        }
    }

    private void onStarted() {
        if (_startedListener != null) {
            _startedListener.started();
        }
    }

    public void setStartedListener(UdpService.StartedListener listener) {
        _startedListener = listener;
    }

    public void setDatagramListener(UdpService.DatagramListener listener) {
        _datagramListener = listener;
    }

    public void startServer() {
        while (_serverThread != null) {
            _stopped = true;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        _stopped = false;
        recreateServerThread();
        _serverThread.start();
    }

    public void stopServer() {
        _stopped = true;
    }

}
