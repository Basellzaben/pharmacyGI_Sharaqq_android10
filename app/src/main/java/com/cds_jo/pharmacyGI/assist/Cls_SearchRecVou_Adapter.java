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


public class Cls_SearchRecVou_Adapter  extends BaseAdapter {
    Context context;
    ArrayList<Cls_SearchRecVou> cls_searchRecVous;

    public Cls_SearchRecVou_Adapter(Context context, ArrayList<Cls_SearchRecVou> list) {

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
        Cls_SearchRecVou cls_searchRecVou = cls_searchRecVous.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_recv_row, null);

        }
        TextView tv_DocNo = (TextView) convertView.findViewById(R.id.tv_DocNo);
        tv_DocNo.setText(cls_searchRecVou.getDocNo());

       TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
        tv_date.setText(cls_searchRecVou.getDate());

       TextView tv_Amt = (TextView) convertView.findViewById(R.id.tv_Amt);
        tv_Amt.setText(cls_searchRecVou.getAmt());

          TextView tv_CustNm = (TextView) convertView.findViewById(R.id.tv_CustNm);
        tv_CustNm.setText(cls_searchRecVou.getAcc());

       TextView tv_notes = (TextView) convertView.findViewById(R.id.tv_notes);
        tv_notes.setText(cls_searchRecVou.getNotes());
        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);
      if(cls_searchRecVou.getPost().equals("-1")){

            tv_CustNm.setTextColor(Color.BLACK);

        }
        else
        {
            tv_CustNm.setTextColor(Color.RED);
        }
        return convertView;


    }

}



