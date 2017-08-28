package eu.geekhome.asymptote.viewmodel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentTouchPressBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.model.WiFiParameters;
import eu.geekhome.asymptote.services.NavigationService;

public class TouchPressViewModel extends HelpViewModelBase<FragmentTouchPressBinding> {
    private WiFiParameters _params;
    private boolean _paused;

    @Inject
    NavigationService _navigationService;
    @Inject
    Context _context;


    public TouchPressViewModel(ActivityComponent activityComponent, WiFiParameters params) {
        super(activityComponent);
        _params = params;
    }

    @Override
    protected String getNoWiFiRationale() {
        return _context.getString(R.string.rationale_nowifi_adding_devices);
    }

    @Override
    public void onPause() {
        _paused = true;
    }

    @Override
    public void onResume() {
        if (!_paused) {
            final View targetHand = getBinding().imageHand;
            final View targetLed = getBinding().imageLed;
            final View targetArrow = getBinding().imageArrow;

            ObjectAnimator darkLed = ObjectAnimator.ofFloat(targetLed, "alpha", 0f);
            darkLed.setDuration(500);
            darkLed.setTarget(targetLed);

            ObjectAnimator lightLed = ObjectAnimator.ofFloat(targetLed, "alpha", 1f);
            darkLed.setDuration(500);
            darkLed.setTarget(targetLed);

            final int[] repeat = {0};
            final AnimatorSet set = new AnimatorSet();
            set.play(lightLed).before(darkLed);
            set.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    set.setStartDelay(0);
                    set.start();
                    repeat[0]++;
                    if (repeat[0] > 5) {
                        //show arrow
                        targetArrow.animate()
                                .alpha(1)
                                .setDuration(1000)
                                .start();

                        //hand down
                        targetHand.animate()
                                .translationY(+18)
                                .setDuration(900)
                                .setInterpolator(new AccelerateDecelerateInterpolator());
                    }
                }

            });


            //hand up
            targetHand.animate()
                    .translationY(-18)
                    .setDuration(900)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //start blinking
                            set.setStartDelay(4000);
                            set.start();
                        }
                    });
        }
    }

    @SuppressWarnings("unused")
    public void onNext(@NonNull final View view) {
        TouchProgressViewModel touchProgressViewModel = new TouchProgressViewModel(getActivityComponent(), _params);
        _navigationService.showViewModel(touchProgressViewModel, new ShowBackButtonInToolbarViewParam());
    }

    @Override
    public FragmentTouchPressBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentTouchPressBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_touch_press, container, false);
        binding.setVm(this);
        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }
}