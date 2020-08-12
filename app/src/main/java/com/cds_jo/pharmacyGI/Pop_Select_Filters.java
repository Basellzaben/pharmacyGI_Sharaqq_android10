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


public class Pop_Select_Filters extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    private SearchView mSearchView;

    ImageView btn_filter_search ;

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;

        int dialogWidth =700; // specify a value here
        int dialogHeight = 700; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form = inflater.inflate(R.layout.search_filters, container, false);
        onProgressUpdate("");
        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_Filter customers = (Cls_Filter) arg0.getItemAtPosition(position);

                Exist_Pop();


                if (getArguments().getString("Scr") == "po") {
                    ((Pop_Po_Select_Items_New_Activity) getActivity()).Set_Filter(customers.getDesc() );

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

        final List<String> items_ls = new ArrayList<String>();
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);
        String query ;



       query = "Select distinct Search_Key from deptf_Filter where Type_No ='" +getArguments().getString("Type_No")+"'";


        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Cls_Filter> customersesList = new ArrayList<Cls_Filter>();
        customersesList.clear();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
             do{
                 Cls_Filter     obj = new Cls_Filter();
                 obj.setDesc(c.getString(c.getColumnIndex("Search_Key")));

                 customersesList.add(obj);

             }while (c.moveToNext());
            }
            c.close();
        }




        Filter_Adapter Customer_List_adapter = new Filter_Adapter(
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
        else  if (bu.getText().toString().equals("Add")) {
            Toast.makeText(getActivity(),
                    "Your Message", Toast.LENGTH_SHORT).show();


        }

    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);
    }

}
