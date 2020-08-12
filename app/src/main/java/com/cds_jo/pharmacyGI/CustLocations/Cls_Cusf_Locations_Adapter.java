package com.cds_jo.pharmacyGI.CustLocations;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

/**
 * Created by Hp on 17/03/2016.
 */
public class Cls_Cusf_Locations_Adapter extends BaseAdapter {

    Context context;
    ArrayList<CustLocaltions> cls_Records;


    public Cls_Cusf_Locations_Adapter(Context context, ArrayList<CustLocaltions> list) {

        this.context = context;
        cls_Records = list;
    }@Override
    public int getCount() {

        return cls_Records.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_Records.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        CustLocaltions  obj = cls_Records.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cust_location_row, null);

        }

        Methdes.MyTextView tv_Desc = ( Methdes.MyTextView) convertView.findViewById(R.id.CustNm);
         tv_Desc.setText(obj.getCustNm());


        Methdes.MyTextView tv_Locat = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_Locat);
        tv_Locat.setText(obj.getLocat());


        Methdes.MyTextView tv_Tr_Date = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_Tr_Date);
        tv_Tr_Date.setText(obj.getTr_Date());

        Methdes.MyTextView tv_Ab_Reference = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_Ab_Reference);

        if(obj.getPost().toString().equalsIgnoreCase("-1")){
            tv_Ab_Reference.setText("غير مرحلة");
            tv_Ab_Reference.setTextColor(Color.RED);
        }else{
            tv_Ab_Reference.setText("مرحلة برقم :" + obj.getPost().toString());
            tv_Ab_Reference.setTextColor(Color.GREEN);
        }



        return convertView;
    }

}


