package eu.geekhome.asymptote.services;

import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.ViewParam;
import eu.geekhome.asymptote.model.UserSnapshot;

public interface NavigationService {
    void showViewModel(ViewModel model, ViewParam... params);
    void showOverlayViewModel(ViewModel model, ViewParam... params);
    boolean goingBack();
    void goBack();
    void goBackTo(Class viewModelClass);
    void clearOverlay();
    void endMainPresentation();
    void startMainPresentation(String userId, boolean emergency, UserSnapshot userSnapshot);
    void showTermsAndPrivacy();
}
