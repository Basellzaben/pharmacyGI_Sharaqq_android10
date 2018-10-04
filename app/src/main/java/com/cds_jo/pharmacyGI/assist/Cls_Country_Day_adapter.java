package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

/**
 * Created by Hp on 05/03/2016.
 */
public class Cls_Country_Day_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Country> cls_search_pos;

    public Cls_Country_Day_adapter(Context context, ArrayList<Cls_Country> list) {

        this.context = context;
        cls_search_pos = list;
    }

    @Override
    public int getCount() {

        return cls_search_pos.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_search_pos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Country obj = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.country_of_day, null);

        }
        TextView tv_ID = (TextView) convertView.findViewById(R.id.tv_ID);
        tv_ID.setText(obj.getID());


        TextView tv_Nm = (TextView) convertView.findViewById(R.id.tv_Nm);
        tv_Nm.setText(obj.getNm());


        return convertView;
    }

}

