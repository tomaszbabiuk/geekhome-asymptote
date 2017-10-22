package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerImpulseBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationSchedulerImpulse;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerImpulseViewModel extends EditAutomationSchedulerViewModelBase<FragmentEditAutomationSchedulerImpulseBinding, Integer> {

    public EditAutomationSchedulerImpulseViewModel(Context context, MainViewModelsFactory factory,
                                                   NavigationService navigationService,
                                                   AutomationAddedListener listener,
                                                   SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<Integer> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditImpulseValueViewModel(sensor);
    }

    public EditAutomationSchedulerImpulseViewModel(Context context, MainViewModelsFactory factory,
                                                   NavigationService navigationService,
                                                   AutomationAddedListener listener,
                                                   SensorItemViewModel sensor, AutomationSchedulerImpulse automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected Automation<SchedulerTrigger, Integer> createAutomation(SchedulerTrigger trigger, Integer value) {
        return new AutomationSchedulerImpulse(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationSchedulerImpulseBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerImpulseBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_impulse, container, false);
        binding.setVm(this);
        return binding;
    }
}
