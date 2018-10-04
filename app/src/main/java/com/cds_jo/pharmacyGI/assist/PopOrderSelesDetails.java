
package com.cds_jo.pharmacyGI.assist;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopOrderSelesDetails extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel;
    Button btn_Save,btn_Cancel;
    LinearLayout mContent;
    View view;

    Bitmap bitmap;
    SqlHandler sqlHandler;
    float min = 0 ;
    EditText Ed_Notes  ;
    ImageButton btn_filter_search ;
    String UnitNo ="";
    String UnitName ="";
    String str= "";
    private Calendar calendar;


    SqlHandler sql_Handler;






    int SaveImg = 0 ;
    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;
        int dialogWidth =  WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight =  WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }


    @Override
    public View onCreateView(LayoutInflater inflater   , ViewGroup container  , Bundle savestate){

        form =inflater.inflate(R.layout.poporderdetails,container,false);
           getDialog().setTitle("الملاحظات");

        sqlHandler = new SqlHandler(getActivity());
        SaveImg = 0 ;


        btn_Save=(Button) form.findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(this);

        btn_Cancel=(Button) form.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(this);


        Ed_Notes=(EditText) form.findViewById(R.id.Ed_Notes);


        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, null);




        final Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);


        calendar = Calendar.getInstance();




        sql_Handler = new SqlHandler(getActivity());
        String q = "SELECT  Notes  From Po_Hdr po   Where OrderNo ='"+getArguments().getString("OrdeNo")+ "'";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);
        String Date="";
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
            Ed_Notes.setText(c1.getString(c1.getColumnIndex("Notes")));

         }


         c1.close();
        }

        return  form;
    }








    private boolean isValidDate(String dateOfBirth) {
        boolean valid = true;

        if(dateOfBirth.trim().length()<10) {
            return false;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");


        } catch (Exception e) {
            valid = false;
            return valid;
        }




        return valid;
    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    private Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }
    public void onClick(View v) {


         if (v == btn_Cancel) {
            this.dismiss();
        }


        if (v == btn_Save) {
            EditText Ed_Notes = (EditText) form.findViewById(R.id.Ed_Notes);

            // Toast.makeText(getActivity(),Date,Toast.LENGTH_SHORT).show();
            String q = " SELECT * FROM PO_Hdr   Where OrderNo ='"+getArguments().getString("OrdeNo")+ "'";
            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 == null || c1.getCount() == 0) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("طلب البيع");
                alertDialog.setMessage("يجب تخزين طلب البيع اولا ّ");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
                c1.close();
                return;
            }

            ContentValues cv = new ContentValues();
            cv.put("Notes", Ed_Notes.getText().toString());

            Long i;
            i = sqlHandler.Update("Po_Hdr", cv, "orderno ='" + getArguments().getString("OrdeNo") + "'");
            if(i>0  ){
               Toast.makeText(getActivity(), "تمت عملية تخزين الملاحظات  بنجاح", Toast.LENGTH_SHORT).show();
            }


            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getResources().updateConfiguration(config, null);


        }
    }

}


