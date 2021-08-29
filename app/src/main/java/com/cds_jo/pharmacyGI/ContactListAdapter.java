package com.cds_jo.pharmacyGI;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.assist.OrdersItems;

import java.util.ArrayList;

public class ContactListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ContactListItems> contactList;

    public ContactListAdapter(Context context, ArrayList<ContactListItems> list) {

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
try {
    if (position == 0) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.header_contact_list_row, null);

    } else {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.contact_list_row, null);


        TextView tvSlNo = (TextView) convertView.findViewById(R.id.tv_no);
        tvSlNo.setText(contactListItems.getno());
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(contactListItems.getName());
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_Price);
        tvPrice.setText(contactListItems.getItemOrgPrice());
        TextView tvQTY = (TextView) convertView.findViewById(R.id.tv_Qty);
        tvQTY.setText(contactListItems.getQty());
        TextView tvTAX = (TextView) convertView.findViewById(R.id.tv_tax);
        tvTAX.setText(contactListItems.getTax());
       /*     tvTAX.setVisibility(View.INVISIBLE);
            tvTAX.setWidth(0);*/

        TextView Unit = (TextView) convertView.findViewById(R.id.tv_Unit);
        Unit.setText(contactListItems.getUniteNm());


        TextView Bounce = (TextView) convertView.findViewById(R.id.tv_Bounce);
        Bounce.setText(contactListItems.getBounce());


        TextView Disc = (TextView) convertView.findViewById(R.id.tv_Disc_Per);
        Disc.setText(String.valueOf(Double.valueOf(contactListItems.getDiscount()) + Double.valueOf(contactListItems.getDisPerFromHdr()) + Double.valueOf(contactListItems.getPro_dis_Per())));


        TextView Disc_Amt = (TextView) convertView.findViewById(R.id.tv_Disc_Amt);
        Disc_Amt.setText(String.valueOf(Double.valueOf(contactListItems.getDis_Amt()) + Double.valueOf(contactListItems.getPro_amt()) + Double.valueOf(contactListItems.getPro_amt())));


        TextView Total = (TextView) convertView.findViewById(R.id.tv_Total);
        Total.setText(contactListItems.getTotal());


        TextView Tax_Amt = (TextView) convertView.findViewById(R.id.tv_Tax_Amt);
        Tax_Amt.setText(contactListItems.getTax_Amt());
        Tax_Amt.setVisibility(View.INVISIBLE);
        Tax_Amt.setWidth(0);


        ImageButton imageButton14=(ImageButton)convertView.findViewById(R.id.imageButton14);
        imageButton14.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                ((OrdersItems)context).btn_Delete_Item(contactListItems.no,position);
            }
        });
    }
}catch ( Exception ex){}
        return convertView;
    }

}
