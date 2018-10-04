package com.cds_jo.pharmacyGI.assist;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.ContactListAdapter;
import com.cds_jo.pharmacyGI.ContactListItems;

import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.PopInvoicInfo;
import com.cds_jo.pharmacyGI.PopMenuItems;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.Sal_Inv_SearchActivity;
import com.cds_jo.pharmacyGI.SearchRetnQtyOrder;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.We_Result;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hearder.main.Header_Frag;

public class CustomerReturnQtyActivity extends AppCompatActivity {
    SqlHandler sqlHandler;
    ListView lvCustomList;
    ArrayList<ContactListItems> contactList ;
    Boolean IsNew;
    String UserID= "";
    public ProgressDialog loadingdialog;
    Double Hdr_Dis_A_Amt , Hdr_Dis_Per;
    EditText hdr_Disc;
    CheckBox chk_hdr_disc ;
    String MaxStoreQtySer = "0" ;
    public  void GetMaxPONo()    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");
        if(Login.toString().equals("No")){
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }
        String query = "SELECT  COALESCE(MAX(OrderNo), 0) +1 AS no FROM ReturnQtyhdr";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";
        EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                max = String.valueOf(c1.getInt(0));
            }
        }
        if (max.length()==1) {
            Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {
            Maxpo.setText(max);

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_customer_return_qty);


        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
        lvCustomList = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);
        // Spinner sp_Invo_Type =(Spinner)findViewById(R.id.sp_Invo_Type);
        contactList = new ArrayList<ContactListItems>();
        contactList.clear();

        IsNew = true;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID= sharedPreferences.getString("UserID", "");
        IsNew = true;

        final List<String> items_cat = new ArrayList<String>();
        items_cat.add("نوع الفاتورة");
        items_cat.add("نقدي");
        items_cat.add("ذمم");
        ArrayAdapter<String> Inoc_Type_cat = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items_cat);
        // sp_Invo_Type.setAdapter(Inoc_Type_cat);

        hdr_Disc = (EditText) findViewById(R.id.et_hdr_Disc);
        hdr_Disc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hdr_Disc.setText("", TextView.BufferType.EDITABLE);
                }
            }
        });


        Hdr_Dis_A_Amt = 0.0;
        Hdr_Dis_Per=0.0;



        hdr_Disc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (hdr_Disc.getText().toString().equals("") ||hdr_Disc.getText().toString().equals(".") )
                    return;
                else
                    Calc_Dis_Hdr();


            }
        });


        chk_hdr_disc = (CheckBox)findViewById(R.id.chk_hdr_disc);
        chk_hdr_disc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (hdr_Disc.getText().toString().equals("") ||hdr_Disc.getText().toString().equals(".") )
                    return;
                else
                    Calc_Dis_Hdr();

            }
        });


        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        Tax_Include.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CalcTotal();
                showList();


            }
        });

     /*lvCustomList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {

             EditText Order = (EditText) findViewById(R.id.et_OrdeNo);
             ContactListItems contactListItems = (ContactListItems) arg0.getItemAtPosition(arg2);
             String slno = contactListItems.getno();
           //  Toast.makeText(CustomerReturnQtyActivity.this, slno.toString(), Toast.LENGTH_SHORT).show();
             String delQuery = "DELETE FROM ReturnQtydetl where itemNo = '" + slno.toString() + "' And OrderNo = '" + Order.getText().toString() + "'";
             sqlHandler.executeQuery(delQuery);
             showList();

             return false;
         }
     });*/
        GetMaxPONo();
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);

        TextView accno = (TextView)findViewById(R.id.tv_acc);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));


        GetStoreQtySer();
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
        }
        c1.close();
    }
    private void Calc_Dis_Hdr(){

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        if(hdr_Disc.getText().toString().equals(""))
            Hdr_Dis_Per = 0.0;
        else
            Hdr_Dis_Per = Double.parseDouble(hdr_Disc.getText().toString().replace(",",""));

        TextView et_Total = (TextView)findViewById(R.id.et_Total);
        ContactListItems contactListItems;
        if (chk_hdr_disc.isChecked()){


            Hdr_Dis_A_Amt =  Double.parseDouble(et_Total.getText().toString().replace(",", "")) * (Hdr_Dis_Per /100);


            for (int x = 0; x < contactList.size(); x++) {


                contactListItems = new ContactListItems();
                contactListItems = contactList.get(x);
                //if( contactListItems.getProID().toString().equals("")) {
                Double NewPercent = 0.0;
                Double NewAmt = 0.0;
                NewPercent = Hdr_Dis_Per;
                NewAmt = (NewPercent / 100) * Double.parseDouble(contactListItems.getTotal().replace(",", ""));


                contactListItems.setDisPerFromHdr( df.format(NewPercent).toString());
                contactListItems.setDisAmtFromHdr( df.format(NewAmt).toString());


              /*  }
                else {
                      contactListItems.setDisPerFromHdr("0");
                      contactListItems.setDisAmtFromHdr("0");
                  }*/
            }

        }
        else{

            for (int x = 0; x < contactList.size(); x++) {
                contactListItems = new ContactListItems();
                contactListItems = contactList.get(x);
                Double NewPercent = 0.0;
                Double NewAmt = 0.0;
                // if( contactListItems.getProID().toString().equals("")) {
                NewAmt = Hdr_Dis_Per;
                if( contactListItems.getTotal().equals("0")){
                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setDisAmtFromHdr("0");
                }
                else {
                    NewPercent = NewAmt / Double.parseDouble(contactListItems.getTotal().replace(",", ""));
                    contactListItems.setDisPerFromHdr(df.format(NewPercent).toString());
                    contactListItems.setDisAmtFromHdr(df.format(NewAmt).toString());
                }
             /* }
              else {
                  contactListItems.setDisPerFromHdr("0");
                  contactListItems.setDisAmtFromHdr("0");
              }*/

            }

           /* Hdr_Dis_A_Amt  = Double.parseDouble(hdr_Disc.getText().toString());
            Hdr_Dis_Per  = Hdr_Dis_A_Amt /  Double.parseDouble(et_Total.getText().toString() ) ;*/
        }
        CalcTotal();
        showList();

    }
    private void showList(   ) {

        lvCustomList.setAdapter(null);
        ContactListAdapter contactListAdapter = new ContactListAdapter(
                CustomerReturnQtyActivity.this, contactList);
        lvCustomList.setAdapter(contactListAdapter);
        //  json = new Gson().toJson(contactList);
    }
    private void FillAdapter(   ) {
        contactList.clear();
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
        etTotal.setText(String.valueOf(Total));
        et_Tottal_Tax.setText(String.valueOf(Total_Tax));
        //ed_date=(TextView) findViewById(R.id.ed_date);

        sqlHandler = new SqlHandler(this);


        EditText Order_no = (EditText) findViewById(R.id.et_OrdeNo);


        query = "  select pod.bounce_qty,pod.dis_per , pod.dis_Amt ,ifnull( pod.OrgPrice,0) as OrgPrice , pod.tax_Amt , pod.total ,Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt    " +
                " from ReturnQtydetl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.OrderNo='" + Order_no.getText().toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemNo")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("price")));
                    contactListItems.setItemOrgPrice( c1.getString(c1
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


                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));

                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));

                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));


                    contactListItems.setProID(c1.getString(c1
                            .getColumnIndex("ProID")));

                    contactListItems.setPro_bounce(c1.getString(c1
                            .getColumnIndex("Pro_bounce")));

                    contactListItems.setPro_dis_Per(c1.getString(c1
                            .getColumnIndex("Pro_dis_Per")));

                    contactListItems.setPro_amt(c1.getString(c1
                            .getColumnIndex("Pro_amt")));

                    contactListItems.setPro_Total(c1.getString(c1
                            .getColumnIndex("pro_Total")));


                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setDisAmtFromHdr("0");


                    contactList.add(contactListItems);

                    TTemp = Float.valueOf(c1.getString(c1.getColumnIndex("tax")));
                    PTemp = Float.valueOf(c1.getString(c1.getColumnIndex("price")));
                    PQty = Float.valueOf(c1.getString(c1.getColumnIndex("qty")));

                    PTemp=PTemp*PQty;

                    TTemp = TTemp/100;
                    TTemp = TTemp *PTemp;

                    Total = Total + PTemp ;
                    Total_Tax = Total_Tax + TTemp;
                    PTemp=0;
                    PQty = 0 ;
                    TTemp = 0 ;
                } while (c1.moveToNext());


                etTotal.setText(String.valueOf(Total));
                et_Tottal_Tax.setText(String.valueOf(Total_Tax));
            }
        }
        c1.close();

    }
    public void btn_searchCustomer(View view) {
        /*Bundle bundle = new Bundle();
        bundle.putString("Scr", "RetnQty");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);*/
    }
    public void Set_Cust(String No, String Nm) {
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }
    public void btn_save_po( final View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT *  from  ReturnQtyhdr where   Post >0 AND   OrderNo ='" + pono.getText().toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("فاتورة مبيعات");
            alertDialog.setMessage("لقد تم ترحيل الفاتورة لايمكن التعديل");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();


            c1.close();
            return;
        } else {


            TextView custNm = (TextView) findViewById(R.id.tv_cusnm);

            TextView acc = (TextView) findViewById(R.id.tv_acc);
            if (custNm.getText().toString().length() == 0) {
                custNm.setError("required!");
                custNm.requestFocus();
                return;
            }

            if (pono.getText().toString().length() == 0) {
                pono.setError("required!");
                pono.requestFocus();
                return;
            }


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("فاتورة مبيعات");

            // Setting Dialog Message
            alertDialog.setMessage("هل  تريد الاستمرار بعملية الحفظ ");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.save);
            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // User pressed YES button. Write Logic Here
                /*Toast.makeText(getApplicationContext(), "You clicked on YES",
                        Toast.LENGTH_SHORT).show();*/

                    Save_Recod_Po();
                }
            });


            alertDialog.show();


        }
    }
    public void  Save_Recod_Po() {

        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        CheckBox chk_Type = (CheckBox)findViewById(R.id.chk_Type);
        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        EditText et_hdr_Disc = (EditText)findViewById(R.id.et_hdr_Disc);

        TextView Total = (TextView)findViewById(R.id.et_Total);
        TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);
        TextView dis = (TextView)findViewById(R.id.et_dis);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        Long i;



        ContentValues cv =new ContentValues();
        cv.put("OrderNo", pono.getText().toString());
        cv.put("acc",acc.getText().toString());
        cv.put("date",currentDateandTime);
        cv.put("userid",UserID);
        cv.put("Total",Total.getText().toString());
        cv.put("Net_Total", NetTotal.getText().toString());
        cv.put("Tax_Total", TotalTax.getText().toString());
        cv.put("Post", "-1");
        cv.put("QtyStoreSer", MaxStoreQtySer);
        cv.put("V_OrderNo",sharedPreferences.getString("V_OrderNo", "0"));



        cv.put("bounce_Total", "0");
        if (Tax_Include.isChecked()){
            cv.put("include_Tax","0");
        }
        else
        {
            cv.put("include_Tax","-1");
        }

        if (chk_Type.isChecked()){
            cv.put("inovice_type","0");
        }
        else
        {
            cv.put("inovice_type", "-1");
        }


        if (chk_hdr_disc.isChecked()){
            cv.put("hdr_dis_per","0");
        }
        else{
            cv.put("hdr_dis_per","-1");
        }
        cv.put("hdr_dis_value", et_hdr_Disc.getText().toString());

        cv.put("disc_Total", dis.getText().toString());

        if (IsNew==true) {
            i = sqlHandler.Insert("ReturnQtyhdr", null, cv);
        }
        else {
            i = sqlHandler.Update("ReturnQtyhdr", cv, "OrderNo ='"+pono.getText().toString()+"'");
        }




        if(i>0){
            String q ="Delete from  ReturnQtydetl where OrderNo ='"+ pono.getText().toString()+"'";
            sqlHandler.executeQuery(q);

            for (int x = 0; x < contactList.size(); x++) {
                ContactListItems contactListItems = new ContactListItems();
                contactListItems = contactList.get(x);


                cv = new ContentValues();
                cv.put("OrderNo", pono.getText().toString());
                cv.put("itemno",contactListItems.getNo());
                cv.put("price", contactListItems.getPrice().toString());
                cv.put("qty", contactListItems.getQty().toString());
                cv.put("tax", contactListItems.getTax().toString());
                cv.put("unitNo", contactListItems.getUnite().toString());
                cv.put("dis_per", contactListItems.getDiscount().toString());
                cv.put("dis_Amt", contactListItems.getDis_Amt().toString());
                cv.put("bounce_qty", contactListItems.getBounce().toString());
                cv.put("tax_Amt", contactListItems.getTax_Amt().toString());
                cv.put("total", contactListItems.getTotal().toString());
                cv.put("ProID", contactListItems.getProID().toString());
                cv.put("Pro_bounce", contactListItems.getPro_bounce().toString());
                cv.put("Pro_dis_Per", contactListItems.getPro_dis_Per().toString());
                cv.put("Pro_amt", contactListItems.getPro_amt().toString());
                cv.put("pro_Total", contactListItems.getPro_Total().toString());
                cv.put("OrgPrice", contactListItems.getItemOrgPrice().toString());

                //ProID
                //  text null , Pro_bounce  Text Null , Pro_dis_Per Text Null , Pro_amt Text null , pro_Total text null    ) ");









                i = sqlHandler.Insert("ReturnQtydetl", null, cv);
            }
        }



      /*  query ="INSERT INTO ReturnQtyhdr (OrderNo,Nm,acc,date) values ('"+ pono.getText().toString()+"','"+custNm.getText().toString()+"','"+acc.getText().toString()+"','"+currentDateandTime+"')";
        sqlHandler.executeQuery(query);*/



        if (i> 0 ) {
            // GetMaxPONo();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("Galaxy");
            alertDialog.setMessage("تمت عملية تخزين الفاتورة بنجاح");
          /*  contactList.clear();
            showList();
            custNm.setText("");
            acc.setText("");
            Total.setText("");
            dis.setText("");
            NetTotal.setText("");
            TotalTax.setText("");*/
            IsNew = false;
            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });


            alertDialog.show();
        }

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
    private void CalcTax(){
        Double All_Dis  = 0.0 ;
        Double RowTotal = 0.0 ;
        Double NetRow = 0.0 ;
        Double TaxAmt = 0.0 ;
        Double TaxFactor = 0.0 ;
        Double All_Dis_Per = 0.0 ;
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);


        ContactListItems contactListItems ;
        for (int x = 0; x < contactList.size(); x++) {
            contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            All_Dis = Double.parseDouble(contactListItems.getDis_Amt().replace(",", "")) + Double.parseDouble(contactListItems.getDisAmtFromHdr().replace(",", "")) +Double.parseDouble(contactListItems.getPro_amt().replace(",", "")) ;
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) +SToD(contactListItems.getPro_dis_Per()) ;

            if(Tax_Include.isChecked()) {

                contactListItems.setprice( SToD(    String.valueOf(    ( SToD(contactListItems.getItemOrgPrice()   )) / ((SToD(contactListItems.getTax()) / 100) + 1))).toString());
            }
            else
            {
                contactListItems.setprice(String.valueOf(SToD(contactListItems.getItemOrgPrice())));

            }
            //  contactListItems.setDis_Amt( (SToD(contactListItems.getprice()) * SToD(contactListItems.getQty()))  * (100)   );
            RowTotal =SToD(contactListItems.getprice()) * SToD(contactListItems.getQty())  ;
            TaxFactor =    (Double.parseDouble(contactListItems.getTax()) /100)   ;
            NetRow =RowTotal -(RowTotal * (All_Dis_Per/100)) ;
             /*if(Tax_Include.isChecked()) {
                 TaxAmt = NetRow - ( NetRow / (TaxFactor + 1)) ;
                  TaxAmt = NetRow - ( NetRow / (TaxFactor + 1)) ;
             }
             else {
                TaxAmt = NetRow  *  TaxFactor ;
           }*/
            TaxAmt = NetRow  *  TaxFactor ;
            contactListItems.setTax_Amt(df.format(TaxAmt).toString());
        }
        showList();
    }
    private void CalcTotal(){
        Double Total,Tax_Total,Dis_Amt,Po_Total;
        ContactListItems contactListItems = new ContactListItems();
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;
        Double All_Dis= 0.0;
        Double All_Dis_Per= 0.0;
        Total = 0.0;
        Tax_Total = 0.0;
        Dis_Amt = 0.0;
        Po_Total = 0.0;
        TextView Subtotal = (TextView)findViewById(R.id.et_Total);
        TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);
        TextView dis = (TextView)findViewById(R.id.et_dis);
        TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        CalcTax();
        Double RowTotal = 0.0 ;
        Double pq = 0.0;
        Double opq = 0.0;
        Double V_NetTotal = 0.0;
        for (int x = 0; x < contactList.size(); x++) {
            contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            All_Dis = SToD(contactListItems.getDis_Amt()) + SToD(contactListItems.getDisAmtFromHdr()) + SToD(contactListItems.getPro_amt());
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());
            pq = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
            opq = SToD(contactListItems.getItemOrgPrice()) * SToD(contactListItems.getQty());

            Tax_Total = Tax_Total + (SToD(contactListItems.getTax_Amt().toString()));
            Dis_Amt = Dis_Amt + (((pq) * (All_Dis_Per / 100)));

            if (Tax_Include.isChecked()) {
                RowTotal =opq  -  ((opq )*( All_Dis_Per/100)) ;
            } else{
                RowTotal =pq  -  ((pq )*( All_Dis_Per/100)) + SToD(contactListItems.getTax_Amt());
                Total = Total + pq;

            }

            V_NetTotal = V_NetTotal + SToD(RowTotal.toString());

            contactListItems.setTotal((SToD(RowTotal.toString())).toString());
            All_Dis = 0.0;

        }
        Total =  V_NetTotal - Tax_Total + Dis_Amt;
        TotalTax.setText(String.valueOf(df.format(Tax_Total)));
        Subtotal.setText(String.valueOf(df.format(Total)));
        dis.setText(String.valueOf(df.format(Dis_Amt )));
        Po_Total = Po_Total + ((SToD(Subtotal.getText().toString()) - SToD(dis.getText().toString()) ) + SToD(TotalTax.getText().toString())    );
        showList();
        NetTotal.setText(String.valueOf(df.format(V_NetTotal)));
    }
    public void btn_show_Pop(View view) {
        showPop();
    }
    public  void showPop(){
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "RetnQty");
        FragmentManager Manager =  getFragmentManager();
        PopMenuItems obj = new PopMenuItems();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void Save_List(String ItemNo , String p , String q ,String t,String u ,String dis ,String bounce,String ItemNm,String UnitName,String dis_Amt) {

        if (bounce.toString().equals(""))
            bounce = "0";

        if (dis.toString().equals(""))
            dis = "0";

        if (dis_Amt.toString().equals(""))
            dis_Amt = "0";

        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;

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

   /*     if ( checkStoreQty(ItemNo ,u  , q) == 0 )  {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("Galaxy");
            alertDialog.setMessage("الكمية المطلوبة غير متوفرة");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();
            return;
        }*/

        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        Item_Total =Double.parseDouble(q) * Double.parseDouble(p);
        Price= Double.parseDouble(p);
        Tax = Double.parseDouble(t);
        Item_Total = Double.parseDouble(Item_Total.toString());
        Double DisValue =     Double.parseDouble( dis_Amt.toString().replace(",","") );
        // Tax_Amt =(Double.parseDouble(Tax.toString()) /100)   *  ( Double.parseDouble(Item_Total.toString().replace(",","")) -  Double.parseDouble( dis_Amt.toString().replace(",","") ));
      /*  double TaxFactor = 0.0 ;
        if(Tax_Include.isChecked()) {
            TaxFactor =    (Double.parseDouble(Tax.toString()) /100)    + 1   ;
            Tax_Amt = (Item_Total - DisValue) -  (( Item_Total  - DisValue)/TaxFactor)   ;
            //Item_Total = (Item_Total/TaxFactor);
        }else  {
            Tax_Amt =(Double.parseDouble(Tax.toString()) /100)   *   ( Double.parseDouble(Item_Total.toString().replace(",",""))- DisValue);
        }*/

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        ContactListItems contactListItems = new ContactListItems();
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        if(Tax_Include.isChecked()) {
            contactListItems.setprice(   String.valueOf( Price/ ((Tax / 100) + 1)));
        }
        else
        {
            contactListItems.setprice( String.valueOf(Price ));

        }
        contactListItems.setItemOrgPrice(String.valueOf(Price));
        contactListItems.setQty(q);
        contactListItems.setTax(String.valueOf(Tax));
        contactListItems.setUnite(u);
        contactListItems.setBounce(bounce);
        contactListItems.setDiscount(dis);
        contactListItems.setProID("");
        contactListItems.setDis_Amt(dis_Amt);
        contactListItems.setUniteNm(UnitName);
        contactListItems.setPro_amt("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_Total("0");
        contactListItems.setDisAmtFromHdr("0");
        contactListItems.setDisPerFromHdr("0");

        contactListItems.setTax_Amt("0");
        contactListItems.setTotal(String.valueOf(df.format(Item_Total)));
        contactList.add(contactListItems);

        CalcTotal();
        showList();
        Calc_Dis_Hdr();
    }
    public void Update_List(String ItemNo , String p , String q ,String t,String u ,String dis ,String bounce,String ItemNm,String UnitName,String dis_Amt) {

        if (bounce.toString().equals(""))
            bounce = "0";

        if (dis.toString().equals(""))
            dis = "0";

        if (dis_Amt.toString().equals(""))
            dis_Amt = "0";

        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;



   /*     if ( checkStoreQty(ItemNo ,u  , q) == 0 )  {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("Galaxy");
            alertDialog.setMessage("الكمية المطلوبة غير متوفرة");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();
            return;
        }*/

        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        Item_Total =Double.parseDouble(q) * Double.parseDouble(p);
        Price= Double.parseDouble(p);
        Tax = Double.parseDouble(t);
        Item_Total = Double.parseDouble(Item_Total.toString());
        Double DisValue =     Double.parseDouble( dis_Amt.toString().replace(",","") );


        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        ContactListItems contactListItems = new ContactListItems();
        contactListItems = contactList.get(position);
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        contactListItems.setprice(String.valueOf(Price));
        contactListItems.setQty(q);
        contactListItems.setTax(String.valueOf(Tax));
        contactListItems.setUnite(u);
        contactListItems.setBounce(bounce);
        contactListItems.setDiscount(dis);
        contactListItems.setProID("");
        contactListItems.setDis_Amt(dis_Amt);
        contactListItems.setUniteNm(UnitName);
        contactListItems.setPro_amt("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_Total("0");
        contactListItems.setDisAmtFromHdr("0");
        contactListItems.setDisPerFromHdr("0");
        contactListItems.setTax_Amt("0");
        contactListItems.setItemOrgPrice(String.valueOf(Price));
        contactListItems.setTotal(String.valueOf(df.format(Item_Total)));
        // contactList.add(contactListItems);

        CalcTotal();
        showList();
        Calc_Dis_Hdr();
        position = -1 ;
    }
    public void Save_Method(String m , String p , String q ,String t ,String u    ) {
        EditText Order=(EditText)findViewById(R.id.et_OrdeNo);
        String query =  "INSERT INTO ReturnQtydetl(OrderNo,itemNo,unitNo,price,qty,tax) values ("+ Order.getText().toString()+",'" + m +"','"+ u +"','" + p + "','" + q + "','" + t + "')";
        sqlHandler.executeQuery(query);
        showList();
    }
    public void btn_delete(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT *  from  ReturnQtyhdr where   Post >0 AND   OrderNo ='" + pono.getText().toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("فاتورة مبيعات");
            alertDialog.setMessage("لقد تم ترحيل الفاتورة لايمكن التعديل");            // Setting Icon to Dialog
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

            // Setting Dialog Title
            alertDialog.setTitle("حذف فاتورة مبيعات");

            // Setting Dialog Message
            alertDialog.setMessage("هل انت متاكد من عملية الحذف");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.delete);
            // Setting Negative "NO" Button
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
                    Delete_Record_PO();
                    // Write your code here to invoke YES event
                    // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                }
            });


            // Showing Alert Message
            alertDialog.show();
        }

    }
    public void Delete_Record_PO(){

        TextView OrdeNo = (TextView)findViewById(R.id.et_OrdeNo);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());




        String query ="Delete from  ReturnQtyhdr where OrderNo ='"+ OrdeNo.getText().toString()+"'";
        sqlHandler.executeQuery(query);


        query ="Delete from  ReturnQtydetl where OrderNo ='"+ OrdeNo.getText().toString()+"'";
        sqlHandler.executeQuery(query);

        GetMaxPONo();
        showList();
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView accno = (TextView)findViewById(R.id.tv_acc);
        CustNm.setText("");
        accno.setText("");
        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        CheckBox chk_Type = (CheckBox)findViewById(R.id.chk_Type);

        Tax_Include.setChecked(false);
        chk_Type.setChecked(false);


        EditText et_hdr_Disc = (EditText)findViewById(R.id.et_hdr_Disc);
        et_hdr_Disc.setText("0");
        CheckBox chk_hdr_disc = (CheckBox)findViewById(R.id.chk_hdr_disc);
        chk_hdr_disc.setChecked(false);


        IsNew = true;
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();

        // Setting Dialog Title
        alertDialog.setTitle("مردودات المبيعات");

        // Setting Dialog Message
        alertDialog.setMessage("تمت عملية الحذف بنجاح");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
    public void btn_Search_Orders(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "RetQty");
        FragmentManager Manager =  getFragmentManager();
        SearchRetnQtyOrder obj = new SearchRetnQtyOrder();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void Set_Order(String No) {
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView no = (TextView)findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView)findViewById(R.id.tv_acc);
        EditText et_hdr_Disc = (EditText)findViewById(R.id.et_hdr_Disc);

        TextView tv_NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
        TextView et_TotalTax = (TextView)findViewById(R.id.et_TotalTax);
        TextView et_dis = (TextView)findViewById(R.id.et_dis);
        no.setText(No);

        FillAdapter();
        showList();
        String q = "Select ifnull( s.hdr_dis_value,0) as hdr_dis_value  ,  ifnull(s.hdr_dis_per,-1) as hdr_dis_per ,  s.inovice_type   ,s.include_Tax ,  s.disc_Total, s.OrderNo,s.Net_Total,s.Tax_Total ,s.acc ,s.date , c.name  from  ReturnQtyhdr s inner join Customers c on c.no =s.acc   where s.OrderNo = '"+No+"'";

        Cursor c1 = sqlHandler.selectQuery(q);
        CustNm.setText("");
        accno.setText("");
        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        CheckBox chk_Type = (CheckBox)findViewById(R.id.chk_Type);


        CheckBox chk_hdrdiscount = (CheckBox)findViewById(R.id.chk_hdr_disc);
        chk_Type.setChecked(false);
        Tax_Include.setChecked(false);
        chk_hdrdiscount.setChecked(false);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                CustNm.setText(c1.getString(c1.getColumnIndex("name")).toString());
                accno.setText(c1.getString(c1.getColumnIndex("acc")));
                tv_NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")));
                et_TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")));
                et_dis.setText(c1.getString(c1.getColumnIndex("disc_Total")));
                //  et_hdr_Disc.setText(c1.getString(c1.getColumnIndex("hdr_dis_value")));



                //   if (c1.getString(c1.getColumnIndex("hdr_dis_per")).equals("0")) {
                //      chk_hdrdiscount.setChecked(true);
                //   }
                if (c1.getString(c1.getColumnIndex("include_Tax")).equals("0")) {
                    Tax_Include.setChecked(true);
                }
                if (c1.getString(c1.getColumnIndex("inovice_type")).equals("0")){
                    chk_Type.setChecked(true);
                }
            }
        }



        IsNew = false;
    }
    public void btn_print(View view) {
        Intent k = new Intent(this,Convert_Sal_Invoice_To_ImgActivity.class);
        // Intent k = new Intent(this,BluetoothConnectMenu.class);
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView OrdeNo = (TextView)findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView)findViewById(R.id.tv_acc);
        CheckBox chk_showTax= (CheckBox)findViewById(R.id.chk_showTax);

        k.putExtra("OrderNo", OrdeNo.getText().toString());
        if(chk_showTax.isChecked())
        {
            k.putExtra("ShowTax", "1");
        }
        else
        {
            k.putExtra("ShowTax", "0");
        }
        startActivity(k);
    }
    public void btn_new(View view) {

        GetMaxPONo();
        showList();
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView accno = (TextView)findViewById(R.id.tv_acc);
        EditText et_hdr_Disc = (EditText)findViewById(R.id.et_hdr_Disc);
        et_hdr_Disc.setText("0");
        // CustNm.setText("");
        // accno.setText("");
        CheckBox chk_hdr_disc = (CheckBox)findViewById(R.id.chk_hdr_disc);
        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        CheckBox chk_Type = (CheckBox)findViewById(R.id.chk_Type);
        IsNew = true;
        Tax_Include.setChecked(false);
        chk_Type.setChecked(false);
        chk_hdr_disc.setChecked(false);
    }
    public void btn_back(View view) {
        Intent k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);
    }
    int position ;
    public void btn_Delete_Item( final View view) {
        position = lvCustomList.getPositionForView(view);
        registerForContextMenu(view);
        openContextMenu(view);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ContactListItems contactListItems =new ContactListItems();


        contactListItems = contactList.get(position);
        menu.setHeaderTitle(contactListItems.getName());
        menu.add(Menu.NONE, 1, Menu.NONE, "تعديل");
        menu.add(Menu.NONE, 2, Menu.NONE, "حذف");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId())
        {
            case 1:
            {

                ArrayList<ContactListItems> Itemlist = new ArrayList <ContactListItems> ();

                Itemlist.add(contactList.get(position))  ;
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "RetnQty");
                bundle.putSerializable("List", Itemlist);
                FragmentManager Manager =  getFragmentManager();
                PopMenuItems obj = new PopMenuItems();
                obj.setArguments(bundle);
                obj.show(Manager, null);
            }
            break;
            case 2:
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("فاتورة مبيعات");
                alertDialog.setMessage("هل انت متاكد من عملية الحذف");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        contactList.remove(position);
                        CalcTotal();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());






        ContactListItems Inv_Obj ;

        for (int j = 0; j < contactList.size(); j++) {
            Inv_Obj = new ContactListItems();
            Inv_Obj = contactList.get(j);

            Inv_Obj.setDis_Amt(String.valueOf(Double.parseDouble(Inv_Obj.getDis_Amt()) + Double.parseDouble(Inv_Obj.getPro_amt()) + Double.parseDouble(Inv_Obj.getDisAmtFromHdr())));
            Inv_Obj.setDiscount(String.valueOf(Double.parseDouble(Inv_Obj.getDiscount()) + Double.parseDouble(Inv_Obj.getPro_dis_Per()) + Double.parseDouble(Inv_Obj.getDisPerFromHdr())));
            Inv_Obj.setBounce(String.valueOf(Double.parseDouble(Inv_Obj.getBounce()) + Double.parseDouble(Inv_Obj.getPro_bounce()) ));

        }



        final  String str;
        String  query = "SELECT V_OrderNo, OrderNo, acc,date,UserID, COALESCE(hdr_dis_per,0) as hdr_dis_per  , COALESCE(hdr_dis_value ,0) as  hdr_dis_value , COALESCE(Total ,0) as  Total , COALESCE(Net_Total ,0) as Net_Total , COALESCE( Tax_Total ,0) as Tax_Total , COALESCE(bounce_Total ,0) as bounce_Total , COALESCE( include_Tax ,0) as include_Tax" +
                " ,COALESCE( disc_Total ,0) as  disc_Total , COALESCE(inovice_type ,0)  as inovice_type  FROM ReturnQtyhdr where OrderNo  ='" +pono.getText().toString()+"'";

        if (IsNew == true){


        }
        Cursor c1 = sqlHandler.selectQuery(query);
        String  Date,Cust_No,Delv_day_count;
        Date=Cust_No=Delv_day_count="";
        JSONObject jsonObject = new JSONObject();
        if (c1 !=null&&  c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("Cust_No", c1.getString(c1.getColumnIndex("acc")));
                jsonObject.put("Date",c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));
                jsonObject.put("OrderNo",c1.getString(c1.getColumnIndex("OrderNo")));
                jsonObject.put("V_OrderNo",c1.getString(c1.getColumnIndex("V_OrderNo")));

                jsonObject.put("hdr_dis_per",c1.getString(c1.getColumnIndex("hdr_dis_per")));
                if (c1.getString(c1.getColumnIndex("hdr_dis_value")).equals("")) {
                    jsonObject.put("hdr_dis_value","0");
                } else {
                    jsonObject.put("hdr_dis_value",c1.getString(c1.getColumnIndex("hdr_dis_value")));
                }
                jsonObject.put("Total",c1.getString(c1.getColumnIndex("Total")));
                jsonObject.put("Net_Total",c1.getString(c1.getColumnIndex("Net_Total")));
                jsonObject.put("Tax_Total",c1.getString(c1.getColumnIndex("Tax_Total")));
                jsonObject.put("bounce_Total",c1.getString(c1.getColumnIndex("bounce_Total")));
                jsonObject.put("include_Tax",c1.getString(c1.getColumnIndex("include_Tax")));
                jsonObject.put("disc_Total",c1.getString(c1.getColumnIndex("disc_Total")));
                jsonObject.put("inovice_type",c1.getString(c1.getColumnIndex("inovice_type")));

            }
            catch ( JSONException ex){
                ex.printStackTrace();
            }

        }
        c1.close();
        String json = new Gson().toJson(contactList);
        str = jsonObject.toString()+ json;


        loadingdialog = ProgressDialog.show(CustomerReturnQtyActivity.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد مردود المبيعات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(CustomerReturnQtyActivity.this);
                ws.Save_Ret_Sal_Invoice(str);
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("Post", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("ReturnQtyhdr", cv, "OrderNo='"+ DocNo.getText().toString()+"'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        CustomerReturnQtyActivity.this).create();
                                alertDialog.setTitle("ترحيل مردود مبيعات");
                                alertDialog.setMessage("تمت عملية ترحيل مردود المبيعات بنجاح");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();
                                /*contactList.clear();
                                showList();
                                custNm.setText("");
                                acc.setText("");
                                Total.setText("");
                                dis.setText("");
                                NetTotal.setText("");
                                TotalTax.setText("");*/
                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        CustomerReturnQtyActivity.this).create();
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
                                    CustomerReturnQtyActivity.this).create();
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
    public void btn_Search_Invoice(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "RetnQty");
        FragmentManager Manager =  getFragmentManager();
        Sal_Inv_SearchActivity obj = new Sal_Inv_SearchActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void Set_Order_From_invoice(String No) {
        // TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView no = (TextView)findViewById(R.id.et_InvoiceNo);
        // TextView accno = (TextView)findViewById(R.id.tv_acc);
        EditText et_hdr_Disc = (EditText)findViewById(R.id.et_hdr_Disc);

        TextView tv_NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
        TextView et_TotalTax = (TextView)findViewById(R.id.et_TotalTax);
        TextView et_dis = (TextView)findViewById(R.id.et_dis);
        no.setText(No);

        FillAdapterFrom_Invoice();
        showList();
        String q = "Select ifnull( s.hdr_dis_value,0) as hdr_dis_value  ,  ifnull(s.hdr_dis_per,-1) as hdr_dis_per ,  s.inovice_type   ,s.include_Tax ,  s.disc_Total, s.OrderNo,s.Net_Total,s.Tax_Total ,s.acc ,s.date , c.name  from  Sal_invoice_Hdr s inner join Customers c on c.no =s.acc   where s.OrderNo = '"+No+"'";

        Cursor c1 = sqlHandler.selectQuery(q);
        // CustNm.setText("");
        // accno.setText("");
        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        CheckBox chk_Type = (CheckBox)findViewById(R.id.chk_Type);


        CheckBox chk_hdrdiscount = (CheckBox)findViewById(R.id.chk_hdr_disc);
        chk_Type.setChecked(false);
        Tax_Include.setChecked(false);
        chk_hdrdiscount.setChecked(false);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                //  CustNm.setText(c1.getString(c1.getColumnIndex("name")).toString());
                //  accno.setText(c1.getString(c1.getColumnIndex("acc")));
                tv_NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")));
                et_TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")));
                et_dis.setText(c1.getString(c1.getColumnIndex("disc_Total")));
                et_hdr_Disc.setText(c1.getString(c1.getColumnIndex("hdr_dis_value")));



                if (c1.getString(c1.getColumnIndex("hdr_dis_per")).equals("0")) {
                    chk_hdrdiscount.setChecked(true);
                }
                if (c1.getString(c1.getColumnIndex("include_Tax")).equals("0")) {
                    Tax_Include.setChecked(true);
                }
                if (c1.getString(c1.getColumnIndex("inovice_type")).equals("0")){
                    chk_Type.setChecked(true);
                }
            }
        }

    }
    private void FillAdapterFrom_Invoice(   ) {
        contactList.clear();
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
        etTotal.setText(String.valueOf(Total));
        et_Tottal_Tax.setText(String.valueOf(Total_Tax));
        //ed_date=(TextView) findViewById(R.id.ed_date);

        sqlHandler = new SqlHandler(this);


        EditText Order_no = (EditText) findViewById(R.id.et_OrdeNo);
        query = "  select pod.bounce_qty,pod.dis_per , pod.dis_Amt , pod.tax_Amt , pod.total ,Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt    " +
                " from Sal_invoice_Det pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.OrderNo='" + Order_no.getText().toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemNo")));
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
                    contactListItems.setBounce(c1.getString(c1
                            .getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1
                            .getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));


                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));

                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));

                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));


                    contactListItems.setProID(c1.getString(c1
                            .getColumnIndex("ProID")));

                    contactListItems.setPro_bounce(c1.getString(c1
                            .getColumnIndex("Pro_bounce")));

                    contactListItems.setPro_dis_Per(c1.getString(c1
                            .getColumnIndex("Pro_dis_Per")));

                    contactListItems.setPro_amt(c1.getString(c1
                            .getColumnIndex("Pro_amt")));

                    contactListItems.setPro_Total(c1.getString(c1
                            .getColumnIndex("pro_Total")));
                    contactListItems.setItemOrgPrice(c1.getString(c1
                            .getColumnIndex("price")));


                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setDisAmtFromHdr("0");


                    contactList.add(contactListItems);

                    TTemp = Float.valueOf(c1.getString(c1.getColumnIndex("tax")));
                    PTemp = Float.valueOf(c1.getString(c1.getColumnIndex("price")));
                    PQty = Float.valueOf(c1.getString(c1.getColumnIndex("qty")));

                    PTemp=PTemp*PQty;

                    TTemp = TTemp/100;
                    TTemp = TTemp *PTemp;

                    Total = Total + PTemp ;
                    Total_Tax = Total_Tax + TTemp;
                    PTemp=0;
                    PQty = 0 ;
                    TTemp = 0 ;
                } while (c1.moveToNext());


                etTotal.setText(String.valueOf(Total));
                et_Tottal_Tax.setText(String.valueOf(Total_Tax));
            }
        }
        c1.close();

    }
    public void ShowInvoicePop(View view) {

        EditText InvoiceNo = (EditText)findViewById(R.id.et_InvoiceNo);
        Bundle bundle = new Bundle();
        bundle.putString("No", InvoiceNo.getText().toString());
        FragmentManager Manager =  getFragmentManager();
        PopInvoicInfo obj = new PopInvoicInfo();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
}
