package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.Cls_SearchRecVou;
import com.cds_jo.pharmacyGI.assist.Cls_SearchRecVou_Adapter;

import java.util.ArrayList;

public class RecVoucherSearchActivity  extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
   // TextView itemnm;
    ArrayList<Cls_SearchRecVou> cls_search_pos_list;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        form =inflater.inflate(R.layout.activity_rec_voucher_search,container,false);
        getDialog().setTitle(getResources().getText(R.string.Catch_Receipt) );
        FillList("");
        EditText  filter =    (EditText) form.findViewById(R.id.et_Search_filter);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                FillList(s.toString());

            }
        });


       /* itemnm = (TextView)form.findViewById(R.id.ed_item_nm);
        items_Lsit=(ListView) form.findViewById(R.id.listView2);*/


        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_SearchRecVou po_obj = (Cls_SearchRecVou) arg0.getItemAtPosition(position);
                String nm = po_obj.getDocNo();

                if (getArguments().getString("Scr") == "Rec") {
                    ((RecvVoucherActivity) getActivity()).Set_Order(po_obj.getDocNo());

              } else if (getArguments().getString("Scr") == "EditeRec") {
                    ((EditeTransActivity) getActivity()).Set_Order(po_obj.getDocNo());
              }

                 Exist_Pop();

            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }
    private  void FillList(String s){
        cls_search_pos_list  = new ArrayList<Cls_SearchRecVou>();
        cls_search_pos_list.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u =  sharedPreferences.getString("UserID", "");
        String q ="";
        if(s.equalsIgnoreCase("")){
            q = "Select distinct RecVoucher.DocNo,RecVoucher.TrDate,RecVoucher.Amnt,Customers.name,RecVoucher.Desc, COALESCE(RecVoucher.Post, -1)  as Post from RecVoucher left join Customers  on Customers.no =RecVoucher.CustAcc" +
                    " where  RecVoucher.UserID ='"+u.toString()+"' ";
        }else{
            q = "Select distinct RecVoucher.DocNo,RecVoucher.TrDate,RecVoucher.Amnt,Customers.name,RecVoucher.Desc, COALESCE(RecVoucher.Post, -1)  as Post from RecVoucher left join Customers  on Customers.no =RecVoucher.CustAcc" +
                    " where  RecVoucher.UserID ='"+u.toString()+"' And Customers.name like  '%"+s.toString()+"%'";
        }

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_SearchRecVou cls_searchRecVou= new Cls_SearchRecVou();

                    cls_searchRecVou.setDocNo(c1.getString(c1.getColumnIndex("DocNo")));
                    cls_searchRecVou.setDate(c1.getString(c1.getColumnIndex("TrDate")));
                    cls_searchRecVou.setAmt(c1.getString(c1.getColumnIndex("Amnt")));
                    cls_searchRecVou.setAcc(c1.getString(c1.getColumnIndex("name")));
                    cls_searchRecVou.setNotes(c1.getString(c1.getColumnIndex("Desc")));
                    cls_searchRecVou.setPost(c1.getString(c1.getColumnIndex("Post")));

                    cls_search_pos_list.add(cls_searchRecVou);



                }while (c1.moveToNext());
            }
            c1.close();
        }



        Cls_SearchRecVou_Adapter cls_searchRecVou_adapter = new Cls_SearchRecVou_Adapter(
                this.getActivity(),cls_search_pos_list);

        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(cls_searchRecVou_adapter);




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
            Toast.makeText(getActivity(),
                    "Your Message", Toast.LENGTH_SHORT).show();
            //   ((OrdersItems)getActivity()).Save_Method("maen");


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