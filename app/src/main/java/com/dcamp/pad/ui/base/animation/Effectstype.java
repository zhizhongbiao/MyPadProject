package com.dcamp.pad.ui.base.animation;


import com.dcamp.pad.ui.base.animation.effects.BaseEffects;

import com.dcamp.pad.ui.base.animation.effects.FlipH;
import com.dcamp.pad.ui.base.animation.effects.Fall;
import com.dcamp.pad.ui.base.animation.effects.FlipV;
import com.dcamp.pad.ui.base.animation.effects.NewsPaper;
import com.dcamp.pad.ui.base.animation.effects.SideFall;
import com.dcamp.pad.ui.base.animation.effects.SlideLeft;
import com.dcamp.pad.ui.base.animation.effects.SlideRight;
import com.dcamp.pad.ui.base.animation.effects.SlideTop;
import com.dcamp.pad.ui.base.animation.effects.SlideBottom;
import com.dcamp.pad.ui.base.animation.effects.RotateBottom;
import com.dcamp.pad.ui.base.animation.effects.RotateLeft;
import com.dcamp.pad.ui.base.animation.effects.Slit;
import com.dcamp.pad.ui.base.animation.effects.Shake;
import com.dcamp.pad.ui.base.animation.effects.myeffects.*;

public enum  Effectstype {

    MyFadeIn(MyFadeIn.class),
    MyFadeOut(MyFadeOut.class),
    ZoomIn(ZoomIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class<? extends BaseEffects> effectsClazz;

    private Effectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects=null;
	try {
		bEffects = effectsClazz.newInstance();
	} catch (ClassCastException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	}
	return bEffects;
    }
}
