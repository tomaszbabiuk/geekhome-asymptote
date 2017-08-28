package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlThermostatItemViewModel extends ValueSync<Integer>  implements LayoutHolder {
    private int _min;
    private int _max;
    private int _valueIndex;
    private int _maxIndex;
    private String _activeMessage;
    private String _inactiveMessage;

    public ControlThermostatItemViewModel(SensorItemViewModel sensor, Context context,
                                          int channel, long[] initialParams, String activeMessage, String inactiveMessage) {
        super(sensor, context, channel, (int)initialParams[0]);
        sync(initialParams);
        setActiveMessage(activeMessage);
        setInactiveMessage(inactiveMessage);
    }

    public void sync(long[] params) {
        setMin((int)(params[2]));
        setMax((int)(params[3]));
        setValueIndex(Math.round((params[0] - params[2])/50));
        setValue((int)params[0]);
        long delta = _max -_min;
        setMaxIndex(Math.round(delta/50));
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_thermostat;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    @Bindable
    public int getMin() {
        return _min;
    }

    public void setMin(int min) {
        _min = min;
        notifyPropertyChanged(BR.min);
    }

    @Bindable
    public int getMax() {
        return _max;
    }

    public void setMax(int max) {
        _max = max;
        notifyPropertyChanged(BR.max);
    }

    @Bindable
    public int getValueIndex() {
        return _valueIndex;
    }

    public void setValueIndex(int valueIndex) {
        if (_valueIndex != valueIndex) {
            setValue(getMin() + valueIndex * 50);
        }
        _valueIndex = valueIndex;
        notifyPropertyChanged(BR.valueIndex);
    }

    @Bindable
    public int getMaxIndex() {
        return _maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        _maxIndex = maxIndex;
        notifyPropertyChanged(BR.maxIndex);
    }

    @Override
    protected void execute(Integer value) {
        paramChanged(getChannel(), value);
        getSensor().requestSyncDelayed();
    }

    @Override
    protected boolean isValueUpdateRequested() {
        return false;
    }

    @Override
    protected String composeValueMessage(Context context, Integer value) {
        return null;
    }

    @Override
    protected String composeWaitMessage(Context context, Integer value) {
        return null;
    }

    @Bindable
    public String getActiveMessage() {
        return _activeMessage;
    }

    public void setActiveMessage(String activeMessage) {
        _activeMessage = activeMessage;
        notifyPropertyChanged(BR.activeMessage);
    }

    @Bindable
    public String getInactiveMessage() {
        return _inactiveMessage;
    }

    public void setInactiveMessage(String inactiveMessage) {
        _inactiveMessage = inactiveMessage;
        notifyPropertyChanged(BR.inactiveMessage);
    }

    @Override
    public void sync(DeviceSyncData data) {

    }
}
