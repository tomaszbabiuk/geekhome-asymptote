package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.DialogMoreBinding;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class MoreViewModel extends ViewModel<DialogMoreBinding> {
    private final MainViewModelsFactory _factory;
    private final GeneralDialogService _generalDialogService;
    private final EmergencyManager _emergencyManager;
    private final SensorItemViewModel _sensor;
    private final NavigationService _navigationService;


    public MoreViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                         GeneralDialogService generalDialogService, EmergencyManager emergencyManager,
                         SensorItemViewModel sensor) {
        _factory = factory;
        _generalDialogService = generalDialogService;
        _emergencyManager = emergencyManager;
        _sensor = sensor;
        _navigationService = navigationService;
    }

    @Override
    public DialogMoreBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        DialogMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_more, container, false);
        binding.setVm(this);
        return binding;
    }

    public void close() {
        _navigationService.clearOverlay();
    }

    public void settings() {
        _navigationService.clearOverlay();
        EditSensorViewModel renameModel = _factory.createEditSensorViewModel(_sensor);
        _navigationService.showViewModel(renameModel, new ShowBackButtonInToolbarViewParam());
    }

    public void automation() {
        _navigationService.clearOverlay();

        EditAutomationViewModel triggersModel = _factory.createEditTriggersViewModel(_sensor);
        _navigationService.showViewModel(triggersModel, new ShowBackButtonInToolbarViewParam());
    }

    public void changeFirmware() {
        _navigationService.clearOverlay();

        if (_emergencyManager.isEmergency()) {
            _generalDialogService.showOKDialog(R.string.sign_in_to_install, null);
        } else {
            if (!_sensor.getSyncData().isLocked() && _sensor.getSyncData().getSystemInfo().getVariant().isWifi()) {
                _generalDialogService.showOKDialog(R.string.device_locked_not_upgradable, null);
            } else {
                ChangeFirmwareViewModel changeFirmwareModel = _factory.createChangeFirmwareViewModel(_sensor);
                _navigationService.showViewModel(changeFirmwareModel, new ShowBackButtonInToolbarViewParam());
            }
        }
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

}
