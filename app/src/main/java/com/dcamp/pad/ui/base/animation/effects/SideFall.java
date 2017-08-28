package com.dcamp.pad.ui.base.animation.effects;

import android.view.View;


import android.animation.ObjectAnimator;


public class SideFall extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 2, 1.5f, 1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view,"scaleY",2,1.5f,1).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "rotation", 25,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "translationX",80,0).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration*3/2)

        );
    }
}
