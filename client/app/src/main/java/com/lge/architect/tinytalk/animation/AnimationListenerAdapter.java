package com.lge.architect.tinytalk.animation;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * Provides empty implementations of the methods in {@link AnimationListener}
 * for convenience reasons.
 */
public class AnimationListenerAdapter implements AnimationListener {

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
