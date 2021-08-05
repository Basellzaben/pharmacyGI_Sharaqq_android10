package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Cls_Cur;
import com.cds_jo.pharmacyGI.assist.Cls_Cur_Adapter;
import com.cds_jo.pharmacyGI.assist.LoginActivity;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;
import hearder.main.Header_Frag;

public class DoctorReportActivity extends AppCompatActivity {
    SqlHandler sql_Handler;
    Spinner VisitType,Location;
    EditText tv_No,spi,SampleType,VNotes,SNotes  ;
    TextView TrDate,CustNm,CustNo,tv_Time;
    MyTextView tv_CustNm ;
    ImageButton CustSearch;
    Boolean IsNew;
    int PrvVisitType = 1;
    public ProgressDialog loadingdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.n_activity_doctor_report_new);

            Fragment frag = new Header_Frag();
           FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            String currentDateandTime = sdf.format(new Date());

            SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            String StringTime = StartTime.format(new Date());

     /*   TrDate = (TextView) findViewById(R.id.et_Date);
        tv_Time = (TextView) findViewById(R.id.tv_Time);
        TrDate.setText(currentDateandTime);
        tv_Time.setText(StringTime);*/

            tv_No = (EditText) findViewById(R.id.et_No);
            spi = (EditText) findViewById(R.id.et_spi);
            SampleType = (EditText) findViewById(R.id.et_SampleType);
            VNotes = (EditText) findViewById(R.id.et_VNotes);
            SNotes = (EditText) findViewById(R.id.et_SNotes);
            SampleType = (EditText) findViewById(R.id.et_SampleType);
            SampleType = (EditText) findViewById(R.id.et_SampleType);
            CustSearch = (ImageButton) findViewById(R.id.btn_Cust_Search);
            VisitType = (Spinner) findViewById(R.id.sp_VisitType);
            Location = (Spinner) findViewById(R.id.sp_Location);
            CustNm = (TextView) findViewById(R.id.tv_cusnm);
            CustNo = (TextView) findViewById(R.id.tv_acc);
            tv_CustNm = (MyTextView) findViewById(R.id.tv_CustNm);
            GetMaxRecNo();
            FillVisitType();
            FillLocation();
            IsNew = true;

            VisitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();

                    if (position == 0) {
                        PrvVisitType = 1;
                        tv_CustNm.setText("اسم الطبيب :");
                    } else {
                        PrvVisitType = 2;
                        tv_CustNm.setText("اسم الصيدلية :");
                    }
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch ( Exception ex){
            Toast.makeText(this,ex.getMessage()+"",Toast.LENGTH_LONG).show();
        }
