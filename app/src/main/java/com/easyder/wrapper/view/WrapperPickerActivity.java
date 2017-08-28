package com.easyder.wrapper.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;


import com.easyder.wrapper.presenter.MvpBasePresenter;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;



/**
 * Auther:  winds
 * Data:    2017/4/19
 * Desc:    图片选择
 */

public abstract class WrapperPickerActivity<P extends MvpBasePresenter> extends WrapperSwipeActivity<P> implements TakePhoto.TakeResultListener, InvokeListener{

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }


    /**
     * 裁剪配置
     * @return
     */
    protected CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(300).setAspectY(300);
        builder.setOutputX(300).setOutputY(300);
        builder.setWithOwnCrop(false);
        return builder.create();
    }

    /**
     * 压缩配置
     * @return
     */
    protected CompressConfig getCompressConfig() {
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(1024 * 100) //质量压缩
                .setMaxPixel(800)       //像素压缩
                .enableReserveRaw(true) //是否保留原文件
                .create();
        return config;
    }

    /**
     * 提供拍照默认uri
     * @return
     */
    protected Uri getFileUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }
//
//    /**
//     * 图片选择器 默认不压缩
//     */
//    protected void showPickerDialog() {
//        new PhotoChioceDialog(mActivity).setClickCallback(new PhotoChioceDialog.ClickCallback() {
//            @Override
//            public void doAlbum() {
//                getTakePhoto().onPickMultiple(1);
//            }
//
//            @Override
//            public void doCamera() {
//                getTakePhoto().onPickFromCapture(getFileUri());
//            }
//
//            @Override
//            public void doCancel() {
//
//            }
//        }).show();
//    }
//
//    /**
//     * 图片压缩
//     *
//     * @param path
//     */
//    protected void compress(String path, OnCompressListener listener) {
//        Luban.get(this).putGear(Luban.THIRD_GEAR)
//                .load(new File(path))
//                .setCompressListener(listener)
//                .launch();
//    }
}

