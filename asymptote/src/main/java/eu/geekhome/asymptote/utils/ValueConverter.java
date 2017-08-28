package eu.geekhome.asymptote.utils;


import java.util.Locale;

public class ValueConverter {
    public static String intToCelsius(Integer value) {
        if (value == null) {
            return null;
        }

        return intToValue(value - 27315, "°C");
    }

    public static String intToHumidity(Integer value) {
        return intToValue(value, "%");
    }

    public static String intToLuminosity(Integer value) {
        return intToValue(value, "lux");
    }

    public static String intToNoise(Integer value) {
        return intToValue(value, "%");
    }

    public static String intToDust(Integer value) {
        return intToValue(value, "µg/m3");
    }


    private static String intToValue(Integer value, String unit) {
        if (value != null) {
            String asString = String.valueOf(value);
            String dec;
            String frac;
            if (asString.length() < 3) {
                dec = "0";
                frac = asString;
            } else {
                dec = asString.substring(0, asString.length() - 2);
                frac = asString.substring(asString.length() - 2);
            }

            return dec + "." + frac + " " + unit;
        }

        return null;
    }

    public static String secondsToText(Long value) {
        long hourOfDay = Math.abs(value / 3600);
        long minutes = Math.abs(value % 3600 / 60);
        long secs = Math.abs(value % 60);
        return String.format(Locale.US, "%02d:%02d:%02d", hourOfDay, minutes, secs);
    }

    public static String secondsToPrettyText(Long value) {
        if (value > 3600) {
            return String.format(Locale.US,"> %dh", value /3600);
        } else if (value > 60) {
            return String.format(Locale.US,"> %dm", value /60);
        }

        return "< min";
    }
}
