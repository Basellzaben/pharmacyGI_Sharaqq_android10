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
 * Created by Hp on 17/03/2016.
 */
public class Cls_Deptf_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Deptf> cls_deptfs;


    public Cls_Deptf_adapter(Context context, ArrayList<Cls_Deptf> list) {

        this.context = context;
        cls_deptfs = list;
    }@Override
    public int getCount() {

        return cls_deptfs.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_deptfs.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Deptf  cls_deptf = cls_deptfs.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.deptf_row, null);

        }
        MyTextView tv_itemno = (MyTextView) convertView.findViewById(R.id.tv_no );
        tv_itemno.setText(cls_deptf.getType_No());

        MyTextView tv_itemname = (MyTextView) convertView.findViewById(R.id.tv_Desc);
        tv_itemname.setText(cls_deptf.getType_Name());

        return convertView;
    }

}


