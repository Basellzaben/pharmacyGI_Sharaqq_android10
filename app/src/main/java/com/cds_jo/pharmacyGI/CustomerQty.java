package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cds_jo.pharmacyGI.CustLocations.CustLocaltions;
import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hearder.main.Header_Frag;

public class CustomerQty extends FragmentActivity {
    SqlHandler sqlHandler;
    ListView lv_Items;
    ArrayList<ContactListItems> contactList ;
    EditText etItemNm, etPrice, etQuantity,etTax;
    Button btnsubmit;
    String UserID= "";
    public ProgressDialog loadingdialog;
    public   String json;
    Boolean IsNew;
    TextView pono;
    public  void GetMaxPONo()    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");

        String query = "SELECT  COALESCE(MAX(orderno), 0) +1 AS no FROM CustStoreQtyhdr where userid ='"+UserID+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            max= String.valueOf(c1.getInt(0));
        }

        if (max.length()==1) {
            pono.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {
            pono.setText(max);

        }

        // Maxpo.setText(max);

    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_customer_qty);

          pono = (TextView)findViewById(R.id.et_OrdeNo);

        lv_Items = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);
        IsNew = true;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID= sharedPreferences.getString("UserID", "");

        GetMaxPONo();

        contactList = new ArrayList<ContactListItems>();
        contactList.clear();

        showList(0);





        LinearLayout back=(LinearLayout)findViewById(R.id.imageButton7);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(CustomerQty.this, GalaxyMainActivity.class);
                startActivity(k);
            }
        });


        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);

        TextView accno = (TextView)findViewById(R.id.tv_acc);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));

        Fragment frag=new Header_Frag();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
    }
    private void showList( Integer f  ) {

         lv_Items.setAdapter(null);


        Cls_Cust_Qty_Item_Adapter contactListAdapter = new Cls_Cust_Qty_Item_Adapter(
                CustomerQty.this, contactList);
        lv_Items.setAdapter(contactListAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public  void showPop()    {


        Bundle bundle = new Bundle();
        bundle.putString("Scr", "CustQty");

        android.app.FragmentManager Manager = getFragmentManager();
        PopCus_Qty_Items obj = new PopCus_Qty_Items();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void btn_back(View view) {
        Intent k= new Intent(this, Main2Activity.class);
        startActivity(k);

    }
    public void Save_Method(String ItemNo , String p , String q ,String t,String u ,String dis ,String bounce) {


        String bounce_unitno,bounce_qty,dis_per,dis_value;
        bounce_unitno=bounce_qty=dis_per=dis_value="";
        String  query = "INSERT INTO CustStoreQtydetl(orderno,itemno,price,qty,tax,unitNo,dis_Amy,dis_per,bounce_qty,bounce_unitno) values " +
                "('"+ pono.getText().toString()+"','"+ItemNo +"',"+p+","+q+","+t+",'"+u+"','"+dis_value+"','"+dis_value+"','"+bounce_qty+"','"+bounce_unitno+"')";

        sqlHandler.executeQuery(query);
        showList(1);
    }
    public void Save_List(String ItemNo   , String q ,  String   u ,  String   ItemNm , String UnitName, String   ExpDate,String Batch ) {



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        TextView accno = (TextView)findViewById(R.id.tv_acc);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        ContactListItems contactListItems = new ContactListItems();
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        contactListItems.setUnite(u);
        contactListItems.setQty(q);
        contactListItems.setUniteNm(UnitName);
        contactListItems.setCust_No(accno.getText().toString());
        contactListItems.setOrderNo(pono.getText().toString());
        contactListItems.setOrder_Date(currentDateandTime);
        contactListItems.setBatch(Batch);
        contactListItems.setExpDate(ExpDate);
        contactListItems.setUserID(UserID);
        contactList.add(contactListItems);
        showList(1);




    }


    public void Set_Cust(String No, String Nm) {
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }
    @SuppressLint("Range")
    public void Set_Order(String No, String Nm, String acc) {
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);

        TextView accno = (TextView)findViewById(R.id.tv_acc);



        pono.setText(No);
        CustNm.setText(Nm);
        accno.setText(acc);
        contactList.clear();
        showList(0);

        sqlHandler = new SqlHandler(this);


        String query = "  select     CustStoreQtyhdr.acc  ,IFNULL( c.name ,'') as name    from CustStoreQtyhdr Left join Customers c on c.no = CustStoreQtyhdr.acc     where orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                accno.setText(c1.getString(c1.getColumnIndex("acc")).toString());
                CustNm.setText(c1.getString(c1.getColumnIndex("name")).toString());



            }
         c1.close();
    }

        query = "  select  pod.Batch,pod.ExpDate   ,pod.orderno, pod.Cust_No ,pod.Order_Date ,pod.UserID,     Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo ,pod.dis_Amt,pod.dis_per,pod.bounce_qty ,  pod.tax_Amt   , pod.total  " +
                "from CustStoreQtydetl pod left join invf on invf.Item_No =  pod.itemno " +
                " left join Unites on Unites.Unitno=  pod.unitNo  " +
                " Where pod.orderno='" + pono.getText().toString() + "'";
        c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice("0");
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax("0");
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setBounce("0");
                    contactListItems.setDiscount("0");
                    contactListItems.setDis_Amt("0");

                    contactListItems.setDis_Amt("0");
                    contactListItems.setBatch(c1.getString(c1.getColumnIndex("Batch")));
                    contactListItems.setExpDate(c1.getString(c1.getColumnIndex("ExpDate")));

                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));

                    contactListItems.setTax_Amt("0");

                    contactListItems.setCust_No(c1.getString(c1
                            .getColumnIndex("Cust_No")));

                    contactListItems.setOrder_Date(c1.getString(c1
                            .getColumnIndex("Order_Date")));
                    contactListItems.setUserID(c1.getString(c1
                            .getColumnIndex("UserID")));

                    contactListItems.setOrderNo(c1.getString(c1
                            .getColumnIndex("orderno")));

                    contactListItems.setTax_Amt("0");

                    contactListItems.setTotal("0");


                    contactList.add(contactListItems);

                } while (c1.moveToNext());

            }

        c1.close();
    }

        showList(0);
        IsNew=false;
    }
    public void btn_searchCustomer(View view) {

      /*  Bundle bundle = new Bundle();
        bundle.putString("Scr", "CustQty");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);*/
    }
    public void btn_show_Pop(View view) {
        showPop();
    }
    public void btn_print(View view) {

/*
          Intent k = new Intent(this,Convert_Layout_Img.class);

        k.putExtra("Scr", "po");
        TextView   CustNm =(TextView)findViewById(R.id.tv_cusnm);

        TextView   accno = (TextView)findViewById(R.id.tv_acc);
        k.putExtra("cusnm", CustNm.getText().toString());
        k.putExtra("OrderNo", OrdeNo.getText().toString());
        k.putExtra("accno", accno.getText().toString());

        startActivity(k);*/
    }
    public void Save_Recod_Po() {


        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);

        TextView acc = (TextView)findViewById(R.id.tv_acc);



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        String q = "SELECT Distinct *  from  CustStoreQtyhdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor cc = sqlHandler.selectQuery(q);

        if (cc != null && cc.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("جرد كميات العميل");
            alertDialog.setMessage("لا يمكن التعديل لقد  تم ترحيل المستند");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            cc.close();
            return;
        } else {


            String q1 = "Select * From CustStoreQtyhdr Where orderno='" + pono.getText().toString() + "'";
            Cursor c1;
            c1 = sqlHandler.selectQuery(q1);

            if (c1 != null && c1.getCount() != 0) {
                IsNew = false;
                c1.close();
            } else {
                IsNew = true;
            }


            Long i;
            ContentValues cv = new ContentValues();
            cv.put("orderno", pono.getText().toString());
            cv.put("acc", acc.getText().toString());
            cv.put("date", currentDateandTime);
            cv.put("userid", UserID);
            cv.put("Total", "0");
            cv.put("Net_Total", "0");
            cv.put("Tax_Total", "0");
            cv.put("bounce_Total", "0");
            cv.put("include_Tax", "-1");
            cv.put("posted", "-1");

            cv.put("disc_Total", "0");


            if (IsNew == true) {
                i = sqlHandler.Insert("CustStoreQtyhdr", null, cv);
            } else {
                i = sqlHandler.Update("CustStoreQtyhdr", cv, "orderno ='" + pono.getText().toString() + "'");
            }


            if (i > 0) {
                  q = "Delete from  CustStoreQtydetl where orderno ='" + pono.getText().toString() + "'";
                sqlHandler.executeQuery(q);

                for (int x = 0; x < contactList.size(); x++) {
                    ContactListItems contactListItems = new ContactListItems();
                    contactListItems = contactList.get(x);


                    cv = new ContentValues();
                    cv.put("orderno", pono.getText().toString());
                    cv.put("itemno", contactListItems.getNo());
                    cv.put("price", "0");
                    cv.put("qty", contactListItems.getQty().toString());
                    cv.put("tax", "0");
                    cv.put("unitNo", contactListItems.getUnite().toString());
                    cv.put("Cust_No", acc.getText().toString());
                    cv.put("Order_Date", currentDateandTime);
                    cv.put("userid", UserID);
                    cv.put("tax_Amt", "0");
                    cv.put("total", "0");
                    cv.put("Batch", contactListItems.getBatch().toString());
                    cv.put("ExpDate", contactListItems.getExpDate().toString());

                    i = sqlHandler.Insert("CustStoreQtydetl", null, cv);
                }
            }

            if (i > 0) {
                //   GetMaxPONo();
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("Galaxy");
                alertDialog.setMessage("تمت عملية  الحفظ بنجاح");
                //contactList.clear();
                //showList(0);
           /* custNm.setText("");
            acc.setText("");*/

                alertDialog.setIcon(R.drawable.tick);

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                alertDialog.show();
            }
        }
    }
    public void btn_save_po(View view) {


        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);

        TextView acc = (TextView)findViewById(R.id.tv_acc);
        if( custNm.getText().toString().length() == 0 ) {
            custNm.setError("required!");
            custNm.requestFocus();
            return;
        }

        if( pono.getText().toString().length() == 0 ) {
            pono.setError("required!");
            pono.requestFocus();
            return;
        }


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Galaxy");
        alertDialog.setMessage("هل تريد الاستمرار بعملية الحفظ");
        alertDialog.setIcon(R.drawable.save);
        alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Save_Recod_Po();
            }
        });


        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });


        alertDialog.show();


    }
    public void btn_delete(View view) {

        String q = "SELECT Distinct *  from  CustStoreQtyhdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("جرد كميات العميل");
            alertDialog.setMessage("لا يمكن التعديل لقد  تم ترحيل المستند");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            c1.close();
            return;
        } else {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("جرد كميات العميل");
            alertDialog.setMessage("هل انت متاكد من عملية الحذف");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Delete_Record_PO();

                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }

    }
    public void Delete_Record_PO(){
        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);

        TextView acc = (TextView)findViewById(R.id.tv_acc);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());




        String query ="Delete from  CustStoreQtyhdr where orderno ='"+ pono.getText().toString()+"'";
        sqlHandler.executeQuery(query);


        query ="Delete from  CustStoreQtydetl where orderno ='"+ pono.getText().toString()+"'";
        sqlHandler.executeQuery(query);
        contactList.clear();
        GetMaxPONo();
        showList(0);
        IsNew = true;
      /*  custNm.setText("");
        acc.setText("");*/

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("جرد كميات العميل ");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
    public void btn_Search_Orders(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        android.app.FragmentManager Manager = getFragmentManager();
        SearchCustStoreQtyActivity obj = new SearchCustStoreQtyActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_share(View view) {
        final  SqlHandler sql_Handler = new SqlHandler(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);




        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        final  String str;

        String q ="";

        q ="Delete from  CustStoreQtydetl  where orderno in   ( select orderno from  CustStoreQtyhdr   where ifnull(posted,'-1') !='-1' And ( (Order_Date)   <  ('"+currentDateandTime+"')))  " ;
        sqlHandler.executeQuery(q);
        q ="Delete from  CustStoreQtyhdr where ifnull(posted,'-1')!='-1'  And ( (Order_Date)   <  ('"+currentDateandTime+"'))  " ;
        sqlHandler.executeQuery(q);





          q = " SELECT  Distinct COALESCE (orderno, 0)  AS no FROM CustStoreQtyhdr   where orderno = '" + pono.getText().toString() + "'";
        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c = sqlHandler.selectQuery(q);
        if (c != null && c.getCount() > 0) {

            c.close();
            Do_Share();
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("جرد كميات العميل");
            alertDialog.setMessage("يجب تخزين المستند اولاَ");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();



        }



    }
    private  void Do_Share(){

        final  SqlHandler sql_Handler = new SqlHandler(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        final  String str;

        String json = new Gson().toJson(contactList);
        str =  json;
        loadingdialog = ProgressDialog.show(CustomerQty.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد المستند", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(CustomerQty.this);
                ws.Save_CustQty(str);
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();

                        cv.put("posted", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("CustStoreQtyhdr", cv, "orderno='"+ pono.getText().toString()+"'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        CustomerQty.this).create();
                                alertDialog.setTitle("اعتماد جرد كميات العميل");
                                alertDialog.setMessage("تمت عملية اعتماد المستند" + We_Result.ID +"");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();
                                contactList.clear();
                                GetMaxPONo();
                                showList(0);






                            }
                        });
                    }
                    else if(We_Result.ID ==-777) {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        CustomerQty.this).create();
                                alertDialog.setTitle("جرد كميات العميل" );
                                alertDialog.setMessage("فشل في عملية الاعتماد،تم اعتماد الجرد من قبل");
                                alertDialog.setIcon(R.drawable.error_new);
                                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                            }
                        });
                    }else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        CustomerQty.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " +We_Result.ID+"" );
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
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
                                    CustomerQty.this).create();
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
    public void btn_new(View view) {
        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);

        TextView acc = (TextView)findViewById(R.id.tv_acc);
        // TextView Total = (TextView)findViewById(R.id.et_Total);
        //TextView dis = (TextView)findViewById(R.id.et_dis);
        //TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);
        //TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);


//        Total.setText("0.0");
        //dis.setText("0.0");
        //TotalTax.setText("0.0");
        //NetTotal.setText("0.0");


       // custNm.setText("");
        pono.setText("");
      //  acc.setText("");
        GetMaxPONo();
        contactList.clear();
        showList(0);
    }
    public void btn_Delete_Item( final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("جرد كميات العميل ");
        alertDialog.setMessage("هل انت متاكد من عملية الحذف");
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int position =lv_Items.getPositionForView(view);
                contactList.remove(position);

                showList(0);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void btn_SalesOrders(View view) {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), OrdersItems.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
