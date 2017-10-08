package eu.geekhome.asymptote.model;

public enum AutomationUnit {
    Relay(0),
    Impulse(1),
    Temperature(2),
    Humidity(3),
    Unknown(255);

    private final int _code;

    AutomationUnit(int code) {
        _code = code;
    }

    public int toInt() {
        return _code;
    }

    public static AutomationUnit fromInt(int unitAsInt) {
        for(AutomationUnit value : values()) {
            if (value.toInt() == unitAsInt) {
                return value;
            }
        }

        return Unknown;
    }
}
