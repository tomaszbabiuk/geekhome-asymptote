package eu.geekhome.asymptote.model;


public enum BoardId {
    //asymptote light controller (works with any discontinuation switch)
    //1-ch remote switch with energy meter
    //geekhome slave device (1-ch output, 1-ch energy meter input)
    SonoffPOW(0x00),

    //multisensor
    //geekhome slave device (temperature/humidity/dust/light/noise input)
    SonoffSC(0x01),

    //2-ch remote switch
    //geekhome slave device (2-ch output)
    SonoffDUAL(0x02),

    //light switch
    //notify button and 1-ch remote switch
    //geekhome slave device (1-ch input, 1-ch output)
    //geekhome slave device (1-ch output, manually switchable)
    SonoffTOUCH(0x03),

    //4-ch remote switch
    //geekhome slave device (4-ch output)
    Sonoff4CH(0x04),

    //1-ch remote switch
    //geekhome slave device (1-ch output)
    SonoffTH_None(0x05),

    //1-ch remote switch and comfortmeter
    //heating thermostat
    //cooling thermostat
    //heating higrostat
    //cooling higrostat
    //heating comfortmeter
    //cooling comfortmeter
    //geekhome slave device (1-ch output, temperature input, humidity input)
    SonoffTH_AM2301(0x06),

    //1-ch remote switch and thermometer
    //heating thermostat
    //cooling thermostat
    //geekhome slave device (1-ch output, temperature input, humidity input)
    SonoffTH_DS18B20(0x07),

    //2-ch remote switch
    //geekhome slave device (2-ch output)
    Elecrodragon2REL(0x08),

    //2-ch remote spdt switch
    //open/close gate opener (with optional contactron)
    //push gate opener (with optional contactron)
    //blinds controller
    //geekhome slave device (2-ch output)
    Elecrodragon2REL_SPDT(0x09),

    //1-rgb + 2-pwm channels controller
    //5-pwm channels controller
    //geekhome slave device (5-ch pwm output)
    H801(0x0A),


    //1-rgb + 1-pwm channels controller
    //4-pwm channels controller
    //geekhome slave device (4-ch pwm output)
    H802(0x0B),

    //1-ch remote switch
    //geekhome slave device (1-ch output)
    SonoffBASIC(0x0C);


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
