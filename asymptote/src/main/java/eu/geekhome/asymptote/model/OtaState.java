package eu.geekhome.asymptote.model;

public enum OtaState {
    //general
    StateSuccess((byte)0, "STATE SUCCESS", false),
    StatePrompt((byte)1, "STATE PROMPT", false),
    StateProgress((byte)2, "STATE PROGRESS", false),
    StateUnknown((byte)10, "STATE UNKNOWN", true),

    //general
    ErrorNoLock((byte)100, "NO LOCK", true),

    //arduino http errors
    HttpErrorConnectionRefused((byte)-1, "HTTP ERROR CONNECTION REFUSED", true),
    HttpErrorSendHeaderFailed((byte)-2, "HTTP ERROR SEND HEADER FAILED", true),
    HttpErrorSendPayloadFailed((byte)-3, "HTTP ERROR SEND PAYLOAD FAILED", true),
    HttpErrorNotConnected((byte)-4, "HTTP ERROR NOT CONNECTED", true),
    HttpErrorConnectionLost((byte)-5, "HTTP ERROR CONNECTION LOST", true),
    HttpErrorNoStream((byte)-6, "HTTP ERROR NO STREAM", true),
    HttpErrorNoHttpServer((byte)-7, "HTTP ERROR NO HTTP SERVER", true),
    HttpErrorTooLessRam((byte)-8, "HTTP ERROR TOO LESS RAM", true),
    HttpErrorEncoding((byte)-9, "HTTP ERROR ENCODING", true),
    HttpErrorStreamWrite((byte)-10, "HTTP ERROR STREAM WRITE", true),
    HttpErrorReadTimeout((byte)-11, "HTTP ERROR READ TIMEOUT", true),

    //arduino ota errors
    OtaErrorTooLessSpace((byte)-100, "OTA ERROR TOO LESS SPACE", true),
    OtaErrorServerNotReportSize((byte)-101, "OTA ERROR SERVER NOT REPORT SIZE", true),
    OtaErrorServerFileNotFound((byte)-102, "OTA ERROR SERVER FILE NOT FOUND", true),
    OtaErrorServerForbidden((byte)-103, "OTA ERROR SERVER FORBIDDEN", true),
    OtaErrorServerWrongHttpCode((byte)-104, "OTA ERROR SERVER WRING HTTP CODE", true),
    OtaErrorServerFaultyMd5((byte)-105, "OTA ERROR SERVER FAULTY MD5", true),
    OtaErrorServerBinForWrongFlash((byte)-106, "OTA ERROR SERVER BIN FOR WRONG FLASH", true),
    OtaErrorServerBinVerifyHeaderFailed((byte)-107, "OTA ERROR SERVER BIN VERIFY HEADER FAILED", true),
    OtaErrorServerInvalidChecksum((byte)-108, "OTA ERROR SERVER INVALID CHECKSUM", true),
    OtaErrorServerInvalidSignature((byte)-109, "OTA ERROR INVALID SIGNATURE", true);

    private byte _errorNumber;
    private String _errorCode;
    private boolean _error;

    OtaState(byte errorNumber, String errorCode, boolean error) {

        _errorNumber = errorNumber;
        _errorCode = errorCode;
        _error = error;
    }

    public byte getErrorNumber() {
        return _errorNumber;
    }

    public String getErrorCode() {
        return _errorCode;
    }

    public static OtaState fromByte(byte otaStatedAsInt) {
        for(OtaState value : values()) {
            if (value.getErrorNumber() == otaStatedAsInt) {
                return value;
            }
        }

        return OtaState.StateUnknown;
    }

    public boolean isError() {
        return _error;
    }
}
