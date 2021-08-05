package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Methdes.MyTextView;
import hearder.main.Header_Frag;

public class ScheduleManActivity extends FragmentActivity {
  SqlHandler  sqlHandler ;
    ListView Cust_Visits ;
    int dayOfWeek ;
    Spinner sp_day;
    int Week_Num = 1 ;
    ImageButton  OpenBarCode;
    MyTextView tv_Barcode;
     ArrayList<Cls_Daily_Visits>   cls_daily_visitses;
    String OrderNo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_schedule_man);

        Fragment frag=new Header_Frag();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

        Cust_Visits =(ListView)findViewById(R.id.lst_Cust_Visits);
        sp_day = (Spinner) findViewById(R.id.sp_day);
          cls_daily_visitses = new ArrayList<Cls_Daily_Visits>();

         FillDeptf();
         btn_GetData();

         sp_day.setEnabled(false);
        sp_day.setClickable(false);

        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        Cls_Day obj = new Cls_Day();
        Cls_Day_Adapter cls_day_adapter = (Cls_Day_Adapter) sp_day.getAdapter();
        for (int i = 0; i < cls_day_adapter.getCount(); i++) {
            obj = (Cls_Day) cls_day_adapter.getItem(i);
               if (obj.getDayNo().equals(String.valueOf(dayOfWeek))) {
                   sp_day.setSelection(i);
                break;
            }
        }
        tv_Barcode = (MyTextView)findViewById(R.id.tv_Barcode);
        OpenBarCode =(ImageButton) findViewById(R.id.btn_openbarcode);
        OpenBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_Barcode.setText("");
                openScanBarCode();
            }
        });

        sp_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                //String imc_met=spin.getSelectedItem().toString();
                // btn_GetData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
private  void openScanBarCode(){
    IntentIntegrator scanIntegrator = new IntentIntegrator(this);
    scanIntegrator.initiateScan();
}


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

            //String scanFormat = scanningResult.getFormatName();

            if (scanningResult.getContents() != null) {
                final   String scanContent = scanningResult.getContents().toString();
                tv_Barcode.setText(scanContent.toString());
                checkCust(scanContent.toString());


            }


        }

      //  checkCust("11020408035");
    }
