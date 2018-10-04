package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cds_jo.pharmacyGI.assist.Cls_Search_Prepear_Qty_Order;
import com.cds_jo.pharmacyGI.assist.cls_Search_Pepeare_Qty_adapter;

import java.util.ArrayList;

public class SearchPreaperQty extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;

  //  TextView itemnm;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.activity_search_preaperqt,container,false);
        getDialog().setTitle("Galaxy International Group");
       /* add =(Button)form.findViewById(R.id.btn_add);
        cancel=(Button)form.findViewById(R.id.btn_cancel);
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
*/
        //itemnm = (TextView)form.findViewById(R.id.ed_item_nm);
         items_Lsit=(ListView) form.findViewById(R.id.listView2);
        ArrayList<Cls_Search_Prepear_Qty_Order> cls_search_pos_list  = new ArrayList<Cls_Search_Prepear_Qty_Order>();
        cls_search_pos_list.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String UserID= sharedPreferences.getString("UserID", "");

        String q = "Select po.orderno,po.date   , m.man  , m.name from PrepManQtyhdr po Left join manf m on m.man = po.manno  where po.userid ='"+UserID+"'";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Search_Prepear_Qty_Order cls_searchpos= new Cls_Search_Prepear_Qty_Order();
                    cls_searchpos.setOrderNo(c1.getString(c1.getColumnIndex("orderno")));
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
                /*arg1.setBackgroundColor(Color.GREEN);
                Object o = items_Lsit.getItemAtPosition(position);
                String str=(String)o;//As you are using Default String Adapter*/
                Cls_Search_Prepear_Qty_Order po_obj = (Cls_Search_Prepear_Qty_Order) arg0.getItemAtPosition(position);
                String nm = po_obj.getOrderNo();

                //  Toast.makeText(getActivity(), nm, Toast.LENGTH_LONG).show();

                ((PreapareManQty) getActivity()).Set_Order(po_obj.getOrderNo(), po_obj.getDate());
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
