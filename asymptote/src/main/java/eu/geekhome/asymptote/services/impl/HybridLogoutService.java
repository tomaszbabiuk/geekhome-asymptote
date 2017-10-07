package eu.geekhome.asymptote.services.impl;

import javax.inject.Inject;

import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.LogoutService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.PasswordService;

public class HybridLogoutService implements LogoutService {
    private final EmergencyManager _emergencyManager;
    private final PasswordService _passwordService;
    private final NavigationService _navigationService;
    private final CloudUserService _cloudUserService;

    @Inject
    public HybridLogoutService(EmergencyManager emergencyManager, PasswordService passwordService,
                               NavigationService navigationService, CloudUserService cloudUserService) {
        _emergencyManager = emergencyManager;
        _passwordService = passwordService;
        _navigationService = navigationService;
        _cloudUserService = cloudUserService;
    }

    @Override
    public void logout() {
        if (_emergencyManager.isEmergency()) {
            _passwordService.setRememberEmergencyPassword(false);
            _passwordService.setEmergencyPassword(null);
        } else {
            _passwordService.setRememberCloudCredentials(false);
            _passwordService.setCloudEmail(null);
            _passwordService.setCloudPassword(null);
            _cloudUserService.signOut();
        }

        _navigationService.endMainPresentation();
    }
}
