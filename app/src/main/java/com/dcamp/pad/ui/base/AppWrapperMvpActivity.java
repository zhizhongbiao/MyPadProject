package com.dcamp.pad.ui.base;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

import com.dcamp.pad.R;
import com.dcamp.pad.utils.WindowUtil;
import com.easyder.wrapper.presenter.MvpBasePresenter;
import com.easyder.wrapper.view.WrapperMvpActivity;

/**
 * Auther   : ZZB
 * Date     : 2017/6/30
 * Desc     : 原有的Activity的封装类
 */

public abstract class AppWrapperMvpActivity<P extends MvpBasePresenter> extends WrapperMvpActivity<P> {

    private Fragment currentFragment = null;

    /**
     * 设置当前Fragment
     * @param currentFragment
     */
    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    /**
     * 获取当前Fragment
     * @return
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    protected void beforeOnCreate() {
        super.beforeOnCreate();
        //设置透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //设置状态字体颜色
        WindowUtil.setMiuiStatusBarDarkMode(this,true);
//        统一管理竖屏，不用在Manifest中每个Activity都设置
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }




    /**
     * 统一管理启动动画
     *
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        overridePendingTransition(R.anim.window_fade_in, R.anim.window_fade_out);
//        overridePendingTransition(R.anim.window_top_in, R.anim.window_bottom_out);
        overridePendingTransition(R.anim.window_slide_right_in, R.anim.window_slide_left_out);
    }


    /**
     * 统一管理结束动画
     */
    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.window_top_in, R.anim.window_bottom_out);
        overridePendingTransition(R.anim.window_slide_left_in, R.anim.window_slide_right_out);
    }

}
