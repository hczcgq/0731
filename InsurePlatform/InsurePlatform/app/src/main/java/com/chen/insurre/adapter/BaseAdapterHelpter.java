package com.chen.insurre.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseAdapterHelpter<T> extends BaseAdapter {

    @SuppressWarnings("unused")
    private Context context;

    private List<T> datas;

    public BaseAdapterHelpter(Context context, List<T> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int arg0, View arg1, ViewGroup arg2);

}
