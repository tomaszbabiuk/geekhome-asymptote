package eu.geekhome.asymptote.model;

public enum BoardMode {
    MANUAL(0),
    TIMERS(1);

    private final int _id;

    BoardMode(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }

    public static BoardMode fromInt(int roleAsInt) {
        for(BoardMode value : values()) {
            if (value.getId() == roleAsInt) {
                return value;
            }
        }

        return MANUAL;
    }
}
