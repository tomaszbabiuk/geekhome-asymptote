package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeHumidityBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeHumidity;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeHumidityViewModel extends EditAutomationDateTimeViewModelBase<FragmentEditAutomationDatetimeHumidityBinding, ParamValue> {

    public EditAutomationDateTimeHumidityViewModel(Context context, MainViewModelsFactory factory,
                                                   NavigationService navigationService,
                                                   AutomationAddedListener listener,
                                                   SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<ParamValue> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditHumidityValueViewModel(sensor);
    }

    public EditAutomationDateTimeHumidityViewModel(Context context, MainViewModelsFactory factory,
                                                   NavigationService navigationService,
                                                   AutomationAddedListener listener,
                                                   SensorItemViewModel sensor, AutomationDateTimeHumidity automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected Automation<DateTimeTrigger, ParamValue> createAutomation(DateTimeTrigger trigger, ParamValue value) {
        return new AutomationDateTimeHumidity(getIndex(), trigger, value, isEnabled());

    }

    @Override
    public FragmentEditAutomationDatetimeHumidityBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeHumidityBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_humidity, container, false);
        binding.setVm(this);
        return binding;
    }
}
