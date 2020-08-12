package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Methdes.MyTextView;

/**
 * Created by Hp on 06/03/2016.
 */
public class Cls_Sales_Compare_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_CompareSalesReport> cls_acc_reports;


    public Cls_Sales_Compare_Adapter(Context context, ArrayList<Cls_CompareSalesReport> list) {

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
        Cls_CompareSalesReport  cls_acc_report = cls_acc_reports.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sales_compare_row, null);
        }


        MyTextView tv_CusNm = (MyTextView) convertView.findViewById(R.id.tv_CusNm);

        MyTextView tv_Date = (MyTextView) convertView.findViewById(R.id.tv_Date);
        MyTextView tv_Total = (MyTextView) convertView.findViewById(R.id.tv_Total);



        tv_CusNm.setText(cls_acc_report.getCusname());

        tv_Date.setText(cls_acc_report.getBillDate());
        if(position==0){
            tv_Total.setText(cls_acc_report.getTotal());

        }else {
            tv_Total.setText(cls_acc_report.getTotal() );

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

        tv_CusNm.setTextColor(Color.BLACK);
        tv_CusNm.setBackgroundColor(Color.WHITE);

        tv_Date.setTextColor(Color.BLACK);
        tv_Date.setBackgroundColor(Color.WHITE);
        tv_Total.setTextColor(Color.BLACK);
        tv_Total.setBackgroundColor(Color.WHITE);


        if(position==0)
        {
            //RR.setBackgroundColor(context.getResources().getColor(R.color.Black11));

            tv_CusNm.setTextColor(Color.WHITE);
            tv_CusNm.setBackgroundColor(Color.parseColor("#17181c"));

            tv_Date.setTextColor(Color.WHITE);
            tv_Date.setBackgroundColor(Color.parseColor("#17181c"));
            tv_Total.setTextColor(Color.WHITE);
            tv_Total.setBackgroundColor(Color.parseColor("#17181c"));

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
    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
}