// DoctorReport  "( ID ,VType ,No,CustNo ,LocatNo , Sp1 , SampleType  , VNotes ,SNotes  ,Tr_Date , Tr_Time   ,UserNo , Posted
    }
    public void btn_back(View view) {
        Intent k = new Intent(this, GalaxyMainActivity2.class);
        startActivity(k);
    }
    @SuppressLint("Range")
    public void btn_share(View view) {

        if(IsNew){
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("زيارة طبية");
            alertDialog.setMessage("يجب تخزين الزيارة اولا");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();
            return;
        }

        final String str;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sql_Handler = new SqlHandler(this);


        ArrayList<Cls_DoctorReport> objlist    ;
        objlist = new ArrayList<Cls_DoctorReport>();
        objlist.clear();

        Integer index = VisitType.getSelectedItemPosition();
        Cls_Cur v = (Cls_Cur) VisitType.getItemAtPosition(index);
       String query = "  select distinct  ID ,VType ,No,CustNo ,LocatNo , Sp1 , SampleType  , VNotes ,SNotes  ,Tr_Date , Tr_Time   ,UserNo , Posted from DoctorReport   " +
               "where No ='" + tv_No.getText().toString() +"'";
        Cursor c1 = sql_Handler.selectQuery(query);
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
        }


        final String json = new Gson().toJson(objlist);

        loadingdialog = ProgressDialog.show(DoctorReportActivity.this, "الرجاء الانتظار ...", "الرجاء الانتظار .... العمل جاري على اعتماد زيارة طبية", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();


        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(DoctorReportActivity.this);
                ws.SaveDoctorReport(json);
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("Posted", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("DoctorReport", cv, "No='"+ tv_No.getText().toString()+"'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        DoctorReportActivity.this).create();
                                alertDialog.setTitle(" اعتماد زيارة طبية");
                                alertDialog.setMessage("تمت عملية اعتماد زيارة طبية بنجاح");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();
                                DoNew();
                                GetMaxRecNo();

                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        DoctorReportActivity.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " +We_Result.ID+"");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage("عملية الاعتماد لم تتم بنجاح" +"    " );
                            }
                        });
                    }

                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    DoctorReportActivity.this).create();
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
    @SuppressLint("Range")
    public void ShowRecord() {

        Cls_Cur_Adapter Location_adapter = (Cls_Cur_Adapter) Location.getAdapter();
        Cls_Cur obj = new Cls_Cur();

        Cls_Cur_Adapter VisitType_adapter = (Cls_Cur_Adapter) VisitType.getAdapter();


        String q = "Select distinct  CASE  VType  WHEN '1' THEN  D.Dr_AName ELSE Customers.name   END   as Name,  DoctorReport.ID ,VType ,DoctorReport.No,CustNo ,LocatNo , Sp1 , SampleType  , VNotes ,SNotes  " +
                "  ,Tr_Date , Tr_Time   ,UserNo , COALESCE(DoctorReport.Posted, -1)  as Post    " +
                "  from DoctorReport left join Customers  on Customers.no =DoctorReport.CustNo  " +
                "   left join Doctor D  on D.Dr_No = DoctorReport.CustNo   " +
                   "  where DoctorReport.No = '" + tv_No.getText().toString() + "'";
        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {

                spi.setText(c1.getString(c1.getColumnIndex("Sp1")));
                SampleType.setText(c1.getString(c1.getColumnIndex("SampleType")));
                VNotes.setText(c1.getString(c1.getColumnIndex("VNotes")));
                SNotes.setText(c1.getString(c1.getColumnIndex("SNotes")));
                TrDate.setText(c1.getString(c1.getColumnIndex("Tr_Date")));
                CustNm.setText(c1.getString(c1.getColumnIndex("Name")));
                CustNo.setText(c1.getString(c1.getColumnIndex("CustNo")));
                tv_Time.setText(c1.getString(c1.getColumnIndex("Tr_Time")));

                IsNew=false;

               /* ImageButton btn_delete = (ImageButton)findViewById(R.id.imageButton6);
                ImageButton btn_add = (ImageButton)findViewById(R.id.imageButton3);
                ImageButton btn_share= (ImageButton)findViewById(R.id.imageButton9);*/

               /* if(c1.getString(c1.getColumnIndex("Post")).equals("-1")){
                    btn_delete.setEnabled(true);
                    btn_add.setEnabled(true);
                    btn_share.setEnabled(true);
                }
                else {
                    btn_delete.setEnabled(false);

                    btn_share.setEnabled(false);
                }*/
                for (int i = 0; i < Location_adapter.getCount(); i++) {
                    obj = (Cls_Cur) Location_adapter.getItem(i);


                    if (obj.getNo().equals(c1.getString(c1.getColumnIndex("LocatNo")).toString())) {
                        Location.setSelection(i);
                        break;
                    }
                }


                for (int i = 0; i < VisitType_adapter.getCount(); i++) {
                    obj = (Cls_Cur) VisitType_adapter.getItem(i);
                    //  Toast.makeText(this,c1.getString(c1.getColumnIndex("VouchType")).toString(),Toast.LENGTH_SHORT).show();

                    if (obj.getNo().equals(c1.getString(c1.getColumnIndex("VType")).toString())) {
                        VisitType.setSelection(i);
                        break;
                    }
                }




            }
       c1.close();
    }


}
    public void Set_Order(String No) {
        tv_No.setText(No);
        ShowRecord();
    }
    public void btn_search_Recv(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "DoctorReport");
        android.app.FragmentManager Manager = getFragmentManager();
        DoctorReportSearchActivity obj = new DoctorReportSearchActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void Delete_Record_PO() {

        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        long i = 0 ;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("زيارة طبية");
        i=   sql_Handler.Delete("DoctorReport","No='"+ tv_No.getText().toString()+"'");

        if ( i >0 ){
            DoNew();
            alertDialog.setMessage("تمت عملية الحذف بنجاح");
            alertDialog.setIcon(R.drawable.tick);
        }

        else
        {
            alertDialog.setMessage("عملية الحذف لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
        }


        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        alertDialog.show();



    }
    public void btn_delete(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("زيارة طبية");

        // Setting Dialog Message
        alertDialog.setMessage("هل انت متاكد من عملية الحذف ..؟");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Delete_Record_PO();

            }
        });


        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void btn_new(View view) {
        DoNew();
    }
    public void DoNew() {
        GetMaxRecNo();
        IsNew = true;
        CustNm.setText("");
        CustNo.setText("");
        spi.setText("");
        SampleType.setText("");
        VNotes.setText("");
        SNotes.setText("");
    }
    public void Save_Recod_Po() {




        Integer indexValue = Location.getSelectedItemPosition();
        Cls_Cur l = (Cls_Cur) Location.getItemAtPosition(indexValue);


        Integer index = VisitType.getSelectedItemPosition();
        Cls_Cur v = (Cls_Cur) VisitType.getItemAtPosition(index);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();



// DoctorReport  "( ID ,VType ,No,CustNo ,LocatNo , Sp1 , SampleType  , VNotes ,SNotes  ,Tr_Date , Tr_Time   ,UserNo , Posted

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ContentValues cv = new ContentValues();
        cv.put("VType",v.getNo().toString());
        cv.put("No", tv_No.getText().toString());
        cv.put("CustNo", CustNo.getText().toString());
        cv.put("LocatNo", l.getNo().toString());
        cv.put("Sp1", spi.getText().toString());
        cv.put("SampleType", SampleType.getText().toString());
        cv.put("VNotes", VNotes.getText().toString());
        cv.put("SNotes", SNotes.getText().toString());
        cv.put("Tr_Date", TrDate.getText().toString());
        cv.put("Tr_Time", tv_Time.getText().toString());
        cv.put("Posted","-1");
        cv.put("UserNo", sharedPreferences.getString("UserID", ""));
        long i;

        if (IsNew==true) {
            i = sql_Handler.Insert("DoctorReport", null, cv);
        }
        else
        {
            i = sql_Handler.Update("DoctorReport", cv, "No='"+ tv_No.getText().toString()+"'");
        }




        final View view=null;
        alertDialog = new AlertDialog.Builder(
                this).create();
             alertDialog.setTitle("زيارة طبية");
        if (i > 0) {
            alertDialog.setMessage(" تمت عملية الحفظ بنجاح");
            alertDialog.setIcon(R.drawable.tick);
           /*  GetMaxRecNo();
            showList();
            DoNew();*/
            SharedPreferences.Editor editor    = sharedPreferences.edit();

            //IsNew = false;
            DoNew();
        } else {
            alertDialog.setMessage("عملية الحفظ لم تتم ");
            alertDialog.setIcon(R.drawable.delete);
        }
        // Setting OK Button
             alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                     // btn_print(view);
                 }
        });

        // Showing Alert Message
             alertDialog.show();
        //GetMaxRecNo();


    }
    public void btn_save_po(View view) {

        tv_No.setError(null);
        CustNm.setError(null);
        VNotes.setError(null);
        if ( SToD(tv_No.getText().toString()) == 0) {
            tv_No.setError("required!");
            tv_No.requestFocus();
            return;
        }

        if (CustNm.getText().toString().length() == 0) {
            CustNm.setError("required!");
            CustNm.requestFocus();
            return;
        }

        if (VNotes.getText().toString().length() == 0) {
            VNotes.setError("required!");
            VNotes.requestFocus();
            return;
        }

        AlertDialog.Builder alert_Dialog = new AlertDialog.Builder(this);
        alert_Dialog.setTitle("زيارة طبية");
        alert_Dialog.setMessage("هل  تريد الاستمرار بعملية الحفظ " + "؟");
        alert_Dialog.setIcon(R.drawable.save);
        alert_Dialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Save_Recod_Po();
            }
        });


        alert_Dialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });


        alert_Dialog.show();


    }
    public void btn_SearchCust(View v) {
        ImageButton ib = (ImageButton) v;

        if (ib == CustSearch) {
            if(PrvVisitType==2) {

                Bundle bundle = new Bundle();
                bundle.putString("Scr", "DoctorReprot");
                bundle.putString("PrvVisitType", PrvVisitType + "");
                android.app.FragmentManager Manager = getFragmentManager();
                Select_Customer obj = new Select_Customer();
                obj.setArguments(bundle);
                obj.show(Manager, null);
            }else
            {
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "DoctorReprot");
                android.app.FragmentManager Manager = getFragmentManager();
                Select_Doctor obj = new Select_Doctor();
                obj.setArguments(bundle);
                obj.show(Manager, null);
            }
        }
    }
    public void Set_Cust(final String No, String Nm) {

        CustNo.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }
    private void FillVisitType() {
          VisitType = (Spinner) findViewById(R.id.sp_VisitType);


        ArrayList<Cls_Cur> VouchTypeList = new ArrayList<Cls_Cur>();
        VouchTypeList.clear();

        Cls_Cur cls_cur = new Cls_Cur();
        cls_cur.setName("طبيب");
        cls_cur.setNo("1");
        VouchTypeList.add(cls_cur);


        cls_cur = new Cls_Cur();
        cls_cur.setName("صيدلية");
        cls_cur.setNo("2");
        VouchTypeList.add(cls_cur);


        Cls_Cur_Adapter cls_cur_adapter = new Cls_Cur_Adapter(
                this, VouchTypeList);
        VisitType.setAdapter(cls_cur_adapter);
    }
    @SuppressLint("Range")
    private void FillLocation() {
          Location = (Spinner) findViewById(R.id.sp_Location);


        ArrayList<Cls_Cur> Locations = new ArrayList<Cls_Cur>();
        Locations.clear();
        SqlHandler sqlHandler = new SqlHandler(this);

        String query = "Select  distinct No , Name from Area";
        ArrayList<Cls_Cur> cls_curs = new ArrayList<Cls_Cur>();
        cls_curs.clear();

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Cur cls_Loc = new Cls_Cur();

                    cls_Loc.setName(c1.getString(c1
                            .getColumnIndex("Name")));
                    cls_Loc.setNo(c1.getString(c1
                            .getColumnIndex("No")));

                    Locations.add(cls_Loc);

                } while (c1.moveToNext());

          c1.close();  }
        }




        Cls_Cur_Adapter cls_cur_adapter = new Cls_Cur_Adapter(
                this, Locations);
        Location.setAdapter(cls_cur_adapter);
    }
    @SuppressLint("Range")
    public void GetMaxRecNo() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String Login = sharedPreferences.getString("Login", "No");
        if(Login.toString().equals("No")){
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }

        String u =  sharedPreferences.getString("UserID", "");
        sql_Handler = new SqlHandler(this);
        String query = "SELECT  COALESCE(MAX(No), 0) +1 AS No FROM DoctorReport   where UserNo = '"+u.toString()+"'";
        Cursor c1 = sql_Handler.selectQuery(query);
        String max = "0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("No"));
            c1.close();
        }

        String max1="0";
       // max1 = sharedPreferences.getString("m2", "");
        if (max1==""){
            max1 ="0";
        }
        max1 =String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max))
        {
            max = max1 ;
        }

        if (max.length()==1) {
            tv_No.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {

            tv_No.setText(intToString(Integer.valueOf(max), 7)  );

        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv_No.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        tv_No.setFocusable(false);
        tv_No.setEnabled(false);
        tv_No.setCursorVisible(false);


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
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    @Override
    public void onBackPressed() {
        Intent k ;
        k = new Intent(this, GalaxyMainActivity2.class);
        startActivity(k);

    }
}
