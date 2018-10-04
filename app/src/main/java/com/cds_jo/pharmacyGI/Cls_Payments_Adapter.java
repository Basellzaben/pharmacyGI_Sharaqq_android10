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


public class Cls_Payments_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_TabPayments> cls_searchRecVous;

    public Cls_Payments_Adapter(Context context, ArrayList<Cls_TabPayments> list) {

        this.context = context;
        cls_searchRecVous = list;
    }

    @Override
    public int getCount() {

        return cls_searchRecVous.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_searchRecVous.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_TabPayments cls_searchRecVou = cls_searchRecVous.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tab_payments, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView tv_DocNo = (MyTextView) convertView.findViewById(R.id.tv_DocNo);
        tv_DocNo.setText(cls_searchRecVou.getDocNo());

        MyTextView tv_date = (MyTextView) convertView.findViewById(R.id.tv_date);
        tv_date.setText(cls_searchRecVou.getDate());

        MyTextView tv_Amt = (MyTextView) convertView.findViewById(R.id.tv_Amt);
        tv_Amt.setText(cls_searchRecVou.getAmt());

        MyTextView tv_CustNm = (MyTextView) convertView.findViewById(R.id.tv_CustNm);
        tv_CustNm.setText(cls_searchRecVou.getCustNm());

        MyTextView tv_notes = (MyTextView) convertView.findViewById(R.id.tv_notes);
        tv_notes.setText(cls_searchRecVou.getNotes());

        MyTextView tv_acc = (MyTextView) convertView.findViewById(R.id.tv_acc);
        tv_acc.setText(cls_searchRecVou.getAcc());

        MyTextView tv_type = (MyTextView) convertView.findViewById(R.id.tv_type);

        if(ComInfo.Lan.equalsIgnoreCase("ar")) {
                if (cls_searchRecVou.getType().equals("1")) {
                    tv_type.setText("نقدي");
                } else if (cls_searchRecVou.getType().equals("2")) {
                    tv_type.setText("شيكات");
                } else {
                    tv_type.setText("نقدي + شيكات");
                }
        }else {

            if(cls_searchRecVou.getType().equals("1")){
                tv_type.setText("Cash");
            }
            else if(cls_searchRecVou.getType().equals("2"))
            {
                tv_type.setText("Checks");
            }
            else
            {
                tv_type.setText("Cash And Checks");
            }



        }

        if(position%2==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
        }
        else
        {
            RR.setBackgroundColor(Color.WHITE);
        }



      if(cls_searchRecVou.getPost().equals("-1")){
            tv_CustNm.setTextColor(Color.BLACK);
        }
        else
        {
            tv_CustNm.setTextColor(Color.RED);
        }
        return convertView;


    }

}



