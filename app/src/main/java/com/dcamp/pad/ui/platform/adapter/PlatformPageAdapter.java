package com.dcamp.pad.ui.platform.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dcamp.pad.R;
import com.dcamp.pad.ui.base.transformer.RoundedCornersTransformation;
import com.dcamp.pad.ui.platform.bean.SubjectDetailBean;
import com.easyder.wrapper.manager.ImageManager;
import com.easyder.wrapper.network.ApiConfig;
import com.easyder.wrapper.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.winds.widget.autolayout.utils.AutoUtils;

/**
 * Auther   : ZZB
 * Date     : 2017/6/30
 * Desc     : 展台页面适配器
 */

public class PlatformPageAdapter extends PagerAdapter {


    @BindView(R.id.ivGood)
    ImageView ivGood;
    @BindView(R.id.tvGoodName)
    TextView tvGoodName;
    @BindView(R.id.tvGoodDesc)
    TextView tvGoodDesc;
    @BindView(R.id.tvGoodPrice)
    TextView tvGoodPrice;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.rvActivity)
    RecyclerView rvActivity;
    @BindView(R.id.ivBargain)
    ImageView ivBargain;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<SubjectDetailBean.ListBean> data;

    public void setData(List<SubjectDetailBean.ListBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public PlatformPageAdapter(Context context, List<SubjectDetailBean.ListBean> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View contentView = layoutInflater
                .inflate(R.layout.view_subject_platform, null, false);
        ButterKnife.bind(this, contentView);
        AutoUtils.auto(contentView);
        showContentView(position);
        container.addView(contentView);
        return contentView;
    }

    /**
     * 展示每一个展台View中的数据
     */
    private void showContentView(int position) {
        if (data == null) {
            LogUtils.e("PlatformPageAdapter data=null");
            return;
        }
        final SubjectDetailBean.ListBean bean = data.get(position);

        ivBargain.setVisibility(bean.isJoinBargain ? View.VISIBLE : View.GONE);

        tvGoodName.setText(bean.name);
        tvGoodDesc.setText(bean.slogan);
        tvGoodPrice.setText(String.format("￥%1$.2f", bean.price));
//        ImageManager.load(context, bean.pic, R.mipmap.place_holder, ivGood);
        ImageManager.load(context, bean.qrcode, R.mipmap.qrcode_place_holder, ivQRCode);

        if (!bean.pic.startsWith("http")) {
            bean.pic = ApiConfig.HOST + bean.pic;
        }
        //原图处理成圆角，如果是四周都是圆角则是RoundedCornersTransformation.CornerType.ALL
        Glide.with(context)
                .load(bean.pic)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.place_holder)
                .bitmapTransform(new RoundedCornersTransformation(context, 5, 0, RoundedCornersTransformation.CornerType.ALL))
                .crossFade(1000)
                .into(ivGood);


        rvActivity.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvActivity.setAdapter(new ActivityAdapter(bean.joinActivity));

        rvActivity.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                //TODO 产品说 暂时不要
//                Intent intent = new Intent("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(bean.joinActivity.get(position).url);
//                intent.setData(content_url);
//                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//                context.startActivity(intent);

            }
        });

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

}