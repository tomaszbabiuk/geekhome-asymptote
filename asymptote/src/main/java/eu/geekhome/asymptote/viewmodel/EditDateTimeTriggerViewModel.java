package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentEditDatetimeTriggerBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditDateTimeTriggerViewModel extends ViewModel<FragmentEditDatetimeTriggerBinding> {

    private HelpActionBarViewModel _actionBarModel;
    private final SensorItemViewModel _sensor;
    private final NavigationService _navigationService;
    private final MainViewModelsFactory _factory;

    private boolean _onOffValue;
    private boolean _editMode;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public EditDateTimeTriggerViewModel(MainViewModelsFactory factory, NavigationService navigationService, SensorItemViewModel sensor) {
        _factory = factory;
        _navigationService = navigationService;
        _sensor = sensor;
        _actionBarModel = _factory.createHelpActionBarModel();
    }

    @Override
    public FragmentEditDatetimeTriggerBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditDatetimeTriggerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_datetime_trigger, container, false);
        binding.setVm(this);
        binding.picker.setStepMinutes(1);
        return binding;
    }

    @Bindable
    public boolean isOnOffValue() {
        return _onOffValue;
    }

    public void setOnOffValue(boolean onOffValue) {
        _onOffValue = onOffValue;
        notifyPropertyChanged(BR.onOffValue);
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
        _navigationService.goBack();
    }
}
