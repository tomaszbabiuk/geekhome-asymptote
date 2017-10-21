package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationSchedulerPwmBinding;
import eu.geekhome.asymptote.model.AutomationSchedulerPWM;
import eu.geekhome.asymptote.model.PWMValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationSchedulerPWMViewModel extends EditAutomationViewModelBase<FragmentEditAutomationSchedulerPwmBinding, AutomationSchedulerPWM> {

    private EditPWMValueViewModel _editPWMValueViewModel;
    private EditSchedulerViewModel _editSchedulerViewModel;

    public EditAutomationSchedulerPWMViewModel(Context context, MainViewModelsFactory factory,
                                               NavigationService navigationService,
                                               AutomationAddedListener listener,
                                               SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationSchedulerPWMViewModel(Context context, MainViewModelsFactory factory,
                                               NavigationService navigationService,
                                               AutomationAddedListener listener,
                                               SensorItemViewModel sensor, AutomationSchedulerPWM automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editPWMValueViewModel = factory.createEditPWMValueViewModel(sensor);
        _editSchedulerViewModel = factory.createEditSchedulerViewModel(sensor);
    }

    @Override
    protected AutomationSchedulerPWM createAutomation() {
        PWMValue value = _editPWMValueViewModel.buildPWMValue();
        SchedulerTrigger trigger = _editSchedulerViewModel.buildSchedulerTrigger();
        return new AutomationSchedulerPWM(getIndex(), trigger, value, isEnabled());
    }

    @Override
    protected void applyAutomationChanges(AutomationSchedulerPWM automation) {
        _editPWMValueViewModel.applyPWMValue(automation.getValue());
        _editSchedulerViewModel.applySchedulerTrigger(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationSchedulerPwmBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationSchedulerPwmBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_scheduler_pwm, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditPWMValueViewModel getEditPWMValueViewModel() {
        return _editPWMValueViewModel;
    }

    @Bindable
    public EditSchedulerViewModel getEditSchedulerViewModel() {
        return _editSchedulerViewModel;
    }
}
