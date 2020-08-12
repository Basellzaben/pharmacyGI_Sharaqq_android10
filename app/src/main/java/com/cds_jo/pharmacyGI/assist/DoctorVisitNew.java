package com.cds_jo.pharmacyGI.assist;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.Cls_Offers_Dtl_Gifts;
import com.cds_jo.pharmacyGI.Cls_Offers_Groups;
import com.cds_jo.pharmacyGI.Cls_Offers_Hdr;
import com.cds_jo.pharmacyGI.Cls_Sal_InvItems;
import com.cds_jo.pharmacyGI.Cls_SampleItam_Adapter;
import com.cds_jo.pharmacyGI.Cls_SampleItem;
import com.cds_jo.pharmacyGI.Cls_Sal_Inv_Adapter;
import com.cds_jo.pharmacyGI.Cls_SampleItem;
import com.cds_jo.pharmacyGI.DB;
import com.cds_jo.pharmacyGI.Doctor;
import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.GalaxyMainActivity2;
import com.cds_jo.pharmacyGI.PopSal_Inv_Select_Items;
import com.cds_jo.pharmacyGI.PopSampleItem_Select_Items;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.Sal_Inv_SearchActivity;
import com.cds_jo.pharmacyGI.Sample_Item_SearchActivity;
import com.cds_jo.pharmacyGI.SearchManBalanceQty;
import com.cds_jo.pharmacyGI.Select_Cash_Customer;
import com.cds_jo.pharmacyGI.Select_Customer;
import com.cds_jo.pharmacyGI.Select_Serial;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.UpdateDataToMobileActivity2;
import com.cds_jo.pharmacyGI.We_Result;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Methdes.MethodToUse;
import Methdes.MyTextView;
import Methdes.MyTextView_Digital;
import hearder.main.Header_Frag;

public class DoctorVisitNew extends AppCompatActivity {
    int ExistAfterSacve = 0;
    SqlHandler sqlHandler;
    ListView lvCustomList;

    String CatNo = "-1";

    ArrayList<Cls_SampleItem> contactList;

    Boolean IsNew;
    Boolean IsChange, BalanceQtyTrans;
    String UserID = "";
    public ProgressDialog loadingdialog;
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;

    EditText Tv_Notes;

    MyTextView tv_Date, tv_serialNo, tv_serialDesc;
    private Context context = DoctorVisitNew.this;

    int AllowSalInvMinus;

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

