
package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hp on 07/02/2016.
 */

public class PromotionEffict extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0 ;
    EditText filter   ;
    ImageButton btn_filter_search ;
    String UnitNo ="";
    String UnitName ="";
    String str= "";
      RadioButton rad_Per ;
       RadioButton rad_Amt;
    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){

        form =inflater.inflate(R.layout.promotioneffict,container,false);

        getDialog().setTitle("Galaxy");
        add =(ImageButton) form.findViewById(R.id.btn_add_item);
        add.setOnClickListener(this);
        cancel=(ImageButton) form.findViewById(R.id.btn_cancel_item);

        cancel.setOnClickListener(this);


        final List<String> promotion_ls = new ArrayList<String>();



        sqlHandler =  new SqlHandler(getActivity());


        ListView lst_Promotion = (ListView)form.findViewById(R.id.lst_Promotion);

        String q = "Select * from Offers_Hdr ";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    promotion_ls.add(c1.getString(c1.getColumnIndex("Offer_Name"))  );

                }while (c1.moveToNext());

                }
                c1.close();
            }
       /* promotion_ls.add("مع كل 50 باكيت بسكوت كناري تحصل على 2 باكيت مجانا");
        promotion_ls.add("مع كل 30 باكيت نسكافية تحصل على خصم 10% على قيمة الفاتورة النهائية");*/
        ArrayAdapter<String> promotion_ad = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1,promotion_ls);

        lst_Promotion.setAdapter(promotion_ad);






        return  form;
    }





 @Override
 public void onClick(View v) {


     final EditText dis = (EditText)form.findViewById(R.id.et_disc_per);

     if(v==add) {
         EditText Price = (EditText) form.findViewById(R.id.et_price);
         EditText qty = (EditText) form.findViewById(R.id.et_qty);
         EditText tax = (EditText) form.findViewById(R.id.et_tax);

         EditText bo = (EditText) form.findViewById(R.id.et_bo);
         // Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);
         EditText  bounce = (EditText) form.findViewById(R.id.et_bo);
         EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
         EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);


         EditText net_total = (EditText) form.findViewById(R.id.et_totl);


         if (Price.getText().toString().length() == 0) {
             Price.setError("required!");
             Price.requestFocus();
             return;
         }

         if (qty.getText().toString().length() == 0) {
             qty.setError("required!");
             qty.requestFocus();
             return;
         }
         if (tax.getText().toString().length() == 0) {
             tax.setError("required!");
             tax.requestFocus();
             return;
         }
        /* if( dis.getText().toString().length() == 0 ) {
             dis.setError("required!");
             dis.requestFocus();
             return;
         }*/

     if( ItemNo.toString().length() == 0 ) {

             return;
         }

         if (getArguments().getString("Scr") == "po") {
            //    ((OrdersItems) getActivity()).Save_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(),str,UnitName,disc_Amt.getText().toString());
                Price.requestFocus();
         }
         /*else if (getArguments().getString("Scr") == "Sal_inv") {
             ((Sale_InvoiceActivity) getActivity()).Save_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString());
             Price.requestFocus();
         }*/


         try {
             min = 0;
             Price.setText("0");
             qty.setText("");
             tax.setText("");
             bo.setText("");
             dis.setText("");
             net_total.setText("");
             ItemNo = "";
             UnitNo = "";


         } catch (Exception e) {
e.printStackTrace();
         }
     }
     if(v==cancel){
         this.dismiss();
     }



    }








}


