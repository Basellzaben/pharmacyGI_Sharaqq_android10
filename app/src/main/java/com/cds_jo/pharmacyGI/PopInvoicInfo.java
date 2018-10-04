
package com.cds_jo.pharmacyGI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopInvoicInfo extends DialogFragment implements View.OnClickListener  {
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
    Spinner sp_banks ;
    ArrayList<Cls_Bank_Search> cls_bank_searches;
    SqlHandler sql_Handler;
    TextView CheckData;
    private int year, month, day;
    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = 340; // specify a value here
        int dialogHeight = 300; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }


    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){

        form =inflater.inflate(R.layout.popinvoiceinfo,container,false);

        getDialog().setTitle("معلومات الفاتورة");

        cancel=(ImageButton) form.findViewById(R.id.btn_Back);

        cancel.setOnClickListener(this);



        sql_Handler = new SqlHandler(getActivity());
        invoice();
       /* CheckData = (TextView) form.findViewById(R.id.CheckData);



        CheckData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog picker = new  DatePickerDialog(getActivity(), myDateListener, year, month, day);
                picker.show();

             //showDialog(999);

            }
        });*/
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
       // CheckData.setText(currentDateandTime);
        return  form;
    }

    public void setDate(View view) {

        DatePickerDialog picker = new  DatePickerDialog(getActivity(), myDateListener, year, month, day);
        picker.show();


        //  showDialog(999);
       /* Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();*/
    }


    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(getActivity(), myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

            CheckData.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));

    }

    private  void invoice(){
        TextView tv_InvoiceNo = (TextView)form.findViewById(R.id.tv_InvoiceNo);
        TextView tv_InvoiceType = (TextView)form.findViewById(R.id.tv_InvoiceType);
        TextView tv_Invoice_Acc = (TextView)form.findViewById(R.id.tv_Invoice_Acc);
        TextView tv_InvoiceDate = (TextView)form.findViewById(R.id.tv_InvoiceDate);
        TextView tv_Nm = (TextView)form.findViewById(R.id.tv_Nm);



        String query = "Select s.OrderNo  ,s.inovice_type  ,s.acc  ,s.date , c.name  from Sal_invoice_Hdr s  left join  Customers c " +
                " on s.acc   = c.no where OrderNo='"+getArguments().getString("No") +"'";
       Cursor c1 = sql_Handler.selectQuery(query);
       if (c1 != null && c1.getCount() != 0) {
           if (c1.moveToFirst()) {


               tv_InvoiceNo.setText(c1.getString(c1.getColumnIndex("OrderNo")));
               if (c1.getString(c1.getColumnIndex("inovice_type")).equals("0"))
               {
                   tv_InvoiceType.setText("نقدية");
               }
               else
               {
                   tv_InvoiceType.setText("ذمم");
               }

               tv_Invoice_Acc.setText(c1.getString(c1.getColumnIndex("acc")));
               tv_InvoiceDate.setText(c1.getString(c1.getColumnIndex("date")));
               tv_Nm.setText(c1.getString(c1.getColumnIndex("name")));

           }

       c1.close();
    }
   }



    public void onClick(View v) {



         if (v == cancel) {
             this.dismiss();
        }

    }


}


