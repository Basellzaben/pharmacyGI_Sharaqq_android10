package com.cds_jo.pharmacyGI;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.ESCPSample3;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.zebra.zq110.ZQ110;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hearder.main.Header_Frag;

public class MainActivity extends AppCompatActivity  {

    ImageView imglogo;
    Button btn_print, scanBtn;
    RelativeLayout button10;
    LocationManager locationmanager;
    private TextView contentTxt;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print = new ESCPSample3();
    int dayOfWeek;
    SqlHandler sqlHandler;
    TextView TrDate, et_Day, et_StartTime, et_EndTime;
    RelativeLayout  EndRound, StartRound;
    int Week_Num = 1;
    ArrayList<Cls_SaleManDailyRound> RoundList;
    public ProgressDialog loadingdialog;
    static ZQ110 mZQ110;
    boolean isGPSEnabled = false;

    String OrderNo = "";
    // Minimum time fluctuation for next update (in milliseconds)
    private static final long TIME = 30000;
    // Minimum distance fluctuation for next update (in meters)
    private static final long DISTANCE = 20;
    TextView et_Notes;
    CheckBox V1,V2,V3,V4;
    TextView tv_x ,tv_y;
    Methdes.MyTextView tv_x1,tv_y1,tv_location;
    ImageView imageButton4;
    int Isopen = 0 ;
    // Declaring a Location Manager


String GpsStatus ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         imageButton4=(ImageView) findViewById(R.id.imageButton4);

        imageButton4.setVisibility(View.VISIBLE);

        button10 = (RelativeLayout) findViewById(R.id.button10);
        RoundList = new ArrayList<Cls_SaleManDailyRound>();
        RoundList.clear();




        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        TrDate = (TextView) findViewById(R.id.et_Date);
        TrDate.setText(currentDateandTime);

        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        et_Day = (TextView) findViewById(R.id.et_Day);
        et_Day.setText(GetDayName(dayOfWeek));


        contentTxt = (TextView) findViewById(R.id.scan_content);
        RelativeLayout  scanBtn = (RelativeLayout) findViewById(R.id.scan_button);

        et_StartTime = (TextView) findViewById(R.id.et_StartTime);
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        et_StartTime.setText(StringTime);

        et_Notes = (EditText)findViewById(R.id.tv_Notes);
        V1 =(CheckBox)findViewById(R.id.rdoV1);
        V2 =(CheckBox)findViewById(R.id.rdoV2);
        V3 =(CheckBox)findViewById(R.id.rdoV3);
        V4 =(CheckBox)findViewById(R.id.rdoV4);





        RelativeLayout  btn_Save_Location = (RelativeLayout )findViewById(R.id.btn_Save_Location);


            tv_x = (TextView) findViewById(R.id.tv_x);
            tv_y = (TextView) findViewById(R.id.tv_y);


        tv_x1 = (Methdes.MyTextView) findViewById(R.id.tv_x1);
        tv_y1 = (Methdes.MyTextView) findViewById(R.id.tv_y1);


        tv_location = (Methdes.MyTextView) findViewById(R.id.tv_location);
        final TextView tv_Loc = (TextView) findViewById(R.id.tv_Loc);

        sqlHandler=new SqlHandler(this);
        if(ComInfo.ComNo==1){
            tv_x.setVisibility(View.INVISIBLE);
            tv_y.setVisibility(View.INVISIBLE);
            tv_x1.setVisibility(View.INVISIBLE);
            tv_y1.setVisibility(View.INVISIBLE);
            tv_Loc.setVisibility(View.INVISIBLE);
            tv_location.setVisibility(View.INVISIBLE);
            btn_Save_Location.setVisibility(View.INVISIBLE);
            button10.setVisibility(View.INVISIBLE);
        }else{
            tv_x.setVisibility(View.VISIBLE);
            tv_y.setVisibility(View.VISIBLE);
            tv_x1.setVisibility(View.VISIBLE);
            tv_y1.setVisibility(View.VISIBLE);
            tv_Loc.setVisibility(View.VISIBLE);
            tv_location.setVisibility(View.VISIBLE);
            btn_Save_Location.setVisibility(View.VISIBLE);
            button10.setVisibility(View.VISIBLE);
        }


