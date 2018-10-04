package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import hearder.main.Header_Frag;


public class EditeTransActivity extends FragmentActivity {
    EditText OrderNo1 ,OrderNo2,OrderNo3;
    SqlHandler sqlHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_edite_trans);

        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();


        sqlHandler = new SqlHandler(EditeTransActivity.this);
        OrderNo1= (EditText)findViewById(R.id.et_OrderNo1);
        OrderNo2= (EditText)findViewById(R.id.et_OrderNo2);
        OrderNo3= (EditText)findViewById(R.id.et_OrderNo3);

        OrderNo1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    OrderNo1.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        OrderNo2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    OrderNo2.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });



        OrderNo3.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    OrderNo3.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });
    }

    public void btn_Del_Po(View view) {

        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("طلب البيع");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        i =  sqlHandler.Delete("Po_Hdr","orderno ='"+OrderNo3.getText()+"'");
        if (i>0) {
            i = sqlHandler.Delete("Po_dtl", "orderno ='" + OrderNo3.getText() + "'");
        }
        if (i>0){

            OrderNo3.setText("");
            alertDialog.setMessage("تمت عملية الحذف بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else
        {
            alertDialog.setMessage("عملية الحذف لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }

    public void btn_Del_Pay(View view) {

        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("سند قبض");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        i =  sqlHandler.Delete("RecCheck","DocNo ='"+OrderNo1.getText()+"'");
        i =  sqlHandler.Delete("RecVoucher","DocNo ='"+OrderNo1.getText()+"'");
        if (i>0){


            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية الحذف بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else
        {
            alertDialog.setMessage("عملية الحذف لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    public void btn_search_Recv(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "EditeRec");
        FragmentManager Manager = getFragmentManager();
        RecVoucherSearchActivity obj = new RecVoucherSearchActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void btn_search_Po(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "EditeRec");
        FragmentManager Manager = getFragmentManager();
        SearchPoActivity obj = new SearchPoActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void Set_Order(String No) {

        OrderNo1.setText(No);

    }

    public void Set_Order3(String No) {

        OrderNo3.setText(No);

    }
    public void btn_Post_Payment(View view) {

        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("سند قبض");

        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        ContentValues cv = new ContentValues();
        cv.put("Post","-1");
        i =  sqlHandler.Update("RecVoucher",cv,"DocNo ='"+OrderNo1.getText()+"'");
        if (i>0){
            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية الغاء الاعتماد بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else {
            alertDialog.setMessage("عملية الغاء الاعتماد لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }
    public void btn_cancel_doc(View view) {
        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("سند قبض");

        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        ContentValues cv = new ContentValues();
        cv.put("Post","2");
        i =  sqlHandler.Update("RecVoucher",cv,"DocNo ='"+OrderNo1.getText()+"'");
        if (i>0){
            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية اخفاء السند  بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else {
            alertDialog.setMessage("عملية اخفاء السند لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }


    }
    public void btn_Post_SalInvoic(View view) {
        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("طلب البيع");


        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        ContentValues cv = new ContentValues();
        cv.put("posted","-1");
        i =  sqlHandler.Update("Po_Hdr",cv,"orderno ='"+OrderNo3.getText()+"'");
        if (i>0){
            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية الغاء الاعتماد بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else {
            alertDialog.setMessage("عملية الغاء الاعتماد لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }
    public void btn_cancel_SalInvo(View view) {

        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("طلب البيع");

        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        ContentValues cv = new ContentValues();
        cv.put("posted","2");
        i =  sqlHandler.Update("Po_Hdr",cv,"orderno ='"+OrderNo3.getText()+"'");
        if (i>0){
            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية اخفاء طلب البيع بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else {
            alertDialog.setMessage("عملية اخفاء طلب لم تتم  بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }
    public void Set_Order_Sal_Inv(String No) {
        OrderNo2.setText(No);

    }
    public void btn_Search_Orders(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Edite_inv");
        bundle.putString("typ", "0");
        FragmentManager Manager =  getFragmentManager();
        Sal_Inv_SearchActivity obj = new Sal_Inv_SearchActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}