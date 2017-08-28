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

public class ZoomIn extends BaseEffects {
    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().setInterpolator(new OvershootInterpolator());
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.97f, 0.94f, 0.91f, 0.94f, 0.97f, 1f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.97f, 0.94f, 0.91f, 0.94f, 0.97f, 1f).setDuration(mDuration),
//                ObjectAnimator.ofFloat(view, "translationY", 0, -20,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0.5f, 1).setDuration(mDuration)

        );
    }
}
