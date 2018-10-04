package com.cds_jo.pharmacyGI;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class Cls_Sal_Inv_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Sal_InvItems> contactList;

    public Cls_Sal_Inv_Adapter(Context context, ArrayList<Cls_Sal_InvItems> list) {

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
        Cls_Sal_InvItems contactListItems = contactList.get(position);
        if (position == 0) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.header_contact_list_row, null);

        } else {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.sales_list_items_row, null);

            }


            Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
            tvSlNo.setText(contactListItems.getno());
            Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.tv_name);
            tvName.setText(contactListItems.getName());
            Methdes.MyTextView tvPrice = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Price);
            tvPrice.setText(contactListItems.getItemOrgPrice());
            Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Qty);
            tvQTY.setText(contactListItems.getQty());
            Methdes.MyTextView tvTAX = (Methdes.MyTextView) convertView.findViewById(R.id.tv_tax);
            tvTAX.setText(contactListItems.getTax());

            Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Unit);
            Unit.setText(contactListItems.getUniteNm());


            Methdes.MyTextView Bounce = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Bounce);
            Bounce.setText(String.valueOf(Double.valueOf(contactListItems.getBounce()) + Double.valueOf(contactListItems.getPro_bounce())));


            Methdes.MyTextView Disc = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Disc_Per);
            Disc.setText(String.valueOf(Double.valueOf(contactListItems.getDiscount()) + Double.valueOf(contactListItems.getDisPerFromHdr()) + Double.valueOf(contactListItems.getPro_dis_Per())));


            Methdes.MyTextView Disc_Amt = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Disc_Amt);
            Disc_Amt.setText(String.valueOf(Double.valueOf(contactListItems.getDis_Amt()) + Double.valueOf(contactListItems.getPro_amt()) + Double.valueOf(contactListItems.getPro_amt())));


            Methdes.MyTextView Total = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Total);
            Total.setText(contactListItems.getTotal());


            Methdes.MyTextView Tax_Amt = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Tax_Amt);
            Tax_Amt.setText(contactListItems.getTax_Amt());

     }
        return convertView;
    }

}
