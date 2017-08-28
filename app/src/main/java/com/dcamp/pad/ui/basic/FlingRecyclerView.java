package com.dcamp.pad.ui.basic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Auther:  winds
 * Data:    2017/7/8
 * Desc:    控制fling速度的RecyclerView
 */
public class FlingRecyclerView extends RecyclerView {
    private static final float FLING_SCALE = 0.5f; // 减速因子
    private static final int MAX_VELOCIT = 8000; // 最大顺滑速度

    public FlingRecyclerView(Context context) {
        super(context);
    }

    public FlingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = solveVelocity(velocityX);
        velocityY = solveVelocity(velocityY);
        return super.fling(velocityX, velocityY);
    }

    private int solveVelocity(int velocit) {
        if (velocit > 0) {
            return Math.min(velocit, MAX_VELOCIT);
        } else {
            return Math.max(velocit, -MAX_VELOCIT);
        }
    }

}
