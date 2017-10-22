package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeImpulseBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeImpulse;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeImpulseViewModel extends EditAutomationDateTimeViewModelBase<FragmentEditAutomationDatetimeImpulseBinding, Integer> {

    public EditAutomationDateTimeImpulseViewModel(Context context, MainViewModelsFactory factory,
                                                  NavigationService navigationService,
                                                  AutomationAddedListener listener,
                                                  SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<Integer> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditImpulseValueViewModel(sensor);
    }

    public EditAutomationDateTimeImpulseViewModel(Context context, MainViewModelsFactory factory,
                                                  NavigationService navigationService,
                                                  AutomationAddedListener listener,
                                                  SensorItemViewModel sensor, AutomationDateTimeImpulse automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected Automation<DateTimeTrigger, Integer> createAutomation(DateTimeTrigger trigger, Integer value) {
        return new AutomationDateTimeImpulse(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationDatetimeImpulseBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeImpulseBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_impulse, container, false);
        binding.setVm(this);
        return binding;
    }
}
