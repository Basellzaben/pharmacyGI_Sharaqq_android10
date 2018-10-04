package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 18/03/2016.
 */
public class Cls_ItemCost_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_ItemCost> cls_itemCosts;

    public Cls_ItemCost_Adapter(Context context, ArrayList<Cls_ItemCost> list) {

        this.context = context;
        cls_itemCosts = list;
    }

    @Override
    public int getCount() {

        return cls_itemCosts.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_itemCosts.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_ItemCost cls_itemCost = cls_itemCosts.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemcostrow, null);

        }
        MyTextView bill_no = (MyTextView) convertView.findViewById(R.id.tv_bill_no);
        bill_no.setText(cls_itemCost.getBill_no());

        MyTextView bill_date = (MyTextView) convertView.findViewById(R.id.tv_bill_date);
        bill_date.setText(cls_itemCost.getBill_date());

        MyTextView venname = (MyTextView) convertView.findViewById(R.id.tv_venname);
        venname.setText(cls_itemCost.getVenname());

        MyTextView qty = (MyTextView) convertView.findViewById(R.id.tv_qty);
        qty.setText(cls_itemCost.getQty());

        MyTextView cost = (MyTextView) convertView.findViewById(R.id.tv_cost);
        cost.setText(cls_itemCost.getCost());

        MyTextView UnitName = (MyTextView) convertView.findViewById(R.id.tv_UnitName);
        UnitName.setText(cls_itemCost.getUnitName());


        MyTextView unitcost = (MyTextView) convertView.findViewById(R.id.tv_unitcost);
        unitcost.setText(cls_itemCost.getUnitcost());

        if(position==0)
        {
             bill_no.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             bill_no.setTextColor(Color.WHITE);

             bill_date.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             bill_date .setTextColor(Color.WHITE);

             venname.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             venname .setTextColor(Color.WHITE);

             qty .setBackgroundColor(context.getResources().getColor(R.color.Black11));
             qty .setTextColor(Color.WHITE);

             cost.setBackgroundColor(context.getResources().getColor(R.color.Black11));
             cost .setTextColor(Color.WHITE);

             UnitName .setBackgroundColor(context.getResources().getColor(R.color.Black11));
             UnitName .setTextColor(Color.WHITE);


             unitcost .setBackgroundColor(context.getResources().getColor(R.color.Black11));
             unitcost .setTextColor(Color.WHITE);
        }
        else
        {
            bill_no.setBackgroundColor(Color.WHITE);
            bill_no.setTextColor(Color.BLACK);

            bill_date.setBackgroundColor(Color.WHITE);
            bill_date .setTextColor(Color.BLACK);

            venname.setBackgroundColor(Color.WHITE);
            venname .setTextColor(Color.BLACK);

            qty .setBackgroundColor(Color.WHITE);
            qty .setTextColor(Color.BLACK);

            cost.setBackgroundColor(Color.WHITE);
            cost .setTextColor(Color.BLACK);

            UnitName .setBackgroundColor(Color.WHITE);
            UnitName .setTextColor(Color.BLACK);

            unitcost .setBackgroundColor(Color.WHITE);
            unitcost .setTextColor(Color.BLACK);
        }
        return convertView;
    }

}

