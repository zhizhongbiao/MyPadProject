package com.dcamp.pad.ui.platform;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.dcamp.pad.R;
import com.dcamp.pad.ui.base.AppWrapperMvpActivity;
import com.dcamp.pad.ui.base.animation.Effectstype;
import com.dcamp.pad.ui.base.animation.effects.BaseEffects;
import com.dcamp.pad.ui.platform.adapter.PlatformPageAdapter;
import com.dcamp.pad.ui.platform.bean.LikeBean;
import com.dcamp.pad.ui.platform.bean.SubjectDetailBean;
import com.dcamp.pad.ui.platform.listener.OnPageChangeListener;
import com.dcamp.pad.utils.WindowUtil;
import com.easyder.wrapper.model.BaseVo;
import com.easyder.wrapper.network.ApiConfig;
import com.easyder.wrapper.presenter.MvpBasePresenter;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import me.winds.widget.autolayout.AutoFrameLayout;
import me.winds.widget.autolayout.AutoLinearLayout;

/**
 * Auther   : ZZB
 * Date     : 2017/6/29
 * Desc     : 展台页面
 */

public class PlatformActivity extends AppWrapperMvpActivity<MvpBasePresenter> implements View.OnClickListener, Runnable {

    @BindView(R.id.llAllTable)
    AutoLinearLayout llAllTable;
    //TODO 产品说 暂时不要
    //    @BindView(R.id.llBrandIntroduce)
    //    AutoLinearLayout llBrandIntroduce;
    @BindView(R.id.tvPageIndicator)
    TextView tvPageIndicator;
    @BindView(R.id.vpContent)
    ViewPager vpContent;
    @BindView(R.id.tvLike)
    TextView tvLike;
    @BindView(R.id.flVpWrapper)
    AutoFrameLayout flVpWrapper;
    @BindView(R.id.flLike)
    AutoFrameLayout flLike;

    private static final long CLICK_LIKE_INTERVAL = 1000 * 30;
    private static final long AUTO_PLAY_INTERVAL = 1000 * 10;
    private static final String SUBJECT_ID_KEY = "subjectId";
    private int currentPos = 0;
    private float downX;
    PlatformPageAdapter mAdapter;
    private int subjectId;
    private List<SubjectDetailBean.ListBean> goodList;
    private long lastTimeClick;
    String type = "已赞";
    private boolean toRight;

    private boolean isTouching;
    private boolean isAsc = true; //正序   倒序
    private final static int WHAT_TURN = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_TURN:
                    if (!isTouching && mAdapter != null) {
                        int item = vpContent.getCurrentItem();
                        int count = mAdapter.getCount();
                        if (count > 0 && count - 1 >= item) {
                            if (count != 1) {
                                if (item == 0) {
                                    isAsc = true;
                                }
                                if (item == count - 1) {
                                    isAsc = false;
                                }
                                vpContent.setCurrentItem(isAsc ? item + 1 : item - 1, true);
                            }
                        }
                    }
                    handler.sendEmptyMessageDelayed(WHAT_TURN, 8 * 1000);
                    break;
            }
        }
    };

    public static Intent getIntent(Context context, int subjectId) {
        Intent intent = new Intent(context, PlatformActivity.class);
        intent.putExtra(SUBJECT_ID_KEY, subjectId);
        return intent;
    }

    @Override
    public int getViewLayout() {
        return R.layout.activity_platform;
    }

    @Override
    protected void initView(Bundle savedInstanceState, Intent intent) {
        subjectId = getIntent().getIntExtra(SUBJECT_ID_KEY, -1);
        setOnViewClick();
        initialPageAdapter();
        handler.sendEmptyMessageDelayed(WHAT_TURN, 3 * 1000);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getList(subjectId);
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_SUBJECT_DETAIL)) {
            SubjectDetailBean detailBean = (SubjectDetailBean) dataVo;
            resetTvLike(detailBean.timeDownLimit * 1000);
            goodList = detailBean.list;
            mAdapter.setData(goodList);
            initTvLikeAndTvPageIndicator(detailBean);
        } else if (url.contains(ApiConfig.API_SUBJECT_CLICK_LIKE)) {
            LikeBean likeBean = (LikeBean) dataVo;
            tvLike.setText(type + likeBean.info.curGoodsNum);
            setTvLikeState(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAllTable://全部展台
                finish();
                break;
            case R.id.llBrandIntroduce://品牌介绍
                BrandIntroduceFragment dialogFragment = BrandIntroduceFragment
                        .getInstance(goodList.get(currentPos).brandDescription);
                dialogFragment.show(getFragmentManager(), BrandIntroduceFragment.TAG);
                break;
            case R.id.flLike://点赞
//                setClickAniamtion(tvLike);
//                playClickSound(R.raw.click);
                circleAnimation(flLike);
                if (System.currentTimeMillis() - lastTimeClick < 2500) {
//                    showToast("亲!你不能短时间内连续快速点击哦!");
                    return;
                }
                lastTimeClick = System.currentTimeMillis();
                clickLikeRequest(subjectId);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        //释放UI Handler资源，防止内存泄漏
        tvLike.removeCallbacks(this);
        super.onDestroy();
    }

    @Override
    public void run() {
//        规定时间后点赞回复原样
        setTvLikeState(false);
    }

    /**
     * 获取物品列表
     *
     * @param subjectId 对应活动ID
     */
    private void getList(int subjectId) {
        ArrayMap<String, Serializable> params = new ArrayMap<>();
        params.put("id", subjectId);
        presenter.getData(ApiConfig.API_SUBJECT_DETAIL, params, SubjectDetailBean.class);
    }

    /**
     * 定时将点赞恢复原样
     *
     * @param deyTime 定时时间
     */
    private void resetTvLike(long deyTime) {
        tvLike.postDelayed(this, deyTime);
    }

    /**
     * 初始化页面指标和第一页的点赞数
     *
     * @param detailBean
     */
    private void initTvLikeAndTvPageIndicator(SubjectDetailBean detailBean) {
        int page = 0, likeCount = 0;
        if (detailBean.list.size() > 0) {
            page = 1;
            likeCount = detailBean.goodNum;
        }
        tvLike.setText(type + likeCount);
        tvPageIndicator.setText(page + "/" + this.goodList.size());
    }

    /**
     * 初始化PageAdapter
     */
    private void initialPageAdapter() {

        //设置Page间间距
        vpContent.setPageMargin(40);
        //设置缓存的页面数量
//        vpContent.setOffscreenPageLimit(3);
        vpContent.setOffscreenPageLimit(2);
        vpContent.setPageTransformer(false, new ScaleInTransformer());
        mAdapter = new PlatformPageAdapter(this, goodList);
        vpContent.setAdapter(mAdapter);


        vpContent.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position != 0 && position != mAdapter.getCount() - 1) {
                    isAsc = position > currentPos;
                }

                currentPos = position;
                tvPageIndicator.setText(String.format("%1$d/%2$d", position + 1, goodList.size()));
            }
        });


        /**
         * 复写该触摸事件是为了改善ViewPager边缘触控体验
         */
        flVpWrapper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vpContent.dispatchTouchEvent(event);
            }
        });


        vpContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (goodList == null) return false;
                int screenWidth = WindowUtil.getScreenWidth(PlatformActivity.this);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isTouching = true;

