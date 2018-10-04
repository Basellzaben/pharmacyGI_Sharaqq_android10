package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

/**
 * Created by Hp on 18/03/2016.
 */
public class Cls_Cur_Adapter  extends BaseAdapter{
    Context context;
    ArrayList<Cls_Cur> cls_curs;


    public Cls_Cur_Adapter(Context context, ArrayList<Cls_Cur> list) {

        this.context = context;
        cls_curs = list;
    }@Override
     public int getCount() {

        return cls_curs.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_curs.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    public long getPostion(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Cur cls_cur = cls_curs.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_lokup_row, null);

        }
        TextView tv_Name = (TextView) convertView.findViewById(R.id.tv_Name);
        tv_Name.setText(cls_cur.getName());

     /*   TextView tv_No = (TextView) convertView.findViewById(R.id.tv_No);
        tv_No.setText(cls_cur.getNo());*/

        return convertView;
    }

}



