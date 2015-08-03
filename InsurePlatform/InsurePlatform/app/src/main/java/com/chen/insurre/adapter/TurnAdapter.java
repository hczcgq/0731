package com.chen.insurre.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chen.insurre.R;
import com.chen.insurre.bean.TurnListItem;

import java.util.List;

/**
 * Created by chenguoquan on 8/2/15.
 */
public class TurnAdapter extends BaseAdapterHelpter<TurnListItem>{

    private Context context;
    private List<TurnListItem> datas;

    public TurnAdapter(Context context, List<TurnListItem> datas) {
        super(context, datas);
        this.context=context;
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderHelper holder = ViewHolderHelper.get(context, convertView,
                parent, R.layout.view_turn_list_item, position);
        TextView name=holder.getView(R.id.NameTextView);
        TextView idCard=holder.getView(R.id.IDCardTextView);
        TextView time=holder.getView(R.id.TimeTextView);
        TurnListItem item=datas.get(position);
        name.setText(item.getName());
        idCard.setText(item.getIdCard());
        time.setText(item.getCreate_time());
        return holder.getConvertView();
    }
}
