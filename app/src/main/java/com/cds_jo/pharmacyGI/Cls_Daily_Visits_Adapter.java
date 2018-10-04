package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 09/03/2016.
 */
public class Cls_Daily_Visits_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Daily_Visits> cls_daily_visitses;

    public Cls_Daily_Visits_Adapter(Context context, ArrayList<Cls_Daily_Visits> list) {

        this.context = context;
        cls_daily_visitses = list;
    }

    @Override
    public int getCount() {

        return cls_daily_visitses.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_daily_visitses.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Daily_Visits clsDailyVisits = cls_daily_visitses.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.daily_visit_row, null);
        }

        MyTextView CustNo = (MyTextView) convertView.findViewById(R.id.tv_CustNo);
        CustNo.setText(clsDailyVisits.getCustNo());

        MyTextView CustNm = (MyTextView) convertView.findViewById(R.id.tv_CustNm);
        CustNm.setText(clsDailyVisits.getCustNm());

        MyTextView CustMobile = (MyTextView) convertView.findViewById(R.id.tv_CustMobile);
        CustMobile.setText(clsDailyVisits.getCustMobile());

        MyTextView CustAddress = (MyTextView) convertView.findViewById(R.id.tv_CustAddress);
        CustAddress.setText(clsDailyVisits.getCustAddress());

        MyTextView Notes = (MyTextView) convertView.findViewById(R.id.tv_Notes);
        Notes.setText(clsDailyVisits.getNotes());

        MyTextView Flq = (MyTextView) convertView.findViewById(R.id.tv_Flq);
        Flq.setText(clsDailyVisits.getFlq());

        MyTextView tv_tf = (MyTextView) convertView.findViewById(R.id.tv_tf);
        tv_tf.setText(clsDailyVisits.getTf());

        MyTextView tv_tt = (MyTextView) convertView.findViewById(R.id.tv_tt);
        tv_tt.setText(clsDailyVisits.getTt());

        MyTextView tv_custype = (MyTextView) convertView.findViewById(R.id.tv_CusType);
        tv_custype.setText(clsDailyVisits.getCustType());



        if(clsDailyVisits.getSetColor().equals("-2")){
            Flq.setTextColor(Color.BLACK);
        }
         if(clsDailyVisits.getSetColor().equals("0")){
            Flq.setTextColor(Color.BLUE);
        }
        else if(clsDailyVisits.getSetColor().equals("1"))
        {
            Flq.setTextColor(Color.GREEN);
        }
        else if(clsDailyVisits.getSetColor().equals("-1"))
        {
            Flq.setTextColor(Color.RED);
        }

        if(position==0)
        {
             CustNo.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             CustNo.setTextColor(Color.WHITE);

             CustNm .setBackgroundColor(context.getResources().getColor(R.color.Black11));
             CustNm.setTextColor(Color.WHITE);

             CustMobile.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             CustMobile.setTextColor(Color.WHITE);

             CustAddress.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             CustAddress .setTextColor(Color.WHITE);

             Notes.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             Notes .setTextColor(Color.WHITE);

             Flq .setBackgroundColor(context.getResources().getColor(R.color.Black11));
             Flq.setTextColor(Color.WHITE);

             tv_tf.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             tv_tf.setTextColor(Color.WHITE);

             tv_tt .setBackgroundColor(context.getResources().getColor(R.color.Black11));
             tv_tt .setTextColor(Color.WHITE);

             tv_custype .setBackgroundColor(context.getResources().getColor(R.color.Black11));
             tv_custype .setTextColor(Color.WHITE);
        }
        else
        {
            CustNo.setBackgroundColor(Color.WHITE);
            CustNo.setTextColor(Color.BLACK);

            CustNm .setBackgroundColor(Color.WHITE);
            CustNm.setTextColor(Color.BLACK);

            CustMobile.setBackgroundColor(Color.WHITE);
            CustMobile.setTextColor(Color.BLACK);

            CustAddress.setBackgroundColor(Color.WHITE);
            CustAddress .setTextColor(Color.BLACK);

            Notes.setBackgroundColor(Color.WHITE);
            Notes .setTextColor(Color.BLACK);

            Flq .setBackgroundColor(Color.WHITE);
            Flq.setTextColor(Color.BLACK);

            tv_tf.setBackgroundColor(Color.WHITE);
            tv_tf.setTextColor(Color.BLACK);

            tv_tt .setBackgroundColor(Color.WHITE);
            tv_tt .setTextColor(Color.BLACK);

            tv_custype .setBackgroundColor(Color.WHITE);
            tv_custype .setTextColor(Color.BLACK);
        }
        return convertView;
    }

}



