package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Search_Pepeare_Qty_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Search_Prepear_Qty_Order> cls_search_pos;

    public cls_Search_Pepeare_Qty_adapter(Context context, ArrayList<Cls_Search_Prepear_Qty_Order> list) {

        this.context = context;
        cls_search_pos = list;
    }

    @Override
    public int getCount() {

        return cls_search_pos.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_search_pos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Search_Prepear_Qty_Order cls_search_po = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.prepear_qty_row_search, null);

        }
        MyTextView tv_OrderNo = (MyTextView) convertView.findViewById(R.id.tv_OrderNo);
        tv_OrderNo.setText(cls_search_po.getOrderNo());

        MyTextView tv_Date = (MyTextView) convertView.findViewById(R.id.tv_Date);
        tv_Date.setText(cls_search_po.getDate());





        return convertView;
    }

}

