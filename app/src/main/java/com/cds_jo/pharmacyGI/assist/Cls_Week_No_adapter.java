package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

import static com.cds_jo.pharmacyGI.R.id.RR1;


public class Cls_Week_No_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_WeekDays> cls_search_pos;

    public Cls_Week_No_adapter(Context context, ArrayList<Cls_WeekDays> list) {

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
        Cls_WeekDays obj = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.week_no_rows, null);

        }
        TextView tv_ID = (TextView) convertView.findViewById(R.id.tv_ID);
        tv_ID.setText(obj.getNo());


        Methdes.MyTextView tv_Nm = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Nm);
        tv_Nm.setText(obj.getDesc());

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

