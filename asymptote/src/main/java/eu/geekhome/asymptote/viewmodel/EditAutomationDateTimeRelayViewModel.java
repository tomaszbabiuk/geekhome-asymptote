package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeRelayBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeRelayViewModel extends EditAutomationDateTimeViewModelBase<FragmentEditAutomationDatetimeRelayBinding, RelayValue> {

    public EditAutomationDateTimeRelayViewModel(Context context, MainViewModelsFactory factory,
                                                NavigationService navigationService,
                                                AutomationAddedListener listener,
                                                SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<RelayValue> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditRelayValueViewModel(sensor);
    }

    public EditAutomationDateTimeRelayViewModel(Context context, MainViewModelsFactory factory,
                                                NavigationService navigationService,
                                                AutomationAddedListener listener,
                                                SensorItemViewModel sensor, AutomationDateTimeRelay automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected Automation<DateTimeTrigger, RelayValue> createAutomation(DateTimeTrigger trigger, RelayValue value) {
        return new AutomationDateTimeRelay(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationDatetimeRelayBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeRelayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_relay, container, false);
        binding.setVm(this);
        return binding;
    }
}
