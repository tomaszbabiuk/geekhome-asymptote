package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;

import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.ImpulseValue;
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
import eu.geekhome.asymptote.model.StateSyncUpdate;
import eu.geekhome.asymptote.model.SyncUpdate;

abstract class ControlItemViewModelBase extends BaseObservable {

    private SensorItemViewModel _sensor;

    ControlItemViewModelBase(SensorItemViewModel sensor) {
        _sensor = sensor;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public void setSensor(SensorItemViewModel sensor) {
        _sensor = sensor;
        notifyPropertyChanged(BR.sensor);
    }

    public void pwmImpulseChanged(int channel, long impulse) {
        pwmImpulseChangedInternal(channel, impulse, getSensor().getUpdates());
    }

    private void pwmImpulseChangedInternal(int channel, long impulse, ArrayList<SyncUpdate> updates) {
        PWMImpulseSyncUpdate pwmImpulseUpdate = findPwmImpulseUpdate(channel, updates);
        if (pwmImpulseUpdate == null) {
            updates.add(new PWMImpulseSyncUpdate(new ImpulseValue(channel, impulse)));
        } else {
            pwmImpulseUpdate.getValue().setImpulse(impulse);
        }
    }

    private PWMImpulseSyncUpdate findPwmImpulseUpdate(int channel, ArrayList<SyncUpdate> updates) {
        if (updates.size() > 0) {
            for (SyncUpdate syncUpdate : updates) {
                if (syncUpdate instanceof PWMImpulseSyncUpdate) {
                    ImpulseValue impulseValue = ((PWMImpulseSyncUpdate) syncUpdate).getValue();
                    if (impulseValue.getChannel() == channel) {
                        return (PWMImpulseSyncUpdate) syncUpdate;
                    }
                }
            }
        }

        return null;
    }

    void rgbChanged(RGBValue rgbValue) {
        rgbChangedInernal(rgbValue, getSensor().getUpdates());
    }

    private void rgbChangedInernal(RGBValue rgbValue, ArrayList<SyncUpdate> updates) {
        RGBSyncUpdate pwmUpdate = findRGBUpdate(rgbValue, updates);
        if (pwmUpdate == null) {
            updates.add(new RGBSyncUpdate(rgbValue));
        } else {
            pwmUpdate.setValue(rgbValue);
        }
    }

    private RGBSyncUpdate findRGBUpdate(RGBValue rgbValueToFind, ArrayList<SyncUpdate> updates) {
        if (updates.size() > 0) {
            for (SyncUpdate syncUpdate : updates) {
                if (syncUpdate instanceof RGBSyncUpdate) {
                    RGBValue rgbValue = ((RGBSyncUpdate) syncUpdate).getValue();
                    if (rgbValue.getRed().getChannel() == rgbValueToFind.getRed().getChannel() &&
                        rgbValue.getGreen().getChannel() == rgbValueToFind.getGreen().getChannel() &&
                        rgbValue.getBlue().getChannel() == rgbValueToFind.getBlue().getChannel()) {
                        return (RGBSyncUpdate) syncUpdate;
                    }
                }
            }
        }
        return null;
    }

    void pwmChanged(int channel, int duty) {
        pwmChangedInternal(channel, duty, getSensor().getUpdates());
    }

    private void pwmChangedInternal(int channel, int duty, ArrayList<SyncUpdate> updates) {
        PWMSyncUpdate pwmUpdate = findPwmUpdate(channel, updates);
        if (pwmUpdate == null) {
            updates.add(new PWMSyncUpdate(new PWMValue(channel, duty)));
        } else {
            pwmUpdate.getValue().setDuty(duty);
        }
    }

    PWMSyncUpdate findPwmUpdate(int channel, ArrayList<SyncUpdate> updates) {
        if (updates.size() > 0) {
            for (SyncUpdate syncUpdate : updates) {
                if (syncUpdate instanceof PWMSyncUpdate) {
                    PWMValue pwmValue = ((PWMSyncUpdate) syncUpdate).getValue();
                    if (pwmValue.getChannel() == channel) {
                        return (PWMSyncUpdate) syncUpdate;
                    }
                }
            }
        }
        return null;
    }

    void paramChanged(int channel, int value) {
        paramChangedInternal(channel, value, getSensor().getUpdates());
    }

    private void paramChangedInternal(int index, int value, ArrayList<SyncUpdate> updates) {
        ParamSyncUpdate paramSyncUpdate = findParamSyncUpdate(index, updates);
        if (paramSyncUpdate == null) {
            updates.add(new ParamSyncUpdate(new ParamValue(index, value)));
        } else {
            paramSyncUpdate.getValue().setValue(value);
        }
    }

    private ParamSyncUpdate findParamSyncUpdate(int index, ArrayList<SyncUpdate> updates) {
        if (updates.size() > 0) {
            for (SyncUpdate syncUpdate : updates) {
                if (syncUpdate instanceof ParamSyncUpdate) {
                    ParamValue paramValue = ((ParamSyncUpdate) syncUpdate).getValue();
                    if (paramValue.getIndex() == index) {
                        return (ParamSyncUpdate) syncUpdate;
                    }
                }
            }
        }
        return null;
    }

    void relayImpulseChanged(int channel, long impulse) {
        relayImpulseChangedInternal(channel, impulse, getSensor().getUpdates());
    }

    private void relayImpulseChangedInternal(int channel, long impulse, ArrayList<SyncUpdate> updates) {
        RelayImpulseSyncUpdate relayImpulseUpdate = findRelayImpulseUpdate(channel, updates);
        if (relayImpulseUpdate == null) {
            updates.add(new RelayImpulseSyncUpdate(new ImpulseValue(channel, impulse)));
        } else {
            relayImpulseUpdate.getValue().setImpulse(impulse);
        }
    }

    private RelayImpulseSyncUpdate findRelayImpulseUpdate(int channel, ArrayList<SyncUpdate> updates) {
        if (updates.size() > 0) {
            for (SyncUpdate syncUpdate : updates) {
                if (syncUpdate instanceof RelayImpulseSyncUpdate) {
                    ImpulseValue impulseValue = ((RelayImpulseSyncUpdate) syncUpdate).getValue();
                    if (impulseValue.getChannel() == channel) {
                        return (RelayImpulseSyncUpdate) syncUpdate;
                    }
                }
            }
        }

        return null;
    }

    void relayStateChanged(int channel, boolean state) {
        relayStateChangedInternal(channel, state, getSensor().getUpdates());
    }

    private void relayStateChangedInternal(int channel, boolean state, ArrayList<SyncUpdate> updates) {
        RelaySyncUpdate relayUpdate = findRelayUpdate(channel, updates);
        if (relayUpdate == null) {
            updates.add(new RelaySyncUpdate(new RelayValue(channel, state)));
        } else {
            relayUpdate.getValue().setState(state);
        }
    }

    RelaySyncUpdate findRelayUpdate(int channel, ArrayList<SyncUpdate> updates) {
        if (updates.size() > 0) {
            for (SyncUpdate syncUpdate : updates) {
                if (syncUpdate instanceof RelaySyncUpdate) {
                    RelayValue relayValue = ((RelaySyncUpdate) syncUpdate).getValue();
                    if (relayValue.getChannel() == channel) {
                        return (RelaySyncUpdate) syncUpdate;
                    }
                }
            }
        }

        return null;
    }

    void stateChanged(String value) {
        stateChangedInternal(value, getSensor().getUpdates());
    }

    private void stateChangedInternal(String state, ArrayList<SyncUpdate> updates) {
        StateSyncUpdate stateUpdate = findStateUpdate(updates);
        if (stateUpdate == null) {
            updates.add(new StateSyncUpdate(state));
        } else {
            stateUpdate.setValue(state);
        }
    }

    StateSyncUpdate findStateUpdate(ArrayList<SyncUpdate> updates) {
        if (updates.size() > 0) {
            for (SyncUpdate syncUpdate : updates) {
                if (syncUpdate instanceof StateSyncUpdate) {
                    return (StateSyncUpdate)syncUpdate;
                }
            }
        }

        return null;
    }

    public abstract void sync(DeviceSyncData data);
}