    public void GetMaxPONo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u = sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");
        if (Login.toString().equals("No")) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
        String query = "SELECT  ifnull(MAX(OrderNo), 0) +1 AS no FROM SampleItem_Hdr where     UserID ='" + u.toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";
        EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }
        String max1 = "0";
       try {

           max1 = DB.GetValue(DoctorVisitNew.this, "OrdersSitting", "ifnull(SampleItems,0)", "1=1");
       }catch (Exception ex){
           Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
       }

        if (max1 == "") {
            max1 = "0";
        }
        max1 = String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max)) {
            max = max1;
        }

        if (max.length() == 1) {
            Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        } else {

            Maxpo.setText(intToString(Integer.valueOf(max), 7));
        }
        Maxpo.setFocusable(false);
        Maxpo.setEnabled(false);
        Maxpo.setCursorVisible(false);

        contactList.clear();


    }

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_visit_new);


        Fragment frag = new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        tv_Date = (MyTextView) findViewById(R.id.tv_Date);
        tv_Date.setText(currentDateandTime);


        tv_serialNo = (MyTextView) findViewById(R.id.tv_serialNo);
        tv_serialDesc = (MyTextView) findViewById(R.id.tv_serialDesc);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.setTitle(sharedPreferences.getString("CompanyNm", "") + "/" + sharedPreferences.getString("Address", ""));


        Tv_Notes = (EditText) findViewById(R.id.Tv_Notes);
        BalanceQtyTrans = false;
        try {
            trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AllowSalInvMinus = Integer.parseInt(DB.GetValue(this, "ComanyInfo", "AllowSalInvMinus", "1=1"));

        lvCustomList = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);

        contactList = new ArrayList<Cls_SampleItem>();
        contactList.clear();

        IsNew = true;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        IsNew = true;
        IsChange = false;

        GetSerial();
         GetMaxPONo();
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);

        TextView accno = (TextView) findViewById(R.id.tv_acc);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));



      /* tv_serialNo.setText(DB.GetValue(this,"manf","SampleSerial","man ='" +UserID+"'" ));
         tv_serialDesc.setText(DB.GetValue(this,"manf","Serial_name","man ='" +UserID+"'" ));*/
         UpdateStore();

    }
    private void UpdateStore() {

        TextView tv;
        RelativeLayout.LayoutParams lp;

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
        Drawable greenProgressbar;
        greenProgressbar = this.getResources().getDrawable(R.drawable.progrees_states);
        final Handler _handler = new Handler();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(DoctorVisitNew.this);
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("تحديث كميات مستودع العينات");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        String MaxSeer = "1";


        String q="";
        final String Ser = "1";
        q = "Delete from ManStore";
        sqlHandler.executeQuery(q);
        q = "delete from sqlite_sequence where name='ManStore'";
        sqlHandler.executeQuery(q);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(DoctorVisitNew.this);
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


                            custDialog.dismiss();

                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();

                        }
                    });
                }
            }
        }).start();


    }
    private void GetSerial() {


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


        final Handler _handler = new Handler();

        final ProgressDialog custDialog = new ProgressDialog(DoctorVisitNew.this);
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        //custDialog.setProgress(0);
        //custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("التسلسلات");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(DoctorVisitNew.this);
                ws.GetOrdersSerials(UserID);
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Sales = js.getJSONArray("Sales");
                    JSONArray js_Payment = js.getJSONArray("Payment");
                    JSONArray js_SalesOrder = js.getJSONArray("SalesOrder");
                    JSONArray js_PrepareQty = js.getJSONArray("PrepareQty");
                    JSONArray js_RetSales = js.getJSONArray("RetSales");
                    JSONArray js_PostDely = js.getJSONArray("PostDely");
                    JSONArray js_Visits = js.getJSONArray("Visits");
                    JSONArray js_SampleItems = js.getJSONArray("SampleItems");

                    q = "Delete from OrdersSitting";
                    sqlHandler.executeQuery(q);

                    q = " delete from sqlite_sequence where name='OrdersSitting'";
                    sqlHandler.executeQuery(q);


                    q = "INSERT INTO OrdersSitting(Sales, Payment , SalesOrder , PrepareQty , RetSales, PostDely , Visits ,SampleItems ) values ('"
                            + js_Sales.get(0).toString()
                            + "','" + js_Payment.get(0).toString()
                            + "','" + js_SalesOrder.get(0).toString()
                            + "','" + js_PrepareQty.get(0).toString()
                            + "','" + js_RetSales.get(0).toString()
                            + "','" + js_PostDely.get(0).toString()
                            + "','" + js_Visits.get(0).toString()
                            + "','" + js_SampleItems.get(0).toString()
                            + "')";
                    sqlHandler.executeQuery(q);


                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();
                            GetMaxPONo();

                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();

                        }
                    });
                }
            }
        }).start();

    }
    private void showList() {

        lvCustomList.setAdapter(null);
        Cls_SampleItam_Adapter contactListAdapter = new Cls_SampleItam_Adapter(
                DoctorVisitNew.this, contactList);
        lvCustomList.setAdapter(contactListAdapter);

        TextView Total = (TextView) findViewById(R.id.et_Total);

        if (contactList.size() > 0) {
            Total.setText((contactList.size()) + "");
        }
    }

    private void FillAdapter() {


        String query = "";


        sqlHandler = new SqlHandler(this);


        EditText Order_no = (EditText) findViewById(R.id.et_OrdeNo);

        query = "  select distinct pod.DoctorNM,pod.DoctorNo, Unites.UnitName,  invf.Item_Name, pod.itemno, pod.qty   ,pod.unitNo  " +
                " from SampleItem_Det pod left join invf on invf.Item_No =  pod.itemno   " +
                " left join Unites on Unites.Unitno=  pod.unitNo  Where pod.OrderNo='" + Order_no.getText().toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_SampleItem contactListItems = new Cls_SampleItem();

                    contactListItems.setItemNo(c1.getString(c1
                            .getColumnIndex("itemNo")));
                    contactListItems.setItemNm(c1.getString(c1
                            .getColumnIndex("Item_Name")));

                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));

                    contactListItems.setUnitNo(c1.getString(c1
                            .getColumnIndex("unitNo")));
                    contactListItems.setUnitNm(c1.getString(c1
                            .getColumnIndex("UnitName")));

                    contactListItems.setDoctorNm(c1.getString(c1
                            .getColumnIndex("DoctorNM")));
                    contactListItems.setDoctorNo(c1.getString(c1
                            .getColumnIndex("DoctorNo")));


                    contactList.add(contactListItems);


                } while (c1.moveToNext());


            }
            c1.close();
        }

    }

    public void btn_searchCustomer(View view) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u = sharedPreferences.getString("UserID", "");


    }

    public void Set_Cust(String No, String Nm) {
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }

    public void btn_save_po(final View view) {

        ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);


        final TextView tv_acc = (TextView) findViewById(R.id.tv_acc);
        ///////////////////////////////////////////////////
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Count = sharedPreferences.getString("InvCount", "0");
        String NumOfInvPerVisit = DB.GetValue(DoctorVisitNew.this, "ComanyInfo", "NumOfInvPerVisit  ", "1=1");


        String q = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        final TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        q = "SELECT distinct *  from  SampleItem_Hdr where   Post >0 AND   OrderNo ='" + pono.getText().toString() + "'";
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {


            alertDialog.setTitle("طلب عينات");
            alertDialog.setMessage(getResources().getText(R.string.UpdateNotAllowed));
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            c1.close();
            alertDialog.show();


            return;
        } else {


            String Msg = "";

            final TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
            AlertDialog.Builder alertDialogYesNo = new AlertDialog.Builder(this);


            if (contactList.size() == 0) {


                alertDialog.setTitle("طلب عينات");
                alertDialog.setMessage(getResources().getText(R.string.SaveNotAllowedWithoutItem));            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (ExistAfterSacve == 1) {
                            ExistAfterSacve = 0;
                            Intent k = new Intent(DoctorVisitNew.this, GalaxyMainActivity.class);
                            startActivity(k);
                        }


                    }
                });

                alertDialog.show();

                return;

            }


            alertDialogYesNo.setTitle("طلب عينات");
            alertDialogYesNo.setMessage(getResources().getText(R.string.DoYouWantToContinSave));

            // Setting Icon to Dialog
            alertDialogYesNo.setIcon(R.drawable.save);

            alertDialogYesNo.setNegativeButton(getResources().getText(R.string.No), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    return;
                }
            });


            if (tv_serialNo.getText().toString().length() == 0) {
                tv_serialNo.setError("required!");
                tv_serialNo.requestFocus();
                return;
            }

            alertDialogYesNo.setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (pono.getText().toString().length() == 0) {
                        pono.setError("required!");
                        pono.requestFocus();
                        return;
                    }
                    Save_Recod_Po();
                }
            });


            alertDialogYesNo.show();


        }

    }

    public void Save_Recod_Po() {

        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);

        MyTextView_Digital et_Total = (MyTextView_Digital) findViewById(R.id.et_Total);


        String q1 = "Select * From SampleItem_Hdr Where OrderNo='" + pono.getText().toString() + "'";
        Cursor c1;
        c1 = sqlHandler.selectQuery(q1);

        if (c1 != null && c1.getCount() != 0) {
            IsNew = false;
            c1.close();
        } else {
            IsNew = true;
        }


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        Long i;


        ContentValues cv = new ContentValues();
        cv.put("OrderNo", pono.getText().toString());
        cv.put("acc", acc.getText().toString());
        cv.put("date", currentDateandTime);
        cv.put("userid", UserID);
        cv.put("SerialNo", tv_serialNo.getText().toString());
        cv.put("SerialNm", tv_serialDesc.getText().toString());
        cv.put("Notes", Tv_Notes.getText().toString());
        cv.put("ItemsCount", et_Total.getText().toString());
        cv.put("Post", "-1");
        cv.put("V_OrderNo", sharedPreferences.getString("V_OrderNo", "0"));
        if (IsNew == true) {
            i = sqlHandler.Insert("SampleItem_Hdr", null, cv);
        } else {
            i = sqlHandler.Update("SampleItem_Hdr", cv, "OrderNo ='" + pono.getText().toString() + "'");
        }

        if (i > 0) {
            String q = "Delete from  SampleItem_Det where OrderNo ='" + pono.getText().toString() + "'";
            sqlHandler.executeQuery(q);

            for (int x = 0; x < contactList.size(); x++) {
                Cls_SampleItem contactListItems = new Cls_SampleItem();
                contactListItems = contactList.get(x);


                cv = new ContentValues();
                cv.put("OrderNo", pono.getText().toString());
                cv.put("itemno", contactListItems.getItemNo());
                cv.put("unitNo", contactListItems.getUnitNo());
                cv.put("qty", contactListItems.getUnitNo());
                cv.put("DoctorNo", contactListItems.getDoctorNo());
                cv.put("DoctorNM", contactListItems.getDoctorNm());
                i = sqlHandler.Insert("SampleItem_Det", null, cv);
            }
        }



      /*  query ="INSERT INTO SampleItem_Hdr (OrderNo,Nm,acc,date) values ('"+ pono.getText().toString()+"','"+custNm.getText().toString()+"','"+acc.getText().toString()+"','"+currentDateandTime+"')";
        sqlHandler.executeQuery(query);*/


        if (i > 0) {
            // GetMaxPONo();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("طلب عينات");
            alertDialog.setMessage(getResources().getText(R.string.AddCompleteSucc));

            SharedPreferences.Editor editor = sharedPreferences.edit();


            IsChange = false;


            IsNew = false;
            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    View view = null;


                }
            });


            alertDialog.show();
            UpDateMaxOrderNo();

        }

    }

    private void UpDateMaxOrderNo() {

        String query = "SELECT  ifnull(MAX (OrderNo), 0)AS no FROM SampleItem_Hdr";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        max = (intToString(Integer.valueOf(max), 7));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("m6", max);
        editor.commit();
    }

    public void btn_show_Pop(View view) {
        showPop();
    }

    public void showPop() {
        EditText Order = (EditText) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Sal_inv");
        bundle.putString("CatNo", CatNo);
        bundle.putString("OrderNo", Order.getText().toString());
        bundle.putString("CustomerNo", accno.getText().toString());


        FragmentManager Manager = getFragmentManager();
        PopSampleItem_Select_Items obj = new PopSampleItem_Select_Items();
        obj.setArguments(bundle);
        obj.show(Manager, null);
        IsChange = true;
    }

    public int checkStoreQty(String ItemNo) {
        int q = 0;
        for (int x = 0; x < contactList.size(); x++) {
            Cls_SampleItem contactListItems = new Cls_SampleItem();
            contactListItems = contactList.get(x);
            if (contactListItems.getItemNo().equalsIgnoreCase(ItemNo)) {
                q = q + Integer.parseInt(contactListItems.getQty());
            }
        }
        return q;
    }

    public void Save_List(String ItemNo, String q, String u, String ItemNm, String UnitName, String Operand, String DoctorNo, String DoctorNm) {
        int i = 0;
        int qty = 0;
        for (int x = 0; x < contactList.size(); x++) {
            Cls_SampleItem contactListItems = new Cls_SampleItem();
            contactListItems = contactList.get(x);
            if (contactListItems.getDoctorNo().equalsIgnoreCase(DoctorNo)) {
                i = i + 1;
                qty = qty + Integer.parseInt(contactListItems.getQty()) + Integer.parseInt(q);
                // Toast.makeText(this,q+"",Toast.LENGTH_SHORT).show();
                if (i >= 2 || qty > 2) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            this).create();
                    alertDialog.setTitle("طلب عينات");
                    alertDialog.setMessage("لا يمكن اضافة اكثر من عينتين  لنفس الطبيب");            // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();
                    return;
                }
            }
        }

        Double Net_Total;
        AddEmptyRow();
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        Cls_SampleItem obj = new Cls_SampleItem();
        obj.setItemNo(ItemNo);
        obj.setItemNm(ItemNm);
        obj.setQty(q);
        obj.setUnitNo(u);
        obj.setUnitNm(UnitName);
        obj.setDoctorNo(DoctorNo);
        obj.setDoctorNm(DoctorNm);
        contactList.add(obj);

        showList();


    }

    private void AddEmptyRow() {

      /*  if(contactList.size()==0){
            Cls_SampleItem contactListItems = new Cls_SampleItem();
            contactListItems.setItemNo("");
            contactListItems.setItemNo("");

            contactList.add(contactListItems);
        }*/
    }

    public void Update_List(String ItemNo, String q, String u, String ItemNm, String UnitName, String Operand, String DoctorNo, String DoctorNm) {


        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;
       /* if ( checkStoreQty(ItemNo ,u  , q , bounce) < 0 )  {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
            alertDialog.setMessage(getResources().getText(R.string.QtyNotAvalable));            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();
            return;
        }*/


        // Tax_Amt =(Double.parseDouble(Tax.toString()) /100)   *  ( Double.parseDouble(Item_Total.toString().replace(",","")) -  Double.parseDouble( dis_Amt.toString().replace(",","") ));

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        Cls_SampleItem contactListItems = new Cls_SampleItem();
        contactListItems = contactList.get(position);
        contactListItems.setItemNo(ItemNo);


        showList();


    }

    public void Save_Method(String m, String p, String q, String t, String u) {
        EditText Order = (EditText) findViewById(R.id.et_OrdeNo);
        String query = "INSERT INTO Sal_invoice_Det(OrderNo,itemNo,unitNo,price,qty,tax) values (" + Order.getText().toString() + ",'" + m + "','" + u + "','" + p + "','" + q + "','" + t + "')";
        sqlHandler.executeQuery(query);
        showList();
    }

    public void btn_delete(View view) {

        TextView OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT *  from  SampleItem_Hdr where   Post >0 AND   OrderNo ='" + OrdeNo.getText().toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("طلب عينات");
            alertDialog.setMessage("لقد تم ترحيل الطلب لايمكن التعديل");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            alertDialog.show();


            c1.close();
            return;
        } else {


            AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog1.setTitle("طلب عينات");

            // Setting Dialog Message
            alertDialog1.setMessage("هل انت متاكد من عملية الحذف");

            // Setting Icon to Dialog
            alertDialog1.setIcon(R.drawable.delete);
            // Setting Negative "NO" Button
            alertDialog1.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            // Setting Positive "Yes" Button
            alertDialog1.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Delete_Record_PO();

                }
            });


            // Showing Alert Message
            alertDialog1.show();
        }

    }

    public void Delete_Record_PO() {

        TextView OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());


        String query = "Delete from  SampleItem_Hdr where OrderNo ='" + OrdeNo.getText().toString() + "'";
        sqlHandler.executeQuery(query);


        query = "Delete from  SampleItem_Det where OrderNo ='" + OrdeNo.getText().toString() + "'";
        sqlHandler.executeQuery(query);

        GetMaxPONo();
        showList();
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        CustNm.setText("");
        accno.setText("");


        IsNew = true;
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();

        // Setting Dialog Title
        alertDialog.setTitle("طلب عينات");

        // Setting Dialog Message
        alertDialog.setMessage("تمت عملية الحذف بنجاح");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void btn_Search_Orders(View view) {

        ExistAfterSacve = 0;
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Sample");
        bundle.putString("typ", "0");

        FragmentManager Manager = getFragmentManager();
        Sample_Item_SearchActivity obj = new Sample_Item_SearchActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void btn_Search_Serial(View view) {

        ExistAfterSacve = 0;
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "VisitNo");
        bundle.putString("typ", "0");

        FragmentManager Manager = getFragmentManager();
        Select_Serial obj = new Select_Serial();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void Set_Serial(String No, String Nm) {
        TextView tv_serialNo = (TextView) findViewById(R.id.tv_serialNo);
        TextView tv_serialDesc = (TextView) findViewById(R.id.tv_serialDesc);
        tv_serialNo.setText(No);
        tv_serialDesc.setText(Nm);
        tv_serialNo.setError(null);
        tv_serialNo.clearFocus();


    }

    public void Set_Order(String No) {
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView no = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        MyTextView tv_Date = (MyTextView) findViewById(R.id.tv_Date);


        no.setText(No);
        contactList.clear();
        AddEmptyRow();
        FillAdapter();
        showList();
        String q = "Select  distinct  OrderNo,date ,SerialNm ,Notes,SerialNo " +
                " from  SampleItem_Hdr   where  OrderNo = '" + No + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
        CustNm.setText("");
        accno.setText("");

        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                accno.setText("1");
                tv_serialDesc.setText(c1.getString(c1.getColumnIndex("SerialNm")));
                Tv_Notes.setText(c1.getString(c1.getColumnIndex("Notes")));
                tv_serialNo.setText(c1.getString(c1.getColumnIndex("SerialNo")));
                tv_Date.setText(c1.getString(c1.getColumnIndex("date")));

            }
            c1.close();
        }


        IsChange = false;
        IsNew = false;
    }

    public void btn_print(View view) {


    }

    public void btn_new(View view) {
        // RemoveAnmation();
//        ImageButton imageButton8= (ImageButton)findViewById(R.id.imageButton8);

        // imageButton8.startAnimation(shake);

        //Fade_Fun(imageButton8);
        ExistAfterSacve = 0;
        GetMaxPONo();
        showList();
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView accno = (TextView) findViewById(R.id.tv_acc);

        CustNm.setText("");
        accno.setText("");

        TextView Total = (TextView) findViewById(R.id.et_Total);
        Total.setText("0");
        IsNew = true;


        BalanceQtyTrans = false;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));


        tv_serialNo.setText("");
        tv_serialDesc.setText("");
        UpdateStore();
    }

    public void btn_back(View view) {
        //   RemoveAnmation();
        // ImageButton imageButton7= (ImageButton)findViewById(R.id.imageButton7);
        // imageButton7.startAnimation(shake);
        ExistAfterSacve = 1;
        if (contactList.size() > 0 && IsChange == true) {
            btn_save_po(view);
        } else {
            Intent k = new Intent(this, GalaxyMainActivity2.class);
            startActivity(k);
        }


    }

    int position;

    public void btn_Delete_Item(final View view) {
        position = lvCustomList.getPositionForView(view);
        /*registerForContextMenu(view);
        openContextMenu(view);*/
        deleteItems();
    }

    private void deleteItems() {
        final TextView Total = (TextView) findViewById(R.id.et_Total);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("طلب عينات");
        alertDialog.setMessage(getResources().getText(R.string.DoYouWantContinDelete));
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                contactList.remove(position);


                if (contactList.size() > 0) {
                    Total.setText((contactList.size()) + "");
                } else {
                    Total.setText("0");
                }

                showList();

            }
        });

        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Cls_SampleItem contactListItems =new Cls_SampleItem();


        menu.setHeaderTitle(contactList.get(position).getItemNm());
        menu.add(Menu.NONE, 1, Menu.NONE, getResources().getText(R.string.Update));
        menu.add(Menu.NONE, 2, Menu.NONE, getResources().getText(R.string.Delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1: {

               /* ArrayList<Cls_SampleItem> Itemlist = new ArrayList <Cls_SampleItem> ();

                Itemlist.add(contactList.get(position))  ;



                TextView accno = (TextView)findViewById(R.id.tv_acc);

                Bundle bundle = new Bundle();
                bundle.putString("Scr", "Sal_inv");
                bundle.putString("CatNo", CatNo);
                bundle.putString("OrderNo", pono.getText().toString());
                bundle.putString("CustomerNo", accno.getText().toString());

                bundle.putSerializable("List", Itemlist);
                FragmentManager Manager = getFragmentManager();
                PopSal_Inv_Select_Items obj = new PopSal_Inv_Select_Items();
                obj.setArguments(bundle);
                obj.show(Manager, null);*/

            }
            break;
            case 2: {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
                alertDialog.setMessage(getResources().getText(R.string.DoYouWantContinDelete));
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        contactList.remove(position);

                        showList();

                    }
                });

                alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

            }
            break;

        }


        return super.onContextItemSelected(item);
    }

    public void btn_share(View view) {

        final SqlHandler sql_Handler = new SqlHandler(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        final TextView acc = (TextView) findViewById(R.id.tv_acc);
        final TextView Total = (TextView) findViewById(R.id.et_Total);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        Cls_SampleItem Inv_Obj;

        for (int j = 0; j < contactList.size(); j++) {
            Inv_Obj = new Cls_SampleItem();
            Inv_Obj = contactList.get(j);


        }


        final String str;

       /* ContentValues cv =new ContentValues();
        cv.put("OrderNo", pono.getText().toString());
        cv.put("acc",acc.getText().toString());
        cv.put("date",currentDateandTime);
        cv.put("userid",UserID);
        cv.put("SerialNo",tv_serialNo.getText().toString());
        cv.put("SerialNm",tv_serialDesc.getText().toString());
        cv.put("Notes",Tv_Notes.getText().toString());
        cv.put("Post", "-1");
        cv.put("V_OrderNo",sharedPreferences.getString("V_OrderNo", "0"));
*/

        String query = "SELECT distinct OrderNo, acc,date,userid,SerialNo,SerialNm,Notes,V_OrderNo  FROM SampleItem_Hdr where OrderNo  ='" + pono.getText().toString() + "'";


        Cursor c1 = sqlHandler.selectQuery(query);

        JSONObject jsonObject = new JSONObject();
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("OrderNo", c1.getString(c1.getColumnIndex("OrderNo")));
                jsonObject.put("acc", c1.getString(c1.getColumnIndex("acc")));
                jsonObject.put("Order_date", c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("userid", sharedPreferences.getString("UserID", ""));
                jsonObject.put("SerialNo", c1.getString(c1.getColumnIndex("SerialNo")));
                jsonObject.put("SerialNm", c1.getString(c1.getColumnIndex("SerialNm")));
                jsonObject.put("Notes", c1.getString(c1.getColumnIndex("Notes")));
                jsonObject.put("V_OrderNo", c1.getString(c1.getColumnIndex("V_OrderNo")));

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            c1.close();
        }

        String json = new Gson().toJson(contactList);
        str = jsonObject.toString() + json;


        loadingdialog = ProgressDialog.show(DoctorVisitNew.this, getResources().getText(R.string.PleaseWait), getResources().getText(R.string.PostUnderProccess), true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(DoctorVisitNew.this);
                ws.Save_Sample_Item(str);
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("Post", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("SampleItem_Hdr", cv, "OrderNo='" + DocNo.getText().toString() + "'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        DoctorVisitNew.this).create();
                                alertDialog.setTitle("طلب عينات");
                                alertDialog.setMessage(getResources().getText(R.string.PostCompleteSuccfully));
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
                    }else if (We_Result.ID== -3) {
                            loadingdialog.dismiss();
                            _handler.post(new Runnable() {
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(
                                            DoctorVisitNew.this).create();
                                    alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID + "");
                                    alertDialog.setMessage(We_Result.Msg.toString());
                                    alertDialog.setIcon(R.drawable.delete);
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    alertDialog.show();

                                    alertDialog.setIcon(R.drawable.delete);
                                    alertDialog.setMessage(We_Result.Msg);
                                }
                            });

                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        DoctorVisitNew.this).create();
                                alertDialog.setTitle(getResources().getText(R.string.PostNotCompleteSuccfully) + "   " +  We_Result.ID +"");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage(getResources().getText(R.string.PostNotCompleteSuccfully) + "    ");
                            }
                        });
                    }

                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    DoctorVisitNew.this).create();
                            alertDialog.setTitle(getResources().getText(R.string.ConnectError));
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sal_invoice_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.BalanceQty:
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "SalInvoice");
                FragmentManager Manager = getFragmentManager();
                SearchManBalanceQty obj = new SearchManBalanceQty();
                obj.setArguments(bundle);
                obj.show(Manager, null);
                break;
            case R.id.Exist:
                View view = null;
                btn_back(view);
                break;

        }

        return true;
    }

    public void btn_show_Pop1(View view) {
        showPop();
    }
}