        try {





            //  Getlocation ();



            GetlocationNew();

        }catch ( Exception ex){}

        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        UpdateVisitLocation();
        ShowRecord();

    }
    public void UpdateVisitLocation() {
        String   Lat ,Long;
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);
        final TextView tv_x = (TextView) findViewById(R.id.tv_x);

        String q = " Select * From SaleManRounds Where   ifnull(X,'')='' ";
        Cursor c=sqlHandler.selectQuery(q);
        if(c!=null && c.getCount()>0) {
            try {

              if(!tv_y.getText().toString().equalsIgnoreCase("")){
                  q = "Update SaleManRounds set X='" + tv_x.getText().toString() + "' , Y ='" + tv_y.getText().toString() + "'    Where  ifnull(X,'')=''";
                  sqlHandler.executeQuery(q);
                }else {

                  GetLocation mGPSService = new GetLocation();
                  Location l = mGPSService.CurrentLocation(MainActivity.this);
                  double latitude = l.getLatitude();
                  double longitude = l.getLongitude();
                  Lat = String.valueOf(latitude);
                  Long = String.valueOf(longitude);

                  if (!Lat.equalsIgnoreCase("")) {
                      q = "Update SaleManRounds set X='" + Lat + "' , Y ='" + Long + "' Where  ifnull(X,'')=''";
                      sqlHandler.executeQuery(q);
                  }
              }
            } catch (Exception ex) {


            }
            c.close();
        }
    }
    public void GetlocationNew() {


         chkStatus();
        String result;
        String address = "";
               final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);
        final TextView tv_Loc = (TextView) findViewById(R.id.tv_Loc);


        try {
            GetLocation mGPSService = new GetLocation();
            Location l =   mGPSService.CurrentLocation(MainActivity.this);
            double latitude = l.getLatitude();
            double longitude = l.getLongitude();

            tv_x.setText(String.valueOf(latitude));
            tv_y.setText(String.valueOf(longitude));

        Cls_HttpGet_Location task = new Cls_HttpGet_Location();

            result = task.execute(tv_x.getText().toString(), tv_y.getText().toString()).get();
            tv_Loc.setText(result);
            if(tv_Loc.getText().toString().equalsIgnoreCase("fail")){
                Getlocation();
            }
        } catch (Exception ex) {
            Getlocation();
           /* if(!GpsStatus.equalsIgnoreCase("")) {
                tv_Loc.setText(GpsStatus);
            }else
                tv_Loc.setText("الموقع غير معروف.");*/
        }


    }
    public void Getlocation() {


        String address = "";

        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);
        final TextView tv_Loc = (TextView) findViewById(R.id.tv_Loc);
        contentTxt = (TextView) findViewById(R.id.scan_content);
