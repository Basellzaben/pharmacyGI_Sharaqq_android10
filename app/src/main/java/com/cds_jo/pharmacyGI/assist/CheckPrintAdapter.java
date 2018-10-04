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
 * Created by Hp on 08/03/2016.
 */
public class CheckPrintAdapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Check> cls_checks;


    public CheckPrintAdapter(Context context, ArrayList<Cls_Check> list) {

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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.check_print_list_row, null);

        }

        TextView ser = (TextView) convertView.findViewById(R.id.ser);
        ser.setText(cls_check.getSer().toString());

        TextView tv_Checkno = (TextView) convertView.findViewById(R.id.tv_Checkno);
        tv_Checkno.setText(cls_check.getCheckNo());

        TextView cur_no = (TextView) convertView.findViewById(R.id.tv_CheckDate);
        cur_no.setText(cls_check.getCheckDate());

        TextView BankNm = (TextView) convertView.findViewById(R.id.tv_BankNm);
        BankNm.setText(cls_check.getBankName());


        TextView tvdes = (TextView) convertView.findViewById(R.id.tv_Amnt);
        tvdes.setText(cls_check.getAmnt());


       /* TextView tvbb = (TextView) convertView.findViewById(R.id.tv_bb);
        tvbb.setText(cls_check.getUserID());


        TextView tvdept = (TextView) convertView.findViewById(R.id.tv_dept);
        tvdept.setText(cls_check.getPost());
*/

        return convertView;
    }

}
