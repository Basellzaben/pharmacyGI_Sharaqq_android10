package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

import static com.cds_jo.pharmacyGI.R.id.RR1;

/**
 * Created by Hp on 16/03/2016.
 */
public class Po_Fill_Item_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Invf> cls_invfs;


    public Po_Fill_Item_Adapter(Context context, ArrayList<Cls_Invf> list) {

        this.context = context;
        cls_invfs = list;
    }
    @Override
    public int getCount() {

        return cls_invfs.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_invfs.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Invf cls_invf = cls_invfs.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.invf_row, null);

        }
        TextView tv_itemno = (TextView) convertView.findViewById(R.id.tv_itemno);
        tv_itemno.setText(cls_invf.getItem_No());


        TextView tv_itemname = (TextView) convertView.findViewById(R.id.tv_itemname);
        tv_itemname.setText(cls_invf.getItem_Name());


        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);
        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));

        }
        TextView tv_Qty = (TextView) convertView.findViewById(R.id.tv_Qty);


    /*     TextView cur_no = (TextView) convertView.findViewById(R.id.tv_cur_no);
       cur_no.setText(cls_invf.getCur_no());

        TextView tvdate = (TextView) convertView.findViewById(R.id.tv_date);
        tvdate.setText(cls_invf.getDate());


        TextView tvdes = (TextView) convertView.findViewById(R.id.tv_des);
        tvdes.setText(cls_invf.getDes());


        TextView tvbb = (TextView) convertView.findViewById(R.id.tv_bb);
        tvbb.setText(cls_invf.getBb());


        TextView tvdept = (TextView) convertView.findViewById(R.id.tv_dept);
        tvdept.setText(cls_invf.getDept());

        TextView tvcred = (TextView) convertView.findViewById(R.id.tv_cred);
        tvcred.setText(cls_invf.getCred());


        TextView tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
        tv_rate.setText(cls_invf.getRate());


        TextView tv_tot = (TextView) convertView.findViewById(R.id.tv_tot);
        tv_tot.setText(cls_invf.getTot());

*/



        return convertView;
    }

}

