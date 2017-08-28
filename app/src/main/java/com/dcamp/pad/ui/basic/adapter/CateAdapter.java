package com.dcamp.pad.ui.basic.adapter;

import android.content.Context;

import com.dcamp.pad.R;
import com.dcamp.pad.ui.basic.bean.CateVo;
import com.easyder.wrapper.adapter.CommonLVAdapter;
import com.easyder.wrapper.adapter.ListViewHolder;

/**
 * Auther:  winds
 * Data:    2017/7/8
 * Desc:
 */

public class CateAdapter extends CommonLVAdapter<CateVo.ListBean> {

    public CateAdapter(Context context) {
        super(context, null, R.layout.item_cate);
    }

    @Override
    protected void bindData(Context context, ListViewHolder holder, CateVo.ListBean dataVo) {
        holder.setText(R.id.tv_no, dataVo.no);
        holder.setText(R.id.tv_title, dataVo.name);
    }
}
