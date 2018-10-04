package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hp on 12/03/2016.
 */
public class Cls_Po_Print_Adapter extends BaseAdapter {
    Context context;
    ArrayList<ContactListItems> contactList;

    public Cls_Po_Print_Adapter(Context context, ArrayList<ContactListItems> list) {

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
            convertView = inflater.inflate(R.layout.po_print_row, null);

        }
        TextView tvSlNo = (TextView) convertView.findViewById(R.id.tv_no);
        tvSlNo.setText(contactListItems.getno());
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(contactListItems.getName());
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_Price);
        tvPrice.setText(contactListItems.getprice());
        TextView tvQTY = (TextView) convertView.findViewById(R.id.tv_Qty);
        tvQTY.setText(contactListItems.getQty());
        TextView tvTAX = (TextView) convertView.findViewById(R.id.tv_tax);
        tvTAX.setText(contactListItems.getTax());

        TextView Unit = (TextView) convertView.findViewById(R.id.tv_Unit);
        Unit.setText(contactListItems.getUniteNm());


      //  TextView Bounce = (TextView) convertView.findViewById(R.id.tv_Bounce);
      //  Bounce.setText(contactListItems.getBounce());


        //TextView Disc = (TextView) convertView.findViewById(R.id.tv_Disc_Per);
       // Disc.setText(contactListItems.getDiscount());



      //  TextView Disc_Amt = (TextView) convertView.findViewById(R.id.tv_Disc_Amt);
      //  Disc_Amt.setText(contactListItems.getDis_Amt());


     //   TextView Total = (TextView) convertView.findViewById(R.id.tv_Total);
     //   Total.setText(contactListItems.getTotal());


    //    TextView Tax_Amt = (TextView) convertView.findViewById(R.id.tv_Tax_Amt);
     //   Tax_Amt.setText(contactListItems.getTax_Amt());


        return convertView;
    }

}
