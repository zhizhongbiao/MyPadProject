package com.dcamp.pad.ui.basic;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dcamp.pad.BuildConfig;
import com.dcamp.pad.R;
import com.dcamp.pad.ui.base.AppWrapperMvpActivity;
import com.dcamp.pad.ui.basic.adapter.CateAdapter;
import com.dcamp.pad.ui.basic.bean.CateVo;
import com.dcamp.pad.ui.home.DownloadAppFragment;
import com.dcamp.pad.ui.platform.PlatformActivity;
import com.easyder.wrapper.model.BaseVo;
import com.easyder.wrapper.network.ApiConfig;
import com.easyder.wrapper.presenter.MvpBasePresenter;
import com.easyder.wrapper.utils.PackageUtils;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Auther:  winds
 * Data:    2017/7/8
 * Desc:    首页
 */

@RuntimePermissions
public class HomeActivity extends AppWrapperMvpActivity<MvpBasePresenter> {

    @BindView(R.id.gv_cate)
    GridView gv_cate;

    private CateAdapter adapter;
    private long[] times = new long[2];

    @Override
    public int getViewLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState, Intent intent) {
        HomeActivityPermissionsDispatcher.onPermissionAllowedWithCheck(this);
        adapter = new CateAdapter(mActivity);
        gv_cate.setAdapter(adapter);

        gv_cate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(PlatformActivity.getIntent(mActivity, adapter.getItem(position).id));
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        presenter.getData(ApiConfig.API_HOME_SUBJECT_LIST, CateVo.class);
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_HOME_SUBJECT_LIST)) {
            adapter.setList(((CateVo) dataVo).list);
        }
    }

//    @Override
//    protected void beforeOnCreate() {
//        super.beforeOnCreate();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        WindowUtil.setMiuiStatusBarDarkMode(this,true);
//    }

    @Override
    public void onBackPressedSupport() {
        System.arraycopy(times, 1, times, 0, times.length - 1);
        times[times.length - 1] = SystemClock.uptimeMillis();
        if (times[0] > SystemClock.uptimeMillis() - 1500) {
            super.onBackPressedSupport();
        } else {
            showToast("再按次返回键，退出应用");
        }
    }

    @OnClick({R.id.iv_app, R.id.iv_welcome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_app:
                DownloadAppFragment dlgFrg = DownloadAppFragment.getInstance(null);
                dlgFrg.show(getFragmentManager(), DownloadAppFragment.TAG);
                break;
            case R.id.iv_welcome:
                showApkInfoDialog();
                break;
        }
    }

    private long[] hidetimes = new long[6];

    private void showApkInfoDialog() {
        System.arraycopy(hidetimes, 1, hidetimes, 0, hidetimes.length - 1);
        hidetimes[hidetimes.length - 1] = SystemClock.uptimeMillis();
        if (hidetimes[0] > SystemClock.uptimeMillis() - 1000) {
            hidetimes = new long[6];
            new AlertDialog.Builder(mActivity).setMessage(String.format("版本号:\b\b%1$s\n版本名称:\b\b%2$s\nHost地址:\b\b%3$s\n打包时间:\b\b%4$s", PackageUtils.getVersionCode(), PackageUtils.getVersionName(), ApiConfig.HOST, BuildConfig.buildTime))
                    .setCancelable(false)
                    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }



    @NeedsPermission({Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_CALL_LOG
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionAllowed() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @OnShowRationale({Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_CALL_LOG
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onShowRationale(final PermissionRequest request) {

        new AlertDialog.Builder(this)
                .setMessage(String.format("允许%1$s向您的设备上读写文件吗?", getString(R.string.app_name)))
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();

    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_CALL_LOG
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onDenied() {
        showToast("您已拒绝授予相关必要权限");
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_CALL_LOG
            , Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void noNeverAsk() {

    }
}
