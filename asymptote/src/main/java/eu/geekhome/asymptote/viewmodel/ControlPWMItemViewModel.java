package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlPWMItemViewModel extends ValueSync<Integer> implements LayoutHolder {

    @Override
    public void sync(DeviceSyncData data) {
        syncValue(data.getPwmDuties()[getChannel()]);
    }

    @Override
    protected void execute(Integer value) {
        pwmChanged(getChannel(), value);
        getSensor().requestSyncDelayed();
    }

    @Override
    protected boolean isValueUpdateRequested() {
        return findPwmUpdate(getChannel(), getSensor().getUpdates()) != null;
    }

    @Override
    protected String composeValueMessage(Context context, Integer value) {
        return null;
    }

    @Override
    protected String composeWaitMessage(Context context, Integer value) {
        return context.getString(R.string.please_wait);
    }
    public ControlPWMItemViewModel(SensorItemViewModel sensor, Context context,
                                   int channel, int initialValue) {
        super(sensor, context, channel, initialValue);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_pwm;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }
}
