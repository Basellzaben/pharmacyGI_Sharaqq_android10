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
 * Created by Hp on 05/03/2016.
 */
public class cls_Tab_Visits_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_SaleManDailyRound> cls_Tab_Sales;

    public cls_Tab_Visits_adapter(Context context, ArrayList<Cls_SaleManDailyRound> list) {

        this.context = context;
        cls_Tab_Sales = list;
    }

    @Override
    public int getCount() {

        return cls_Tab_Sales.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_Tab_Sales.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_SaleManDailyRound cls_search_po = cls_Tab_Sales.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tab_visits_adapter, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        MyTextView tv_OrderNo = (MyTextView) convertView.findViewById(R.id.tv_OrderNo);
        tv_OrderNo.setText(cls_search_po.getOrderNo());

        MyTextView tv_CustNo = (MyTextView) convertView.findViewById(R.id.tv_CustNo);
        tv_CustNo.setText(cls_search_po.getCusNo());


        MyTextView tv_notes = (MyTextView) convertView.findViewById(R.id.tv_notes);
        tv_notes.setText(cls_search_po.getNotes());

        MyTextView tvDate = (MyTextView) convertView.findViewById(R.id.tvDate);
        tvDate.setText(cls_search_po.getTr_Data());


        MyTextView tv_StartTime = (MyTextView) convertView.findViewById(R.id.tv_StartTime);
        tv_StartTime.setText(cls_search_po.getStart_Time());





        MyTextView tv_EndTime = (MyTextView) convertView.findViewById(R.id.tv_EndTime);
        tv_EndTime.setText(cls_search_po.getEnd_Time());

         MyTextView tv_Stutes = (MyTextView) convertView.findViewById(R.id.tv_Stutes);





          if(cls_search_po.getPosted().equals("-1")) {

              tv_Stutes.setText("غير مرحلة");

        }
        else {
              tv_Stutes.setText("مرحلة");

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

