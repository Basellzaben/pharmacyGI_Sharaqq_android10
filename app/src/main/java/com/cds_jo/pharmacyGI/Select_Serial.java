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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.Customers;
import com.cds_jo.pharmacyGI.assist.DoctorVisitNew;


import java.util.ArrayList;
import java.util.List;


public class Select_Serial extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    private SearchView mSearchView;


    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form = inflater.inflate(R.layout.search_serial, container, false);

        onProgressUpdate( );



        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Cls_Serial customers = (Cls_Serial) arg0.getItemAtPosition(position);
                String nm = customers.getSname();
                Exist_Pop();

        if (getArguments().getString("Scr") == "VisitNo") {
                    ((DoctorVisitNew) getActivity()).Set_Serial(customers.getSNo(), customers.getSname());

                }



            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }


    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }


    public void onProgressUpdate(  ){

        final List<String> items_ls = new ArrayList<String>();
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);

        String query ;



            query = "Select distinct SNo,Sname from VoucherSerial  ";//  And Customers.Cust_type='"+Cust_type+"'";

        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Cls_Serial> customersesList = new ArrayList<Cls_Serial>();
        customersesList.clear();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
             do{
                 Cls_Serial     customers = new Cls_Serial();

                 customers.setSNo(c.getString(c.getColumnIndex("SNo")));

                 customers.setSname(c.getString(c.getColumnIndex("Sname")));

                 customersesList.add(customers);

             }while (c.moveToNext());
            }
            c.close();
        }
        Serial_Adapter Customer_List_adapter = new Serial_Adapter(
                    this.getActivity(), customersesList);
            items_Lsit.setAdapter(Customer_List_adapter);
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

        }


    }


    public void onListItemClick(ListView l, View v, int position, long id) {
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

    }

}
