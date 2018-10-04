package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Methdes.MyTextView;


public class Cls_UsedCodes_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_UsedCodes> cls_usedCodes;

    public Cls_UsedCodes_Adapter(Context context, ArrayList<Cls_UsedCodes> list) {

        this.context = context;
        cls_usedCodes = list;
    }

    @Override
    public int getCount() {

        return cls_usedCodes.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_usedCodes.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_UsedCodes clsusedCodes = cls_usedCodes.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tab_usedcodes_row, null);
        }

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView tv_code = (MyTextView) convertView.findViewById(R.id.tv_code);
        tv_code.setText(clsusedCodes.getCode());

        MyTextView tv_itemNm = (MyTextView) convertView.findViewById(R.id.tv_itemNm);
        tv_itemNm.setText(clsusedCodes.getItemNo());

        MyTextView tv_CustNm = (MyTextView) convertView.findViewById(R.id.tv_CustNm);
        tv_CustNm.setText(clsusedCodes.getCustomerNo());

        MyTextView tv_OrderNo = (MyTextView) convertView.findViewById(R.id.tv_OrderNo);
        tv_OrderNo.setText(clsusedCodes.getOrderNo());

        MyTextView tv_date = (MyTextView) convertView.findViewById(R.id.tv_date);
        tv_date.setText(clsusedCodes.getTr_Date());

        MyTextView tv_Time = (MyTextView) convertView.findViewById(R.id.tv_Time);
        tv_Time.setText(clsusedCodes.getTr_Time());


        MyTextView tv_status = (MyTextView) convertView.findViewById(R.id.tv_status);
        if(clsusedCodes.getStatus().equals("1")){
            tv_status.setText("مقبول");
            tv_status.setTextColor(Color.GREEN);
        }
        else
        {
            tv_status.setText("غير مقبول");
            tv_status.setTextColor(Color.RED);
        }

        if(position%2==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
        }

        else
        {
            RR.setBackgroundColor(Color.WHITE);
        }

        return convertView;

    }

}



