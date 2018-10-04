package com.cds_jo.pharmacyGI.assist.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.assist.Cls_UpdateData;

import java.util.ArrayList;

import Methdes.MethodToUse;


public class Cls_UpdateData_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_UpdateData> cls_updateDatas;

    public Cls_UpdateData_Adapter(Context context, ArrayList<Cls_UpdateData> list) {

        this.context = context;
        cls_updateDatas = list;
    }

    @Override
    public int getCount() {

        return cls_updateDatas.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_updateDatas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_UpdateData obj = cls_updateDatas.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.update_data_row, null);

        }
        TextView tv_Msg = (TextView) convertView.findViewById(R.id.tv_Msg);
        tv_Msg.setText(obj.getMsg());

        TextView tv_Count = (TextView) convertView.findViewById(R.id.tv_Count);

        tv_Msg.setTypeface(MethodToUse.SetTFace(context));
        tv_Count.setTypeface(MethodToUse.SetTFace(context));


        ImageView img = (ImageView) convertView.findViewById(R.id.img);



        if(obj.getFlag()==0){
            tv_Count.setText("");
            img.setImageResource(R.drawable.x);

        }
        else
        {
            tv_Count.setText(obj.getCount().toString());
            img.setImageResource(R.drawable.ss);

        }



        return convertView;


    }

}



