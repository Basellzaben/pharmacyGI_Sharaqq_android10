package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Tab_UnpostedTrans extends BaseAdapter {

    Context context;
    ArrayList<cls_Tab_Sales> cls_Tab_Sales;

    public cls_Tab_UnpostedTrans(Context context, ArrayList<cls_Tab_Sales> list) {

        this.context = context;
        cls_Tab_Sales = list;
    }

    @Override
    public int getCount() {

        return cls_Tab_Sales.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_Tab_Sales.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        cls_Tab_Sales cls_search_po = cls_Tab_Sales.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tab_unposted_trans, null);

        }

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView tvcustNo = (MyTextView) convertView.findViewById(R.id.tv_no);
        tvcustNo.setText(cls_search_po.getCustNo());

        MyTextView tvCustNm = (MyTextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(cls_search_po.getCustNm());

        MyTextView tvDate = (MyTextView) convertView.findViewById(R.id.tvDate);
        tvDate.setText(cls_search_po.getDate());

        MyTextView tvacc = (MyTextView) convertView.findViewById(R.id.tv_AccNo);
        tvacc.setText(cls_search_po.getAcc());


        MyTextView tv_tot = (MyTextView) convertView.findViewById(R.id.tv_tot);
        tv_tot.setText(cls_search_po.getTot());


        MyTextView tv_invoType = (MyTextView) convertView.findViewById(R.id.tv_invoType);
        tv_invoType.setText(cls_search_po.getType());


        /*TextView tv_notes = (TextView) convertView.findViewById(R.id.tv_notes);
        tv_notes.setText(cls_search_po.getNotes());
*/

        if(position%2==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
        }

        else
        {
            RR.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

}
