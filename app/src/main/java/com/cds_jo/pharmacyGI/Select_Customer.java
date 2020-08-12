package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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

import com.cds_jo.pharmacyGI.CustLocations.CustomerLocation;
import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
import com.cds_jo.pharmacyGI.assist.CustomerReturnQtyActivity;
import com.cds_jo.pharmacyGI.assist.Customer_List;
import com.cds_jo.pharmacyGI.assist.Customers;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Select_Customer extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    private SearchView mSearchView;
    EditText filter   ;

    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form = inflater.inflate(R.layout.activity_select__customer, container, false);
        // Get the SearchView and set the searchable configuration

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


        /*Intent intent =getActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getActivity(),SearchManager.QUERY,Toast.LENGTH_SHORT).show();
           // doMySearch(query);
        }*/

        /*Thread rant = new Thread() {
            public void run() {
                CallWebServices ws = new CallWebServices();
                ws.GetAccNo("", "s");
            }
        };
        rant.start();
        try {
            rant.join();
            onProgressUpdate(We_Result.Msg);

        } catch (Exception ex) {

        }*/





/*
        //   Toast.makeText(getActivity(),  getArguments().getString("Scr"),Toast.LENGTH_LONG).show();
        itemnm = (TextView)form.findViewById(R.id.ed_item_nm);
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        ArrayList<Customers> Customer_Items = new ArrayList<Customers>();

        Customers customers = new Customers("1","مجموعة ابو غلوس  ","عمان","0674755855","10251789");
        Customer_Items.add(customers);
        customers = new  Customers("2","المتحدون العرب","اربد","027575757","105178547");
        Customer_Items.add(customers);


        customers = new  Customers("3","النجم الساطع  ","العقبة","0521242424","105101012");
        Customer_Items.add(customers);

        Customer_List customer_list = new Customer_List(
                this.getActivity(),Customer_Items);


        items_Lsit.setAdapter(customer_list);*/



        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                /*arg1.setBackgroundColor(Color.GREEN);
                Object o = items_Lsit.getItemAtPosition(position);
                String str=(String)o;//As you are using Default String Adapter*/
                Customers customers = (Customers) arg0.getItemAtPosition(position);
                String nm = customers.getNm();
                Exist_Pop();
                //Toast.makeText(getActivity(), nm, Toast.LENGTH_LONG).show();

                if (getArguments().getString("Scr") == "po") {
                    ((OrdersItems) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }

                else if (getArguments().getString("Scr") == "AccReport") {
                    ((Acc_ReportActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }

                else if (getArguments().getString("Scr") == "RecVoch") {
                    ((RecvVoucherActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }
                else if (getArguments().getString("Scr") == "Sale_Inv") {
                    ((Sale_InvoiceActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }
                else if (getArguments().getString("Scr") == "SaleRound") {
                    ((SaleManRoundsActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }
                else if (getArguments().getString("Scr") == "Gps") {
                    ((MainActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }
                else if (getArguments().getString("Scr") == "CustQty") {
                    ((CustomerQty) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }

                else if (getArguments().getString("Scr") == "RetnQty") {
                    ((CustomerReturnQtyActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }  else if (getArguments().getString("Scr") == "CusfCard") {
                    ((CustomerLocation) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }
                else if (getArguments().getString("Scr") == "DoctorReprot") {
                    ((DoctorReportActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());
                }


               // Exist_Pop();
                //getDialog().setTitle(str);
                ////itemnm.setText(str);
            }


        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.gravity = Gravity.TOP | Gravity.CENTER;
        lp.x = 0;
        lp.y = 0;
        this.getDialog().getWindow().setAttributes(lp);
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }


    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }


    public void onProgressUpdate(String t ){

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


            if (getArguments().getString("Scr") == "Gps") {

                if (t.toString().equals("")){
                    query = "Select  distinct no,name ,countryNm from Customers  ";//  where countryNo in ( select AreaNo from Month_Dates where Posted='1'  and TodayDate   ='"+currentDateandTime+"')";//   Customers.Cust_type='"+Cust_type+"'";
                }
                else {
                    query = "Select distinct no,name ,countryNm from Customers where   no like '%"+t+"%' or name like '%"+t+"%'  ";// and    countryNo in ( select AreaNo from Month_Dates where  Posted='1'  and TodayDate   ='"+currentDateandTime+"')";//
                }
            }else {

                if (t.toString().equals("")){
                    query = " Select  distinct no,name ,countryNm from Customers  ";//   Customers.Cust_type='"+Cust_type+"'";
                }
                else {
                    query = " Select distinct no,name ,countryNm from Customers where   no like '%"+t+"%' or name like '%"+t+"%' ";
                }
            }

   //   query = "Select distinct no,name ,countryNm from Customers ";

        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Customers> customersesList = new ArrayList<Customers>();
        customersesList.clear();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
             do{
                 Customers     customers = new Customers();
                 customers.setNo(c.getString(c.getColumnIndex("no")));
                 customers.setAcc(c.getString(c.getColumnIndex("no")));
                 customers.setNm(c.getString(c.getColumnIndex("name")));
                 customers.setCountryNm(c.getString(c.getColumnIndex("countryNm")));
                 customersesList.add(customers);

             }while (c.moveToNext());
            }
            c.close();
        }




            Customer_List Customer_List_adapter = new Customer_List(
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


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

    }
/*@Override
    public  void OnClick(View view  ){

}*/
}
