package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeTemperatureBinding;
import eu.geekhome.asymptote.model.AutomationDateTimeTemperature;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeTemperatureViewModel extends EditAutomationViewModelBase<FragmentEditAutomationDatetimeTemperatureBinding, AutomationDateTimeTemperature> {

    private EditTemperatureValueViewModel _editTemperatureValueViewModel;
    private EditDateTimeViewModel _editDateTimeViewModel;

    public EditAutomationDateTimeTemperatureViewModel(Context context, MainViewModelsFactory factory,
                                                      NavigationService navigationService,
                                                      AutomationAddedListener listener,
                                                      SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationDateTimeTemperatureViewModel(Context context, MainViewModelsFactory factory,
                                                      NavigationService navigationService,
                                                      AutomationAddedListener listener,
                                                      SensorItemViewModel sensor, AutomationDateTimeTemperature automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editTemperatureValueViewModel = factory.createEditTemperatureValueViewModel(sensor);
        _editDateTimeViewModel = factory.createEditDateTimeViewModel(sensor);
    }


    @Override
    protected AutomationDateTimeTemperature createAutomation() {
        ParamValue relayValue = _editTemperatureValueViewModel.buildTemperatureValue();
        DateTimeTrigger dateTimeTrigger = _editDateTimeViewModel.buildDateTimeTrigger();
        return new AutomationDateTimeTemperature(getIndex(), dateTimeTrigger, relayValue);
    }

    @Override
    protected void applyAutomationChanges(AutomationDateTimeTemperature automation) {
        _editTemperatureValueViewModel.applyTemperatureValue(automation.getValue());
        _editDateTimeViewModel.applyDateTime(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationDatetimeTemperatureBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeTemperatureBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_temperature, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditTemperatureValueViewModel getEditTemperatureValueViewModel() {
        return _editTemperatureValueViewModel;
    }

    @Bindable
    public EditDateTimeViewModel getEditDateTimeViewModel() {
        return _editDateTimeViewModel;
    }
}