//                        vpContent.removeCallbacks(autoPlayTimer);
                        downX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        isTouching = false;
                        if (downX == event.getX()) {
                            if (downX < 110 && currentPos > 0) {
                                currentPos--;
                                vpContent.setCurrentItem(currentPos, true);
                                return true;

                            } else if (downX > (screenWidth - 110)
                                    && currentPos < (goodList.size() - 1)) {
                                currentPos++;
                                vpContent.setCurrentItem(currentPos, true);
                                return true;
                            }
                        } else if (downX > event.getX()) {
                            toRight = true;
                        } else {
                            toRight = false;
                        }

//                        vpContent.postDelayed(autoPlayTimer, AUTO_PLAY_INTERVAL);
                        break;

                }

                return false;
            }
        });

    }

    /**
     * 设置控件的点击事件
     */
    private void setOnViewClick() {
        llAllTable.setOnClickListener(this);
        flLike.setOnClickListener(this);
    }

    /**
     * 播放点击声音
     *
     * @param soundSourceResId
     */
    private void playClickSound(int soundSourceResId) {
        SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        final int soundID = soundPool.load(this, soundSourceResId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(
                        soundID,
                        1f,      //左耳道音量【0~1】
                        1f,      //右耳道音量【0~1】
                        100,         //播放优先级【0表示最低优先级】
                        1,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                        1          //播放速度【1是正常，范围从0~2】
                );
            }
        });

    }

    /**
     * 发送点赞请求
     *
     * @param goodId
     */
    private void clickLikeRequest(int goodId) {
        ArrayMap<String, Serializable> params = new ArrayMap<>();
        params.put("id", goodId);
        presenter.getData(ApiConfig.API_SUBJECT_CLICK_LIKE, params, LikeBean.class);
    }

    /**
     * 设置点赞状态
     */
    private void setTvLikeState(boolean isLiked) {
        int color, drawableId;
        if (isLiked) {
            color = Color.parseColor("#FFFFFF");
            drawableId = R.drawable.like_press;
            resetTvLike(CLICK_LIKE_INTERVAL);
        } else {
            color = Color.parseColor("#333333");
            drawableId = R.drawable.like;
        }
        tvLike.setTextColor(color);
        flLike.setBackgroundResource(drawableId);
    }

    /**
     * 点赞动画
     */
    private void setClickAniamtion(View view) {
        BaseEffects animator = Effectstype.ZoomIn.getAnimator();
        animator.setDuration(500);
        animator.start(view);
    }

    /**
     * 圆点扩撒动画
     *
     * @param view
     */
    private void circleAnimation(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            double r_I = 0;
            double r_II = Math.hypot(view.getWidth(), view.getHeight());
            Animator animator = ViewAnimationUtils.createCircularReveal(view, view.getWidth() / 2
                    , view.getHeight() / 2
                    , (int) r_I, (int) r_II);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(500);
            animator.start();

        }
    }


    private Runnable autoPlayTimer = new Runnable() {
        @Override
        public void run() {
            autoPlay();
        }
    };


    private void autoPlay() {
        if (goodList != null
                && goodList.size() != 0) {
            if (currentPos == goodList.size() - 1) {
                toRight = false;
            } else if (currentPos == 0) {
                toRight = true;
            }

            if (toRight) {
                vpContent.setCurrentItem(currentPos + 1, true);
            } else {
                vpContent.setCurrentItem(currentPos - 1, true);
            }
        }
        vpContent.postDelayed(autoPlayTimer, AUTO_PLAY_INTERVAL);
    }
}
