package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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

import com.cds_jo.pharmacyGI.assist.cls_SearchCust_Qty_adapter;
import com.cds_jo.pharmacyGI.assist.cls_Search_po;

import java.util.ArrayList;

public class SearchCustStoreQtyActivity extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    EditText Search;
  //  TextView itemnm;
  @Override
  public void onStart() {
      super.onStart();

      // safety check
      if (getDialog() == null)
          return;

      int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
      int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

      getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

  }
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form =inflater.inflate(R.layout.activity_search_po,container,false);
        getDialog().setTitle("Galaxy International Group");

         items_Lsit=(ListView) form.findViewById(R.id.listView2);

      Search=(EditText) form.findViewById(R.id.et_Search);
        ShowData();

        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Toast.makeText(getActivity(),s.toString(),Toast.LENGTH_SHORT).show();

                ShowData();
            }
        });



        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                /*arg1.setBackgroundColor(Color.GREEN);
                Object o = items_Lsit.getItemAtPosition(position);
                String str=(String)o;//As you are using Default String Adapter*/
                cls_Search_po po_obj = (cls_Search_po) arg0.getItemAtPosition(position);
                String nm = po_obj.getCustNo();

                //  Toast.makeText(getActivity(), nm, Toast.LENGTH_LONG).show();

                ((CustomerQty) getActivity()).Set_Order(po_obj.getCustNo(), po_obj.getCustNm(), po_obj.getAcc());
                Exist_Pop();

            }


        });

        return  form;
    }
private  void ShowData(){
    ArrayList<cls_Search_po> cls_search_pos_list  = new ArrayList<cls_Search_po>();
    cls_search_pos_list.clear();

    String q="";
    if (Search.getText().toString().equals("")){

          q = "Select po.orderno,po.date, c.name  , po.acc from CustStoreQtyhdr po Left join Customers c on c.no = po.acc ";
    }
    else {

          q = "Select po.orderno,po.date, c.name  , po.acc from CustStoreQtyhdr po Left join Customers c on c.no = po.acc" +
                  "  WHERE   po.orderno like  '%"+Search.getText().toString()+"%'" ;

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



    cls_SearchCust_Qty_adapter cls_Search_po_adapter_obj = new cls_SearchCust_Qty_adapter(
            this.getActivity(),cls_search_pos_list);
    items_Lsit.setAdapter(cls_Search_po_adapter_obj);

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
            /*Toast.makeText(getActivity(),
                    "Your Message", Toast.LENGTH_SHORT).show();*/
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
