package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.Items;
import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

/**
 * Created by Hp on 06/03/2016.
 */
public class Cls_Main_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Items> cls_acc_reports;


    public Cls_Main_Adapter(Context context, ArrayList<Items> list) {

        this.context = context;
        cls_acc_reports = list;
    }

    @Override
    public int getCount() {

        return cls_acc_reports.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_acc_reports.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Items  cls_acc_report = cls_acc_reports.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridimage, null);

        }


        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_text);
        txtTitle.setText(cls_acc_report.getTitle());

        ImageView imageItem = (ImageView) convertView.findViewById(R.id.item_image);

          imageItem.setImageBitmap(cls_acc_report.getImage());

        return convertView;
    }

}

