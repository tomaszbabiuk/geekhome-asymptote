package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerPwmBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationSchedulerPWM;
import eu.geekhome.asymptote.model.PWMValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerPWMViewModel extends EditAutomationSchedulerViewModelBase<FragmentEditAutomationSchedulerPwmBinding, PWMValue> {

    public EditAutomationSchedulerPWMViewModel(Context context, MainViewModelsFactory factory,
                                               NavigationService navigationService,
                                               AutomationAddedListener listener,
                                               SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<PWMValue> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditPWMValueViewModel(sensor);
    }

    public EditAutomationSchedulerPWMViewModel(Context context, MainViewModelsFactory factory,
                                               NavigationService navigationService,
                                               AutomationAddedListener listener,
                                               SensorItemViewModel sensor, AutomationSchedulerPWM automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected Automation<SchedulerTrigger, PWMValue> createAutomation(SchedulerTrigger trigger, PWMValue value) {
        return new AutomationSchedulerPWM(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationSchedulerPwmBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerPwmBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_pwm, container, false);
        binding.setVm(this);
        return binding;
    }
}
