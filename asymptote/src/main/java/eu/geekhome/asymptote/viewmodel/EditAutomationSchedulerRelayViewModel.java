package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerRelayBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationSchedulerRelay;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerRelayViewModel extends EditAutomationSchedulerViewModelBase<FragmentEditAutomationSchedulerRelayBinding, RelayValue> {

    public EditAutomationSchedulerRelayViewModel(Context context, MainViewModelsFactory factory,
                                                 NavigationService navigationService,
                                                 AutomationAddedListener listener,
                                                 SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<RelayValue> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditRelayValueViewModel(sensor);
    }

    public EditAutomationSchedulerRelayViewModel(Context context, MainViewModelsFactory factory,
                                                 NavigationService navigationService,
                                                 AutomationAddedListener listener,
                                                 SensorItemViewModel sensor, AutomationSchedulerRelay automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected Automation<SchedulerTrigger, RelayValue> createAutomation(SchedulerTrigger trigger, RelayValue value) {
        return new AutomationSchedulerRelay(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationSchedulerRelayBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerRelayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_relay, container, false);
        binding.setVm(this);
        return binding;
    }
}
