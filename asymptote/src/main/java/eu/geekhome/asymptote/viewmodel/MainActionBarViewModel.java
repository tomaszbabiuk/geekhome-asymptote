package eu.geekhome.asymptote.viewmodel;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.NavigationService;

public class MainActionBarViewModel {
    private final ActivityComponent _activityComponent;

    @Inject
    NavigationService _navigationService;
    @Inject
    GeneralDialogService _dialogService;
    @Inject
    EmergencyManager _emergencyManager;

    public MainActionBarViewModel(ActivityComponent activityComponent) {
        activityComponent.inject(this);
        _activityComponent = activityComponent;
    }

    public void pairSensor() {
        TouchConfigurationViewModel configModel = new TouchConfigurationViewModel(_activityComponent);
        _navigationService.showViewModel(configModel, new ShowBackButtonInToolbarViewParam());
    }

    public void showMyAccount() {
        if (_emergencyManager.isEmergency()) {
            _dialogService.showOKDialog(R.string.cannot_access_profile_when_emergency, null);
        } else {
            _navigationService.showViewModel(new ProfileViewModel(_activityComponent),
                    new ShowBackButtonInToolbarViewParam());
        }
    }

    public void showTroubleshooting() {
        _navigationService.showViewModel(new TroubleshootingViewModel(_activityComponent), new ShowBackButtonInToolbarViewParam());
    }
}
