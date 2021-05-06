package com.cds_jo.pharmacyGI.CustLocations;

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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.cds_jo.pharmacyGI.R;

import com.cds_jo.pharmacyGI.SqlHandler;

import com.cds_jo.pharmacyGI.assist.Customer_List;
import com.cds_jo.pharmacyGI.assist.Customers;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Pop_Select_Stutes extends DialogFragment implements View.OnClickListener  {
    View form ;

    ListView items_Lsit;

    EditText filter   ;

    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form = inflater.inflate(R.layout.pop_select_stutes, container, false);
        // Get the SearchView and set the searchable configuration


        items_Lsit =    (ListView) form.findViewById(R.id.listView2);


        onProgressUpdate("");




        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Cls_Stutes obj = (Cls_Stutes) arg0.getItemAtPosition(position);
                String nm = obj.getDesc();
                Exist_Pop();

                    ((CustomerLocation) getActivity()).Set_Stutes( obj.getDesc());






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





    public void onProgressUpdate(String t ){


        items_Lsit.setAdapter(null);


        ArrayList<Cls_Stutes> Stutes = new ArrayList<Cls_Stutes>();
        Stutes.clear();

                 Stutes.add(new Cls_Stutes("مغلق"));
                 Stutes.add(new Cls_Stutes("معلق"));
                 Stutes.add(new Cls_Stutes("يتم التعامل مع "));

        Stutes_Adapter Customer_List_adapter = new Stutes_Adapter(
                    this.getActivity(), Stutes);

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
