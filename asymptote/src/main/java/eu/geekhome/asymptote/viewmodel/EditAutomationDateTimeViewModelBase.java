package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

abstract class EditAutomationDateTimeViewModelBase<B extends ViewDataBinding, V> extends EditAutomationViewModelBase<B, DateTimeTrigger, V> {

    private EditDateTimeViewModel _editDateTimeViewModel;

    EditAutomationDateTimeViewModelBase(Context context, MainViewModelsFactory factory,
                                        NavigationService navigationService,
                                        AutomationAddedListener listener,
                                        SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    EditAutomationDateTimeViewModelBase(Context context, MainViewModelsFactory factory,
                                        NavigationService navigationService,
                                        AutomationAddedListener listener,
                                        SensorItemViewModel sensor, Automation<DateTimeTrigger, V> automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void applyTriggerChanges(Automation<DateTimeTrigger, V> automation) {
        _editDateTimeViewModel.applyDateTime(automation.getTrigger());
    }

    @Override
    protected void createTriggerViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editDateTimeViewModel = factory.createEditDateTimeViewModel(sensor);
    }

    @Override
    protected DateTimeTrigger createTrigger() {
        return _editDateTimeViewModel.buildDateTimeTrigger();
    }

    @Bindable
    public EditDateTimeViewModel getEditDateTimeViewModel() {
        return _editDateTimeViewModel;
    }
}
