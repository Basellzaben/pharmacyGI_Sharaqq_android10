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
 * Created by Hp on 16/03/2016.
 */
public class Cls_UnitItems_Adapter  extends BaseAdapter {

    Context context;
    ArrayList<Cls_UnitItems> cls_unitItemses;

    public Cls_UnitItems_Adapter(Context context, ArrayList<Cls_UnitItems> list) {

        this.context = context;
        cls_unitItemses = list;
    }

    @Override
    public int getCount() {

        return cls_unitItemses.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_unitItemses.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_UnitItems  cls_unitItems = cls_unitItemses.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.unititemrow, null);

        }
        /*TextView tv_no = (TextView) convertView.findViewById(R.id.tv_no);
        tv_no.setText(cls_unitItems.getUnitno());
*/
        MyTextView tv_name = (MyTextView) convertView.findViewById(R.id.tv_name);
        tv_name.setText(cls_unitItems.getUnitDesc());

        return convertView;
    }

}

