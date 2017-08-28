package com.easyder.wrapper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.easyder.wrapper.utils.UIUtils;

import java.util.List;

import me.winds.widget.autolayout.utils.AutoUtils;

/**
 * @author 刘琛慧
 * @version 1.0
 * @date 2017/3/4
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements DataHelper<T> {
    protected List<T> mDataList;
    protected Context mContext;
    protected int layoutResourceId;

    public CommonRecyclerAdapter(List<T> mDataList, Context mContext) {
        this.mDataList = mDataList;
        this.mContext = mContext;
    }


    public CommonRecyclerAdapter(List<T> mDataList, Context mContext, int layoutResourceId) {
        this(mDataList, mContext);
        this.layoutResourceId = layoutResourceId;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AutoUtils.auto(UIUtils.inflate(layoutResourceId, parent));
        return new RecyclerViewHolder(UIUtils.inflate(layoutResourceId, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        T data = mDataList.get(position);
        bindData(holder, data, position);
    }

    protected abstract void bindData(RecyclerViewHolder holder, T data, int position);

    @Override
    public int getItemViewType(int position) {
        return layoutResourceId;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    @Override
    public List<T> getDataSet() {
        return mDataList;
    }


    @Override
    public void updateAll(List<T> newDatas) {
        mDataList = newDatas;
        notifyDataSetChanged();
    }


    @Override
    public void addAll(List<T> newDatas) {
        if (mDataList != null) {
            mDataList.addAll(newDatas);
            notifyDataSetChanged();
        }
    }

    @Override
    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void addHead(T data) {
        if (mDataList != null) {
            mDataList.add(0, data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void append(T data) {
        if (mDataList != null) {
            mDataList.add(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void replace(int index, T data) {
        if (mDataList != null) {
            mDataList.add(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void replace(T oldData, T newData) {
        if (mDataList != null) {
            replace(mDataList.indexOf(oldData), newData);
            notifyDataSetChanged();
        }
    }

    @Override
    public void insert(int index, T data) {
        if (mDataList != null) {
            mDataList.add(index, data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(int index) {
        if (mDataList != null) {
            mDataList.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(T data) {
        if (mDataList != null) {
            mDataList.remove(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeHead() {
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.remove(0);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeTail() {
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.remove(mDataList.size() - 1);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeAll(List<T> datas) {
        if (mDataList != null) {
            mDataList.removeAll(datas);
            notifyDataSetChanged();
        }
    }
}
