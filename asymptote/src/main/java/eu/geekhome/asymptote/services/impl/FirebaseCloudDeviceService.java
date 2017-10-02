package eu.geekhome.asymptote.services.impl;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.model.AutomationSchedulerRelay;
import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.BoardMode;
import eu.geekhome.asymptote.model.BoardNotSupportedException;
import eu.geekhome.asymptote.model.BoardRole;
import eu.geekhome.asymptote.model.CloudFingerprintSyncUpdate;
import eu.geekhome.asymptote.model.CloudPasswordSyncUpdate;
import eu.geekhome.asymptote.model.CloudUsernameSyncUpdate;
import eu.geekhome.asymptote.model.ColorSyncUpdate;
import eu.geekhome.asymptote.model.AutomationSyncUpdate;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.DeviceKey;
import eu.geekhome.asymptote.model.DeviceSnapshot;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.NameSyncUpdate;
import eu.geekhome.asymptote.model.OtaHashSyncUpdate;
import eu.geekhome.asymptote.model.OtaHostSyncUpdate;
import eu.geekhome.asymptote.model.OtaState;
import eu.geekhome.asymptote.model.PWMImpulseSyncUpdate;
import eu.geekhome.asymptote.model.PWMSyncUpdate;
import eu.geekhome.asymptote.model.ParamSyncUpdate;
import eu.geekhome.asymptote.model.RGBSyncUpdate;
import eu.geekhome.asymptote.model.RelayImpulseSyncUpdate;
import eu.geekhome.asymptote.model.RelaySyncUpdate;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.model.RestoreTokenSyncUpdate;
import eu.geekhome.asymptote.model.RoleSyncUpdate;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.model.StateSyncUpdate;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.model.SyncUpdate;
import eu.geekhome.asymptote.model.SystemInformation;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.SyncListener;
import eu.geekhome.asymptote.services.SyncSource;
import eu.geekhome.asymptote.utils.Base64Utils;

public class FirebaseCloudDeviceService implements CloudDeviceService {
    private final Activity _activity;
    private SyncListener _syncListener;
    private Hashtable<DeviceSnapshot, ValueEventListener> _deviceListeners = new Hashtable<>();


    public FirebaseCloudDeviceService(Activity activity) {
        _activity = activity;
    }

    private ValueEventListener createDeviceListener(final DeviceSnapshot deviceSnapshot) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    InetAddress localAddress;
                    try {
                        Iterator<DataSnapshot> bytesSnapshot = dataSnapshot.child("localip").getChildren().iterator();
                        int byte1 = bytesSnapshot.next().getValue(Integer.class);
                        int byte2 = bytesSnapshot.next().getValue(Integer.class);
                        int byte3 = bytesSnapshot.next().getValue(Integer.class);
                        int byte4 = bytesSnapshot.next().getValue(Integer.class);

                        localAddress = InetAddress.getByAddress(new byte[]{(byte) byte1, (byte) byte2, (byte) byte3, (byte) byte4});
                    } catch (Exception ex) {
                        localAddress = null;
                    }

