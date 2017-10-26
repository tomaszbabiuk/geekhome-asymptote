package eu.geekhome.asymptote.model;


public enum BoardId {
    SonoffPOW(0x00),
    SonoffSC(0x01),
    SonoffDUAL(0x02),
    SonoffTOUCH(0x03),
    Sonoff4CH(0x04),
    SonoffTH_None(0x05),
    SonoffTH_AM2301(0x06),
    SonoffTH_DS18B20(0x07),
    Elecrodragon2REL(0x08),
    H801(0x0A),
    H802(0x0B),
    SonoffBASIC(0x0C),
    ElectrodragonVDC(0x0D),
    geekGATE(0x0E),
    ElectrodragonLed(0x0F),
    Electrodragon2REL_None(0x10),
    Electrodragon2REL_AM2301(0x11),
    Electrodragon2REL_DS18B20(0x12);

    private int _id;


    BoardId(int id) {
        _id = id;
    }

    public int toInt() {
        return _id;
    }

    public static BoardId fromInt(int boardIdAsInt) throws BoardNotSupportedException {
        for(BoardId value : values()) {
            if (value.toInt() == boardIdAsInt) {
                return value;
            }
        }

        throw new BoardNotSupportedException(boardIdAsInt);
    }
}
