package com.dcamp.pad.ui.base.animation.effects.myeffects;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.dcamp.pad.ui.base.animation.effects.BaseEffects;


/**
 * Auther   : ZZB
 * Date     : 2017/7/3
 * Desc     :
 */

public class MyFadeIn extends BaseEffects {

    protected void setupAnimation(View view) {
        getAnimatorSet().setInterpolator(new OvershootInterpolator());
//        getAnimatorSet().setInterpolator(new DecelerateInterpolator());
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 0.5f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 0.5f, 1f).setDuration(mDuration),
//                ObjectAnimator.ofFloat(view, "translationY", 0, -20,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0.3f, 0.65f, 1f).setDuration(mDuration)

        );
    }
}