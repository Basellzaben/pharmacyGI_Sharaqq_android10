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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.Customers;

import java.util.ArrayList;
import java.util.List;


public class Select_Location extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    private SearchView mSearchView;
    EditText filter   ;
    ImageView btn_filter_search ;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form = inflater.inflate(R.layout.activity_select_location, container, false);

        btn_filter_search =(ImageView) form.findViewById(R.id.brn_seachAcc);
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
                // Toast.makeText(getActivity(),s.toString(),Toast.LENGTH_SHORT).show();
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
                /*arg1.setBackgroundColor(Color.GREEN);
                Object o = items_Lsit.getItemAtPosition(position);
                String str=(String)o;//As you are using Default String Adapter*/
                Mans mans = (Mans) arg0.getItemAtPosition(position);
                String nm = mans.getNm();
                Exist_Pop();
                //Toast.makeText(getActivity(), nm, Toast.LENGTH_LONG).show();

                if (getArguments().getString("Scr") == "CountrySales") {
                    ((CountrySales) getActivity()).SetLocation(mans.getAcc(), mans.getNm());

                }

                else  if (getArguments().getString("Scr") == "ManVisitReport") {
                    ((ManVisitReport) getActivity()).SetLocation(mans.getAcc(), mans.getNm());

                }
                else if (getArguments().getString("Scr") == "DoctorReprot") {
                    ((DoctorReportActivity) getActivity()).Set_Cust(mans.getAcc(), mans.getNm());
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


    public void onProgressUpdate(String t ){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u = sharedPreferences.getString("UserID", "");

        final List<String> items_ls = new ArrayList<String>();
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);
        float t_dept , t_cred,t_bb ,tot ,t_tot,temp,t_rate;
        t_dept= t_cred=t_bb =tot =t_tot =temp=t_rate =  0 ;
        String query ;

        String Cust_type="1";
        if (getArguments().getString("Scr") == "DoctorReprot") {
            Cust_type= getArguments().getString("PrvVisitType");
        }

        if (getArguments().getString("Scr") == "CountrySales" || (getArguments().getString("Scr") == "ManVisitReport") ) {


            if (t.toString().equals("")){
                query = " Select   * from Area Where Cast( No as integer)>0 and ManNo='"+u+"'";
            }
            else {
                query = "Select * from Area where Cast( No as integer)>0 and ManNo='"+u+"' And  (name like '%" + t + "%' or  no like '%" + t + "%')";
            }

        }else{

            if (t.toString().equals("")){
                query = " Select   * from Area Where Cast( No as integer)>0 ";
            }
            else {
                query = "Select * from Area where Cast( No as integer)>0   And  (name like '%" + t + "%' or  no like '%" + t + "%')";
            }
        }




        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Mans> customersesList = new ArrayList<Mans>();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
             do{
                 Mans     mans = new Mans();

                 mans.setNo(c.getString(c.getColumnIndex("No")));
                 mans.setAcc(c.getString(c.getColumnIndex("No")));
                 mans.setNm(c.getString(c.getColumnIndex("Name")));

                 customersesList.add(mans);

             }while (c.moveToNext());
            }
            c.close();
        }




        Mans_List_Adapter Customer_List_adapter = new Mans_List_Adapter(
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
