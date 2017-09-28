package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlImpulseItemViewModel extends ControlRelayItemViewModel implements LayoutHolder {
    private long _impulse;
    private long _setting;
    private boolean _blocked;


    ControlImpulseItemViewModel(SensorItemViewModel sensor, Context context, int channel,
                                boolean initialValue, long impulseSetting, long impulseNow) {
        super(sensor, context, channel, initialValue);
        _setting = impulseSetting;
        _impulse = impulseNow;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_impulse;
    }

    @Override
    public void sync(DeviceSyncData data) {
        super.sync(data);
        setImpulse(data.getRelayImpulses()[getChannel()]);
        setSetting(data.getParams()[getChannel() * 2]);
        setBlocked(getSensor().getUpdates().size() > 0);
    }

    @Bindable
    public long getImpulse() {
        return _impulse;
    }

    public void setImpulse(long impulse) {
        _impulse = impulse;
        notifyPropertyChanged(BR.impulse);
    }

    @Bindable
    public long getSetting() {
        return _setting;
    }

    private void setSetting(long setting) {
        _setting = setting;
        notifyPropertyChanged(BR.setting);
    }

    @Override
    protected void execute(Boolean value) {
    }


    public void start() {
        setBlocked(true);

        relayImpulseChanged(getChannel(), getSetting());
        getSensor().requestFullSync();
    }

    public void stop() {
        setBlocked(true);

        relayImpulseChanged(getChannel(), 0);
        getSensor().requestFullSync();
    }

    public void toggleDelay() {
        if (getValue()) {
            if (getImpulse() == 0) {
                //disarm and off
                relayImpulseChanged(getChannel(), 0);
                getSensor().requestFullSync();
            }
        } else {
            if (getImpulse() == 0) {
                //arm
                relayImpulseChanged(getChannel(), getSetting());
                getSensor().requestFullSync();
            } else {
                //stop
                relayImpulseChanged(getChannel(), 0);
                getSensor().requestFullSync();
            }

        }
    }

    public void toggle() {
        setBlocked(true);

        if (getSetting() == 0) {
            if (getValue()) {
                relayImpulseChanged(getChannel(), 0);
            } else {
                relayImpulseChanged(getChannel(), 10000);
            }
            getSensor().requestFullSync();
        } else if (getValue()) {
            relayImpulseChanged(getChannel(), 0);
            getSensor().requestFullSync();
        } else {
            relayImpulseChanged(getChannel(), getSetting());
            getSensor().requestFullSync();
        }
    }

    @Bindable
    public boolean isBlocked() {
        return _blocked;
    }

    public void setBlocked(boolean blocked) {
        _blocked = blocked;
        notifyPropertyChanged(BR.blocked);
    }
}
