package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Cls_Check;
import com.cds_jo.pharmacyGI.assist.Cls_UpdateData;
import com.cds_jo.pharmacyGI.assist.dummy.Cls_UpdateData_Adapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Methdes.MethodToUse;
import hearder.main.Header_Frag;


public class UpdateDataToMobileActivity extends AppCompatActivity {

    String str = "";
    private static final int PHRLASTUPDATE =44;
    private Handler progressBarHandler = new Handler();
    SqlHandler sqlHandler ;

    boolean a;
    ArrayList<Cls_UpdateData> List_Result;
    ListView Lv_Result ;
    String q = "";
    Cls_UpdateData_Adapter listAdapter ;
    ArrayList<Cls_Check> ChecklList;
    TextView tv  ;

    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    private  void filllist ( String str , int f , int c){

        Cls_UpdateData obj = new Cls_UpdateData();
        String msg = "";
        if (f ==1){
            msg = " تمت عملية تحديث " + str +  " بنجاح"   ;
        }
        else {
            msg = "عملية تحديث " + str +  " لم تتم بنجاح"      ;

        }

        obj.setMsg(msg);
        obj.setCount(c);
        obj.setFlag(f);
        List_Result.add(obj);

        listAdapter = new Cls_UpdateData_Adapter(UpdateDataToMobileActivity.this,List_Result);
        Lv_Result.setAdapter(listAdapter);

    }
    ArrayList<Cls_Sal_InvItems> contactList ;
    ArrayList<ContactListItems> PoList ;

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
            sqlHandler.executeQuery("Alter Table Area  Add  COLUMN  ManNo   text null" );
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
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  pay_method  text null" );
        }catch ( SQLException e){ }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  TempOrderItems    " +
                    " ( ID integer primary key autoincrement,ItemNo  text null, ItemNm  text null , Qty text null, Bounce text null  )");
        } catch (SQLException e) {
        }


        try{
            sqlHandler.executeQuery("Alter Table TempOrderItems  Add  COLUMN  Price  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table TempOrderItems  Add  COLUMN  tax  text null" );
        }catch ( SQLException e){ }

          try{
            sqlHandler.executeQuery("Alter Table TempOrderItems  Add  COLUMN  Type_No  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS  Monthly_Schedule ( no integer primary key autoincrement,Today_Date text null, Period_No text null,Area_No text null,User_No text null ,Posted text null ,TrMonth  text null , TrYear  text null ) ");
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table Monthly_Schedule  Add  COLUMN  TrMonth  text null" );
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table Monthly_Schedule  Add  COLUMN  TrYear  text null" );
        }catch ( SQLException e){ }




        try{
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Month_Dates ( no integer primary key autoincrement,TodayDate text null, Day_Nm_En text null , Day_No text null ,Day_Nm_Ar text null ,PeriodNo text null ,PeriodDesc text null" +
                    " ,Week_No text null ,CurrentMonth text null ,CurrentYear text null ,Day_No_Sort  text null , AreaNo text null  , Posted  text null  ) ");
        }catch ( SQLException e){ }



        try{
            sqlHandler.executeQuery("Alter Table Month_Dates  Add  COLUMN  Day_No_Sort  text null" );
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table Month_Dates  Add  COLUMN  AreaNo  text null" );
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table Month_Dates  Add  COLUMN  Posted  text null" );
        }catch ( SQLException e){ }



        try{
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  countryNo  text null" );
        }catch ( SQLException e){ }



        try{
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  countryNm  text null" );
        }catch ( SQLException e){ }

       try{
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  UpdateDate  text null" );
        }catch ( SQLException e){ }



        try{
            sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS deptf_Filter ( no integer primary key autoincrement,Type_No text null, Type_Name text null ,Search_Key text null ) ");
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS SERVER_DATETIME ( no integer primary key autoincrement,SERVERDATE text null, SERVERTIME text null ,MYEAR text null,MMONTH text null,MDAY text null " +
                                    " , MHOUR text null ,MMINUTE text null,MSECOND text null ) ");
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table CustStoreQtydetl  Add  COLUMN  Batch  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table CustStoreQtydetl  Add  COLUMN  ExpDate  text null" );
        }catch ( SQLException e){ }

        try{
            sqlHandler.executeQuery("Alter Table SERVER_DATETIME  Add  COLUMN  DAYWEEK  text null" );
        }catch ( SQLException e){ }



        try{
            sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS TempCustQty ( no integer primary key autoincrement,ItemNo text null, Qty text null ,OrderNo text null,CustNo text null) ");
        }catch ( SQLException e){ }


        try{
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  SampleItems  text null" );
        }catch ( SQLException e){ }


          try{
            sqlHandler.executeQuery("Alter Table TempOrderItems  Add  COLUMN  Pack  text null" );
        }catch ( SQLException e){ }




        sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS CustLocaltions ( no integer primary key autoincrement,CustNo text null , ManNo text null , Lat_X text null ,Lat_Y text null,Locat text null,Post INTEGER null " +
                " , Note Text null , Tr_Date text null , PersonNm  text null , MobileNo text null ,Stutes text null   ) ");



        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ACC_REPORT    " +
                    "( ID integer primary key autoincrement,Cust_No  text null,Cust_Nm  text null ,FDate text null,TDate text null , TrDate text null ," +
                    "  Tot  text null    , Rate  text null , Cred text null , Dept text null , Bb text null , Des text null , Date text null" +
                    " , Cur_no text null , Doctype text null , Doc_num text null , CheqBal text null  , Ball text null, CusTop text null, NetBall text null" +
                    " , Notes text null )");
        } catch (SQLException e) {
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List_Result   = new ArrayList<Cls_UpdateData>();
        setContentView(R.layout.n_view_update_data_to_mobile);
        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();


        Lv_Result = (ListView)findViewById(R.id.LvResult);
        List_Result.clear();
        Lv_Result.setAdapter(null);
        sqlHandler = new SqlHandler(this);

        ChecklList = new ArrayList<Cls_Check>();
        ChecklList.clear();

        LinearLayout mainLayout=(LinearLayout)this.findViewById(R.id.phLayout1);
        LinearLayout mainLayout2=(LinearLayout)this.findViewById(R.id.phLayout2);


        TextView process =  (TextView) findViewById(R.id.process);
        TextView getdata =  (TextView) findViewById(R.id.getdata);
        TextView moves =  (TextView) findViewById(R.id.moves);
        TextView updatetext =  (TextView) findViewById(R.id.updatetext);
        TextView updatess = (TextView) findViewById(R.id.textView263);

        process.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        getdata.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));

        moves.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));

        updatetext.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        updatess.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));






        final CheckBox chk_cust = (CheckBox) findViewById(R.id.Chk_Custs);
        final CheckBox chk_banks = (CheckBox) findViewById(R.id.Chk_Banks);
        final CheckBox chk_Items = (CheckBox) findViewById(R.id.Chk_Items);
        final CheckBox chk_Unites = (CheckBox) findViewById(R.id.Chk_Unites);
        final CheckBox Chk_Items_Unites = (CheckBox) findViewById(R.id.Chk_Items_Unites);
        final CheckBox Chk_Curf = (CheckBox) findViewById(R.id.Chk_Curf);
        final CheckBox Chk_deptf = (CheckBox) findViewById(R.id.Chk_deptf);
        final CheckBox Chk_Users = (CheckBox) findViewById(R.id.Chk_Users);
        final CheckBox Chk_TransQty = (CheckBox) findViewById(R.id.Chk_TransQty);

        final CheckBox chkCompany = (CheckBox) findViewById(R.id.chkCompany);
        final CheckBox chkCashCust = (CheckBox) findViewById(R.id.chkCashCust);
        final CheckBox chk_Item_cat = (CheckBox) findViewById(R.id.chk_Item_cat);
        final CheckBox chk_LastPrice = (CheckBox) findViewById(R.id.chk_LastPrice);
        final CheckBox Chk_Locat = (CheckBox) findViewById(R.id.Chk_Locat);
        final CheckBox Chk_Doctors = (CheckBox) findViewById(R.id.Chk_Doctors);
        final CheckBox Chk_Post_Inv = (CheckBox) findViewById(R.id.Chk_Post_Inv);
        final CheckBox Chk_Vistis = (CheckBox) findViewById(R.id.Chk_Vistis);
        final CheckBox Chk_Post_Payments = (CheckBox) findViewById(R.id.Chk_Post_Payments);
        final CheckBox chk_po_post = (CheckBox) findViewById(R.id.chk_po_post);

        final CheckBox chk_Spec = (CheckBox) findViewById(R.id.chk_Spec);
        final CheckBox chk_Doctor_Visit_post = (CheckBox) findViewById(R.id.chk_Doctor_Visit_post);
        final CheckBox chk_ServerDateTIME = (CheckBox) findViewById(R.id.chk_ServerDateTime);

        chk_cust.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chk_banks.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chk_Items.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chk_Unites.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_Items_Unites.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_Curf.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_deptf.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_Users.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_TransQty.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));

        chkCompany.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chkCashCust.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chk_Item_cat.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chk_LastPrice.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_Locat.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_Doctors.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_Post_Inv.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_Vistis.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        Chk_Post_Payments.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chk_po_post.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));

        chk_Spec.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chk_Doctor_Visit_post.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));
        chk_ServerDateTIME.setTypeface(MethodToUse.SetTFace(UpdateDataToMobileActivity.this));


        chk_cust.setChecked(true);
        chk_banks.setChecked(true);
        chk_Items.setChecked(true);
        chk_Unites.setChecked(true);
        Chk_Items_Unites.setChecked(true);
        Chk_Curf.setChecked(true);
        Chk_deptf.setChecked(true);
        Chk_deptf.setChecked(true);
        Chk_Users.setChecked(true);
        Chk_TransQty.setChecked(true);

        chkCompany.setChecked(true);
        chkCashCust.setChecked(false);
        chk_Item_cat.setChecked(false);
        chk_LastPrice.setChecked(true);
        Chk_Post_Inv.setChecked(false);
        Chk_Vistis.setChecked(true);
        Chk_Post_Payments.setChecked(true);
        chk_po_post.setChecked(true);

        Chk_Locat.setChecked(true);
        Chk_Doctors.setChecked(true);
        chk_Spec.setChecked(false);
        chk_ServerDateTIME.setChecked(true);
        chk_ServerDateTIME.setEnabled(false);


        tv = new TextView(getApplicationContext());
        lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, // Width of TextView
                LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tv.setText("");
        tv.setTextColor(Color.parseColor("#FF808562"));
        tv.setBackgroundColor(Color.parseColor("#dee5ae"));
        greenProgressbar = this.getResources().getDrawable(R.drawable.progrees_states);

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  DB_VERVSION    " +
                    "( ID integer primary key autoincrement,No text null)");


            final int DB_VERVSION = Integer.parseInt(DB.GetValue(UpdateDataToMobileActivity.this, "DB_VERVSION", "No", "5=5"));

            q = "DELETE  FROM  DB_VERVSION  ";
            sqlHandler.executeQuery(q);

            q = "INSERT INTO DB_VERVSION(No) values ('"+PHRLASTUPDATE+"')";
              sqlHandler.executeQuery(q);



            if (DB_VERVSION < PHRLASTUPDATE)
                DataBaseChanges();

        }  catch (Exception ex){
            WriteTxtFile.MakeText("DB_VERVSION < LASTUPDATE",ex.getMessage().toString());
        }
      //  DataBaseChanges();
    }
    private void Get_Tab_Password(){


        new   Thread(new Runnable() {
            @Override
            public void run () {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.GetTab_Password();
                try {

                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_PassNo   = js.getJSONArray("PassNo");
                    JSONArray js_PassDesc = js.getJSONArray("PassDesc");
                    JSONArray js_Password = js.getJSONArray("Password");



                    q = "Delete from Tab_Password";
                    sqlHandler.executeQuery(q);

                    q = " delete from sqlite_sequence where name='Tab_Password'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_PassNo.length(); i++) {

                        q = "INSERT INTO Tab_Password(PassNo, PassDesc , Password ) values ('"
                                +       js_PassNo.get(i).toString()
                                + "','" + js_PassDesc.get(i).toString()
                                + "','" + js_Password.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                    }

                } catch (final Exception e) {


                }

            }
        }).start();

    }
    private void GetManPermession(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "-1");


        if(UserID=="-1") {
            return;
        }



        new   Thread(new Runnable() {
            @Override
            public void run () {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.GetGetManPermession(UserID);
                try {

                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_User_ID= js.getJSONArray("User_ID");
                    JSONArray js_APP_Code= js.getJSONArray("APP_Code");
                    JSONArray js_SCR_Code= js.getJSONArray("SCR_Code");
                    JSONArray js_Branch_Code= js.getJSONArray("Branch_Code");
                    JSONArray js_SCR_Action= js.getJSONArray("SCR_Action");
                    JSONArray js_Permession= js.getJSONArray("Permession");

                    q = "Delete from ManPermession";
                    sqlHandler.executeQuery(q);


                    q = " delete from sqlite_sequence where name='ManPermession'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_User_ID.length(); i++) {

                        q = "INSERT INTO ManPermession(User_ID, APP_Code , SCR_Code , Branch_Code, SCR_Action, Permession) values ('"
                                +       js_User_ID.get(i).toString()
                                + "','" + js_APP_Code.get(i).toString()
                                + "','" + js_SCR_Code.get(i).toString()
                                + "','" + js_Branch_Code.get(i).toString()
                                + "','" + js_SCR_Action.get(i).toString()
                                + "','" + js_Permession.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                    }

                } catch (final Exception e) {


                }

            }
        }).start();

    }
    public void btn_Transfer_Data(View view) {
        List_Result.clear();
        Lv_Result.setAdapter(null);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("CustNo", "");
        //editor.putString("CustNm", "");
        //editor.putString("CustAdd", "");
/*
        editor.putString("CompanyID","1");
        editor.putString("CompanyNm", " مجموعة المجرة الدولية ");
        editor.putString("TaxAcc1", "123456");
        editor.putString("Address","عمان  - الاردن");
        editor.putString("Notes", "");

        editor.commit();
*/




        final Handler _handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.GetcompanyInfo();
                try {

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID = js.getJSONArray("ID");
                    _handler.post(new Runnable() {
                        public void run() {
                            System.out.println("Yes Internet");
                            Do_Trans_From_Server();
                        }
                    });

                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                            System.out.println("No Internet");
                            AlertDialog alertDialog = new AlertDialog.Builder(UpdateDataToMobileActivity.this).create();
                            alertDialog = new AlertDialog.Builder(
                                    UpdateDataToMobileActivity.this).create();
                            alertDialog.setTitle(getResources().getText(R.string.UpdatData));
                            alertDialog.setMessage(getResources().getText(R.string.ConnectError));
                            alertDialog.setIcon(R.drawable.delete);
                            alertDialog.setButton(getResources().getText(R.string.Ok    ), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                            alertDialog.show();

                        }
                    });
                }
            }
        }).start();






    }
    public  void Do_Trans_From_Server (){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String query;
        final CheckBox chk_cust = (CheckBox) findViewById(R.id.Chk_Custs);
        final CheckBox chk_banks = (CheckBox) findViewById(R.id.Chk_Banks);
        final CheckBox chk_Items = (CheckBox) findViewById(R.id.Chk_Items);
        final CheckBox chk_Unites = (CheckBox) findViewById(R.id.Chk_Unites);
        final CheckBox Chk_Items_Unites = (CheckBox) findViewById(R.id.Chk_Items_Unites);
        final CheckBox Chk_Curf = (CheckBox) findViewById(R.id.Chk_Curf);
        final CheckBox Chk_deptf = (CheckBox) findViewById(R.id.Chk_deptf);
        final CheckBox Chk_Users = (CheckBox) findViewById(R.id.Chk_Users);
        final CheckBox Chk_TransQty = (CheckBox) findViewById(R.id.Chk_TransQty);

        final CheckBox chkCompany = (CheckBox) findViewById(R.id.chkCompany);
        final CheckBox chkCashCust = (CheckBox) findViewById(R.id.chkCashCust);
        final CheckBox chk_Item_cat = (CheckBox) findViewById(R.id.chk_Item_cat);
        final CheckBox chk_LastPrice = (CheckBox) findViewById(R.id.chk_LastPrice);

        final CheckBox Chk_Locat = (CheckBox) findViewById(R.id.Chk_Locat);
        final CheckBox Chk_Doctors = (CheckBox) findViewById(R.id.Chk_Doctors);
        final CheckBox chk_Spec = (CheckBox) findViewById(R.id.chk_Spec);
        final CheckBox chk_ServerDateTime = (CheckBox) findViewById(R.id.chk_ServerDateTime);


        if (chk_ServerDateTime.isChecked()) {
            final String UserID = sharedPreferences.getString("UserID", "");
            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("التاريخ");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Get_ServerDateTime();
                    try {
                        Integer i;
                        String q = "";
                        if(We_Result.ID>0) {
                            q = "Delete from SERVER_DATETIME";
                            sqlHandler.executeQuery(q);
                            q = " delete from sqlite_sequence where name='SERVER_DATETIME'";
                            sqlHandler.executeQuery(q);

                            JSONObject js = new JSONObject(We_Result.Msg);

                            String js_SERVERDATE = js.getString("SERVERDATE");
                            String js_SERVERTIME = js.getString("SERVERTIME");
                            String js_MYEAR = js.getString("MYEAR");
                            String js_MMONTH = js.getString("MMONTH");
                            String js_MDAY = js.getString("MDAY");
                            String js_MHOUR = js.getString("MHOUR");
                            String js_MMINUTE = js.getString("MMINUTE");
                            String js_MSECOND = js.getString("MSECOND");
                            String js_DAYWEEK = js.getString("DAYWEEK");

                            q = "INSERT INTO SERVER_DATETIME(SERVERDATE ,SERVERTIME,MYEAR, MMONTH, MDAY,MHOUR,MMINUTE,MSECOND,DAYWEEK ) values ('"
                                    + js_SERVERDATE
                                    + "','" + js_SERVERTIME
                                    + "','" + js_MYEAR
                                    + "','" + js_MMONTH
                                    + "','" + js_MDAY
                                    + "','" + js_MHOUR
                                    + "','" + js_MMINUTE
                                    + "','" + js_MSECOND
                                    + "','" + js_DAYWEEK
                                    + "')";
                            sqlHandler.executeQuery(q);

                            _handler.post(new Runnable() {
                                public void run() {
                                    filllist("التاريخ", 1, 1);
                                    chk_ServerDateTime.setChecked(false);
                                    progressDialog.dismiss();
                                    Do_Trans_From_Server();
                                }
                            });
                        }else{
                            _handler.post(new Runnable() {
                                public void run() {
                                    filllist("التاريخ", 0, 0);
                                    chk_ServerDateTime.setChecked(false);
                                    Do_Trans_From_Server();
                                }
                            });

                        }


                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("التاريخ", 0, 0);
                                chk_ServerDateTime.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }
        else  if(chk_Spec.isChecked()) {
            final String UserID = sharedPreferences.getString("UserID", "");
            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("الاختصاص");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetSpecialization();
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_No = js.getJSONArray("No");
                        JSONArray js_Aname = js.getJSONArray("Aname");
                        JSONArray js_Ename = js.getJSONArray("Ename");


                        //  Specialization  No  Aname Ename

                        q = "Delete from Specialization";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='Specialization'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_No.length(); i++)  {
                            q = "INSERT INTO Specialization(No,Aname,Ename) values ('"
                                    + js_No.get(i).toString()
                                    + "','" + js_Aname.get(i).toString()
                                    + "','" + js_Ename.get(i).toString()  + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_No.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("الاختصاص", 1, total);
                                chk_Spec.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("الاختصاص", 0, 0);
                                chk_Spec.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }


        else if (Chk_Doctors.isChecked()) {


            SimpleDateFormat MonthFormt = new SimpleDateFormat("MM", Locale.ENGLISH);
            final String currentMonth = MonthFormt.format(new Date());




            SimpleDateFormat YearFormt = new SimpleDateFormat("yyyy", Locale.ENGLISH);
           final String currentYear = YearFormt.format(new Date());






            final String UserID = sharedPreferences.getString("UserID", "");
            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("الخطة الشهريه");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetMonth_Dates(UserID,currentMonth, currentYear);
                  //  ws.GetDoctors(UserID);
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_TodayDate = js.getJSONArray("TodayDate");
                        JSONArray js_Day_Nm_En = js.getJSONArray("Day_Nm_En");
                        JSONArray js_Day_No = js.getJSONArray("Day_No");
                        JSONArray js_Day_Nm_Ar = js.getJSONArray("Day_Nm_Ar");
                        JSONArray js_PeriodNo = js.getJSONArray("PeriodNo");
                        JSONArray js_PeriodDesc = js.getJSONArray("PeriodDesc");
                        JSONArray js_Week_No = js.getJSONArray("Week_No");
                        JSONArray js_CurrentMonth = js.getJSONArray("CurrentMonth");
                        JSONArray js_CurrentYear = js.getJSONArray("CurrentYear");
                        JSONArray js_Day_No_Sort = js.getJSONArray("Day_No_Sort");
                        JSONArray js_AreaNo = js.getJSONArray("AreaNo");
                        JSONArray js_Posted = js.getJSONArray("Posted");

                        q = "Delete from Month_Dates";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='Month_Dates'";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='Month_Dates'";
                        sqlHandler.executeQuery(q);
                        for (i = 0; i < js_TodayDate.length(); i++) {
                            q = "INSERT INTO Month_Dates (  TodayDate,Day_Nm_En,Day_No,Day_Nm_Ar,PeriodNo,PeriodDesc,Week_No,CurrentMonth,CurrentYear,Day_No_Sort,AreaNo,Posted ) values ('"
                                    + js_TodayDate.get(i).toString()
                                    + "','" + js_Day_Nm_En.get(i).toString()
                                    + "','" + js_Day_No.get(i).toString()
                                    + "','" + js_Day_Nm_Ar.get(i).toString()
                                    + "','" + js_PeriodNo.get(i).toString()
                                    + "','" + js_PeriodDesc.get(i).toString()
                                    + "','" + js_Week_No.get(i).toString()
                                    + "','" + js_CurrentMonth.get(i).toString()
                                    + "','" + js_CurrentYear.get(i).toString()
                                    + "','" + js_Day_No_Sort.get(i).toString()
                                    + "','" + js_AreaNo.get(i).toString()
                                    + "','" + js_Posted.get(i).toString()
                                    + "' )";
                            sqlHandler.executeQuery(q);

                            progressDialog.setMax(js_TodayDate.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {

                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("الخطة الشهرية", 1, total);
                                Chk_Doctors.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("الخطة الشهرية", 0, 0);
                                Chk_Doctors.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }

        else if (Chk_Locat.isChecked()) {

            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("  المناطق");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetAreas();
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_No = js.getJSONArray("No");
                        JSONArray js_Name = js.getJSONArray("Name");
                        JSONArray js_Ename = js.getJSONArray("Ename");
                        JSONArray js_City = js.getJSONArray("City");
                        JSONArray js_Country = js.getJSONArray("Country");
                        JSONArray js_ManNo = js.getJSONArray("ManNo");




                        q = "Delete from Area";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='Area'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_No.length(); i++) {
                            q = "INSERT INTO Area(No,Name,Ename,City,Country,ManNo) values ('"
                                    + js_No.get(i).toString()
                                    + "','" + js_Name.get(i).toString()
                                    + "','" + js_Ename.get(i).toString()
                                    + "','" + js_City.get(i).toString()
                                    + "','" + js_Country.get(i).toString()
                                    + "','" + js_ManNo.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_No.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("لمناطق", 1, total);
                                Chk_Locat.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("المناطق", 0, 0);
                                Chk_Locat.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }


        else if (chk_banks.isChecked()) {
            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("  البنـــوك");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetAccNoBanks();
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Bank = js.getJSONArray("Bank");
                        JSONArray js_BEName = js.getJSONArray("BEName");
                        JSONArray js_bank_num = js.getJSONArray("bank_num");
                        JSONArray js_Accno = js.getJSONArray("Accno");
                        JSONArray js_CCntrNo = js.getJSONArray("CCntrNo");

                        q = "Delete from banks";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='banks'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_Bank.length(); i++) {
                            q = "INSERT INTO banks(Bank,BEName,bank_num,Accno,CCntrNo) values ('"
                                    + js_Bank.get(i).toString()
                                    + "','" + js_BEName.get(i).toString()
                                    + "','" + js_bank_num.get(i).toString()
                                    + "','" + js_Accno.get(i).toString()
                                    + "','" + js_CCntrNo.get(i).toString() + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_Bank.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("البنوك", 1, total);
                                chk_banks.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("البنوك" , 0,0  );
                                chk_banks.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }
        else if (chk_cust.isChecked()) {
            final Handler _handler = new Handler();


            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final String UserID = sharedPreferences.getString("UserID", "");
            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setMessage( " الرحاء الانتظار ... "+ "العمل جاري على نسخ البيانات");
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            tv.setText(" العمــلاء");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetCustomers(UserID);
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_no = js.getJSONArray("no");
                        JSONArray js_name = js.getJSONArray("name");
                        JSONArray js_Ename = js.getJSONArray("Ename");
                        JSONArray js_barCode = js.getJSONArray("barCode");
                        JSONArray js_Address = js.getJSONArray("Address");
                        JSONArray js_State = js.getJSONArray("State");
                        JSONArray js_SMan = js.getJSONArray("SMan");
                        JSONArray js_Latitude = js.getJSONArray("Latitude");
                        JSONArray js_Longitude = js.getJSONArray("Longitude");
                        JSONArray js_Note2 = js.getJSONArray("Note2");
                        JSONArray js_sat = js.getJSONArray("sat");
                        JSONArray js_sun = js.getJSONArray("sun");
                        JSONArray js_mon = js.getJSONArray("mon");
                        JSONArray js_tues = js.getJSONArray("tues");
                        JSONArray js_wens = js.getJSONArray("wens");
                        JSONArray js_thurs = js.getJSONArray("thurs");
                        JSONArray js_sat1 = js.getJSONArray("sat1");
                        JSONArray js_sun1 = js.getJSONArray("sun1");
                        JSONArray js_mon1 = js.getJSONArray("mon1");
                        JSONArray js_tues1 = js.getJSONArray("tues1");
                        JSONArray js_wens1 = js.getJSONArray("wens1");
                        JSONArray js_thurs1 = js.getJSONArray("thurs1");
                        JSONArray js_Celing = js.getJSONArray("Celing");
                        JSONArray js_CatNo = js.getJSONArray("CatNo");
                        JSONArray js_CustType = js.getJSONArray("CustType");
                        JSONArray js_PAMENT_PERIOD_NO = js.getJSONArray("PAMENT_PERIOD_NO");
                        JSONArray js_CUST_PRV_MONTH = js.getJSONArray("CUST_PRV_MONTH");
                        JSONArray js_CUST_NET_BAL = js.getJSONArray("CUST_NET_BAL");
                        JSONArray js_Pay_How = js.getJSONArray("Pay_How");
                        JSONArray js_Cust_type = js.getJSONArray("Cust_type");
                        JSONArray js_countryNm = js.getJSONArray("countryNm");
                        JSONArray js_countryNo = js.getJSONArray("countryNo");
                        JSONArray js_UpdateDate = js.getJSONArray("UpdateDate");

                        q = "Delete from Customers";
                        sqlHandler.executeQuery(q);
                        q = "delete from sqlite_sequence where name='Customers'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_no.length(); i++) {
                            q = "Insert INTO Customers(no,name,Ename,barCode,Address,State,SMan,Latitude,Longitude,Note2,sat " +
                                    " ,sun,mon,tues,wens,thurs,sat1,sun1,mon1,tues1,wens1,thurs1 , Celing , CatNo " +
                                    ",CustType,PAMENT_PERIOD_NO , CUST_PRV_MONTH,CUST_NET_BAL,Pay_How,Cust_type,countryNm,countryNo,UpdateDate) values ('"
                                    + js_no.get(i).toString()
                                    + "','" + js_name.get(i).toString()
                                    + "','" + js_Ename.get(i).toString()
                                    + "','" + js_barCode.get(i).toString()
                                    + "','" + js_Address.get(i).toString()
                                    + "','" + js_State.get(i).toString()
                                    + "','" + js_SMan.get(i).toString()
                                    + "','" + js_Latitude.get(i).toString()
                                    + "','" + js_Longitude.get(i).toString()
                                    + "','" + js_Note2.get(i).toString()
                                    + "'," + js_sat.get(i).toString()
                                    + "," + js_sun.get(i).toString()
                                    + "," + js_mon.get(i).toString()
                                    + "," + js_tues.get(i).toString()
                                    + "," + js_wens.get(i).toString()
                                    + "," + js_thurs.get(i).toString()
                                    + ","  + js_sat1.get(i).toString()
                                    + ","  + js_sun1.get(i).toString()
                                    + ","  + js_mon1.get(i).toString()
                                    + ","  + js_tues1.get(i).toString()
                                    + ","  + js_wens1.get(i).toString()
                                    + ","  + js_thurs1.get(i).toString()
                                    + ",'" + js_Celing.get(i).toString()
                                    + "','"+ js_CatNo.get(i).toString()
                                    + "','"+ js_CustType.get(i).toString()
                                    + "','"+ js_PAMENT_PERIOD_NO.get(i).toString()
                                    + "','"+ js_CUST_PRV_MONTH.get(i).toString()
                                    + "','"+ js_CUST_NET_BAL.get(i).toString()
                                    + "','"+ js_Pay_How.get(i).toString()
                                    + "','"+ js_Cust_type.get(i).toString()
                                    + "','"+ js_countryNm.get(i).toString()
                                    + "','"+ js_countryNo.get(i).toString()
                                    + "','"+ js_UpdateDate.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);

                            custDialog.setMax(js_no.length());
                            custDialog.incrementProgressBy(1);

                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {

                                filllist("العملاء" , 1 ,    total );
                                chk_cust.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("العملاء" ,0 ,    0 );
                                chk_cust.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        }


        else if (chk_Items.isChecked()) {

            tv = new TextView(getApplicationContext());
            tv.setText("المــــــــواد");
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();

            final Handler _handler = new Handler();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetItems();
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray Item_No = js.getJSONArray("Item_No");
                        JSONArray Item_Name = js.getJSONArray("Item_Name");
                        JSONArray Ename = js.getJSONArray("Ename");
                        JSONArray js_Ename = js.getJSONArray("Ename");
                        JSONArray Unit = js.getJSONArray("Unit");
                        JSONArray Price = js.getJSONArray("Price");
                        JSONArray OL = js.getJSONArray("OL");
                        JSONArray OQ1 = js.getJSONArray("OQ1");
                        JSONArray Type_No = js.getJSONArray("Type_No");
                        JSONArray Pack = js.getJSONArray("Pack");
                        JSONArray Place = js.getJSONArray("Place");
                        JSONArray dno = js.getJSONArray("dno");
                        JSONArray tax = js.getJSONArray("tax");

                        q = "Delete from invf";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='invf'";
                        sqlHandler.executeQuery(q);


                        for (i = 0; i < Item_No.length(); i++) {
                            q = "Insert INTO invf(Item_No,Item_Name,Ename,Unit,Price,OL,OQ1,Type_No,Pack,Place,dno,tax) values ('"
                                    + Item_No.get(i).toString()
                                    + "','" + Item_Name.get(i).toString()
                                    + "','" + Ename.get(i).toString()
                                    + "','" + Unit.get(i).toString()
                                    + "','" + Price.get(i).toString()
                                    + "','" + OL.get(i).toString()
                                    + "','" + OQ1.get(i).toString()
                                    + "','" + Type_No.get(i).toString()
                                    + "','" + "0"//    + "','" + Pack.get(i).toString()
                                    + "','" + Place.get(i).toString()
                                    + "','" + dno.get(i).toString()
                                    + "','" + tax.get(i).toString()
                                    + "')";
                            try{
                                sqlHandler.executeQuery(q);
                            }catch (final Exception ex){
                                _handler.post(new Runnable() {
                                    public void run() {
                                        filllist(ex.toString(), 0, 0);
                                        chk_Items.setChecked(false);
                                        Do_Trans_From_Server();

                                    }
                                });
                            }

                            progressDialog.setMax(Item_No.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                chk_Items.setChecked(false);
                                filllist("المواد" , 1 ,    total );
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("المواد" , 0 ,    0 );
                                chk_Items.setChecked(false);
                                Do_Trans_From_Server();

                            }
                        });
                    }
                }
            }).start();
        }


        else if (chk_Unites.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            final ProgressDialog progressDialog;
            final Handler _handler = new Handler();
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setMessage(" الرجاء الانتظار ..."+ "  العمل جاري على نسخ البيانات");
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            tv.setText(" الوحـــــدات");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetUnites();
                    try {
                        Integer i;
                        Integer count =0;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray Unitno = js.getJSONArray("Unitno");
                        JSONArray UnitName = js.getJSONArray("UnitName");
                        JSONArray UnitEname = js.getJSONArray("UnitEname");

                        q = "Delete from Unites";
                        sqlHandler.executeQuery(q);

                        q = "delete from sqlite_sequence where name='Unites'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < Unitno.length(); i++) {
                            q = "Insert INTO Unites(Unitno,UnitName,UnitEname) values ('"
                                    + Unitno.get(i).toString()
                                    + "','" + UnitName.get(i).toString()
                                    + "','" + UnitEname.get(i).toString()
                                    + "')";
                            try{
                                sqlHandler.executeQuery(q);

                                System.out.println("Unites ");

                            }
                            catch ( SQLException ex) {

                                System.out.println(    "Unites " +ex.getMessage());
                            }
                            progressDialog.setMax(Unitno.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }

                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                String q = "DELETE  FROM Unites WHERE no NOT IN (SELECT MAX(no) FROM Unites GROUP BY Unitno )";
                                sqlHandler.executeQuery(q);
                                filllist("الوحدات", 1, total);
                                chk_Unites.setChecked(false);
                                Do_Trans_From_Server();

                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("الوحدات", 0, 0);
                                chk_Unites.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        }

        else   if (Chk_Items_Unites.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            final Handler _handler = new Handler();
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setMessage(" الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات");
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            tv.setText(" وحــــدات بيـــع المــواد");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetUnitItems();
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray item_no = js.getJSONArray("item_no");
                        JSONArray barcode = js.getJSONArray("barcode");
                        JSONArray unitno = js.getJSONArray("unitno");
                        JSONArray Operand = js.getJSONArray("Operand");
                        JSONArray price = js.getJSONArray("price");
                        JSONArray Max = js.getJSONArray("Max");
                        JSONArray Min = js.getJSONArray("Min");
                        JSONArray posprice = js.getJSONArray("posprice");

                        q = "Delete from UnitItems";
                        sqlHandler.executeQuery(q);

                        q = "delete from sqlite_sequence where name='UnitItems'";
                        sqlHandler.executeQuery(q);
                        for (i = 0; i < item_no.length(); i++) {
                            q = "Insert INTO UnitItems(item_no,barcode,unitno,Operand,price,Max,Min,posprice) values ('"
                                    + item_no.get(i).toString()
                                    + "','" + barcode.get(i).toString()
                                    + "','" + unitno.get(i).toString()
                                    + "','" + Operand.get(i).toString()
                                    + "','" + price.get(i).toString()
                                    + "','" + Max.get(i).toString()
                                    + "','" + Min.get(i).toString()
                                    + "','" + posprice.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(item_no.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist("معاملات النحويل للمواد", 1, total);
                                Chk_Items_Unites.setChecked(false);
                                Do_Trans_From_Server();

                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("معاملات النحويل للمواد" , 0 ,    0 );
                                Chk_Items_Unites.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        }


        else if (Chk_Curf.isChecked()) {


            final ProgressDialog progressDialog;
            final Handler _handler = new Handler();
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..."+ "  العمل جاري على نسخ البيانات  ");
            tv.setText(" العمــــلات");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);

            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Getcurf();
                    try {

                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray cur_no = js.getJSONArray("cur_no");
                        JSONArray cur = js.getJSONArray("cur");
                        JSONArray ename = js.getJSONArray("ename");
                        JSONArray dec = js.getJSONArray("dec");
                        JSONArray lrate = js.getJSONArray("lrate");


                        q = "Delete from curf";
                        sqlHandler.executeQuery(q);

                        q = "delete from sqlite_sequence where name='curf' ";
                        sqlHandler.executeQuery(q);


                        for (i = 0; i < cur_no.length(); i++) {
                            q = "Insert INTO curf(cur_no,cur,ename,dec,lrate) values ('"
                                    + cur_no.get(i).toString()
                                    + "','" + cur.get(i).toString()
                                    + "','" + ename.get(i).toString()
                                    + "','" + dec.get(i).toString()
                                    + "','" + lrate.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(cur_no.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist(" العملات ", 1, total);
                                Chk_Curf.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist(" العملات ", 0 ,    0 );
                                Chk_Curf.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        }


        else if (Chk_deptf.isChecked()) {
           UpdateDeptFilter();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            final Handler _handler = new Handler();
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("اصناف الـــمواد");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);

            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.deptf();
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray Type_No = js.getJSONArray("Type_No");
                        JSONArray Type_Name = js.getJSONArray("Type_Name");
                        JSONArray etname = js.getJSONArray("etname");
                        JSONArray route = js.getJSONArray("route");

                        q = "Delete from deptf";
                        sqlHandler.executeQuery(q);

                        q = " delete from sqlite_sequence where name='deptf'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < Type_No.length(); i++) {
                            q = "Insert INTO deptf(Type_No,Type_Name,etname,route) values ('"
                                    + Type_No.get(i).toString()
                                    + "','" + Type_Name.get(i).toString()
                                    + "','" + etname.get(i).toString()
                                    + "','" + route.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(Type_No.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist(" اصناف المواد ", 1, total);
                                Chk_deptf.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist(" اصناف المواد ", 0, 0);
                                Chk_deptf.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        }



        else if (Chk_Users.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            final Handler _handler = new Handler();
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("المستـــــــخدمين");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            GetManPermession();
            Get_Tab_Password();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetManf();
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_man= js.getJSONArray("man");
                        JSONArray js_name= js.getJSONArray("name");
                        JSONArray js_MEName= js.getJSONArray("MEName");
                        JSONArray js_StoreNo= js.getJSONArray("StoreNo");
                        JSONArray js_Stoped= js.getJSONArray("Stoped");
                        JSONArray js_SupNo= js.getJSONArray("SupNo");
                        JSONArray js_UserName= js.getJSONArray("UserName");
                        JSONArray js_Password= js.getJSONArray("Password");
                        JSONArray js_MobileNo= js.getJSONArray("MobileNo");
                        JSONArray js_MobileNo2= js.getJSONArray("MobileNo2");
                        JSONArray js_MANTYPE= js.getJSONArray("MANTYPE");
                        JSONArray js_TypeDesc= js.getJSONArray("TypeDesc");
                        JSONArray js_SampleSerial= js.getJSONArray("SampleSerial");
                        JSONArray js_Sname= js.getJSONArray("Sname");


                        q = "Delete from manf";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='manf'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_man.length(); i++) {
                            q = "Insert INTO manf(man,name,MEName,username,password,StoreNo,Stoped,SupNo,MobileNo,MobileNo2,MANTYPE,TypeDesc ,SampleSerial,Serial_name) values ("
                                    +         js_man.get(i).toString()
                                    + ",'" + js_name.get(i).toString()
                                    + "','" + js_MEName.get(i).toString()
                                    + "','" + js_UserName.get(i).toString()
                                    + "','" + js_Password.get(i).toString()
                                    + "','" + js_StoreNo.get(i).toString()
                                    + "'," + js_Stoped.get(i).toString()
                                    + "," + js_SupNo.get(i).toString()
                                    + ",'" + js_MobileNo.get(i).toString()
                                    + "','" + js_MobileNo2.get(i).toString()
                                    + "','" + js_MANTYPE.get(i).toString()
                                    + "','" + js_TypeDesc.get(i).toString()
                                    + "','" + js_SampleSerial.get(i).toString()
                                    + "','" + js_Sname.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_man.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {

                                filllist("المستخدمين ", 1 ,    total );
                                Chk_Users.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist("المستخدمين ", 0, 0);
                                progressDialog.dismiss();
                                Chk_Users.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        }

        else if (Chk_TransQty.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final Handler _handler = new Handler();

            final String UserID = sharedPreferences.getString("UserID", "");
            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("سنـــد التزويــــد");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();
            String MaxSeer = "1";
           /* query = "SELECT  COALESCE(MAX(ser), 0) +1 AS no from ManStore";
             Cursor c1 = sqlHandler.selectQuery(query);
            String Operand = "0";
            Double Order_qty = 0.0;
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    MaxSeer = String.valueOf(c1.getInt(0));
                }
                c1.close();
            }*/


            final String Ser = "1";
            q = "Delete from ManStore";
            sqlHandler.executeQuery(q);
            q = "delete from sqlite_sequence where name='ManStore'";
            sqlHandler.executeQuery(q);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.TrnsferQtyFromMobile(UserID, "0", "");
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_date = js.getJSONArray("date");
                        JSONArray js_fromstore = js.getJSONArray("fromstore");
                        JSONArray js_tostore = js.getJSONArray("tostore");
                        JSONArray js_des = js.getJSONArray("des");
                        JSONArray js_docno = js.getJSONArray("docno");
                        JSONArray js_itemno = js.getJSONArray("itemno");
                        JSONArray js_qty = js.getJSONArray("qty");
                        JSONArray js_UnitNo = js.getJSONArray("UnitNo");
                        JSONArray js_UnitRate = js.getJSONArray("UnitRate");
                        JSONArray js_myear = js.getJSONArray("myear");
                        JSONArray js_StoreName = js.getJSONArray("StoreName");
                        JSONArray js_RetailPrice = js.getJSONArray("RetailPrice");



                        for (i = 0; i < js_docno.length(); i++) {
                            q = "Insert INTO ManStore(SManNo,date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear,RetailPrice ,StoreName ,ser) values ("
                                    + UserID.toString()
                                    + ",'" + js_date.get(i).toString()
                                    + "','" + js_fromstore.get(i).toString()
                                    + "','" + js_tostore.get(i).toString()
                                    + "','" + js_des.get(i).toString()
                                    + "','" + js_docno.get(i).toString()
                                    + "','" + js_itemno.get(i).toString()
                                    + "','" + js_qty.get(i).toString()
                                    + "','" + js_UnitNo.get(i).toString()
                                    + "','" + js_UnitRate.get(i).toString()
                                    + "','" + js_myear.get(i).toString()
                                    + "','" + js_RetailPrice.get(i).toString()
                                    + "','" + js_StoreName.get(i).toString()
                                    + "'," + Ser.toString()
                                    + " )";
                            sqlHandler.executeQuery(q);
                            custDialog.setMax(js_docno.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {

                                filllist("كميات المستودع ", 1 ,total );
                                Chk_TransQty.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                custDialog.dismiss();
                                filllist("كميات المستودع ", 0, 0);
                                Chk_TransQty.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        }

        else if (chkCompany.isChecked()) {
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final Handler _handler = new Handler();
            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);


            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setIndeterminate(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("معلــومــات الـمؤسـسة");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetcompanyInfo();
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        JSONArray js_CompanyID = js.getJSONArray("CompanyID");
                        JSONArray js_CompanyNm = js.getJSONArray("CompanyNm");
                        JSONArray js_UserNm = js.getJSONArray("UserNm");
                        JSONArray js_TaxAcc1 = js.getJSONArray("TaxAcc1");
                        JSONArray js_TaxAcc2 = js.getJSONArray("TaxAcc2");
                        JSONArray js_Notes = js.getJSONArray("Notes");
                        JSONArray js_Address = js.getJSONArray("Address");
                        JSONArray js_Permession = js.getJSONArray("Permession");
                        JSONArray js_CompanyMobile = js.getJSONArray("CompanyMobile");
                        JSONArray js_CompanyMobile2 = js.getJSONArray("CompanyMobile2");
                        JSONArray js_SuperVisorMobile = js.getJSONArray("SuperVisorMobile");
                        JSONArray js_SalInvoiceUnit = js.getJSONArray("SalInvoiceUnit");
                        JSONArray js_PoUnit = js.getJSONArray("PoUnit");
                        JSONArray js_AllowSalInvMinus = js.getJSONArray("AllowSalInvMinus");
                        JSONArray js_GPSAccurent = js.getJSONArray("GPSAccurent");
                        JSONArray js_NumOfInvPerVisit = js.getJSONArray("NumOfInvPerVisit");
                        JSONArray js_NumOfPayPerVisit = js.getJSONArray("NumOfPayPerVisit");
                        q = "Delete from ComanyInfo";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='ComanyInfo'";
                        sqlHandler.executeQuery(q);
                        for (i = 0; i < js_ID.length(); i++) {
                            q = "INSERT INTO ComanyInfo(ID,CompanyID,CompanyNm,UserNm,TaxAcc1,TaxAcc2,Notes,Address,Permession ,CompanyMobile,CompanyMobile2,SuperVisorMobile" +
                                    ",SalInvoiceUnit,PoUnit,AllowSalInvMinus,GPSAccurent,NumOfInvPerVisit,NumOfPayPerVisit) values ('"
                                    + js_ID.get(i).toString()
                                    + "','" + js_CompanyID.get(i).toString()
                                    + "','" + js_CompanyNm.get(i).toString()
                                    + "','" + js_UserNm.get(i).toString()
                                    + "','" + js_TaxAcc1.get(i).toString()
                                    + "','" + js_TaxAcc2.get(i).toString()
                                    + "','" + js_Notes.get(i).toString()
                                    + "','" + js_Address.get(i).toString()
                                    + "','" + js_Permession.get(i).toString()
                                    + "','" + js_CompanyMobile.get(i).toString()
                                    + "','" + js_CompanyMobile2.get(i).toString()
                                    + "','" + js_SuperVisorMobile.get(i).toString()
                                    + "','" + js_SalInvoiceUnit.get(i).toString()
                                    + "','" + js_PoUnit.get(i).toString()
                                    + "','" + js_AllowSalInvMinus.get(i).toString()
                                    + "','" + js_GPSAccurent.get(i).toString()
                                    + "','" + js_NumOfInvPerVisit.get(i).toString()
                                    + "','" + js_NumOfPayPerVisit.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UpdateDataToMobileActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("CompanyID", js_CompanyID.get(i).toString());
                            editor.putString("CompanyNm", js_CompanyNm.get(i).toString());
                            editor.putString("TaxAcc1", js_TaxAcc1.get(i).toString());
                            editor.putString("Address", js_Address.get(i).toString());
                            editor.putString("Notes", js_Notes.get(i).toString());
                            editor.putString("Permession", js_Permession.get(i).toString());
                            editor.putString("CompanyMobile", js_CompanyMobile.get(i).toString());
                            editor.putString("CompanyMobile2", js_CompanyMobile2.get(i).toString());
                            editor.putString("SuperVisorMobile", js_SuperVisorMobile.get(i).toString());

                            editor.commit();
                            custDialog.setMax(js_ID.length());
                            custDialog.incrementProgressBy(1);

                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("معلومات المؤسسة", 1 , total );
                                chkCompany.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("معلومات المؤسسة", 0 , 0 );
                                chkCompany.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        }

        else if (chkCashCust.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);



            final Handler _handler = new Handler();

            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText(" العمـــلاء النقدييــن");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetCASHCUST();
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_No = js.getJSONArray("No");
                        JSONArray js_Name = js.getJSONArray("Name");
                        JSONArray js_veName = js.getJSONArray("veName");

                        q = "Delete from CASHCUST";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='CASHCUST'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_No.length(); i++) {
                            q = "INSERT INTO CASHCUST (No,Name,veName,UserID ) values ('"
                                    + js_No.get(i).toString()
                                    + "','" + js_Name.get(i).toString()
                                    + "','" + js_veName.get(i).toString()
                                    + "','-1')";
                            sqlHandler.executeQuery(q);

                            custDialog.setMax(js_No.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("العملاء النقديين", 1 , total );
                                chkCashCust.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("العملاء النقديين",0 , 0 );
                                chkCashCust.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        }


        else if (chk_Item_cat.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);



            final Handler _handler = new Handler();

            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("فئات اسعار بيع المواد");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Get_Items_Categs();
                    try {
                        Integer i;
                        String q = "";


                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ItemCode= js.getJSONArray("ItemCode");
                        JSONArray js_CategNo= js.getJSONArray("CategNo");
                        JSONArray js_Price= js.getJSONArray("Price");
                        JSONArray js_MinPrice= js.getJSONArray("MinPrice");
                        JSONArray js_dis= js.getJSONArray("dis");


                        q = "Delete from Items_Categ";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='Items_Categ'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_ItemCode.length(); i++) {
                            q = "INSERT INTO Items_Categ(ItemCode, CategNo , Price , MinPrice , dis ) values ('"
                                    + js_ItemCode.get(i).toString()
                                    + "','" + js_CategNo.get(i).toString()
                                    + "','" + js_Price.get(i).toString()
                                    + "','" + js_MinPrice.get(i).toString()
                                    + "','" + js_dis.get(i).toString()+ "')";
                            sqlHandler.executeQuery(q);



                            custDialog.setMax(js_ItemCode.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("فئات اسعار المواد", 1 , total );
                                chk_Item_cat.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("فئات اسعار المواد", 0 , 0 );
                                chk_Item_cat.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        }

        else if (chk_LastPrice.isChecked()) {



            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);



            final Handler _handler = new Handler();

            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("الاستبيان");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetQuests();
                    try {
                        Integer i;
                        String q = "";


                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_QuesNo= js.getJSONArray("QuestID");
                        JSONArray js_Questxt= js.getJSONArray("QuestTxt");



                        q = "Delete from QuesTbl";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='QuesTbl'";
                        sqlHandler.executeQuery(q);


                      //  sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS QuesTbl ( no integer primary key autoincrement,QuesNo text null, Questxt text null ,date text null ) ");

                        for (i = 0; i < js_QuesNo.length(); i++) {

                            q = "INSERT INTO QuesTbl(QuesNo, Questxt   ) values ('"
                                    +      js_QuesNo.get(i).toString()
                                    + "','" + js_Questxt.get(i).toString() + "')";
                            sqlHandler.executeQuery(q);



                            custDialog.setMax(js_QuesNo.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("الاستبيان", 1 , total );
                                chk_LastPrice.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist( "الاستبيان", 0 , 0 );
                                chk_LastPrice.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        }
// Check Duplicate Data



        q = " DELETE FROM UnitItems WHERE no NOT IN   (SELECT MAX(no) FROM UnitItems GROUP BY item_no,unitno )";
        sqlHandler.executeQuery(q);

        q = "DELETE FROM invf       WHERE no NOT IN   (SELECT MAX(no) FROM invf GROUP BY Item_No )";
        sqlHandler.executeQuery(q);

        q = "DELETE  FROM Unites WHERE no NOT IN (SELECT MAX(no) FROM Unites GROUP BY Unitno )";
        sqlHandler.executeQuery(q);

        q = "DELETE   FROM Customers WHERE ID NOT IN (SELECT MAX(ID) FROM Customers GROUP BY no )";
         sqlHandler.executeQuery(q);

    }
    private void Get_Offers_Groups() {

        final Handler _handler = new Handler();
        q = "Delete from Offers_Groups";
        sqlHandler.executeQuery(q);

        q = "delete from sqlite_sequence where name='Offers_Groups'";
        sqlHandler.executeQuery(q);


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_Offers_Groups();
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID = js.getJSONArray("ID");
                    JSONArray js_grv_no = js.getJSONArray("grv_no");
                    JSONArray js_gro_name = js.getJSONArray("gro_name");
                    JSONArray js_gro_ename = js.getJSONArray("gro_ename");
                    JSONArray js_gro_type = js.getJSONArray("gro_type");
                    JSONArray js_item_no = js.getJSONArray("item_no");
                    JSONArray js_unit_no = js.getJSONArray("unit_no");
                    JSONArray js_unit_rate = js.getJSONArray("unit_rate");
                    JSONArray js_qty = js.getJSONArray("qty");
                    JSONArray js_SerNo = js.getJSONArray("SerNo");

                    for (i = 0; i < js_ID.length(); i++) {
                        q = "INSERT INTO Offers_Groups(ID ,grv_no , gro_name , gro_ename , gro_type , item_no , unit_no , unit_rate,qty , SerNo ) values ('"
                                + js_ID.get(i).toString()
                                + "','" + js_grv_no.get(i).toString()
                                + "','" + js_gro_name.get(i).toString()
                                + "','" + js_gro_ename.get(i).toString()
                                + "','" + js_gro_type.get(i).toString()
                                + "','" + js_item_no.get(i).toString()
                                + "','" + js_unit_no.get(i).toString()
                                + "','" + js_unit_rate.get(i).toString()
                                + "','" + js_qty.get(i).toString()
                                + "','" + js_SerNo.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                    }
                }
                catch (final Exception e) {
                }

            }
        }).start();
    }
    private void GetOffersDtlCond(){
        q = "Delete from Offers_Dtl_Cond";
        sqlHandler.executeQuery(q);
        q = "delete from sqlite_sequence where name='Offers_Dtl_Cond'";
        sqlHandler.executeQuery(q);

        new   Thread(new Runnable() {
            @Override
            public void run () {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_Offers_Dtl_Cond();
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID= js.getJSONArray("ID");
                    JSONArray js_Trans_ID= js.getJSONArray("Trans_ID");
                    JSONArray js_Gro_Num= js.getJSONArray("Gro_Num");
                    JSONArray js_Allaw_Repet= js.getJSONArray("Allaw_Repet");
                    for (i = 0; i < js_ID.length(); i++) {
                        q = "INSERT INTO Offers_Dtl_Cond(ID, Trans_ID , Gro_Num , Allaw_Repet) values ('"
                                + js_ID.get(i).toString()
                                + "','" + js_Trans_ID.get(i).toString()
                                + "','" + js_Gro_Num.get(i).toString()
                                + "','" + js_Allaw_Repet.get(i).toString()+ "')";
                        final String ss = q;
                        sqlHandler.executeQuery(q);
                    }

                } catch (final Exception e) {
                }

            }
        }).start();

    }
    private void Get_Offers_Dtl_Gifts(){
        q = "Delete from Offers_Dtl_Gifts";
        sqlHandler.executeQuery(q);
        q = "delete from sqlite_sequence where name='Offers_Dtl_Gifts'";
        sqlHandler.executeQuery(q);

        new   Thread(new Runnable() {
            @Override
            public void run () {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_Offers_Dtl_Gifts();
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID= js.getJSONArray("ID");
                    JSONArray js_Trans_ID= js.getJSONArray("Trans_ID");
                    JSONArray js_Item_No= js.getJSONArray("Item_No");
                    JSONArray js_Unit_No= js.getJSONArray("Unit_No");
                    JSONArray js_Unit_Rate= js.getJSONArray("Unit_Rate");
                    JSONArray js_QTY= js.getJSONArray("QTY");
                    for (i = 0; i < js_ID.length(); i++) {
                        q = "INSERT INTO Offers_Dtl_Gifts(ID, Trans_ID , Item_No , Unit_No , Unit_Rate , QTY ) values ('"
                                + js_ID.get(i).toString()
                                + "','" + js_Trans_ID.get(i).toString()
                                + "','" + js_Item_No.get(i).toString()
                                + "','" + js_Unit_No.get(i).toString()
                                + "','" + js_Unit_Rate.get(i).toString()
                                + "','" + js_QTY.get(i).toString() + "')";
                        sqlHandler.executeQuery(q);
                    }
                } catch (final Exception e) {
                }

            }
        }).start();

    }
    private void FillSal_InvAdapter( String OrderNo) {
        String query ="";
        contactList = new ArrayList<Cls_Sal_InvItems>();
        contactList.clear();


        query = "  select distinct ifnull(pod.Operand,0) as Operand  ,  pod.bounce_qty,pod.dis_per , pod.dis_Amt , pod.OrgPrice , pod.tax_Amt , pod.total ,Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt    " +
                " from Sal_invoice_Det pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.OrderNo='" +OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemNo")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("price")));
                    contactListItems.setItemOrgPrice(c1.getString(c1
                            .getColumnIndex("OrgPrice")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setBounce(c1.getString(c1
                            .getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1
                            .getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));
                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));
                    contactListItems.setOperand(c1.getString(c1
                            .getColumnIndex("Operand")));
                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));
                    contactListItems.setProID(c1.getString(c1
                            .getColumnIndex("ProID")));
                    contactListItems.setPro_bounce(c1.getString(c1
                            .getColumnIndex("Pro_bounce")));
                    contactListItems.setPro_dis_Per(c1.getString(c1
                            .getColumnIndex("Pro_dis_Per")));
                    contactListItems.setPro_amt(c1.getString(c1
                            .getColumnIndex("Pro_amt")));
                    contactListItems.setPro_Total(c1.getString(c1
                            .getColumnIndex("pro_Total")));
                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setDisAmtFromHdr("0");
                    contactList.add(contactListItems);
                } while (c1.moveToNext());
            }
            c1.close();
        }
        Cls_Sal_InvItems Inv_Obj ;
        for (int j = 0; j < contactList.size(); j++) {
            Inv_Obj = new Cls_Sal_InvItems();
            Inv_Obj = contactList.get(j);
            Inv_Obj.setDis_Amt(String.valueOf(Double.parseDouble(Inv_Obj.getDis_Amt()) + Double.parseDouble(Inv_Obj.getPro_amt()) + Double.parseDouble(Inv_Obj.getDisAmtFromHdr())));
            Inv_Obj.setDiscount(String.valueOf(Double.parseDouble(Inv_Obj.getDiscount()) + Double.parseDouble(Inv_Obj.getPro_dis_Per()) + Double.parseDouble(Inv_Obj.getDisPerFromHdr())));
            Inv_Obj.setBounce(String.valueOf(Double.parseDouble(Inv_Obj.getBounce()) + Double.parseDouble(Inv_Obj.getPro_bounce()) ));
        }

    }
    private void FillPayMents_Check_Adapter( String OrderNo) {
        String query ="";
        ChecklList.clear();
        query = "Select  distinct rc.CheckNo,rc.CheckDate,rc.BankNo, IFNULL(rc.Amnt,0)as  Amnt , b.Bank from  RecCheck rc  left join banks b on b.bank_num = rc.BankNo" +
                " where DocNo ='" +OrderNo.toString() + "'";
        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Check cls_check_obj = new Cls_Check();
                    cls_check_obj.setSer(i);
                    cls_check_obj.setCheckNo(c1.getString(c1
                            .getColumnIndex("CheckNo")));
                    cls_check_obj.setCheckDate(c1.getString(c1
                            .getColumnIndex("CheckDate")));
                    cls_check_obj.setBankName(c1.getString(c1
                            .getColumnIndex("Bank")));
                    cls_check_obj.setBankNo(c1.getString(c1
                            .getColumnIndex("BankNo")));
                    cls_check_obj.setAmnt(c1.getString(c1
                            .getColumnIndex("Amnt")));
                    ChecklList.add(cls_check_obj);
                    i=i+1;
                } while (c1.moveToNext());
            }
            c1.close();
        }

    }
    private void Fill_Po_Adapter( String OrderNo) {
        String query ="";
        PoList = new ArrayList<ContactListItems>();
        PoList.clear();

        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);


        query = "  select distinct Unites.UnitName, pod.OrgPrice ,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo ,pod.dis_Amt,pod.dis_per,pod.bounce_qty ,  pod.tax_Amt   , pod.total  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt     from Po_dtl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + OrderNo.toString() + "'";
        c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("price")));
                    contactListItems.setItemOrgPrice(c1.getString(c1
                            .getColumnIndex("OrgPrice")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setBounce(c1.getString(c1
                            .getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1
                            .getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));

                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));

                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));

                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));

                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));

                    contactListItems.setProID("");

                    contactListItems.setPro_bounce("0");

                    contactListItems.setPro_dis_Per("0");

                    contactListItems.setPro_amt("0");

                    contactListItems.setPro_Total("0");
                    contactListItems.setDisAmtFromHdr("0");

                    contactListItems.setDisPerFromHdr("0");

                    PoList.add(contactListItems);

                } while (c1.moveToNext());

            }

            c1.close();
        }

    }
    private void Post_Sal_Inv(String OrderNo) {
        final String  pno  = OrderNo ;
        final   ProgressDialog loading_dialog;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FillSal_InvAdapter(OrderNo);


        String json ="[{''}]";
        try {
            if (contactList.size() > 0) {
                json = new Gson().toJson(contactList);

            }
        }catch (Exception ex){

        }



        final  String str;

        String query = "SELECT distinct  V_OrderNo,OrderNo, acc,date,UserID, COALESCE(hdr_dis_per,0) as hdr_dis_per  , COALESCE(hdr_dis_value ,0) as  hdr_dis_value , COALESCE(Total ,0) as  Total , COALESCE(Net_Total ,0) as Net_Total , COALESCE( Tax_Total ,0) as Tax_Total , COALESCE(bounce_Total ,0) as bounce_Total , COALESCE( include_Tax ,0) as include_Tax" +
                " ,Nm ,COALESCE( disc_Total ,0) as  disc_Total , COALESCE(inovice_type ,0)  as inovice_type  FROM Sal_invoice_Hdr where OrderNo  ='" +  OrderNo.toString() + "'" ;
        Cursor   c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();
        if (c1 !=null&&  c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("Cust_No", c1.getString(c1.getColumnIndex("acc")));
                jsonObject.put("Date",c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));
                jsonObject.put("OrderNo",c1.getString(c1.getColumnIndex("OrderNo")));
                jsonObject.put("hdr_dis_per",c1.getString(c1.getColumnIndex("hdr_dis_per")));
                if (c1.getString(c1.getColumnIndex("hdr_dis_value")).equals("")) {
                    jsonObject.put("hdr_dis_value","0");
                } else {
                    jsonObject.put("hdr_dis_value",c1.getString(c1.getColumnIndex("hdr_dis_value")));
                }
                jsonObject.put("Total",c1.getString(c1.getColumnIndex("Total")));
                jsonObject.put("Net_Total",c1.getString(c1.getColumnIndex("Net_Total")));
                jsonObject.put("Tax_Total",c1.getString(c1.getColumnIndex("Tax_Total")));
                jsonObject.put("bounce_Total",c1.getString(c1.getColumnIndex("bounce_Total")));
                jsonObject.put("include_Tax",c1.getString(c1.getColumnIndex("include_Tax")));
                jsonObject.put("disc_Total",c1.getString(c1.getColumnIndex("disc_Total")));
                jsonObject.put("inovice_type",c1.getString(c1.getColumnIndex("inovice_type")));
                jsonObject.put("CashCustNm",c1.getString(c1.getColumnIndex("Nm")));
                jsonObject.put("V_OrderNo",c1.getString(c1.getColumnIndex("V_OrderNo")));

            }
            catch ( JSONException ex){
                WriteTxtFile.MakeText(" 1 Post_Sal_Inv No = "+OrderNo, ex.getMessage().toString());
                ex.printStackTrace();
            }
            catch (Exception ex){
                WriteTxtFile.MakeText(" 2 Post_Sal_Inv No = " + OrderNo, ex.getMessage().toString());

            }
            c1.close();
        }


        str = jsonObject.toString()+ json;

        loading_dialog = ProgressDialog.show(UpdateDataToMobileActivity.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد فاتورة المبيعات", true);
        loading_dialog.setCancelable(false);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.show();
        final Handler _handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Save_Sal_Invoice(str);
                try {
                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("Post", We_Result.ID);
                        long i;
                        i = sqlHandler.Update("Sal_invoice_Hdr", cv, "OrderNo='"+ pno+"'");
                        _handler.post(new Runnable() {
                            public void run() {
                                //  WriteTxtFile.MakeText(" 3 Post_Sal_Inv No " + pno, We_Result.Msg.toString());
                                filllist_post("فاتورة مبيعات رقم " ,1,pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }
                    else {
                        _handler.post(new Runnable() {
                            public void run() {
                                WriteTxtFile.MakeText(" 4 Post_Sal_Inv No= "+  pno , We_Result.Msg.toString());
                                filllist_post("فاتورة مبيعات رقم  " , 0, pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }

                } catch (final Exception e) {
                    WriteTxtFile.MakeText(" 5 Post_Sal_Inv  No= " + pno , e.getMessage().toString());
                    _handler.post(new Runnable() {
                        public void run() {
                            filllist_post("فاتورة مبيعات رقم  " , 0, pno);
                            loading_dialog.dismiss();
                        }
                    });
                }

            }
        }).start();
    }
    private void Post_Payments(String OrderNo) {

        final String  pno  = OrderNo ;
        final   ProgressDialog loading_dialog;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FillPayMents_Check_Adapter(OrderNo);

        String json ="[{''}]";
        try {
            if (ChecklList.size() > 0) {
                json = new Gson().toJson(ChecklList);            }
        }catch (Exception ex){
        }


        String query = "Select  distinct rc.V_OrderNo, rc.DocNo,   IFNULL(rc.CheckTotal,0) as CheckTotal, IFNULL(rc.Cash,0) as Cash, rc.Desc,rc.Amnt,rc.TrDate,rc.CustAcc  ,c.name , rc.curno  ,COALESCE(Post, -1)  as Post , " +
                "rc.UserID ,rc.VouchType  from RecVoucher rc   left join Customers c on c.no = rc.CustAcc  where rc.DocNo = '" + OrderNo.toString() + "'";
        Cursor   c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();
        if (c1 !=null&&  c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("OrderNo",c1.getString(c1.getColumnIndex("DocNo")));
                jsonObject.put("acc",c1.getString(c1.getColumnIndex("CustAcc")));
                jsonObject.put("Amt",c1.getString(c1.getColumnIndex("Amnt")));
                jsonObject.put("Date",c1.getString(c1.getColumnIndex("TrDate")));
                jsonObject.put("notes", c1.getString(c1.getColumnIndex("Desc")));
                jsonObject.put("VouchType", c1.getString(c1.getColumnIndex("VouchType")));
                jsonObject.put("CurNo", c1.getString(c1.getColumnIndex("curno")));
                if( c1.getString(c1.getColumnIndex("Cash")).toString()==""){
                    jsonObject.put("Cash","0.0" );
                }else {
                    jsonObject.put("Cash", c1.getString(c1.getColumnIndex("Cash")));
                }
                jsonObject.put("CheckTotal",c1.getString(c1.getColumnIndex("CheckTotal")));
                jsonObject.put("V_OrderNo",c1.getString(c1.getColumnIndex("V_OrderNo")));
                jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));

            }
            catch ( JSONException ex){
                WriteTxtFile.MakeText("Post_Payments  No= " + OrderNo , ex.getMessage().toString());
                ex.printStackTrace();
            }
            catch ( Exception ex){
                WriteTxtFile.MakeText("Post_Payments  No= " + OrderNo , ex.getMessage().toString());
                ex.printStackTrace();
            }


            c1.close();
        }


        str = jsonObject.toString() + json;

        loading_dialog = ProgressDialog.show(UpdateDataToMobileActivity.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد سندات القبض ", true);
        loading_dialog.setCancelable(false);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.show();
        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.SavePayment(str);

                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("Post", We_Result.ID);
                        long i;
                        i = sqlHandler.Update("RecVoucher", cv, "DocNo='"+ pno+"'");

                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post("سند قبض رقم " ,1,pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }
                    else {


                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post("سند قبض رقم " ,0,pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }

                } catch ( Exception e) {

                    _handler.post(new Runnable() {
                        public void run() {

                            filllist_post("سند قبض رقم " ,0,pno);
                            loading_dialog.dismiss();
                        }
                    });
                }

            }
        }).start();

    }
    private void Post_Purch_Order(String OrderNo) {
        final String  pno  = OrderNo ;
        final   ProgressDialog loading_dialog;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Fill_Po_Adapter(OrderNo);

        String json ="[{''}]";
        try {
            if (PoList.size() > 0) {
                json = new Gson().toJson(PoList);

            }
        }catch (Exception ex){
        }

        final  String str;



        String  query = "SELECT  distinct V_OrderNo , Notes, acc, userid , Delv_day_count ,date" +
                "   , orderno ,  Total , Net_Total ,Tax_Total , disc_Total" +
                "    ,include_Tax ,pay_method FROM Po_Hdr  where orderno  ='" +OrderNo.toString()+"'";

        Cursor   c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();

        if (c1 !=null&&  c1.getCount() != 0) {
            c1.moveToFirst();

            try {
                jsonObject.put("Cust_No", c1.getString(c1.getColumnIndex("acc")));
                jsonObject.put("day_Count",c1.getString(c1.getColumnIndex("Delv_day_count")));
                jsonObject.put("Date",c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("UserID",c1.getString(c1.getColumnIndex("userid")).replace(",", ""));
                jsonObject.put("OrderNo",c1.getString(c1.getColumnIndex("orderno")));
                jsonObject.put("Total",c1.getString(c1.getColumnIndex("Total")).replace(",", ""));
                jsonObject.put("Net_Total",c1.getString(c1.getColumnIndex("Net_Total")).replace(",", ""));
                jsonObject.put("Tax_Total",c1.getString(c1.getColumnIndex("Tax_Total")).replace(",", ""));
                jsonObject.put("bounce_Total","0");
                jsonObject.put("disc_Total",c1.getString(c1.getColumnIndex("disc_Total")).replace(",", ""));
                jsonObject.put("include_Tax",c1.getString(c1.getColumnIndex("include_Tax")).replace(",", ""));
                jsonObject.put("V_OrderNo",c1.getString(c1.getColumnIndex("V_OrderNo")));
                jsonObject.put("Notes",c1.getString(c1.getColumnIndex("Notes")));
                jsonObject.put("pay_method",c1.getString(c1.getColumnIndex("pay_method")));

            }
            catch ( JSONException ex){
                ex.printStackTrace();
            }


            c1.close();
        }

        str = jsonObject.toString()+ json;


        loading_dialog = ProgressDialog.show(UpdateDataToMobileActivity.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد طلبات البيع", true);
        loading_dialog.setCancelable(false);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.show();
        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Save_po(str, "Insert_PurshOrder");

                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("posted", We_Result.ID);
                        long i;
                        i = sqlHandler.Update("Po_Hdr", cv, "orderno='"+ pno+"'");

                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post( "طلب البيع" ,1,pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }
                    else {


                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post(" طلب البيع " ,0,pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }

                } catch (final Exception e) {

                    _handler.post(new Runnable() {
                        public void run() {

                            filllist_post(" طلب البيع " ,0,pno);
                            loading_dialog.dismiss();
                        }
                    });
                }

            }
        }).start();

    }
    private  void UpdateDeptFilter(){


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.deptf_Filter();
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray Type_No = js.getJSONArray("Type_No");
                    JSONArray Type_Name = js.getJSONArray("Type_Name");
                    JSONArray Search_Key = js.getJSONArray("Search_Key");


                    q = "Delete from deptf_Filter";
                    sqlHandler.executeQuery(q);

                    q = " delete from sqlite_sequence where name='deptf_Filter'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < Type_No.length(); i++) {
                        q = "Insert INTO deptf_Filter(Type_No,Type_Name,Search_Key) values ('"
                                + Type_No.get(i).toString()
                                + "','" + Type_Name.get(i).toString()
                                + "','" + Search_Key.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);


                    }


                } catch (final Exception e) {

                }
            }
        }).start();
    }
    public void btn_Post_Trans(View view) {
        final CheckBox Chk_Post_Inv = (CheckBox) findViewById(R.id.Chk_Post_Inv);
        final CheckBox Chk_Vistis = (CheckBox) findViewById(R.id.Chk_Vistis);

        final CheckBox Chk_Post_Payments = (CheckBox) findViewById(R.id.Chk_Post_Payments);
        final CheckBox chk_po_post = (CheckBox) findViewById(R.id.chk_po_post);
        final CheckBox chk_Doctor_Visit_post = (CheckBox) findViewById(R.id.chk_Doctor_Visit_post);
        List_Result.clear();
        Lv_Result.setAdapter(null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String query="";



        if (Chk_Vistis.isChecked()){
            try {
               // SharManVisits();
                SharcustLocation();
                SharUseCode();

            }catch(Exception ex){
                WriteTxtFile.MakeText("UpdateDataToMobileActivity SharManVisits ", ex.getMessage().toString()  );
            }
        }
        if (Chk_Post_Inv.isChecked()){


            try{
                query = "Delete from  Sal_invoice_Det  where OrderNo in   ( select OrderNo from  Sal_invoice_Hdr   where Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')))  ";
                sqlHandler.executeQuery(query);
                query = "Delete from  Sal_invoice_Hdr where Post !='-1' And ( (date)   <  ('" + currentDateandTime + "'))  ";
                sqlHandler.executeQuery(query);
            } catch (Exception ex) {
                WriteTxtFile.MakeText("UpdateDataToMobileActivity Delete from  Sal_invoice_Det", ex.getMessage().toString()  );


            }
            query = "Select   distinct OrderNo from  Sal_invoice_Hdr where Post  ='-1'";
            Cursor c1 = sqlHandler.selectQuery(query);

            if (c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    do {
                        Post_Sal_Inv(c1.getString(c1.getColumnIndex("OrderNo")));

                    } while (c1.moveToNext());
                }
                c1.close();
            }else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("ترحيل الحركات");
                alertDialog.setMessage("لا يوجد فواتير غير مرحلة ");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();

            }


        }

        if (Chk_Post_Payments.isChecked()){

            query ="Delete from  RecCheck  where DocNo in   ( select DocNo from  RecVoucher   where ifnull(Post,-1) !=-1 And ( (TrDate)   <  ('"+currentDateandTime+"')))  " ;
            sqlHandler.executeQuery(query);
            query ="Delete from  RecVoucher where ifnull(Post,-1)!=-1  And ( (TrDate)   <  ('"+currentDateandTime+"'))  " ;
            sqlHandler.executeQuery(query);

            query = "Select   distinct DocNo from  RecVoucher  where ifnull(Post,-1)  =-1";
            Cursor c1 = sqlHandler.selectQuery(query);

            if (c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    do {

                        Post_Payments(c1.getString(c1.getColumnIndex("DocNo")));

                    } while (c1.moveToNext());
                }
                c1.close();
            }else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("ترحيل الحركات");
                alertDialog.setMessage(" لا يوجد سندات قبض غير مرحلة");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();

            }


        }

        if (chk_Doctor_Visit_post.isChecked()){

            query ="Delete from  DoctorReport where ifnull(Posted,-1)!=-1  And ( (Tr_Date)   <  ('"+currentDateandTime+"'))  " ;
            sqlHandler.executeQuery(query);

            final Handler _handler = new Handler();
            //DoctorReport  "( ID ,VType ,No,CustNo ,LocatNo , Sp1 , SampleType  , VNotes ,SNotes  ,Tr_Date , Tr_Time   ,UserNo , Posted
            query = "  select distinct  ID ,VType ,No,CustNo ,LocatNo , Sp1 , SampleType  , VNotes ,SNotes  ,Tr_Date , Tr_Time   ,UserNo , Posted from DoctorReport   where posted = -1";
            Cursor c1 = sqlHandler.selectQuery(query);
            ArrayList<Cls_DoctorReport> objlist    ;
            objlist = new ArrayList<Cls_DoctorReport>();
            objlist.clear();



            if (c1 != null && c1.getCount() > 0) {
                if (c1.moveToFirst()) {
                    do {
                        Cls_DoctorReport obj = new Cls_DoctorReport();
                        obj.setVType(c1.getString(c1
                                .getColumnIndex("VType")));
                        obj.setNo(c1.getString(c1
                                .getColumnIndex("No")));
                        obj.setCustNo(c1.getString(c1
                                .getColumnIndex("CustNo")));
                        obj.setLocatNo(c1.getString(c1
                                .getColumnIndex("LocatNo")));
                        obj.setSp1(c1.getString(c1
                                .getColumnIndex("Sp1")));
                        obj.setSampleType(c1.getString(c1
                                .getColumnIndex("SampleType")));
                        obj.setVNotes(c1.getString(c1
                                .getColumnIndex("VNotes")));
                        obj.setTr_Date(c1.getString(c1
                                .getColumnIndex("Tr_Date")));
                        obj.setTr_Time(c1.getString(c1
                                .getColumnIndex("Tr_Time")));
                        obj.setUserNo(c1.getString(c1
                                .getColumnIndex("UserNo")));
                        obj.setPosted("-1");

                        objlist.add(obj);

                    } while (c1.moveToNext());

                }
                c1.close();

                final String json = new Gson().toJson(objlist);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                        ws.SaveDoctorReport(json);
                        try {
                            if (We_Result.ID > 0) {
                                String query = " Update  DoctorReport  set Posted='1'  where Posted = '-1'";
                                sqlHandler.executeQuery(query);
                                _handler.post(new Runnable() {
                                    public void run() {

                                        filllist_post("زيارات الاطباء", 1, "");

                                    }
                                });


                            }
                        } catch (final Exception e) {

                        }
                    }
                }).start();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("ترحيل الحركات");
                alertDialog.setMessage("لا يوجد حركات غير مرحلة");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();

            }
        }
        if (chk_po_post.isChecked()){ // طلبات البيع

           /* query ="Delete from  Po_dtl  where orderno in   ( select orderno from  Po_Hdr   where ifnull(posted,'-1') !='-1' And ( (date)   <  ('"+currentDateandTime+"')))  " ;
            sqlHandler.executeQuery(query);
            query ="Delete from  Po_Hdr where ifnull(posted,'-1')!='-1'  And ( (date)   <  ('"+currentDateandTime+"'))  " ;
            sqlHandler.executeQuery(query);
*/
            query = "Select   distinct orderno from  Po_Hdr   where ifnull(posted,-1)  ='-1'";
            Cursor c1 = sqlHandler.selectQuery(query);

            if (c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    do {

                        Post_Purch_Order(c1.getString(c1.getColumnIndex("orderno")));

                    } while (c1.moveToNext());
                }
                c1.close();
            }else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("ترحيل الحركات");
                alertDialog.setMessage("لا يوجد طلبات بيع  غير مرحلة");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();

            }


        }

    }
    public void SharcustLocation() {

        //sqlHandler=new SqlHandler(this);

        final Handler _handler = new Handler();
        String query = "  select distinct CusNo ,Lat ,Long ,Address, date, UserID from CustLocation   where posted = -1";
        Cursor c1 = sqlHandler.selectQuery(query);
        ArrayList<Cls_CustLocation> objlist    ;
        objlist = new ArrayList<Cls_CustLocation>();
        objlist.clear();



        if (c1 != null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_CustLocation obj = new Cls_CustLocation();
                    obj.setCusNo(c1.getString(c1
                            .getColumnIndex("CusNo")));
                    obj.setLat(c1.getString(c1
                            .getColumnIndex("Lat")));
                    obj.setLong(c1.getString(c1
                            .getColumnIndex("Long")));
                    obj.setAddress(c1.getString(c1
                            .getColumnIndex("Address")));
                    obj.setDate(c1.getString(c1
                            .getColumnIndex("date")));
                    obj.setUserID(c1.getString(c1
                            .getColumnIndex("UserID")));
                    obj.setPosted("-1");

                    objlist.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(objlist);
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.SaveCustLocation(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  CustLocation  set Posted='1'  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post("تحديد موقع العميل", 1, "");

                            }
                        });


                    }
                } catch (final Exception e) {

                }
            }
        }).start();
    }
    private  void ShareVisitNew(){

        String query = "  select  Po_Hdr.orderno as Po_Order ,s.Notes  , s.no as no ,s.ManNo as ManNo, s.CusNo as CusNo , s.DayNum as DayNum,s.Tr_Data as Tr_Data ," +
                " s.Start_Time as Start_Time ,s.End_Time as End_Time, s.Duration as Duration , s.VisitType1 as VisitType1, " +
                " s.VisitType2 as VisitType2 ,s.VisitType3 as VisitType3 ,s.VisitType4 as VisitType4  ,s.X as X,s.Y as Y ,s.Locat as  Locat" +
                " , s.OrderNo as  OrderNo from SaleManRounds  s " +
                " Left join  Po_Hdr on Po_Hdr.V_OrderNo = s.OrderNo   where s.Posted = -1 and s.Closed='1' and  s.End_Time!='' order by s.no desc  Limit 1 ";

         String    COMPUTERNAME= Settings.Secure.getString(this.getContentResolver(), "bluetooth_name"  );
        COMPUTERNAME=COMPUTERNAME+" (" + Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID  )+")";

        Cursor c1 = sqlHandler.selectQuery(query);

         if(c1!=null && c1.getCount()>0)
         {
             Cls_SaleManDailyRound cls_saleManDailyRound = new Cls_SaleManDailyRound();
             c1.moveToFirst();
             cls_saleManDailyRound.setNo(c1.getString(c1.getColumnIndex("no")));
             cls_saleManDailyRound.setManNo(c1.getString(c1.getColumnIndex("ManNo")));
             cls_saleManDailyRound.setCusNo(c1.getString(c1.getColumnIndex("CusNo")));
             cls_saleManDailyRound.setDayNum(c1.getString(c1.getColumnIndex("DayNum")));
             cls_saleManDailyRound.setTr_Data(c1.getString(c1.getColumnIndex("Tr_Data")));
             cls_saleManDailyRound.setStart_Time(c1.getString(c1.getColumnIndex("Start_Time")));
             cls_saleManDailyRound.setEnd_Time(c1.getString(c1.getColumnIndex("End_Time")));
             cls_saleManDailyRound.setDuration(c1.getString(c1.getColumnIndex("Duration")));
             cls_saleManDailyRound.setOrderNo(c1.getString(c1.getColumnIndex("OrderNo")));
             cls_saleManDailyRound.setVisitType1(c1.getString(c1.getColumnIndex("VisitType1")));
             cls_saleManDailyRound.setVisitType2(c1.getString(c1.getColumnIndex("VisitType2")));
             cls_saleManDailyRound.setVisitType3(c1.getString(c1.getColumnIndex("VisitType3")));
             cls_saleManDailyRound.setVisitType4(c1.getString(c1.getColumnIndex("VisitType4")));
             cls_saleManDailyRound.setX(c1.getString(c1.getColumnIndex("X")));
             cls_saleManDailyRound.setY(c1.getString(c1.getColumnIndex("Y")));
             cls_saleManDailyRound.setLocat(c1.getString(c1.getColumnIndex("Locat")));
             cls_saleManDailyRound.setPo_Order(c1.getString(c1.getColumnIndex("Po_Order")));
             cls_saleManDailyRound.setNotes(c1.getString(c1.getColumnIndex("Notes")));
             c1.close();
             cls_saleManDailyRound.setCOMPUTERNAME(COMPUTERNAME);
             Do_share_Visits(cls_saleManDailyRound);

         }else{
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("ترحيل الحركات");
        alertDialog.setMessage("لا يوجد زيارات غير مرحلة ");            // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
        return;}



    }
    public ProgressDialog loadingdialog;
    public void Do_share_Visits( final Cls_SaleManDailyRound obj) {
        final String str;
        loadingdialog = ProgressDialog.show(UpdateDataToMobileActivity.this, "الرجاء الانتظار ...",    "العمل جاري على اعتماد جولة رقم :" + obj.getOrderNo(), true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.dismiss();
        loadingdialog.show();
        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
              long i =   ws.SaveManVisitsNew(obj );
                try {
                    if (i> 0) {
                         String query = " Update  SaleManRounds  set Posted='"+We_Result.ID+"'  where OrderNo ='"+obj.getOrderNo() +"'";
                         sqlHandler.executeQuery(query );

                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();
                                filllist_post("جولة رقم  " ,1,obj.getOrderNo());
                                 ShareVisitNew();
                            }
                        });
                    } else {
                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();
                                filllist_post("جولة رقم  " ,0,obj.getOrderNo());
                            }
                        });
                    }
                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                            filllist_post("جولة رقم  " ,0,obj.getOrderNo());
                            loadingdialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    public void SharManVisits() {
        ShareVisitNew();
        //sqlHandler=new SqlHandler(this);
/*
        final Handler _handler = new Handler();

        String query = "  select  Po_Hdr.orderno as Po_Order ,s.Notes  , s.no as no ,s.ManNo as ManNo, s.CusNo as CusNo , s.DayNum as DayNum,s.Tr_Data as Tr_Data ," +
                " s.Start_Time as Start_Time ,s.End_Time as End_Time, s.Duration as Duration , s.VisitType1 as VisitType1, " +
                " s.VisitType2 as VisitType2 ,s.VisitType3 as VisitType3 ,s.VisitType4 as VisitType4  ,s.X as X,s.Y as Y ,s.Locat as  Locat" +
                " , s.OrderNo as  OrderNo from SaleManRounds  s " +
                " Left join  Po_Hdr on Po_Hdr.V_OrderNo = s.OrderNo   where s.Posted = -1 and s.Closed='1' and  s.End_Time!='' ";

        Cursor c1 = sqlHandler.selectQuery(query);
        ArrayList<Cls_SaleManDailyRound> RoundList    ;
        RoundList = new ArrayList<Cls_SaleManDailyRound>();
        RoundList.clear();

        //query = " delete from    SaleManRounds   ";
        // sqlHandler.executeQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_SaleManDailyRound cls_saleManDailyRound = new Cls_SaleManDailyRound();
                    cls_saleManDailyRound.setNo(c1.getString(c1
                            .getColumnIndex("no")));
                    cls_saleManDailyRound.setManNo(c1.getString(c1
                            .getColumnIndex("ManNo")));
                    cls_saleManDailyRound.setCusNo(c1.getString(c1
                            .getColumnIndex("CusNo")));
                    cls_saleManDailyRound.setDayNum(c1.getString(c1
                            .getColumnIndex("DayNum")));
                    cls_saleManDailyRound.setTr_Data(c1.getString(c1
                            .getColumnIndex("Tr_Data")));
                    cls_saleManDailyRound.setStart_Time(c1.getString(c1
                            .getColumnIndex("Start_Time")));
                    cls_saleManDailyRound.setEnd_Time(c1.getString(c1
                            .getColumnIndex("End_Time")));

                    cls_saleManDailyRound.setDuration(c1.getString(c1
                            .getColumnIndex("Duration")));

                    cls_saleManDailyRound.setOrderNo(c1.getString(c1
                            .getColumnIndex("OrderNo")));

                    cls_saleManDailyRound.setVisitType1(c1.getString(c1
                            .getColumnIndex("VisitType1")));
                    cls_saleManDailyRound.setVisitType2(c1.getString(c1
                            .getColumnIndex("VisitType2")));
                    cls_saleManDailyRound.setVisitType3(c1.getString(c1
                            .getColumnIndex("VisitType3")));
                    cls_saleManDailyRound.setVisitType4(c1.getString(c1
                            .getColumnIndex("VisitType4")));

                    cls_saleManDailyRound.setX(c1.getString(c1
                            .getColumnIndex("X")));
                    cls_saleManDailyRound.setY(c1.getString(c1
                            .getColumnIndex("Y")));
                    cls_saleManDailyRound.setLocat(c1.getString(c1
                            .getColumnIndex("Locat")));

                    cls_saleManDailyRound.setPo_Order(c1.getString(c1
                            .getColumnIndex("Po_Order")));

                    cls_saleManDailyRound.setNotes(c1.getString(c1
                            .getColumnIndex("Notes")));
                    RoundList.add(cls_saleManDailyRound);

                } while (c1.moveToNext());

            }
            c1.close();
        }else{

                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("ترحيل الحركات");
                alertDialog.setMessage("لا يوجد زيارات غير مرحلة ");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
              alertDialog.show();
              return;
        }
        final String json = new Gson().toJson(RoundList);

        Calendar c = Calendar.getInstance();
        final int   dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        //  final Handler _handler = new Handler();


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.SaveManVisits(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  SaleManRounds  set Posted=1  where Posted = -1 and Closed = '1' and   End_Time!='' ";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist_post("جولات المندوب", 1, "");
                            }
                        });

                        query = " delete from   SaleManRounds   where Posted =1 and  DayNum < " + dayOfWeek;
                        sqlHandler.executeQuery(query);

                    }else if(We_Result.ID==-404)  {

                        _handler.post(new Runnable() {
                            public void run() {

                                AlertDialog alertDialog = new AlertDialog.Builder(UpdateDataToMobileActivity.this).create();
                                alertDialog = new AlertDialog.Builder(
                                        UpdateDataToMobileActivity.this).create();
                                alertDialog.setTitle(getResources().getText(R.string.UpdatData));
                                alertDialog.setMessage(getResources().getText(R.string.ConnectError));
                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setButton(getResources().getText(R.string.Ok    ), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alertDialog.show();
                            }
                        });

                        }
                    else{

                        _handler.post(new Runnable() {
                            public void run() {

                                AlertDialog alertDialog = new AlertDialog.Builder(UpdateDataToMobileActivity.this).create();
                                alertDialog = new AlertDialog.Builder(
                                        UpdateDataToMobileActivity.this).create();
                                alertDialog.setTitle(getResources().getText(R.string.UpdatData));
                                alertDialog.setMessage(getResources().getText(R.string.ConnectError));
                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setButton(getResources().getText(R.string.Ok    ), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alertDialog.show();
                            }
                        });

                    }

                } catch (final Exception e) {
                }
            }
        }).start();
        */
    }
    public void SharUseCode() {

        //sqlHandler=new SqlHandler(this);
        //q = "INSERT INTO UsedCode(Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo , Posted ) values ('1"


        final Handler _handler = new Handler();
        String query = "  select distinct Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo" +
                " from UsedCode   where Posted = -1";

        Cursor c1 = sqlHandler.selectQuery(query);
/*
        query = " delete from   UsedCode  ";
        sqlHandler.executeQuery(query);*/

        ArrayList<Cls_UsedCodes> CodeList    ;
        CodeList = new ArrayList<Cls_UsedCodes>();
        CodeList.clear();



        if (c1 != null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_UsedCodes obj = new Cls_UsedCodes();
                    obj.setStatus(c1.getString(c1
                            .getColumnIndex("Status")));
                    obj.setCode(c1.getString(c1
                            .getColumnIndex("Code")));
                    obj.setOrderNo(c1.getString(c1
                            .getColumnIndex("OrderNo")));
                    obj.setCustomerNo(c1.getString(c1
                            .getColumnIndex("CustomerNo")));
                    obj.setItemNo(c1.getString(c1
                            .getColumnIndex("ItemNo")));
                    obj.setTr_Date(c1.getString(c1
                            .getColumnIndex("Tr_Date")));
                    obj.setTr_Time(c1.getString(c1
                            .getColumnIndex("Tr_Time")));
                    obj.setUserNo(c1.getString(c1
                            .getColumnIndex("UserNo")));

                    CodeList.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(CodeList);
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.ShareUsedCode(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  UsedCode  set Posted='1'  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist_post("الرموز المستخدمة", 1, "");
                            }
                        });
                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }
    private  void filllist_post ( String str , int f , String  c){

        Cls_UpdateData obj = new Cls_UpdateData();
        String msg = "";
        if (f ==1){

            msg = "تمت عملية اعتماد " + str + c+ " بنجاح"   ;
        }
        else {
            msg = "عملية اعتماد "+ str +  c+ " لم تتم بنجاح"      ;

        }

        obj.setMsg(msg);
        obj.setCount(0);
        obj.setFlag(f);
        List_Result.add(obj);

        listAdapter = new Cls_UpdateData_Adapter(UpdateDataToMobileActivity.this,List_Result);
        Lv_Result.setAdapter(listAdapter);

    }
    public void btn_back(View view) {
        Intent intent ;
        if ( ComInfo.UserType ==1) {
            intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        }else
        {
            intent = new Intent(getApplicationContext(), GalaxyMainActivity2.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent ;
        intent = new Intent(this, GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void btn_Back(View view) {
        Intent intent = new Intent(this, GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}


