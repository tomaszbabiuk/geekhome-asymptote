package eu.geekhome.asymptote.services.impl;

import java.io.IOException;
import java.security.MessageDigest;

import java.security.cert.X509Certificate;

import eu.geekhome.asymptote.services.CloudCertificateChecker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FirebaseCertificateChecker implements CloudCertificateChecker {

    private final OkHttpClient _client;
    private final String _address;

    public FirebaseCertificateChecker(String address) {
        _address = address;
        _client = new OkHttpClient.Builder().build();
    }


    @Override
    public void check(final Listener listener) {
        if (listener != null) {
            Request request = new Request.Builder()
                    .url(_address + "/public.json")
                    .build();

            _client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    X509Certificate peerCertificate = (X509Certificate) response.handshake().peerCertificates().get(0);
                    byte[] sha1Thumbprint = getThumbPrint(peerCertificate);
                    listener.onChecked(sha1Thumbprint);
                }
            });
        }
    }

    private static byte[] getThumbPrint(X509Certificate cert) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] der = cert.getEncoded();
            md.update(der);
            return md.digest();
        } catch (Exception ex) {
            return null;
        }
    }
}
