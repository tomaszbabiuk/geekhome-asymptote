package eu.geekhome.asymptote.model;


public class RGBValue {

    private final PWMValue _red;
    private final PWMValue _green;
    private final PWMValue _blue;

    public RGBValue(PWMValue red, PWMValue green, PWMValue blue) {
        _red = red;
        _green = green;
        _blue = blue;
    }

    public PWMValue getRed() {
        return _red;
    }

    public PWMValue getGreen() {
        return _green;
    }

    public PWMValue getBlue() {
        return _blue;
    }
}
