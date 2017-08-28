package com.easyder.wrapper.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.easyder.wrapper.listener.OnViewInflate;
import com.easyder.wrapper.presenter.MvpBasePresenter;

import java.util.List;

import me.winds.widget.autolayout.utils.AutoUtils;


/**
 * Auther:  winds
 * Data:    2017/5/5
 * Desc:    提供上层包装专用
 */

public abstract class WrapperMvpFragment<P extends MvpBasePresenter> extends WrapperFragment<P> {
    protected View getInflateView(RecyclerView mRecyclerView, int layoutId, OnViewInflate linstener) {
        View view = _mActivity.getLayoutInflater().inflate(layoutId, mRecyclerView == null ? null : (ViewGroup) mRecyclerView.getParent(), false);
        if (linstener != null) {
            linstener.afterInflate(view);
        }
        AutoUtils.auto(view);
        return view;
    }

    public void hideInputKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) _mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void showInputKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) _mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

}
