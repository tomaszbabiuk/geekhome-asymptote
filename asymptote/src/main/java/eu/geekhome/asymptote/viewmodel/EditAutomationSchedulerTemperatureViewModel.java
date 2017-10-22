package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerTemperatureBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationSchedulerTemperature;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerTemperatureViewModel extends EditAutomationSchedulerViewModelBase<FragmentEditAutomationSchedulerTemperatureBinding, ParamValue> {

    public EditAutomationSchedulerTemperatureViewModel(Context context, MainViewModelsFactory factory,
                                                       NavigationService navigationService,
                                                       AutomationAddedListener listener,
                                                       SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<ParamValue> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditTemperatureValueViewModel(sensor);
    }

    public EditAutomationSchedulerTemperatureViewModel(Context context, MainViewModelsFactory factory,
                                                       NavigationService navigationService,
                                                       AutomationAddedListener listener,
                                                       SensorItemViewModel sensor, AutomationSchedulerTemperature automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected Automation<SchedulerTrigger, ParamValue> createAutomation(SchedulerTrigger trigger, ParamValue value) {
        return new AutomationSchedulerTemperature(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationSchedulerTemperatureBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerTemperatureBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_temperature, container, false);
        binding.setVm(this);
        return binding;
    }
}
