package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeRelayBinding;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeRelayViewModel extends EditAutomationViewModelBase<FragmentEditAutomationDatetimeRelayBinding, AutomationDateTimeRelay> {

    private EditRelayValueViewModel _editRelayValueViewModel;
    private EditDateTimeViewModel _editDateTimeViewModel;

    public EditAutomationDateTimeRelayViewModel(Context context, MainViewModelsFactory factory,
                                                NavigationService navigationService,
                                                AutomationAddedListener listener,
                                                SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationDateTimeRelayViewModel(Context context, MainViewModelsFactory factory,
                                                NavigationService navigationService,
                                                AutomationAddedListener listener,
                                                SensorItemViewModel sensor, AutomationDateTimeRelay automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editRelayValueViewModel = factory.createEditRelayValueViewModel(sensor);
        _editDateTimeViewModel = factory.createEditDateTimeViewModel(sensor);
    }


    @Override
    protected AutomationDateTimeRelay createAutomation() {
        RelayValue relayValue = _editRelayValueViewModel.buildRelayValue();
        DateTimeTrigger dateTimeTrigger = _editDateTimeViewModel.buildDateTimeTrigger();
        return new AutomationDateTimeRelay(getContext(), getIndex(), dateTimeTrigger, relayValue);
    }

    @Override
    protected void applyAutomationChanges(AutomationDateTimeRelay automation) {
        _editRelayValueViewModel.applyRelayValue(automation.getValue());
        _editDateTimeViewModel.applyDateTime(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationDatetimeRelayBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeRelayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_relay, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditRelayValueViewModel getEditRelayValueViewModel() {
        return _editRelayValueViewModel;
    }

    @Bindable
    public EditDateTimeViewModel getEditDateTimeViewModel() {
        return _editDateTimeViewModel;
    }
}
