package com.boyrocket.pete.metronome;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationHelper {
    public static void expandOrCollapse(final View v, String exp_or_colpse) {
        TranslateAnimation animation = null;
        if (exp_or_colpse.equals("expand")) {
            animation = new TranslateAnimation(0.0f, 0.0f, -v.getHeight(), 0.0f);
            v.setVisibility(View.VISIBLE);
        } else {
            animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -v.getHeight());
            Animation.AnimationListener collapseListener = new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            };

            animation.setAnimationListener(collapseListener);
        }

        animation.setDuration(300);
        animation.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(animation);
    }

    public static void expandHorizontally(final View view) {

        Animation animation = new ScaleAnimation(0, 1, 1, 1);
        view.setVisibility(View.VISIBLE);

        animation.setDuration(300);
        animation.setInterpolator(new AccelerateInterpolator(0.5f));
        view.startAnimation(animation);
    }

    public static void collapseHorizontally(final View view) {

        Animation animation = new ScaleAnimation(1, 0, 1, 1);
        Animation.AnimationListener collapseListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        };

        animation.setAnimationListener(collapseListener);

        animation.setDuration(300);
        animation.setInterpolator(new AccelerateInterpolator(0.5f));
        view.startAnimation(animation);
    }
}
