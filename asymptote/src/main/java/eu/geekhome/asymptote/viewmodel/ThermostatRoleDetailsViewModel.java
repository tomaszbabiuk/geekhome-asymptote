package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentThermostatRoleDetailsBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.model.ParamSyncUpdate;
import eu.geekhome.asymptote.model.ParamValue;
import me.bendik.simplerangeview.SimpleRangeView;

public class ThermostatRoleDetailsViewModel extends XStatRoleDetailsViewModelBase<FragmentThermostatRoleDetailsBinding> {

    private int _rangeStart;
    private int _rangeEnd;
    private double _value;
    private int _hysteresis;
    private int _valueIndex;

    public ThermostatRoleDetailsViewModel(ActivityComponent activityComponent, EditSensorViewModel parent,
                                          SensorItemViewModel sensor, String title, String instruction,
                                          boolean reset) {
        super(activityComponent, parent, sensor, title, instruction, reset);
        long value = isReset() ? -1 : getSensor().getSyncData().getParams()[0] - 27315;
        long hysteresis = isReset() ? -1 : getSensor().getSyncData().getParams()[1];
        long min = isReset() ? -1 : getSensor().getSyncData().getParams()[2] - 27315;
        long max = isReset() ? -1 : getSensor().getSyncData().getParams()[3] - 27315;
        setRangeStart(isReset() ? 6 : Math.round(min/100/5 + 5));
        setRangeEnd(isReset() ? 12 : Math.round(max/100/5 + 5));
        setHysteresis(isReset() ? 0 : Math.round(hysteresis/100));
        setValueIndex(isReset() ? 0 : Math.round((value-min)/50));
    }

    @Override
    public FragmentThermostatRoleDetailsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        final FragmentThermostatRoleDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_thermostat_role_details, container, false);
        binding.setVm(this);
        binding.range.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView rangeView, int pos, @NotNull SimpleRangeView.State state) {
                return String.valueOf(pos * 5 - 25);
            }
        });

        binding.range.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int start, int end) {
                setValueIndex(0);
                setRangeStart(start);
                setRangeEnd(end);
            }
        });

        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public void onDone() {
        getSensor().setBlocked(true);
        getParent().commit();
        paramChanged(0, celsiusToIntegerKelvins(getValue()));
        paramChanged(1, getHysteresis() * 100);
        paramChanged(2, celsiusToIntegerKelvins(getRangeStart() * 5 - 25));
        paramChanged(3, celsiusToIntegerKelvins(getRangeEnd() * 5 - 25));
        getSensor().onRequestFullSync();

        getNavigationService().goBackTo(MainViewModel.class);
    }

    public void paramChanged(int paramIndex, long paramValue) {
        getSensor().getUpdates().add(new ParamSyncUpdate(new ParamValue(paramIndex, paramValue)));
    }

    private long celsiusToIntegerKelvins(double celsius) {
        double result = celsius * 100 + 27315;
        return Math.round(result);
    }

    @Bindable
    public int getRangeStart() {
        return _rangeStart;
    }

    public void setRangeStart(int value) {
        _rangeStart = value;
        notifyPropertyChanged(BR.rangeStart);
    }

    @Bindable
    public int getRangeEnd() {
        return _rangeEnd;
    }

    public void setRangeEnd(int value) {
        _rangeEnd = value;
        notifyPropertyChanged(BR.rangeEnd);
    }

    @Bindable
    public int getValueIndex() {
        return _valueIndex;
    }

    public void setValueIndex(int value) {
        _valueIndex = value;
        setValue(((getRangeStart() * 5 - 25) + value * 0.5));
        notifyPropertyChanged(BR.valueIndex);
    }

    @Bindable
    public double getValue() {
        return _value;
    }

    public void setValue(double value) {
        _value = value;
        notifyPropertyChanged(BR.value);
    }

    @Bindable
    public int getHysteresis() {
        return _hysteresis;
    }

    public void setHysteresis(int hysteresis) {
        _hysteresis = hysteresis;
        notifyPropertyChanged(BR.hysteresis);
    }
}
