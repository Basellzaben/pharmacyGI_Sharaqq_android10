package com.cds_jo.pharmacyGI.assist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.MainActivity;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.Select_Customer;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.We_Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import Methdes.MethodToUse;
import Methdes.MyTextView;
import hearder.main.Header_Frag;

//import com.loopj.android.http.HttpGet;// temp

public class Acc_ReportActivity extends FragmentActivity {
    ListView items_Lsit;
    public ProgressDialog loadingdialog;
    private Calendar calendar;
    private int year, month, day;
    public int FlgDate = 0;
    Methdes.MyTextView FromDate;
    Methdes.MyTextView ToDate ;
    SqlHandler sqlHandler;
    TextView acc;
    TextView CustNm;
    MyTextView CheqBal;
    MyTextView Ball ;
    MyTextView CusTop;
    MyTextView NetBall;
    NumberFormat nf_out;
    ArrayList<Cls_Acc_Report> cls_acc_reportsList;
    String Amt;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_ee);

        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

        items_Lsit=(ListView)findViewById(R.id.lst_acc);
         cls_acc_reportsList = new ArrayList<Cls_Acc_Report>();
         CheqBal = (MyTextView)findViewById(R.id.tv_CheqBal);
         Ball = (MyTextView)findViewById(R.id.tv_Ball);
         CusTop = (MyTextView)findViewById(R.id.tv_CusTop);
         NetBall = (MyTextView)findViewById(R.id.tv_NetBall);

         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
         CustNm =(TextView)findViewById(R.id.tv_cusnm);
         acc = (TextView)findViewById(R.id.tv_acc);

        Button Retrive=(Button)findViewById(R.id.button17);
        Retrive.setTypeface(MethodToUse.SetTFace(Acc_ReportActivity.this));

        acc.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));


        CustNm.setError(null);

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

        FromDate.setOnClickListener(new OnClickListener() {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String currentYear = sdf.format(new Date());


        FromDate.setText("01/01/"+currentYear);
        ToDate.setText("31/12/"+currentYear);


        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_Acc_Report o = (Cls_Acc_Report) items_Lsit.getItemAtPosition(position);

                if(o.getDoctype().equalsIgnoreCase("2")){
                    Amt=o.getDept();
                }else if (o.getDoctype().equalsIgnoreCase("3")){
                    Amt=o.getCred();
                }
                showDtl(o.getDoc_num(),o.getDoctype(),Amt+"");
            }
        });

    }
    private  void showDtl(String DocNo , String DocType,String Amt){


        if(DocType.equalsIgnoreCase("2")   ||DocType.equalsIgnoreCase("3") ) {
            Bundle bundle = new Bundle();
            bundle.putString("Scr", "po");
            bundle.putString("DocNo", DocNo);
            bundle.putString("Amt", Amt);
            FragmentManager Manager = getFragmentManager();
           /* PopShowInvoiceDtl obj = new PopShowInvoiceDtl();
            obj.setArguments(bundle);
            obj.show(Manager, null);*/

        }
    }
    public void btnPrint(View view) {

//        TextView tv_acc = (TextView)findViewById(R.id.tv_acc);
//        TextView tv_cusnm = (TextView)findViewById(R.id.tv_cusnm);
//        EditText ed_ToDate = (EditText)findViewById(R.id.ed_ToDate);
//        EditText ed_FromDate = (EditText)findViewById(R.id.ed_FromDate);



        sqlHandler = new SqlHandler(this);

        String q ="Delete from  ACC_REPORT where Cust_No ='"+ acc.getText().toString()+"'";
        q ="Delete from  ACC_REPORT ";
        sqlHandler.executeQuery(q);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        Long i;
        ContentValues cv =new ContentValues();
        for (int x = 0; x < cls_acc_reportsList.size(); x++) {
            Cls_Acc_Report contactListItems = new Cls_Acc_Report();
            contactListItems = cls_acc_reportsList.get(x);


            cv = new ContentValues();



            cv.put("Cust_No", acc.getText().toString());
            cv.put("Cust_Nm", CustNm.getText().toString());
            cv.put("FDate",FromDate.getText().toString());
            cv.put("TDate", ToDate.getText().toString());
            cv.put("TrDate", currentDateandTime);
            cv.put("Tot", contactListItems.getTot().toString().replace(",", ""));
            cv.put("Rate", contactListItems.getRate().toString().replace(",", ""));
            cv.put("Cred", contactListItems.getCred().toString().replace(",", ""));
            cv.put("Dept", contactListItems.getDept().toString().replace(",", ""));
            cv.put("Bb", contactListItems.getBb().toString().replace(",", ""));
            cv.put("Des", contactListItems.getDes().toString().replace(",", ""));
            cv.put("Date", contactListItems.getDate().toString()==null ?"" :  contactListItems.getDate().toString());
            cv.put("Cur_no", contactListItems.getCur_no().toString().replace(",", ""));
            cv.put("Doctype", contactListItems.getDoctype().toString().replace(",", ""));
            cv.put("Doc_num", contactListItems.getDoc_num().toString().replace(",", ""));
            cv.put("CheqBal", CheqBal.getText().toString());
            cv.put("Ball",  Ball.getText().toString());
            cv.put("CusTop",  CusTop.getText().toString());
            cv.put("NetBall",  NetBall.getText().toString());
            cv.put("Notes", "");
            i = sqlHandler.Insert("ACC_REPORT", null, cv);
        }

        Intent k = new Intent(this, Convert_ccReportTo_ImgActivity.class);


        k.putExtra("Scr", "po");
        CustNm =(TextView)findViewById(R.id.tv_cusnm);

        TextView   accno = (TextView)findViewById(R.id.tv_acc);
        k.putExtra("cusnm", CustNm.getText().toString());
        k.putExtra("OrderNo","");
        k.putExtra("accno", accno.getText().toString());

        startActivity(k);

    }
     private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",","");
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


        String[] parts = dateOfBirth.split("/");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        if ( SToD(part1) <0  || SToD(part1)>31 ){
            valid = false;
            return valid;
        }
        if ( SToD(part2) <0  || SToD(part2)>12 ){
            valid = false;
            return valid;
        }

        if ( SToD(part3) <1990  || SToD(part3)>2050 ){
            valid = false;
            return valid;
        }

        return valid;
    }
    public void onProgressUpdate( ){



        final List<String> items_ls = new ArrayList<String>();

        items_Lsit.setAdapter(null);



        final   TextView acc = (TextView)findViewById(R.id.tv_acc);


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

        if(!isValidDate(FromDate.getText().toString())){
            alertDialog.show();
            return;
        }



        alertDialog.setTitle(getResources().getText(R.string.To_Date));
        alertDialog.setMessage( getResources().getText(R.string.PleaseCheckDate));
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ToDate.setText("");
                ToDate.requestFocus();

            }
        });

        if(!isValidDate(ToDate.getText().toString())){

            alertDialog.show();
            return;
        }

        CheqBal.setText("");
        Ball.setText("");
        CusTop.setText("");
        NetBall.setText("");

        final Handler _handler = new Handler();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(Acc_ReportActivity.this);

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

                CallWebServices ws = new CallWebServices(Acc_ReportActivity.this);
               ws.CallReport(acc.getText().toString(), FromDate.getText().toString(),ToDate.getText().toString(),"");


                try {
                     float t_dept , t_cred,t_bb ,tot ,t_tot,temp,t_rate;
                     t_dept= t_cred=t_bb =tot =t_tot =temp=t_rate =  0 ;
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);


                    JSONArray js_doc_num= js.getJSONArray("doc_num");
                    JSONArray js_doctype= js.getJSONArray("doctype");
                    JSONArray js_cur_no= js.getJSONArray("cur_no");
                    JSONArray js_date= js.getJSONArray("date");
                    JSONArray js_des= js.getJSONArray("des");
                    JSONArray js_bb= js.getJSONArray("bb");
                    JSONArray js_dept= js.getJSONArray("dept");
                    JSONArray js_cred= js.getJSONArray("cred");
                   JSONArray js_rate= js.getJSONArray("rate");




                    cls_acc_reportsList = new ArrayList<Cls_Acc_Report>();

                    Cls_Acc_Report cls_acc_report = new Cls_Acc_Report();

                  /*  cls_acc_report.setTot("الرصيد");
                    cls_acc_report.setRate("العملة المقابلة");
                    cls_acc_report.setCred("الدائن");
                    cls_acc_report.setDept("المدين");
                    cls_acc_report.setBb("رصيد بداية مدة");
                    cls_acc_report.setDes(" البيــــــــان");
                    cls_acc_report.setDate("التاريــخ");
                    cls_acc_report.setCur_no("العملة");
                    cls_acc_report.setDoctype("نوع المستند ");
                    cls_acc_report.setDoc_num("رقم المستند");*/



                    cls_acc_report.setTot(getResources().getText(R.string.Balance)+"");
                    cls_acc_report.setRate(getResources().getText(R.string.CurrencyValue)+"");
                    cls_acc_report.setCred(getResources().getText(R.string.Credit)+"");
                    cls_acc_report.setDept(getResources().getText(R.string.Debt)+"");
                    cls_acc_report.setBb(getResources().getText(R.string.Beginning_balance)+"");
                    cls_acc_report.setDes(getResources().getText(R.string.description)+"");
                    cls_acc_report.setDate(getResources().getText(R.string.date)+"");
                    cls_acc_report.setCur_no(getResources().getText(R.string.Currency)+"");
                    cls_acc_report.setDoctype(getResources().getText(R.string.DocType)+"");
                    cls_acc_report.setDoc_num(getResources().getText(R.string.DocNo)+"");



                      cls_acc_reportsList.add(cls_acc_report);

                    // date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear





                    for( i =0 ; i<js_doc_num.length();i++)
                    {
                        cls_acc_report = new Cls_Acc_Report();

                        cls_acc_report.setDoc_num(js_doc_num.get(i).toString());
                        cls_acc_report.setDoctype(js_doctype.get(i).toString());
                        cls_acc_report.setCur_no(js_cur_no.get(i).toString());
                        cls_acc_report.setDate(js_date.get(i).toString());
                        cls_acc_report.setDes(js_des.get(i).toString());

                        if(i==0)
                             cls_acc_report.setBb( nf_out.format(Double.parseDouble( js_bb.get(i).toString()))+"");
                        else
                            cls_acc_report.setBb("0.000");

                        cls_acc_report.setDept( nf_out.format(Double.parseDouble( js_dept.get(i).toString()))+"");
                        cls_acc_report.setCred( nf_out.format(Double.parseDouble( js_cred.get(i).toString()))+"");

                        if( Float.parseFloat( js_dept.get(i).toString())>0)
                            temp = Float.parseFloat(js_dept.get(i).toString()) * Float.parseFloat(js_rate.get(i).toString());
                        else
                            temp = Float.parseFloat(js_cred.get(i).toString()) * Float.parseFloat(js_rate.get(i).toString());


                        cls_acc_report.setRate( nf_out.format(temp)+"");
                        t_rate = t_rate + temp;
                        t_dept  = t_dept +  Float.parseFloat( js_dept.get(i).toString());
                        t_cred  = t_cred +  Float.parseFloat( js_cred.get(i).toString());
                        t_bb  =  Float.parseFloat( js_bb.get(i).toString());


                        if(i==0)
                            tot= tot +   (Float.parseFloat( js_dept.get(i).toString()) +   Float.parseFloat( js_bb.get(0).toString())  -   Float.parseFloat( js_cred.get(i).toString()));
                        else
                            tot= tot +   (Float.parseFloat( js_dept.get(i).toString())  -   Float.parseFloat( js_cred.get(i).toString()));


                        cls_acc_report.setTot(String.valueOf(nf_out.format(tot)));

                        cls_acc_reportsList.add(cls_acc_report);


                        custDialog.setMax(js_doc_num.length());
                        custDialog.incrementProgressBy(1);

                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }

                    }



                    final int total = i;
                    final  String   txtCheqBal = js.getString("CheqBal");
                    final  String   txtBall = js.getString("Ball");
                    final  String  txtCusTop = js.getString("CusTop");
                    final  String   txtNetBall = js.getString("NetBall");




                    final  String   S_t_bb =  String.valueOf(t_bb);
                    final  String   S_dept =  String.valueOf(t_dept);
                    final  String   S_cred =  String.valueOf(t_cred);
                    final  String   S_rate =  String.valueOf(t_rate);
                    final  String   S_tot =  String.valueOf(tot);


                    _handler.post(new Runnable() {

                        public void run() {


                            CheqBal.setText( nf_out.format(Double.parseDouble( txtCheqBal))+"");

                            Ball.setText( nf_out.format(Double.parseDouble( txtBall))+"");

                            CusTop.setText( nf_out.format(Double.parseDouble( txtCusTop))+"");

                            NetBall.setText( nf_out.format(Double.parseDouble( txtNetBall))+"");

                            Cls_Acc_Report cls_acc_report1 = new Cls_Acc_Report();
                            cls_acc_report1 = new Cls_Acc_Report();
                            cls_acc_report1.setCur_no  ("عدد الحركات");
                            cls_acc_report1.setDate((cls_acc_reportsList.size()-1)+"");
                            cls_acc_report1.setDoctype("");
                            cls_acc_report1.setDoc_num("");
                            cls_acc_report1.setDes("المجموع");
                            cls_acc_report1.setBb(nf_out.format(Double.parseDouble( S_t_bb))+"" );
                            cls_acc_report1.setDept( nf_out.format(Double.parseDouble( S_dept))+"");
                            cls_acc_report1.setCred(nf_out.format(Double.parseDouble(S_cred))+"");
                            cls_acc_report1.setRate(nf_out.format(Double.parseDouble( S_rate))+"");
                            cls_acc_report1.setTot(nf_out.format(Double.parseDouble(S_tot))+"");
                            cls_acc_report1.setTot(nf_out.format(Double.parseDouble(S_tot))+"");


                            cls_acc_reportsList.add(cls_acc_report1);

                            Cls_Acc_Report_Adapter cls_acc_report_adapter = new Cls_Acc_Report_Adapter(
                                    Acc_ReportActivity.this, cls_acc_reportsList);

                            items_Lsit.setAdapter(cls_acc_report_adapter);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    Acc_ReportActivity.this).create();
                            alertDialog.setTitle("تحديث البيانات");

                            alertDialog.setMessage("تمت عملية استرجاع  البيانات بنجاح ");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            // alertDialog.show();

                            custDialog.dismiss();
                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    Acc_ReportActivity.this).create();
                            alertDialog.setTitle("كشف حساب تفصيلي");
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


    public void btn_searchCustomer(View view) {


        TextView acc = (TextView)findViewById(R.id.tv_acc);
        MyTextView tv_cusnm = (MyTextView)findViewById(R.id.tv_cusnm);
        acc.setText("");
        tv_cusnm.setText("");

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "AccReport");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void Set_Cust(final String No, String Nm) {
        final    ProgressDialog progressDialog;
       final TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        MyTextView acc = (MyTextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);

         //onProgressUpdate();

    }

    public void btn_back(View view) {
        Intent k = new Intent(this,GalaxyMainActivity.class);
        startActivity(k);
    }

    public void btn_GetData(View view) {
        onProgressUpdate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent k ;
        if(ComInfo.ComNo==1) {
            k = new Intent(this, MainActivity.class);
        }else {
            k = new Intent(this, GalaxyMainActivity.class);
        }
        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(k);

        finish();
    }

    public void btn_fromYear(View view) {


        DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date today = new Date();

        Calendar c1 = Calendar.getInstance();

        c1.setTime(today);
        c1.set(Calendar.DAY_OF_YEAR, 1);

        FromDate.setText(sdf1.format( c1.getTime()) +"");
        c1.setTime(today);

        /*c1.setTime(today);
        c1.set(Calendar.DAY_OF_MONTH, 1);
        c1.add(Calendar.MONTH, 12);
        c1.add(Calendar.DATE, -1);
        Date lastDayOfMonth = c1.getTime();
        ToDate.setText(sdf1.format(lastDayOfMonth) +"");
*/
    }

    public void btn_fromMonth(View view) {


        DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date today = new Date();

        Calendar c1 = Calendar.getInstance();

        c1.setTime(today);
        c1.set(Calendar.DAY_OF_MONTH, 1);

        FromDate.setText(sdf1.format( c1.getTime()) +"");
       /* c1.setTime(today);
        c1.add(Calendar.MONTH, 1);
        c1.set(Calendar.DAY_OF_MONTH, 1);
        c1.add(Calendar.DATE, -1);
        Date lastDayOfMonth = c1.getTime();
        ToDate.setText(sdf1.format(lastDayOfMonth) +"");

*/

    }


    public void btn_searchCustomer_New(View view) {
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        MyTextView tv_cusnm = (MyTextView)findViewById(R.id.tv_cusnm);
        acc.setText("");
        tv_cusnm.setText("");

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "AccReport");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
}




