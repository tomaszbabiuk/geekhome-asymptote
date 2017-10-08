package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerTemperatureBinding;
import eu.geekhome.asymptote.model.AutomationSchedulerTemperature;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerTemperatureViewModel extends EditAutomationViewModelBase<FragmentEditAutomationSchedulerTemperatureBinding, AutomationSchedulerTemperature> {

    private EditTemperatureValueViewModel _editTemperatureValueViewModel;
    private EditSchedulerViewModel _editSchedulerViewModel;

    public EditAutomationSchedulerTemperatureViewModel(Context context, MainViewModelsFactory factory,
                                                       NavigationService navigationService,
                                                       AutomationAddedListener listener,
                                                       SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationSchedulerTemperatureViewModel(Context context, MainViewModelsFactory factory,
                                                       NavigationService navigationService,
                                                       AutomationAddedListener listener,
                                                       SensorItemViewModel sensor, AutomationSchedulerTemperature automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editTemperatureValueViewModel = factory.createEditTemperatureValueViewModel(sensor);
        _editSchedulerViewModel = factory.createEditSchedulerViewModel(sensor);
    }

    @Override
    protected AutomationSchedulerTemperature createAutomation() {
        ParamValue value = _editTemperatureValueViewModel.buildTemperatureValue();
        SchedulerTrigger trigger = _editSchedulerViewModel.buildSchedulerTrigger();
        return new AutomationSchedulerTemperature(getIndex(), trigger, value);
    }

    @Override
    protected void applyAutomationChanges(AutomationSchedulerTemperature automation) {
        _editTemperatureValueViewModel.applyTemperatureValue(automation.getValue());
        _editSchedulerViewModel.applySchedulerTrigger(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationSchedulerTemperatureBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerTemperatureBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_temperature, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditTemperatureValueViewModel getEditTemperatureValueViewModel() {
        return _editTemperatureValueViewModel;
    }

    @Bindable
    public EditSchedulerViewModel getEditSchedulerViewModel() {
        return _editSchedulerViewModel;
    }
}
