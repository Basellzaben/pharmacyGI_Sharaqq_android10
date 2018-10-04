
package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopSmallMenue extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel,acc,invoice,rec,order;
    Button back;



    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

      //  int dialogWidth =410; // specify a value here
      //  int dialogHeight = 300; // specify a value here


      //  int dialogWidth = 420; // specify a value here
      //  int dialogHeight =WindowManager.LayoutParams.WRAP_CONTENT;
      //  getDialog().getWindow().setLayout(dialogWidth, dialogHeight);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom( getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(lp);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(0);


    }


    @Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container,Bundle savestate){

         form =inflater.inflate(R.layout.popsmallmenue,container,false);
         getDialog().setTitle(getArguments().getString("Msg"));


        back = (Button)form.findViewById(R.id.button22);
        acc=(ImageButton) form.findViewById(R.id.btn_Acc);
        invoice=(ImageButton) form.findViewById(R.id.btn_invoice);
        rec=(ImageButton) form.findViewById(R.id.btn_rec);
        order=(ImageButton) form.findViewById(R.id.btn_order);


        back.setOnClickListener(this);
        order.setOnClickListener(this);
        acc.setOnClickListener(this);
        invoice.setOnClickListener(this);
        rec.setOnClickListener(this);

         getDialog().setCanceledOnTouchOutside(false);

     //   getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        return  form;
    }



    public void onClick(View v) {

         if (v == back) {
             this.dismiss();
        }

        if (v == invoice) {
            Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
            startActivity(k);
        }

        if (v == acc) {
            Intent k = new Intent(v.getContext(), Acc_ReportActivity.class);
            startActivity(k);
        }

        if (v == rec) {
            Intent k = new Intent(v.getContext(), RecvVoucherActivity.class);
            startActivity(k);
        }

        if (v == order) {
            Intent k = new Intent(v.getContext(), OrdersItems.class);
            startActivity(k);
        }


    }


}


