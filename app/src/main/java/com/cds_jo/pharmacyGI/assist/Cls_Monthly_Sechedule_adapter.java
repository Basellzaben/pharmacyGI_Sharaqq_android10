package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

import Methdes.MyTextView;

import static com.cds_jo.pharmacyGI.R.id.RR1;

/**
 * Created by Hp on 05/03/2016.
 */
public class Cls_Monthly_Sechedule_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Monthly_Schedule> cls_search_pos;

    public Cls_Monthly_Sechedule_adapter(Context context, ArrayList<Cls_Monthly_Schedule> list) {

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
        Cls_Monthly_Schedule obj = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.monthly_sechedule_row, null);

        }
        MyTextView tv_Date = (MyTextView) convertView.findViewById(R.id.tv_Date);
        tv_Date.setText(obj.getDate());


        MyTextView tv_dayNum = (MyTextView) convertView.findViewById(R.id.tv_dayNum);
        tv_dayNum.setText(obj.getDayNm());

        MyTextView tv_CountryNm = (MyTextView) convertView.findViewById(R.id.tv_CountryNm);
        tv_CountryNm.setText(obj.getCountryNm());


        MyTextView tv_PeriodDesc = (MyTextView) convertView.findViewById(R.id.tv_PeriodDesc);
        tv_PeriodDesc.setText(obj.getPeriodDesc());


        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);
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

