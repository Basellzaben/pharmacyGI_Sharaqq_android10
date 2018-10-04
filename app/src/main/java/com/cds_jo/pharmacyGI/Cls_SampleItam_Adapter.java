package com.cds_jo.pharmacyGI;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class Cls_SampleItam_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_SampleItem> contactList;

    public Cls_SampleItam_Adapter(Context context, ArrayList<Cls_SampleItem> list) {

        this.context = context;
        contactList = list;
    }

    @Override
    public int getCount() {

        return contactList.size();
    }

    @Override
    public Object getItem(int position) {

        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_SampleItem contactListItems = contactList.get(position);
       /* if (position == 0) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.header_sample_item_row, null);

        } else {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.sample_item, null);

            }
        }*/

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sample_item, null);

        }

          LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);


            Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
            tvSlNo.setText(contactListItems.getItemNo());
            Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.tv_name);
            tvName.setText(contactListItems.getItemNm());

            Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Qty);
            tvQTY.setText(contactListItems.getQty());

            Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Unit);
            Unit.setText(contactListItems.getUnitNm());


            Methdes.MyTextView tv_doctNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_doctNo);
            tv_doctNo.setText(contactListItems.getDoctorNo());

            Methdes.MyTextView tv_doctNm = (Methdes.MyTextView) convertView.findViewById(R.id.tv_doctNm);
            tv_doctNm.setText(contactListItems.getDoctorNm());

     //  Toast.makeText(context,position+"" , Toast.LENGTH_SHORT).show();
     /*   if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Black11));
            tvSlNo.setTextColor(Color.WHITE);
            tvName.setTextColor(Color.WHITE);
            tvQTY.setTextColor(Color.WHITE);
            Unit.setTextColor(Color.WHITE);
            tv_doctNo.setTextColor(Color.WHITE);
            tv_doctNm.setTextColor(Color.WHITE);

        }*/

          if(position%2==0  )
            {   RR.setBackgroundColor(Color.WHITE);
                tvSlNo.setTextColor(Color.BLACK);
                tvName.setTextColor(Color.BLACK);
                tvQTY.setTextColor(Color.BLACK);
                Unit.setTextColor(Color.BLACK);
                tv_doctNo.setTextColor(Color.BLACK);
                tv_doctNm.setTextColor(Color.BLACK);

            }
            else if(position%2==1    )
            {
                RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
                tvSlNo.setTextColor(Color.BLACK);
                tvName.setTextColor(Color.BLACK);
                tvQTY.setTextColor(Color.BLACK);
                Unit.setTextColor(Color.BLACK);
                tv_doctNo.setTextColor(Color.BLACK);
                tv_doctNm.setTextColor(Color.BLACK);

            }






        return convertView;
    }

}
