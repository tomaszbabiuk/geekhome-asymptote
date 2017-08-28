package eu.geekhome.asymptote.services;

import android.support.annotation.WorkerThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;


public interface UdpService {
    @WorkerThread
    void sendDatagram(byte[] data, InetAddress address) throws IOException;

    @WorkerThread
    void sendBroadcast(byte[] data) throws IOException;

    void startServer();
    void stopServer();
    void discover();

    void sync(InetAddress address);
    void requestOtaRestore(InetAddress address, String restoreToken, String md5);
    void requestCertRestore(InetAddress address, String restoreToken, String fingerprint);

    void setStartedListener(UdpService.StartedListener listener);
    void setDatagramListener(UdpService.DatagramListener listener);

    interface StartedListener {
        void started();
    }

    interface DatagramListener {
        void datagramReceived(DatagramPacket datagram);
    }
}
