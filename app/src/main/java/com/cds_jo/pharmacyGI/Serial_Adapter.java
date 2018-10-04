package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.assist.Customers;

import java.util.ArrayList;

import static com.cds_jo.pharmacyGI.R.id.RR1;

/**
 * Created by Hp on 03/03/2016.
 */
public class Serial_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Serial> customerses;

    public Serial_Adapter(Context context, ArrayList<Cls_Serial> list) {

        this.context = context;
        customerses = list;
    }

    @Override
    public int getCount() {

        return customerses.size();
    }

    @Override
    public Object getItem(int position) {

        return customerses.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        Cls_Serial customersesobj = customerses.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.serial_search, null);

        }

        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);
        TextView tvNm = (TextView) convertView.findViewById(R.id.tv_nm);
        tvNm.setText(customersesobj.getSname());



        TextView tvNo = (TextView) convertView.findViewById(R.id.tv_no);
        tvNo.setText(customersesobj.getSNo());

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
