package com.easyder.wrapper.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dcamp.pad.R;
import com.easyder.wrapper.network.ResponseInfo;
import com.easyder.wrapper.presenter.MvpBasePresenter;
import com.easyder.wrapper.presenter.MvpPresenter;
import com.easyder.wrapper.reveiver.NetworkStateEvent;
import com.easyder.wrapper.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.winds.widget.autolayout.utils.AutoUtils;
import me.winds.widget.usage.ToastView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Auther:  winds
 * Data:    2017/5/26
 * Desc:
 */

public abstract class WrapperFragment<P extends MvpBasePresenter> extends SupportFragment implements MvpView {
    protected P presenter;
    protected MaterialDialog progressDialog;
    private boolean loadSuccess;
    private boolean loadFinish; //请求是否成功
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<? extends MvpPresenter> presenterClass = (Class<? extends MvpPresenter>) type.getActualTypeArguments()[0];
        try {
            this.presenter = (P) presenterClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }

        presenter.attachView(this);
        progressDialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.layout_progress_bar, false)
                .canceledOnTouchOutside(false).build();
        progressDialog.getWindow().setLayout(AutoUtils.getPercentWidthSize(200), AutoUtils.getPercentWidthSize(136));
        progressDialog.getWindow().getDecorView().setBackgroundColor(UIUtils.getColor(android.R.color.transparent));

        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(getViewLayout(), container, false);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
        loadData(savedInstanceState);
    }


    /**
     * @return 布局resourceId
     */

    public abstract int getViewLayout();


    /**
     * 初始化View。或者其他view级第三方控件的初始化,及相关点击事件的绑定
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取请求参数，初始化vo对象，并发送请求
     *
     * @param savedInstanceState
     */
    protected abstract void loadData(Bundle savedInstanceState);


    @Override
    public void beforeSuccess() {
        loadSuccess = true;
        loadFinish = true;
    }


    @Override
    public void onLoading() {
        if (!progressDialog.isShowing()) {
            loadSuccess = false;
            progressDialog.show();
        }
    }


    @Override
    public void onError(ResponseInfo responseInfo) {
        loadFinish = true;
        switch (responseInfo.getState()) {
            case ResponseInfo.TIME_OUT:
                showTimeOutDialog(responseInfo);
                break;
            case ResponseInfo.NO_INTERNET_ERROR:
                showOpenNetworkDialog(responseInfo);
                break;
        }

    }

    private void showOpenNetworkDialog(ResponseInfo responseInfo) {
        showToast("网络连接不可用，请打开网络！");
        /*View view = UIUtils.inflate(R.layout.network_setting_dialog);
        MaterialDialog networkSettingDialog = new MaterialDialog.Builder(getActivity()).customView(view, false)
                .cancelable(true).build();
        networkSettingDialog.show();*/
    }

    protected void showTimeOutDialog(ResponseInfo responseInfo) {
        showToast("连接网络超时");
    }


    @Subscribe
    public void onEvent(NetworkStateEvent networkStateEvent) {
        if (!networkStateEvent.hasNetworkConnected) {

            showToast("网络连接已断开");
        } else {
            if (loadFinish && !loadSuccess) {
                //网络已连接数据没有加载成功，重新加载
                loadData(null);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        presenter.detachView();
        presenter = null;
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onStopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void showLoadingDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    protected void showToast(String msg) {
        ToastView.showToastInCenter(_mActivity, msg);
    }

    protected void showToast(String msg, int resId) {
        ToastView.showToastInCenter(_mActivity, msg, resId);
    }

    protected void showToast(String msg, int resId, boolean longtime) {
        ToastView.showVerticalToast(_mActivity, msg, resId, longtime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
    }

}

