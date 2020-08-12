package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.pharmacyGI.assist.Customers;

import java.util.ArrayList;

import Methdes.MyTextView;

import static com.cds_jo.pharmacyGI.R.id.RR1;

/**
 * Created by Hp on 03/03/2016.
 */
public class Filter_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Filter> customerses;

    public Filter_Adapter(Context context, ArrayList<Cls_Filter> list) {

        this.context = context;
        customerses = list;
    }

    @Override
    public int getCount() {

        return customerses.size();
    }

    @Override
    public Object getItem(int position) {

        return customerses.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        Cls_Filter customersesobj = customerses.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.filters_row, null);

        }

        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);
        MyTextView tv_Desc = (MyTextView) convertView.findViewById(R.id.tv_Desc);
        tv_Desc.setText(customersesobj.getDesc());
        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));

        }
        return convertView;
    }

}
