package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import javax.inject.Inject;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.NavigationService;

abstract class XStatRoleDetailsViewModelBase<T extends ViewDataBinding> extends ViewModel<T> {
    private final boolean _reset;
    private final EditSensorViewModel _parent;
    private HelpActionBarViewModel _actionBarModel;
    private String _title;
    private String _instruction;
    private SensorItemViewModel _sensor;

    @Inject
    NavigationService _navigationService;


    public XStatRoleDetailsViewModelBase(ActivityComponent activityComponent, EditSensorViewModel parent,
                                         SensorItemViewModel sensor,
                                         String title, String instruction, boolean reset) {
        super(activityComponent);
        _parent = parent;
        _sensor = sensor;
        _reset = reset;
        setTitle(title);
        setInstruction(instruction);
        _actionBarModel = new HelpActionBarViewModel(activityComponent);
    }

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getInstruction() {
        return _instruction;
    }

    public void setInstruction(String instruction) {
        _instruction = instruction;
        notifyPropertyChanged(BR.instruction);
    }

    protected boolean isReset() {
        return _reset;
    }

    protected SensorItemViewModel getSensor() {
        return _sensor;
    }

    protected NavigationService getNavigationService() {
        return _navigationService;
    }

    protected EditSensorViewModel getParent() {
        return _parent;
    }
}
