package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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

import com.cds_jo.pharmacyGI.assist.CallWebServices;

import java.util.ArrayList;

/*import android.widget.Toast;*/


public class Select_Items extends DialogFragment implements View.OnClickListener  {
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
        form = inflater.inflate(R.layout.select_item, container, false);

        getDialog().setTitle("مجموعة المجرة الدولية");
        // Get the SearchView and set the searchable configuration
        btn_filter_search =(ImageView) form.findViewById(R.id.brn_seachAcc);
        filter =    (EditText) form.findViewById(R.id.et_Search_filter);
      onProgressUpdate("");

        btn_filter_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                onProgressUpdate(filter.getText().toString());
            }
        });

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
               // Toast.makeText(getActivity(), s.toString(), Toast.LENGTH_SHORT).show();
                onProgressUpdate("");

            }
        });

       items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Cls_Item_Search cls_item_search = (Cls_Item_Search) arg0.getItemAtPosition(position);
                Exist_Pop();
                if (getArguments().getString("Scr") == "ItemCost") {
                    ((ItemCostActivity) getActivity()).Set_Item(cls_item_search.getNo(), cls_item_search.getName());

                }

               /* else if (getArguments().getString("Scr") == "AccReport") {
                    ((Acc_ReportActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }

                else if (getArguments().getString("Scr") == "RecVoch") {
                    ((RecvVoucherActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }
                else if (getArguments().getString("Scr") == "Sale_Inv") {
                    ((Sale_InvoiceActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }*/
                Exist_Pop();
                //getDialog().setTitle(str);
                ////itemnm.setText(str);
            }


        });

        return  form;
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            //Toast.makeText(getActivity(),newText,Toast.LENGTH_SHORT).show();

            //mListView.clearTextFilter();
        } else {
           // Toast.makeText(getActivity(),newText,Toast.LENGTH_SHORT).show();

            //mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {

        Thread rant = new Thread() {
            public void run() {
                CallWebServices ws = new CallWebServices(getContext());
                ws.GetAccNo("s", "s");
            }
        };
        rant.start();
        try {
            rant.join();
            onProgressUpdate(We_Result.Msg);

        } catch (Exception ex) {

        }
        return false;
    }
    public void onProgressUpdate(String t ){

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);
        float t_dept , t_cred,t_bb ,tot ,t_tot,temp,t_rate;
        t_dept= t_cred=t_bb =tot =t_tot =temp=t_rate =  0 ;
        filter =    (EditText) form.findViewById(R.id.et_Search_filter);
        String  query;
        sqlHandler = new SqlHandler(getActivity());
        if (filter.getText().toString().equals("")){
            query = "Select * from invf    ";
        }
        else
        {
            query = "Select * from invf where Item_Name  like '%"+filter.getText().toString()+"%'  or  Item_No like '%"+ filter.getText().toString() +"%'  ";
        }


        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Cls_Item_Search> cls_item_searches = new ArrayList<Cls_Item_Search>();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
             do{
                 Cls_Item_Search cls_item_search = new Cls_Item_Search();
                 cls_item_search.setNo(c.getString(c.getColumnIndex("Item_No")));
                 cls_item_search.setName(c.getString(c.getColumnIndex("Item_Name")));
                 cls_item_searches.add(cls_item_search);

             }while (c.moveToNext());
            }
        }

            Cls_Item_Search_Adapter cls_item_search_adapter = new Cls_Item_Search_Adapter(
                    this.getActivity(), cls_item_searches);

            items_Lsit.setAdapter(cls_item_search_adapter);

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
            //Toast.makeText(getActivity(),"Your Message", Toast.LENGTH_SHORT).show();
         //   ((OrdersItems)getActivity()).Save_Method("maen");
         }

    }
    public void onListItemClick(ListView l, View v, int position, long id) {

        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);



    }

}
