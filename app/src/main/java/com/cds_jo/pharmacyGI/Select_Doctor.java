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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Select_Doctor extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;

    EditText filter   ;
ImageButton btn_filter_search ;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {

        form = inflater.inflate(R.layout.activity_select_doctor, container, false);

        getDialog().setTitle("الاطباء");


        btn_filter_search =(ImageButton) form.findViewById(R.id.brn_seachAcc);
        filter =    (EditText) form.findViewById(R.id.et_Search_filter);


        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                onProgressUpdate(s.toString());

            }
        });
        onProgressUpdate("");

        btn_filter_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                onProgressUpdate(filter.getText().toString());
            }
        });


        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Doctor doctor = (Doctor) arg0.getItemAtPosition(position);
                String nm = doctor.getName();
                Exist_Pop();
                //Toast.makeText(getActivity(), nm, Toast.LENGTH_LONG).show();

                if (getArguments().getString("Scr") == "DoctorReprot") {
                    ((DoctorReportActivity) getActivity()).Set_Cust(doctor.getNo(), doctor.getName());
                }


            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }



   public void onProgressUpdate(String t ){
        final List<String> items_ls = new ArrayList<String>();
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);
        String query ;

        if (t.toString().equals("")){
              query = "Select Dr_No ,Dr_AName , s.Aname  from Doctor   Left  Join Specialization s on s.No = Doctor.Specialization_No";//   Customers.Cust_type='"+Cust_type+"'";
        }
        else {
            query = "Select Dr_No ,Dr_AName , s.Aname  from Doctor Left  Join Specialization s on s.No = Doctor.Specialization_No " +
                    "  where Dr_AName like '%" + t + "%' or  no like '%" + t + "%'";//  And Customers.Cust_type='"+Cust_type+"'";
        }
        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Doctor> customersesList = new ArrayList<Doctor>();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
             do{
                 Doctor     customers = new Doctor();
                 customers.setNo(c.getString(c.getColumnIndex("Dr_No")));
                 customers.setSpec(c.getString(c.getColumnIndex("Aname")));
                 customers.setName(c.getString(c.getColumnIndex("Dr_AName")));
                 customersesList.add(customers);
             }while (c.moveToNext());
            }
            c.close();
        }

        Cls_Doctor_Search_Adapter Doct_Adp = new Cls_Doctor_Search_Adapter(
                    this.getActivity(), customersesList);

        items_Lsit.setAdapter(Doct_Adp);


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
