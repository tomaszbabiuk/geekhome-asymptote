package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlRelayItemViewModel extends ValueSync<Boolean> implements LayoutHolder {

    @Override
    public void sync(DeviceSyncData data) {
        syncValue(data.getRelayStates()[getChannel()]);
    }

    @Override
    protected void execute(Boolean value) {
        relayStateChanged(getChannel(), value);
        getSensor().onRequestFullSync();
    }

    @Override
    protected boolean isValueUpdateRequested() {
        return findRelayUpdate(getChannel(), getSensor().getUpdates()) != null;
    }

    @Override
    protected String composeValueMessage(Context context, Boolean value) {
        return value ? context.getString(R.string.relay_x_is_on, getChannel()) :
                context.getString(R.string.relay_x_is_off, getChannel());
    }

    @Override
    protected String composeWaitMessage(Context context, Boolean value) {
        return context.getString(R.string.please_wait);
    }

    public ControlRelayItemViewModel(SensorItemViewModel sensor, Context context,
                                     int channel, boolean initialValue) {
        super(sensor, context, channel, initialValue);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_relay;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }
}
