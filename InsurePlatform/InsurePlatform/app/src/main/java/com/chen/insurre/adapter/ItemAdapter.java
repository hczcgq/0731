package com.chen.insurre.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chen.insurre.R;
import com.chen.insurre.bean.ItemInfo;

import java.util.List;

/**
 * Created by hm-soft on 2015/8/3.
 */
public class ItemAdapter extends BaseAdapterHelpter<ItemInfo>{

    private Context context;
    private List<ItemInfo> datas;

    public ItemAdapter(Context context, List<ItemInfo> datas) {
        super(context, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderHelper holder = ViewHolderHelper.get(context, convertView,
                parent, R.layout.view_item, position);
        TextView NameTextView=holder.getView(R.id.NameTextView);
        NameTextView.setText(datas.get(position).getName());
        return holder.getConvertView();
    }
}
