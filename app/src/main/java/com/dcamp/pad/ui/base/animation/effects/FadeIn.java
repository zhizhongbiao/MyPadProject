package com.dcamp.pad.ui.base.animation.effects;

import android.animation.ObjectAnimator;
import android.view.View;




public class FadeIn extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view,"alpha",0,1).setDuration(mDuration)

        );
    }
}
