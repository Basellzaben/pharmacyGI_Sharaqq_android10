package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Convert_Layout_Img;
import com.cds_jo.pharmacyGI.assist.Convert_Prapre_Qty_To_Img;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hearder.main.Header_Frag;

public class PreapareManQty extends AppCompatActivity {
    SqlHandler sqlHandler;
    ListView lv_Items;
    ArrayList<ContactListItems> contactList ;
    EditText etItemNm, etPrice, etQuantity,etTax;
    Button btnsubmit;
    String UserID= "";
    public ProgressDialog loadingdialog;
    public   String json;
    Boolean IsNew ,IsChange;
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
    public  void GetMaxPONo()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");

        String query = "SELECT  Distinct COALESCE(MAX(orderno), 0) +1 AS no FROM PrepManQtyhdr where userid ='"+UserID+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";
        EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
        if (c1 != null && c1.getCount() > 0 ) {
            c1.moveToFirst();
            max= String.valueOf(c1.getInt(0));
            c1.close();
        }

        String max1="0";
        max1 = sharedPreferences.getString("m3", "");
        if (max1==""){
            max1 ="0";
        }
        max1 =String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max))
        {
            max = max1 ;

        }

        if (max.length()==1) {
            Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {

            Maxpo.setText(intToString(Integer.valueOf(max), 7)  );
        }
        Maxpo.setFocusable(false);
        Maxpo.setEnabled(false);
        Maxpo.setCursorVisible(false);



    }

    private  void  UpDateMaxOrderNo() {

        String query = " SELECT Distinct COALESCE(MAX(orderno), 0)  AS no FROM PrepManQtyhdr where userid ='"+UserID+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        max=(intToString(Integer.valueOf(max), 7)  );

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("m3", max);
        editor.commit();
    }

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_preaparemanqty);
        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
        lv_Items = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);
        IsNew = true;
        IsChange = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID= sharedPreferences.getString("UserID", "");
        try {
            GetMaxPONo();
        }
        catch (Exception ex){
            Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
        }
        try {
            contactList = new ArrayList<ContactListItems>();
            contactList.clear();
        }catch (Exception ex){
            Toast.makeText(this,"0",Toast.LENGTH_SHORT).show();
        }

        try {
            showList(0);
        }catch (Exception ex){
            Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
        }


        LinearLayout back=(LinearLayout)findViewById(R.id.imageButton7);

        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsChange){
                    btn_save_po(v);
                    return;
                Intent k = new Intent(PreapareManQty.this, MasterActivity.class);
                startActivity(k);
            }
        });
