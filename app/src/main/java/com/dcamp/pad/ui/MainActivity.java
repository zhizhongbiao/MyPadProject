package com.dcamp.pad.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.dcamp.pad.R;
import com.dcamp.pad.ui.base.AppWrapperMvpActivity;
import com.dcamp.pad.ui.home.DownloadAppFragment;
import com.dcamp.pad.ui.home.adapter.SubjectTitleAdapter;
import com.dcamp.pad.ui.home.bean.SubjectListBean;
import com.dcamp.pad.ui.platform.PlatformActivity;
import com.easyder.wrapper.model.BaseVo;
import com.easyder.wrapper.network.ApiConfig;
import com.easyder.wrapper.presenter.MvpBasePresenter;

import java.util.List;

import butterknife.BindView;


public class MainActivity extends AppWrapperMvpActivity<MvpBasePresenter> implements AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.ivDownloadApp)
    ImageView ivDownloadApp;
    @BindView(R.id.gvSubjectTitle)
    GridView gvSubjectTitle;
    private SubjectTitleAdapter adapter;
    private long lastClickTimeStamp;

    private List<SubjectListBean.ListBean> subjectList;


    @Override
    public int getViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState, Intent intent) {
        setOnViewClick();
        initialGridView();
    }


    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        presenter.getData(ApiConfig.API_HOME_SUBJECT_LIST, SubjectListBean.class);
    }


    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_HOME_SUBJECT_LIST)) {
            SubjectListBean subjectListBean = (SubjectListBean) dataVo;
            subjectList = subjectListBean.list;
            adapter.setData(subjectList);
        }
    }

    /**
     * 初始化GridView
     */
    private void initialGridView() {
        adapter = new SubjectTitleAdapter(this, subjectList);
        gvSubjectTitle.setAdapter(adapter);
        gvSubjectTitle.setOnItemClickListener(this);
        gvSubjectTitle.setVerticalScrollBarEnabled(false);
    }

    /**
     * 设置控件的点击监听
     */
    private void setOnViewClick() {
        ivDownloadApp.setOnClickListener(this);
    }


    /**
     * 控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDownloadApp://下载App
                DownloadAppFragment dlgFrg = DownloadAppFragment.getInstance(null);
                dlgFrg.show(getFragmentManager(), DownloadAppFragment.TAG);
                break;


        }
    }


    /**
     * GridView Item 点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(PlatformActivity.getIntent(this, subjectList.get(position).id));
    }


    /**
     * 监听回退键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - lastClickTimeStamp) > 2000) {
                showToast("再按一次退出应用");
                lastClickTimeStamp = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


//    @OnPermissionDenied(Manifest.permission.INTERNET)
//    void onUserDenyInternetAccess() {
//        showToast("您拒绝访问网络！");
//        finish();
//    }
//
//
//    @OnShowRationale(Manifest.permission.INTERNET)
//    void showRationaleIntenet(final PermissionRequest request) {
//        new AlertDialog.Builder(this)
//                .setMessage(String.format("允许%1$s向您的设备上访问网络吗?", getString(R.string.app_name)))
//                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        request.proceed();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        request.cancel();
//                    }
//                })
//                .show();
//    }
//
//
//    @NeedsPermission(Manifest.permission.INTERNET)
//    void applyInternet() {
//
//    }
//
//
//    @OnNeverAskAgain(Manifest.permission.INTERNET)
//    void onNeverAskAgain() {
//
//    }



}
