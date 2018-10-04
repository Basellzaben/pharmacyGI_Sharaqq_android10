package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 09/03/2016.
 */
public class Cls_Day_Adapter  extends BaseAdapter {

    Context context;
    ArrayList<Cls_Day> cls_days;

    public Cls_Day_Adapter(Context context, ArrayList<Cls_Day> list) {

        this.context = context;
        cls_days = list;
    }

    @Override
    public int getCount() {

        return cls_days.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_days.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Day cls_day = cls_days.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.day_row, null);

        }
        MyTextView DayNm = (MyTextView) convertView.findViewById(R.id.tv_DayNm);
        DayNm.setText(cls_day.getDayNm());

        return convertView;
    }

}


