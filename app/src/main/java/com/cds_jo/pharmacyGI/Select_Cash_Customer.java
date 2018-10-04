package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.Customers;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Select_Cash_Customer extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    private SearchView mSearchView;
    EditText filter,et_nm   ;
    SqlHandler sqlHandler;
ImageButton btn_filter_search ;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {

        form = inflater.inflate(R.layout.activity_select__cash_customer, container, false);
        GetMaxPONo();
        getDialog().setTitle("العملاء النقديين");

        // Get the SearchView and set the searchable configuration
       // btn_filter_search =(ImageButton) form.findViewById(R.id.brn_seachAcc);
         filter =    (EditText) form.findViewById(R.id.et_Search_filter);
         et_nm =    (EditText) form.findViewById(R.id.et_nm);


      final   EditText et_nm =(EditText) form.findViewById(R.id.et_nm);
      final    TextView tv_No =(TextView) form.findViewById(R.id.tv_No);
       Button add = (Button)form.findViewById(R.id.btn_add);
        Button back = (Button)form.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exist_Pop();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_nm.getText().toString().equals("")) {
                    et_nm.setError("*");
                    return;
                }

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String u = sharedPreferences.getString("UserID", "");

                String q = " Insert";
                ContentValues cv = new ContentValues();
                cv.put("No", tv_No.getText().toString());
                cv.put("Name", et_nm.getText().toString());
                cv.put("veName", et_nm.getText().toString());
                cv.put("UserID", u);


                Long i = sqlHandler.Insert("CASHCUST", null, cv);

                UpDateMaxOrderNo();
                if (i > 0) {

                    //  (getArguments().getString("Scr") == "Sale_Inv")
                    ((Sale_InvoiceActivity) getActivity()).Set_Cust(tv_No.getText().toString(), et_nm.getText().toString());


                    Exist_Pop();
                }
            }
        });
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

        et_nm.addTextChangedListener(new TextWatcher() {
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
/*

        btn_filter_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                onProgressUpdate(filter.getText().toString());
            }
        });
*/




        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                /*arg1.setBackgroundColor(Color.GREEN);
                Object o = items_Lsit.getItemAtPosition(position);
                String str=(String)o;//As you are using Default String Adapter*/
                Customers customers = (Customers) arg0.getItemAtPosition(position);
                String nm = customers.getNm();
                Exist_Pop();
                ((Sale_InvoiceActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());


            }


        });

        return  form;
    }

    private  void  UpDateMaxOrderNo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u =  sharedPreferences.getString("UserID", "");
        String query = "SELECT  COALESCE(MAX(No), 0)   AS no FROM CASHCUST  where UserID = '"+u+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        max=(intToString(Integer.valueOf(max), 7)  );


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("m5", max);
        editor.commit();
    }

    private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",","");
        Double d = 0.0;
        if (str.length()==0) {
            str = "0";
        }
        if (str.length()>0)
            try {
                d =  Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            }
            catch (Exception ex)
            {
                str="0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
    public  void GetMaxPONo(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u =  sharedPreferences.getString("UserID", "");
        sqlHandler = new SqlHandler(getActivity());
        String query = "  SELECT ifnull (max( (cast (No as Long) )), 0) +1  AS no FROM CASHCUST  ";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";
        TextView tv_No = (TextView) form.findViewById(R.id.tv_No);
        if (c1 != null && c1.getCount() != 0) {

                c1.moveToFirst();
                 max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        String max1="0";
        max1 = sharedPreferences.getString("m5", "");
        if (max1==""){
            max1 ="0";
        }
        max1 =String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max))
        {
            max = max1 ;
        }


        if (max.length()==1) {
            tv_No.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {
            tv_No.setText(max);

        }
        tv_No.setFocusable(false);
        tv_No.setEnabled(false);
        tv_No.setCursorVisible(false);



    }

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            Toast.makeText(getActivity(),newText,Toast.LENGTH_SHORT).show();

            //mListView.clearTextFilter();
        } else {
            Toast.makeText(getActivity(),newText,Toast.LENGTH_SHORT).show();

            //mListView.setFilterText(newText.toString());
        }
        return true;
    }


    public void onProgressUpdate(String t ){
        final List<String> items_ls = new ArrayList<String>();
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);

        String query ;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u =  sharedPreferences.getString("UserID", "");

        if (t.toString().equals("")){
              query = "Select * from CASHCUST where UserID ='"+u+"'";
        }
        else {
            query = "Select * from CASHCUST where Name like '%" + t + "%' or  no like '%" + t + "%' and UserID ='"+u+"'";
        }
        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Customers> customersesList = new ArrayList<Customers>();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
             do{
                 Customers     customers = new Customers();

                 customers.setNo(c.getString(c.getColumnIndex("No")));
                 customers.setAcc(c.getString(c.getColumnIndex("No")));
                 customers.setNm(c.getString(c.getColumnIndex("Name")));

                 customersesList.add(customers);

             }while (c.moveToNext());
            }
        }




            Customer_List_Cash Customer_List_adapter = new Customer_List_Cash(
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
