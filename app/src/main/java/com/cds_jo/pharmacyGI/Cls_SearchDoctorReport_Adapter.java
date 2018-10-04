package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class Cls_SearchDoctorReport_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_DoctorReport> cls_searchRecVous;

    public Cls_SearchDoctorReport_Adapter(Context context, ArrayList<Cls_DoctorReport> list) {

        this.context = context;
        cls_searchRecVous = list;
    }

    @Override
    public int getCount() {

        return cls_searchRecVous.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_searchRecVous.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_DoctorReport cls_searchRecVou = cls_searchRecVous.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_doctor_report, null);

        }
        TextView tv_DocNo = (TextView) convertView.findViewById(R.id.tv_DocNo);
        tv_DocNo.setText(cls_searchRecVou.getNo());

       TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
        tv_date.setText(cls_searchRecVou.getTr_Date());

       TextView tv_Loct = (TextView) convertView.findViewById(R.id.tv_Loct);
        if(cls_searchRecVou.getVType().equals("1")){
            tv_Loct.setText("طبيب");
           }
        else
        {
            tv_Loct.setText("صيدلية");
        }

          TextView tv_CustNm = (TextView) convertView.findViewById(R.id.tv_CustNm);
        tv_CustNm.setText(cls_searchRecVou.getCustName());

       TextView tv_notes = (TextView) convertView.findViewById(R.id.tv_notes);
        tv_notes.setText(cls_searchRecVou.getVNotes());

      if(cls_searchRecVou.getPosted().equals("-1")){
            tv_CustNm.setTextColor(Color.BLACK);
        }
        else
        {
            tv_CustNm.setTextColor(Color.RED);
        }
        return convertView;


    }

}