                    try {
                        int boardIdAsInt = dataSnapshot.child("boardid").getValue(Integer.class);
                        BoardId boardId = BoardId.fromInt(boardIdAsInt);
                        DeviceKey key = new DeviceKey(deviceSnapshot.getChipId(), boardId);
                        String name = Base64Utils.decodeNameFromBase64(dataSnapshot.child("name").getValue(String.class));
                        boolean locked = false;
                        if (dataSnapshot.child("locked").exists()) {
                            locked = dataSnapshot.child("locked").getValue(Integer.class) == 1;
                        }
                        int roleAsInt = dataSnapshot.child("role").getValue(Integer.class);

                        BoardMode mode = BoardMode.MANUAL;
                        if (dataSnapshot.child("mode").exists()) {
                            int modeAsInt = dataSnapshot.child("mode").getValue(Integer.class);
                            mode = BoardMode.fromInt(modeAsInt);
                        }

                        BoardRole role = BoardRole.fromInt(roleAsInt);
                        int color = dataSnapshot.child("color").getValue(Integer.class);
                        int versionMajor = dataSnapshot.child("vmajor").getValue(Integer.class);
                        int versionMinor = dataSnapshot.child("vminor").getValue(Integer.class);
                        int firmwareVariant = dataSnapshot.child("fvariant").getValue(Integer.class);
                        boolean hasTemperatureSensor = dataSnapshot.child("temp").exists();
                        boolean hasHumiditySensor = dataSnapshot.child("hum").exists();
                        boolean hasNoiseSensor = dataSnapshot.child("noise").exists();
                        boolean hasDustSensor = dataSnapshot.child("dust").exists();
                        boolean hasLuminositySensor = dataSnapshot.child("lum").exists();
                        int relays = (int) dataSnapshot.child("relays").getChildrenCount();
                        int relayImpulses = (int) dataSnapshot.child("relayImpulses").getChildrenCount();
                        int pwms = (int) dataSnapshot.child("pwms").getChildrenCount();
                        int pwmImpulses = (int) dataSnapshot.child("pwmImpulses").getChildrenCount();
                        int params = (int) dataSnapshot.child("params").getChildrenCount();
                        int ota = dataSnapshot.child("ota").getValue(Integer.class);
                        String token = deviceSnapshot.getDeviceToken();
                        SystemInformation info = new SystemInformation(boardId, versionMajor, versionMinor, Variant.fromInt(firmwareVariant));
                        String state = "unknown";
                        if (dataSnapshot.child("state").exists()) {
                            state = dataSnapshot.child("state").getValue(String.class);
                        }

                        DeviceSyncData syncData = new DeviceSyncData(locked, info, key, name, color,
                                role, OtaState.fromByte((byte) ota), mode, state, SyncSource.CLOUD);



                        if (hasTemperatureSensor) {
                            int temp = dataSnapshot.child("temp").getValue(Integer.class);
                            syncData.setTemperature(temp);
                        }

                        if (hasHumiditySensor) {
                            int hum = dataSnapshot.child("hum").getValue(Integer.class);
                            syncData.setHumidity(hum);
                        }

                        if (hasLuminositySensor) {
                            int lum = dataSnapshot.child("lum").getValue(Integer.class);
                            syncData.setLuminosity(lum);
                        }

                        if (hasDustSensor) {
                            int dust = dataSnapshot.child("dust").getValue(Integer.class);
                            syncData.setDust(dust);
                        }

                        if (hasNoiseSensor) {
                            int noise = dataSnapshot.child("noise").getValue(Integer.class);
                            syncData.setNoise(noise);
                        }

                        int i = 0;
                        syncData.setRelayStates(new boolean[relays]);
                        for (DataSnapshot snapshot : dataSnapshot.child("relays").getChildren()) {
                            int relayState = snapshot.getValue(Integer.class);
                            syncData.getRelayStates()[i] = relayState == 1;
                            i++;
                        }

                        i = 0;
                        syncData.setRelayImpulses(new long[relayImpulses]);
                        for (DataSnapshot snapshot : dataSnapshot.child("relayImpulses").getChildren()) {
                            long impulse = snapshot.getValue(Long.class);
                            syncData.getRelayImpulses()[i] = impulse;
                            i++;
                        }

                        i = 0;
                        syncData.setPwmDuties(new int[pwms]);
                        for (DataSnapshot snapshot : dataSnapshot.child("pwms").getChildren()) {
                            int duty = snapshot.getValue(Integer.class);
                            syncData.getPwmDuties()[i] = duty;
                            i++;
                        }

                        i = 0;
                        syncData.setPwmImpulses(new long[pwmImpulses]);
                        for (DataSnapshot snapshot : dataSnapshot.child("pwmImpulses").getChildren()) {
                            long impulse = snapshot.getValue(Long.class);
                            syncData.getPwmImpulses()[i] = impulse;
                            i++;
                        }

                        i = 0;
                        syncData.setParams(new long[params]);
                        for (DataSnapshot snapshot : dataSnapshot.child("params").getChildren()) {
                            long value = snapshot.getValue(Integer.class);
                            syncData.getParams()[i] = value;
                            i++;
                        }

                        long timestamp = dataSnapshot.child("time").getValue(Long.class);

                        if (_syncListener != null) {
                            _syncListener.onAfterSync(localAddress, syncData, timestamp, token);
                        }
                    } catch (BoardNotSupportedException ex) {
                        if (_syncListener != null) {
                            _syncListener.onUnsupportedBoard(localAddress, deviceSnapshot.getChipId());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void registerDevice(String userId, DeviceSnapshot deviceSnapshot, final CloudActionCallback<Void> callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final Map<String, Object> registers = new HashMap<>();
        registers.put("deviceToken", deviceSnapshot.getDeviceToken());
        registers.put("restoreToken", deviceSnapshot.getRestoreToken());

        DatabaseReference usersRef = database.getReference("users")
                .child(userId)
                .child("devices")
                .child(deviceSnapshot.getChipId());

        usersRef.updateChildren(registers).addOnCompleteListener(_activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    callback.success(null);
                } else {
                    CloudException exception = new CloudException(task.getException(), false);
                    callback.failure(exception);
                }
            }
        });
    }

    @Override
    public void getUserSnapshot(String userId, final CloudActionCallback<UserSnapshot> callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList<DeviceSnapshot> deviceSnapshots = new ArrayList<>();

                        for (DataSnapshot data : dataSnapshot.child("devices").getChildren()) {
                            String chipId = data.getKey();
                            String deviceToken = data.child("deviceToken").getValue(String.class);
                            String restoreToken = data.child("restoreToken").getValue(String.class);
                            DeviceSnapshot deviceSnapshot = new DeviceSnapshot(chipId, deviceToken, restoreToken);
                            deviceSnapshots.add(deviceSnapshot);
                        }

                        String emergencyPassword = dataSnapshot.child("emergencyPassword").getValue(String.class);

                        UserSnapshot userSnapshot = new UserSnapshot(deviceSnapshots, emergencyPassword);
                        callback.success(userSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.failure(new CloudException(databaseError.toException(), false));
                    }
                });
    }

    @Override
    public void removeDevice(String userId, final DeviceKey key, final CloudActionCallback<Void> callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users")
                .child(userId)
                .child("devices")
                .child(key.getChipId())
                .removeValue()
                .addOnCompleteListener(_activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (callback != null) {
                            if (task.isSuccessful()) {
                                callback.success(null);
                            } else {
                                CloudException cex = new CloudException(task.getException(), false);
                                callback.failure(cex);
                            }
                        }
                    }
                });
    }

    @Override
    public void registerForDeviceSyncEvents(String userId, final DeviceSnapshot deviceSnapshot) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ValueEventListener listener = createDeviceListener(deviceSnapshot);
        _deviceListeners.put(deviceSnapshot, listener);
        database
                .getReference("devices")
                .child(userId)
                .child(deviceSnapshot.getDeviceToken())
                .child("data")
                .addValueEventListener(listener);
    }

    @Override
    public void unregisterFromDeviceSyncEvents(String userId) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        for (DeviceSnapshot deviceSnapshot : _deviceListeners.keySet()) {
            database
                    .getReference("devices")
                    .child(userId)
                    .child(deviceSnapshot.getDeviceToken())
                    .child("data")
                    .removeEventListener(_deviceListeners.get(deviceSnapshot));
        }
    }

    @Override
    public void setSyncListener(SyncListener syncListener) {
        _syncListener = syncListener;
    }

    @Override
    public void pushUpdates(final String userId, final String token, final DeviceSyncData syncData, final ArrayList<SyncUpdate> updates, final CloudActionCallback<Void> callback) {
        if (updates != null) {
            final Map<String, Object> orders = new HashMap<>();
            clearOrders(userId, token, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull final Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (SyncUpdate update : updates) {

                            if (update instanceof RelaySyncUpdate) {
                                RelaySyncUpdate relayUpdate = (RelaySyncUpdate) update;
                                String ix = String.format("relays/%02X", relayUpdate.getValue().getChannel());
                                orders.put(ix, relayUpdate.getValue().getState() ? 1 : 0);
                            }

                            if (update instanceof RelayImpulseSyncUpdate) {
                                RelayImpulseSyncUpdate relayImpulseUpdate = (RelayImpulseSyncUpdate) update;
                                String ix = String.format("relayImpulses/%02X", relayImpulseUpdate.getValue().getChannel());
                                orders.put(ix, relayImpulseUpdate.getValue().getImpulse());
                            }

                            if (update instanceof NameSyncUpdate) {
                                NameSyncUpdate nameUpdate = (NameSyncUpdate) update;
                                orders.put("name", Base64Utils.encodeNameToBase64(nameUpdate.getValue()));
                            }

                            if (update instanceof OtaHostSyncUpdate) {
                                OtaHostSyncUpdate otaHostUpdate = (OtaHostSyncUpdate) update;
                                orders.put("otaHost", otaHostUpdate.getValue());
                            }

                            if (update instanceof OtaHashSyncUpdate) {
                                OtaHashSyncUpdate otaHashSyncUpdate = (OtaHashSyncUpdate) update;
                                orders.put("otaHash", otaHashSyncUpdate.getValue());
                            }

                            if (update instanceof RestoreTokenSyncUpdate) {
                                RestoreTokenSyncUpdate restoreTokenSyncUpdate = (RestoreTokenSyncUpdate) update;
                                orders.put("restoreToken", restoreTokenSyncUpdate.getValue());
                            }

                            if (update instanceof CloudUsernameSyncUpdate) {
                                CloudUsernameSyncUpdate cloudUsernameSyncUpdate = (CloudUsernameSyncUpdate) update;
                                orders.put("cloudUsername", cloudUsernameSyncUpdate.getValue());
                            }

                            if (update instanceof CloudPasswordSyncUpdate) {
                                CloudPasswordSyncUpdate cloudPasswordSyncUpdate = (CloudPasswordSyncUpdate) update;
                                orders.put("cloudPassword", cloudPasswordSyncUpdate.getValue());
                            }

                            if (update instanceof CloudFingerprintSyncUpdate) {
                                CloudFingerprintSyncUpdate cloudFingerprintSyncUpdate = (CloudFingerprintSyncUpdate) update;
                                orders.put("cloudFingerprint", cloudFingerprintSyncUpdate.getValue());
                            }

                            if (update instanceof ColorSyncUpdate) {
                                ColorSyncUpdate colorUpdate = (ColorSyncUpdate) update;
                                orders.put("color", colorUpdate.getValue());
                            }

                            if (update instanceof RoleSyncUpdate) {
                                RoleSyncUpdate roleUpdate = (RoleSyncUpdate) update;
                                orders.put("role", roleUpdate.getValue().getId());
                            }

                            if (update instanceof PWMSyncUpdate) {
                                PWMSyncUpdate pwmUpdate = (PWMSyncUpdate) update;
                                String ix = String.format("pwms/%02X", pwmUpdate.getValue().getChannel());
                                orders.put(ix, pwmUpdate.getValue().getDuty());
                            }

                            if (update instanceof RGBSyncUpdate) {
                                RGBSyncUpdate rgbUpdate = (RGBSyncUpdate) update;
                                int redChannel = rgbUpdate.getValue().getRed().getChannel();
                                int greenChannel = rgbUpdate.getValue().getGreen().getChannel();
                                int blueChannel = rgbUpdate.getValue().getBlue().getChannel();
                                int redDuty = rgbUpdate.getValue().getRed().getDuty();
                                int greenDuty = rgbUpdate.getValue().getGreen().getDuty();
                                int blueDuty = rgbUpdate.getValue().getBlue().getDuty();
                                String ix = String.format("rgbs/%02X%02X%02X", redChannel, greenChannel, blueChannel);
                                String val = String.format("%02X%02X%02X", redDuty, greenDuty, blueDuty);
                                orders.put(ix, val);
                            }

                            if (update instanceof PWMImpulseSyncUpdate) {
                                PWMImpulseSyncUpdate pwmImpulseUpdate = (PWMImpulseSyncUpdate) update;
                                String ix = String.format("pwmImpulses/%02X", pwmImpulseUpdate.getValue().getChannel());
                                orders.put(ix, pwmImpulseUpdate.getValue().getImpulse());
                            }

                            if (update instanceof ParamSyncUpdate) {
                                ParamSyncUpdate paramUpdate = (ParamSyncUpdate) update;
                                String ix = String.format("params/%02X", paramUpdate.getValue().getIndex());
                                orders.put(ix, paramUpdate.getValue().getValue());
                            }

                            if (update instanceof StateSyncUpdate) {
                                StateSyncUpdate stateUpdate = (StateSyncUpdate) update;
                                orders.put("state", stateUpdate.getValue());
                            }

                            if (update instanceof AutomationSyncUpdate) {
                                AutomationSyncUpdate automationUpdate = (AutomationSyncUpdate) update;
                                Automation automation = (Automation)automationUpdate.getValue();
                                String ix = String.format("auto/%02X/", automation.getIndex());
                                if (automation instanceof AutomationDateTimeRelay) {
                                    orders.put(ix + "type", "dr");
                                }

                                if (automation instanceof AutomationSchedulerRelay) {
                                    orders.put(ix + "type", "sr");
                                }

                                if (automation.getTrigger() instanceof DateTimeTrigger) {
                                    DateTimeTrigger dateTimeTrigger = (DateTimeTrigger)automation.getTrigger();
                                    orders.put(ix + "epoch", dateTimeTrigger.getUtcTimestamp());
                                }

                                if (automation.getTrigger() instanceof SchedulerTrigger) {
                                    SchedulerTrigger schedulerTrigger = (SchedulerTrigger)automation.getTrigger();
                                    orders.put(ix + "days", schedulerTrigger.getDays());
                                    orders.put(ix + "time", schedulerTrigger.getTimeMark());
                                }

                                if (automation.getValue() instanceof RelayValue) {
                                    RelayValue relayValue = (RelayValue)automation.getValue();
                                    orders.put(ix + "val", relayValue.getState() ? 1 : 0);
                                    orders.put(ix + "channel", relayValue.getChannel());
                                }
                            }
                        }
                        orders.put("timestamp", ServerValue.TIMESTAMP);

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference ordersRef = database
                                .getReference("devices")
                                .child(userId)
                                .child(token)
                                .child("orders");

                        ordersRef.updateChildren(orders).addOnCompleteListener(_activity, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    callback.success(null);
                                } else {
                                    CloudException cex = new CloudException(task.getException(), false);
                                    callback.failure(cex);
                                }
                            }
                        });
                    } else {
                        CloudException exception = new CloudException(task.getException(), false);
                        callback.failure(exception);
                    }
                }
            });
        }
    }

    @Override
    public void updateCertificateFingerprint(final String userId, final String token, String fingerprint, String hash, final CloudActionCallback<Void> callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final Map<String, Object> data = new HashMap<>();
        data.put("fingerprint", fingerprint);
        data.put("hash", hash);

        DatabaseReference ordersRef = database
                .getReference("devices")
                .child(userId)
                .child(token)
                .child("cert");

        ordersRef.updateChildren(data).addOnCompleteListener(_activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.success(null);
                } else {
                    CloudException cex = new CloudException(task.getException(), false);
                    callback.failure(cex);
                }
            }
        });
    }


    private void clearOrders(String userId, String token, OnCompleteListener<Void> callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        database
                .getReference("devices")
                .child(userId)
                .child(token)
                .child("orders").removeValue().addOnCompleteListener(_activity, callback);
    }
}