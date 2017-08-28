package com.dcamp.pad.ui.platform.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dcamp.pad.R;
import com.dcamp.pad.ui.platform.bean.SubjectDetailBean;

import java.util.List;

import me.winds.widget.autolayout.utils.AutoUtils;

/**
 * Auther   : ZZB
 * Date     : 2017/7/1
 * Desc     : 活动recyclerView适配器
 */

public class ActivityAdapter extends BaseQuickAdapter<SubjectDetailBean.ListBean.JoinActivityBean, BaseViewHolder> {


    public ActivityAdapter(List<SubjectDetailBean.ListBean.JoinActivityBean> data) {
        super(R.layout.item_activity, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
        AutoUtils.auto(view);
        return new BaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubjectDetailBean.ListBean.JoinActivityBean item) {
        helper.setText(R.id.tvActivityFirst, item.name);
    }


}