try {

    GPSService mGPSService = new GPSService(this);
    mGPSService.getLocation();

    if (mGPSService.isLocationAvailable == false) {
        return;
    } else {
        double latitude = mGPSService.getLatitude();
        double longitude = mGPSService.getLongitude();
        address = mGPSService.getLocationAddress();
        try {
             tv_x.setText(String.valueOf(latitude));
            tv_y.setText(String.valueOf(longitude));
            if (address.contains("IO Exception")) {
                if(!GpsStatus.equalsIgnoreCase("")) {
                    tv_Loc.setText(GpsStatus);
                }else
                    tv_Loc.setText("الموقع غير معروف  .");

            } else {
                tv_Loc.setText(address);
            }

        } catch (Exception ex) {
          /*  tv_x.setText("0.0");
            tv_y.setText("0.0");*/
        }

    }
        }catch ( Exception ex ){

        }
    }
    void chkStatus() {
       /* final LocationManager manager = (LocationManager)this.getSystemService    (Context.LOCATION_SERVICE );
        GpsStatus="";
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //Toast.makeText(this, "GPS is disabled!", Toast.LENGTH_LONG).show();
            GpsStatus="GPS is disabled!";
            final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            //Toast.makeText(this, "GPS is enabled!", Toast.LENGTH_LONG).show();
        }


        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {
          //  Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();
        } else if (mobile.isConnectedOrConnecting ()) {
          //  Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show();
        } else {
          //  Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
            GpsStatus = GpsStatus+"  ,  No Network ";
        }
*/
        GpsStatus = "";
    }
    public void btn_SearchCust_dis(View v) {

        final TextView tv_x = (TextView) findViewById(R.id.tv_x);

        if  (tv_x.getText().toString().equalsIgnoreCase("")){
            GetlocationNew();
        }

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Gps");
        FragmentManager Manager = getFragmentManager();
        Select_Customer_Dis obj = new Select_Customer_Dis();
        obj.setArguments(bundle);
        obj.show(Manager, null);



    }
    public void btn_SearchCust(View v) {




       final TextView tv_x = (TextView) findViewById(R.id.tv_x);

      if  (tv_x.getText().toString().equalsIgnoreCase("")){
          GetlocationNew();
      }



        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Gps");
        FragmentManager Manager = getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    static  int id = 1;
    MsgNotification noti = new MsgNotification();
    public void btn_StartRound(View view) {
        String q1 = "Select * From SaleManRounds Where Closed='0'";
        Cursor c1 ;
        c1 =sqlHandler.selectQuery(q1);

        if(c1!=null && c1.getCount()!=0){
            Toast.makeText(this,"يوجد زيارة مفتوحة",Toast.LENGTH_SHORT).show();
            Isopen =1;
            c1.close();
            ShowRecord();
            return;
        }else
        {
            Isopen =0;
        }





      if(  Isopen ==1) {
          Toast.makeText(this,"يوجد زيارة مفتوحة",Toast.LENGTH_SHORT).show();
          return;
      }


        TextView CustNo =(TextView)findViewById(R.id.tv_Acc);
        if (CustNo.getText().toString().length() == 0) {
            CustNo.setError("required!");
            CustNo.requestFocus();
            return;
        }
        //GetlocationNew();

        OrderNo = GetMaxPONo();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler=new SqlHandler(this);


        TextView tv_Loc =(TextView)findViewById(R.id.tv_Loc);
        TextView tv_x =(TextView)findViewById(R.id.tv_x);
        TextView tv_y =(TextView)findViewById(R.id.tv_y);


        TextView CustNm =(TextView)findViewById(R.id.tv_CustName);
        TextView Custadd =(TextView)findViewById(R.id.tv_Loc);

        TrDate =(TextView)findViewById(R.id.et_Date);
        et_StartTime =(TextView)findViewById(R.id.et_StartTime);

        ContentValues cv = new ContentValues();
        cv.put("CusNo",CustNo.getText().toString());
        cv.put("ManNo",sharedPreferences.getString("UserID", ""));
        cv.put("DayNum", String.valueOf(dayOfWeek));
        cv.put("Tr_Data", TrDate.getText().toString());
        cv.put("Start_Time", et_StartTime.getText().toString());
        cv.put("Closed","0");
        cv.put("Posted", "-1");
        cv.put("OrderNo", OrderNo);
        cv.put("Notes", et_Notes.getText().toString());
        cv.put("X", tv_x.getText().toString());
        cv.put("Y", tv_y.getText().toString());
        cv.put("Locat", tv_Loc.getText().toString());

        if(V1.isChecked()) {
            cv.put("VisitType1", "1");
        }else{
            cv.put("VisitType1", "0");
        }
        if(V2.isChecked()) {
            cv.put("VisitType2", "1");
        }else{
            cv.put("VisitType2", "0");
        }
        if(V3.isChecked() ) {
            cv.put("VisitType3", "1");
        }else{
            cv.put("VisitType3", "0");
        }
        if(V4.isChecked() ) {
            cv.put("VisitType4", "1");
        }else{
            cv.put("VisitType4", "0");
        }


        final  String CusNm =  CustNm.getText().toString();
        long i;
        i = sqlHandler.Insert("SaleManRounds", null, cv);
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();


        alertDialog.setTitle(getResources().getText(R.string.VisiMangemnet));
        if (i>0) {

            SharedPreferences.Editor editor    = sharedPreferences.edit();
            editor.putString("CustNo", CustNo.getText().toString());
            editor.putString("CustNm", CustNm.getText().toString());
            editor.putString("CustAdd", Custadd.getText().toString());
            editor.putString("PayCount", "0");
            editor.putString("InvCount", "0");
            editor.putString("V_OrderNo", OrderNo);
            editor.putString("Notes", et_Notes.getText().toString());
            if(V1.isChecked()) {
                editor.putString("VisitType1", "1");
            }
            if(V2.isChecked()) {
                editor.putString("VisitType2", "1");
            }
            if(V3.isChecked()) {
                editor.putString("VisitType3", "1");
            }
            if(V4.isChecked()) {
                editor.putString("VisitType4", "1");
            }

            editor.commit();
            Isopen=1;
            alertDialog.setMessage(getResources().getText(R.string.StartVisitSucc) + ":"+OrderNo);
            alertDialog.setIcon(R.drawable.tick);
            imageButton4.setVisibility(View.INVISIBLE);

            /*StartRound = (RelativeLayout )findViewById(R.id.btnStartRound);
            StartRound.setVisibility(View.INVISIBLE);*/
            UpDateMaxOrderNo();
            if(ComInfo.ComNo==1) {
                Save_Cust_Location(view);
            }

        }
        else
        {
            Isopen=0;
            alertDialog.setMessage(getResources().getText(R.string.StartVisitNotSucc));
            alertDialog.setIcon(R.drawable.delete);
        }





        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String Str = "1- دفع المبالغ المستحقة";
             Str = Str + "\n\r"+ "2- الاجتماع السنوي سيكون بتاريخ 15/12/2016";

                // noti.notify(MainActivity.this, Str, " الرجاء تبليغ العميل بالملاحظات التالية",  CusNm,id);
                id ++ ;
            }
        });


        alertDialog.show();


    }
    public void btn_EndRound(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler=new SqlHandler(this);
        TextView CustNo =(TextView)findViewById(R.id.tv_Acc);
        TextView CustNm =(TextView)findViewById(R.id.tv_CustName);
        EditText tv_Notes =(EditText)findViewById(R.id.tv_Notes);
        TrDate =(TextView)findViewById(R.id.et_Date);
        et_EndTime =(TextView)findViewById(R.id.et_EndTime);
        OrderNo = GetMaxPONo();
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        UpdateVisitLocation();
        String q1 = "Select * From SaleManRounds Where Closed ='0' ";
        Cursor c1 ;
        c1 =sqlHandler.selectQuery(q1);

        if(c1!=null && c1.getCount()!=0){
            Isopen =1;
            c1.close();

        }else
        {
            Isopen =0;
            alertDialog.setTitle("نهاية الجولة");
            alertDialog.setMessage(getResources().getText(R.string.EndVisitNotSucc));
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  return;
                }
            });
            alertDialog.show();


        }


        ContentValues cv = new ContentValues();
        cv.put("Notes", et_Notes.getText().toString());
        cv.put("Tr_Data", TrDate.getText().toString());
        cv.put("End_Time", et_EndTime.getText().toString());
        cv.put("Closed", "1");


        long i;
        i = sqlHandler.Update("SaleManRounds", cv, "Closed ='0'");

        alertDialog.setTitle(getResources().getText(R.string.VisiMangemnet));
        if (i>0) {

            SharedPreferences.Editor editor    = sharedPreferences.edit();
            editor.putString("CustNo", "");
            editor.putString("CustNm", "");
            editor.putString("CustAdd", "");
            editor.putString("V_OrderNo", "-1");
            editor.putString("Notes", et_Notes.getText().toString());
            editor.commit();
            tv_Notes.setText("");
            alertDialog.setMessage(getResources().getText(R.string.EndVisitSucc));
            alertDialog.setIcon(R.drawable.tick);
          //  noti.cancel(this);
            Isopen=0;
            DoNew();
            imageButton4.setVisibility(View.VISIBLE);

        }
        else
        {
            Isopen=0;
            alertDialog.setMessage(getResources().getText(R.string.EndVisitNotSucc));
            alertDialog.setIcon(R.drawable.delete);
        }
       alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
       alertDialog.show();



    }
    public void Set_Cust(String No, String Nm) {
        TextView CustNm =(TextView)findViewById(R.id.tv_CustName);
        TextView acc = (TextView)findViewById(R.id.tv_Acc);
        acc.setText(No);
        CustNm.setText(Nm);
        acc.setError(null);
    }
    public  void GetCustomer() {

        final TextView tv_x = (TextView)findViewById(R.id.tv_x);
        final TextView tv_y = (TextView)findViewById(R.id.tv_y);

        final TextView tv_CustName= (TextView)findViewById(R.id.tv_CustName);
        final TextView tv_Acc = (TextView)findViewById(R.id.tv_Acc);
        final TextView tv_Loc = (TextView)findViewById(R.id.tv_Loc);
        final TextView tv_Address = (TextView)findViewById(R.id.tv_Loc);

        String q = "";

        if (Week_Num == 1){

            if (dayOfWeek == 7 )
                q = " sat = 1 ";

            if (dayOfWeek == 1 )
                q = " sun = 1 ";

            if (dayOfWeek == 2 )
                q = " mon = 1 ";


            if (dayOfWeek == 3 )
                q = " tues = 1 ";

            if (dayOfWeek == 4 )
                q = " wens = 1 ";

            if (dayOfWeek == 5 )
                q = " thurs = 1 ";
        }
        if (Week_Num == 2){
            if (dayOfWeek == 7 )
                q = " sat1 = 1 ";

            if (dayOfWeek == 1 )
                q = " sun1 = 1 ";

            if (dayOfWeek == 2 )
                q = " mon1 = 1 ";


            if (dayOfWeek == 3 )
                q = " tues1 = 1 ";

            if (dayOfWeek == 4 )
                q = " wens1 = 1 ";

            if (dayOfWeek == 5 )
                q = " thurs1 = 1 ";

        }


        // Toast.makeText(this,contentTxt.getText().toString().substring(1,6),Toast.LENGTH_SHORT).show();
        q = "Select   c.no,c.name ,c.Latitude,c.Longitude,c.Address,c.barCode, c.SMan, c.State from Customers  c" +
                "     Where (  c.barCode='-1' or    c.barCode = '"+ contentTxt.getText().toString()+"' ) And ( c.Latitude = '-1' or   c.Latitude = '"+tv_x.getText().toString()+"' )" +
                " And (  c.Longitude = '-1' or   c.Longitude = '"+tv_y.getText().toString()+"') And  c." + q;

        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_CustName.setText(c1.getString(c1.getColumnIndex("name")));
                tv_Acc.setText(c1.getString(c1.getColumnIndex("no")));
                tv_Address.setText(c1.getString(c1.getColumnIndex("Address")));
                tv_Acc.setError(null);
            }
            c1.close();
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    MainActivity.this).create();
            alertDialog.setTitle("خط بيع مندوب");

            alertDialog.setMessage("لا يوجد بيانات عميل لهذا المندوب");
            alertDialog.setIcon(R.drawable.delete);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            // progressDialog.incrementProgressBy(1);
        }

    }
    public  String GetDayName(Integer Day){


        String DayNm ="" ;
        if (ComInfo.Lan.equalsIgnoreCase("ar")) {
            if (Day == 1) DayNm = "الاحد";
            else if (Day == 2) DayNm = "الاثنين";
            else if (Day == 3) DayNm = "الثلاثاء";
            else if (Day == 4) DayNm = "الاربعاء";
            else if (Day == 5) DayNm = "الخميس";
            else if (Day == 6) DayNm = "الجمعة";
            else if (Day == 7) DayNm = "السبت";
        }
        else{
            if (Day == 1) DayNm = "Sunday";
            else if (Day == 2) DayNm = "Monday";
            else if (Day == 3) DayNm = "Tuesday";
            else if (Day == 4) DayNm = "Wednesday";
            else if (Day == 5) DayNm = "Thursday";
            else if (Day == 6) DayNm = "Friday";
            else if (Day == 7) DayNm = "Saturday";
        }


        return  DayNm;

    }
    public void BtnScan(View view) {
        if (view.getId() == R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
         /*   String address = "";
            GPSService mGPSService = new GPSService(view.getContext());
            mGPSService.getLocation();*/

        }


    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();


            contentTxt = (TextView) findViewById(R.id.scan_content);


            contentTxt.setText("");



            if (scanningResult.getContents() != null) {
                contentTxt.setText( scanContent.toString());

                GetCustomer();
            }
            //contentTxt.setText( "6253803400018");
            //  GetCustomer();


        }
        // GetCustomer();
    }
    public void btn_GetLocation(View view) {
        try {
            GetlocationNew();
        }catch ( Exception ex){}

        //Getlocation();

    }
    public void btn0_back(View view) {
        Intent i = new Intent(this,GalaxyMainActivity.class);
        startActivity(i);
    }
    private void  DoNew(){
        // ShowRecord();
        TextView CustNo =(TextView)findViewById(R.id.tv_Acc);
        TextView CustNm =(TextView)findViewById(R.id.tv_CustName);

        CustNo.setText("");
        CustNm.setText("");


        StartRound = (RelativeLayout )findViewById(R.id.btnStartRound);
        StartRound.setVisibility(View.VISIBLE);

        EndRound = (RelativeLayout )findViewById(R.id.btnEndRound);
        EndRound.setVisibility(View.VISIBLE);

        TextView tv_Duration =(TextView)findViewById(R.id.tv_Duration);
        tv_Duration.setText("");



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        TrDate=(TextView)findViewById(R.id.et_Date);
        TrDate.setText(currentDateandTime);

        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        et_Day=(TextView)findViewById(R.id.et_Day);
        et_Day.setText(GetDayName(dayOfWeek));



        et_StartTime = (TextView)findViewById(R.id.et_StartTime);
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        et_StartTime.setText(StringTime);


          et_EndTime = (TextView)findViewById(R.id.et_EndTime);
          StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
          StringTime = StartTime.format(new Date());
          et_EndTime.setText(StringTime);


        GetlocationNew();
        V1.setChecked(false);
        V2.setChecked(false);
        V3.setChecked(false);
        V4.setChecked(false);
        GetMaxPONo();

    }
    public  void ShowRecord(){


        TextView CustNo =(TextView)findViewById(R.id.tv_Acc);
        TextView CustNm =(TextView)findViewById(R.id.tv_CustName);



        String query = "SELECT  SaleManRounds.no   , SaleManRounds.CusNo ,Customers.name ,Tr_Data,DayNum,Start_Time ,ifnull(SaleManRounds.Notes,' ')as  Notes,  " +
                " ifnull( SaleManRounds.VisitType1,'1') as VisitType1, ifnull( SaleManRounds.VisitType2,'1') as VisitType2 ,ifnull( SaleManRounds.VisitType3,'1') as VisitType3,ifnull( SaleManRounds.VisitType4,'1') as VisitType4  FROM SaleManRounds Left join Customers on Customers.no =SaleManRounds.CusNo  where Closed = 0";
        Cursor c1 = sqlHandler.selectQuery(query);

        et_StartTime = (TextView)findViewById(R.id.et_StartTime);

        et_Day=(TextView)findViewById(R.id.et_Day);
        RelativeLayout EndRound = (RelativeLayout)findViewById(R.id.btnEndRound);
        RelativeLayout StartRound = (RelativeLayout)findViewById(R.id.btnStartRound);  et_EndTime =(TextView)findViewById(R.id.et_EndTime);
        TextView tv_Duration =(TextView)findViewById(R.id.tv_Duration);
        tv_Duration.setText("");
        EndRound.setVisibility(View.VISIBLE);
        StartRound.setVisibility(View.VISIBLE);
        Isopen =0 ;
        if ( c1 !=null&&c1.getCount() > 0 ) {
            imageButton4.setVisibility(View.INVISIBLE);
            Isopen = 1 ;
         //  Toast.makeText(this,"يوجد ملف  مفتوح",Toast.LENGTH_SHORT).show();
            c1.moveToFirst();
            CustNo.setText(c1.getString(c1.getColumnIndex("CusNo")));
            CustNm.setText(c1.getString(c1.getColumnIndex("name")));
            TrDate.setText(c1.getString(c1.getColumnIndex("Tr_Data")));
            et_StartTime.setText(c1.getString(c1.getColumnIndex("Start_Time")));
            et_Day.setText(GetDayName(Integer.valueOf(c1.getString(c1.getColumnIndex("DayNum")))));
            et_Notes.setText(c1.getString(c1.getColumnIndex("Notes")));

           if(c1.getString(c1.getColumnIndex("VisitType1")).toString().equalsIgnoreCase("1")) {
               V1.setChecked(true);
               /*V2.setChecked(false);
               V3.setChecked(false);
               V4.setChecked(false);*/


           } if (c1.getString(c1.getColumnIndex("VisitType2")).toString().equalsIgnoreCase("1")){
                V2.setChecked(true);
               /* V1.setChecked(false);
                V3.setChecked(false);
                V4.setChecked(false);*/

        }  if (c1.getString(c1.getColumnIndex("VisitType3")).toString().equalsIgnoreCase("1")){
            V3.setChecked(true);
           /* V1.setChecked(false);
            V2.setChecked(false);
            V4.setChecked(false);*/


        }  if (c1.getString(c1.getColumnIndex("VisitType4")).toString().equalsIgnoreCase("1")){
           /* V1.setChecked(false);
            V2.setChecked(false);
            V3.setChecked(false);*/
            V4.setChecked(true);

        }
            EndRound.setVisibility(View.VISIBLE);
            StartRound.setVisibility(View.VISIBLE);



          c1.close();
            // OrderNo = c1.getInt(c1.getColumnIndex("no"));

        }else{
            imageButton4.setVisibility(View.VISIBLE);
        }

        tv_Duration.setText("");
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        et_EndTime.setText(StringTime);


        // et_StartTime
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

            Date date1= simpleDateFormat.parse(et_StartTime.getText().toString());
            Date date2= simpleDateFormat.parse(et_EndTime.getText().toString());

            long diff = date2.getTime() - date1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            tv_Duration.setText(String.valueOf(diffHours)+":"+String.valueOf(diffMinutes)+":" + String.valueOf(diffSeconds)  );
        }
        catch (Exception ex){}

    }
    public void btn_delete(View view) {


        sqlHandler.Delete("SaleManRounds", "");



    }
    public void btn_Share(View view) {


        sqlHandler = new SqlHandler(this);
        String query = "  select Po_Hdr.orderno as Po_Order  , s.no as no ,s.ManNo as ManNo, s.CusNo as CusNo , s.DayNum as DayNum,s.Tr_Data as Tr_Data ," +
                        " s.Start_Time as Start_Time ,s.End_Time as End_Time, s.Duration as Duration , s.VisitType1 as VisitType1, " +
                        " s.VisitType2 as VisitType2 ,s.VisitType3 as VisitType3 ,s.VisitType4 as VisitType4  ,s.X as X,s.Y as Y ,s.Locat as  Locat" +
                        " , s.OrderNo as  OrderNo from SaleManRounds  s " +
                        " Left join  Po_Hdr on Po_Hdr.V_OrderNo = s.OrderNo   where s.Posted = -1";
        Cursor c1 = sqlHandler.selectQuery(query);
        RoundList.clear();

        //query = " delete from   SaleManRounds   ";
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
                    cls_saleManDailyRound.setOrderNo(c1.getString(c1
                            .getColumnIndex("OrderNo")));
                    cls_saleManDailyRound.setPo_Order(c1.getString(c1
                            .getColumnIndex("Po_Order")));
                    RoundList.add(cls_saleManDailyRound);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(RoundList);

        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        loadingdialog = ProgressDialog.show(MainActivity.this, "الرجاء الانتظار ...", "العمل جاري على ترحيل زيارات المندوب", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(MainActivity.this);
                ws.SaveManVisits(json);
                try {

                    if (We_Result.ID > 0) {

                        String query = " Update  SaleManRounds  set Posted=1  where Posted = '-1'";
                        sqlHandler.executeQuery(query);

                        query = " delete from   SaleManRounds   where Posted =1 and  DayNum < " + dayOfWeek;
                        sqlHandler.executeQuery(query);


                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        MainActivity.this).create();
                                alertDialog.setTitle("ترحيل زيارات المندوب ");
                                alertDialog.setMessage("تمت عملية ترحيل زيارات العميل بنجاح");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();

                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        MainActivity.this).create();
                                if (We_Result.ID == 0) {
                                    alertDialog.setMessage("لا يوجد زيارات غير مرحلة");

                                } else {
                                    alertDialog.setMessage("عملية الترحيل لم تتم بنجاح " + "    ");

                                }

                                alertDialog.setTitle(" عملية الترحيل لم تتم بنجاح"+ "   " + We_Result.ID+"");
                                //  alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                            }
                        });
                    }

                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    MainActivity.this).create();
                            alertDialog.setTitle("فشل في عمليه الاتصال");
                            alertDialog.setMessage(e.getMessage().toString());
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertDialog.show();
                        }
                    });
                }
            }
        }).start();
    }
    public void btm_delete(View view) {
        sqlHandler = new SqlHandler(this);

        String   query = " delete from   SaleManRounds   ";
        sqlHandler.executeQuery(query);

    }
    public  void Save_Cust_Location(View view ) {
        final TextView tv_x = (TextView)findViewById(R.id.tv_x);
        final TextView tv_y = (TextView)findViewById(R.id.tv_y);
        TextView CustNo =(TextView)findViewById(R.id.tv_Acc);
        if (CustNo.getText().toString().length() == 0) {


            if (ComInfo.ComNo!=1) {
                CustNo.setError("required!");
                CustNo.requestFocus();
            }
            return;
        }

        if (tv_y.getText().toString().length() == 0) {
            if (ComInfo.ComNo!=1) {
                tv_y.setError("required!");
                tv_y.requestFocus();
            }
            return;
        }

        if (tv_x.getText().toString().length() == 0) {
            if (ComInfo.ComNo!=1) {
                tv_x.setError("required!");
                tv_x.requestFocus();
            }
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler=new SqlHandler(this);

        TextView CustNm =(TextView)findViewById(R.id.tv_CustName);
        TextView Custadd =(TextView)findViewById(R.id.tv_Loc);

        TrDate =(TextView)findViewById(R.id.et_Date);
        et_StartTime =(TextView)findViewById(R.id.et_StartTime);
        ContentValues cv = new ContentValues();
        cv.put("CusNo",CustNo.getText().toString());
        cv.put("Lat",tv_x.getText().toString());
        cv.put("Long",tv_y.getText().toString() );
        cv.put("Address",Custadd.getText().toString() );
        cv.put("date", TrDate.getText().toString());
        cv.put("UserID", sharedPreferences.getString("UserID", "-1"));
        cv.put("posted", "-1");
        long i;



        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("تحديد موقع العميل");

        String  q = "select * from CustLocation where CusNo ='"+CustNo.getText().toString()+"'";
        Cursor c = sqlHandler.selectQuery(q);

        if(c!=null && c.getCount() > 0 ){

            i = sqlHandler.Update("CustLocation", cv, "CusNo ='"+CustNo.getText().toString()+"'");
            String s = "Update  Customers set Latitude = '"+tv_x.getText().toString() + "',  Longitude ='"+tv_y.getText().toString() +"' where no = '"+CustNo.getText().toString()+"'";
            sqlHandler.executeQuery(s);
            alertDialog.setMessage("تمت عملية تعديل موقع العميل بنجاح");
            c.close();


        }
        else{
            i = sqlHandler.Insert("CustLocation", null, cv);
            alertDialog.setMessage("عملية تخزين موقع العميل تمت بنجاح");


        }


        if (i>0) {

            alertDialog.setIcon(R.drawable.tick);

        }
        else
        {
            alertDialog.setMessage("عملية  الحفظ لم تتم ");
            alertDialog.setIcon(R.drawable.delete);
        }

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        if (ComInfo.ComNo!=1) {
            alertDialog.show();
        }
    }
    public  String GetMaxPONo(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");

        String query = "SELECT  ifnull(MAX(OrderNo), 0) +1 AS no FROM SaleManRounds where  ManNo ='" +u.toString()+"'"  ;
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }
        String max1="0";
     //   max1 = sharedPreferences.getString("VisitSerial", "");
        max1 = DB.GetValue(MainActivity.this, "OrdersSitting", "Visits", "1=1");



        if (max1==""){
            max1 ="0";
        }
        max1 =String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max))
        {
            max = max1 ;
        }



        if (max.length()==1) {
            max = intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5);
        }
        else {
            max= (intToString(Integer.valueOf(max), 7));
        }
        return max;
    }
    private  void  UpDateMaxOrderNo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");

        String query = "SELECT  ifnull(MAX(OrderNo), 0) AS no FROM SaleManRounds where  ManNo ='" +u.toString()+"'"  ;

        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }



        max=(intToString(Integer.valueOf(max), 7)  );
        query = "Update OrdersSitting SET Visits ='" + max + "'";
        sqlHandler.executeQuery(query);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VisitSerial",max);
        editor.commit();
    }
    private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",", "");
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
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void btn_RecVoucher(View view) {
        Intent intent = new Intent(getApplicationContext(), RecvVoucherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void btn_Invoice(View view) {
        Intent intent = new Intent(getApplicationContext(), Sale_InvoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void btn_Order(View view) {
        Intent intent = new Intent(getApplicationContext(), OrdersItems.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void btn_AccReport(View view) {
        Intent intent = new Intent(getApplicationContext(), Acc_ReportActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void btn_back(View view) {

        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
