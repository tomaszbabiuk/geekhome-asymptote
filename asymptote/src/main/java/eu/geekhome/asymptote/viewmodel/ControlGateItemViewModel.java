package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlGateItemViewModel extends ValueSync<String> implements LayoutHolder {

    @Override
    public void sync(DeviceSyncData data) {
        syncValue(data.getState());
    }

    @Override
    protected void execute(String value) {
        stateChanged(value);
        getSensor().onRequestFullSync();
    }

    @Override
    protected boolean isValueUpdateRequested() {
        return findStateUpdate(getSensor().getUpdates()) != null;
    }

    @Override
    protected String composeValueMessage(Context context, String value) {
        switch (value) {
            case "open":
                return context.getString(R.string.gate_open);
            case "closed":
                return context.getString(R.string.gate_closed);
            case "openingerror":
                return context.getString(R.string.gate_opening_error);
            case "closingerror":
                return context.getString(R.string.gate_closing_error);
            case "opening":
                return context.getString(R.string.gate_opening);
            case "closing":
                return context.getString(R.string.gate_closing);
            case "stopped":
                return context.getString(R.string.gate_stopped);
        }
        return context.getString(R.string.na);
    }

    @Override
    protected String composeWaitMessage(Context context, String value) {
        return context.getString(R.string.please_wait);
    }

    ControlGateItemViewModel(SensorItemViewModel sensor, Context context,
                             String state) {
        super(sensor, context, -1, state);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_gate;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    public void openClose() {
        if (getValue().equals("closed")) {
            stateChanged("*doopen");
            getSensor().onRequestFullSync();
        }
        if (getValue().equals("open")) {
            stateChanged("*doclose");
            getSensor().onRequestFullSync();
        }
    }

    public void stopRelease() {
        if (getValue().equals("stopped")) {
            stateChanged("*dorelease");
        } else {
            stateChanged("*dostop");
        }
        getSensor().onRequestFullSync();
    }

    public void reset() {
        stateChanged("*dorelease");
        getSensor().onRequestFullSync();
    }
}
