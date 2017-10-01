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

public abstract class EditAutomationViewModelBase<T extends ViewDataBinding, A extends Automation> extends ViewModel<T> {


    private HelpActionBarViewModel _actionBarModel;
    private final AutomationAddedListener _listener;
    private final SensorItemViewModel _sensor;
    private int _index;
    private final NavigationService _navigationService;
    private final Context _context;
    private boolean _editMode;

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
        createSubmodels(factory, sensor);
        setEditMode(false);
    }

    EditAutomationViewModelBase(Context context, MainViewModelsFactory factory,
                                NavigationService navigationService,
                                AutomationAddedListener listener,
                                SensorItemViewModel sensor, A automation) {
        this(context, factory, navigationService, listener, sensor, automation.getIndex());
        createSubmodels(factory, sensor);
        setIndex(automation.getIndex());
        setEditMode(true);
        applyAutomationChanges(automation);
    }

    protected abstract void createSubmodels(MainViewModelsFactory factory, SensorItemViewModel sensor);

    protected abstract void applyAutomationChanges(A automation);

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
            Automation automation = createAutomation();
            if (isEditMode()) {
                _listener.onAutomationEdit(automation);
            } else {
                _listener.onAutomationAdded(automation);
            }
        }

        _navigationService.goBack();
    }

    protected abstract A createAutomation();

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
}
