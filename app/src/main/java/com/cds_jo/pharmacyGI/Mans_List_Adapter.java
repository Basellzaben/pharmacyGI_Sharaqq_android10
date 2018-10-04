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
public class Mans_List_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Mans> customerses;

    public Mans_List_Adapter(Context context, ArrayList<Mans> list) {

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

        Mans customersesobj = customerses.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.man_search, null);

        }

        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);
        TextView tvNm = (TextView) convertView.findViewById(R.id.tv_nm);
        tvNm.setText(customersesobj.getNm());

//        TextView tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
//        tvAddress.setText(customersesobj.getAddress());
//
//        TextView tvMobile = (TextView) convertView.findViewById(R.id.tv_mobile);
//        tvMobile.setText(customersesobj.getMobile());

//        TextView tvAcc = (TextView) convertView.findViewById(R.id.tv_acc);
//        tvAcc.setText(customersesobj.getAcc());


        TextView tvNo = (TextView) convertView.findViewById(R.id.tv_no);
        tvNo.setText(customersesobj.getNo());

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
