package com.example.r2snote.DTO;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.r2snote.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<Modal> list;
    public ListViewAdapter(ArrayList<Modal> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Modal getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayoutCompat view;
        if (convertView == null) {
            view = (LinearLayoutCompat) View.inflate(parent.getContext(), R.layout.activity_list_item, null);
        } else view = (LinearLayoutCompat) convertView;

        Modal item = getItem(position);
        ((TextView) view.findViewById(R.id.txtNameCate)).setText(("Name: " +  item.getName()));
        String cd = item.getCreateDate().getDate() + "/" + (item.getCreateDate().getMonth()+1) + "/"
                + (item.getCreateDate().getYear() + 1900);
        ((TextView) view.findViewById(R.id.txtCreateDateCate)).setText(String.format("Create date: %s", cd));
        return view;
    }
}