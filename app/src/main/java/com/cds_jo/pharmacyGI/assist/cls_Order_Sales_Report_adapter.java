package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.cls_Order_Sales_details;
import com.cds_jo.pharmacyGI.cls_Tab_Order_Sales;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Order_Sales_Report_adapter extends BaseAdapter {

    Context context;
    ArrayList<cls_Order_Sales_details> cls_Tab_Sales;

    public cls_Order_Sales_Report_adapter(Context context, ArrayList<cls_Order_Sales_details> list) {

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
        cls_Order_Sales_details cls_search_po = cls_Tab_Sales.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cls_sales_order_report_row, null);

        }

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        MyTextView tvcustNo = (MyTextView) convertView.findViewById(R.id.tv_no);
        tvcustNo.setText(cls_search_po.getCustNo());

        MyTextView tvCustNm = (MyTextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(cls_search_po.getCustNm());

        MyTextView tvDate = (MyTextView) convertView.findViewById(R.id.tvDate);
        tvDate.setText(cls_search_po.getDate());

        MyTextView tvacc = (MyTextView) convertView.findViewById(R.id.tv_AccNo);
        tvacc.setText(cls_search_po.getOrderNo());


        MyTextView tv_tot = (MyTextView) convertView.findViewById(R.id.tv_tot);
        tv_tot.setText(cls_search_po.getTot());

        MyTextView tv_ABPrint = (MyTextView) convertView.findViewById(R.id.tv_ABPrint);
        tv_ABPrint.setText(cls_search_po.getABPrint());

        MyTextView tv_notes = (MyTextView) convertView.findViewById(R.id.tv_notes);


        MyTextView tv_ABOrder = (MyTextView) convertView.findViewById(R.id.tv_ABOrder);
        tv_ABOrder.setText(cls_search_po.getABOrder());


        MyTextView tv_ABBill = (MyTextView) convertView.findViewById(R.id.tv_ABBill);
        tv_ABBill.setText(cls_search_po.getABill());


           if (cls_search_po.getPosted().equals("-3")) {
               tv_notes.setText("حالة الفاتورة");
           } else if (cls_search_po.getPosted().equals("-1")) {
               tv_notes.setText("غير معتمد");
           } else {
               tv_notes.setText("معتمد");
           }

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

