package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.Cls_Man_Balanc;
import com.cds_jo.pharmacyGI.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Cls_Man_Qty_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Man_Balanc> clsTransQties;

    public Cls_Man_Qty_Adapter(Context context, ArrayList<Cls_Man_Balanc> list) {

        this.context = context;
        clsTransQties = list;
    }

    @Override
    public int getCount() {

        return clsTransQties.size();
    }

    @Override
    public Object getItem(int position) {

        return clsTransQties.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final Cls_Man_Balanc cls_trans_qty = clsTransQties.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.man_balanc_qty_row, null);

        }
        DecimalFormat precision = new DecimalFormat("0.00");
        TextView Item_No = (TextView) convertView.findViewById(R.id.tv_Item_No);
        Item_No.setText(cls_trans_qty.getItemno());

        TextView ItemNm = (TextView) convertView.findViewById(R.id.tv_ItemNm);
        ItemNm.setText(cls_trans_qty.getItem_Name());

        TextView UnitName = (TextView) convertView.findViewById(R.id.tv_UnitName);
        UnitName.setText(cls_trans_qty.getUnitName());


        TextView Qty = (TextView) convertView.findViewById(R.id.tv_Qty);
        Qty.setText(cls_trans_qty.getQty());

        TextView QtyAcc = (TextView) convertView.findViewById(R.id.tv_QtyAcc);
         QtyAcc.setText(cls_trans_qty.getQtyAcc());


        TextView QtySaled = (TextView) convertView.findViewById(R.id.tv_qtysaled);
        QtySaled.setText(cls_trans_qty.getQtySaled());


        TextView tv_ac_qty = (TextView) convertView.findViewById(R.id.tv_ac_qty);
        tv_ac_qty.setText(cls_trans_qty.getAct_Aty());


        TextView tv_diff = (TextView) convertView.findViewById(R.id.tv_diff);
        tv_diff.setText(cls_trans_qty.getDiff());


        if(cls_trans_qty.getAct_Aty() == cls_trans_qty.getQty()){

            // convertView.setBackgroundColor(Color.GREEN);
        }
       /* else
        {
            tv_CustNm.setTextColor(Color.RED);
        }*/

        return convertView;


    }

}



