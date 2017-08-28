package com.dcamp.pad.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dcamp.pad.R;
import com.dcamp.pad.ui.home.bean.SubjectListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.winds.widget.autolayout.utils.AutoUtils;

/**
 * Auther   : ZZB
 * Date     : 2017/6/29
 * Desc     : 首页GridView适配器
 */

public class SubjectTitleAdapter extends BaseAdapter {

    private Context context;
    private List<SubjectListBean.ListBean> data;

    private LayoutInflater layoutInflater;

    public void setData(List<SubjectListBean.ListBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public SubjectTitleAdapter(Context context, List<SubjectListBean.ListBean> data) {
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public SubjectListBean.ListBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_home_page, parent, false);
            AutoUtils.auto(convertView);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }

        SubjectListBean.ListBean bean = data.get(position);
        holder.tvNum.setText(bean.no);
        holder.tvSortTitle.setText(bean.name);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.tvSortTitle)
        TextView tvSortTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
