package com.easyder.wrapper.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dcamp.pad.R;
import com.easyder.wrapper.listener.OnViewInflate;
import com.easyder.wrapper.network.ResponseInfo;
import com.easyder.wrapper.presenter.MvpBasePresenter;
import com.easyder.wrapper.presenter.MvpPresenter;
import com.easyder.wrapper.reveiver.NetworkStateEvent;
import com.easyder.wrapper.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.ParameterizedType;

import me.winds.widget.autolayout.utils.AutoUtils;
import me.winds.widget.usage.ToastView;


/**
 * Auther:  winds
 * Data:    2017/4/11
 * Desc:
 */

public abstract class WrapperMvpActivity<P extends MvpBasePresenter> extends WrapperActivity implements MvpView {
    protected P presenter;
    protected MaterialDialog progressDialog;
    private boolean loadSuccess; //数据是否加载成功
    private boolean loadFinish; //请求是否成功
    private boolean focus = true;  //自动显示和隐藏输入法


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<? extends MvpPresenter> presenterClass = (Class<? extends MvpPresenter>) type.getActualTypeArguments()[0];
        try {
            this.presenter = (P) presenterClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        presenter.attachView(this);
        progressDialog = new MaterialDialog.Builder(this)
                .customView(R.layout.layout_progress_bar, false)
                .canceledOnTouchOutside(false)
                .cancelable(true).build();
        progressDialog.getWindow().setLayout(AutoUtils.getPercentWidthSize(200), AutoUtils.getPercentWidthSize(136));
        progressDialog.getWindow().getDecorView().setBackgroundColor(UIUtils.getColor(android.R.color.transparent));
        EventBus.getDefault().register(this);
        loadData(savedInstanceState, getIntent());
    }

    @Override
    public void beforeSuccess() {
        loadSuccess = true;
        loadFinish = true;
    }

    @Override
    public void onLoading() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
            loadSuccess = false;
        }
    }


    public void showLoadingView() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void onError(ResponseInfo responseInfo) {
        loadFinish = true;
    }

    /**
     * 网络状态改变事件，由子类重写实现相关的逻辑
     *
     * @param networkStateEvent
     */
    @Subscribe
    public void onEvent(NetworkStateEvent networkStateEvent) {
        if (!networkStateEvent.hasNetworkConnected) {
            showToast("网络连接已断开!");
        } else {
            if (loadFinish && !loadSuccess) {
                //网络已连接数据没有加载成功，重新加载
                loadData(null, getIntent());
            }
        }
    }

    /**
     * 从intent中获取请求参数，初始化vo对象，并发送请求
     *
     * @param savedInstanceState
     * @param intent
     */
    protected abstract void loadData(Bundle savedInstanceState, Intent intent);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStopLoading();
        progressDialog = null;
        presenter.detachView();
        presenter = null;
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onStopLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (focus) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {

                    hideKeyboard(v);
                }
                return super.dispatchTouchEvent(ev);
            }
            // 其他组件响应点击事件
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 判断键盘是否应该隐藏
     * 点击除EditText的区域隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                ((EditText) v).setCursorVisible(true);
                return false;
            } else {
                ((EditText) v).setCursorVisible(false);  //隐藏光标
                return true;
            }
        }
        return false;
    }


    protected View getInflateView(RecyclerView mRecyclerView, int layoutId, OnViewInflate linstener) {
        View view = getLayoutInflater().inflate(layoutId, (ViewGroup) mRecyclerView.getParent(), false);
        if (linstener != null) {
            linstener.afterInflate(view);
        }
        AutoUtils.auto(view);
        return view;
    }

    /**
     * 设置自动隐藏输入法
     *
     * @param focus 默认 true 自动隐藏
     */
    protected void setAutoFocus(boolean focus) {
        this.focus = focus;
    }

    /**
     * 普通Toast提示
     *
     * @param msg
     */
    protected void showToast(String msg) {
        ToastView.showToastInCenter(mActivity, msg, Toast.LENGTH_LONG);
    }

    /**
     * 图文样式的Toast提示
     *
     * @param msg
     * @param resId
     */
    protected void showToast(String msg, int resId) {
        ToastView.showVerticalToast(mActivity, msg, resId);
    }
}

