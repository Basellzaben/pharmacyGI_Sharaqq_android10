
package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import Methdes.MyTextView;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopEnterActQty extends DialogFragment implements View.OnClickListener  {
    View form ;
    NumberPicker np;
    Button btnSave ;
    EditText et_Act_qty,ed_qty , ed_diff;
    @Override
    public void onStart()
    {
        super.onStart();


        if (getDialog() == null)
            return;

         int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here*/




        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }


    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.view_enter_act_qty,container,false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        MyTextView Title=(MyTextView)form.findViewById(R.id.textView260) ;
        Title.setText( getArguments().getString("ItemName"));
        /*np = (NumberPicker) form.findViewById(R.id.np_act_aqt);
        np.setMaxValue(120);
        np.setMinValue(0);
        np.setValue(30);*/

        et_Act_qty = (EditText)form.findViewById(R.id.et_Act_qty);
        ed_qty = (EditText)form.findViewById(R.id.ed_qty);
        ed_diff = (EditText)form.findViewById(R.id.ed_diff);


        ed_qty.setText(getArguments().getString("Qty"));
        et_Act_qty.requestFocus();

        btnSave = (Button)form.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        et_Act_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                CalcDiff();
            }


        });
        return  form;
    }
private void CalcDiff (){
    double r = 0.0;
    r= (SToD(ed_qty.getText().toString()) - SToD(et_Act_qty.getText().toString())) ;
    ed_diff.setText( SToD(r+"")+"");
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


    public void onClick(View v) {
        ((ManBalanceQtyActivity) getActivity()).SaveQty(et_Act_qty.getText().toString(), ed_diff.getText().toString()) ;
  this.dismiss();
    }


}


