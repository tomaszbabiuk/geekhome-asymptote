package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeRelayBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeRelayValueViewModel extends ViewModel<FragmentEditAutomationDatetimeRelayBinding> {


    private HelpActionBarViewModel _actionBarModel;
    private final AutomationAddedListener _listener;
    private final SensorItemViewModel _sensor;
    private int _index;
    private final NavigationService _navigationService;
    private final Context _context;
    private boolean _editMode;
    private EditRelayValueViewModel _editRelayValueViewModel;
    private EditDateTimeViewModel _editDateTimeViewModel;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public EditAutomationDateTimeRelayValueViewModel(Context context, MainViewModelsFactory factory,
                                                     NavigationService navigationService,
                                                     AutomationAddedListener listener,
                                                     SensorItemViewModel sensor, int index) {
        _context = context;
        _navigationService = navigationService;
        _listener = listener;
        _sensor = sensor;
        _index = index;
        _actionBarModel = factory.createHelpActionBarModel();
        _editRelayValueViewModel = factory.createEditRelayValueViewModel(sensor);
        _editDateTimeViewModel = factory.createEditDateTimeViewModel(sensor);
        setEditMode(false);
    }

    public EditAutomationDateTimeRelayValueViewModel(Context context, MainViewModelsFactory factory,
                                                     NavigationService navigationService,
                                                     AutomationAddedListener listener,
                                                     SensorItemViewModel sensor, AutomationDateTimeRelay automation) {
        this(context, factory, navigationService, listener, sensor, automation.getIndex());
        applyAutomationChanges(automation);
    }


    private void applyAutomationChanges(AutomationDateTimeRelay automation) {
        setIndex(automation.getIndex());
        setEditMode(true);
        _editRelayValueViewModel.applyRelayValue(automation.getValue());
        _editDateTimeViewModel.applyDateTime(automation.getTrigger());
    }

    @Override
    public FragmentEditAutomationDatetimeRelayBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationDatetimeRelayBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation_datetime_relay, container, false);
        binding.setVm(this);
        return binding;
    }

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
            RelayValue relayValue = _editRelayValueViewModel.buildRelayValue();
            DateTimeTrigger dateTimeTrigger = _editDateTimeViewModel.buildDataTimeTrigger();
            Automation<DateTimeTrigger, RelayValue> automation =
                    new AutomationDateTimeRelay(_context, _index, dateTimeTrigger, relayValue);
            if (isEditMode()) {
                _listener.onAutomationEdit(automation);
            } else {
                _listener.onAutomationAdded(automation);
            }
        }

        _navigationService.goBack();
    }

    @Bindable
    public EditRelayValueViewModel getEditRelayValueViewModel() {
        return _editRelayValueViewModel;
    }

    @Bindable
    public EditDateTimeViewModel getEditDateTimeViewModel() {
        return _editDateTimeViewModel;
    }

    public void setIndex(int index) {
        _index = index;
    }
}
