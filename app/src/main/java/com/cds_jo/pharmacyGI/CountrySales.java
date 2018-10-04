package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Cls_Acc_Report;
import com.cds_jo.pharmacyGI.assist.Cls_Acc_Report_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Methdes.MyTextView;
import hearder.main.Header_Frag;

public class CountrySales extends FragmentActivity {
LinearLayout Row1 ;
    ListView items_Lsit;
    RelativeLayout Row2;
    Methdes.MyTextView FromDate;
    Methdes.MyTextView ToDate ;
    public int FlgDate = 0;
    private Calendar calendar;
    private int year, month, day;
    NumberFormat nf_out;
    MyTextView ManNm;
    MyTextView tv_Area;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_sales);

          ManNm =(MyTextView)findViewById(R.id.tv_ManNm);
        tv_Area =(MyTextView)findViewById(R.id.tv_Area);


        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

        Row1 = (LinearLayout)findViewById(R.id.LinearRow1);
        Row2 = (RelativeLayout)findViewById(R.id.RaltiveRow2);

        if(ComInfo.Lan.equalsIgnoreCase("ar")){
            Row1.setBackgroundResource(R.mipmap.row1);
            Row2.setBackgroundResource(R.mipmap.row2);
        }

        FromDate = (Methdes.MyTextView)findViewById(R.id.ed_FromDate);
        ToDate = (Methdes.MyTextView)findViewById(R.id.ed_ToDate);


        nf_out = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf_out.setMaximumFractionDigits(3);
        nf_out.setMinimumFractionDigits(3);

        final Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);


        calendar = Calendar.getInstance();
        year = c.get(Calendar.YEAR);

        month = c.get(Calendar.MONTH);

        day = c.get(Calendar.DAY_OF_MONTH);
        showDate(year, month, day);

        FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlgDate = 1;
                showDialog(999);


            }
        });

        ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlgDate = 2 ;
                showDialog(999);


            }
        });

        items_Lsit=(ListView)findViewById(R.id.lst_acc);
        items_Lsit.setAdapter(null);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ManNo= sharedPreferences.getString("UserID", "");

        ManNm.setText( sharedPreferences.getString("UserName", ""));
        ManNm.setError(null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String currentYear = sdf.format(new Date());


        FromDate.setText("01/01/"+currentYear);
        ToDate.setText("31/12/"+currentYear);
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year,month, day);
        }
        return null;
    }
    public void setDate(View view) {
        showDialog(999);
       /* Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();*/
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        if (FlgDate == 1) {
            FromDate.setText(new StringBuilder().append(intToString(Integer.valueOf(day), 2)).append("/")
                    .append(intToString(Integer.valueOf(month), 2)).append("/").append(year));
        }

        if (FlgDate == 2) {
            ToDate.setText(new StringBuilder().append(intToString(Integer.valueOf(day), 2)).append("/")
                    .append(intToString(Integer.valueOf(month), 2)).append("/").append(year));
        }
    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    public void btn_searchMan(View view) {

       /* ManNm.setText("");

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "CountrySales");
        FragmentManager Manager =  getFragmentManager();
        Select_Man obj = new Select_Man();
        obj.setArguments(bundle);
        obj.show(Manager, null);*/

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ManNo= sharedPreferences.getString("UserID", "");

        ManNm.setText( sharedPreferences.getString("UserName", ""));
        ManNm.setError(null);

    }
    String ManNo = "";
    String Area = "";
    public void SetMan(final String No, String Nm) {
         ManNo    = No ;
         ManNm.setText(Nm);
         ManNm.setError(null);

        //onProgressUpdate();

    }

    public void btn_CountrySearch(View view) {
        tv_Area.setText("");

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "CountrySales");
        FragmentManager Manager =  getFragmentManager();
        Select_Location obj = new Select_Location();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void SetLocation(final String No, String Nm) {
        Area    = No ;
        tv_Area.setText(Nm);
        tv_Area.setError(null);



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void btn_GetData(View view) {
        onProgressUpdate();
    }
    public void onProgressUpdate( ){



        final List<String> items_ls = new ArrayList<String>();
        items_Lsit=(ListView)findViewById(R.id.lst_acc);
        items_Lsit.setAdapter(null);



        FromDate.setError(null);
        ToDate.setError(null);

        if ( FromDate.getText().toString().length()   <= 0) {
            FromDate.setError("*");
            FromDate.requestFocus();
            return;
        }

        if ( ToDate.getText().toString().length()   <= 0) {
            ToDate.setError("*");
            ToDate.requestFocus();
            return;
        }


        AlertDialog alertDialog  ;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResources().getText(R.string.From_Date));
        alertDialog.setMessage( getResources().getText(R.string.PleaseCheckDate));
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setButton(getResources().getText(R.string.Ok)    , new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FromDate.setText("");
                FromDate.requestFocus();
            }
        });




        alertDialog.setTitle(getResources().getText(R.string.To_Date));
        alertDialog.setMessage( getResources().getText(R.string.PleaseCheckDate));
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ToDate.setText("");
                ToDate.requestFocus();

            }
        });





        final Handler _handler = new Handler();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(CountrySales.this);

        custDialog.setTitle(getResources().getText(R.string.PleaseWait));
        custDialog.setMessage(getResources().getText(R.string.Retrive_DataUnderProcess));
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.show();




        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(CountrySales.this);
                ws.CallCountrySalesReport(Area , ManNo,   FromDate.getText().toString(),ToDate.getText().toString());



                try {
                    float t_dept , t_cred,t_bb ,tot ,t_tot,temp,t_rate;
                    t_dept= t_cred=t_bb =tot =t_tot =temp=t_rate =  0 ;
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);


                    JSONArray js_item_no= js.getJSONArray("item_no");
                    JSONArray js_qty= js.getJSONArray("qty");
                    JSONArray js_Item_Name= js.getJSONArray("Item_Name");
                    JSONArray js_Type_no= js.getJSONArray("Type_no");
                    JSONArray js_BillDate= js.getJSONArray("BillDate");
                    JSONArray js_cusname= js.getJSONArray("cusname");
                    JSONArray js_ManName= js.getJSONArray("ManName");
                    JSONArray js_UnitName= js.getJSONArray("UnitName");
                    JSONArray js_price= js.getJSONArray("price");




                    final  ArrayList<Cls_Country_Sales> cls_acc_reportsList = new ArrayList<Cls_Country_Sales>();

                    Cls_Country_Sales cls_acc_report = new Cls_Country_Sales();




                    cls_acc_report.setQty("الكمية");
                    cls_acc_report.setUnitNm("الوحدة");
                    cls_acc_report.setPrice("السعر");
                    cls_acc_report.setManNm("المندوب");
                    cls_acc_report.setCusNm("العميل");
                    cls_acc_report.setDate(getResources().getText(R.string.date)+"");

                    cls_acc_report.setItemNm("المادة");



                    cls_acc_reportsList.add(cls_acc_report);



                    for( i =0 ; i<js_item_no.length();i++)
                    {
                        cls_acc_report = new Cls_Country_Sales();

                        cls_acc_report.setItemNo(js_item_no.get(i).toString());
                        cls_acc_report.setQty(js_qty.get(i).toString());
                        cls_acc_report.setItemNm(js_Item_Name.get(i).toString());
                        cls_acc_report.setDate(js_Type_no.get(i).toString());
                        cls_acc_report.setDate(js_BillDate.get(i).toString());
                        cls_acc_report.setCusNm(js_cusname.get(i).toString());
                        cls_acc_report.setPrice(js_price.get(i).toString());


                        cls_acc_report.setManNm(   js_ManName.get(i).toString()  );
                        cls_acc_report.setUnitNm(  js_UnitName.get(i).toString() );

                        cls_acc_reportsList.add(cls_acc_report);


                        custDialog.setMax(js_item_no.length());
                        custDialog.incrementProgressBy(1);

                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }

                    }

                    _handler.post(new Runnable() {

                        public void run() {




                            Cls_CountrySales_Report_Adapter cls_acc_report_adapter = new Cls_CountrySales_Report_Adapter(
                                    CountrySales.this, cls_acc_reportsList);

                            items_Lsit.setAdapter(cls_acc_report_adapter);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    CountrySales.this).create();
                            alertDialog.setTitle("تحديث البيانات");

                            alertDialog.setMessage("تمت عملية استرجاع  البيانات بنجاح ");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });


                            custDialog.dismiss();
                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    CountrySales.this).create();
                            alertDialog.setTitle("كشف  المبيعات  ");
                            if (We_Result.ID == -404) {
                                alertDialog.setMessage(We_Result.Msg);
                            } else {
                                alertDialog.setMessage("لا يوجد بيانات" );
                            }
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

    public void btn_fromYear(View view) {

        DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date today = new Date();

        Calendar c1 = Calendar.getInstance();

        c1.setTime(today);
        c1.set(Calendar.DAY_OF_YEAR, 1);

        FromDate.setText(sdf1.format( c1.getTime()) +"");
        c1.setTime(today);


    }

    public void btn_fromMonth(View view) {


        DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date today = new Date();

        Calendar c1 = Calendar.getInstance();

        c1.setTime(today);
        c1.set(Calendar.DAY_OF_MONTH, 1);

        FromDate.setText(sdf1.format( c1.getTime()) +"");


    }
}
