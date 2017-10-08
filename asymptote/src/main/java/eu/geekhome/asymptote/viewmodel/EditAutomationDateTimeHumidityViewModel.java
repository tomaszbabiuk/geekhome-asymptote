package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeHumidityBinding;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeTemperatureBinding;
import eu.geekhome.asymptote.model.AutomationDateTimeHumidity;
import eu.geekhome.asymptote.model.AutomationDateTimeTemperature;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeHumidityViewModel extends EditAutomationViewModelBase<FragmentEditAutomationDatetimeHumidityBinding, AutomationDateTimeHumidity> {

    private EditHumidityValueViewModel _editHumidityValueViewModel;
    private EditDateTimeViewModel _editDateTimeViewModel;

    public EditAutomationDateTimeHumidityViewModel(Context context, MainViewModelsFactory factory,
                                                   NavigationService navigationService,
                                                   AutomationAddedListener listener,
                                                   SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationDateTimeHumidityViewModel(Context context, MainViewModelsFactory factory,
                                                   NavigationService navigationService,
                                                   AutomationAddedListener listener,
                                                   SensorItemViewModel sensor, AutomationDateTimeHumidity automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editHumidityValueViewModel = factory.createEditHumidityValueViewModel(sensor);
        _editDateTimeViewModel = factory.createEditDateTimeViewModel(sensor);
    }


    @Override
    protected AutomationDateTimeHumidity createAutomation() {
        ParamValue relayValue = _editHumidityValueViewModel.buildHumidityValue();
        DateTimeTrigger dateTimeTrigger = _editDateTimeViewModel.buildDateTimeTrigger();
        return new AutomationDateTimeHumidity(getIndex(), dateTimeTrigger, relayValue);
    }

    @Override
    protected void applyAutomationChanges(AutomationDateTimeHumidity automation) {
        _editHumidityValueViewModel.applyHumidityValue(automation.getValue());
        _editDateTimeViewModel.applyDateTime(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationDatetimeHumidityBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeHumidityBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_humidity, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditHumidityValueViewModel getEditHumidityValueViewModel() {
        return _editHumidityValueViewModel;
    }

    @Bindable
    public EditDateTimeViewModel getEditDateTimeViewModel() {
        return _editDateTimeViewModel;
    }
}
