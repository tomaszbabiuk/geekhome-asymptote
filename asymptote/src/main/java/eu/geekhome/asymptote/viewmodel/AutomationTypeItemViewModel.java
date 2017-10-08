package eu.geekhome.asymptote.viewmodel;

import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class AutomationTypeItemViewModel extends SelectableItemViewModel implements LayoutHolder {
    private final ChooseAutomationViewModel _chooseAutomationViewModel;
    private final SensorItemViewModel _sensor;
    private final AutomationType _automationType;

    AutomationTypeItemViewModel(ChooseAutomationViewModel chooseAutomationViewModel, SensorItemViewModel sensor, AutomationType automationType) {
        _chooseAutomationViewModel = chooseAutomationViewModel;
        _sensor = sensor;
        _automationType = automationType;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_automation_type;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public AutomationType getAutomationType() {
        return _automationType;
    }

    public void selectTrigger() {
        _chooseAutomationViewModel.selectTrigger(getAutomationType());
        setSelected(true);
    }
}
