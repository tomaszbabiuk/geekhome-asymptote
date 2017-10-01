package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.DialogChooseTriggerBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;
import eu.geekhome.asymptote.utils.KeyboardHelper;

public class ChooseTriggerViewModel extends ViewModel<DialogChooseTriggerBinding> {
    private final MainViewModelsFactory _factory;
    private final NavigationService _navigationService;
    private final AutomationAddedListener _listener;
    private final int _index;
    private final SensorItemViewModel _sensor;

    private TriggerType _selectedTriggerType;
    private ObservableArrayList<LayoutHolder> _triggerTypes;


    public ChooseTriggerViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                                  AutomationAddedListener listener, int index, SensorItemViewModel sensor) {
        _factory = factory;
        _navigationService = navigationService;
        _listener = listener;
        _index = index;
        _sensor = sensor;
        _triggerTypes = createTriggerTypes(sensor);
        selectTrigger(TriggerType.DateTimeOfRelay);
    }

    private ObservableArrayList<LayoutHolder> createTriggerTypes(SensorItemViewModel sensor) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();
        TriggerTypeItemViewModel exactTrigger = new TriggerTypeItemViewModel(this, sensor, TriggerType.DateTimeOfRelay);
        TriggerTypeItemViewModel scheduleTrigger = new TriggerTypeItemViewModel(this, sensor, TriggerType.SchedulerOfRelay);
        result.add(exactTrigger);
        result.add(scheduleTrigger);
        return result;
    }

    @Override
    public DialogChooseTriggerBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        DialogChooseTriggerBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.dialog_choose_trigger, container, false);
        binding.setVm(this);
        return binding;
    }

    public void close() {
        KeyboardHelper.hideKeyboard(getBinding().getRoot());
        _navigationService.goBack();
    }

    public void ok() {
        _navigationService.goBack();
        switch (getSelectedTriggerType()) {
            case DateTimeOfRelay:
                EditAutomationDateTimeRelayViewModel modelDR = _factory.createEditAutomationDateTimeRelayViewModel(_listener, _sensor, _index);
                _navigationService.showViewModel(modelDR, new ShowBackButtonInToolbarViewParam());
                break;
            case SchedulerOfRelay:
                EditAutomationSchedulerRelayViewModel modelSR = _factory.createEditAutomationSchedulerRelayViewModel(_listener, _sensor, _index);
                _navigationService.showViewModel(modelSR);
        }
    }

    @Bindable
    private void setSelectedTriggerType(TriggerType triggerType) {
        _selectedTriggerType = triggerType;
        notifyPropertyChanged(BR.selectedTriggerType);
    }

    @Bindable
    private TriggerType getSelectedTriggerType() {
        return _selectedTriggerType;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getTriggerTypes() {
        return _triggerTypes;
    }

    void selectTrigger(TriggerType triggerType) {
        if (_triggerTypes != null) {
            for (LayoutHolder holderItem : _triggerTypes) {
                TriggerTypeItemViewModel roleItem = (TriggerTypeItemViewModel) holderItem;
                roleItem.setSelected(roleItem.getTriggerType() == triggerType);
            }
        }
        setSelectedTriggerType(triggerType);
    }
}