private  void checkCust(String BarCodeNo){

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

     AlertDialog alert_Dialog = new AlertDialog.Builder(
            this).create();
  /*  alert_Dialog.setMessage(BarCodeNo);
    alert_Dialog.setIcon(R.drawable.tick);
    alert_Dialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    alert_Dialog.show();*/


    Cls_Daily_Visits Inv_Obj ;
    for (int j = 0; j < cls_daily_visitses.size(); j++) {
        Inv_Obj = new Cls_Daily_Visits();
        Inv_Obj = cls_daily_visitses.get(j);
        int op = 0 ;
        String q ="0";
        /*if(  (Inv_Obj.getCustNo().toString().trim().equals(BarCodeNo.toString().trim()))){
            q="1";
        }

        if(    (Inv_Obj.getCustNo().toString().trim()== BarCodeNo.toString().trim())     ){
            q="2";
        }


        if (  (Inv_Obj.getBarcode().toString().trim()== BarCodeNo.toString().trim()) ){
            q="3";
        }


        if  (Inv_Obj.getBarcode().toString().trim().equals(BarCodeNo.toString().trim()))  {
            q="4";
        }



        if(  (Inv_Obj.getCustNo().toString().trim().equals(BarCodeNo.toString().trim()))
                ||  (Inv_Obj.getCustNo().toString().trim()== BarCodeNo.toString().trim())
                ||  (Inv_Obj.getBarcode().toString().trim()== BarCodeNo.toString().trim())
                ||  (Inv_Obj.getBarcode().toString().trim().equals(BarCodeNo.toString().trim()))
         )
        */

        if  (Inv_Obj.getBarcode().toString().trim().equals(BarCodeNo.toString().trim()))
        {
            alertDialog.setTitle(Inv_Obj.getCustNm().toString() + "(" + Inv_Obj.getCustNo().toString() + ")");
            if ((Inv_Obj.getStates().toString().equals("1")) ||(Inv_Obj.getStates().toString().equals("-1")) ){
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setMessage(" هل تريد فتح جولة "   +  q);
                op = 1 ;
            }
            if (Inv_Obj.getStates().toString().equals("0")){
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setMessage("هل تريد اغلاق الجولة");
                op = 2 ;
            }

            final  int Excute_Op = op ;
            final  String  Cust_No =  Inv_Obj.getCustNo().toString() ;
            final  String Cust_Nm = Inv_Obj.getCustNm().toString() ;
            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                  /*  if (Excute_Op == 1) {
                        StartRound(Cust_No, Cust_Nm);
                        btn_GetData();
                    }


                    if (Excute_Op == 2) {
                        btn_EndRound(Cust_No);
                        btn_GetData();
                    }*/

                }
            });

            if(op!=0) {
               // alertDialog.show();

                   if (Excute_Op == 1) {
                        StartRound(Cust_No, Cust_Nm);
                        btn_GetData();
                    }


                    if (Excute_Op == 2) {
                        btn_EndRound(Cust_No);
                        btn_GetData();
                    }


            }
            else
            {

                 alert_Dialog.setMessage("لا يمكن فتح جولة ");
                alert_Dialog.setIcon(R.drawable.delete);
                alert_Dialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert_Dialog.show();

            }
        }

    }




}


    public class FetchTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id", "12345"));
                nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                String line = "0";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
                String result11 = sb.toString();

                // parsing data
                return new JSONArray(result11);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            if (result != null) {
                // do something
            } else {
                // error occured
            }
        }
    }
    private double getElevationFromGoogleMaps(double longitude, double latitude) {
        double result = Double.NaN;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String url = "http://maps.googleapis.com/maps/api/elevation/"
                + "xml?locations=" + String.valueOf(latitude)
                + "," + String.valueOf(longitude)
                + "&sensor=true";
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int r = -1;
                StringBuffer respStr = new StringBuffer();
                while ((r = instream.read()) != -1)
                    respStr.append((char) r);
                String tagOpen = "<elevation>";
                String tagClose = "</elevation>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result = (double)(Double.parseDouble(value)*3.2808399); // convert from meters to feet
                }
                instream.close();
            }
        }
        catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
    @SuppressLint("Range")
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
        if (max.length()==1) {
            max = intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5);
        }
        else {
            max= (intToString(Integer.valueOf(max), 7));
        }
        return max;
    }

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    public void StartRound(String CustNo,String  CustNm) {
        String OrderNo = GetMaxPONo();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler=new SqlHandler(this);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        String Tr_Date  , Tr_Time;

        Tr_Date =currentDateandTime;

        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        Tr_Time = StringTime;


        ContentValues cv = new ContentValues();
        cv.put("CusNo",CustNo);
        cv.put("ManNo",sharedPreferences.getString("UserID", ""));
        cv.put("DayNum",String.valueOf(dayOfWeek));
        cv.put("Tr_Data", Tr_Date);
        cv.put("Start_Time",Tr_Time);
        cv.put("Closed","0");
        cv.put("Posted", "-1");
        cv.put("OrderNo", OrderNo);


        long i;
        i = sqlHandler.Insert("SaleManRounds", null, cv);
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();


        alertDialog.setTitle("المجرة الدولية");
        if (i>0) {

            SharedPreferences.Editor editor    = sharedPreferences.edit();
            editor.putString("CustNo", CustNo);
            editor.putString("CustNm", CustNm);
            editor.putString("CustAdd",CustNo);
            editor.putString("PayCount","0");
            editor.putString("InvCount","0");
            editor.putString("V_OrderNo", OrderNo);
            editor.commit();


            Bundle bundle = new Bundle();
            android.app.FragmentManager Manager = getFragmentManager();

            PopSmallMenue obj = new PopSmallMenue();
            bundle.putString("Msg",  CustNo + "   " +CustNm );
            obj.setArguments(bundle);
            obj.show(Manager, null);


            // alertDialog.setMessage("عملية بداية الجولة تمت بنجاح");
            //alertDialog.setIcon(R.drawable.tick);

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


       // alertDialog.show();





    }
    public void btn_EndRound(String CustNo) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler=new SqlHandler(this);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        String Tr_Date  , Tr_Time;

        Tr_Date =currentDateandTime;

        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        Tr_Time = StringTime;

        ContentValues cv = new ContentValues();

        cv.put("Tr_Data", Tr_Date.toString());
        cv.put("End_Time",Tr_Time.toString());
        cv.put("Closed", "1");


        long i;
        i = sqlHandler.Update("SaleManRounds", cv, "Closed =0 And CusNo  = '"+CustNo.toString()+"'");
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();


        alertDialog.setTitle("المجرة الدولية");
        if (i>0) {

            SharedPreferences.Editor editor    = sharedPreferences.edit();
            editor.putString("CustNo", "");
            editor.putString("CustNm", "");
            editor.putString("CustAdd", "");
            editor.putString("V_OrderNo", "-1");
            editor.commit();

            alertDialog.setMessage("نهاية الجولة تمت بنجاح");
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


        alertDialog.show();



    }


    private void   FillDeptf(){
        final Spinner sp_day = (Spinner) findViewById(R.id.sp_day);
        String  query = "Select  Type_No , Type_Name , etname from deptf";
        ArrayList<Cls_Day> cls_days = new ArrayList<Cls_Day>();
        cls_days.clear();
        Cls_Day cls_day;
        for(int i = 1 ;i<8;i++) {
            cls_day = new Cls_Day();
            cls_day.setDayNo(String.valueOf(i));
            cls_day.setDayNm(GetDayName(i));
            cls_days.add(cls_day);
        }

        Cls_Day_Adapter cls_day_adapter = new Cls_Day_Adapter(
                this, cls_days);
        sp_day.setAdapter(cls_day_adapter);

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

    @SuppressLint("Range")
    public void btn_GetData() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        final String currentDateandTime = sdf.format(new Date());

        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();



        Calendar c = Calendar.getInstance();
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                    Integer i;
        String q = "";


                     cls_daily_visitses.clear();

                    Cls_Daily_Visits clsDailyVisits = new Cls_Daily_Visits();

    clsDailyVisits.setCustNo(" " + " رقم العميل" + " ");
    clsDailyVisits.setCustNm("     " + "   اسم العميل" + "  ");
    clsDailyVisits.setCustType("  نوع العميل ");
    clsDailyVisits.setCustMobile("ذمة العميل");
    clsDailyVisits.setCustAddress("ذمة الشهر السابق");
    clsDailyVisits.setNotes("المسافة" + "(" + "متر" + ")");
    clsDailyVisits.setFlq("حالة الزيارة");
    clsDailyVisits.setTf("وقت البداية");
    clsDailyVisits.setTt(" وقت النهاية   ");
    clsDailyVisits.setSetColor("-2");
    clsDailyVisits.setBarcode("الباركود");




                     cls_daily_visitses.add(clsDailyVisits);
                    SqlHandler sqlHandler = new SqlHandler(ScheduleManActivity.this);
        float[] results = new float[1];
        int Dis ;

        Integer indexValue = sp_day.getSelectedItemPosition();
        Cls_Day o = (Cls_Day) sp_day.getItemAtPosition(indexValue);


                  q="-1";

                    // if (Week_Num == 1){

                         if (dayOfWeek== 7 )
                             q = " sat = '1' ";

                          else  if (dayOfWeek == 1 )
                             q = " sun = '1' ";

                         else  if (dayOfWeek == 2 )
                             q = " mon = '1' ";


                         else if (dayOfWeek == 3 )
                             q = " tues = '1' ";

                         else  if (dayOfWeek == 4 )
                             q = " wens = '1' ";

                         else  if  (dayOfWeek == 5 )
                             q = " thurs = '1' ";
                   //  }
                     /*if (Week_Num == 2){
                         if (dayOfWeek == 7 )
                             q = " sat1 = '1' ";

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

                     }*/

                  q = "Select DISTINCT  no,name ,Address , Note2 ,'' as Mobile  ,ifnull(Latitude,0.0) as Latitude   , ifnull(Longitude,0.0) as Longitude" +
                          "  ,ifnull(barCode,-1)  as barCode   " +
                          "  ,ifnull( CUST_PRV_MONTH,0) as CUST_PRV_MONTH  , ifnull(CUST_NET_BAL,0) as CUST_NET_BAL , Pay_How ,ifnull( CustType,'****')  as CustType, PAMENT_PERIOD_NO from Customers where   " + q ;

                   // Toast.makeText(this,q,Toast.LENGTH_SHORT).show();
                    Cursor c1 =  sqlHandler.selectQuery(q);
                    i = 0;
                    if (c1 != null && c1.getCount() != 0) {
                        if (c1.moveToFirst()) {
                            do {
                                //date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear
                                clsDailyVisits = new Cls_Daily_Visits();

                                clsDailyVisits.setCustNo(c1.getString(c1
                                        .getColumnIndex("no")));
                                clsDailyVisits.setCustNm(c1.getString(c1
                                        .getColumnIndex("name")));
                                clsDailyVisits.setCustMobile(c1.getString(c1
                                        .getColumnIndex("CUST_NET_BAL")));
                                clsDailyVisits.setCustAddress(c1.getString(c1
                                        .getColumnIndex("CUST_PRV_MONTH")));

                                clsDailyVisits.setBarcode(c1.getString(c1
                                        .getColumnIndex("barCode")));


                                clsDailyVisits.setCustType(c1.getString(c1
                                        .getColumnIndex("CustType")));

                                Location selected_location=new Location("locationA");
                                if (c1.getString(c1.getColumnIndex("Latitude")).equals("")){
                                    selected_location.setLatitude(0.0);
                                }
                                else
                                {
                                    selected_location.setLatitude(Double.parseDouble(c1.getString(c1.getColumnIndex("Latitude"))));
                               }

                                if (c1.getString(c1.getColumnIndex("Longitude")).equals("")){
                                    selected_location.setLongitude(0.0);
                                }
                                else
                                {
                                    selected_location.setLongitude(Double.parseDouble(c1.getString(c1.getColumnIndex("Longitude"))));
                                }



//32.018105
                                //

                                String tv_x = "";
                                String tv_y ="";
                                Location near_locations=new Location("locationA");
                               try {
                                    // tv_x = (String.valueOf(mGPSService.getLatitude()).substring(0, String.valueOf(mGPSService.getLatitude()).indexOf(".") + 5));
                                    // tv_y = (String.valueOf(mGPSService.getLongitude()).substring(0, String.valueOf(mGPSService.getLongitude()).indexOf(".") + 5));

                                   tv_x = (String.valueOf(mGPSService.getLatitude()));
                                   tv_y = (String.valueOf(mGPSService.getLongitude()));
                                }
                                catch (Exception ex){
                                    tv_x="0.0";
                                    tv_y ="0.0";
                                }


                               // tv_x = (String.valueOf("32.018087"));
                               // tv_y = (String.valueOf("35.893956"));

                                near_locations.setLatitude(Double.parseDouble(tv_x));
                                near_locations.setLongitude(Double.parseDouble(tv_y));


                               /* selected_location.setLatitude(Double.parseDouble("32.020599"));
                                selected_location.setLongitude(Double.parseDouble("35.875937"));

                                selected_location.setLatitude(Double.parseDouble("31.2572"));
                                selected_location.setLongitude(Double.parseDouble("35.7367"));*/




                                Location.distanceBetween(selected_location.getLatitude(), selected_location.getLongitude(),
                                        near_locations.getLatitude(), near_locations.getLongitude(),
                                        results);

                               double distance=selected_location.distanceTo(near_locations);
                                distance=distance/1000;

                                //distance = getElevationFromGoogleMaps(near_locations.getLatitude(),near_locations.getLongitude());
                                //Cls_HttpGet_Location task = new Cls_HttpGet_Location();
                                //try {
                                //    String result = task.execute(String.valueOf(near_locations.getLatitude()), String.valueOf(near_locations.getLongitude())).get();
                                //}
                               // catch (Exception ex){}

                                Dis = (int) results[0] ;
                                clsDailyVisits.setNotes(Dis + "");
                                if( selected_location.getLatitude()==0.0 ||selected_location.getLongitude()==0.0 || near_locations.getLatitude()==0.0 ||near_locations.getLongitude()==0.0 ){
                                     clsDailyVisits.setNotes("غير محدد");

                                }




                               String qq = "select Start_Time,End_Time ,ifnull( Closed,-1) as Closed   from SaleManRounds where Tr_Data =  '"+  currentDateandTime.toString()  +"'" +
                               " And CusNo='"+ c1.getString(c1.getColumnIndex("no")) +"'  order by no desc limit 1" ;
                                Cursor cc =  sqlHandler.selectQuery(qq);



                                if (cc != null && cc.getCount() != 0) {
                                    cc.moveToFirst()  ;

                                    if(cc.getString(cc.getColumnIndex("Closed")).equals("0")) {
                                        clsDailyVisits.setFlq("تم فتح الزيارة");
                                        clsDailyVisits.setSetColor("0");
                                    }

                                    if(cc.getString(cc.getColumnIndex("Closed")).equals("1")) {
                                        clsDailyVisits.setFlq("تمت الزيارة");
                                        clsDailyVisits.setSetColor("1");
                                    }
                                    clsDailyVisits.setStates(cc.getString(cc.getColumnIndex("Closed")));

                                    clsDailyVisits.setTf(cc.getString(cc
                                            .getColumnIndex("Start_Time")));
                                    clsDailyVisits.setTt(cc.getString(cc
                                            .getColumnIndex("End_Time")));
                                    cc.close();
                                }
                                else {
                                    clsDailyVisits.setFlq("لم تتم ");
                                    clsDailyVisits.setStates("-1");

                                    clsDailyVisits.setSetColor("-1");
                                    clsDailyVisits.setTf("0:0:0");
                                    clsDailyVisits.setTt("0:0:0");
                                }




                                cls_daily_visitses.add(clsDailyVisits);


                            } while (c1.moveToNext());

                        }
                        c1.close();
                    }
        Cls_Daily_Visits_Adapter cls_daily_visits_adapter = new Cls_Daily_Visits_Adapter(
                ScheduleManActivity.this, cls_daily_visitses);
        Cust_Visits.setAdapter(cls_daily_visits_adapter);

        mGPSService.closeGPS();
    }

    public void btn_back(View view) {
        Intent i = new Intent(this,GalaxyMainActivity.class);
        startActivity(i);
    }
    public  String GetDayName(Integer Day){
        String DayNm ="" ;

            if (Day == 2) DayNm = "الاثنين";
            else if (Day == 3) DayNm = "الثلاثاء";
            else if (Day == 4) DayNm = "الاربعاء";
            else if (Day == 5) DayNm = "الخميس";
            else if (Day == 6) DayNm = "الجمعة";
            else if (Day == 7) DayNm = "السبت";
            else if (Day == 1) DayNm = "الاحد";


        return  DayNm;

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
