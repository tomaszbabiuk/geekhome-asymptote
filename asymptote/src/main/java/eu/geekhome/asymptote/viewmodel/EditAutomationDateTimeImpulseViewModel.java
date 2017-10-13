package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeImpulseBinding;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeRelayBinding;
import eu.geekhome.asymptote.model.AutomationDateTimeImpulse;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeImpulseViewModel extends EditAutomationViewModelBase<FragmentEditAutomationDatetimeImpulseBinding, AutomationDateTimeImpulse> {

    private EditImpulseValueViewModel _editImpulseValueViewModel;
    private EditDateTimeViewModel _editDateTimeViewModel;

    public EditAutomationDateTimeImpulseViewModel(Context context, MainViewModelsFactory factory,
                                                  NavigationService navigationService,
                                                  AutomationAddedListener listener,
                                                  SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationDateTimeImpulseViewModel(Context context, MainViewModelsFactory factory,
                                                  NavigationService navigationService,
                                                  AutomationAddedListener listener,
                                                  SensorItemViewModel sensor, AutomationDateTimeImpulse automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editImpulseValueViewModel = factory.createEditImpulseValueViewModel(sensor);
        _editDateTimeViewModel = factory.createEditDateTimeViewModel(sensor);
    }


    @Override
    protected AutomationDateTimeImpulse createAutomation() {
        int relayValue = _editImpulseValueViewModel.buildImpulseValue();
        DateTimeTrigger dateTimeTrigger = _editDateTimeViewModel.buildDateTimeTrigger();
        return new AutomationDateTimeImpulse(getIndex(), dateTimeTrigger, relayValue, isEnabled());
    }

    @Override
    protected void applyAutomationChanges(AutomationDateTimeImpulse automation) {
        _editImpulseValueViewModel.applyImpulseValue(automation.getValue());
        _editDateTimeViewModel.applyDateTime(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationDatetimeImpulseBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeImpulseBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_impulse, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditImpulseValueViewModel getEditImpulseValueViewModel() {
        return _editImpulseValueViewModel;
    }

    @Bindable
    public EditDateTimeViewModel getEditDateTimeViewModel() {
        return _editDateTimeViewModel;
    }
}
