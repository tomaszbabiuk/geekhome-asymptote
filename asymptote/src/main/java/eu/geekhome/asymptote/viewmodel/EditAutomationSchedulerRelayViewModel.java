package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerRelayBinding;
import eu.geekhome.asymptote.model.AutomationSchedulerRelay;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerRelayViewModel extends EditAutomationViewModelBase<FragmentEditAutomationSchedulerRelayBinding, AutomationSchedulerRelay> {

    private EditRelayValueViewModel _editRelayValueViewModel;
    private EditSchedulerViewModel _editSchedulerViewModel;

    public EditAutomationSchedulerRelayViewModel(Context context, MainViewModelsFactory factory,
                                                 NavigationService navigationService,
                                                 AutomationAddedListener listener,
                                                 SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationSchedulerRelayViewModel(Context context, MainViewModelsFactory factory,
                                                 NavigationService navigationService,
                                                 AutomationAddedListener listener,
                                                 SensorItemViewModel sensor, AutomationSchedulerRelay automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editRelayValueViewModel = factory.createEditRelayValueViewModel(sensor);
        _editSchedulerViewModel = factory.createEditSchedulerViewModel(sensor);
    }

    @Override
    protected AutomationSchedulerRelay createAutomation() {
        RelayValue value = _editRelayValueViewModel.buildRelayValue();
        SchedulerTrigger trigger = _editSchedulerViewModel.buildSchedulerTrigger();
        return new AutomationSchedulerRelay(getIndex(), trigger, value);
    }

    @Override
    protected void applyAutomationChanges(AutomationSchedulerRelay automation) {
        _editRelayValueViewModel.applyRelayValue(automation.getValue());
        _editSchedulerViewModel.applySchedulerTrigger(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationSchedulerRelayBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerRelayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_relay, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditRelayValueViewModel getEditRelayValueViewModel() {
        return _editRelayValueViewModel;
    }

    @Bindable
    public EditSchedulerViewModel getEditSchedulerViewModel() {
        return _editSchedulerViewModel;
    }
}
