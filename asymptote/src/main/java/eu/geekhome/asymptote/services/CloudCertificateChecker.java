package eu.geekhome.asymptote.services;

import java.io.IOException;

public interface CloudCertificateChecker {
    interface Listener {
        void onChecked(byte[] sha1Thumbprint);
        void onError(IOException e);
    }

    void check(Listener listener);
}
