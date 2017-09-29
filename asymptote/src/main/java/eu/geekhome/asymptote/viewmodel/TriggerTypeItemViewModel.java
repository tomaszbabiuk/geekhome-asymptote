package eu.geekhome.asymptote.viewmodel;

import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class TriggerTypeItemViewModel extends SelectableItemViewModel implements LayoutHolder {
    private final ChooseTriggerViewModel _chooseTriggerViewModel;
    private final SensorItemViewModel _sensor;
    private final TriggerType _triggerType;

    TriggerTypeItemViewModel(ChooseTriggerViewModel chooseTriggerViewModel, SensorItemViewModel sensor, TriggerType triggerType) {
        _chooseTriggerViewModel = chooseTriggerViewModel;
        _sensor = sensor;
        _triggerType = triggerType;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_trigger_type;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public TriggerType getTriggerType() {
        return _triggerType;
    }

    public void selectTrigger() {
        _chooseTriggerViewModel.selectTrigger(getTriggerType());
        setSelected(true);
    }
}
