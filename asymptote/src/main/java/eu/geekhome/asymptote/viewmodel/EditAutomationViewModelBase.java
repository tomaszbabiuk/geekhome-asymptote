package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public abstract class EditAutomationViewModelBase<B extends ViewDataBinding, T, V> extends ViewModel<B> {

    private HelpActionBarViewModel _actionBarModel;
    private final AutomationAddedListener _listener;
    private final SensorItemViewModel _sensor;
    private int _index;
    private final NavigationService _navigationService;
    private final Context _context;
    private boolean _editMode;
    private boolean _enabled;
    private EditValueViewModelBase<V> _editValueViewModel;

    @Bindable
    public EditValueViewModelBase<V> getEditValueViewModel() {
        return _editValueViewModel;
    }

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    EditAutomationViewModelBase(Context context, MainViewModelsFactory factory,
                                NavigationService navigationService,
                                AutomationAddedListener listener,
                                SensorItemViewModel sensor, int index) {
        _context = context;
        _navigationService = navigationService;
        _listener = listener;
        _sensor = sensor;
        _index = index;
        _actionBarModel = factory.createHelpActionBarModel();
        _editValueViewModel = createValueViewModel(factory, sensor);
        createTriggerViewModel(factory, sensor);
        setEditMode(false);
        setEnabled(true);
    }

    protected abstract EditValueViewModelBase<V> createValueViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor);

    protected abstract void createTriggerViewModel(MainViewModelsFactory factory, SensorItemViewModel sensor);

    EditAutomationViewModelBase(Context context, MainViewModelsFactory factory,
                                NavigationService navigationService,
                                AutomationAddedListener listener,
                                SensorItemViewModel sensor, Automation<T,V> automation) {
        this(context, factory, navigationService, listener, sensor, automation.getIndex());
        createTriggerViewModel(factory, sensor);
        setIndex(automation.getIndex());
        setEnabled(automation.isEnabled());
        setEditMode(true);
        _editValueViewModel.applyValue(automation.getValue());
        applyTriggerChanges(automation);
    }

    protected abstract void applyTriggerChanges(Automation<T,V> automation);

    @Bindable
    public boolean isEditMode() {
        return _editMode;
    }

    private void setEditMode(boolean editMode) {
        _editMode = editMode;
        notifyPropertyChanged(BR.editMode);
    }

    public void done() {
        if (_listener != null) {
            V value = _editValueViewModel.buildValue();
            T trigger = createTrigger();
            Automation<T,V> automation = createAutomation(trigger, value);
            if (isEditMode()) {
                _listener.onAutomationEdit(automation);
            } else {
                _listener.onAutomationAdded(automation);
            }
        }

        _navigationService.goBack();
    }

    protected abstract T createTrigger();

    protected abstract Automation<T,V> createAutomation(T trigger, V value);

    public void setIndex(int index) {
        _index = index;
    }

    @Bindable
    public int getIndex() {
        return _index;
    }

    protected Context getContext() {
        return _context;
    }

    @Bindable
    public boolean isEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean enabled) {
        _enabled = enabled;
        notifyPropertyChanged(BR.enabled);
    }
}
