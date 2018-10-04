package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.pharmacyGI.assist.Cls_Acc_Report;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 06/03/2016.
 */
public class Cls_CountrySales_Report_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Country_Sales> cls_acc_reports;


    public Cls_CountrySales_Report_Adapter(Context context, ArrayList<Cls_Country_Sales> list) {

        this.context = context;
        cls_acc_reports = list;
    }

    @Override
    public int getCount() {

        return cls_acc_reports.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_acc_reports.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Country_Sales  cls_acc_report = cls_acc_reports.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.country_sales_report_row, null);
        }


        MyTextView tv_CusNm = (MyTextView) convertView.findViewById(R.id.tv_CusNm);
        MyTextView tv_ItemNm = (MyTextView) convertView.findViewById(R.id.tv_ItemNm);
        MyTextView tv_UnitNm = (MyTextView) convertView.findViewById(R.id.tv_UnitNm);
        MyTextView tv_Qty = (MyTextView) convertView.findViewById(R.id.tv_Qty);
        MyTextView tv_price = (MyTextView) convertView.findViewById(R.id.tv_price);
        MyTextView tv_Date = (MyTextView) convertView.findViewById(R.id.tv_Date);
        MyTextView tv_ManNm = (MyTextView) convertView.findViewById(R.id.tv_ManNm);



        tv_CusNm.setText(cls_acc_report.getCusNm());
        tv_ItemNm.setText(cls_acc_report.getItemNm());
        tv_UnitNm.setText(cls_acc_report.getUnitNm());
        tv_Qty.setText(cls_acc_report.getQty());
        tv_price.setText(cls_acc_report.getPrice());
        tv_Date.setText(cls_acc_report.getDate());
        tv_ManNm.setText(cls_acc_report.getManNm());

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

        tv_CusNm.setTextColor(Color.BLACK);
        tv_CusNm.setBackgroundColor(Color.WHITE);
        tv_ItemNm.setTextColor(Color.BLACK);
        tv_ItemNm.setBackgroundColor(Color.WHITE);
        tv_UnitNm.setTextColor(Color.BLACK);
        tv_UnitNm.setBackgroundColor(Color.WHITE);
        tv_Qty.setTextColor(Color.BLACK);
        tv_Qty.setBackgroundColor(Color.WHITE);
        tv_price.setTextColor(Color.BLACK);
        tv_price.setBackgroundColor(Color.WHITE);
        tv_Date.setTextColor(Color.BLACK);
        tv_Date.setBackgroundColor(Color.WHITE);
        tv_ManNm.setTextColor(Color.BLACK);
        tv_ManNm.setBackgroundColor(Color.WHITE);


        if(position==0)
        {
            //RR.setBackgroundColor(context.getResources().getColor(R.color.Black11));

            tv_CusNm.setTextColor(Color.WHITE);
            tv_CusNm.setBackgroundColor(Color.parseColor("#17181c"));
            tv_ItemNm.setTextColor(Color.WHITE);
            tv_ItemNm.setBackgroundColor(Color.parseColor("#17181c"));
            tv_UnitNm.setTextColor(Color.WHITE);
            tv_UnitNm.setBackgroundColor(Color.parseColor("#17181c"));
            tv_Qty.setTextColor(Color.WHITE);
            tv_Qty.setBackgroundColor(Color.parseColor("#17181c"));
            tv_price.setTextColor(Color.WHITE);
            tv_price.setBackgroundColor(Color.parseColor("#17181c"));
            tv_Date.setTextColor(Color.WHITE);
            tv_Date.setBackgroundColor(Color.parseColor("#17181c"));
            tv_ManNm.setTextColor(Color.WHITE);
            tv_ManNm.setBackgroundColor(Color.parseColor("#17181c"));

        }
        else if(position%2==0)
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

