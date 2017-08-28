package eu.geekhome.asymptote.animations;

import android.animation.Animator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class PopupsAnimation {
    private View _target;

    public PopupsAnimation(View target) {
        _target = target;
    }

    public void animate() {
        final Animator.AnimatorListener retry = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animate();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        };

        Animator.AnimatorListener movingDownAnimator = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                _target.animate().translationY(20).setDuration(900)
                        .setInterpolator(new AccelerateDecelerateInterpolator()).setListener(retry);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        };
        _target.animate().translationY(-20).setDuration(900)
                .setInterpolator(new AccelerateDecelerateInterpolator()).setListener(movingDownAnimator);
    }
}
