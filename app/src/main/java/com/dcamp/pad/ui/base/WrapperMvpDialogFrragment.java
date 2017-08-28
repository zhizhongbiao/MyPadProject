package com.dcamp.pad.ui.base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcamp.pad.R;
import com.dcamp.pad.ui.base.animation.Effectstype;
import com.dcamp.pad.ui.base.animation.effects.BaseEffects;
import com.easyder.wrapper.model.BaseVo;
import com.easyder.wrapper.presenter.MvpBasePresenter;
import com.easyder.wrapper.view.WrapperDialogFragment;


/**
 * Auther   : ZZB
 * Date     : 2017/6/30
 * Desc     : MvpDialogFragment 封装类
 */

public abstract class WrapperMvpDialogFrragment<P extends MvpBasePresenter> extends WrapperDialogFragment<P> implements DialogInterface.OnShowListener, Animator.AnimatorListener, DialogInterface.OnKeyListener {

    private View contentView;
    private long animaDuration = 650l;
    protected static final int SHOW_ANIMA_KEY = 0;
    protected static final int DISMISS_ANIMA_KEY = 1;
    protected static final int ANIMA_DURATION_KEY = 2;
    private Effectstype showAnimaEffect = Effectstype.MyFadeIn;
    private Effectstype dismissAnimaEffect = Effectstype.MyFadeOut;
    private Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = super.onCreateView(inflater, container, savedInstanceState);
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
        //关闭点击外面消失的效果
        getDialog().setCanceledOnTouchOutside(false);
        //设置动画参数
        initialDialogAnimaParams();
        //设置监听对话框显示
        getDialog().setOnShowListener(this);
        //监听显示Dialog时的键盘事件
        getDialog().setOnKeyListener(this);

        return contentView;
    }


    @Override
    public void onResume() {
        initialDialogLayoutParams(getDialog());
        super.onResume();
//        mActivity.setCurrentFragment(this);
    }


    @Override
    public void onStop() {
        super.onStop();
//        mActivity.setCurrentFragment(null);
    }

    /**
     * 提供给子类初始化DialogFragment的进场和出场动画，和动画时间
     */
    public void initialDialogAnimaParams() {
        SparseArrayCompat<Object> emptyArray = new SparseArrayCompat<>();
        SparseArrayCompat<Object> animaParams = getAnimaParams(emptyArray);
        if (animaParams == null || animaParams.size() != 3) return;
        //进场动画
        showAnimaEffect = ((Effectstype) animaParams.get(SHOW_ANIMA_KEY));
        //出场动画
        dismissAnimaEffect = ((Effectstype) animaParams.get(DISMISS_ANIMA_KEY));
        //动画时间
        animaDuration = (Long) animaParams.get(ANIMA_DURATION_KEY);

    }

    @Nullable
    public abstract SparseArrayCompat<Object> getAnimaParams(SparseArrayCompat<Object> paramsArray);


    /**
     * 初始化DialogFragment的layout参数
     *
     * @param dialog
     */
    public abstract void initialDialogLayoutParams(Dialog dialog);


    /**
     * 显示时播放动画
     *
     * @param dialog
     */
    @Override
    public void onShow(DialogInterface dialog) {
        startAnimation(showAnimaEffect);
    }

    /**
     * 关闭时播放动画
     */
    @Override
    public void dismiss() {
        AnimatorSet animatorSet = startAnimation(dismissAnimaEffect);
        animatorSet.addListener(this);
    }


    /**
     * 开始动画
     *
     * @param type 动画类型
     * @return
     */
    protected AnimatorSet startAnimation(Effectstype type) {
        if (type == null) {
            type = Effectstype.Shake;
        }
        BaseEffects animator = type.getAnimator();
        if (animaDuration != -1) {
            animator.setDuration(Math.abs(animaDuration));
        }
        animator.start(contentView);
        return animator.getAnimatorSet();
    }


    @Override
    public void onAnimationEnd(Animator animation) {
        //播完动画就关闭
        super.dismiss();
    }


    @Override
    protected void loadData(Bundle savedInstanceState) {
// TODO Temporarily nothing needs to do here
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
// TODO Temporarily nothing needs to do here
    }


    @Override
    public void onAnimationStart(Animator animation) {
// TODO Temporarily nothing needs to do here
    }

    @Override
    public void onAnimationCancel(Animator animation) {
// TODO Temporarily nothing needs to do here
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
// TODO Temporarily nothing needs to do here
    }


    /**
     * 使用返回键依然有动画
     */
    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dismiss();
            return true;
        }
        return false;
    }


}
