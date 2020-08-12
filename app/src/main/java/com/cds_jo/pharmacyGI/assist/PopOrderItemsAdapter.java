package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.pharmacyGI.ContactListItems;
import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

public class PopOrderItemsAdapter extends BaseAdapter {

    Context context;
    ArrayList<ContactListItems> contactList;

    public PopOrderItemsAdapter(Context context, ArrayList<ContactListItems> list) {

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
        ContactListItems contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rwo_item_detials, null);

        }
        Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
        tvSlNo.setText(contactListItems.getno());
        Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(contactListItems.getName());

        Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Qty);
        tvQTY.setText(contactListItems.getQty());


        Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Unit);
        Unit.setText(contactListItems.getUniteNm());


        Methdes.MyTextView Bounce = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Bounce);
        Bounce.setText( contactListItems.getBounce());








        Methdes.MyTextView Total = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Total);
        Total.setText(contactListItems.getTotal());




        tvSlNo.setTextColor(Color.BLACK);
        tvName.setTextColor(Color.BLACK);

        tvQTY.setTextColor(Color.BLACK);
        Unit.setTextColor(Color.BLACK);
        Bounce.setTextColor(Color.BLACK);


        Total.setTextColor(Color.BLACK);


        tvSlNo.setBackgroundColor(Color.WHITE);
        tvName.setBackgroundColor(Color.WHITE);

        tvQTY.setBackgroundColor(Color.WHITE);
        Unit.setBackgroundColor(Color.WHITE);
        Bounce.setBackgroundColor(Color.WHITE);


        Total.setBackgroundColor(Color.WHITE);




        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        LinearLayout RR2=(LinearLayout)convertView.findViewById(R.id.RR2);
        if(position%2==0  )
        {
            RR.setBackgroundColor(Color.WHITE);
            RR2.setBackgroundColor(Color.WHITE);
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);

            tvQTY.setTextColor(Color.BLACK);

            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);


            Total.setTextColor(Color.BLACK);

        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray4));
            RR2.setBackgroundColor(context.getResources().getColor(R.color.Gray4));
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);

            tvQTY.setTextColor(Color.BLACK);

            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);

            Total.setTextColor(Color.BLACK);


        }


        return convertView;
    }

}
