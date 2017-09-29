package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationDatetimeRelayBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationDateTimeRelayValueViewModel extends ViewModel<FragmentEditAutomationDatetimeRelayBinding> {


    private HelpActionBarViewModel _actionBarModel;
    private final AutomationAddedListener _listener;
    private final SensorItemViewModel _sensor;
    private final int _index;
    private final NavigationService _navigationService;
    private final MainViewModelsFactory _factory;
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

    public EditAutomationDateTimeRelayValueViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                                                     AutomationAddedListener listener,
                                                     SensorItemViewModel sensor, int index) {
        _factory = factory;
        _navigationService = navigationService;
        _listener = listener;
        _sensor = sensor;
        _index = index;
        _actionBarModel = _factory.createHelpActionBarModel();
        _editRelayValueViewModel = _factory.createEditRelayValueViewModel(sensor);
        _editDateTimeViewModel = _factory.createEditDateTimeViewModel(sensor);
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

    public void setEditMode(boolean editMode) {
        _editMode = editMode;
        notifyPropertyChanged(BR.editMode);
    }

    public void done() {
        if (_listener != null) {
            RelayValue relayValue = _editRelayValueViewModel.buildRelayValue();
            DateTimeTrigger dateTimeTrigger = _editDateTimeViewModel.buildDataTimeTrigger();
            Automation<DateTimeTrigger, RelayValue> automation = new Automation<>(_index, dateTimeTrigger, relayValue);
            _listener.onAutomationAdded(automation);
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
}
