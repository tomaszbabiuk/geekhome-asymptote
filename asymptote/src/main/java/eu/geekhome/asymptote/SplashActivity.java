package eu.geekhome.asymptote;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import javax.inject.Inject;

import eu.geekhome.asymptote.databinding.ActivityPrivacyBinding;
import eu.geekhome.asymptote.databinding.ActivitySplashBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.PrivacyService;
import eu.geekhome.asymptote.services.impl.SplashViewModelsFactory;
import eu.geekhome.asymptote.services.impl.SplashViewModelsFactory;
import eu.geekhome.asymptote.viewmodel.PrivacyViewModel;
import eu.geekhome.asymptote.viewmodel.SplashViewModel;
import eu.geekhome.controls.ScrollViewExt;

public class SplashActivity extends InjectedActivity {

    public static final String INTENT_SHOW_PRIVACY_AT_START = "INTENT_SHOW_PRIVACY_AT_START";

    public static Intent createOpeningIntent(Context context, boolean showPrivacyAtStart) {
        Intent result = new Intent(context, SplashActivity.class);
        result.putExtra(INTENT_SHOW_PRIVACY_AT_START, showPrivacyAtStart);
        result.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_NEW_TASK);
        return result;
    }

    @Inject PrivacyService _privacyService;
    @Inject NavigationService _navigationService;
    @Inject
    SplashViewModelsFactory _factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean showPrivacyAtStart = getIntent().getBooleanExtra(INTENT_SHOW_PRIVACY_AT_START, false);
        if (_privacyService.isAccepted() && !showPrivacyAtStart) {
            SplashViewModel splashViewModel = _factory.createSplashViewModel();
            _navigationService.showViewModel(_factory.createSignInViewModel(splashViewModel));
            ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
            Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point_linear);
            binding.gearImage.startAnimation(rotateAnimation);
            binding.setVm(splashViewModel);
        } else {
            final PrivacyViewModel privacyViewModel = _factory.createPrivacyViewModel(!showPrivacyAtStart);
            ActivityPrivacyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy);
            binding.setVm(privacyViewModel);
            binding.privacyScroll.setOnBottomReachedListener(new ScrollViewExt.OnBottomReachedListener() {
                @Override
                public void onBottomReached() {
                    privacyViewModel.setBottomReached(true);
                }
            });
        }
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finishAffinity();
            return;
        }

        super.onBackPressed();
    }

}
