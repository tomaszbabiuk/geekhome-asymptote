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
import eu.geekhome.asymptote.databinding.DialogChooseAutomationBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;
import eu.geekhome.asymptote.utils.KeyboardHelper;

public class ChooseAutomationViewModel extends ViewModel<DialogChooseAutomationBinding> {
    private final MainViewModelsFactory _factory;
    private final NavigationService _navigationService;
    private final AutomationAddedListener _listener;
    private final int _index;
    private final SensorItemViewModel _sensor;

    private AutomationType _selectedAutomationType;
    private ObservableArrayList<LayoutHolder> _automationTypes;


    public ChooseAutomationViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                                     AutomationAddedListener listener, int index, SensorItemViewModel sensor) {
        _factory = factory;
        _navigationService = navigationService;
        _listener = listener;
        _index = index;
        _sensor = sensor;
        _automationTypes = createAutomationTypes(sensor);
        selectTrigger(AutomationType.DateTimeOfRelay);
    }

    private ObservableArrayList<LayoutHolder> createAutomationTypes(SensorItemViewModel sensor) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();
        AutomationTypeItemViewModel exactTriggerOfRelay = new AutomationTypeItemViewModel(this, sensor, AutomationType.DateTimeOfRelay);
        AutomationTypeItemViewModel exactTriggerOfTemperature = new AutomationTypeItemViewModel(this, sensor, AutomationType.DateTimeOfTemperature);
        AutomationTypeItemViewModel exactTriggerOfHumidity = new AutomationTypeItemViewModel(this, sensor, AutomationType.DateTimeOfHumidity);
        AutomationTypeItemViewModel scheduleTriggerOfRelay = new AutomationTypeItemViewModel(this, sensor, AutomationType.SchedulerOfRelay);
        AutomationTypeItemViewModel scheduleTriggerOfTemperature = new AutomationTypeItemViewModel(this, sensor, AutomationType.SchedulerOfTemperature);
        AutomationTypeItemViewModel scheduleTriggerOfHumidity = new AutomationTypeItemViewModel(this, sensor, AutomationType.SchedulerOfHumidity);
        result.add(exactTriggerOfRelay);
        result.add(exactTriggerOfTemperature);
        result.add(exactTriggerOfHumidity);
        result.add(scheduleTriggerOfRelay);
        result.add(scheduleTriggerOfTemperature);
        result.add(scheduleTriggerOfHumidity);
        return result;
    }

    @Override
    public DialogChooseAutomationBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        DialogChooseAutomationBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.dialog_choose_automation, container, false);
        binding.setVm(this);
        return binding;
    }

    public void close() {
        KeyboardHelper.hideKeyboard(getBinding().getRoot());
        _navigationService.goBack();
    }

    public void ok() {
        _navigationService.goBack();
        switch (getSelectedAutomationType()) {
            case DateTimeOfRelay:
                EditAutomationDateTimeRelayViewModel modelDR = _factory.createEditAutomationDateTimeRelayViewModel(_listener, _sensor, _index);
                _navigationService.showViewModel(modelDR, new ShowBackButtonInToolbarViewParam());
                break;
            case DateTimeOfTemperature:
                EditAutomationDateTimeTemperatureViewModel modelDT = _factory.createEditAutomationDateTimeTemperatureViewModel(_listener, _sensor, _index);
                _navigationService.showViewModel(modelDT, new ShowBackButtonInToolbarViewParam());
                break;
            case DateTimeOfHumidity:
                EditAutomationDateTimeHumidityViewModel modelDH = _factory.createEditAutomationDateTimeHumidityViewModel(_listener, _sensor, _index);
                _navigationService.showViewModel(modelDH, new ShowBackButtonInToolbarViewParam());
                break;
            case SchedulerOfRelay:
                EditAutomationSchedulerRelayViewModel modelSR = _factory.createEditAutomationSchedulerRelayViewModel(_listener, _sensor, _index);
                _navigationService.showViewModel(modelSR, new ShowBackButtonInToolbarViewParam());
                break;
            case SchedulerOfTemperature:
                EditAutomationSchedulerTemperatureViewModel modelST = _factory.createEditAutomationSchedulerTemperatureViewModel(_listener, _sensor, _index);
                _navigationService.showViewModel(modelST, new ShowBackButtonInToolbarViewParam());
                break;
            case SchedulerOfHumidity:
                EditAutomationSchedulerHumidityViewModel modelSH = _factory.createEditAutomationSchedulerHumidityViewModel(_listener, _sensor, _index);
                _navigationService.showViewModel(modelSH, new ShowBackButtonInToolbarViewParam());
                break;
        }
    }

    @Bindable
    private void setSelectedAutomationType(AutomationType triggerType) {
        _selectedAutomationType = triggerType;
        notifyPropertyChanged(BR.selectedAutomationType);
    }

    @Bindable
    private AutomationType getSelectedAutomationType() {
        return _selectedAutomationType;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getAutomationTypes() {
        return _automationTypes;
    }

    void selectTrigger(AutomationType triggerType) {
        if (_automationTypes != null) {
            for (LayoutHolder holderItem : _automationTypes) {
                AutomationTypeItemViewModel roleItem = (AutomationTypeItemViewModel) holderItem;
                roleItem.setSelected(roleItem.getAutomationType() == triggerType);
            }
        }
        setSelectedAutomationType(triggerType);
    }
}
