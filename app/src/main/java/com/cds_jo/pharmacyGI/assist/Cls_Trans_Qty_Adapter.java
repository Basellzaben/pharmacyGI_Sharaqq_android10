package com.cds_jo.pharmacyGI.assist;

        import android.content.Context;
        import android.graphics.Color;
        import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

import Methdes.MyTextView;


public class Cls_Trans_Qty_Adapter  extends BaseAdapter {
    Context context;
    ArrayList<Cls_Trans_Qty> clsTransQties;

    public Cls_Trans_Qty_Adapter(Context context, ArrayList<Cls_Trans_Qty> list) {

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
        Cls_Trans_Qty cls_trans_qty = clsTransQties.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trans_qty_row, null);

        }

        // MyTextView tv_DocNo = (MyTextView) convertView.findViewById(R.id.tv_DocNo);
        //tv_DocNo.setText(cls_trans_qty.getDocno());

        MyTextView Item_No = (MyTextView) convertView.findViewById(R.id.tv_Item_No);
        Item_No.setText(cls_trans_qty.getItemno());

        MyTextView ItemNm = (MyTextView) convertView.findViewById(R.id.tv_ItemNm);
        ItemNm.setText(cls_trans_qty.getItem_Name());

        MyTextView UnitName = (MyTextView) convertView.findViewById(R.id.tv_UnitName);
        UnitName.setText(cls_trans_qty.getUnitNo());

        MyTextView StroeNo = (MyTextView) convertView.findViewById(R.id.tv_StroeNo);
        StroeNo.setText(cls_trans_qty.getStoreName());

        MyTextView Qty = (MyTextView) convertView.findViewById(R.id.tv_Qty);
        Qty.setText(cls_trans_qty.getQty());


        if (position == 0)
        {
            Item_No.setBackgroundColor(convertView.getResources().getColor(R.color.Black11));
            Item_No.setTextColor(Color.WHITE);


            ItemNm.setBackgroundColor(convertView.getResources().getColor(R.color.Black11));
            ItemNm.setTextColor(Color.WHITE);


            UnitName.setBackgroundColor(convertView.getResources().getColor(R.color.Black11));
            UnitName.setTextColor(Color.WHITE);

            StroeNo.setBackgroundColor(convertView.getResources().getColor(R.color.Black11));
            StroeNo.setTextColor(Color.WHITE);


            Qty.setBackgroundColor(convertView.getResources().getColor(R.color.Black11));
            Qty.setTextColor(Color.WHITE);

    }

        return convertView;


    }

}



