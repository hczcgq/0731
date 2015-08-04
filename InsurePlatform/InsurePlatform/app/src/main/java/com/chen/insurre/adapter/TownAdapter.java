package com.chen.insurre.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chen.insurre.R;
import com.chen.insurre.bean.ItemInfo;
import com.chen.insurre.bean.TownInfo;

import java.util.List;

/**
 * Created by hm-soft on 2015/8/3.
 */
public class TownAdapter extends BaseAdapterHelpter<TownInfo>{

    private Context context;
    private List<TownInfo> datas;

    public TownAdapter(Context context, List<TownInfo> datas) {
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
