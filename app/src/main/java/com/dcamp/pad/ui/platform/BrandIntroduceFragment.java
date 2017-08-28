package com.dcamp.pad.ui.platform;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.dcamp.pad.R;
import com.dcamp.pad.ui.base.WrapperMvpDialogFrragment;
import com.dcamp.pad.ui.base.animation.Effectstype;
import com.dcamp.pad.utils.WindowUtil;
import com.easyder.wrapper.presenter.MvpBasePresenter;
import com.easyder.wrapper.utils.LogUtils;

import butterknife.BindView;

/**
 * Auther   : ZZB
 * Date     : 2017/6/30
 * Desc     : 品牌介绍DialogFragment
 */

public class BrandIntroduceFragment extends WrapperMvpDialogFrragment<MvpBasePresenter> implements View.OnClickListener {

    public static final String TAG = "BrandIntroduceFragment";
    public static final String INTRODUCE_KEY = "brandIntroduce";
    private static final double DIALOG_PARAMS_FACTOR = 0.73;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.wvContent)
    WebView wvContent;
    private String brandIntro;


    public static BrandIntroduceFragment getInstance(String brandIntroduce) {
        BrandIntroduceFragment brandIntroduceFragment = new BrandIntroduceFragment();
        Bundle args = new Bundle();
        args.putString(INTRODUCE_KEY, brandIntroduce);
        brandIntroduceFragment.setArguments(args);
        return brandIntroduceFragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.dialog_brand_introduce;
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
        paramsArray.put(ANIMA_DURATION_KEY, 500l);
        return paramsArray;
    }

    /**
     * 设置DialogFragment的大小
     *
     * @param dialog
     */
    @Override
    public void initialDialogLayoutParams(Dialog dialog) {
        WindowUtil.setDialogFragmentWindowLayoutParams(getActivity(), dialog, DIALOG_PARAMS_FACTOR);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        brandIntro = getArguments().getString(INTRODUCE_KEY, null);
        intialWebView(brandIntro);
        setOnViewClick();
    }

    /**
     * 初始化WebView
     */
    private void intialWebView(String html) {
        if (html == null) {
            LogUtils.e("html==null");
            return;
        }
        wvContent.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //释放webView资源
        wvContent.clearCache(true);
        wvContent.destroy();
    }


}
