package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerRgbBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationSchedulerRGB;
import eu.geekhome.asymptote.model.RGBValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerRGBViewModel extends EditAutomationSchedulerViewModelBase<FragmentEditAutomationSchedulerRgbBinding, RGBValue> {

    public EditAutomationSchedulerRGBViewModel(Context context, MainViewModelsFactory factory,
                                               NavigationService navigationService,
                                               AutomationAddedListener listener,
                                               SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    @Override
    protected EditValueViewModelBase<RGBValue> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        return factory.createEditRGBValueViewModel(sensor);
    }

    public EditAutomationSchedulerRGBViewModel(Context context, MainViewModelsFactory factory,
                                               NavigationService navigationService,
                                               AutomationAddedListener listener,
                                               SensorItemViewModel sensor, AutomationSchedulerRGB automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected Automation<SchedulerTrigger, RGBValue> createAutomation(SchedulerTrigger trigger, RGBValue value) {
        return new AutomationSchedulerRGB(getIndex(), trigger, value, isEnabled());
    }

    @Override
    public FragmentEditAutomationSchedulerRgbBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerRgbBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_rgb, container, false);
        binding.setVm(this);
        return binding;
    }
}
