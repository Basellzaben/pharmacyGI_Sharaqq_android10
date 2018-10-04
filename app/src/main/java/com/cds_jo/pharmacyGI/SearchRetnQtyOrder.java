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

import com.cds_jo.pharmacyGI.assist.CustomerReturnQtyActivity;
import com.cds_jo.pharmacyGI.assist.cls_Search_Ret_Qty_adapter;
import com.cds_jo.pharmacyGI.assist.cls_Search_po;

import java.util.ArrayList;

public class SearchRetnQtyOrder extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
  //  TextView itemnm;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form =inflater.inflate(R.layout.activity_search_po,container,false);
        getDialog().setTitle("Galaxy International Group");
       /* add =(Button)form.findViewById(R.id.btn_add);
        cancel=(Button)form.findViewById(R.id.btn_cancel);
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
*/
        //itemnm = (TextView)form.findViewById(R.id.ed_item_nm);
         items_Lsit=(ListView) form.findViewById(R.id.listView2);
        ArrayList<cls_Search_po> cls_search_pos_list  = new ArrayList<cls_Search_po>();
        cls_search_pos_list.clear();
        String q = "Select po.OrderNo,po.date    , c.name  , po.acc from ReturnQtyhdr po Left join Customers c on c.no = po.acc ";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_Search_po cls_searchpos= new cls_Search_po();

                    cls_searchpos.setCustNo(c1.getString(c1.getColumnIndex("OrderNo")));
                    cls_searchpos.setCustNm(c1.getString(c1.getColumnIndex("name")));
                    cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                    cls_searchpos.setAcc(c1.getString(c1.getColumnIndex("acc")));
                    cls_searchpos.setNotes(" ");
                    cls_search_pos_list.add(cls_searchpos);
                }while (c1.moveToNext());
            }
            c1.close();
        }



        cls_Search_Ret_Qty_adapter cls_search_ret_qty_adapter = new cls_Search_Ret_Qty_adapter(
                this.getActivity(),cls_search_pos_list);


        items_Lsit.setAdapter(cls_search_ret_qty_adapter);






        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                cls_Search_po po_obj = (cls_Search_po) arg0.getItemAtPosition(position);
                String nm = po_obj.getCustNo();


                ((CustomerReturnQtyActivity) getActivity()).Set_Order(po_obj.getCustNo().toString() );
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
