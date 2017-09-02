package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentHygrostatRoleDetailsBinding;
import eu.geekhome.asymptote.model.ParamSyncUpdate;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;
import me.bendik.simplerangeview.SimpleRangeView;

public class HygrostatRoleDetailsViewModel extends XStatRoleDetailsViewModelBase<FragmentHygrostatRoleDetailsBinding> {
    private int _rangeStart;
    private int _rangeEnd;
    private int _value;
    private int _hysteresis;
    private int _valueIndex;


    public HygrostatRoleDetailsViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                                         EditSensorViewModel parent, SensorItemViewModel sensor,
                                         String title, String instruction, boolean reset) {
        super(factory, navigationService, parent, sensor, title, instruction, reset);


        long value = isReset() ? -1 : getSensor().getSyncData().getParams()[0];
        long hysteresis = isReset() ? -1 : getSensor().getSyncData().getParams()[1];
        long min = isReset() ? -1 : getSensor().getSyncData().getParams()[2];
        long max = isReset() ? -1 : getSensor().getSyncData().getParams()[3];

        setRangeStart(isReset() ? 3 : Math.round(min/100/10));
        setRangeEnd(isReset() ? 6 : Math.round(max/100/10));
        setHysteresis(isReset() ? 1 : Math.round(hysteresis/100));
        setValueIndex(isReset() ? 0 : Math.round((value - min)/100));
    }

    @Override
    public FragmentHygrostatRoleDetailsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        final FragmentHygrostatRoleDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hygrostat_role_details, container, false);
        binding.setVm(this);
        binding.range.setOnRangeLabelsListener(new SimpleRangeView.OnRangeLabelsListener() {
            @Nullable
            @Override
            public String getLabelTextForPosition(@NotNull SimpleRangeView rangeView, int pos, @NotNull SimpleRangeView.State state) {
                return String.valueOf(pos * 10);
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

    public void onDone() {
        getSensor().setBlocked(true);
        getParent().commit();
        paramChanged(0, getValue() * 100);
        paramChanged(1, getHysteresis() * 100);
        paramChanged(2, getRangeStart() * 10 * 100);
        paramChanged(3, getRangeEnd() * 10 * 100);
        getSensor().onRequestFullSync();

        getNavigationService().goBackTo(MainViewModel.class);
    }

    public void paramChanged(int paramIndex, long paramValue) {
        getSensor().getUpdates().add(new ParamSyncUpdate(new ParamValue(paramIndex, paramValue)));
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
    public int getValue() {
        return _value;
    }

    public void setValue(int value) {
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


    @Bindable
    public int getValueIndex() {
        return _valueIndex;
    }

    public void setValueIndex(int valueIndex) {
        _valueIndex = valueIndex;
        setValue(Math.round(getRangeStart() * 10 + valueIndex));
        notifyPropertyChanged(BR.valueIndex);
    }
}
