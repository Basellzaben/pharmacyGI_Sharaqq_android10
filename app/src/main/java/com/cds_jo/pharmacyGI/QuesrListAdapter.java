package com.cds_jo.pharmacyGI;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class QuesrListAdapter extends BaseAdapter {
    Cls_Quest contactListItems;
    Context context;
    Cls_Quest[] questionsList;
    ArrayList<Cls_Quest> contactList;
    public static ArrayList<String> ans;
    public QuesrListAdapter(Context context, ArrayList<Cls_Quest> list) {

        this.context = context;
        contactList = list;

    }

    @Override
    public int getCount() {

        return contactList.size();
    }

    @Override
    public Object getItem(int position) {

        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final  int position, View convertView, ViewGroup arg2) {
          contactListItems = contactList.get(position);
/*
        if(position==0)
       {
              LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.questrowheader, null);

        }
        else {*/

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.quest_list_row, null);

        RadioButton r1 = (RadioButton) convertView.findViewById(R.id.r1);
        RadioButton r2 = (RadioButton) convertView.findViewById(R.id.r2);
        RadioButton r3 = (RadioButton) convertView.findViewById(R.id.r3);





      /*  r1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           if (isChecked)
              ans.set(position, "1");
            }
        });

        r2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ans.set(position, "2");
            }
        });

        r3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ans.set(position, "3");
            }
        });*/

            TextView tvSlNo = (TextView) convertView.findViewById(R.id.tv_no);
            tvSlNo.setText(contactListItems.getQuesNo());
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvName.setText(contactListItems.getQuestxt());



          if(contactListItems.getAns1().equalsIgnoreCase("1") ){
              r1.setChecked(true);
          }else if ( contactListItems.getAns1().equalsIgnoreCase("2")){
              r2.setChecked(true);
          }else if ( contactListItems.getAns1().equalsIgnoreCase("3")){
              r3.setChecked(true);
          }
       // }
        return convertView;
    }

}
