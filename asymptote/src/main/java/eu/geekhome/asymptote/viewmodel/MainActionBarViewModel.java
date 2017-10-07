package eu.geekhome.asymptote.viewmodel;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.LogoutService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class MainActionBarViewModel {
    private final NavigationService _navigationService;
    private final GeneralDialogService _dialogService;
    private final EmergencyManager _emergencyManager;
    private final LogoutService _logoutService;
    private final MainViewModelsFactory _factory;

    public MainActionBarViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                                  GeneralDialogService generalDialogService, EmergencyManager emergencyManager,
                                  LogoutService logoutService) {

        _factory = factory;
        _navigationService = navigationService;
        _dialogService = generalDialogService;
        _emergencyManager = emergencyManager;
        _logoutService = logoutService;
    }

    public void pairSensor() {
        TouchConfigurationViewModel configModel = _factory.createTouchConfigurationViewModel();
        _navigationService.showViewModel(configModel, new ShowBackButtonInToolbarViewParam());
    }

    public void showMyAccount() {
        if (_emergencyManager.isEmergency()) {
            _dialogService.showOKDialog(R.string.cannot_access_profile_when_emergency, null);
        } else {
            ProfileViewModel profileViewModel = _factory.createProfileViewModel();
            _navigationService.showViewModel(profileViewModel, new ShowBackButtonInToolbarViewParam());
        }
    }

    public void showTroubleshooting() {
        TroubleshootingViewModel troubleshootingViewModel = _factory.createTroubleshootingViewModel();
        _navigationService.showViewModel(troubleshootingViewModel, new ShowBackButtonInToolbarViewParam());
    }

    public void logout() {
        _logoutService.logout();
    }
}
