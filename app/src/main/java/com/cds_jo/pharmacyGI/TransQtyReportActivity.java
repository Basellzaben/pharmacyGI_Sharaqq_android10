package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.cds_jo.pharmacyGI.assist.Cls_Trans_Qty;
import com.cds_jo.pharmacyGI.assist.Cls_Trans_Qty_Adapter;
import com.cds_jo.pharmacyGI.assist.Convert_TransQty_To_Img;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Methdes.MethodToUse;
import Methdes.MyTextView;
import hearder.main.Header_Frag;

public class TransQtyReportActivity extends FragmentActivity {
    ListView lst_Items ;
    CheckBox chk_hdr_disc ;
    String MaxStoreQtySer = "0" ;
    MyTextView tv_msg ;
    int AllowSalInvMinus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.n_activity_trans_qty_report);

        Button button=(Button)findViewById(R.id.button13);
        Button button2=(Button)findViewById(R.id.button33);

        button.setTypeface(MethodToUse.SetTFace(TransQtyReportActivity.this));
        button2.setTypeface(MethodToUse.SetTFace(TransQtyReportActivity.this));
        lst_Items =(ListView)findViewById(R.id.lst_Items);
        tv_msg = (MyTextView) findViewById(R.id.tv_msg);
        tv_msg.setText("");

        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
    }
    private void  GetStoreQtySer(){
        SqlHandler sqlHandler = new SqlHandler(this);
        String query = "SELECT  COALESCE(MAX(ser), 0)   AS no from ManStore";
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                MaxStoreQtySer = String.valueOf(c1.getInt(0));
            }
            c1.close();
        }

    }
    public void btn_GetData(View view) {
         GetStoreQtySer();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SqlHandler sqlHandler = new SqlHandler(TransQtyReportActivity.this);
        String query = "SELECT    *   from  Sal_invoice_Hdr  where    Post = -1  and    UserID='"+sharedPreferences.getString("UserID", "-1")+"'";
        Cursor c1 = sqlHandler.selectQuery(query);

        Double Sal_Qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    TransQtyReportActivity.this).create();

            alertDialog.setMessage("يوجد فواتير غير مرحلة ، الرجاء ترحيل الفواتير ومن ثم عمل سند تزويد ");

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ShowData();
                }

            });
            alertDialog.show();
            c1.close();
        }
        else
        {
            ShowData();
        }


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
    private void ShowData() {
        final ProgressDialog progressDialog;
        final Handler _handler = new Handler();
        AllowSalInvMinus =Integer.parseInt( DB.GetValue(this,"ComanyInfo","AllowSalInvMinus","1=1"));

        progressDialog = new ProgressDialog(TransQtyReportActivity.this);
        progressDialog.setTitle("الرجاء الانتظار");
        progressDialog.setMessage("العمل جاري على استرجاع كميات المستودع");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        final String currentDateandTime = sdf.format(new Date());

        Calendar cc = Calendar.getInstance();
        final int dayOfWeek = cc.get(Calendar.DAY_OF_WEEK);
        new Thread(new Runnable() {
            @Override
            public void run() {

               /* CallWebServices ws = new CallWebServices();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TransQtyReportActivity.this);
                ws.TrnsferQtyFromMobile(sharedPreferences.getString("UserID", ""), String.valueOf(dayOfWeek), currentDateandTime);
*/
                try {
                    Integer i;
                    String q = "";

                    final ArrayList<Cls_Trans_Qty> cls_trans_qties = new ArrayList<Cls_Trans_Qty>();
                    cls_trans_qties.clear();

                    Cls_Trans_Qty cls_trans_qty = new Cls_Trans_Qty();

                    cls_trans_qty.setDate("      " + "التاريخ" + "        ");
                    cls_trans_qty.setDocno("     " + "رقم السند" + "  ");
                    cls_trans_qty.setItemno("رقم المادة");
                    cls_trans_qty.setItem_Name("اسم  المادة");
                    cls_trans_qty.setUnitNo("الوحدة");
                    cls_trans_qty.setQty("الكمية");
                    cls_trans_qty.setStoreName("المستوع");
                    cls_trans_qty.setDes("البيان");
                    cls_trans_qties.add(cls_trans_qty);

                    SqlHandler sqlHandler = new SqlHandler(TransQtyReportActivity.this);



                    if(AllowSalInvMinus==1){
                        q = " Select distinct   ifnull(ms.ser,0) as ser, ifnull(ms.docno,0) as docno ,ifnull(ms.StoreName,0)  as StoreName, ifnull(invf.Item_No,0) as itemno  , ifnull(invf.Item_Name,0) as Item_Name" +
                                ",ifnull(Unites.UnitName ,0)  as  UnitName ,ifnull(ms.qty,0) as qty   ,ifnull(ms.des,0)as des ,  ifnull(ms.date  ,0)as date" +
                                "  from  invf left join ManStore  ms   on invf.Item_No =  ms.itemno    left join Unites on Unites.Unitno=  ms.UnitNo";

                    }else {
                        q = "Select distinct   ms.ser, ms.docno ,ms.StoreName , ms.itemno , invf.Item_Name   ,Unites.UnitName     ,ms.qty  ,ms.des ,  ms.date " +
                                "   from  ManStore  ms left join invf on invf.Item_No =  ms.itemno    left join Unites on Unites.Unitno=  ms.UnitNo";
                    }


                    Double qty = 0.0;
                    Cursor c = sqlHandler.selectQuery(q);
                    i = 0;
                    if (c != null && c.getCount() != 0) {
                        if (c.moveToFirst()) {
                            do {

                                //date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear
                                cls_trans_qty = new Cls_Trans_Qty();

                                cls_trans_qty.setDocno(c.getString(c
                                        .getColumnIndex("ser")));
                                cls_trans_qty.setItemno(c.getString(c
                                        .getColumnIndex("itemno")));
                                cls_trans_qty.setItem_Name(c.getString(c
                                        .getColumnIndex("Item_Name")));
                                cls_trans_qty.setUnitNo(c.getString(c
                                        .getColumnIndex("UnitName")));
                                qty = ((Double.parseDouble(c.getString(c.getColumnIndex("qty"))) - GetSaledQtyNotPosted(c.getString(c.getColumnIndex("itemno")))));

                                cls_trans_qty.setQty((SToD(qty.toString())).toString());

                                cls_trans_qty.setDate(c.getString(c
                                        .getColumnIndex("date")));
                                cls_trans_qty.setDes(c.getString(c
                                        .getColumnIndex("des")));

                                cls_trans_qty.setStoreName(c.getString(c
                                        .getColumnIndex("StoreName")));

                                if ( qty !=0){
                                    cls_trans_qties.add(cls_trans_qty);
                                }
                                qty = 0.0;


                                progressDialog.setMax(c.getCount());
                                progressDialog.incrementProgressBy(1);

                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }

                                i = i + 1;

                            } while (c.moveToNext());

                        }
                        c.close();
                    }
                    _handler.post(new Runnable() {
                        public void run() {
                            Cls_Trans_Qty_Adapter cls_trans_qty_adapter = new Cls_Trans_Qty_Adapter(
                                    TransQtyReportActivity.this, cls_trans_qties);
                            lst_Items.setAdapter(cls_trans_qty_adapter);
                        }
                    });

                    final int total = i;
                    _handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    TransQtyReportActivity.this).create();

                            alertDialog.setMessage("تمت عملية تحديث البيانات بنجاح" + "   " + String.valueOf(total));
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            // alertDialog.show();

                        }
                    });
                } catch (final Exception e) {
                    progressDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    TransQtyReportActivity.this).create();
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


    private Double GetSaledQtyNotPosted(String ItemNo ){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SqlHandler sqlHandler = new SqlHandler(TransQtyReportActivity.this);
        String query = "SELECT     (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                "    where   sih.Post = -1  and ui.item_no ='"+ItemNo+"'  and sih.UserID='"+sharedPreferences.getString("UserID", "-1")+"'";
        Cursor c1 = sqlHandler.selectQuery(query);

        Double Sal_Qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                Sal_Qty =   Double.parseDouble(  (c1.getString(c1.getColumnIndex("Sal_Qty"))).toString());
            }
            c1.close();
        }



        return Sal_Qty;
    }
    @Override
    public void onBackPressed() {
        Intent k = new Intent(this, GalaxyMainActivity.class);
          startActivity(k);
    }

    public void btn_back(View view) {
        Intent i = new Intent(this,GalaxyMainActivity.class);
        startActivity(i);
    }

    public void btn_print(View view) {
        tv_msg.setText("الرجاء الانتظار العمل جاري على طباعة المستند");
        Intent k = new Intent(this, Convert_TransQty_To_Img.class);
        startActivity(k);
    }
}
