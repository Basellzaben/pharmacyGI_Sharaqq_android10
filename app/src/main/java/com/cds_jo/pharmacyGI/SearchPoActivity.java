package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.cls_Search_po;
import com.cds_jo.pharmacyGI.assist.cls_Search_po_adapter;

import java.util.ArrayList;

public class SearchPoActivity extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
  //  TextView itemnm;
    EditText et_Search ;
  ArrayList<cls_Search_po> cls_search_pos_list;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form =inflater.inflate(R.layout.activity_search_po,container,false);
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        cls_search_pos_list  = new ArrayList<cls_Search_po>();
        et_Search  =(EditText) form.findViewById(R.id.et_Search);;
        fillList();
        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
               cls_Search_po po_obj = (cls_Search_po) arg0.getItemAtPosition(position);

                if (getArguments().getString("Scr") == "EditeRec"){
                    ((EditeTransActivity) getActivity()).Set_Order3( po_obj.getCustNo());

            } else if (getArguments().getString("Scr") == "po")  {
                ((OrdersItems) getActivity()).Set_Order(po_obj.getCustNo(), po_obj.getCustNm(), po_obj.getAcc());
            }
            Exist_Pop();


            }


        });


        et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fillList();
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        items_Lsit.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        return  form;
    }

    private  void fillList(){
        cls_search_pos_list.clear();
        String q ="";
        if(et_Search.getText().toString().equalsIgnoreCase("")){
            q = "Select distinct po.orderno,po.date    ,IFNULL( c.name ,'') as name , po.acc from Po_Hdr po Left join Customers c on c.no = po.acc ";
        }else{
            q = "Select distinct po.orderno,po.date    ,IFNULL( c.name ,'') as name , po.acc from Po_Hdr po Left join Customers c on c.no = po.acc  " +
                    "   Where c.name like '%"+et_Search.getText().toString() +"%'  or po.orderno='"+et_Search.getText().toString()+"'";
        }

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_Search_po cls_searchpos= new cls_Search_po();

                    cls_searchpos.setCustNo(c1.getString(c1.getColumnIndex("orderno")));
                    cls_searchpos.setCustNm(c1.getString(c1.getColumnIndex("name")));
                    cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                    cls_searchpos.setAcc(c1.getString(c1.getColumnIndex("acc")));
                    cls_searchpos.setNotes("Notes");
                    cls_search_pos_list.add(cls_searchpos);
                }while (c1.moveToNext());
            }
            c1.close();
        }
        cls_Search_po_adapter cls_search_po_adapter = new cls_Search_po_adapter(
                this.getActivity(),cls_search_pos_list);
        items_Lsit.setAdapter(cls_search_po_adapter);

    }

    public void Exist_Pop ()
    {
        this.dismiss();
    }
    @Override
    public void onClick(View v) {
        Button bu = (Button) v ;
        if (bu.getText().toString().equals("Cancel")){
            this.dismiss();
        }
        else  if (bu.getText().toString().equals("Add")){


        }


    }


    public void onListItemClick(ListView l, View v, int position, long id) {


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

    }
/*@Override
    public  void OnClick(View view  ){

}*/
}
