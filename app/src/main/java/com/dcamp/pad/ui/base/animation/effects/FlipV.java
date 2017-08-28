package com.dcamp.pad.ui.base.animation.effects;

import android.animation.ObjectAnimator;
import android.view.View;

public class FlipV extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "rotationX", -90, 0).setDuration(mDuration)

        );
    }
}
