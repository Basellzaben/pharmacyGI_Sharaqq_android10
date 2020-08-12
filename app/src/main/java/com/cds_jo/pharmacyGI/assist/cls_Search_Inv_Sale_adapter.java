package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Search_Inv_Sale_adapter extends BaseAdapter {

    Context context;
    ArrayList<cls_Search_po> cls_search_pos;

    public cls_Search_Inv_Sale_adapter(Context context, ArrayList<cls_Search_po> list) {

        this.context = context;
        cls_search_pos = list;
    }

    @Override
    public int getCount() {

        return cls_search_pos.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_search_pos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        cls_Search_po cls_search_po = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sale_inv_row_search, null);

        }
        TextView tvcustNo = (TextView) convertView.findViewById(R.id.tv_no);
        tvcustNo.setText(cls_search_po.getCustNo());

        TextView tvCustNm = (TextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(cls_search_po.getCustNm());

        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_notes);
        tvDate.setText(cls_search_po.getDate());

        TextView tvacc = (TextView) convertView.findViewById(R.id.tv_AccNo);
        tvacc.setText(cls_search_po.getAcc());


        TextView tv_tot = (TextView) convertView.findViewById(R.id.tv_tot);
        tv_tot.setText(cls_search_po.getTot());


        TextView tv_invoType = (TextView) convertView.findViewById(R.id.tv_invoType);

       if(cls_search_po.getType().equals("0")) {
               tv_invoType.setText("نقدي");
       }
       else {

               tv_invoType.setText("ذمم");

       }


        return convertView;
    }

}

