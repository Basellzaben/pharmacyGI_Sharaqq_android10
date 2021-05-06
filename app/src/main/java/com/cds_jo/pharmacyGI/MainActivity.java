package com.cds_jo.pharmacyGI;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.ESCPSample3;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.zebra.zq110.ZQ110;


import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;


import Methdes.MyTextView;
import Methdes.MyTextView_Digital;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hearder.main.Header_Frag;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    ImageView imglogo;
    Button btn_print, scanBtn;
    RelativeLayout button10;
    LocationManager locationmanager;
    private TextView contentTxt;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print = new ESCPSample3();
    String dayOfWeek;
    SqlHandler sqlHandler;
    String DeviceDate,js_SERVERDATE, js_SERVERTIME, js_MYEAR, js_MMONTH, js_MDAY, js_MHOUR, js_MMINUTE, js_MSECOND, js_DAYWEEK;
    SweetAlertDialog pDialog ;
    Date ServrTime;
    int GetTime = 0;
    int Week_Num = 1;
    ArrayList<Cls_SaleManDailyRound> RoundList;
    LinearLayout LytMenu, LytGps;
    static ZQ110 mZQ110;
    boolean isGPSEnabled = false;
    private Timer timer;
    String OrderNo = "";
    // Minimum time fluctuation for next update (in milliseconds)
    private static final long TIME = 30000;
    // Minimum distance fluctuation for next update (in meters)
    private static final long DISTANCE = 20;
    TextView et_Notes;
    MyTextView_Digital TrDate, tv_Duration, et_StartTime, et_EndTime, et_ServerTime  ;
    CheckBox V1, V2, V3, V4;
    TextView tv_AlloweDistance,tv_x, tv_y,tv_Loc,tv_CustAddress, tv_Cust_Y,tv_Cust_X,tv_Distance;
    Methdes.MyTextView et_Day, tv_y1, tv_location,lblCurrentDist;
    ImageView imageButton4;
    int Isopen = 0;
    // Declaring a Location Manager
    Calendar CalnederServerTime;
    Calendar CalnederVisitStartTime ;
    SimpleDateFormat ServerFormat;
    String GpsStatus = "";
    String TabDate;
    String url,tv_UserNm;
    Document doc;
    String[] tags;
    Elements elements;
    String Unix_time ;
    String[] split;
    Intent BackInt;
    String  Gpsflag="0",CheckGps="1",GPSAccurent="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_new);
            Gpsflag="0";
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            et_EndTime = (MyTextView_Digital) findViewById(R.id.et_EndTime);
            et_EndTime.setText("00:00:00");
            CalnederServerTime  = Calendar.getInstance();
            CalnederVisitStartTime  = Calendar.getInstance();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            ServerFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            TabDate = sdf.format(new Date());
            et_Day = (MyTextView) findViewById(R.id.et_Day);
            et_ServerTime = (MyTextView_Digital) findViewById(R.id.et_ServerTime);
            //et_ServerTime.setText(ServerFormat.format(new Date()));
            TrDate = (MyTextView_Digital) findViewById(R.id.et_Date);
            // TrDate.setText(TabDate);
            UpdateTimeEverySecond();

            tv_Duration = (MyTextView_Digital) findViewById(R.id.tv_Duration);
            imageButton4 = (ImageView) findViewById(R.id.imageButton4);
            imageButton4.setVisibility(View.VISIBLE);
            button10 = (RelativeLayout) findViewById(R.id.button10);
            RoundList = new ArrayList<Cls_SaleManDailyRound>();
            RoundList.clear();



            LytMenu = (LinearLayout) findViewById(R.id.LytMenu);
            LytGps = (LinearLayout) findViewById(R.id.LytGps);


            tv_AlloweDistance = (TextView) findViewById(R.id.tv_AlloweDistance);
            GPSAccurent = DB.GetValue(MainActivity.this, "ComanyInfo", "GPSAccurent", "1=1");
            tv_AlloweDistance.setText(GPSAccurent);



            lblCurrentDist = (MyTextView) findViewById(R.id.lblCurrentDist);

            tv_CustAddress = (TextView) findViewById(R.id.tv_CustAddress);
            tv_Cust_Y = (TextView) findViewById(R.id.tv_Cust_Y);
            tv_Cust_X = (TextView) findViewById(R.id.tv_Cust_X);
            tv_Distance = (TextView) findViewById(R.id.tv_Distance);
              tv_Distance.setText("0");
            contentTxt = (TextView) findViewById(R.id.scan_content);
            RelativeLayout scanBtn = (RelativeLayout) findViewById(R.id.scan_button);


            et_StartTime = (MyTextView_Digital) findViewById(R.id.et_StartTime);
            SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            String StringTime = StartTime.format(CalnederServerTime.getTime());
            et_StartTime.setText("00:00:00");

            et_Notes = (EditText) findViewById(R.id.tv_Notes);
            V1 = (CheckBox) findViewById(R.id.rdoV1);
            V2 = (CheckBox) findViewById(R.id.rdoV2);
            V3 = (CheckBox) findViewById(R.id.rdoV3);
            V4 = (CheckBox) findViewById(R.id.rdoV4);

            V1.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            V2.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            V3.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            V4.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));

            TextView CustNm = (TextView) findViewById(R.id.tv_CustName);
            CustNm.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));

            RelativeLayout btn_Save_Location = (RelativeLayout) findViewById(R.id.btn_Save_Location);
            tv_x = (TextView) findViewById(R.id.tv_x);
            tv_y = (TextView) findViewById(R.id.tv_y);


            tv_y1 = (Methdes.MyTextView) findViewById(R.id.tv_y1);


            tv_location = (Methdes.MyTextView) findViewById(R.id.tv_location);
             tv_Loc = (TextView) findViewById(R.id.tv_Loc);

            sqlHandler = new SqlHandler(this);
           /* if (ComInfo.ComNo == 1) {
                tv_x.setVisibility(View.INVISIBLE);
                tv_y.setVisibility(View.INVISIBLE);

                tv_y1.setVisibility(View.INVISIBLE);
                tv_Loc.setVisibility(View.INVISIBLE);
                tv_location.setVisibility(View.INVISIBLE);
                btn_Save_Location.setVisibility(View.INVISIBLE);
                button10.setVisibility(View.INVISIBLE);
            } else {
                tv_x.setVisibility(View.VISIBLE);
                tv_y.setVisibility(View.VISIBLE);

                tv_y1.setVisibility(View.VISIBLE);
                tv_Loc.setVisibility(View.VISIBLE);
                tv_location.setVisibility(View.VISIBLE);
                btn_Save_Location.setVisibility(View.VISIBLE);
                button10.setVisibility(View.VISIBLE);
            }*/
            js_SERVERDATE="";
            Get_ServerDateTime();
            sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            DeviceDate = sdf.format(new Date());
            String msg = "تاريخ التابلت غير مطابق لتاريخ السيرفر ، الرجاء تحديث البيانات" + "\r\n";
            msg = msg + "تاريخ التابلت  : " + DeviceDate + "\r\n" + "تاريخ السيرفر : " + js_SERVERDATE;
            if ((!js_SERVERDATE.equalsIgnoreCase(DeviceDate))   || js_SERVERDATE.equalsIgnoreCase("") ) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("فتح زيارة");
                alertDialog.setCancelable(false);
                alertDialog.setMessage(msg);
                alertDialog.setIcon(R.drawable.error_new);
                alertDialog.setButton("رجـــــــوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BackInt = new Intent(MainActivity.this, GalaxyMainActivity.class);
                        startActivity(BackInt);
                    }
                });
                alertDialog.show();

            }


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String u =  sharedPreferences.getString("UserID", "");



            CheckGps = DB.GetValue(MainActivity.this, "manf", "SupNo", "man='"+u+"'");
            if(CheckGps.equalsIgnoreCase("1")){
                Toast.makeText(MainActivity.this,"تم تفعيل الإحداثيات",Toast.LENGTH_LONG).show();
            }

            getUnixTime(-1);

            try {

                //  Getlocation ();
                GetlocationNew();
            } catch (Exception ex) {
            }

            UpdateVisitLocation();
            ShowRecord();

            tv_UserNm=sharedPreferences.getString("UserName", "");

            Fragment frag = new Header_Frag();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        } catch (Exception ex) {
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                     .setTitleText(ex.getMessage().toString())
                    .setContentText(ex.getMessage().toString())
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
        }

    }

    public void btn_HideGps(View view) {
            LytMenu.setVisibility(View.VISIBLE);
            LytGps.setVisibility(View.GONE);
    }

    public void btn_ShowGps(View view) {


        TextView CustNm = (TextView) findViewById(R.id.tv_CustName);


        if(CustNm.getText().toString().equalsIgnoreCase("") ){
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setContentText("يجب تحديد الصيدلية اولا")
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
            return;
        }


        if(tv_Cust_X.getText().toString().equalsIgnoreCase("")||tv_Cust_Y.getText().toString().equalsIgnoreCase("")||tv_Cust_X.getText().toString().equalsIgnoreCase("0.000") ||tv_Cust_Y.getText().toString().equalsIgnoreCase("0.000") ){
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setContentText("موقع الصيدلية غير مدخل")
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
            return;
        }


        if(tv_x.getText().toString().equalsIgnoreCase("")||tv_y.getText().toString().equalsIgnoreCase("")  ){
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setContentText("موقع المندوب غير معرف")
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
            return;
        }

        LytMenu.setVisibility(View.GONE);
        LytGps.setVisibility(View.VISIBLE);
        ShowMapWithTitle();
    }
    LatLng ManLoc ;
    private GoogleMap mMap;
    private Polyline mPolyline = null;
    private  void ShowMapWithTitle(){


        mMap.clear();
        PolylineOptions polylineOpt = new PolylineOptions();
        TextView CustNm = (TextView) findViewById(R.id.tv_CustName);



        Double  Lat = Double.parseDouble(tv_Cust_X.getText().toString());
        Double Long = Double.parseDouble(tv_Cust_Y.getText().toString());;
        ManLoc= new LatLng(Lat, Long);
        mMap.addMarker(new MarkerOptions().position(ManLoc).title(CustNm.getText().toString())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_phar));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ManLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        polylineOpt.add(ManLoc);

