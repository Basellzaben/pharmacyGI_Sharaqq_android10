package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

import static com.cds_jo.pharmacyGI.R.id.RR1;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Search_Sample_Item_adapter extends BaseAdapter {

    Context context;
    ArrayList<cls_Search_po> cls_search_pos;

    public cls_Search_Sample_Item_adapter(Context context, ArrayList<cls_Search_po> list) {

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
            convertView = inflater.inflate(R.layout.sample_item_row_search, null);

        }


        TextView tvCustNm = (TextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(cls_search_po.getCustNm());

        TextView tv_notes = (TextView) convertView.findViewById(R.id.tv_notes);
        tv_notes.setText(cls_search_po.getNotes());


        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        tvDate.setText(cls_search_po.getDate());


        TextView tvacc = (TextView) convertView.findViewById(R.id.tv_AccNo);
        tvacc.setText(cls_search_po.getAcc());


        TextView tv_tot = (TextView) convertView.findViewById(R.id.tv_tot);
        tv_tot.setText(cls_search_po.getTot());

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

