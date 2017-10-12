package eu.geekhome.asymptote.utils;


import android.content.Context;

import java.util.Locale;

import eu.geekhome.asymptote.R;

public class ValueConverter {
    public static String intToCelsius(Integer value) {
        if (value == null) {
            return null;
        }

        return intToValue(value - 27315, "°C");
    }

    public static int celsiusToInt(String value) {
        return Integer.valueOf(value.replace(" °C", "").replace(".00","").replace(".50","")) * 100 + 27315;
    }

    public static int humidityToInt(String value) {
        return Integer.valueOf(value.replace(" %", "").replace(".00","").replace(".50","")) * 100;
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

    public static String minutesToText(Long value) {
        long hourOfDay = Math.abs(value / 3600);
        long minutes = Math.abs(value % 3600 / 60);
        return String.format(Locale.US, "%02d:%02d", hourOfDay, minutes);
    }

    public static String daysToText(Long value) {
        long year = Math.abs(value / (31 * 12));
        long month = Math.abs(value % (31 * 12) / 31);
        long day = Math.abs(value % 31);
        return String.format(Locale.US, "%04d-%02d-%02d", year, month, day);
    }

    public static String secondsToPrettyText(Long value) {
        if (value > 3600) {
            return String.format(Locale.US,"> %dh", value /3600);
        } else if (value > 60) {
            return String.format(Locale.US,"> %dm", value /60);
        }

        return "< min";
    }

    public static String daysToNames(Context context, int days) {
        StringBuilder daysTextBuilder = new StringBuilder();
        if (ByteUtils.getBit(days, 0)) {
            daysTextBuilder.append(context.getString(R.string.at_monday)).append(", ");
        }
        if (ByteUtils.getBit(days, 1)) {
            daysTextBuilder.append(context.getString(R.string.at_tuesday)).append(", ");
        }
        if (ByteUtils.getBit(days, 2)) {
            daysTextBuilder.append(context.getString(R.string.at_wednesday)).append(", ");
        }
        if (ByteUtils.getBit(days, 3)) {
            daysTextBuilder.append(context.getString(R.string.at_thursday)).append(", ");
        }
        if (ByteUtils.getBit(days, 4)) {
            daysTextBuilder.append(context.getString(R.string.at_friday)).append(", ");
        }
        if (ByteUtils.getBit(days, 5)) {
            daysTextBuilder.append(context.getString(R.string.at_saturday)).append(", ");
        }
        if (ByteUtils.getBit(days, 6)) {
            daysTextBuilder.append(context.getString(R.string.sunday)).append(", ");
        }

        return daysTextBuilder.toString();
    }
}
