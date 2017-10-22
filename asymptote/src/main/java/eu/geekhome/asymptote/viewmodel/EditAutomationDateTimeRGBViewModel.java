package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeRgbBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeRGB;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.RGBValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeRGBViewModel extends EditAutomationDateTimeViewModelBase<FragmentEditAutomationDatetimeRgbBinding, RGBValue> {

    public EditAutomationDateTimeRGBViewModel(Context context, MainViewModelsFactory factory,
                                              NavigationService navigationService,
                                              AutomationAddedListener listener,
                                              SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<RGBValue> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditRGBValueViewModel(sensor);
    }

    public EditAutomationDateTimeRGBViewModel(Context context, MainViewModelsFactory factory,
                                              NavigationService navigationService,
                                              AutomationAddedListener listener,
                                              SensorItemViewModel sensor, Automation<DateTimeTrigger, RGBValue> automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected AutomationDateTimeRGB createAutomation(DateTimeTrigger trigger, RGBValue value) {
        return new AutomationDateTimeRGB(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationDatetimeRgbBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeRgbBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_rgb, container, false);
        binding.setVm(this);
        return binding;
    }
}
