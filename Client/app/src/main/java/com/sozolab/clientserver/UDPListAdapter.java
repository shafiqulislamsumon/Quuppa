package com.sozolab.clientserver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UDPListAdapter extends BaseAdapter {

    private ArrayList<UDPData> listData;
    private LayoutInflater layoutInflater;

    public UDPListAdapter(Context aContext, ArrayList<UDPData> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.messageItem = (TextView) v.findViewById(R.id.messageItem);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.messageItem.setText(listData.get(position).getMessage());
        return v;
    }
    static class ViewHolder {
        TextView messageItem;
    }
}
