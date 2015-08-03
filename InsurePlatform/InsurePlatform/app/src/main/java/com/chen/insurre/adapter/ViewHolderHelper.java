package com.chen.insurre.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolderHelper {

    private SparseArray<View> mViews;

    private int mPosition;

    private View mConvertView;

    public ViewHolderHelper(Context context, ViewGroup parent, int layoutId,
            int position) {
        mViews = new SparseArray<View>();
        this.mPosition = position;
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId,
                parent, false);
        this.mConvertView.setTag(this);
    }

    public static ViewHolderHelper get(Context context, View convertView,
            ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolderHelper(context, parent, layoutId, position);
        } else {
            ViewHolderHelper holder = (ViewHolderHelper) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

}
