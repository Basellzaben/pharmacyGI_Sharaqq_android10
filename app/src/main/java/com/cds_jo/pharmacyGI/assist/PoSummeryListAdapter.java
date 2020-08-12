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

public class PoSummeryListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ContactListItems> contactList;

    public PoSummeryListAdapter(Context context, ArrayList<ContactListItems> list) {

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
            convertView = inflater.inflate(R.layout.list_row_summery, null);

        }
        Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
        tvSlNo.setText(contactListItems.getno());
        Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(contactListItems.getName());
        Methdes.MyTextView tvPrice = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Price);
        tvPrice.setText(contactListItems.getItemOrgPrice());
        Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Qty);
        tvQTY.setText(contactListItems.getQty());


        Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Unit);
        Unit.setText(contactListItems.getUniteNm());


        Methdes.MyTextView Bounce = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Bounce);
        Bounce.setText( contactListItems.getBounce());


        Methdes.MyTextView Disc = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Disc_Per);
        Disc.setText(contactListItems.getDiscount());






        Methdes.MyTextView Total = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Total);
        Total.setText(contactListItems.getTotal());

        Methdes.MyTextView tv_tax = (Methdes.MyTextView) convertView.findViewById(R.id.tv_tax);
        tv_tax.setText(contactListItems.getTax());


        tvSlNo.setTextColor(Color.BLACK);
        tvName.setTextColor(Color.BLACK);
        tvPrice.setTextColor(Color.BLACK);
        tvQTY.setTextColor(Color.BLACK);
        Unit.setTextColor(Color.BLACK);
        Bounce.setTextColor(Color.BLACK);
        Disc.setTextColor(Color.BLACK);

        Total.setTextColor(Color.BLACK);
        tv_tax.setTextColor(Color.BLACK);

        tvSlNo.setBackgroundColor(Color.WHITE);
        tvName.setBackgroundColor(Color.WHITE);
        tvPrice.setBackgroundColor(Color.WHITE);
        tvQTY.setBackgroundColor(Color.WHITE);
        Unit.setBackgroundColor(Color.WHITE);
        Bounce.setBackgroundColor(Color.WHITE);
        Disc.setBackgroundColor(Color.WHITE);

        Total.setBackgroundColor(Color.WHITE);
        tv_tax.setBackgroundColor(Color.WHITE);



        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);
        if(position%2==0 && position >0 )
        {
            RR.setBackgroundColor(Color.WHITE);
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);

            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);
            Disc.setTextColor(Color.BLACK);

            Total.setTextColor(Color.BLACK);
            tv_tax.setTextColor(Color.BLACK);

        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tvSlNo.setTextColor(Color.BLACK);
            tvName.setTextColor(Color.BLACK);
            tvPrice.setTextColor(Color.BLACK);
            tvQTY.setTextColor(Color.BLACK);

            Unit.setTextColor(Color.BLACK);
            Bounce.setTextColor(Color.BLACK);
            Disc.setTextColor(Color.BLACK);

            Total.setTextColor(Color.BLACK);
            tv_tax.setTextColor(Color.BLACK);


        }

        if(position==0) {


            tvSlNo.setTextColor(Color.WHITE);
            tvName.setTextColor(Color.WHITE);
            tvPrice.setTextColor(Color.WHITE);
            tvQTY.setTextColor(Color.WHITE);
            Unit.setTextColor(Color.WHITE);
            Bounce.setTextColor(Color.WHITE);
            Disc.setTextColor(Color.WHITE);

            Total.setTextColor(Color.WHITE);
            tv_tax.setTextColor(Color.WHITE);

            tvSlNo.setBackgroundColor(Color.BLACK);
            tvName.setBackgroundColor(Color.BLACK);
            tvPrice.setBackgroundColor(Color.BLACK);
            tvQTY.setBackgroundColor(Color.BLACK);
            Unit.setBackgroundColor(Color.BLACK);
            Bounce.setBackgroundColor(Color.BLACK);
            Disc.setBackgroundColor(Color.BLACK);

            Total.setBackgroundColor(Color.BLACK);
            tv_tax.setBackgroundColor(Color.BLACK);

        }

        return convertView;
    }

}
