package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cds_jo.pharmacyGI.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
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
String CurrentYear="";
    MyTextView tv_Area,tv_TotalQty,tv_TotalPrice,tv_Total;
    double Total_Qty,Total_Price,Total_Row;
    RadioButton rdoSales,rdoReturn,rdoCompare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_sales);

        CurrentYear= DB.GetValue(this,"SERVER_DATETIME","MYEAR","1=1");
        rdoSales =(RadioButton)findViewById(R.id.rdoSales);
        rdoReturn =(RadioButton)findViewById(R.id.rdoReturn);
        rdoCompare =(RadioButton)findViewById(R.id.rdoCompare);
        rdoSales.setChecked(true);


        tv_Area =(MyTextView)findViewById(R.id.tv_Area);
        tv_TotalQty =(MyTextView)findViewById(R.id.tv_TotalQty);
        tv_TotalPrice =(MyTextView)findViewById(R.id.tv_TotalPrice);
        tv_Total =(MyTextView)findViewById(R.id.tv_Total);

        tv_TotalQty.setText("0");
        tv_TotalPrice.setText("0.000");
        tv_Total.setText("0.000");

        Fragment frag=new Header_Frag();
       FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

        Row1 = (LinearLayout)findViewById(R.id.LinearRow1);
        Row2 = (RelativeLayout)findViewById(R.id.RaltiveRow2);


            Row1.setBackgroundResource(R.mipmap.row1);
            Row2.setBackgroundResource(R.mipmap.row2);


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
        if(CurrentYear.equalsIgnoreCase("-1")){
            CurrentYear=year+"";
        }
        if (FlgDate == 1) {
           /* FromDate.setText(new StringBuilder().append(intToString(Integer.valueOf(day), 2)).append("/")
                    .append(intToString(Integer.valueOf(month), 2)).append("/").append(year));*/

            FromDate.setText(new StringBuilder().append(intToString(Integer.valueOf(day), 2)).append("/")
                    .append(intToString(Integer.valueOf(month), 2)).append("/").append(CurrentYear));
        }

        if (FlgDate == 2) {
            ToDate.setText(new StringBuilder().append(intToString(Integer.valueOf(day), 2)).append("/")
                    .append(intToString(Integer.valueOf(month), 2)).append("/").append(CurrentYear));
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


    }
    String ManNo = "";
    String Area = "";
    public void SetMan(final String No, String Nm) {
         ManNo    = No ;


        //onProgressUpdate();

    }

    public void btn_CountrySearch(View view) {
        tv_Area.setText("");

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "CountrySales");
        android.app.FragmentManager Manager = getFragmentManager();
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
    private  void ShowComapre(){


        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("Area",Area);
        bundle.putString("ManNo",ManNo);
        bundle.putString("FromDate",  FromDate.getText().toString());
        bundle.putString("ToDate",  ToDate.getText().toString());


        android.app.FragmentManager Manager = getFragmentManager();
        Pop_Po_CompareSales obj = new Pop_Po_CompareSales();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_GetData(View view) {
        onProgressUpdate();
    }
    public void onProgressUpdate( ){


        if (rdoCompare.isChecked()== true){

            ShowComapre();
        }else {

            if (tv_Area.getText().toString().equalsIgnoreCase("")) {
                Area = "-1";
            }


            final List<String> items_ls = new ArrayList<String>();
            items_Lsit = (ListView) findViewById(R.id.lst_acc);
            items_Lsit.setAdapter(null);


            FromDate.setError(null);
            ToDate.setError(null);

            if (FromDate.getText().toString().length() <= 0) {
                FromDate.setError("*");
                FromDate.requestFocus();
                return;
            }

            if (ToDate.getText().toString().length() <= 0) {
                ToDate.setError("*");
                ToDate.requestFocus();
                return;
            }


            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getResources().getText(R.string.From_Date));
            alertDialog.setMessage(getResources().getText(R.string.PleaseCheckDate));
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    FromDate.setText("");
                    FromDate.requestFocus();
                }
            });


            alertDialog.setTitle(getResources().getText(R.string.To_Date));
            alertDialog.setMessage(getResources().getText(R.string.PleaseCheckDate));
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
                    if (rdoSales.isChecked() == true) {
                        ws.CallCountrySalesReport(Area, ManNo, FromDate.getText().toString(), ToDate.getText().toString());
                    } else {
                        ws.CallCountryReturnsReport(Area, ManNo, FromDate.getText().toString(), ToDate.getText().toString());
                    }


                    try {
                        float t_dept, t_cred, t_bb, tot, t_tot, temp, t_rate;
                        t_dept = t_cred = t_bb = tot = t_tot = temp = t_rate = 0;
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);


                        JSONArray js_item_no = js.getJSONArray("item_no");
                        JSONArray js_qty = js.getJSONArray("qty");
                        JSONArray js_Item_Name = js.getJSONArray("Item_Name");
                        JSONArray js_Type_no = js.getJSONArray("Type_no");
                        JSONArray js_BillDate = js.getJSONArray("BillDate");
                        JSONArray js_cusname = js.getJSONArray("cusname");
                        JSONArray js_ManName = js.getJSONArray("ManName");
                        JSONArray js_UnitName = js.getJSONArray("UnitName");
                        JSONArray js_price = js.getJSONArray("price");


                        final ArrayList<Cls_Country_Sales> cls_acc_reportsList = new ArrayList<Cls_Country_Sales>();

                        Cls_Country_Sales cls_acc_report = new Cls_Country_Sales();


                        cls_acc_report.setQty("الكمية");
                        cls_acc_report.setUnitNm("الوحدة");
                        cls_acc_report.setPrice("السعر");
                        cls_acc_report.setManNm("المجموع");
                        cls_acc_report.setCusNm("العميل");
                        cls_acc_report.setDate(getResources().getText(R.string.date) + "");

                        cls_acc_report.setItemNm("المادة");


                        cls_acc_reportsList.add(cls_acc_report);
                        Total_Qty = 0;
                        Total_Price = 0;
                        Total_Row = 0;


                        for (i = 0; i < js_item_no.length(); i++) {
                            cls_acc_report = new Cls_Country_Sales();

                            cls_acc_report.setItemNo(js_item_no.get(i).toString());
                            cls_acc_report.setQty(js_qty.get(i).toString());
                            cls_acc_report.setItemNm(js_Item_Name.get(i).toString());
                            cls_acc_report.setDate(js_Type_no.get(i).toString());
                            cls_acc_report.setDate(js_BillDate.get(i).toString());
                            cls_acc_report.setCusNm(js_cusname.get(i).toString());
                            cls_acc_report.setPrice(js_price.get(i).toString());


                            cls_acc_report.setManNm(js_ManName.get(i).toString());
                            cls_acc_report.setUnitNm(js_UnitName.get(i).toString());

                            cls_acc_reportsList.add(cls_acc_report);

                            Total_Qty = Total_Qty + SToD(js_qty.get(i).toString());
                            Total_Price = Total_Price + SToD(js_price.get(i).toString());
                            Total_Row = Total_Row + (SToD(js_qty.get(i).toString()) * SToD(js_price.get(i).toString()));


                            custDialog.setMax(js_item_no.length());
                            custDialog.incrementProgressBy(1);

                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                            }

                        }

                        _handler.post(new Runnable() {

                            public void run() {


                                Total_Qty = SToD(Total_Qty + "");
                                Total_Price = SToD(Total_Price + "");
                                Total_Row = SToD(Total_Row + "");

                                tv_TotalQty.setText(Total_Qty + "");
                                tv_TotalPrice.setText(Total_Price + "");
                                tv_Total.setText(Total_Row + "");


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
                                    alertDialog.setMessage("لا يوجد بيانات");
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



    }
    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
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

    public void btn_back(View view) {
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
