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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.CustomerReturnQtyActivity;
import com.cds_jo.pharmacyGI.assist.DoctorVisitNew;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;
import com.cds_jo.pharmacyGI.assist.cls_Search_Inv_Sale_adapter;
import com.cds_jo.pharmacyGI.assist.cls_Search_Sample_Item_adapter;
import com.cds_jo.pharmacyGI.assist.cls_Search_po;

import java.util.ArrayList;

public class Sample_Item_SearchActivity extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
   // TextView itemnm;

    ArrayList<cls_Search_po> cls_search_pos_list  ;
    @Override
   public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.activity_sample_item__search,container,false);
        getDialog().setTitle("البحث في طلب العينات");

        final TextView Search = (TextView)form.findViewById(R.id.tv_Search)  ;

        items_Lsit=(ListView) form.findViewById(R.id.listView2);
          cls_search_pos_list  = new ArrayList<cls_Search_po>();
          cls_search_pos_list.clear();


        FillList("");



        Search.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Search.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });


        Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //   if(s.toString().length()>0)
                FillList(s.toString());


            }


        });







        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                cls_Search_po po_obj = (cls_Search_po) arg0.getItemAtPosition(position);
                String nm = po_obj.getCustNo();

                if (getArguments().getString("Scr") == "Sample") {

                    ((DoctorVisitNew) getActivity()).Set_Order(po_obj.getCustNo().toString());
                    Exist_Pop();
                } else if (getArguments().getString("Scr") == "RetnQty") {
                    ((CustomerReturnQtyActivity) getActivity()).Set_Order_From_invoice(po_obj.getCustNo().toString());
                    Exist_Pop();
                } else if (getArguments().getString("Scr") == "Edite_inv") {
                    ((EditeTransActivity) getActivity()).Set_Order_Sal_Inv(po_obj.getCustNo().toString());
                    Exist_Pop();
                }


            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }


     private void FillList(String Filter){
         String q ="";

         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
         cls_search_pos_list.clear();
         items_Lsit.setAdapter(null);
         if (Filter.length()==0) {
             q = "Select distinct s.ItemsCount , s.OrderNo ,s.acc ,s.date ,     c.name   as  name ,s.SerialNm  ,s.Notes " +
                     "from  SampleItem_Hdr s left join Customers c on c.no =s.acc where  UserID='"+sharedPreferences.getString("UserID", "")+"'";
         }else {
             q = "Select  distinct s.OrderNo ,s.ItemsCount ,s.acc ,s.date ,     c.name   as  name  ,s.SerialNm ,s.Notes  from  SampleItem_Hdr  s  " +
                     " left join Customers c on c.no =s.acc" +
                     " Where UserID='"+sharedPreferences.getString("UserID","") +"'" +
                     " and s.OrderNo like '%" + Filter + "%' " ;
         }

         SqlHandler sqlHandler = new SqlHandler(getActivity());
         Cursor c1 = sqlHandler.selectQuery(q);

         if (c1 != null && c1.getCount() != 0) {
             if (c1.moveToFirst()) {
                 do {
                     cls_Search_po cls_searchpos= new cls_Search_po();

                     cls_searchpos.setCustNo(c1.getString(c1.getColumnIndex("OrderNo")));
                     cls_searchpos.setCustNm(c1.getString(c1.getColumnIndex("SerialNm")));
                     cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                     cls_searchpos.setAcc(c1.getString(c1.getColumnIndex("OrderNo")));
                     cls_searchpos.setNotes(c1.getString(c1.getColumnIndex("Notes")));
                     cls_searchpos.setType("");
                     cls_searchpos.setTot(c1.getString(c1.getColumnIndex("ItemsCount")));
                     cls_search_pos_list.add(cls_searchpos);
                 }while (c1.moveToNext());
             }
          c1.close();
     }


         TextView count = (TextView)form.findViewById(R.id.tv_Count);
         count.setText(  cls_search_pos_list.size() +"");
         cls_Search_Sample_Item_adapter cls_Search_invSale_adapter_obj = new cls_Search_Sample_Item_adapter(
                 this.getActivity(),cls_search_pos_list);


         items_Lsit.setAdapter(cls_Search_invSale_adapter_obj);
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

}
