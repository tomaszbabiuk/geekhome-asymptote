package eu.geekhome.asymptote.utils;

import java.net.DatagramPacket;
import java.util.Arrays;

public class UdpUtils {
    public static String datagramToString(DatagramPacket datagram) {
        byte[] data = Arrays.copyOf(datagram.getData(), datagram.getLength());
        return String.format("[%s] from: [%s]", ByteUtils.bytesToHex(data), datagram.getAddress());
    }
}
