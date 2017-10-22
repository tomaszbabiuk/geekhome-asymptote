package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

abstract class EditAutomationSchedulerViewModelBase<B extends ViewDataBinding, V> extends EditAutomationViewModelBase<B, SchedulerTrigger, V> {

    private EditSchedulerViewModel _editSchedulerViewModel;

    EditAutomationSchedulerViewModelBase(Context context, MainViewModelsFactory factory,
                                         NavigationService navigationService,
                                         AutomationAddedListener listener,
                                         SensorItemViewModel sensor, int index) {
        super(context, factory, navigationService, listener, sensor, index);
    }

    EditAutomationSchedulerViewModelBase(Context context, MainViewModelsFactory factory,
                                         NavigationService navigationService,
                                         AutomationAddedListener listener,
                                         SensorItemViewModel sensor, Automation<SchedulerTrigger, V> automation) {
        super(context, factory, navigationService, listener, sensor, automation);
    }

    @Override
    protected void applyTriggerChanges(Automation<SchedulerTrigger, V> automation) {
        _editSchedulerViewModel.applySchedulerTrigger(automation.getTrigger());
    }

    @Override
    protected void createTriggerViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor) {
        _editSchedulerViewModel = factory.createEditSchedulerViewModel(sensor);
    }

    @Override
    protected SchedulerTrigger createTrigger() {
        return _editSchedulerViewModel.buildSchedulerTrigger();
    }

    @Bindable
    public EditSchedulerViewModel getEditSchedulerViewModel() {
        return _editSchedulerViewModel;
    }
}
