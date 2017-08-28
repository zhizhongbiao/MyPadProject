package com.easyder.wrapper.presenter;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.easyder.wrapper.MainApplication;
import com.easyder.wrapper.manager.DataManager;
import com.easyder.wrapper.manager.FileManager;
import com.easyder.wrapper.model.BaseVo;
import com.easyder.wrapper.network.RequestInfo;
import com.easyder.wrapper.network.ResponseInfo;
import com.easyder.wrapper.utils.LogUtils;
import com.easyder.wrapper.view.MvpView;

import java.io.Serializable;

import me.winds.widget.usage.ToastView;


/**
 * @author 刘琛慧
 *         date 2015/10/27.
 */
public class MvpBasePresenter extends MvpPresenter<MvpView> implements Callback {
    protected boolean needDialog = true;
    private int requestCount;
    private ArrayMap<String, Serializable> mParams = new ArrayMap<>();

    public void getData(String url, ArrayMap<String, Serializable> params) {
        getData(url, params, null);
    }


    public void getData(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        if (isViewAttached() && needDialog && requestCount == 0) {
            getView().onLoading();
        }

        RequestInfo requestInfo = new RequestInfo(url, dataClass);
        requestInfo.setRequestType(RequestInfo.REQUEST_GET);
        requestInfo.setRequestParams(params);
        //requestInfo.setNeedMockData(true);
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }

    public void getData(String url, Class<? extends BaseVo> dataClass) {
        getData(url, null, dataClass);
    }

    public void postData(String url, ArrayMap<String, Serializable> params) {
        postData(url, params, null);
    }

    /**
     *
     */
    public void postData(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        if (isViewAttached() && needDialog && requestCount == 0) {
            getView().onLoading();
        }

        RequestInfo requestInfo = new RequestInfo(url, dataClass);
        requestInfo.setRequestType(RequestInfo.REQUEST_POST);
        requestInfo.setRequestParams(params);
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }

    public FileManager getFileManager() {
        return FileManager.getInstance();
    }


    public SharedPreferences getDefaultPrefs() {
        //返回整个默认的SharedPreference文件;
        return MainApplication.getMainPreferences();
    }

    /**
     *
     */
    public void postData(String url, Class<? extends BaseVo> dataClass) {
        postData(url, null, dataClass);
    }


    public void syncDataInDB(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataVoClass) {
//        DataManager.getDefault().loadDataFromLocal(url, params, dataVoClass, this);
    }

    /**
     * 请求成功，回调View层方法处理成功的结果
     *
     * @param responseInfo 包含的返回数据的BaseVo子类对象
     */
    @Override
    public void onSuccess(ResponseInfo responseInfo) {
        requestCount--;
        if (isViewAttached()) {
            getView().beforeSuccess();
            getView().showContentView(responseInfo.getUrl(), responseInfo.getDataVo());
            if (requestCount == 0) {
                getView().onStopLoading();
            }
        } else {
            LogUtils.e("MvpView已被销毁，onSuccess方法无法回调showContentView方法");
        }
    }


    public boolean isNeedDialog() {
        return needDialog;
    }

    public void setNeedDialog(boolean needDialog) {
        this.needDialog = needDialog;
    }

    /**
     * 请求失败，回调View层的方法处理错误信息
     *
     * @param responseInfo 包含错误码和错误信息的BaseVo子类对象
     */
    @Override
    public void onError(ResponseInfo responseInfo) {
        requestCount--;
        if (isViewAttached()) {
            getView().onStopLoading();

            switch (responseInfo.getState()) {
                case ResponseInfo.FAILURE:
                case ResponseInfo.CACHE_PARSE_ERROR:
                case ResponseInfo.JSON_PARSE_ERROR:
                    showToast(responseInfo.getMsg());
                    break;
                case ResponseInfo.TIME_OUT:
                    showToast("网络连接不稳定，请检查网络设置");
                    break;
                case ResponseInfo.NO_INTERNET_ERROR:
                    showToast("没有可用的网络，请检查您的网络设置");
                    break;
                case ResponseInfo.SERVER_UNAVAILABLE:
                    showToast("接口不可用!");
                    break;
                case ResponseInfo.UN_LOGIN:
                    Context context;
                    if (getView() instanceof Context) {
                        context = (Context) getView();
                    } else {
                        context = ((Fragment) getView()).getContext();
                    }
                    // TODO: 2017/6/3  unlogin

//                    Intent intent = new Intent(context, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(intent);
                    break;
                case ResponseInfo.UN_SET_APP:
                    setClientType();
                    break;
                case 8000:
                    break;
            }

            getView().onError(responseInfo);
        } else {
            LogUtils.e("MvpView已销毁，onError方法无法回调MvpView层的方法: " + viewClassName);
        }
    }

    private void setClientType() {
//        getData(ApiConfig.API_SET_CLIENT_TYPE, new ParamsMap().put("type", "STORE_CASH_REGISTER").get(), BaseVo.class);
    }

    @Override
    public void detachView() {
        super.detachView();
        //取消默认的还未完成的请求
        DataManager.getDefault().onViewDetach(getView());
    }

    protected void showToast(String msg) {
        ToastView.showToastInCenter(MainApplication.getInstance(), msg, Toast.LENGTH_LONG);
    }
}
