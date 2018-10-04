package com.cds_jo.pharmacyGI;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.pharmacyGI.assist.Cls_TransQtyItems;

import java.util.ArrayList;

public class TransIteQtyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_TransQtyItems> contactList;

    public TransIteQtyAdapter(Context context, ArrayList<Cls_TransQtyItems> list) {

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
        Cls_TransQtyItems contactListItems = contactList.get(position);
//
//        if(position==0)
//        {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.header_contact_list_row, null);
//
//        }
//        else {
//

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.contact_list_row, null);

            }
            Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
            tvSlNo.setText(contactListItems.getItemNo());
            Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.tv_name);
            tvName.setText(contactListItems.getItemNm());

            Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Qty);
            tvQTY.setText(contactListItems.getQty());


            Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Unit);
            Unit.setText(contactListItems.getUnitNm());



//        }
        return convertView;
    }

}
