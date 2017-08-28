package eu.geekhome.asymptote.model;

public class FirmwareSet {
    private final Firmware _wifiFirmware;
    private final Firmware _firebaseFirmware;
    private Firmware _hybridFirmware;

    public FirmwareSet(Firmware wifiFirmware, Firmware firebaseFirmware, Firmware hybridFirmware) {

        _wifiFirmware = wifiFirmware;
        _firebaseFirmware = firebaseFirmware;
        _hybridFirmware = hybridFirmware;
    }

    public Firmware getWifiFirmware() {
        return _wifiFirmware;
    }

    public Firmware getFirebaseFirmware() {
        return _firebaseFirmware;
    }

    public Firmware getHybridFirmware() {
        return _hybridFirmware;
    }
}
