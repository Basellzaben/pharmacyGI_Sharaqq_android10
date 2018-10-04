package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cds_jo.pharmacyGI.assist.Cls_Search_Prepear_Qty_Order;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;
import com.cds_jo.pharmacyGI.assist.cls_Search_Pepeare_Qty_adapter;

import java.util.ArrayList;

public class SearchManBalanceQty extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
  //  TextView itemnm;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form =inflater.inflate(R.layout.activity_search_preaperqt,container,false);


         items_Lsit=(ListView) form.findViewById(R.id.listView2);
        ArrayList<Cls_Search_Prepear_Qty_Order> cls_search_pos_list  = new ArrayList<Cls_Search_Prepear_Qty_Order>();
        cls_search_pos_list.clear();
        String q = "Select distinct OrderNo ,date from BalanceQty";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Search_Prepear_Qty_Order cls_searchpos= new Cls_Search_Prepear_Qty_Order();
                    cls_searchpos.setOrderNo(c1.getString(c1.getColumnIndex("OrderNo")));
                    cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                    cls_search_pos_list.add(cls_searchpos);
                }while (c1.moveToNext());
            }
            c1.close();
        }



        cls_Search_Pepeare_Qty_adapter cls_Search_po_adapter_obj = new cls_Search_Pepeare_Qty_adapter(
                 this.getActivity(),cls_search_pos_list);


       items_Lsit.setAdapter(cls_Search_po_adapter_obj);






        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Cls_Search_Prepear_Qty_Order po_obj = (Cls_Search_Prepear_Qty_Order) arg0.getItemAtPosition(position);
                String nm = po_obj.getOrderNo();

                if (getArguments().getString("Scr") == "ManBalance") {
                    ((ManBalanceQtyActivity) getActivity()).Set_Order(po_obj.getOrderNo(), po_obj.getDate());

                } else if (getArguments().getString("Scr") == "SalInvoice") {
                    ((Sale_InvoiceActivity) getActivity()).InsertBalanceQty(po_obj.getOrderNo(), po_obj.getDate());
                }
                Exist_Pop();

            }


        });

        return  form;
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



        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);



    }

}
