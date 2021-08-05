package com.cds_jo.pharmacyGI;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Locale;

import Methdes.MethodToUse;
import port.bluetooth.BluetoothConnectMenu;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;


public class GalaxyLoginActivity extends AppCompatActivity {
    private Context context;
    private EditText UserName, Password;
    private ImageView Login_Img;
    int PHRLASTUPDATE=19;
    Locale locale ;
    String q ;
    SqlHandler sqlHandler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_galaxy_login);


       // requestPermission();

         Initi();
            sqlHandler = new SqlHandler(this);

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  DB_VERVSION    " +
                    "( ID integer primary key autoincrement,No text null)");


            final int DB_VERVSION = Integer.parseInt(DB.GetValue(GalaxyLoginActivity.this, "DB_VERVSION", "No", "5=5"));

            q = "DELETE FROM  DB_VERVSION  ";
            sqlHandler.executeQuery(q);

            q = "INSERT INTO DB_VERVSION(No) values ('"+PHRLASTUPDATE+"')";
            sqlHandler.executeQuery(q);



            if (DB_VERVSION < PHRLASTUPDATE)
                DataBaseChanges();

        }  catch (Exception ex){
            WriteTxtFile.MakeText("DB_VERVSION < LASTUPDATE",ex.getMessage().toString());
        }
        RadioButton rdoAr = (RadioButton)findViewById(R.id.rdoAr);
        RadioButton rdoEn = (RadioButton)findViewById(R.id.rdoEn);

        rdoAr.setChecked(true);
        UserName.setText(sharedPreferences.getString("man", ""));
        Password.setText(sharedPreferences.getString("password", ""));


        Login_Img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                RadioButton rdoAr = (RadioButton)findViewById(R.id.rdoAr);
                RadioButton rdoEn = (RadioButton)findViewById(R.id.rdoEn);
                SharedPreferences.Editor editor    = sharedPreferences.edit();





                  if(UserName.getText().toString().equals("admin") && Password.getText().toString().equalsIgnoreCase("12589")) {

                      editor.putString("man","admin");
                      editor.putString("UserName", "admin");
                      editor.putString("LoginAccount","admin");
                      editor.putString("password","12589");
                      editor.putString("UserID","admin");
                      editor.putString("TypeDesc","-1");
                      editor.putString("Login", "Yes");
                      editor.commit();
                    Intent k = new Intent(context,BluetoothConnectMenu.class);
                    startActivity(k);
                }


               else  if (UserName.getText().toString().equals("") && (!Password.getText().toString().equals(""))) {
                    UserName.setError(getResources().getText(R.string.EnterUserName));
                      Toast.makeText(getApplicationContext(),getResources().getText(R.string.EnterUserName),Toast.LENGTH_SHORT).show();
                } else if (!(UserName.getText().toString().equals("")) && Password.getText().toString().equals("")) {
                      Toast.makeText(getApplicationContext(),getResources().getText(R.string.EnterPassword),Toast.LENGTH_SHORT).show();
                      Password.setError(getResources().getText(R.string.EnterPassword));
                } else if (UserName.getText().toString().equals("") && Password.getText().toString().equals("")) {
                    UserName.setError(getResources().getText(R.string.EnterUserName));
                      Toast.makeText(getApplicationContext(),getResources().getText(R.string.EnterUserName),Toast.LENGTH_SHORT).show();
                      Password.setError(getResources().getText(R.string.EnterPassword));
                } else {
                        SqlHandler sqlHandler = new SqlHandler(context);
                        String query = "SELECT  name ,man,username ,password ,TypeDesc, ifnull(MANTYPE,0) as MANTYPE      from  manf   where man = '"+ UserName.getText().toString()+"' And password='"+Password.getText().toString()+"'";
                        Cursor c1 = sqlHandler.selectQuery(query);

                        if (c1!=null&&  c1.getCount() > 0 ) {
                            c1.moveToFirst();
                            editor.putString("man", c1.getString(c1.getColumnIndex("man")).toString());
                            editor.putString("UserName", c1.getString(c1.getColumnIndex("name")).toString());
                            editor.putString("LoginAccount",c1.getString(c1.getColumnIndex("username")).toString());
                            editor.putString("password",c1.getString(c1.getColumnIndex("password")).toString());
                            editor.putString("UserID",c1.getString(c1.getColumnIndex("man")).toString());
                            editor.putString("TypeDesc",c1.getString(c1.getColumnIndex("TypeDesc")).toString());
                            editor.putString("Login", "Yes");

                            ComInfo.ComNo = Integer.parseInt(DB.GetValue(GalaxyLoginActivity.this, "ComanyInfo", "CompanyID", "1=1"));
                            editor.commit();
                            ComInfo.UserType = 1 ;
                            Intent k  ;
                             if(c1.getString(c1.getColumnIndex("MANTYPE")).equalsIgnoreCase("0")){
                                k= new Intent(context,GalaxyMainActivity.class);
                               //k= new Intent(context,GalaxyMainActivity2.class);
                                 ComInfo.UserType = 1 ;
                               // ComInfo.UserType = 2 ;
                            }else{
                                k= new Intent(context,GalaxyMainActivity2.class);
                                 ComInfo.UserType = 2 ;
                            }
                            c1.close();
                             startActivity(k);

                        }else{
       Toast.makeText(getApplicationContext(),"يوجد خطأ في بيانات الدخول",Toast.LENGTH_SHORT).show();
                             }
                    }


                 /*else {
                    MethodToUse.ToastCreate(context, "الرجاء التأكد من البيانات");*/

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
    private void  DataBaseChanges(){


        try{
            sqlHandler.executeQuery("Alter Table CustStoreQtydetl  Add  COLUMN  Cust_No  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table CustStoreQtydetl  Add  COLUMN  Order_Date  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table CustStoreQtydetl  Add  COLUMN  OrderNo  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table CustStoreQtydetl  Add  COLUMN  UserID   text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  Cust_type  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  MANTYPE  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  TypeDesc  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  MobileNo  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  MobileNo2  text null" );
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  VisitType  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  VisitType1  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  VisitType2  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  VisitType3  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  VisitType4  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  Notes  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  QuesHdr ( no integer primary key autoincrement,Orderno text null, Nm text null , acc text null ,date text null ,posted text null ,userid text null) ");
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS QuesDtl ( no integer primary key autoincrement,Orderno text null, QuesTxt text null" +
                    ",   Ans1 text null ,Ans2 text null ,Ans3 text null,Ans4 text null  ) ");
        }catch ( SQLException e){ }



        try{
            sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS QuesTbl ( no integer primary key autoincrement,QuesNo text null, Questxt text null ,date text null ) ");
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table QuesHdr  Add  COLUMN  Notes  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  MobileOrder  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  X  text null" );
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  Y  text null" );
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  Locat  text null" );
        }catch ( SQLException e){ }


        sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS SampleItem_Hdr ( no integer primary key autoincrement,OrderNo text null , Nm text null , acc text null ,date text null,UserID INTEGER null,Post INTEGER null " +
                " , Total Text null , inovice_type text null , V_OrderNo  text null , SerialNo text null ,SerialNm text null ,Notes text null ) ");


        sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS SampleItem_Det ( no integer primary key autoincrement, OrderNo text null, itemNo text null,unitNo text null  , price text null ,qty text null ,tax text null  ,UserID INTEGER null ,Post INTEGER null   " +
                "  ,total text null ,StoreNo  ,  Operand  text null   ) ");

        try{
            sqlHandler.executeQuery("Alter Table SampleItem_Det  Add  COLUMN  DoctorNo  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SampleItem_Det  Add  COLUMN  DoctorNM  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  SampleSerial  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  Serial_name  text null" );
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table SampleItem_Hdr  Add  COLUMN  SerialNm  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SampleItem_Hdr  Add  COLUMN  Notes  text null" );
        }catch ( SQLException e){ }
        try{
            sqlHandler.executeQuery("Alter Table PO_Hdr  Add  COLUMN  Notes  text null" );
        }catch ( SQLException e){ }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  OrdersSitting    " +
                    "( ID integer primary key autoincrement,Sales  text null,Payment  text null ,SalesOrder text null,PrepareQty text null , RetSales text null ," +
                    "  PostDely  text null )");
        } catch (SQLException e) {
        }

        try{
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  Visits  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ORDER_STUTES ( no integer primary key autoincrement,OrderNo text null, Posted text null , AB_SalesOrder text null ,AB_SalesBill text null ,Notes text null ,PrintOrder text null) ");
        }catch ( SQLException e){ }

    }
    private void Initi() {
        context = GalaxyLoginActivity.this;
        UserName = (EditText)findViewById(R.id.editText);
        Password = (EditText)findViewById(R.id.editText2);
        UserName.setTypeface(MethodToUse.SetTFace(context));
        Password.setTypeface(MethodToUse.SetTFace(context));
        Login_Img = (ImageView)findViewById(R.id.imageView4);

    }



    private void requestPermission() {
      if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
          ActivityCompat.requestPermissions(GalaxyLoginActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
          ActivityCompat.requestPermissions(GalaxyLoginActivity.this, new String[]{READ_EXTERNAL_STORAGE}, 1);

              ActivityCompat.requestPermissions(GalaxyLoginActivity.this, new String[]{MANAGE_EXTERNAL_STORAGE}, 1);

      }

/*

        if(SDK_INT >= 30){


            if (Environment.isExternalStorageManager()){

// If you don't have access, launch a new activity to show the user the system's dialog
// to allow access to the external storage
            }else{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }

            if(!Environment.isExternalStorageManager()){
                Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                    startActivity(intent);
                                } catch (Exception ex){
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
            }

    }


*/


}

}
