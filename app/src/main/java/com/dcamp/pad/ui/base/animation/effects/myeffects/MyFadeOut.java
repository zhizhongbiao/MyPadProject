package com.dcamp.pad.ui.base.animation.effects.myeffects;


import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dcamp.pad.ui.base.animation.effects.BaseEffects;


/**
 * Auther   : ZZB
 * Date     : 2017/7/3
 * Desc     :
 */

public class MyFadeOut extends BaseEffects {
    @Override
    protected void setupAnimation(View view) {

        getAnimatorSet().setInterpolator(new AccelerateDecelerateInterpolator());
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "scaleX",1.0f,0.5f, 0f).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "scaleY",1.0f,0.5f, 0f).setDuration(mDuration),
//                ObjectAnimator.ofFloat(view, "translationY", 0, -20,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha",1f,0.5f, 0f).setDuration(mDuration)

        );
    }
}