//////////////////////////////ManLocation/////////////
          Lat = Double.parseDouble(tv_x.getText().toString());
          Long = Double.parseDouble(tv_y.getText().toString());;
          ManLoc= new LatLng(Lat, Long);
        // create marker



        mMap.addMarker(new MarkerOptions().position(ManLoc).title(tv_UserNm)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_mngr));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ManLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        polylineOpt.add(ManLoc);



        ////////////////////////////////////////////////////////




            polylineOpt.width(5);

        polylineOpt.color(Color.BLUE);


        if (mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
        if (mMap != null) {
            mPolyline = mMap.addPolyline(polylineOpt);

        } else {

        }





    }
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

        @Override
        public void onMyLocationChange(Location location) {
            tv_x.setText(String.valueOf(location.getLatitude()));
            tv_y.setText(String.valueOf(location.getLongitude()));
            tv_Loc.setText(GetStreetName(tv_x.getText().toString(),tv_y.getText().toString()));
            ShowMapWithTitle();
            CalcDist();
            // Toast.makeText(ManAttenActivity.this,location.getProvider()+" ",Toast.LENGTH_SHORT).show();

        }
    };
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(1);



    }

    private class UpdateClock extends AsyncTask<String, Void, Void> {
        String flag;

        final ProgressDialog custDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("العمل جاري على تحديث الوقت ، الرجاء الانتظار");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(String ... params) {
            flag = params[0];

            Unix_time="00:00:00";
            elements=null;
            doc = null;
            tags = new String[]{
                    "span[id=smalltime]"
            };
            url = "https://time.is/Unix_time_now";

            try {
                doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

            } catch (Exception ex) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        GetTime = 0;
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setContentText("حدث خطأ اثناء عملية تحديث الوقت")
                                .setCustomImage(R.drawable.error_new)
                                .setConfirmText("رجــــوع")
                                .show();
                        pDialog.dismiss();
                    }
                });

                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                pDialog.dismiss();
                elements = doc.select(tags[0]);

                for (int i = 0; i < tags.length; i++) {
                    elements = elements.select(tags[i]);
                    GetTime=1;
                }

                Unix_time = elements.text().substring(0, 8);

                if (Unix_time.equalsIgnoreCase("00:00:00") ||  GetTime == 0 ) {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)

                            .setContentText("حصل خطأ اثناء تحديث الوقت")
                            .setCustomImage(R.drawable.error_new)
                            .setConfirmText("رجــــوع")
                            .show();
                    GetTime = 0;
                } else {
                    GetTime = 1;
                    et_ServerTime.setText(Unix_time);
                    split = Unix_time.split(":");
                    String H = split[0];
                    String M = split[1];
                    String S = split[2];
                    CalnederServerTime.set(Integer.parseInt(js_MYEAR), Integer.parseInt(js_MMONTH), Integer.parseInt(js_MDAY), Integer.parseInt(H), Integer.parseInt(M), Integer.parseInt(S));


                    if (flag == "1") {
                        StartRound();
                    } else if (flag == "2") {
                        EndRound();
                    }

                    if (Unix_time.equalsIgnoreCase("00:00:00")) {
                        GetTime = 0;

                        CalnederServerTime.set(Integer.parseInt(js_MYEAR), Integer.parseInt(js_MMONTH), Integer.parseInt(js_MDAY), 0, 0, 0);
                    }



                }
            }catch(Exception Ex){

            }


        }

    }

    private String getUnixTime( final int f)   {
        final Handler _handler = new Handler();
        try {
            Unix_time="00:00:00";

            tags = new String[]{
                    "span[id=smalltime]"
            };
            url = "https://time.is/Unix_time_now";
            new   Thread(new Runnable() {
                @Override
                public void run () {
                    try {
                        doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
                        elements = doc.select(tags[0]);
                        for (int i = 0; i < tags.length; i++) {
                            elements = elements.select(tags[i]);
                        }
                        Unix_time = elements.text().substring(0, 8);
                        _handler.post(new Runnable() {
                            public void run() {
                                GetTime=1;
                                et_ServerTime.setText(Unix_time);
                                split = Unix_time.split(":");
                                String H = split[0];
                                String M = split[1];
                                String S = split[2];
                                CalnederServerTime.set(Integer.parseInt(js_MYEAR), Integer.parseInt(js_MMONTH), Integer.parseInt(js_MDAY), Integer.parseInt(H), Integer.parseInt(M), Integer.parseInt(S));
                                // TrDate.setText(js_SERVERDATE);
                               // Toast.makeText(MainActivity.this, "الوقت في عمّان - الاردن :"+Unix_time,Toast.LENGTH_SHORT).show();


                                if ( f==1){
                                    StartRound();
                                }else if (f==2){
                                    EndRound();
                                }
                            }
                        });

                    } catch (final Exception e) { }
                }
            }).start();


        }catch ( Exception ex){
            GetTime=0;
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)

                    .setContentText(ex.getMessage().toString())
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
        }


        if(Unix_time.equalsIgnoreCase("00:00:00")){
            GetTime=0;
            //TrDate.setText("");
            CalnederServerTime.set(Integer.parseInt(js_MYEAR), Integer.parseInt(js_MMONTH), Integer.parseInt(js_MDAY), 0, 0, 0);
        }

        return  (    Unix_time );
    }

    private void   UpdateTimeEverySecond(){
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }
    private void updateTextView() {
        if (GetTime==0){
            et_ServerTime.setText("00:00:00");
            return;
        }

        java.util.Date date=null;


        try {
            CalnederServerTime.add(Calendar.SECOND,1);
            String StringTime = ServerFormat.format(CalnederServerTime.getTime());
            et_ServerTime.setText(StringTime);
            CaculateTimeVist();
        } catch (Exception e) {

        }
    }
    private void Get_ServerDateTime() {
        String q = " SELECT SERVERDATE ,SERVERTIME , MYEAR ,MMONTH ,MDAY,MDAY,MHOUR,MMINUTE ,MSECOND,DAYWEEK FROM SERVER_DATETIME " ;
        Cursor c = sqlHandler.selectQuery(q);
        if (c!=null && c.getCount()>0){
            c.moveToFirst();

            js_SERVERDATE = c.getString(c.getColumnIndex("SERVERDATE"))  ;
            js_SERVERTIME = c.getString(c.getColumnIndex("SERVERTIME"))  ;
            js_MYEAR = c.getString(c.getColumnIndex("MYEAR"))  ;
            js_MMONTH = c.getString(c.getColumnIndex("MMONTH")) ;
            js_MDAY = c.getString(c.getColumnIndex("MDAY"))  ;
            js_MHOUR = c.getString(c.getColumnIndex("MHOUR"))  ;
            js_MMINUTE =c.getString(c.getColumnIndex("MMINUTE"))  ;
            js_MSECOND = c.getString(c.getColumnIndex("MSECOND"))  ;
            js_DAYWEEK = c.getString(c.getColumnIndex("DAYWEEK"))  ;
            c.close();

            CalnederServerTime.set(Integer.parseInt(js_MYEAR), Integer.parseInt(js_MMONTH), Integer.parseInt(js_MDAY), Integer.parseInt(js_MHOUR), Integer.parseInt(js_MMINUTE), Integer.parseInt(js_MSECOND));
            TrDate.setText(js_SERVERDATE);
            String StringTime = ServerFormat.format(CalnederServerTime.getTime());
            et_ServerTime.setText(StringTime);
            dayOfWeek=js_DAYWEEK;
            et_Day.setText(GetDayName(dayOfWeek));
            et_Day.setError(null);
            TrDate.setError(null);
        }else {
            TrDate.setText("");
        }
    }
    public void UpdateVisitLocation() {
        String   Lat ,Long,address;
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);
        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        address=  GetStreetName(tv_x.getText().toString() ,tv_y.getText().toString());
        String q = " Select * From SaleManRounds Where   ifnull(X,'')=''  and  Closed='0' ";
        Cursor c=sqlHandler.selectQuery(q);
        if(c!=null && c.getCount()>0) {
            try {

                if(!tv_y.getText().toString().equalsIgnoreCase("")){
                    q = "Update SaleManRounds set X='" + tv_x.getText().toString() + "' , Y ='" + tv_y.getText().toString() +"' , Locat='"+address+"'    Where    Closed='0' and ifnull(X,'')=''";
                    sqlHandler.executeQuery(q);
                }else {

                    GetLocation mGPSService = new GetLocation();
                    Location l = mGPSService.CurrentLocation(MainActivity.this);
                    double latitude = l.getLatitude();
                    double longitude = l.getLongitude();
                    Lat = String.valueOf(latitude);
                    Long = String.valueOf(longitude);
                    address=  GetStreetName(Lat+"" ,Long+"");
                    if (!Lat.equalsIgnoreCase("")) {
                        q = "Update SaleManRounds set X='" + Lat + "' , Y ='" + Long + "' , Locat='"+address+"' Where    Closed='0'  and ifnull(X,'')=''";
                        sqlHandler.executeQuery(q);
                    }
                }
            } catch (Exception ex) {


            }
            c.close();
        }
    }

    LocationManager lm;
    LocationListener ll;
    Location l  ;
    private  void  GetLocation2(){

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                l = (Location) location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };




        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(lm.NETWORK_PROVIDER, 0, 0, ll);
        }
        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);

        /*tv_x.setText(l.getLatitude()+"");
        tv_y.setText(l.getLongitude()+"");*/

    }
    public void GetlocationNew() {


        chkStatus();
        String result;
        String address = "";
        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);
        final TextView tv_Loc = (TextView) findViewById(R.id.tv_Loc);

        tv_x.setText("0.000");
        tv_y.setText("0.000");
        tv_Loc.setText("");
        try {
            GetLocation mGPSService = new GetLocation();
            Location l =   mGPSService.CurrentDeviceLocation(MainActivity.this);
            double latitude = l.getLatitude();
            double longitude = l.getLongitude();

            tv_x.setText(String.valueOf(latitude));
            tv_y.setText(String.valueOf(longitude));

         /*  Cls_HttpGet_Location task = new Cls_HttpGet_Location();

            result = task.execute(tv_x.getText().toString(), tv_y.getText().toString()).get();
            tv_Loc.setText(result);
            if(tv_Loc.getText().toString().equalsIgnoreCase("fail")){
                Getlocation();
            }*/
            address=  GetStreetName(tv_x.getText().toString() ,tv_y.getText().toString());
            tv_Loc.setText(address);
            if(tv_Loc.getText().toString().equalsIgnoreCase("fail") ||address.equalsIgnoreCase("") ){
                Getlocation();
                address=  GetStreetName(tv_x.getText().toString() ,tv_y.getText().toString());
            }
        } catch (Exception ex) {
            Getlocation();
            address=  GetStreetName(tv_x.getText().toString() ,tv_y.getText().toString());
           /* if(!GpsStatus.equalsIgnoreCase("")) {
                tv_Loc.setText(GpsStatus);
            }else
                tv_Loc.setText("الموقع غير معروف.");*/
        }
        tv_Loc.setText(address);


      //  GetLocation2();
    }
    public  String GetStreetName(String X , String Y) {
        String  StreetName ="";
        try {


            Geocoder myLocation = new Geocoder(this, Locale.getDefault());
            List<Address> myList = null;
            try {
                myList = myLocation.getFromLocation(Double.parseDouble(X), Double.parseDouble(Y), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address1 = (Address) myList.get(0);
            String addressStr = "";
            addressStr += address1.getAddressLine(0) + ", ";
            addressStr += address1.getAddressLine(1) + ", ";
            addressStr += address1.getAddressLine(2);
            StreetName = addressStr.replace("null", "").replace(",", "") ;
        } catch (Exception ex) {
            StreetName="";
            //   Toast.makeText(this, ex.getMessage().toString() + "  GetStreetName", Toast.LENGTH_SHORT).show();
        }
        return   StreetName;
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
        ShowPharmcy();



    }
    private  void UpdateCustomer(){
        final Handler _handler = new Handler();
        tv = new TextView(getApplicationContext());
        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(MainActivity.this);
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
                CallWebServices ws = new CallWebServices(MainActivity.this);
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

                    q = "Delete from Customers";
                    sqlHandler.executeQuery(q);
                    q = "delete from sqlite_sequence where name='Customers'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_no.length(); i++) {
                        q = "Insert INTO Customers(no,name,Ename,barCode,Address,State,SMan,Latitude,Longitude,Note2,sat " +
                                " ,sun,mon,tues,wens,thurs,sat1,sun1,mon1,tues1,wens1,thurs1 , Celing , CatNo " +
                                ",CustType,PAMENT_PERIOD_NO , CUST_PRV_MONTH,CUST_NET_BAL,Pay_How,Cust_type,countryNm,countryNo) values ('"
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
                            ShowPharmcy();
                            custDialog.dismiss();
                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            ShowPharmcy();
                        }
                    });
                }
            }
        }).start();

    }
    private void ShowPharmcy(){
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
    public void StartRound( ) {
        CalcDist();
        if (CheckGps.equalsIgnoreCase("1")) {
            if (Gpsflag.equalsIgnoreCase("0")) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setContentText("الرجاء التأكد من معلومات الموقع")
                        .setCustomImage(R.drawable.error_new)
                        .setConfirmText("رجــــوع")
                        .show();
                return;
            }
        }

        if (GetTime==0){
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("")
                    .setContentText("الرجاء عمل تحديث للساعة")
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
            return;
        }
        String q1 = "Select * From SaleManRounds Where Closed='0'";
        Cursor c1 ;
        c1 =sqlHandler.selectQuery(q1);

        if(c1!=null && c1.getCount()!=0){
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)

                    .setContentText("يوجد زيارة مفتوحة")
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
            Isopen =1;
            c1.close();
            ShowRecord();
            return;
        }else
        {
            Isopen =0;
        }





        if(  Isopen ==1) {

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)

                    .setContentText("يوجد زيارة مفتوحة")
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
            return;
        }

        Methdes.MyTextView_Digital et_Date =(Methdes.MyTextView_Digital)findViewById(R.id.et_Date);

        if (et_Day.getText().toString().length() == 0) {
            et_Day.setError("required!");
            et_Day.requestFocus();
            return;
        }


        if (et_Date.getText().toString().length() == 0) {
            et_Date.setError("required!");
            et_Date.requestFocus();
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


        et_StartTime.setText( et_ServerTime.getText());

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



        if (i>0) {
            CaculateTimeVist();
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
            } else{
                editor.putString("VisitType1", "0");
            }

            if(V2.isChecked()) {
                editor.putString("VisitType2", "1");
            } else{
                editor.putString("VisitType2", "0");
            }
            if(V3.isChecked()) {
                editor.putString("VisitType3", "1");
            } else{
                editor.putString("VisitType3", "0");
            }
            if(V4.isChecked()) {
                editor.putString("VisitType4", "1");
            } else{
                editor.putString("VisitType4", "0");
            }

            editor.commit();
            Isopen=1;



            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText( "عملية فتح الجولة تمت بنجاح")
                    .setContentText(OrderNo)
                    .setCustomImage(R.drawable.tick)
                    .setConfirmText("رجــــوع")
                    .show();
            imageButton4.setVisibility(View.INVISIBLE);

            /*StartRound = (RelativeLayout )findViewById(R.id.btnStartRound);
            StartRound.setVisibility(View.INVISIBLE);*/
            UpDateMaxOrderNo();
            if(ComInfo.ComNo==1) {
                Save_Cust_Location( );
            }
            FillTempCustQty("");
        }
        else
        {
            Isopen=0;

            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText( getResources().getText(R.string.StartVisitNotSucc).toString())

                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
        }








        //alertDialog.show();
    }
    public void btn_StartRound(View view) {
        // getUnixTime(1);
        new UpdateClock().execute("1");
    }
    private  void FillTempCustQty(String OrderNo){
        sqlHandler.executeQuery(" Update   invf  Set Pack = '0' ");
    }
    public void  EndRound( ) {
    CalcDist();
        if (CheckGps.equalsIgnoreCase("1")) {
            if (Gpsflag.equalsIgnoreCase("0")) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setContentText("الرجاء التأكد من معلومات الموقع")
                        .setCustomImage(R.drawable.error_new)
                        .setConfirmText("رجــــوع")
                        .show();
                return;
            }
        }
        if (GetTime==0){
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("")
                    .setContentText("الرجاء عمل تحديث للساعة")
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
            // Toast.makeText(this,"الرجاء عمل تحديث للساعة",Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler=new SqlHandler(this);

        EditText tv_Notes =(EditText)findViewById(R.id.tv_Notes);


        OrderNo = GetMaxPONo();

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

            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("نهاية الجولة")
                    .setContentText(getResources().getText(R.string.EndVisitNotSucc).toString())
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();

            return;

        }

        if (et_Day.getText().toString().length() == 0) {
            et_Day.setError("required!");
            et_Day.requestFocus();
            return;
        }

        Methdes.MyTextView_Digital et_Date =(Methdes.MyTextView_Digital)findViewById(R.id.et_Date);

        if (et_Date.getText().toString().length() == 0) {
            et_Date.setError("required!");
            et_Date.requestFocus();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put("Notes", et_Notes.getText().toString());
        cv.put("Tr_Data", TrDate.getText().toString());
        cv.put("End_Time", et_ServerTime.getText().toString());
        cv.put("Closed", "1");
        if(V1.isChecked()) {
            cv.put( "VisitType1", "1");
        } else{
            cv.put("VisitType1", "0");
        }

        if(V2.isChecked()) {
            cv.put("VisitType2", "1");
        } else{
            cv.put("VisitType2", "0");
        }
        if(V3.isChecked()) {
            cv.put("VisitType3", "1");
        } else{
            cv.put("VisitType3", "0");
        }
        if(V4.isChecked()) {
            cv.put("VisitType4", "1");
        } else{
            cv.put("VisitType4", "0");
        }
        long i;
        i = sqlHandler.Update("SaleManRounds", cv, "Closed ='0'");


        if (i>0) {
            et_EndTime.setText(et_ServerTime.getText());
            SharedPreferences.Editor editor    = sharedPreferences.edit();
            editor.putString("CustNo", "");
            editor.putString("CustNm", "");
            editor.putString("CustAdd", "");
            editor.putString("V_OrderNo", "-1");
            editor.putString("Notes", et_Notes.getText().toString());
            editor.commit();
            tv_Notes.setText("");

            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("نهاية الجولة")
                    .setContentText(getResources().getText(R.string.EndVisitSucc).toString())
                    .setCustomImage(R.drawable.tick)
                    .setConfirmText("رجــــوع")
                    .show();


            Isopen=0;
            DoNew();
            imageButton4.setVisibility(View.VISIBLE);
            CaculateTimeVist();

            sqlHandler.executeQuery(" Update   invf  Set Pack = '0' ");
            ShareVisitNew();

        }
        else
        {
            Isopen=0;
            new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("نهاية الجولة")
                    .setContentText(getResources().getText(R.string.EndVisitNotSucc).toString())
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();


        }





    }
    public void btn_EndRound(View view) {
        //getUnixTime(2);
        new UpdateClock().execute("2");

    }
    public void Set_Cust(String No, String Nm) {
        TextView CustNm =(TextView)findViewById(R.id.tv_CustName);
        TextView acc = (TextView)findViewById(R.id.tv_Acc);
        acc.setText(No);
        CustNm.setText(Nm);
        et_StartTime.setText("00:00:00");
        et_EndTime.setText("00:00:00");
        tv_Duration.setText("00:00:00");
        acc.setError(null);
        GetCustLocation(No);
    }
    private  void GetCustLocation(String CustNo){

        tv_CustAddress.setText("");
        tv_Cust_X.setText("0.000");
        tv_Cust_Y.setText("0.000");
        String q = "Select Address,Latitude,Longitude From Customers where no='"+CustNo+"'  and  length(Latitude)>4  and  length(Longitude)>4 ";

        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {


                tv_CustAddress.setText(c1.getString(c1.getColumnIndex("Address")));
                tv_Cust_X.setText(c1.getString(c1.getColumnIndex("Latitude")));
                tv_Cust_Y.setText(c1.getString(c1.getColumnIndex("Longitude")));

            }

            c1.close();
            tv_CustAddress.setText( tv_CustAddress.getText()+ "  -- " + GetStreetName(tv_Cust_X.getText().toString(),tv_Cust_Y.getText().toString()));
            CalcDist();
        }
        else  {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    MainActivity.this).create();
            alertDialog.setTitle("خط بيع مندوب");

            alertDialog.setMessage("بيانات الموقع الخاصة بالصيدلية غير متوفرة");
            alertDialog.setIcon(R.drawable.error_new);

            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();


            tv_Cust_Y.setError(null);
            tv_Cust_X.setError(null);

        }

    }
    private void Haversine_distanceBetween(double lat1, double lat2, double lon1,
                                   double lon2, double el1, double el2) {

        Toast.makeText(this,"Haversine",Toast.LENGTH_SHORT).show();

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);




        String Meter="";
        try {
            Meter = distance+"";
        }catch (Exception es){

        }

        lblCurrentDist.setTextColor(Color.BLACK);

        tv_Distance.setText(Meter.substring(0,5)+"");



        if (SToD(Meter)<=SToD(tv_AlloweDistance.getText()+"")){
            lblCurrentDist.setTextColor(Color.GREEN);
            Gpsflag="1";
        }else
        {
            lblCurrentDist.setTextColor(Color.RED);
            Gpsflag="0";
        }


      //  return Math.sqrt(distance);
    }


    private  void DistaneBetweenPoints(){
        Location locationA = new Location("point A");

        if (tv_Cust_X.getText().toString().equalsIgnoreCase("")){
            tv_Cust_X.setText("0.000");
        }
        if (tv_Cust_Y.getText().toString().equalsIgnoreCase("")){
            tv_Cust_Y.setText("0.000");
        }

        if (tv_x.getText().toString().equalsIgnoreCase("")){
            tv_x.setText("0.000");
        }

        if (tv_y.getText().toString().equalsIgnoreCase("")){
            tv_y.setText("0.000");
        }

        locationA.setLatitude(Double.parseDouble(   tv_Cust_X.getText().toString()));
        locationA.setLongitude(Double.parseDouble(   tv_Cust_Y.getText().toString()));

        Location locationB = new Location("point B");

        locationB.setLatitude(Double.parseDouble(   tv_x.getText().toString()));
        locationB.setLongitude(Double.parseDouble(   tv_y.getText().toString()));

        float distance = locationA.distanceTo(locationB );
        String Meter="";
        try {
            Meter = (distance + "").split("\\.")[0];
        }catch (Exception es){
            Meter = (distance + "");
        }
        lblCurrentDist.setTextColor(Color.BLACK);

        tv_Distance.setText(Meter+"");

        if (Integer.parseInt(Meter)<=Integer.parseInt(tv_AlloweDistance.getText()+"")){
            lblCurrentDist.setTextColor(Color.GREEN);
            Gpsflag="1";
        }else
        {
            lblCurrentDist.setTextColor(Color.RED);
            Gpsflag="0";
        }

    }

    private  void CalcDist(){

        if (tv_Cust_X.getText().toString().equalsIgnoreCase("")){
            tv_Cust_X.setText("0.000");
        }
        if (tv_Cust_Y.getText().toString().equalsIgnoreCase("")){
            tv_Cust_Y.setText("0.000");
        }

        if (tv_x.getText().toString().equalsIgnoreCase("")){
            tv_x.setText("0.000");
        }

        if (tv_y.getText().toString().equalsIgnoreCase("")){
            tv_y.setText("0.000");
        }


        //    Haversine_distanceBetween(Double.parseDouble(   tv_Cust_X.getText().toString()),Double.parseDouble(   tv_x.getText().toString())
        //   ,Double.parseDouble(   tv_Cust_Y.getText().toString()),Double.parseDouble(   tv_y.getText().toString()),0,0);

      DistaneBetweenPoints();



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

            if (dayOfWeek.equalsIgnoreCase("7") )
                q = " sat = 1 ";

            if (dayOfWeek.equalsIgnoreCase("1") )
                q = " sun = 1 ";

            if (dayOfWeek.equalsIgnoreCase("2") )
                q = " mon = 1 ";


            if (dayOfWeek.equalsIgnoreCase("3") )
                q = " tues = 1 ";

            if (dayOfWeek.equalsIgnoreCase("4") )
                q = " wens = 1 ";

            if (dayOfWeek.equalsIgnoreCase("5") )
                q = " thurs = 1 ";
        }
        if (Week_Num == 2){
            if (dayOfWeek.equalsIgnoreCase("7") )
                q = " sat1 = 1 ";

            if (dayOfWeek.equalsIgnoreCase("1") )
                q = " sun1 = 1 ";

            if (dayOfWeek.equalsIgnoreCase("2") )
                q = " mon1 = 1 ";


            if (dayOfWeek.equalsIgnoreCase("3") )
                q = " tues1 = 1 ";

            if (dayOfWeek.equalsIgnoreCase("4") )
                q = " wens1 = 1 ";

            if (dayOfWeek.equalsIgnoreCase("5") )
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
    public  String GetDayName(String Day){

        String DayNm ="" ;
        if (Day.equalsIgnoreCase("1")) DayNm = "الاحد";
        else if (Day.equalsIgnoreCase("2")) DayNm = "الاثنين";
        else if (Day.equalsIgnoreCase("3")) DayNm = "الثلاثاء";
        else if (Day.equalsIgnoreCase("4")) DayNm = "الاربعاء";
        else if (Day.equalsIgnoreCase("5")) DayNm = "الخميس";
        else if (Day.equalsIgnoreCase("6")) DayNm = "الجمعة";
        else if (Day.equalsIgnoreCase("7")) DayNm = "السبت";

     /*
        if (ComInfo.Lan.equalsIgnoreCase("ar")) {

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

*/
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
          //  Toast.makeText(this,"تحديث الموقع",Toast.LENGTH_SHORT).show();
            GetlocationNew();
            CalcDist();
            ShowMapWithTitle();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        //String currentDateandTime = sdf.format(new Date());
        //Calendar c = Calendar.getInstance();
        //  dayOfWeek = c.get(Calendar.DAY_OF_WEEK);





      /*  SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        et_StartTime.setText(StringTime);



          StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
          StringTime = StartTime.format(new Date());
          et_EndTime.setText(StringTime);*/


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


        tv_Duration.setText("00:00:00");

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
            et_Day.setText(GetDayName(c1.getString(c1.getColumnIndex("DayNum"))));
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



            c1.close();
            // OrderNo = c1.getInt(c1.getColumnIndex("no"));
             GetCustLocation(CustNo.getText().toString());
             CalcDist();
        }else{
            imageButton4.setVisibility(View.VISIBLE);
        }

        tv_Duration.setText("00:00:00");
       /* SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        et_EndTime.setText(StringTime);

        */
        // et_StartTime

        CaculateTimeVist();
    }
    private  void CaculateTimeVist(){
        if(  Isopen ==1) {

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

                Date date1 = simpleDateFormat.parse(et_StartTime.getText().toString());
                Date date2 = simpleDateFormat.parse(et_ServerTime.getText().toString());

                long diff = date2.getTime() - date1.getTime();
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                tv_Duration.setText(String.valueOf(diffHours) + ":" + String.valueOf(diffMinutes) + ":" + String.valueOf(diffSeconds));
            } catch (Exception ex) {
            }
        }
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



                            }
                        });
                    } else {


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
    public  void Save_Cust_Location(  ) {
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

    public void btn_CustQty(View view) {
        Intent intent = new Intent(getApplicationContext(), CustomerQty.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void btn_UpdateData(View view) {
        Intent intent = new Intent(getApplicationContext(), UpdateDataToMobileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void btn_RefreshTime(View view) {

        getUnixTime(-1);
    }

    private  void ShareVisitNew(){

        String query = "  select  Po_Hdr.orderno as Po_Order ,s.Notes  , s.no as no ,s.ManNo as ManNo, s.CusNo as CusNo , s.DayNum as DayNum,s.Tr_Data as Tr_Data ," +
                " s.Start_Time as Start_Time ,s.End_Time as End_Time, s.Duration as Duration , s.VisitType1 as VisitType1, " +
                " s.VisitType2 as VisitType2 ,s.VisitType3 as VisitType3 ,s.VisitType4 as VisitType4  ,s.X as X,s.Y as Y ,s.Locat as  Locat" +
                " , s.OrderNo as  OrderNo from SaleManRounds  s " +
                " Left join  Po_Hdr on Po_Hdr.V_OrderNo = s.OrderNo   where s.Posted = -1 and s.Closed='1' and  s.End_Time!='' order by s.no desc  Limit 1 ";

        String    COMPUTERNAME= Settings.Secure.getString(getApplicationContext().getContentResolver(), "bluetooth_name"  );
        COMPUTERNAME=COMPUTERNAME+" (" + Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID  )+")";

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

            return;}
    }
    public void Do_share_Visits( final Cls_SaleManDailyRound obj) {
        final String str;

        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getApplicationContext());
                long i =   ws.SaveManVisitsNew(obj );
                try {
                    if (i> 0) {
                        String query = " Update  SaleManRounds  set Posted='"+We_Result.ID+"'  where OrderNo ='"+obj.getOrderNo() +"'";
                        sqlHandler.executeQuery(query );

                        _handler.post(new Runnable() {
                            public void run() {
                                 ShareVisitNew();
                            }
                        });
                    } else {
                        _handler.post(new Runnable() {
                            public void run() {





                            }
                        });
                    }
                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {


                        }
                    });
                }
            }
        }).start();
    }
}
