package com.dcamp.pad.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.widget.ImageView;

import com.dcamp.pad.R;
import com.dcamp.pad.ui.base.WrapperMvpDialogFrragment;
import com.dcamp.pad.ui.base.animation.Effectstype;
import com.dcamp.pad.utils.WindowUtil;
import com.easyder.wrapper.presenter.MvpBasePresenter;

import butterknife.BindView;


/**
 * Auther   : ZZB
 * Date     : 2017/6/30
 * Desc     : 下载App应用Dialog
 */

public class DownloadAppFragment extends WrapperMvpDialogFrragment<MvpBasePresenter> implements View.OnClickListener {

    public static final String TAG = "DownloadAppFragment";
    //Dialog layout参数比例因子
    private static final double DIALOG_PARAMS_FACTOR = 0.56d;
    @BindView(R.id.ivClose)
    ImageView ivClose;


    public static DownloadAppFragment getInstance(String data) {
        DownloadAppFragment downloadAppFragment = new DownloadAppFragment();
        Bundle args = new Bundle();
        downloadAppFragment.setArguments(args);
        return downloadAppFragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.dialog_download_app;
    }

    /**
     *  设置参数动画 ，参数类型和顺序不能错；
     *  参数类型以此为 Effectstype，Effectstype，long
     * @param paramsArray 参数容器
     * @return 装了参数的容器
     */
    @Nullable
    @Override
    public SparseArrayCompat<Object> getAnimaParams(SparseArrayCompat<Object> paramsArray) {
        //设置开始动画类型
        paramsArray.put(SHOW_ANIMA_KEY, Effectstype.MyFadeIn);
        //设置关闭动画类型
        paramsArray.put(DISMISS_ANIMA_KEY, Effectstype.MyFadeOut);
        //设置开始动画时长
        paramsArray.put(ANIMA_DURATION_KEY, 300L);
        return paramsArray;
    }

    /**
     * 设置 dialogFragment的大小
     * @param dialog
     */
    @Override
    public void initialDialogLayoutParams(Dialog dialog) {
        WindowUtil.setDialogFragmentWindowLayoutParams(getActivity(), dialog, DIALOG_PARAMS_FACTOR);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setOnViewClick();
    }

    /**
     * 设置控件的点击监听
     */
    private void setOnViewClick() {
        ivClose.setOnClickListener(this);
    }

    /**
     * 控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
                dismiss();
                break;
        }
    }
}
