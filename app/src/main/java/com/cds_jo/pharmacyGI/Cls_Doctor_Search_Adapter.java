package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.cds_jo.pharmacyGI.R.id.RR1;

/**
 * Created by Hp on 18/03/2016.
 */
public class Cls_Doctor_Search_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Doctor> doctors;

    public Cls_Doctor_Search_Adapter(Context context, ArrayList<Doctor> list) {
        this.context = context;
        doctors = list;
    }

    @Override
    public int getCount() {

        return doctors.size();
    }

    @Override
    public Object getItem(int position) {

        return doctors.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Doctor cls_item_search = doctors.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.doctor_search_row, null);

        }
        Methdes.MyTextView name = (Methdes.MyTextView) convertView.findViewById(R.id.tv_name);
        name.setText(cls_item_search.getName());

        Methdes.MyTextView no = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
        no.setText(cls_item_search.getNo());


        Methdes.MyTextView tv_Spec = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Spec);
        tv_Spec.setText(cls_item_search.getSpec());


        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);
        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));

        }


        return convertView;
    }

}


