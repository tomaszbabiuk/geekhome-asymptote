package eu.geekhome.asymptote.model;

public enum AutomationUnit {
    Relay(0),
    Impulse(1),
    Temperature(2),
    Humidity(3),
    Pwm(4),
    Rgb(5),
    Gate(6),
    Remote(7),
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
