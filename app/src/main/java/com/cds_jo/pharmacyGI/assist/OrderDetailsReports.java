package com.cds_jo.pharmacyGI.assist;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.ContactListAdapter;
import com.cds_jo.pharmacyGI.ContactListItems;
import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.MainActivity;
import com.cds_jo.pharmacyGI.PostTransActions.PopOrderItems;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.UpdateDataToMobileActivity;
import com.cds_jo.pharmacyGI.We_Result;
import com.cds_jo.pharmacyGI.cls_Order_Sales_details;

import com.cds_jo.pharmacyGI.cls_Tab__Order_Sales_adapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hearder.main.Header_Frag;

public class OrderDetailsReports extends AppCompatActivity {
    SwipeMenuListView items_Lsit;
    SwipeMenuCreator creator;
    int List_index;
    SqlHandler sqlHandler;
    ArrayList<cls_Order_Sales_details> cls_Tab_Order_Sales  ;
    String Order_No,query;
    TextView tv;
    String Op = "";
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_reports);
          sqlHandler = new SqlHandler(this);
        cls_Tab_Order_Sales  = new ArrayList<cls_Order_Sales_details>();
        cls_Tab_Order_Sales.clear();
        items_Lsit = (SwipeMenuListView) findViewById(R.id.Lst);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                try {
                    SwipeMenuItem Approve = new SwipeMenuItem(OrderDetailsReports.this);
                    Approve.setBackground(R.color.Green);
                    Approve.setWidth(200);
                    Approve.setTitle("المواد");
                    //Approve.setIcon(R.mipmap.edite_white);
                    Approve.setTitleSize(16);
                    Approve.setTitleColor(Color.WHITE);
                    menu.addMenuItem(Approve);

                    SwipeMenuItem Share = new SwipeMenuItem(OrderDetailsReports.this);
                    Share.setBackground(R.color.colorPrimary);
                    Share.setWidth(200);
                    Share.setTitle("أعتماد");
                    //Approve.setIcon(R.mipmap.edite_white);
                    Share.setTitleSize(16);
                    Share.setTitleColor(Color.WHITE);
                    menu.addMenuItem(Share);



                    SwipeMenuItem NotApprove = new SwipeMenuItem(OrderDetailsReports.this);
                    NotApprove.setBackground(R.color.RED);
                    NotApprove.setWidth(200);
                    NotApprove.setTitle("حذف  ");
                  //  NotApprove.setIcon(R.mipmap.icon_delete);
                    NotApprove.setTitleSize(16);
                    NotApprove.setTitleColor(Color.WHITE);
                    menu.addMenuItem(NotApprove);
                } catch (Exception ex) {
                    Toast.makeText(OrderDetailsReports.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        };

        items_Lsit.setMenuCreator(creator);
        items_Lsit.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

                List_index = position;
                items_Lsit.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

        items_Lsit.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        List_index = position;
                        ShowDetials(position);
                        break;
                    case 1:
                        List_index = position;
                        Share_Order(position);
                        break;
                    case 2:
                        List_index = position;
                        Delete_Order(position);
                        break;
                }

                return false;
            }
        });
        GetServer_Date();
    FillOrder();
    Fragment frag = new Header_Frag();
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();


    }
    private void ShowDetials(final int position){
        try {
            cls_Order_Sales_details obj = (cls_Order_Sales_details) items_Lsit.getItemAtPosition(position);
            Order_No = obj.getOrderNo();
            Bundle bundle = new Bundle();
            bundle.putString("Order_No", Order_No);
            bundle.putString("CustName", obj.getCustNm().toString());
            FragmentManager Manager = getFragmentManager();
            PopOrderItems _obj = new PopOrderItems();
            _obj.setArguments(bundle);
            _obj.show(Manager, null);
        }catch ( Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    private void RelatedTrans(final int position){}

    private void FillOrder() {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
          String currentDateandTime = sdf.format(new Date());
          String q ="";
          SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
          cls_Tab_Order_Sales.clear();




          q =     " Select distinct po.orderno,po.date, ifnull(c.name,'الأسم غير موجود') as name ,po.acc ,po.posted  ,COALESCE(po.Net_Total,0) as total" +
                  " ,os.PrintOrder,os.Posted as ms,os.AB_SalesOrder,os.AB_SalesBill,os.Notes as nn from Po_Hdr po Left join Customers c on c.no = po.acc " +
                  " left join   ORDER_STUTES os  on cast(os.OrderNo as integer)= cast( po.orderno  as integer)  "+
                  "where  userid='"+sharedPreferences.getString("UserID", "") + "'" ;



          Cursor c1 = sqlHandler.selectQuery(q);


        cls_Order_Sales_details cls_searchpos;


          Double sum = 0.0 ;
          if (c1 != null && c1.getCount() != 0) {
              if (c1.moveToFirst()) {
                  do {
                      cls_searchpos= new cls_Order_Sales_details();
                      cls_searchpos.setOrderNo(c1.getString(c1.getColumnIndex("orderno")));
                      cls_searchpos.setCustNm(c1.getString(c1.getColumnIndex("name")));
                      cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                      cls_searchpos.setCustNo(c1.getString(c1.getColumnIndex("acc")));
                      cls_searchpos.setPosted(c1.getString(c1.getColumnIndex("posted")));
                      cls_searchpos.setABPrint(c1.getString(c1.getColumnIndex("PrintOrder")));
                      cls_searchpos.setABOrder(c1.getString(c1.getColumnIndex("AB_SalesOrder")));
                      cls_searchpos.setABill(c1.getString(c1.getColumnIndex("AB_SalesBill")));
                      cls_searchpos.setTot(c1.getString(c1.getColumnIndex("total")));

                      cls_Tab_Order_Sales.add(cls_searchpos);
                  }while (c1.moveToNext());
              }

              c1.close();
          }

          /*tv_Summatin.setText(sum +"");
          tv_Summatin.setText(SToD(tv_Summatin.getText().toString()).toString());
          count.setText((cls_Tab_Order_Sales.size()) +"");*/
        cls_Order_Sales_Report_adapter SalesAdapter = new cls_Order_Sales_Report_adapter(
                  this,cls_Tab_Order_Sales);

          items_Lsit.setAdapter(SalesAdapter);
      }

    private void Delete_Order(final int position) {


        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("هل انت متأكد من عملية الحذف ؟")
                //.setContentText("الاصلاحات والأعطال")
                .setConfirmText("نعم")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        cls_Order_Sales_details obj = (cls_Order_Sales_details) items_Lsit.getItemAtPosition(position);
                        Order_No = obj.getOrderNo();
                        query = "Delete from  Po_Hdr where orderno ='" + Order_No + "'";
                        sqlHandler.executeQuery(query);

                        query = "Delete from  Po_dtl where orderno ='" +Order_No + "'";
                        sqlHandler.executeQuery(query);

                        FillOrder();

                    }
                })
                .setCancelButton("لا", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
    private void Share_Order(final int position) {
        cls_Order_Sales_details obj = (cls_Order_Sales_details) items_Lsit.getItemAtPosition(position);
        Order_No = obj.getOrderNo();

            final ProgressDialog loading_dialog;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            Fill_Po_Adapter(Order_No);

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
                    "    ,include_Tax ,pay_method FROM Po_Hdr  where orderno  ='" +Order_No+"'";

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


            loading_dialog = ProgressDialog.show(this, "الرجاء الانتظار ...", "العمل جاري على اعتماد طلبات البيع", true);
            loading_dialog.setCancelable(false);
            loading_dialog.setCanceledOnTouchOutside(false);
            loading_dialog.show();
            final Handler _handler = new Handler();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    CallWebServices ws = new CallWebServices(OrderDetailsReports.this);
                    ws.Save_po(str, "Insert_PurshOrder");

                    try {

                        if (We_Result.ID > 0) {
                            ContentValues cv = new ContentValues();
                            cv.put("posted", We_Result.ID);
                            long i;
                            i = sqlHandler.Update("Po_Hdr", cv, "orderno='"+ Order_No+"'");

                            _handler.post(new Runnable() {
                                public void run() {

                                    GetServer_Date();
                                    new SweetAlertDialog(OrderDetailsReports.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("إعتماد طلب البيع")
                                            .setContentText("تمت عملية الأعتماد بنجاح")
                                            .setCustomImage(R.drawable.tick)
                                            .setConfirmText("رجــوع")
                                            .show();
                                    loading_dialog.dismiss();


                                }
                            });
                        }
                        else {


                            _handler.post(new Runnable() {
                                public void run() {
                                    new SweetAlertDialog(OrderDetailsReports.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("إعتماد طلب البيع")
                                            .setContentText("العملية لم تتم بنجاح")
                                            .setCustomImage(R.drawable.error_new)
                                            .setConfirmText("رجــوع")
                                            .show();
                                    loading_dialog.dismiss();
                                }
                            });
                        }

                    } catch (final Exception e) {

                        _handler.post(new Runnable() {
                            public void run() {

                                new SweetAlertDialog(OrderDetailsReports.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("إعتماد طلب البيع")
                                        .setContentText("العملية لم تتم بنجاح")
                                        .setCustomImage(R.drawable.error_new)
                                        .setConfirmText("رجــوع")
                                        .show();
                                loading_dialog.dismiss();
                            }
                        });
                    }

                }
            }).start();

        }
    ArrayList<ContactListItems> PoList ;
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
    public void btn_back(View view) {
        Intent k;
        k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);
    }
    @Override
    public void onBackPressed() {
        Intent k;
        k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);
    }
    private void GetServer_Date() {

        final Handler _handler = new Handler();
        tv = new TextView(this);
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

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog( this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على استرجاع البيانات  ");
        tv.setText("تفاصيل طلبات البيع");
        progressDialog.setCustomTitle(tv);
        progressDialog.setProgressDrawable(greenProgressbar);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String q;
                q = "Delete from ORDER_STUTES";
                sqlHandler.executeQuery(q);
                q = " delete from sqlite_sequence where name='ORDER_STUTES'";
                sqlHandler.executeQuery(q);

                CallWebServices ws = new CallWebServices(OrderDetailsReports.this    );
                ws.GET_ORDER_STUTES(UserID);
                try {
                    Integer i;
                    if (We_Result.ID > 0)
                    {
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_OrderNo = js.getJSONArray("OrderNo");
                        JSONArray js_PrintOrder = js.getJSONArray("PrintOrder");
                        JSONArray js_Posted = js.getJSONArray("Posted");
                        JSONArray js_AB_SalesOrder = js.getJSONArray("AB_SalesOrder");
                        JSONArray js_AB_SalesBill = js.getJSONArray("AB_SalesBill");
                        JSONArray js_Notes = js.getJSONArray("Notes");



                        for (i = 0; i < js_OrderNo.length(); i++) {
                            q = "INSERT INTO ORDER_STUTES(OrderNo,PrintOrder,Posted,AB_SalesOrder,AB_SalesBill,Notes) " +
                                    " values ('"
                                    + js_OrderNo.get(i).toString() + "','"
                                    + js_PrintOrder.get(i).toString() + "','"
                                    + js_Posted.get(i).toString() + "','"
                                    + js_AB_SalesOrder.get(i).toString() + "','"
                                    + js_AB_SalesBill.get(i).toString() + "','"
                                    + js_Notes.get(i).toString() + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_OrderNo.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }

                        _handler.post(new Runnable() {
                            public void run() {
                                FillOrder();

                                progressDialog.dismiss();
                            }
                        });

                    }else{
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                new SweetAlertDialog(OrderDetailsReports.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                        .setContentText("لا يوجد بيانات")
                                        .setCustomImage(R.drawable.error_new)
                                        .setConfirmText("رجــــوع")
                                        .show();
                            }
                        });
                    }

                } catch (final Exception e) {
                    FillOrder();

                    progressDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {

                        }
                    });
                }
            }
        }).start();


    }


}
