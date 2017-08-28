package eu.geekhome.asymptote.utils;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.BoardNotSupportedException;
import eu.geekhome.asymptote.model.Variant;

public class DatagramProcessor {
    private static final byte UDP_PROTOCOL_MATCH = 0x01;
    private static final byte UDP_COMMAND_DISCOVER = (byte)0x01;
    private static final byte UDP_COMMAND_SYNC = (byte)0x02;
    private static final byte UDP_COMMAND_OTARESTORE = (byte)0x03;
    private static final byte UDP_COMMAND_CERTRESTORE = (byte)0x04;

    private static final byte UDP_RESPONSE_ME = (byte)0x10;
    private static final byte UDP_RESPONSE_ME_WITH_RESTORE_TOKEN = (byte)0x11;

    public byte[] createDiscoverCommand() {
        return new byte[] {
                UDP_PROTOCOL_MATCH,
                UDP_COMMAND_DISCOVER,
        };
    }

    public byte[] createSyncCommand() {
        return new byte[] {
                UDP_PROTOCOL_MATCH,
                UDP_COMMAND_SYNC,
        };
    }



    public byte[] createCertificateUpdateDatagram(String restoreToken, String fingerprint) throws UnsupportedEncodingException {
        byte[] fingerprintBytes = ByteUtils.hexStringToBytes(fingerprint);
        byte[] hashBytes = ByteUtils.createHmacSHA256(restoreToken, fingerprint.toLowerCase());
        if (hashBytes == null) {
            throw new IllegalArgumentException("Cannot generate hash from restore token and fingerprint!");
        }
        return new byte[] {
                UDP_PROTOCOL_MATCH,
                UDP_COMMAND_CERTRESTORE,
                fingerprintBytes[0],
                fingerprintBytes[1],
                fingerprintBytes[2],
                fingerprintBytes[3],
                fingerprintBytes[4],
                fingerprintBytes[5],
                fingerprintBytes[6],
                fingerprintBytes[7],
                fingerprintBytes[8],
                fingerprintBytes[9],
                fingerprintBytes[10],
                fingerprintBytes[11],
                fingerprintBytes[12],
                fingerprintBytes[13],
                fingerprintBytes[14],
                fingerprintBytes[15],
                fingerprintBytes[16],
                fingerprintBytes[17],
                fingerprintBytes[18],
                fingerprintBytes[19],
                hashBytes[0],
                hashBytes[1],
                hashBytes[2],
                hashBytes[3],
                hashBytes[4],
                hashBytes[5],
                hashBytes[6],
                hashBytes[7],
                hashBytes[8],
                hashBytes[9],
                hashBytes[10],
                hashBytes[11],
                hashBytes[12],
                hashBytes[13],
                hashBytes[14],
                hashBytes[15],
                hashBytes[16],
                hashBytes[17],
                hashBytes[18],
                hashBytes[19],
                hashBytes[20],
                hashBytes[21],
                hashBytes[22],
                hashBytes[23],
                hashBytes[24],
                hashBytes[25],
                hashBytes[26],
                hashBytes[27],
                hashBytes[28],
                hashBytes[29],
                hashBytes[30],
                hashBytes[31],
        };
    }

    public byte[] createOtaRestoreDatagram(String restoreToken, String md5) throws UnsupportedEncodingException {
        byte[] md5Bytes = ByteUtils.hexStringToBytes(md5);
        byte[] hashBytes = ByteUtils.createHmacSHA256(restoreToken, md5);
        if (hashBytes == null) {
            throw new IllegalArgumentException("Cannot generate hash from restore token and md5!");
        }
        return new byte[] {
                UDP_PROTOCOL_MATCH,
                UDP_COMMAND_OTARESTORE,
                md5Bytes[0],
                md5Bytes[1],
                md5Bytes[2],
                md5Bytes[3],
                md5Bytes[4],
                md5Bytes[5],
                md5Bytes[6],
                md5Bytes[7],
                md5Bytes[8],
                md5Bytes[9],
                md5Bytes[10],
                md5Bytes[11],
                md5Bytes[12],
                md5Bytes[13],
                md5Bytes[14],
                md5Bytes[15],
                hashBytes[0],
                hashBytes[1],
                hashBytes[2],
                hashBytes[3],
                hashBytes[4],
                hashBytes[5],
                hashBytes[6],
                hashBytes[7],
                hashBytes[8],
                hashBytes[9],
                hashBytes[10],
                hashBytes[11],
                hashBytes[12],
                hashBytes[13],
                hashBytes[14],
                hashBytes[15],
                hashBytes[16],
                hashBytes[17],
                hashBytes[18],
                hashBytes[19],
                hashBytes[20],
                hashBytes[21],
                hashBytes[22],
                hashBytes[23],
                hashBytes[24],
                hashBytes[25],
                hashBytes[26],
                hashBytes[27],
                hashBytes[28],
                hashBytes[29],
                hashBytes[30],
                hashBytes[31],
        };
    }


    public static boolean isMeDatagram(DatagramPacket datagram) {
        boolean isMeDatagram = datagram.getLength() == 4 && datagram.getData()[1] == UDP_RESPONSE_ME;
        boolean isMeWithRestoreTokenDatagram = datagram.getLength() == 12 && datagram.getData()[1] == UDP_RESPONSE_ME_WITH_RESTORE_TOKEN;

        return isMeDatagram || isMeWithRestoreTokenDatagram;
    }

    public static Byte[] getRestoreTokenPart(DatagramPacket datagram) {
        if (isMeDatagram(datagram)) {
            return new Byte[] {
                    datagram.getData()[4],
                    datagram.getData()[5],
                    datagram.getData()[6],
                    datagram.getData()[7],
                    datagram.getData()[8],
                    datagram.getData()[9],
                    datagram.getData()[10],
                    datagram.getData()[11]
            };
        }
        return new Byte[0];
    }

    public static Variant getFirmwareVariant(DatagramPacket datagram) {
        if (isMeDatagram(datagram)) {
            return Variant.fromInt(datagram.getData()[2]);
        }

        return Variant.Unknown;
    }

    public static BoardId getBoardId(DatagramPacket datagram) {
        if (isMeDatagram(datagram)) {
            try {
                return BoardId.fromInt(datagram.getData()[3]);
            } catch (Exception ex) {
                return null;
            }
        }

        return  null;
    }

}
