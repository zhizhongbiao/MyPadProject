package com.easyder.wrapper.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;

import butterknife.ButterKnife;
import me.winds.widget.autolayout.AutoFrameLayout;
import me.winds.widget.autolayout.AutoLinearLayout;
import me.winds.widget.autolayout.AutoRelativeLayout;
import me.winds.widget.autolayout.widget.AutoCardView;
import me.winds.widget.autolayout.widget.AutoRadioGroup;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Auther:  winds
 * Data:    2017/4/11
 * Desc:
 */

public abstract class WrapperActivity extends SupportActivity {
    protected Activity mActivity;

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String LAYOUT_RADIOGROUP = "RadioGroup";
    private static final String LAYOUT_CARDVIEW = "CardView";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        } else if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        } else if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        } else if (name.equals(LAYOUT_RADIOGROUP)) {
            view = new AutoRadioGroup(context, attrs);
        } else if (name.equals(LAYOUT_CARDVIEW)) {
            view = new AutoCardView(context, attrs);
        }
        if (view != null) {
            return view;
        }
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeOnCreate();
        setContentView(getViewLayout());
        init();
        initView(savedInstanceState, getIntent());
    }

    private void init() {
        ButterKnife.bind(this);
        mActivity = this;
    }


    protected void beforeOnCreate() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //去除默认actionbar
    }


    protected abstract int getViewLayout();


    protected abstract void initView(Bundle savedInstanceState, Intent intent);

}
