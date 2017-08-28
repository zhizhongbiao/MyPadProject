package com.easyder.wrapper.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcamp.pad.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Auther:  winds
 * Data:    2017/4/12
 * Desc:
 */
public class TitleView extends RelativeLayout {
    @BindView(R.id.title_tv_left)
    public TextView title_tv_left;

    @BindView(R.id.title_tv_right)
    public TextView title_tv_right;

    @BindView(R.id.title_tv_center)
    TextView title_tv_center;

    @BindView(R.id.title_iv_left)
    public ImageView title_iv_left;

    @BindView(R.id.title_iv_right)
    public ImageView title_iv_right;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.view_title, this);
        ButterKnife.bind(this, view);
    }

    public TitleView setLeftImage(int resId) {
        if (!checkVisible(title_iv_left)) {
            title_iv_left.setVisibility(View.VISIBLE);
        }
        title_iv_left.setImageResource(resId);
        return this;
    }

    public TitleView setRightImage(int resId) {
        if (!checkVisible(title_iv_right)) {
            title_iv_right.setVisibility(View.VISIBLE);
        }
        title_iv_right.setImageResource(resId);
        return this;
    }

    public TitleView setCenterText(CharSequence text) {
        title_tv_center.setText(text);
        return this;
    }

    public TitleView setRightText(CharSequence text) {
        title_tv_right.setText(text);
        title_tv_right.setVisibility(View.VISIBLE);
        return this;
    }

    public TitleView setRightText(CharSequence text, int color) {
        title_tv_right.setText(text);
        title_tv_right.setTextColor(color);
        return this;
    }


    boolean checkVisible(View view) {
        return view.getVisibility() == VISIBLE ? true : false;
    }

    public TitleView setVisible(View view, boolean visible) {
        view.setVisibility(visible ? VISIBLE : GONE);
        return this;
    }

}
