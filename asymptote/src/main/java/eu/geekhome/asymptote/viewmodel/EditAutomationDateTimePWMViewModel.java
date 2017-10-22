package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimePwmBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimePWM;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.PWMValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimePWMViewModel extends EditAutomationDateTimeViewModelBase<FragmentEditAutomationDatetimePwmBinding, PWMValue> {

    public EditAutomationDateTimePWMViewModel(Context context, MainViewModelsFactory factory,
                                              NavigationService navigationService,
                                              AutomationAddedListener listener,
                                              SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<PWMValue> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditPWMValueViewModel(sensor);
    }

    public EditAutomationDateTimePWMViewModel(Context context, MainViewModelsFactory factory,
                                              NavigationService navigationService,
                                              AutomationAddedListener listener,
                                              SensorItemViewModel sensor, Automation<DateTimeTrigger, PWMValue> automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected AutomationDateTimePWM createAutomation(DateTimeTrigger trigger, PWMValue value) {
        return new AutomationDateTimePWM(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationDatetimePwmBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimePwmBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_pwm, container, false);
        binding.setVm(this);
        return binding;
    }
}
