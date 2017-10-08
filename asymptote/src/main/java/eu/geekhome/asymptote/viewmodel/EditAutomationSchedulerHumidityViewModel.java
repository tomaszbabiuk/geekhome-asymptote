package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerHumidityBinding;
import eu.geekhome.asymptote.model.AutomationSchedulerHumidity;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerHumidityViewModel extends EditAutomationViewModelBase<FragmentEditAutomationSchedulerHumidityBinding, AutomationSchedulerHumidity> {

    private EditHumidityValueViewModel _editHumidityValueViewModel;
    private EditSchedulerViewModel _editSchedulerViewModel;

    public EditAutomationSchedulerHumidityViewModel(Context context, MainViewModelsFactory factory,
                                                    NavigationService navigationService,
                                                    AutomationAddedListener listener,
                                                    SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationSchedulerHumidityViewModel(Context context, MainViewModelsFactory factory,
                                                    NavigationService navigationService,
                                                    AutomationAddedListener listener,
                                                    SensorItemViewModel sensor, AutomationSchedulerHumidity automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editHumidityValueViewModel = factory.createEditHumidityValueViewModel(sensor);
        _editSchedulerViewModel = factory.createEditSchedulerViewModel(sensor);
    }

    @Override
    protected AutomationSchedulerHumidity createAutomation() {
        ParamValue value = _editHumidityValueViewModel.buildHumidityValue();
        SchedulerTrigger trigger = _editSchedulerViewModel.buildSchedulerTrigger();
        return new AutomationSchedulerHumidity(getIndex(), trigger, value);
    }

    @Override
    protected void applyAutomationChanges(AutomationSchedulerHumidity automation) {
        _editHumidityValueViewModel.applyHumidityValue(automation.getValue());
        _editSchedulerViewModel.applySchedulerTrigger(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationSchedulerHumidityBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerHumidityBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_humidity, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditHumidityValueViewModel getEditHumidityValueViewModel() {
        return _editHumidityValueViewModel;
    }

    @Bindable
    public EditSchedulerViewModel getEditSchedulerViewModel() {
        return _editSchedulerViewModel;
    }
}
