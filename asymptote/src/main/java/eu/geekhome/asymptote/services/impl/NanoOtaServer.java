package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.content.res.AssetManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.services.FirmwareRepository;
import eu.geekhome.asymptote.services.OtaServer;
import fi.iki.elonen.NanoHTTPD;

public class NanoOtaServer extends NanoHTTPD implements OtaServer {
    private final static int PORT = 8890;
    private static final Logger _logger = LoggerFactory.getLogger(NanoOtaServer.class);
    private Context _context;
    private DeviceSyncData _syncData;
    private Listener _listener;
    private Firmware _firmware;

    public NanoOtaServer(Context context) throws Exception {
        super(PORT);
        _context = context;
        _logger.info("OTA server running on port " + PORT);
    }

    @Override
    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void start(DeviceSyncData syncData, Firmware firmware) throws IOException {
        _syncData = syncData;
        _firmware = firmware;
        start();
    }

    @Override
    public boolean isRunning() {
        return isAlive();
    }

    @Override
    public Response serve(IHTTPSession session) {
        AssetManager assetsManager = _context.getAssets();
        InputStream is;
        try {
            if (_listener != null) {
                _listener.downloadStarted();
            }

            is = assetsManager.open(_firmware.getLocation());
            long length = is.available();
            Response response = newFixedLengthResponse(Response.Status.OK, "application/ocet-stream", is, length);
            response.addHeader("x-MD5", _firmware.getMd5());
            return response;
        } catch (IOException e) {
            String msg = "<html><body><h1>Update server error</h1>\n";
            msg += "<p>"+ e.toString() +"</p>";
            return newFixedLengthResponse(msg + "</body></html>\n");
        }
    }
}