*/

        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);

        TextView accno = (TextView)findViewById(R.id.tv_acc);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));
        accno.setText(UserID);
        CustNm.setText(sharedPreferences.getString("UserName", ""));



    }

    private void showList( Integer f  ) {

        lv_Items.setAdapter(null);
        float Total = 0 ;
        float Total_Tax = 0 ;
        float  TTemp= 0 ;
        float PTemp = 0 ;
        float PQty = 0 ;
        String query ="";
        TextView     etTotal, et_Tottal_Tax ;
        TextView ed_date ;
        etTotal = (TextView) findViewById(R.id.et_Total);
        et_Tottal_Tax = (TextView) findViewById(R.id.et_TotalTax);

        ed_date=(TextView) findViewById(R.id.ed_date);

        Cls_Cust_Qty_Item_Adapter contactListAdapter = new Cls_Cust_Qty_Item_Adapter(
                PreapareManQty.this, contactList);
        lv_Items.setAdapter(contactListAdapter);
        //etTotal.setText("2222");
        // TextView et_Total = (TextView)findViewById(R.id.et_Total);
        // et_Total.setText(contactList.size());
        //  json = new Gson().toJson(contactList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    public  void showPop()
    {
        Bundle bundle = new Bundle();
        bundle.putString("Scr","PrePareQty");
        IsChange = true;
        FragmentManager Manager =  getFragmentManager();
        PopCus_Qty_Items obj = new PopCus_Qty_Items();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void btn_Po(View view) {

        Intent k = new Intent(this,Convert_Layout_Img.class);
        //  Intent k = new Intent(this,BluetoothConnectMenu.class);



        startActivity(k);
    }
    public void btn_back(View view) {
        if(IsChange){
            btn_save_po(view);
            return;
        }
        Intent k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);

    }
    public void btn_showPop(View view) {

        showPop();
    }
    public void Save_Method(String ItemNo , String p , String q ,String t,String u ,String dis ,String bounce) {

        TextView OrderNo = (TextView)findViewById(R.id.et_OrdeNo);
        String bounce_unitno,bounce_qty,dis_per,dis_value;
        bounce_unitno=bounce_qty=dis_per=dis_value="";
        String  query = "INSERT INTO PrepManQtydetl(orderno,itemno,price,qty,tax,unitNo,dis_Amy,dis_per,bounce_qty,bounce_unitno) values " +
                "('"+ OrderNo.getText().toString()+"','"+ItemNo +"',"+p+","+q+","+t+",'"+u+"','"+dis_value+"','"+dis_value+"','"+bounce_qty+"','"+bounce_unitno+"')";

        sqlHandler.executeQuery(query);
        showList(1);
    }
    public void Save_List(String ItemNo   , String q ,  String   u ,  String   ItemNm , String UnitName ) {


        for (int x = 0; x < contactList.size(); x++) {
            ContactListItems contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);

            if ( contactListItems.getNo().equals(ItemNo)) {
                AlertDialog alertDialog = new AlertDialog.Builder(

                        this).create();

                alertDialog.setTitle("Galaxy");
                alertDialog.setMessage("المادة موجودة");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();
                return;
            }

        }

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        ContactListItems contactListItems = new ContactListItems();
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        contactListItems.setUnite(u);
        contactListItems.setQty(q);
        contactListItems.setUniteNm(UnitName);
        contactListItems.setRow(0+"");
        contactList.add(contactListItems);

        showList(1);




    }
    public void Update_List(String ItemNo   , String q ,  String   u ,  String   ItemNm , String UnitName ) {





        ContactListItems contactListItems = new ContactListItems();
        contactListItems = contactList.get(position);
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        contactListItems.setUnite(u);
        contactListItems.setQty(q);
        contactListItems.setRow(0 + "");
        contactListItems.setUniteNm(UnitName);
        //contactList.add(contactListItems);

        showList(1);




    }
    public void btn_Search_Cust(View view) {

        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.show(Manager, null);
    }
    public void Set_Cust(String No, String Nm) {
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }
    public void Set_Order(String No, String Nm) {
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView Order_no = (TextView)findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView)findViewById(R.id.tv_acc);
        TextView Total = (TextView)findViewById(R.id.et_Total);
        Order_no.setText(No);
        contactList.clear();
        showList(0);

        sqlHandler = new SqlHandler(this);



        String query     = "  select Distinct Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo from PrepManQtydetl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + Order_no.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
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
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));
                    contactListItems.setBounce("0");
                    contactListItems.setDiscount("0");
                    contactListItems.setDis_Amt("0");
                    contactListItems.setDis_Amt("0");
                    contactListItems.setRow(0+"");
                    contactListItems.setTax_Amt("0");
                    contactListItems.setTotal("0");
                    contactListItems.setPro_amt("0");
                    contactListItems.setPro_dis_Per("0");
                    contactListItems.setPro_bounce("0");
                    contactListItems.setPro_Total("0");
                    contactListItems.setDisAmtFromHdr("0");
                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setProID("0");
                    contactList.add(contactListItems);
                } while (c1.moveToNext());

            }

            c1.close();
        }

        showList(0);
        IsNew=false;
    }
    public void btn_searchCustomer(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "CustQty");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_show_Pop(View view) {
        showPop();
    }
    public void btn_print(View view) {
        if(IsChange){
            btn_save_po(view);
            return;
        }
        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        Intent i = new Intent(this,Convert_Prapre_Qty_To_Img.class);
        i.putExtra("DocNo", DocNo.getText().toString());
        startActivity(i);
    }
    public void Save_Recod_Po()  {
        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView)findViewById(R.id.tv_acc);

        TextView Total = (TextView)findViewById(R.id.et_Total);
        TextView dis = (TextView)findViewById(R.id.et_dis);
        TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);

        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());



        Long i;
        ContentValues cv =new ContentValues();
        cv.put("orderno", pono.getText().toString());
        cv.put("manno", acc.getText().toString());
        cv.put("date", currentDateandTime);
        cv.put("posted", "-1");
        cv.put("userid", UserID);

        if (IsNew==true) {
            i = sqlHandler.Insert("PrepManQtyhdr", null, cv);
        }
        else {
            i = sqlHandler.Update("PrepManQtyhdr", cv, "orderno ='"+pono.getText().toString()+"'");
        }





        if(i>0){
            String q ="Delete from  PrepManQtydetl where orderno ='"+ pono.getText().toString()+"'";
            sqlHandler.executeQuery(q);

            for (int x = 0; x < contactList.size(); x++) {
                ContactListItems contactListItems = new ContactListItems();
                contactListItems = contactList.get(x);


                cv = new ContentValues();
                cv.put("orderno", pono.getText().toString());
                cv.put("itemno",contactListItems.getNo());
                cv.put("price","0");
                cv.put("qty", contactListItems.getQty().toString());
                cv.put("tax", "0");
                cv.put("unitNo", contactListItems.getUnite().toString());

                cv.put("total", "0");
                i = sqlHandler.Insert("PrepManQtydetl", null, cv);
            }
        }

        if (i> 0 ) {
            UpDateMaxOrderNo();
            // GetMaxPONo();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("Galaxy");
            alertDialog.setMessage("تمت عملية  الحفظ بنجاح");
            IsNew=false;
            IsChange = false;
            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    View view = null ;
                    btn_print(view);
                }
            });


            alertDialog.show();
        }
    }
    public void btn_save_po(View view) {


        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
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
        alertDialog.setTitle("طلب تجهيز مواد");
        alertDialog.setMessage("هل تريد الاستمرار بعملية الحفظ");
        alertDialog.setIcon(R.drawable.save);
        alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (IsNew==true) {
                    Save_Recod_Po();
                }
                else {
                    checkUpdate();
                }

            }
        });


        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });


        alertDialog.show();


    }
    private void   checkUpdate(){

        final String pass = DB.GetValue(PreapareManQty.this, "Tab_Password", "Password", "PassNo = 6");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreapareManQty.this);
        alertDialog.setTitle(DB.GetValue(PreapareManQty.this, "Tab_Password", "PassDesc", "PassNo = 6"));
        alertDialog.setMessage("الرجاء ادخال الرمز  الخاص بعملية التعديل");

        final EditText input = new EditText(PreapareManQty.this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(new PasswordTransformationMethod());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("موافق",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();

                        if (pass.equals(password)) {
                            Save_Recod_Po();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

        alertDialog.setNegativeButton("لا",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();







    }
    public void btn_delete(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("حذف طلب تجهيز مواد");
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
    public void Delete_Record_PO(){
        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView)findViewById(R.id.tv_acc);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());




        String query ="Delete from  PrepManQtyhdr where orderno ='"+ pono.getText().toString()+"'";
        sqlHandler.executeQuery(query);


        query ="Delete from  PrepManQtydetl where orderno ='"+ pono.getText().toString()+"'";
        sqlHandler.executeQuery(query);
        contactList.clear();
        GetMaxPONo();
        showList(0);
        IsNew = true;


        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("Galaxy");
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
        FragmentManager Manager =  getFragmentManager();
        SearchPreaperQty obj = new SearchPreaperQty();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_share(View view) {

        if(IsNew==true){
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("طلب تجهيز مواد");
            alertDialog.setMessage("  يجب تخزين طلب التجهيز اولاَ");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();
            return;
        }

        final  SqlHandler sql_Handler = new SqlHandler(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final   TextView custNm = (TextView)findViewById(R.id.tv_cusnm);
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        final  TextView acc = (TextView)findViewById(R.id.tv_acc);
        final  TextView Total = (TextView)findViewById(R.id.et_Total);
        final  TextView dis = (TextView)findViewById(R.id.et_dis);
        final  TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
        final  TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);
        final CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        final  String str;





        String  query = "SELECT Distinct manno,  date  FROM PrepManQtyhdr where orderno  ='" +pono.getText().toString()+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String  Date,Cust_No,Delv_day_count;
        Date=Cust_No=Delv_day_count="";

        if (c1 !=null&&  c1.getCount() != 0) {
            c1.moveToFirst();
            Cust_No=c1.getString(c1.getColumnIndex("manno"));
            Delv_day_count   ="0";
            Date  =c1.getString(c1.getColumnIndex("date"));
            c1.close();
        }
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("Cust_No", Cust_No.toString());
            jsonObject.put("Date",Date.toString());
            jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));
            jsonObject.put("OrderNo",pono.getText().toString());


        }
        catch ( JSONException ex){
            ex.printStackTrace();
        }
        String json = new Gson().toJson(contactList);
        str = jsonObject.toString()+ json;


        loadingdialog = ProgressDialog.show(PreapareManQty.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد طلب تجهز كميات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(PreapareManQty.this);
                ws.SavePrepareQty(str);
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("posted", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("PrepManQtyhdr", cv, "orderno='"+ DocNo.getText().toString()+"'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        PreapareManQty.this).create();
                                alertDialog.setTitle("اعتماد تجهيز مواد");
                                alertDialog.setMessage("تمت عملية اعتماد طلب تجهيز المواد بنجاح" + We_Result.ID +"");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();
                                contactList.clear();
                                showList(0);
                                // custNm.setText("");
                                // acc.setText("");
                                //Total.setText("");
                                //  dis.setText("");
                                //  NetTotal.setText("");
                                //  TotalTax.setText("");
                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        PreapareManQty.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " +We_Result.ID+"" );
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
                                    PreapareManQty.this).create();
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
        //  TextView custNm = (TextView)findViewById(R.id.tv_cusnm);
        //TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
//        TextView acc = (TextView)findViewById(R.id.tv_acc);
//        TextView Total = (TextView)findViewById(R.id.et_Total);
//        TextView dis = (TextView)findViewById(R.id.et_dis);
//        TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);
//        TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);


//        Total.setText("0.0");
        //     dis.setText("0.0");
        //   TotalTax.setText("0.0");
        // NetTotal.setText("0.0");


        //custNm.setText("");
        // pono.setText("");
        // acc.setText("");
        GetMaxPONo();
        contactList.clear();
        showList(0);
        IsNew = true ;
        IsChange = false;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Cls_Sal_InvItems contactListItems =new Cls_Sal_InvItems();



        menu.setHeaderTitle(contactList.get(position).getName());
        menu.add(Menu.NONE, 1, Menu.NONE, "تعديل");
        menu.add(Menu.NONE, 2, Menu.NONE, "حذف");
    }

    int position ;
    public void btn_Delete_Item( final View view) {
        position = lv_Items.getPositionForView(view);
        registerForContextMenu(view);
        openContextMenu(view);

    }
    public boolean onContextItemSelected(MenuItem item) {

        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        // TODO Auto-generated method stub
        switch(item.getItemId())
        {
            case 1:
            {
                ArrayList<ContactListItems> Itemlist = new ArrayList <ContactListItems> ();
                Itemlist.add(contactList.get(position))  ;
                IsChange = true;
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "PrePareQty");
                bundle.putString("OrderNo", pono.getText().toString());
                bundle.putSerializable("List", Itemlist);
                FragmentManager Manager = getFragmentManager();
                PopCus_Qty_Items obj = new PopCus_Qty_Items();
                obj.setArguments(bundle);
                obj.show(Manager, null);
            }
            break;
            case 2:
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("طلب تجهيز مواد");
                alertDialog.setMessage("هل انت متاكد من عملية الحذف");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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
            break;
        }

        return super.onContextItemSelected(item);
    }

}
