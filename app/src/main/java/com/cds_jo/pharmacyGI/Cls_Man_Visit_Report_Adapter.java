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

/**
 * Created by Hp on 06/03/2016.
 */
public class Cls_Man_Visit_Report_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Man_Visit_Report> cls_acc_reports;


    public Cls_Man_Visit_Report_Adapter(Context context, ArrayList<Cls_Man_Visit_Report> list) {

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

        Cls_Man_Visit_Report  cls_acc_report = cls_acc_reports.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.man_visit_report_row, null);
        }


        MyTextView tv_Day = (MyTextView) convertView.findViewById(R.id.tv_Day);
        MyTextView tv_Date = (MyTextView) convertView.findViewById(R.id.tv_Date);
        MyTextView tv_Visited = (MyTextView) convertView.findViewById(R.id.tv_Visited);
        MyTextView tv_Located = (MyTextView) convertView.findViewById(R.id.tv_Located);
        MyTextView tv_percent = (MyTextView) convertView.findViewById(R.id.tv_percent);

        MyTextView tv_CityNm = (MyTextView) convertView.findViewById(R.id.tv_CityNm);




        tv_Day.setText(cls_acc_report.getDay());
        tv_Date.setText(cls_acc_report.getDate());
        tv_Visited.setText(cls_acc_report.getCustVisitedCount());
        tv_Located.setText(cls_acc_report.getCustInLoctCount());
        tv_percent.setText(cls_acc_report.getPrecent());
        tv_Date.setText(cls_acc_report.getDate());

        tv_CityNm.setText(cls_acc_report.getCityName());





        LinearLayout   RR=(LinearLayout)convertView.findViewById(R.id.RR);
        if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.White));
            tv_Day.setBackgroundColor(context.getResources().getColor(R.color.Black11));
            tv_Date.setBackgroundColor(context.getResources().getColor(R.color.Black11));
            tv_Visited.setBackgroundColor(context.getResources().getColor(R.color.Black11));
            tv_Located.setBackgroundColor(context.getResources().getColor(R.color.Black11));
            tv_percent.setBackgroundColor(context.getResources().getColor(R.color.Black11));
            tv_CityNm.setBackgroundColor(context.getResources().getColor(R.color.Black11));

            tv_Day.setTextColor(Color.WHITE);
            tv_Date.setTextColor(Color.WHITE);
            tv_Visited.setTextColor(Color.WHITE);
            tv_Located.setTextColor(Color.WHITE);
            tv_percent.setTextColor(Color.WHITE);
            tv_CityNm.setTextColor(Color.WHITE);

        }
        else if(position%2==0)
        {
            RR.setBackgroundColor(Color.BLACK);

            tv_Day.setBackgroundColor(Color.WHITE);
            tv_Date.setBackgroundColor(Color.WHITE);
            tv_Visited.setBackgroundColor(Color.WHITE);
            tv_Located.setBackgroundColor(Color.WHITE);
            tv_percent.setBackgroundColor(Color.WHITE);
            tv_CityNm.setBackgroundColor(Color.WHITE);

            tv_Day.setTextColor(Color.BLACK);
            tv_Date.setTextColor(Color.BLACK);
            tv_Visited.setTextColor(Color.BLACK);
            tv_Located.setTextColor(Color.BLACK);
            tv_percent.setTextColor(Color.BLACK);
            tv_CityNm.setTextColor(Color.BLACK);

        }
        else
        {
            RR.setBackgroundColor(Color.BLACK);

            tv_Day.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_Date.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_Visited.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_Located.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_percent.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_CityNm.setBackgroundColor(context.getResources().getColor(R.color.Gray2));


            tv_Day.setTextColor(Color.BLACK);
            tv_Date.setTextColor(Color.BLACK);
            tv_Visited.setTextColor(Color.BLACK);
            tv_Located.setTextColor(Color.BLACK);
            tv_percent.setTextColor(Color.BLACK);
            tv_CityNm.setTextColor(Color.BLACK);

        }




        return convertView;
    }

}

