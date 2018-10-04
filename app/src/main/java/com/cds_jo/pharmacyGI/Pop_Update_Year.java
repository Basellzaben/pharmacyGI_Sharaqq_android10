package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.cds_jo.pharmacyGI.assist.MonthlySalesManSchedule;

import Methdes.MyTextView;



public class Pop_Update_Year extends DialogFragment implements View.OnClickListener {
    Button Inc, Dec;
    View form;
    MyTextView tv_qty ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {

        form = inflater.inflate(R.layout.update_qty, container, false);


        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP| Gravity.CENTER);



        Inc = (Button) form.findViewById(R.id.btn_Increment);
        Inc.setOnClickListener(this);
        Dec = (Button) form.findViewById(R.id.btn_Dicrement);
        Dec.setOnClickListener(this);


        tv_qty = (MyTextView)form.findViewById(R.id.tv_qty);
        tv_qty.setText(getArguments().getString("Year"));
        getDialog().setTitle(getArguments().getString("Nm"));

        return form;
    }
    @Override
    public void onClick(View v) {

        if (v==Inc){
            tv_qty.setText( (Integer.parseInt(tv_qty.getText().toString())+1)   +"" );

                    ((MonthlySalesManSchedule) getActivity()).UpdateYear(tv_qty.getText().toString());
            if( Integer.parseInt( tv_qty.getText().toString()) > 1 ) {
                Dec.setVisibility(View.VISIBLE);
            }

        }
        if(v==Dec){

            tv_qty.setText( (Integer.parseInt(tv_qty.getText().toString())-1)   +"" );
            if( Integer.parseInt( tv_qty.getText().toString()) < 1 ) {
                tv_qty.setText("1");
                Dec.setVisibility(View.INVISIBLE);
            }
            ((MonthlySalesManSchedule) getActivity()).UpdateYear(tv_qty.getText().toString());
        }
  }
}
