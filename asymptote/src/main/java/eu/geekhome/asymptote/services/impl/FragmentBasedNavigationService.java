package eu.geekhome.asymptote.services.impl;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import eu.geekhome.asymptote.MainActivity;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.SplashActivity;
import eu.geekhome.asymptote.bindingutils.BoundFragment;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.ViewParam;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.services.NavigationService;

public class FragmentBasedNavigationService implements NavigationService {
    private FragmentManager _fragmentManager;
    private Activity _activity;
    private Class _overlayClass;

    public FragmentBasedNavigationService(FragmentManager fragmentManager, Activity activity) {
        _fragmentManager = fragmentManager;
        _activity = activity;
    }

    private void showViewModel(@IdRes int containerId, ViewModel model, ViewParam... params) {
        FragmentTransaction transaction = _fragmentManager.beginTransaction();
        String tag = model.getClass().getName();
        BoundFragment fragment = model.createFragment(params);
        transaction.replace(containerId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void showViewModel(ViewModel model, ViewParam... params) {
        showViewModel(R.id.fragments_container, model, params);
    }

    @Override
    public void showOverlayViewModel(ViewModel model, ViewParam... params) {
        if (!model.getClass().equals(_overlayClass)) {
            showViewModel(R.id.overlays_container, model, params);
            _overlayClass = model.getClass();
        }
    }

    @Override
    public boolean goingBack() {
        BoundFragment topFragment = findTopFragment();
        return topFragment == null || topFragment.goingBack();
    }

    private BoundFragment findTopFragment() {
        FragmentManager.BackStackEntry backEntry = _fragmentManager.getBackStackEntryAt(_fragmentManager.getBackStackEntryCount() - 1);
        String str = backEntry.getName();
        return (BoundFragment)(_fragmentManager.findFragmentByTag(str));
    }

    @Override
    public void goBack() {
        _overlayClass = null;
        _fragmentManager.popBackStack();
    }

    public void goBackTo(Class modelClass) {
        _fragmentManager.popBackStack(modelClass.getName(), 0);
    }

    @Override
    public void clearOverlay() {
        _overlayClass = null;
        Fragment overlayFragment = _fragmentManager.findFragmentById(R.id.overlays_container);
        if (overlayFragment != null) {
            _fragmentManager.popBackStack();
        }
    }

    @Override
    public void endMainPresentation() {
        _activity.finishAffinity();
        Intent showSplashIntent = new Intent(_activity, SplashActivity.class);
        _activity.startActivity(showSplashIntent);
    }

    @Override
    public void startMainPresentation(String userId, boolean emergency, UserSnapshot userSnapshot) {
        try {
            Intent showMainActivityIntent = MainActivity.createOpeningIntent(_activity, userId, emergency, userSnapshot);
            _activity.startActivity(showMainActivityIntent);
        } catch (RuntimeException tex) {
            UserSnapshot smallerUserSnapshot = new UserSnapshot(null, userSnapshot.getEmergencyPassword());
            Intent showMainActivityIntent = MainActivity.createOpeningIntent(_activity, userId, emergency, smallerUserSnapshot);
            _activity.startActivity(showMainActivityIntent);
        }
    }

    @Override
    public void showTermsAndPrivacy() {
        _activity.startActivity(SplashActivity.createOpeningIntent(_activity, true));
    }
}
