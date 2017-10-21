package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimePwmBinding;
import eu.geekhome.asymptote.model.AutomationDateTimePWM;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.PWMValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimePWMViewModel extends EditAutomationViewModelBase<FragmentEditAutomationDatetimePwmBinding, AutomationDateTimePWM> {

    private EditPWMValueViewModel _editPWMValueViewModel;
    private EditDateTimeViewModel _editDateTimeViewModel;

    public EditAutomationDateTimePWMViewModel(Context context, MainViewModelsFactory factory,
                                              NavigationService navigationService,
                                              AutomationAddedListener listener,
                                              SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    public EditAutomationDateTimePWMViewModel(Context context, MainViewModelsFactory factory,
                                              NavigationService navigationService,
                                              AutomationAddedListener listener,
                                              SensorItemViewModel sensor, AutomationDateTimePWM automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editPWMValueViewModel = factory.createEditPWMValueViewModel(sensor);
        _editDateTimeViewModel = factory.createEditDateTimeViewModel(sensor);
    }


    @Override
    protected AutomationDateTimePWM createAutomation() {
        PWMValue pwmValue = _editPWMValueViewModel.buildPWMValue();
        DateTimeTrigger dateTimeTrigger = _editDateTimeViewModel.buildDateTimeTrigger();
        return new AutomationDateTimePWM(getIndex(), dateTimeTrigger, pwmValue, isEnabled());
    }

    @Override
    protected void applyAutomationChanges(AutomationDateTimePWM automation) {
        _editPWMValueViewModel.applyPWMValue(automation.getValue());
        _editDateTimeViewModel.applyDateTime(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationDatetimePwmBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimePwmBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_pwm, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public EditPWMValueViewModel getEditPWMValueViewModel() {
        return _editPWMValueViewModel;
    }

    @Bindable
    public EditDateTimeViewModel getEditDateTimeViewModel() {
        return _editDateTimeViewModel;
    }
}
