package eu.geekhome.asymptote.model;

public class Firmware {
    private final Variant _variant;
    private final String _location;
    private final String _md5;
    private int _versionMajor;
    private int _versionMinor;


    public String getLocation() {
        return _location;
    }

    public String getMd5() {
        return _md5;
    }

    public int getVersionMajor() {
        return _versionMajor;
    }

    public int getVersionMinor() {
        return _versionMinor;
    }

    public Firmware(Variant variant, String location, String md5, int versionMajor, int versionMinor) {
        _variant = variant;
        _location = location;
        _md5 = md5;
        _versionMajor = versionMajor;
        _versionMinor = versionMinor;
    }

    public Variant getVariant() {
        return _variant;
    }
}
