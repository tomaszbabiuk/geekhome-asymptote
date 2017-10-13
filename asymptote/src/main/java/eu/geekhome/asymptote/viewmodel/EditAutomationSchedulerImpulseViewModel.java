package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerImpulseBinding;
import eu.geekhome.asymptote.model.AutomationSchedulerImpulse;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerImpulseViewModel extends EditAutomationViewModelBase<FragmentEditAutomationSchedulerImpulseBinding, AutomationSchedulerImpulse> {

    private EditImpulseValueViewModel _editImpulseValueViewModel;
    private EditSchedulerViewModel _editSchedulerViewModel;

    public EditAutomationSchedulerImpulseViewModel(Context context, MainViewModelsFactory factory,
                                                   NavigationService navigationService,
                                                   AutomationAddedListener listener,
                                                   SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationSchedulerImpulseViewModel(Context context, MainViewModelsFactory factory,
                                                   NavigationService navigationService,
                                                   AutomationAddedListener listener,
                                                   SensorItemViewModel sensor, AutomationSchedulerImpulse automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editImpulseValueViewModel = factory.createEditImpulseValueViewModel(sensor);
        _editSchedulerViewModel = factory.createEditSchedulerViewModel(sensor);
    }

    @Override
    protected AutomationSchedulerImpulse createAutomation() {
        int value = _editImpulseValueViewModel.buildImpulseValue();
        SchedulerTrigger trigger = _editSchedulerViewModel.buildSchedulerTrigger();
        return new AutomationSchedulerImpulse(getIndex(), trigger, value, isEnabled());
    }

    @Override
    protected void applyAutomationChanges(AutomationSchedulerImpulse automation) {
        _editImpulseValueViewModel.applyImpulseValue(automation.getValue());
        _editSchedulerViewModel.applySchedulerTrigger(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationSchedulerImpulseBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerImpulseBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_impulse, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditImpulseValueViewModel getEditImpulseValueViewModel() {
        return _editImpulseValueViewModel;
    }

    @Bindable
    public EditSchedulerViewModel getEditSchedulerViewModel() {
        return _editSchedulerViewModel;
    }
}
