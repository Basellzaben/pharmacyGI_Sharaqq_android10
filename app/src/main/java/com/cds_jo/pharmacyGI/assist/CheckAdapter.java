package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;

/**
 * Created by Hp on 08/03/2016.
 */
public class CheckAdapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Check> cls_checks;


    public CheckAdapter(Context context, ArrayList<Cls_Check> list) {

        this.context = context;
        cls_checks = list;
    }

    @Override
    public int getCount() {

        return cls_checks.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_checks.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Check  cls_check = cls_checks.get(position);

        if(position==0)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.headeer_check_list_row, null);

        }
        else {


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.check_list_row, null);

            }

            Methdes.MyTextView ser = (Methdes.MyTextView) convertView.findViewById(R.id.ser);
            ser.setText(cls_check.getSer().toString());

            Methdes.MyTextView tv_Checkno = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Checkno);
            tv_Checkno.setText(cls_check.getCheckNo());

            Methdes.MyTextView cur_no = (Methdes.MyTextView) convertView.findViewById(R.id.tv_CheckDate);
            cur_no.setText(cls_check.getCheckDate());

            Methdes.MyTextView BankNm = (Methdes.MyTextView) convertView.findViewById(R.id.tv_BankNm);
            BankNm.setText(cls_check.getBankName());


            Methdes.MyTextView tvdes = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Amnt);
            tvdes.setText(cls_check.getAmnt());


       /* TextView tvbb = (TextView) convertView.findViewById(R.id.tv_bb);
        tvbb.setText(cls_check.getUserID());


        TextView tvdept = (TextView) convertView.findViewById(R.id.tv_dept);
        tvdept.setText(cls_check.getPost());
*/
        }
        return convertView;
    }

}
