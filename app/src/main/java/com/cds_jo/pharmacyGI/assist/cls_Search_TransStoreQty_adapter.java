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
public class cls_Search_TransStoreQty_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Trans_Store_Qty_Search> cls_search_pos;

    public cls_Search_TransStoreQty_adapter(Context context, ArrayList<Cls_Trans_Store_Qty_Search> list) {

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
        Cls_Trans_Store_Qty_Search cls_search_po = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transfer_store_qty, null);

        }
        MyTextView tv_OrderNo = (MyTextView) convertView.findViewById(R.id.tv_OrderNo);
        tv_OrderNo.setText(" رقم المستند"+" : "+cls_search_po.getOrderNo());

        MyTextView tv_From = (MyTextView) convertView.findViewById(R.id.tv_From);
        tv_From.setText(" من مستودع"+ ": " +cls_search_po.getFrom());

        MyTextView tvDate = (MyTextView) convertView.findViewById(R.id.vDate);
        tvDate.setText("تاريخ المستند" + ":" + cls_search_po.getTr_Date());

        MyTextView tv_To = (MyTextView) convertView.findViewById(R.id.tv_To);
        tv_To.setText("إلى مستودع"  +" : "+cls_search_po.getTo());

        return convertView;
    }

}

