package eu.geekhome.asymptote.services;

import com.google.firebase.FirebaseNetworkException;

public class CloudException extends Exception {
    private boolean _implicatesSecurity;
    private boolean _isNetworkException;

    public CloudException(Exception baseException, boolean implicatesSecurity) {
        super(baseException);
        _isNetworkException = baseException instanceof FirebaseNetworkException;
        _implicatesSecurity = implicatesSecurity;
    }

    public CloudException(String message, boolean implicatesSecurity) {
        super(message);
        _implicatesSecurity = implicatesSecurity;
    }

    public boolean isImplicatesSecurity() {
        return _implicatesSecurity;
    }

    public boolean isNetworkException() {
        return _isNetworkException;
    }
}
