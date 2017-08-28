package eu.geekhome.asymptote.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Utils {
    public static String encodeNameToBase64(String name) {
        try {
            return new String(Base64.encode(name.getBytes("UTF-8"), Base64.NO_WRAP));
        } catch (UnsupportedEncodingException ignored) {
        }

        return null;
    }

    public static String decodeNameFromBase64(String nameEncoded) {
        try {
            return new String(Base64.decode(nameEncoded.getBytes("UTF-8"), Base64.NO_WRAP));
        } catch (UnsupportedEncodingException ignored) {
        }

        return null;
    }
}
