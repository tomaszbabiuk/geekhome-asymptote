package eu.geekhome.asymptote.services.impl;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationSyncUpdate;
import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.BoardMode;
import eu.geekhome.asymptote.model.BoardNotSupportedException;
import eu.geekhome.asymptote.model.BoardRole;
import eu.geekhome.asymptote.model.CloudFingerprintSyncUpdate;
import eu.geekhome.asymptote.model.CloudPasswordSyncUpdate;
import eu.geekhome.asymptote.model.CloudUsernameSyncUpdate;
import eu.geekhome.asymptote.model.ColorSyncUpdate;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.DeleteAutomationSyncUpdate;
import eu.geekhome.asymptote.model.DeviceKey;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.NameSyncUpdate;
import eu.geekhome.asymptote.model.OtaHashSyncUpdate;
import eu.geekhome.asymptote.model.OtaHostSyncUpdate;
import eu.geekhome.asymptote.model.OtaState;
import eu.geekhome.asymptote.model.PWMImpulseSyncUpdate;
import eu.geekhome.asymptote.model.PWMSyncUpdate;
import eu.geekhome.asymptote.model.PWMValue;
import eu.geekhome.asymptote.model.ParamSyncUpdate;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.model.RGBSyncUpdate;
import eu.geekhome.asymptote.model.RGBValue;
import eu.geekhome.asymptote.model.RelayImpulseSyncUpdate;
import eu.geekhome.asymptote.model.RelaySyncUpdate;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.model.RestoreTokenSyncUpdate;
import eu.geekhome.asymptote.model.RoleSyncUpdate;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.model.StateSyncUpdate;
import eu.geekhome.asymptote.model.SyncUpdate;
import eu.geekhome.asymptote.model.SystemInformation;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.LocalDiscoveryService;
import eu.geekhome.asymptote.services.SyncListener;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.SyncSource;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.utils.AutomationBuilder;
import eu.geekhome.asymptote.utils.Base64Utils;
import eu.geekhome.asymptote.utils.ByteUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpClientSyncManager implements SyncManager, LocalDiscoveryService.Listener, Runnable {
    private final Gson _gson;
    private final OkHttpClient _client;
    private LocalDiscoveryService _discoveryService;
    private AddressesPersistenceService _persistenceService;
    private ThreadRunner _threadRunner;
    private EmergencyManager _emergencyManager;
    private final AutomationBuilder _automationBuilder;
    private SyncListener _listener;

    @SuppressWarnings("deprecation")
    public HttpClientSyncManager(Context context, LocalDiscoveryService discoveryService,
                                 AddressesPersistenceService persistenceService, ThreadRunner threadRunner,
                                 EmergencyManager emergencyManager, AutomationBuilder automationBuilder) throws Exception {
        _discoveryService = discoveryService;
        _persistenceService = persistenceService;
        _threadRunner = threadRunner;
        _emergencyManager = emergencyManager;
        _automationBuilder = automationBuilder;
        _gson = new Gson();

        //load certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream in = context.getResources().openRawResource(R.raw.ca_x509);
        InputStream caInput = new BufferedInputStream(in);
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext sc = SSLContext.getInstance("TLSv1.2");

        sc.init(null, tmf.getTrustManagers(), null);
        SSLSocketFactory sslSocketFactory = sc.getSocketFactory();
        HostnameVerifier hostNameVerifier = new HostnameVerifier() {
            @SuppressLint("BadHostnameVerifier")
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        _client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory)
                .hostnameVerifier(hostNameVerifier)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        if (_emergencyManager.getPassword() != null) {
                            Request request = chain.request();
                            Request newRequest;

                            newRequest = request.newBuilder()
                                    .header("g-lockpass", _emergencyManager.getPassword())
                                    .build();
                            return chain.proceed(newRequest);
                        } else {
                            return chain.proceed(chain.request());
                        }
                    }
                })
                .build();
    }

    @Override
    public void start() throws IOException {
        _discoveryService.setListener(this);
        _discoveryService.startDiscovery();
        _threadRunner.runInBackground(this);
    }

    @Override
    public void stop() {
        _discoveryService.setListener(null);
        _discoveryService.stopDiscovery();
        _persistenceService.save();
    }

    @Override
    public void setSyncListener(SyncListener listener) {
        _listener = listener;
    }


    private String updateToQuery(SyncUpdate update) {
        if (update instanceof RelaySyncUpdate) {
            RelaySyncUpdate relayUpdate = (RelaySyncUpdate) update;
            int channel = relayUpdate.getValue().getChannel();
            boolean state = relayUpdate.getValue().getState();
            return String.format(Locale.US, "rel%02X=%d", channel, state ? 1 : 0);
        }

        if (update instanceof RelayImpulseSyncUpdate) {
            RelayImpulseSyncUpdate relayImpulseUpdate = (RelayImpulseSyncUpdate) update;
            int channel = relayImpulseUpdate.getValue().getChannel();
            long impulse = relayImpulseUpdate.getValue().getImpulse();
            return String.format(Locale.US, "rimp%02X=%d", channel, impulse);
        }

        if (update instanceof PWMSyncUpdate) {
            PWMSyncUpdate pwmUpdate = (PWMSyncUpdate) update;
            int channel = pwmUpdate.getValue().getChannel();
            int duty = pwmUpdate.getValue().getDuty();
            return String.format(Locale.US, "pwm%02X=%d", channel, duty);
        }

        if (update instanceof RGBSyncUpdate) {
            RGBSyncUpdate rgbUpdate = (RGBSyncUpdate) update;
            int redChannel = rgbUpdate.getValue().getRed().getChannel();
            int greenChannel = rgbUpdate.getValue().getGreen().getChannel();
            int blueChannel = rgbUpdate.getValue().getBlue().getChannel();
            int redDuty = rgbUpdate.getValue().getRed().getDuty();
            int greenDuty = rgbUpdate.getValue().getGreen().getDuty();
            int blueDuty = rgbUpdate.getValue().getBlue().getDuty();
            return String.format(Locale.US, "rgb%02X%02X%02X=%02X%02X%02X",
                    redChannel, greenChannel, blueChannel,
                    redDuty, greenDuty, blueDuty);
        }

        if (update instanceof PWMImpulseSyncUpdate) {
            PWMImpulseSyncUpdate pwmImpulseUpdate = (PWMImpulseSyncUpdate) update;
            int channel = pwmImpulseUpdate.getValue().getChannel();
            long impulse = pwmImpulseUpdate.getValue().getImpulse();
            return String.format(Locale.US, "pimp%02X=%d", channel, impulse);
        }

        if (update instanceof ParamSyncUpdate) {
            ParamSyncUpdate paramUpdate = (ParamSyncUpdate) update;
            int index = paramUpdate.getValue().getIndex();
            long value = paramUpdate.getValue().getValue();
            return String.format(Locale.US, "param%02X=%d", index, value);
        }

        if (update instanceof NameSyncUpdate) {
            NameSyncUpdate nameUpdate = (NameSyncUpdate) update;
            return String.format("name=%s", Base64Utils.encodeNameToBase64(nameUpdate.getValue()));
        }

        if (update instanceof OtaHostSyncUpdate) {
            OtaHostSyncUpdate otaHostUpdate = (OtaHostSyncUpdate) update;
            return String.format("otaHost=%s", otaHostUpdate.getValue());
        }

        if (update instanceof OtaHashSyncUpdate) {
            OtaHashSyncUpdate otaHashUpdate = (OtaHashSyncUpdate) update;
            return String.format("otaHash=%s", otaHashUpdate.getValue());
        }

        if (update instanceof RestoreTokenSyncUpdate) {
            RestoreTokenSyncUpdate restoreTokenSyncUpdate = (RestoreTokenSyncUpdate) update;
            return String.format("restoreToken=%s", restoreTokenSyncUpdate.getValue());
        }

        if (update instanceof CloudUsernameSyncUpdate) {
            CloudUsernameSyncUpdate cloudUsernameSyncUpdate = (CloudUsernameSyncUpdate) update;
            return String.format("cloudUsername=%s", cloudUsernameSyncUpdate.getValue());
        }

        if (update instanceof CloudPasswordSyncUpdate) {
            CloudPasswordSyncUpdate cloudPasswordSyncUpdate = (CloudPasswordSyncUpdate) update;
            return String.format("cloudPassword=%s", cloudPasswordSyncUpdate.getValue());
        }

        if (update instanceof CloudFingerprintSyncUpdate) {
            CloudFingerprintSyncUpdate cloudFingerprintSyncUpdate = (CloudFingerprintSyncUpdate) update;
            return String.format("cloudFingerprint=%s", cloudFingerprintSyncUpdate.getValue());
        }

        if (update instanceof ColorSyncUpdate) {
            ColorSyncUpdate colorUpdate = (ColorSyncUpdate) update;
            return String.format(Locale.US, "color=%d", colorUpdate.getValue());
        }

        if (update instanceof RoleSyncUpdate) {
            RoleSyncUpdate roleUpdate = (RoleSyncUpdate) update;
            return String.format(Locale.US, "role=%d", roleUpdate.getValue().getId());
        }

        if (update instanceof StateSyncUpdate) {
            StateSyncUpdate stateUpdate = (StateSyncUpdate) update;
            return String.format(Locale.US, "state=%s", stateUpdate.getValue());
        }

        if (update instanceof DeleteAutomationSyncUpdate) {
            DeleteAutomationSyncUpdate deleteUpdate = (DeleteAutomationSyncUpdate) update;
            Automation automation = (Automation) deleteUpdate.getValue();
            return String.format(Locale.US, "deleteauto=%d", automation.getIndex());
        }

        return null;
    }

    @Override
    public void pushUpdates(DeviceSyncData syncData, List<SyncUpdate> updates,
                            final InetAddress address, final SyncCallback syncCallback) {
        Variant variant = syncData.getSystemInfo().getVariant();
        if (updates != null && updates.size() > 0) {
            for (SyncUpdate update : updates) {
                if (update instanceof AutomationSyncUpdate) {
                    AutomationSyncUpdate automationUpdate = (AutomationSyncUpdate) update;
                    Automation automation = (Automation) automationUpdate.getValue();
                    pushAutomation(variant, automation, address, syncCallback);
                }

                String query = updateToQuery(update);
                if (query != null) {
                    pushUpdateQuery(variant, query, address, syncCallback);
                }
            }
        }
    }

    @Override
    public void pushUpdatesAtOnce(DeviceSyncData syncData, List<SyncUpdate> updates, InetAddress address, SyncCallback syncCallback) {
        if (updates != null && updates.size() > 0) {
            boolean canRunUpdatesAtOnce = true;
            for (SyncUpdate update : updates) {
                if (update instanceof AutomationSyncUpdate) {
                    canRunUpdatesAtOnce = false;
                    break;
                }
            }

            if (canRunUpdatesAtOnce) {
                StringBuilder fullQuery = new StringBuilder();
                Variant variant = syncData.getSystemInfo().getVariant();
                for (int i = 0; i < updates.size(); i++) {
                    SyncUpdate update = updates.get(i);
                    String query = updateToQuery(update);
                    if (i > 0) {
                        fullQuery.append("&");
                    }
                    fullQuery.append(query);
                }

                pushUpdateQuery(variant, fullQuery.toString(), address, syncCallback);
            } else {
                pushUpdates(syncData, updates, address, syncCallback);
            }
        }
    }

    private void pushAutomation(Variant variant, Automation automation, final InetAddress address, final SyncCallback syncCallback) {
        String triggerQuery = "";
        String valueQuery = "";
        String type = "";

        String ix = String.format(Locale.US, "&index=%d&enabled=%d", automation.getIndex(), automation.isEnabled() ? 1 : 0);

        if (automation.getTrigger() instanceof DateTimeTrigger) {
            type = "type=dt&unit=" + automation.getUnit().toInt();
        }
        if (automation.getTrigger() instanceof SchedulerTrigger) {
            type = "type=sr&unit=" + automation.getUnit().toInt();
        }

        if (automation.getTrigger() instanceof DateTimeTrigger) {
            DateTimeTrigger dateTimeTrigger = (DateTimeTrigger) automation.getTrigger();
            triggerQuery = String.format(Locale.US, "&time=%d", dateTimeTrigger.getUtcTimestamp());
        }

        if (automation.getTrigger() instanceof SchedulerTrigger) {
            SchedulerTrigger schedulerTrigger = (SchedulerTrigger) automation.getTrigger();
            triggerQuery = String.format(Locale.US, "&days=%d&time=%d", schedulerTrigger.getDays(), schedulerTrigger.getTimeMark());
        }

        if (automation.getValue() instanceof RelayValue) {
            RelayValue relayValue = (RelayValue) automation.getValue();
            valueQuery = String.format(Locale.US, "&value=%d&param=%d", relayValue.getState() ? 1 : 0, relayValue.getChannel());
        }

        if (automation.getValue() instanceof ParamValue) {
            ParamValue paramValue = (ParamValue) automation.getValue();
            valueQuery = String.format(Locale.US, "&value=%d&param=%d", paramValue.getValue(), paramValue.getIndex());
        }

        if (automation.getValue() instanceof PWMValue) {
            PWMValue pwmValue = (PWMValue) automation.getValue();
            valueQuery = String.format(Locale.US, "&value=%d&param=%d", pwmValue.getDuty(), pwmValue.getChannel());
        }

        if (automation.getValue() instanceof RGBValue) {
            RGBValue rgbValue = (RGBValue) automation.getValue();
            valueQuery = String.format(Locale.US, "&value=%d&param=%d", rgbValue.getDutyValueAsLong(), rgbValue.getChannelValueAsLong());
        }

        if (automation.getValue() instanceof Integer) {
            valueQuery = String.format(Locale.US, "&value=%d&param=%d", 0, automation.getValue());
        }

        Request request = new Request.Builder()
                .url((variant == Variant.WiFi ? "https:/" : "http:/") + address.toString() + "/addauto?" + type + ix + triggerQuery + valueQuery)
                .build();

        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                syncCallback.failure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                syncCallback.success();
            }
        });
    }

    @Override
    public void listAutomations(Variant variant, final InetAddress address, final SyncCallback syncCallback) {
        Request request = new Request.Builder()
                .url((variant == Variant.WiFi ? "https:/" : "http:/") + address.toString() + "/listauto")
                .build();

        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                syncCallback.failure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                HttpListAutomationResponse listResponse = _gson.fromJson(json, HttpListAutomationResponse.class);

                if (listResponse != null) {
                    ArrayList<Automation> automationList = new ArrayList<>();
                    for (HttpDateTimeAutomationResponse dta : listResponse.getDateTimeAutomations()) {
                        Automation automation = null;
                        try {
                            automation = _automationBuilder.buildDateTimeAutomationFromNumbers(
                                    dta.getIndex(), dta.getTime(), dta.getUnit(), dta.getParam(), dta.getValue(), dta.getEnabled() == 1);
                        } catch (Exception ignored) {
                        }
                        automationList.add(automation);
                    }
                    for (HttpSchedulerAutomationResponse sra : listResponse.getSchedulerAutomations()) {
                        Automation automation = null;
                        try {
                            automation = _automationBuilder.buildSchedulerAutomationFromNumbers(
                                    sra.getIndex(), sra.getTime(), sra.getDays(), sra.getUnit(), sra.getParam(), sra.getValue(), sra.getEnabled() == 1);
                        } catch (Exception ignored) {
                        }
                        automationList.add(automation);
                    }

                    if (_listener != null) {
                        _listener.onAutomationListLoaded(automationList);
                    }

                    syncCallback.success();
                }
            }
        });
    }

    private boolean pushBlockingUpdateQuery(Variant variant, String query, final InetAddress address) {
        Request request = new Request.Builder()
                .url((variant == Variant.WiFi ? "https:/" : "http:/") + address.toString() + "/sync?" + query)
                .build();

        try {
            Response response = _client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }

    private void pushUpdateQuery(Variant variant, String query, final InetAddress address, final SyncCallback syncCallback) {
        Request request = new Request.Builder()
                .url((variant == Variant.WiFi ? "https:/" : "http:/") + address.toString() + "/sync?" + query)
                .build();

        _client.newCall(request).enqueue(syncResponse2SyncCallback(syncCallback, variant));
    }

    private Callback syncResponse2SyncCallback(final SyncCallback syncCallback, final Variant variant) {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                syncCallback.failure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    InetAddress address = InetAddress.getByName(call.request().url().host());
                    if (response.code() == 401) {
                        if (_listener != null) {
                            _listener.onSecuredDeviceFound(address);
                        }
                    } else {
                        long timestamp = Calendar.getInstance().getTimeInMillis();
                        String chipId = null;
                        if (variant == Variant.WiFi) {
                            try {
                                chipId = extractChipIdFromCertificate(response);
                            } catch (Exception ex) {
                                chipId = address.toString();
                            }
                        }
                        DeviceSyncData responseSyncData = processSyncResponse(chipId, response);

                        if (_listener != null) {
                            _listener.onAfterSync(address, responseSyncData, timestamp, null);
                        }
                        syncCallback.success();
                    }
                } catch (Exception ex) {
                    syncCallback.failure(ex);
                }
            }
        };
    }

    private Callback lockResponse2SyncCallback(final SyncCallback syncCallback) {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                syncCallback.failure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    syncCallback.success();
                } else {
                    syncCallback.failure(new SecurityException());
                }
            }
        };
    }

    private DeviceSyncData processSyncResponse(String chipId, Response response) throws IOException, BoardNotSupportedException {
        String json = response.body().string();
        HttpSyncResponse values = _gson.fromJson(json, HttpSyncResponse.class);

        int boardIdAsInt = values.getBoardId();
        if (chipId == null) {
            chipId = "00" + values.getChipId().toLowerCase();
        }
        BoardId boardId = BoardId.fromInt(boardIdAsInt);
        int relays = values.getRelays() != null ? values.getRelays().size() : 0;
        int relayImpulses = values.getRelayImpulses() != null ? values.getRelayImpulses().size() : 0;
        int pwms = values.getPwms() != null ? values.getPwms().size() : 0;
        int pwmImpulses = values.getPwmImpulses() != null ? values.getPwmImpulses().size() : 0;
        int params = values.getParams() != null ? values.getParams().size() : 0;
        String name = Base64Utils.decodeNameFromBase64(values.getName());
        BoardRole role = BoardRole.fromInt(values.getRole());
        SystemInformation systemInformation = new SystemInformation(boardId, values.getVmajor(),
                values.getVminor(), Variant.fromInt(values.getFvariant()));
        BoardMode mode = BoardMode.fromInt(values.getRole());
        DeviceKey key = new DeviceKey(chipId, boardId);
        OtaState ota = OtaState.fromByte(values.getOta());
        DeviceSyncData syncData = new DeviceSyncData(values.isLocked() == 1, systemInformation,
                key, name, values.getColor(), role, ota, mode, values.getState(), SyncSource.LAN);

        syncData.setRelayStates(new boolean[relays]);
        if (relays > 0) {
            int i = 0;
            for (Byte state : values.getRelays()) {
                syncData.getRelayStates()[i] = state == 1;
                i++;
            }
        }

        syncData.setRelayImpulses(new long[relayImpulses]);
        if (relayImpulses > 0) {
            int i = 0;
            for (Long impulse : values.getRelayImpulses()) {
                syncData.getRelayImpulses()[i] = impulse;
                i++;
            }
        }

        syncData.setPwmDuties(new int[pwms]);
        if (pwms > 0) {
            int i = 0;
            for (Integer duty : values.getPwms()) {
                syncData.getPwmDuties()[i] = duty;
                i++;
            }
        }

        syncData.setPwmImpulses(new long[pwmImpulses]);
        if (pwmImpulses > 0) {
            int i = 0;
            for (Long impulse : values.getPwmImpulses()) {
                syncData.getPwmImpulses()[i] = impulse;
                i++;
            }
        }

        syncData.setParams(new long[params]);
        if (params > 0) {
            int i = 0;
            for (Long value : values.getParams()) {
                syncData.getParams()[i] = value;
                i++;
            }
        }

        if (values.getTemp() != null) {
            syncData.setTemperature(values.getTemp());
        }

        if (values.getHum() != null) {
            syncData.setHumidity(values.getHum());
        }

        if (values.getDust() != null) {
            syncData.setDust(values.getDust());
        }

        if (values.getLum() != null) {
            syncData.setLuminosity(values.getLum());
        }

        if (values.getNoise() != null) {
            syncData.setNoise(values.getNoise());
        }

        return syncData;
    }

    @Override
    public void sync(Variant variant, final InetAddress address, SyncCallback syncCallback) {
        final Request request = new Request.Builder()
                .url((variant == Variant.WiFi ? "https:/" : "http:/") + address.toString() + "/sync")
                .build();
        _client.newCall(request).enqueue(syncResponse2SyncCallback(syncCallback, variant));
    }

    @Override
    public void lock(Variant variant, final InetAddress address, SyncCallback callback, String password) {
        final Request request = new Request.Builder()
                .url((variant == Variant.WiFi ? "https:/" : "http:/") + address.toString() + "/lock?password=" + password)
                .build();
        _client.newCall(request).enqueue(lockResponse2SyncCallback(callback));
    }

    @Override
    public void onFound(InetAddress address, Variant variant, BoardId boardId, Byte[] restoreTokenPart) {
        if (!_persistenceService.contains(address) && _listener != null) {
            sync(variant, address, new SyncCallback() {
                @Override
                public void success() {

                }

                @Override
                public void failure(Exception exception) {

                }
            });
        }

        if (variant.isCloud() && _listener != null) {
            _listener.onCloudDeviceFound(address, variant, boardId, restoreTokenPart);
        }
    }

    private String extractChipIdFromCertificate(Response response) {
        X509Certificate peerCertificate = (X509Certificate) response.handshake().peerCertificates().get(0);
        byte[] serialAsBytes = peerCertificate.getSerialNumber().toByteArray();
        return ByteUtils.bytesToHex(serialAsBytes);
    }

    @Override
    public void run() {
        _persistenceService.load();
        if (!_persistenceService.isEmpty()) {
            Enumeration<InetAddress> enumerator = _persistenceService.getAddresses();
            _persistenceService.clear();
            while (enumerator.hasMoreElements()) {
                InetAddress address = enumerator.nextElement();
                onFound(address, Variant.Unknown, null, new Byte[0]);
            }
        }
    }
}
