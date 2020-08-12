package com.cds_jo.pharmacyGI;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Cls_Cust_Qty_Item_Adapter extends BaseAdapter {

    Context context;
    ArrayList<ContactListItems> contactList;

    public Cls_Cust_Qty_Item_Adapter(Context context, ArrayList<ContactListItems> list) {

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
            convertView = inflater.inflate(R.layout.cust_qty_item_row, null);

        }
        TextView tvSlNo = (TextView) convertView.findViewById(R.id.tv_no);
        tvSlNo.setText(contactListItems.getno());
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(contactListItems.getName());

        TextView tvQTY = (TextView) convertView.findViewById(R.id.tv_Qty);
        tvQTY.setText(contactListItems.getQty());


        TextView Unit = (TextView) convertView.findViewById(R.id.tv_Unit);
        Unit.setText(contactListItems.getUniteNm());




        TextView Exp = (TextView) convertView.findViewById(R.id.tv_ExpDate);
        Exp.setText(contactListItems.getExpDate());




        TextView Batch = (TextView) convertView.findViewById(R.id.tv_Batch);
        Batch.setText(contactListItems.getBatch());

        return convertView;
    }

}
