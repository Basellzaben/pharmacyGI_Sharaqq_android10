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
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.Cls_Offers_Dtl_Gifts;
import com.cds_jo.pharmacyGI.Cls_Offers_Groups;
import com.cds_jo.pharmacyGI.Cls_Offers_Hdr;
import com.cds_jo.pharmacyGI.Cls_Sal_Inv_Adapter;
import com.cds_jo.pharmacyGI.Cls_Sal_InvItems;
import com.cds_jo.pharmacyGI.DB;
import com.cds_jo.pharmacyGI.GalaxyMainActivity;

import com.cds_jo.pharmacyGI.PopSal_Inv_Select_Items;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.Sal_Inv_SearchActivity;
import com.cds_jo.pharmacyGI.SearchManBalanceQty;
import com.cds_jo.pharmacyGI.Select_Cash_Customer;
import com.cds_jo.pharmacyGI.Select_Customer;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.We_Result;
import com.google.gson.Gson;

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
import Methdes.MyTextView_Digital;
import hearder.main.Header_Frag;

public class Sale_InvoiceActivity extends AppCompatActivity {
    int ExistAfterSacve = 0 ;
    SqlHandler sqlHandler;
    ListView lvCustomList;
    Integer  DoPrint = 0 ;
    String CatNo = "-1";
    ArrayList<Cls_Sal_InvItems> contactList ;
    ArrayList<Cls_Offers_Groups>offer_groups_List ;
    ArrayList<Cls_Offers_Groups>offer_groups_Effict_List ;
    ArrayList<Cls_Offers_Dtl_Gifts>cls_offers_dtl_giftses ;
    public ArrayList<Cls_Offers_Hdr>cls_offers_hdrs ;
    EditText etItemNm, etPrice, etQuantity,etTax;
    Button btnsubmit;
    Boolean IsNew;
    Boolean IsChange , BalanceQtyTrans ;
    String UserID= "";
    public ProgressDialog loadingdialog;
    Double Hdr_Dis_A_Amt , Hdr_Dis_Per;
    EditText hdr_Disc;
    CheckBox chk_hdr_disc ;
    String MaxStoreQtySer = "0" ;
    Animation shake;
    private Context context=Sale_InvoiceActivity.this;
    CheckBox chk_Type,chk_showTax;
     int AllowSalInvMinus;

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
    public  void GetMaxPONo(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");
        if(Login.toString().equals("No")){
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }
        String query = "SELECT  ifnull(MAX(OrderNo), 0) +1 AS no FROM Sal_invoice_Hdr where     UserID ='" +u.toString()+"'"  ;
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";
        EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
        if (c1 != null && c1.getCount() != 0) {
                  c1.moveToFirst();
                  max = c1.getString(c1.getColumnIndex("no"));

        }

        String max1="0";
        max1 = sharedPreferences.getString("m1", "");
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
    private void  Fade_Fun (     ImageButton myButton ){
      Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
      Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);

      myButton.setAnimation(animFadeOut);
      myButton.setAnimation(animFadeIn);
      myButton.setAnimation(animFadeOut);
      myButton.setAnimation(animFadeIn);

  }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.n_view_sale__invoice);


        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

        chk_Type = (CheckBox)findViewById(R.id.chk_Type);
        chk_Type.setTypeface(MethodToUse.SetTFace(context));

        chk_showTax = (CheckBox) findViewById(R.id.chk_showTax);
        chk_showTax.setTypeface(MethodToUse.SetTFace(Sale_InvoiceActivity.this));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.setTitle(sharedPreferences.getString("CompanyNm", "") + "/" + sharedPreferences.getString("Address", ""));
        // setContentView(R.layout.activity_sale__invoice);
        shake = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

//if necessary then call:
        BalanceQtyTrans = false ;
        try {
            trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AllowSalInvMinus =Integer.parseInt( DB.GetValue(this,"ComanyInfo","AllowSalInvMinus","1=1"));

       lvCustomList = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);
       // Spinner sp_Invo_Type =(Spinner)findViewById(R.id.sp_Invo_Type);
        contactList = new ArrayList<Cls_Sal_InvItems>();
        contactList.clear();

        cls_offers_dtl_giftses = new ArrayList <Cls_Offers_Dtl_Gifts>();
        cls_offers_dtl_giftses.clear();
        cls_offers_hdrs = new ArrayList<Cls_Offers_Hdr>();
        cls_offers_hdrs.clear();

        offer_groups_List = new ArrayList<Cls_Offers_Groups>();
        offer_groups_List.clear();




        fill_Offers_Group();


        offer_groups_Effict_List = new ArrayList<Cls_Offers_Groups>();
        offer_groups_Effict_List.clear();

        IsNew = true;
         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID= sharedPreferences.getString("UserID", "");
        IsNew = true;
        IsChange = false ;

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

        chk_hdr_disc = (CheckBox)findViewById(R.id.chk_hdr_disc);
       final TextView et_Total = (TextView)findViewById(R.id.et_Total);
        hdr_Disc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (hdr_Disc.getText().toString().equals("") || hdr_Disc.getText().toString().equals("."))
                    return;
                else if (SToD(hdr_Disc.getText().toString()) > 100 && chk_hdr_disc.isChecked()) {
                    hdr_Disc.setText("100");
                    return;
                } else if ((SToD(hdr_Disc.getText().toString()) > SToD(et_Total.getText().toString())) && chk_hdr_disc.isChecked() == false) {
                    hdr_Disc.setText(et_Total.getText());
                    return;
                } else
                    Calc_Dis_Hdr();


            }
        });



        chk_hdr_disc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (hdr_Disc.getText().toString().equals("") || hdr_Disc.getText().toString().equals("."))
                    return;
                if (hdr_Disc.getText().toString().equals("") || hdr_Disc.getText().toString().equals("."))
                    return;
                else if (SToD(hdr_Disc.getText().toString()) > 100 && chk_hdr_disc.isChecked()) {
                    hdr_Disc.setText("100");
                    return;
                } else if ((SToD(hdr_Disc.getText().toString()) > SToD(et_Total.getText().toString())) && chk_hdr_disc.isChecked() == false) {
                    hdr_Disc.setText(et_Total.getText());
                    return;
                } else
                    Calc_Dis_Hdr();

            }
        });


        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        Tax_Include.setTypeface(MethodToUse.SetTFace(Sale_InvoiceActivity.this));
        Tax_Include.setChecked(true);
        Tax_Include.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CalcTotal();
                showList();


            }
        });

  /*   lvCustomList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {

             EditText Order = (EditText) findViewById(R.id.et_OrdeNo);
             Cls_Sal_InvItems contactListItems = (Cls_Sal_InvItems) arg0.getItemAtPosition(arg2);
             String slno = contactListItems.getno();
             Toast.makeText(Sale_InvoiceActivity.this, slno.toString(), Toast.LENGTH_SHORT).show();
             String delQuery = "DELETE FROM Sal_invoice_Det where itemNo = '" + slno.toString() + "' And OrderNo = '" + Order.getText().toString() + "'";
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

        Get_CatNo(accno.getText().toString());

        GetStoreQtySer();
        Bundle extras = getIntent().getExtras();
        try {
            if (extras.getString("BalanceQtyOrderNo")!="") {
                InsertBalanceQty(extras.getString("BalanceQtyOrderNo"),"");
            }
        }
            catch (Exception ex ){

            }



        ///////////////////////////////////////////////////
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        String Count = sharedPreferences.getString("InvCount", "0");
        String NumOfInvPerVisit   = DB.GetValue(Sale_InvoiceActivity.this, "ComanyInfo", "NumOfInvPerVisit  ", "1=1");

        if(  ( SToD(Count)>=SToD(NumOfInvPerVisit)) && IsNew==true && BalanceQtyTrans ==false){
            alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("فاتورة مبيعات");
            alertDialog.setMessage("يجب فتح جولة جديدة حتى تتمكن من تنفيذ هذ العملية");

            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
           // alertDialog.show();

        }
        ///////////////////////////////////////////////////////
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String RecVoucher_DocNo = DB.GetValue(Sale_InvoiceActivity.this, "RecVoucher", "DocNo", "CustAcc ='" + accno.getText() + "' AND ((TrDate)=('" + currentDateandTime + "'))");
        String PAMENT_PERIOD_NO = DB.GetValue(Sale_InvoiceActivity.this, "Customers", "PAMENT_PERIOD_NO", "no ='" + accno.getText() + "' ");

        if (RecVoucher_DocNo=="-1" && PAMENT_PERIOD_NO.equals("0")){
            AlertDialog alert_Dialog = new AlertDialog.Builder(this).create();
            alert_Dialog.setTitle("فاتورة مبيعات");
            alert_Dialog.setMessage("يجب عمل سند قبض اولاَ ، لان نوع العميل فاتورة بفاتورة");
            alert_Dialog.setIcon(R.drawable.delete);
            alert_Dialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

       }
            });
           // alert_Dialog.show();
            //return;

        }
        String CUST_PRV_MONTH = DB.GetValue(Sale_InvoiceActivity.this, "Customers", "CUST_PRV_MONTH", "no ='" + accno.getText() + "' ");

        if (  PAMENT_PERIOD_NO.equals("1") && SToD(CUST_PRV_MONTH) >0){
            AlertDialog alert_Dialog = new AlertDialog.Builder(this).create();
            alert_Dialog.setTitle("فاتورة مبيعات");
            alert_Dialog.setMessage("    نوع العميل اغلاق الذمة شهريا ، يجب تسديد القيمة :"  +"  "+ CUST_PRV_MONTH);
            alert_Dialog.setIcon(R.drawable.delete);
            alert_Dialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
           // alert_Dialog.show();
          //  return;

        }





    }
    private void Get_CatNo(String ACC_NO){
    SqlHandler sqlHandler = new SqlHandler(this);
    String q = "Select  distinct ifnull( CatNo,0) as catno from Customers where no = '"+ACC_NO+"'";
    Cursor c1 = sqlHandler.selectQuery(q);
    if (c1 != null && c1.getCount() != 0) {
        if (c1.getCount() > 0) {
            c1.moveToFirst();
            CatNo = c1.getString(c1.getColumnIndex("catno"));
        }
     c1.close();
}

}
    private void  GetStoreQtySer(){
        SqlHandler sqlHandler = new SqlHandler(this);
        String query = "SELECT  distinct COALESCE(MAX(ser), 0)   AS no from ManStore";
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
        Cls_Sal_InvItems contactListItems;
        if (chk_hdr_disc.isChecked()){
            Hdr_Dis_A_Amt =  SToD(et_Total.getText().toString()) * (Hdr_Dis_Per /100);
            for (int x = 1; x < contactList.size(); x++) {


                    contactListItems = new Cls_Sal_InvItems();
                    contactListItems = contactList.get(x);
                  //if( contactListItems.getProID().toString().equals("")) {
                    Double NewPercent = 0.0;
                    Double NewAmt = 0.0;
                    NewPercent = Hdr_Dis_Per;
                   // NewAmt = (NewPercent / 100) * Double.parseDouble(contactListItems.getTotal().replace(",", ""));
                     NewAmt = (NewPercent / 100) * (SToD(contactListItems.getprice())* SToD(contactListItems.getQty()));


                    contactListItems.setDisPerFromHdr( (NewPercent).toString());
                    contactListItems.setDisAmtFromHdr((NewAmt).toString());


              /*  }
                else {
                      contactListItems.setDisPerFromHdr("0");
                      contactListItems.setDisAmtFromHdr("0");
                  }*/
            }

        }
        else{

            for (int x = 1; x < contactList.size(); x++) {
                contactListItems = new Cls_Sal_InvItems();
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
                  // NewPercent = NewAmt / Double.parseDouble(contactListItems.getTotal().replace(",", ""));
                   if((SToD(contactListItems.getprice())* SToD(contactListItems.getQty())==0)) {
                       NewPercent = 0.0;

                   }else {
                       NewPercent = (NewAmt / (SToD(contactListItems.getprice()) * SToD(contactListItems.getQty())) * 100);
                   }
                   contactListItems.setDisPerFromHdr(SToD(NewPercent.toString()).toString());
                   contactListItems.setDisAmtFromHdr(SToD( NewAmt.toString()).toString());
               }


            }

        }
         CalcTotal();
        //    showList();

    }
    private void fill_Offers_Group() {
        String query = " select * from Offers_Groups";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Offers_Groups cls_offers_groups = new Cls_Offers_Groups();
                    cls_offers_groups.setID(c1.getString(c1
                            .getColumnIndex("ID")));
                    cls_offers_groups.setGrv_no(c1.getString(c1
                            .getColumnIndex("grv_no")));
                    cls_offers_groups.setGro_ename(c1.getString(c1
                            .getColumnIndex("gro_name")));
                    cls_offers_groups.setGro_type(c1.getString(c1
                            .getColumnIndex("gro_type")));
                    cls_offers_groups.setItem_no(c1.getString(c1
                            .getColumnIndex("item_no")));
                    cls_offers_groups.setUnit_no(c1.getString(c1
                            .getColumnIndex("unit_no")));
                    cls_offers_groups.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    cls_offers_groups.setSerNo(c1.getString(c1
                            .getColumnIndex("SerNo")));
                    offer_groups_List.add(cls_offers_groups);

                } while (c1.moveToNext());
            }
        }

    }
    private void showList() {

      lvCustomList.setAdapter(null);
      Cls_Sal_Inv_Adapter contactListAdapter = new Cls_Sal_Inv_Adapter(
              Sale_InvoiceActivity.this, contactList);
      lvCustomList.setAdapter(contactListAdapter);
      //  json = new Gson().toJson(contactList);
  }
    private void FillAdapter() {

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
        query = "  select distinct ifnull(pod.Operand,0) as Operand  ,  pod.bounce_qty,pod.dis_per , pod.dis_Amt , pod.OrgPrice , pod.tax_Amt , pod.total ,Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo  " +
               " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt    " +
                " from Sal_invoice_Det pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.OrderNo='" + Order_no.getText().toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();

                   contactListItems.setno(c1.getString(c1
                           .getColumnIndex("itemNo")));
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


                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));

                    contactListItems.setOperand(c1.getString(c1
                            .getColumnIndex("Operand")));

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
                    contactListItems.setProType("0");

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
          c1.close();
    }

 }
    public void btn_searchCustomer(View view) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");


        if(chk_Type.isChecked()){
            Bundle bundle = new Bundle();
            bundle.putString("Scr", "Sale_Inv");
            FragmentManager Manager = getFragmentManager();
            Select_Cash_Customer obj = new Select_Cash_Customer();
            obj.setArguments(bundle);
            obj.show(Manager, null);

        }else {
             if (u=="-1") {
                 Bundle bundle = new Bundle();
                 bundle.putString("Scr", "Sale_Inv");
                 FragmentManager Manager = getFragmentManager();
                 Select_Customer obj = new Select_Customer();
                 obj.setArguments(bundle);
                 obj.show(Manager, null);
             }
        }
    }
    public void Set_Cust(String No, String Nm) {
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }
    private  void CheckCeling(){
     final  String f ;
     CheckBox chk_Type = (CheckBox)findViewById(R.id.chk_Type);
     TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
     TextView acc = (TextView)findViewById(R.id.tv_acc);
    String q;
     if (!chk_Type.isChecked()) {
         q = " select distinct  ifnull(Celing,0) as Celing  from Customers where no='" + acc.getText().toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
         if (c1 != null && c1.getCount() != 0) {
             c1.moveToFirst();
             if (SToD( c1.getString(c1.getColumnIndex("Celing"))) < SToD(NetTotal.getText().toString())){
                 f= c1.getString(c1.getColumnIndex("Celing"));

              //   alertDialog.setTitle("فاتورة مبيعات");

                         final String  pass = "123" ;
                         AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sale_InvoiceActivity.this);
                         alertDialog.setTitle("رمز التحقق ");
                         alertDialog.setMessage("لقد تجاوزت سقف التسهيلات للعميل ، سقف تسهيلات العميل هو "+ f );

                         final EditText input = new EditText(Sale_InvoiceActivity.this);
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
                                          /*   Toast.makeText(getApplicationContext(),
                                                     "رمز التحقق صحيح", Toast.LENGTH_SHORT).show();*/
                                             Save_Recod_Po();

                                         } else {
                                             Toast.makeText(getApplicationContext(),
                                                     "رمز التحقق غير صحيح", Toast.LENGTH_SHORT).show();

                                         }

                                     }
                                 });

                         alertDialog.setNegativeButton("لا",
                                 new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int which) {
                                         dialog.cancel();
                                     }
                                 });

                        // alertDialog.show();
                           Save_Recod_Po();



                     }
                        else {
                 Save_Recod_Po();
                     }


             c1.close();

             }

         else {
             Save_Recod_Po();
         }


     }
     else {
         Save_Recod_Po();
     }


 }
    public void btn_save_po( final View view) {

        ImageButton imageButton3= (ImageButton)findViewById(R.id.imageButton3);
       /* if(DoPrint==0) {
            RemoveAnmation();
             imageButton3.startAnimation(shake);
        }*/

        final    TextView tv_acc = (TextView) findViewById(R.id.tv_acc);
       ///////////////////////////////////////////////////
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Count = sharedPreferences.getString("InvCount", "0");
        String NumOfInvPerVisit   = DB.GetValue(Sale_InvoiceActivity.this, "ComanyInfo", "NumOfInvPerVisit  ", "1=1");

        if(   (SToD(Count)>= SToD(NumOfInvPerVisit)) && IsNew==true && BalanceQtyTrans ==false){
            alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("فاتورة مبيعات");
            alertDialog.setMessage("يجب فتح جولة جديدة حتى تتمكن من تنفيذ هذ العملية");

            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    btn_new(view);
                }
            });
           // alertDialog.show();
            //return;
        }
        ///////////////////////////////////////////////////////
        String q = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());



    String RecVoucher_DocNo = DB.GetValue(Sale_InvoiceActivity.this, "RecVoucher", "DocNo", "CustAcc ='"+ tv_acc.getText()+"' AND ((TrDate)=('"+currentDateandTime+"'))");
    String PAMENT_PERIOD_NO = DB.GetValue(Sale_InvoiceActivity.this, "Customers", "PAMENT_PERIOD_NO", "no ='"+ tv_acc.getText()+"' ");

   if (RecVoucher_DocNo=="-1" && PAMENT_PERIOD_NO.equals("0")){
       alertDialog.setTitle("فاتورة مبيعات");
       alertDialog.setMessage("يجب عمل سند قبض اولاَ ، لان نوع العميل فاتورة بفاتورة");
       alertDialog.setIcon(R.drawable.delete);
       alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               return ;
           }
       });
       //alertDialog.show();
      // return;
   }
    //////////////////////////////////////////////////////////////////////////////////////

        final TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
         q = "SELECT distinct *  from  Sal_invoice_Hdr where   Post >0 AND   OrderNo ='" + pono.getText().toString() + "'";
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {


            alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
            alertDialog.setMessage(getResources().getText(R.string.UpdateNotAllowed));
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (ExistAfterSacve == 1 ){
                        ExistAfterSacve = 0;
                        Intent k = new Intent(Sale_InvoiceActivity.this, GalaxyMainActivity.class);
                        startActivity(k);
                    }
                     if(DoPrint==1){
                    View view = null;
                    btn_print(view);
                    }

                }
            });

            alertDialog.show();


            c1.close();
            return;
        } else {



            String Msg ="";

            final    TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
            AlertDialog.Builder alertDialogYesNo = new AlertDialog.Builder(this);


             q = "SELECT distinct *  from  Sal_invoice_Hdr where   acc  ='"+acc.getText()+"'   AND   date  ='" + currentDateandTime + "' " +
                     " And   OrderNo !='" + pono.getText().toString() + "'";

              c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() > 0) {
                 Msg =   "يوجد فاتورة لنفس هذا العميل في نفس هذا اليوم  "  + "\n\r";
                Msg = "";
                c1.close();

            }





            if (contactList.size()==0){



                alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
                alertDialog.setMessage(getResources().getText(R.string.SaveNotAllowedWithoutItem));            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (ExistAfterSacve == 1) {
                            ExistAfterSacve = 0;
                            Intent k = new Intent(Sale_InvoiceActivity.this, GalaxyMainActivity.class);
                            startActivity(k);
                        }


                    }
                });

                alertDialog.show();

                return;

            }




            alertDialogYesNo.setTitle(getResources().getText(R.string.Sale_invoice));
            alertDialogYesNo.setMessage(getResources().getText(R.string.DoYouWantToContinSave));

            // Setting Icon to Dialog
            alertDialogYesNo.setIcon(R.drawable.save);

            alertDialogYesNo.setNegativeButton(getResources().getText(R.string.No), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (ExistAfterSacve == 1 ){
                        ExistAfterSacve = 0;
                        Intent k = new Intent(Sale_InvoiceActivity.this, GalaxyMainActivity.class);
                        startActivity(k);
                    }
                    if(DoPrint==1){
                    View view = null;
                    btn_print(view);
                    }

                    return;
                }
            });

            alertDialogYesNo.setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
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
                    CheckCeling();
                }
            });


            alertDialogYesNo.show();


        }

    }
    public void  Save_Recod_Po(){

        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        Tax_Include.setTypeface(MethodToUse.SetTFace(Sale_InvoiceActivity.this));
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
        cv.put("Total",SToD(Total.getText().toString()));
        cv.put("Net_Total", SToD(NetTotal.getText().toString()));
        cv.put("Tax_Total", SToD(TotalTax.getText().toString()));
        cv.put("Post", "-1");
        cv.put("QtyStoreSer", MaxStoreQtySer);
        cv.put("Nm",custNm.getText().toString());
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
            i = sqlHandler.Insert("Sal_invoice_Hdr", null, cv);
        }
        else {
            i = sqlHandler.Update("Sal_invoice_Hdr", cv, "OrderNo ='"+pono.getText().toString()+"'");
        }




        if(i>0){
           String q ="Delete from  Sal_invoice_Det where OrderNo ='"+ pono.getText().toString()+"'";
            sqlHandler.executeQuery(q);

            for (int x = 1; x < contactList.size(); x++) {
                Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
                contactListItems = contactList.get(x);


                cv = new ContentValues();
                cv.put("OrderNo", pono.getText().toString());
                cv.put("itemno",contactListItems.getNo());
                cv.put("price", contactListItems.getPrice().toString());
                cv.put("qty", SToD(contactListItems.getQty().toString()));
                cv.put("tax", SToD(contactListItems.getTax().toString()));
                cv.put("unitNo", contactListItems.getUnite().toString());
                cv.put("dis_per",  SToD(contactListItems.getDiscount().toString()));
                cv.put("dis_Amt",  SToD(contactListItems.getDis_Amt().toString()));
                cv.put("bounce_qty", contactListItems.getBounce().toString());
                cv.put("tax_Amt",  SToD(contactListItems.getTax_Amt().toString()));
                cv.put("total",  SToD(contactListItems.getTotal().toString()));
                cv.put("ProID", contactListItems.getProID().toString());
                cv.put("Pro_bounce", contactListItems.getPro_bounce().toString());
                cv.put("Pro_dis_Per", SToD(contactListItems.getPro_dis_Per().toString()));
                cv.put("Pro_amt", SToD(contactListItems.getPro_amt().toString()));
                cv.put("pro_Total",  SToD(contactListItems.getPro_Total().toString()));
                cv.put("OrgPrice", SToD(contactListItems.getItemOrgPrice().toString()));

                //ProID
               //  text null , Pro_bounce  Text Null , Pro_dis_Per Text Null , Pro_amt Text null , pro_Total text null    ) ");


                i = sqlHandler.Insert("Sal_invoice_Det", null, cv);
            }
        }



      /*  query ="INSERT INTO Sal_invoice_Hdr (OrderNo,Nm,acc,date) values ('"+ pono.getText().toString()+"','"+custNm.getText().toString()+"','"+acc.getText().toString()+"','"+currentDateandTime+"')";
        sqlHandler.executeQuery(query);*/



        if (i> 0 ) {
           // GetMaxPONo();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
            alertDialog.setMessage(getResources().getText(R.string.AddCompleteSucc));

            SharedPreferences.Editor editor    = sharedPreferences.edit();
            String Count = sharedPreferences.getString("InvCount", "0");
            Count = (SToD(Count)+1) + "";
            editor.putString("InvCount",Count);
            editor.commit();
            IsChange = false;

          /*  contactList.clear();
            showList();
            custNm.setText("");
            acc.setText("");
            Total.setText("");
            dis.setText("");
            NetTotal.setText("");
            TotalTax.setText("");*/

            chk_Type.setEnabled(false);

            IsNew = false;
            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (ExistAfterSacve == 1) {
                        Intent k = new Intent(Sale_InvoiceActivity.this, GalaxyMainActivity.class);
                        startActivity(k);
                    }
                   // if(DoPrint==1){
                        View view = null;
                        btn_print(view);
                    //}


                }
            });


            alertDialog.show();
            UpDateMaxOrderNo();

        }

    }
    private  void  UpDateMaxOrderNo() {

    String query = "SELECT  ifnull(MAX (OrderNo), 0)AS no FROM Sal_invoice_Hdr";
    Cursor c1 = sqlHandler.selectQuery(query);
    String max="0";

    if (c1 != null && c1.getCount() != 0) {
        c1.moveToFirst();
        max = c1.getString(c1.getColumnIndex("no"));
    }

    max=(intToString(Integer.valueOf(max), 7)  );

    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("m1",max);
    editor.commit();
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


        Cls_Sal_InvItems contactListItems ;
        for (int x =1; x < contactList.size(); x++) {
            contactListItems = new Cls_Sal_InvItems();
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
        Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;
        Double All_Dis= 0.0;
        Double All_Dis_Per= 0.0;
        Total = 0.0;
        Tax_Total = 0.0;
        Dis_Amt = 0.0;
        Po_Total = 0.0;
        MyTextView_Digital Subtotal = (MyTextView_Digital)findViewById(R.id.et_Total);
        MyTextView_Digital TotalTax = (MyTextView_Digital)findViewById(R.id.et_TotalTax);
        MyTextView_Digital dis = (MyTextView_Digital)findViewById(R.id.et_dis);
        MyTextView_Digital NetTotal = (MyTextView_Digital)findViewById(R.id.tv_NetTotal);
        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        Double TaxVal = 0.0;
        double TaxFactor = 0.0 ;
        CalcTax();
        Double RowTotal = 0.0 ;
        Double pq = 0.0;
        Double opq = 0.0;
        Double V_NetTotal = 0.0;
        for (int x = 1; x < contactList.size(); x++) {
            contactListItems = new Cls_Sal_InvItems();
            contactListItems = contactList.get(x);
            All_Dis = SToD(contactListItems.getDis_Amt()) + SToD(contactListItems.getDisAmtFromHdr()) + SToD(contactListItems.getPro_amt());
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());
            pq = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
            opq = SToD(contactListItems.getItemOrgPrice()) * SToD(contactListItems.getQty());

            Tax_Total = Tax_Total + (SToD(contactListItems.getTax_Amt().toString()));
            Dis_Amt = Dis_Amt + (((pq) * (All_Dis_Per / 100)));

            if (Tax_Include.isChecked()) {
                RowTotal =opq  -  ((opq )*( All_Dis_Per/100)) ;//+ SToD(contactListItems.getTax_Amt());
               /* if( All_Dis_Per > 0) {
                    Total = Total + ((opq * (All_Dis_Per / 100)) - SToD(contactListItems.getTax_Amt()) + Dis_Amt);
                }else{
                    Total = Total + ((opq ) - SToD(contactListItems.getTax_Amt()) );

                }*/


            } else{
                RowTotal =pq  -  ((pq )*( All_Dis_Per/100)) + SToD(contactListItems.getTax_Amt());
                Total = Total + pq;

            }

            V_NetTotal = V_NetTotal + SToD(RowTotal.toString().replace(",", ""));

            contactListItems.setTotal((SToD(RowTotal.toString())).toString().replace(",", ""));
            All_Dis = 0.0;

        }
        Total =  V_NetTotal - Tax_Total + Dis_Amt;
        TotalTax.setText(String.valueOf(df.format(Tax_Total)).replace(",", ""));
        Subtotal.setText(String.valueOf(df.format(Total)).replace(",", ""));
        dis.setText(String.valueOf(df.format(Dis_Amt )).replace(",",""));


       /* if (Tax_Include.isChecked()){
            Po_Total = Po_Total + ((Double.parseDouble(Subtotal.getText().toString().replace(",", "")) - Double.parseDouble(dis.getText().toString().replace(",", "")) ) + Double.parseDouble(TotalTax.getText().toString().replace(",", ""))    );
        }
        else{
            Po_Total = Po_Total + ((Double.parseDouble(Subtotal.getText().toString().replace(",", "")) - Double.parseDouble(dis.getText().toString().replace(",","")) )   + Double.parseDouble(TotalTax.getText().toString().replace(",", "")) );
        }*/
        Po_Total = Po_Total + ((SToD(Subtotal.getText().toString()) - SToD(dis.getText().toString()) ) + SToD(TotalTax.getText().toString())    );

        showList();
        NetTotal.setText(String.valueOf(df.format(V_NetTotal)));
    }
    public void btn_show_Pop(View view) {
        showPop();
    }
    public  void showPop()    {
        EditText Order=(EditText)findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView)findViewById(R.id.tv_acc);
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Sal_inv");
        bundle.putString("CatNo", CatNo);
        bundle.putString("OrderNo", Order.getText().toString());
        bundle.putString("CustomerNo", accno.getText().toString());


        FragmentManager Manager =  getFragmentManager();
        PopSal_Inv_Select_Items obj = new PopSal_Inv_Select_Items();
        obj.setArguments(bundle);
        obj.show(Manager, null);
        IsChange = true ;
     }
    private int checkStoreQty(String ItemNo , String UnitNo , String qty , String bounce){


        int  q = 1 ;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");

        sqlHandler = new SqlHandler(this);
        String query = "SELECT distinct  ifnull(Operand,1)  as  Operand  from UnitItems where  item_no ='"+ItemNo  + "'  and  unitno ='"+UnitNo +"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String  Operand ="0";
        Double Order_qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                Operand =c1.getString(c1.getColumnIndex("Operand"));
            }
        }
        c1.close();
        Order_qty = Double.parseDouble(Operand) * (Double.parseDouble(qty) +SToD(bounce) );


          query = "SELECT  distinct  ifnull( qty,0)   as  qty   from ManStore where  itemno = '"+ItemNo  + "'  ";
          c1 = sqlHandler.selectQuery(query);

        Double Store_qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                Store_qty =   Double.parseDouble(c1.getString(c1.getColumnIndex("qty")));
            }
        }
        c1.close();
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
       query = "SELECT    distinct    ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( ui.Operand,1))) ,0)  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                "    where  sih.Post = -1 and ui.item_no ='"+ItemNo+"'  " +
               "    And sid.OrderNo !='"+ pono.getText().toString()+"'  and sih.UserID ='" +u.toString()+"'"  ;

         //   "    where  QtyStoreSer = "+MaxStoreQtySer+" and ui.item_no ='"+ItemNo+"'";

        c1 = sqlHandler.selectQuery(query);

        Double Sal_Qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                Sal_Qty =   Double.parseDouble(  (c1.getString(c1.getColumnIndex("Sal_Qty"))).toString());
            }
        }
        c1.close();

        if( (Store_qty - Sal_Qty -  Order_qty)>=0 )
        {
               q = 1 ;
        }
        else {
            q =-1 ;
        }
       // q = 1;
        return q;
    }
    public void Save_List(String ItemNo , String p , String q ,String t,String u ,String dis ,String bounce,String ItemNm,String UnitName,String dis_Amt,String Operand) {

        if (bounce.toString().equals(""))
            bounce = "0";

        if (dis.toString().equals(""))
            dis = "0";

        if (dis_Amt.toString().equals(""))
            dis_Amt = "0";

        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;

        for (int x = 1; x < contactList.size(); x++) {
            Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
            contactListItems = contactList.get(x);
            if ( contactListItems.getNo().equals(ItemNo) && contactListItems.getProType()!="3" ) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
                alertDialog.setMessage(getResources().getText(R.string.ItemExists));            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();
                return;
            }

        }
        AddEmptyRow();
      /*  if(AllowSalInvMinus!=1) {
            if (checkStoreQty(ItemNo, u, q, bounce) < 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
                alertDialog.setMessage( getResources().getText(R.string.QtyNotAvalable));            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();
                return;
            }
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

        Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        if(Tax_Include.isChecked()) {
            contactListItems.setprice(   String.valueOf( Price/ ((Tax / 100) + 1)));
        }
        else
        {
            contactListItems.setprice( String.valueOf(Price ));

        }
        //contactListItems.setprice(String.valueOf(Price));
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
        contactListItems.setProType("0");
        contactListItems.setOperand(Operand);
        contactListItems.setTotal(String.valueOf(df.format(Item_Total)));
        contactList.add(contactListItems);
     //    Gf_Calc_Promotion();
         CalcTotal();
         showList();
         Calc_Dis_Hdr();





    }
    private  void AddEmptyRow(){

        if(contactList.size()==0){
            Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
            contactListItems.setno("");
            contactListItems.setName("");
            contactListItems.setprice("");
            contactListItems.setItemOrgPrice("");
            contactListItems.setQty("");
            contactListItems.setTax("");
            contactListItems.setUniteNm("");
            contactListItems.setBounce("");
            contactListItems.setDiscount("");
            contactListItems.setDis_Amt("");
            contactListItems.setDis_Amt("");
            contactListItems.setUnite("");
            contactListItems.setTax_Amt("");
            contactListItems.setTotal("");
            contactListItems.setProID("");
            contactListItems.setPro_bounce("0");
            contactListItems.setPro_dis_Per("0");
            contactListItems.setPro_amt("0");
            contactListItems.setPro_Total("0");
            contactListItems.setDisAmtFromHdr("0");
            contactListItems.setDisPerFromHdr("0");
            contactList.add(contactListItems);
        }
    }
    public void Update_List(String ItemNo , String p , String q ,String t,String u ,String dis ,String bounce,String ItemNm,String UnitName,String dis_Amt,String Operand) {

        if (bounce.toString().equals(""))
            bounce = "0";

        if (dis.toString().equals(""))
            dis = "0";

        if (dis_Amt.toString().equals(""))
            dis_Amt = "0";
        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;
        if ( checkStoreQty(ItemNo ,u  , q , bounce) < 0 )  {
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
        }

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

        Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
        contactListItems = contactList.get(position);
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        if(Tax_Include.isChecked()) {
            contactListItems.setprice(   String.valueOf( Price/ ((Tax / 100) + 1)));
        }
        else
        {
            contactListItems.setprice( String.valueOf(Price ));

        }
        //contactListItems.setprice(String.valueOf(Price));
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
        contactListItems.setOperand(Operand);
        contactListItems.setProType("0");
        contactListItems.setTotal(String.valueOf(df.format(Item_Total)));


        CalcTotal();
        showList();
        Calc_Dis_Hdr();

        Gf_Calc_Promotion();
    }
    public void Save_Method(String m , String p , String q ,String t ,String u    ) {
        EditText Order=(EditText)findViewById(R.id.et_OrdeNo);
        String query =  "INSERT INTO Sal_invoice_Det(OrderNo,itemNo,unitNo,price,qty,tax) values ("+ Order.getText().toString()+",'" + m +"','"+ u +"','" + p + "','" + q + "','" + t + "')";
        sqlHandler.executeQuery(query);
      showList();
    }
    public void btn_delete(View view) {
//      RemoveAnmation();
//      ImageButton imageButton6= (ImageButton)findViewById(R.id.imageButton6);
     // imageButton6.startAnimation(shake);
      TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
      ////////////////////////////////////////////////////////////////////
      AlertDialog alertDialog = new AlertDialog.Builder(
              this).create();
      alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
      alertDialog.setMessage(getResources().getText(R.string.DeleteNotAllowed));
      alertDialog.setIcon(R.drawable.tick);
      alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
              return;

          }
      });

      alertDialog.show();




      ///////////////////////////////////////////////////////////////////////
     /* String q = "SELECT *  from  Sal_invoice_Hdr where   Post >0 AND   OrderNo ='" + pono.getText().toString() + "'";

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
      }*/

  }
    public void Delete_Record_PO(){

        TextView OrdeNo = (TextView)findViewById(R.id.et_OrdeNo);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());




        String query ="Delete from  Sal_invoice_Hdr where OrderNo ='"+ OrdeNo.getText().toString()+"'";
        sqlHandler.executeQuery(query);


        query ="Delete from  Sal_invoice_Det where OrderNo ='"+ OrdeNo.getText().toString()+"'";
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
        alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getText(R.string.AddCompleteSucc));

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

        ExistAfterSacve = 0 ;
        CheckBox chk_Type = (CheckBox)findViewById(R.id.chk_Type);
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Sal_inv");
        if(chk_Type.isChecked()){
            bundle.putString("typ", "0");
        }
        else
        {
        bundle.putString("typ", "-1");
        }
        FragmentManager Manager =  getFragmentManager();
        Sal_Inv_SearchActivity obj = new Sal_Inv_SearchActivity();
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
        contactList.clear();
        AddEmptyRow();
        FillAdapter();
        showList();
       String q = "Select  distinct ifnull( s.hdr_dis_value,0) as hdr_dis_value ,s.Nm ,  ifnull(s.hdr_dis_per,-1) as hdr_dis_per ,  s.inovice_type   ,s.include_Tax ,  s.disc_Total, s.OrderNo,s.Net_Total,s.Tax_Total ,s.acc ,s.date , c.name  from  Sal_invoice_Hdr s left join Customers c on c.no =s.acc   where s.OrderNo = '"+No+"'";

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
                try {
                    if (c1.getString(c1.getColumnIndex("inovice_type")).equals("0")) {

                        CustNm.setText(c1.getString(c1.getColumnIndex("Nm")).toString());

                    } else {
                        CustNm.setText(c1.getString(c1.getColumnIndex("name")).toString());

                    }
                }catch (Exception ex){

                    Toast.makeText(this,getResources().getText(R.string.ConnectError ),Toast.LENGTH_SHORT).show();
                }

                accno.setText(c1.getString(c1.getColumnIndex("acc")));
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
                chk_Type.setEnabled(false);
            }
            c1.close();
        }


      IsChange = false;
      IsNew = false;
    }
    public void RemoveAnmation() {
        ImageButton imageButton8= (ImageButton)findViewById(R.id.imageButton8);
        ImageButton imageButton3= (ImageButton)findViewById(R.id.imageButton3);
        ImageButton imageButton6= (ImageButton)findViewById(R.id.imageButton6);
        ImageButton imageButton2= (ImageButton)findViewById(R.id.imageButton2);
        ImageButton imageButton7= (ImageButton)findViewById(R.id.imageButton7);
        ImageButton imageButton9= (ImageButton)findViewById(R.id.imageButton9);



        imageButton8.clearAnimation();
        imageButton3.clearAnimation();
        imageButton6.clearAnimation();
        imageButton2.clearAnimation();
        imageButton7.clearAnimation();
        imageButton9.clearAnimation();

    }
    public void btn_print(View view) {

       /*// RemoveAnmation();
        ImageButton imageButton2= (ImageButton)findViewById(R.id.imageButton2);
       // imageButton2.startAnimation(shake);

        DoPrint = 1 ;
        if ( IsChange == true) {
            btn_save_po(view);
        }
else {
            Intent k = new Intent(this, Convert_Sal_Invoice_To_ImgActivity.class);
            // Intent k = new Intent(this,BluetoothConnectMenu.class);
            TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
            TextView OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);
            TextView accno = (TextView) findViewById(R.id.tv_acc);

            k.putExtra("OrderNo", OrdeNo.getText().toString());
            if (chk_showTax.isChecked()) {
                k.putExtra("ShowTax", "1");
            } else {
                k.putExtra("ShowTax", "0");
            }
            DoPrint = 0;
            startActivity(k);
            btn_new(view);
        }*/

    }
    public void btn_new(View view) {
       // RemoveAnmation();
//        ImageButton imageButton8= (ImageButton)findViewById(R.id.imageButton8);

       // imageButton8.startAnimation(shake);

        //Fade_Fun(imageButton8);
        ExistAfterSacve=0;
        GetMaxPONo();
        showList();
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView accno = (TextView)findViewById(R.id.tv_acc);
        EditText et_hdr_Disc = (EditText)findViewById(R.id.et_hdr_Disc);
        et_hdr_Disc.setText("0");
        CustNm.setText("");
        accno.setText("");
        CheckBox chk_hdr_disc = (CheckBox)findViewById(R.id.chk_hdr_disc);
        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        CheckBox chk_Type = (CheckBox)findViewById(R.id.chk_Type);
        chk_Type.setTypeface(MethodToUse.SetTFace(Sale_InvoiceActivity.this));
        IsNew = true;
        Tax_Include.setChecked(true);
        chk_Type.setChecked(false);
        chk_Type.setEnabled(true);
        chk_hdr_disc.setChecked(false);
        BalanceQtyTrans=false ;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));

    }
    public void btn_back(View view) {
     //   RemoveAnmation();
       // ImageButton imageButton7= (ImageButton)findViewById(R.id.imageButton7);
       // imageButton7.startAnimation(shake);
        ExistAfterSacve = 1 ;
        if (contactList.size()>0  && IsChange == true) {
            btn_save_po(view);
        }else {
            Intent k = new Intent(this, GalaxyMainActivity.class);
            startActivity(k);
        }


    }
    int position ;
    public void btn_Delete_Item( final View view) {
        position = lvCustomList.getPositionForView(view);
        registerForContextMenu(view);
        openContextMenu(view);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
       // Cls_Sal_InvItems contactListItems =new Cls_Sal_InvItems();



        menu.setHeaderTitle(contactList.get(position).getName());
        menu.add(Menu.NONE, 1, Menu.NONE, getResources().getText(R.string.Update));
        menu.add(Menu.NONE, 2, Menu.NONE, getResources().getText(R.string.Delete));
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        // TODO Auto-generated method stub
        switch(item.getItemId())
        {
            case 1:
            {

                 ArrayList<Cls_Sal_InvItems> Itemlist = new ArrayList <Cls_Sal_InvItems> ();

                Itemlist.add(contactList.get(position))  ;
                if (contactList.get(position).getProType().equals("3")) {
                    break;
                }


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
                    obj.show(Manager, null);

            }
            break;
            case 2:
            {
                if (contactList.get(position).getProType().equals("3")) {
                    break;
                }
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
                alertDialog.setMessage(getResources().getText(R.string.DoYouWantContinDelete));
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       contactList.remove(position);
                        ResetPromotion();
                       Gf_Calc_Promotion();
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
    private void fill_Offers_Group_Effict(String GroupNo) {

        offer_groups_Effict_List.clear();
        String query = " select distinct og.ID  , og.grv_no,   og.gro_name,  og.gro_type,  og.item_no,  og.unit_no, og.qty,  og.SerNo" +
                " from Offers_Groups og inner join Offers_Dtl_Cond on Gro_Num = og.grv_no where Trans_ID ='"+GroupNo+"'";
        Cursor c1 = sqlHandler.selectQuery(query);

        int f = 1 ;

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {

                do {
                    Cls_Offers_Groups cls_offers_groups = new Cls_Offers_Groups();

                    ////////////////////////////////////////////////
                    Cls_Sal_InvItems Inv_Obj ;

                    for (int j = 1; j < contactList.size(); j++) {
                        Inv_Obj = new Cls_Sal_InvItems();
                        Inv_Obj = contactList.get(j);

                            if (Inv_Obj.getno().equals(c1.getString(c1.getColumnIndex("item_no")))  && c1.getString(c1.getColumnIndex("gro_type")).equals("1") ) {

                                f=Integer.parseInt( Inv_Obj.getQty()) /  Integer.parseInt(c1.getString(c1.getColumnIndex("qty"))) ;

                        }
                        //else

                    }
                    ///////////////////////////////////////////////////

                    cls_offers_groups.setID(c1.getString(c1
                            .getColumnIndex("ID")));
                    cls_offers_groups.setGrv_no(c1.getString(c1
                            .getColumnIndex("grv_no")));
                    cls_offers_groups.setGro_ename(c1.getString(c1
                            .getColumnIndex("gro_name")));
                    cls_offers_groups.setGro_type(c1.getString(c1
                            .getColumnIndex("gro_type")));
                    cls_offers_groups.setItem_no(c1.getString(c1
                            .getColumnIndex("item_no")));
                    cls_offers_groups.setUnit_no(c1.getString(c1
                            .getColumnIndex("unit_no")));
                    cls_offers_groups.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    cls_offers_groups.setSerNo(c1.getString(c1
                            .getColumnIndex("SerNo")));
                    cls_offers_groups.setFactor(f +"");
                    offer_groups_Effict_List.add(cls_offers_groups);

                } while (c1.moveToNext());
            }
        }

    }
    private int checkItemInGroup(String ItemNo , String Group ){
        sqlHandler = new SqlHandler(this);
    String q = " select distinct * from Offers_Dtl_Cond odc  inner join Offers_Groups ogs on ogs.grv_no= odc.Gro_Num  " +
            "  where odc.Trans_ID ='"+ Group +  "' and ogs.item_no = '"+ItemNo+"'";
    Cursor c1 = sqlHandler.selectQuery(q)  ;
       if (c1 !=null && c1.getCount()>0)
        {
            return  1 ;
        }

        return  0;
    }
    private void InsertItemsOffersType3(String GroupNo , int f) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        Cls_Sal_InvItems Inv_Obj ;
        cls_offers_dtl_giftses.clear();

        for (int j = 1; j < contactList.size(); j++) {
            Inv_Obj = new Cls_Sal_InvItems();
            Inv_Obj = contactList.get(j);

            if(checkItemInGroup(Inv_Obj.getNo(),GroupNo)==1){
                Inv_Obj.setProID(GroupNo);

            }

        }
        String query = " Select distinct u.UnitName , odc.Allaw_Repet,   i.Item_Name ,  odg.Item_No , odg.Unit_No , odg.QTY , odg.Unit_Rate from Offers_Dtl_Gifts  odg" +
                " Left join invf i on i.Item_No =  odg.Item_No   " +
                "  left join Unites  u on u.Unitno = odg.Unit_No" +
                "   left join  Offers_Dtl_Cond odc on odc.Trans_ID =  odg.Trans_ID"+
                " where odg.Trans_ID = '"+GroupNo+"'"; //"  where odc.Gro_Num  = '"+GroupNo+"'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {


                   if ( c1.getString(c1.getColumnIndex("Allaw_Repet")).equals("0"))
                   {
                       f =1;
                   }


                    String msg = " لا يمكن تطبيق العرض رقم "+GroupNo  +  " الخاص بالمادة رقم "+ c1.getString(c1.getColumnIndex("Item_No")) +"وذلك بسبب عدم توافر كمية";
                    if ( checkStoreQty(c1.getString(c1.getColumnIndex("Item_No")) ,c1.getString(c1.getColumnIndex("Unit_No"))  ,"" +(f * Double.parseDouble(c1.getString(c1.getColumnIndex("QTY")))),"0") < 0 )  {

                        alertDialog.setTitle("عروض العميل");
                        alertDialog.setMessage(msg);            // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.tick);
                        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alertDialog.show();

                    }

             else {
                        Inv_Obj = new Cls_Sal_InvItems();
                        Inv_Obj.setno(c1.getString(c1.getColumnIndex("Item_No")));
                        Inv_Obj.setName(c1.getString(c1.getColumnIndex("Item_Name")));
                        Inv_Obj.setprice("0");
                        Inv_Obj.setItemOrgPrice("0");
                        Inv_Obj.setQty("0");
                        Inv_Obj.setTax("0");
                        Inv_Obj.setUnite(c1.getString(c1.getColumnIndex("Unit_No")));
                        Inv_Obj.setOperand(c1.getString(c1.getColumnIndex("Unit_Rate")));
                        Inv_Obj.setPro_bounce(String.valueOf(f * Double.parseDouble(c1.getString(c1.getColumnIndex("QTY")))));
                        Inv_Obj.setDiscount("0");
                        Inv_Obj.setProID(GroupNo);
                        Inv_Obj.setDis_Amt("0");
                        Inv_Obj.setUniteNm(c1.getString(c1.getColumnIndex("UnitName")));
                        Inv_Obj.setPro_amt("0");
                        Inv_Obj.setPro_dis_Per("0");
                        Inv_Obj.setBounce("0");
                        Inv_Obj.setPro_Total("0");
                        Inv_Obj.setTax_Amt("0");
                        Inv_Obj.setTotal("0");
                        Inv_Obj.setDisPerFromHdr("0");
                        Inv_Obj.setDisAmtFromHdr("0");
                        Inv_Obj.setProType("3");
                        contactList.add(Inv_Obj);
                        CalcTotal();
                        showList();
                    }

                } while (c1.moveToNext());

            }
        }

    }
    private  int CalcFactor( String g){
        int f = 1;

        return  f ;
    }
    public  void Apply_Promotion() {

        Double Rowtotal = 0.0;
        int x;
        Cls_Sal_InvItems Inv_Obj;
        Cls_Offers_Groups Effict_List;
        int min = 0 ;
        for (x = 0; x < cls_offers_hdrs.size(); x++) { // Fill Promtion With Item Promotions

            fill_Offers_Group_Effict(cls_offers_hdrs.get(x).getID());


            if (offer_groups_Effict_List.size() >0){

                min =Integer.valueOf( offer_groups_Effict_List.get(0).getFactor());
               for(int m=0; m<offer_groups_Effict_List.size(); m++){
                if( Integer.valueOf(  offer_groups_Effict_List.get(m).getFactor() )< min){
                    min =Integer.valueOf( offer_groups_Effict_List.get(m).getFactor());
                }
            }

        }
            else
            {
                min=1;
            }

            if(     cls_offers_hdrs.get(x).getOffer_Result_Type().toString().equals("3")){
                InsertItemsOffersType3(cls_offers_hdrs.get(x).getID(), min);
            }

            int  f = 0 ;
             ////////////////////////////////////////////////////////////////
            String query = " select distinct Offers_Dtl_Cond.Allaw_Repet  from Offers_Dtl_Cond " +
                    " where Offers_Dtl_Cond.Trans_ID = '"+cls_offers_hdrs.get(x).getID()+"'";
            Cursor c1 = sqlHandler.selectQuery(query);
            if (c1 != null && c1.getCount() != 0) {

                if (c1.moveToFirst()) {
                    if (c1.getString(c1.getColumnIndex("Allaw_Repet")).equals("0")) {
                        min = 1;
                    }
                }
            }



        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

          for (int j = 1; j < contactList.size(); j++) {
                Inv_Obj = new Cls_Sal_InvItems();
                Inv_Obj = contactList.get(j);
                for (int i = 0; i < offer_groups_Effict_List.size(); i++) {
                    Effict_List = new Cls_Offers_Groups();
                    Effict_List = offer_groups_Effict_List.get(i);

                    if (Inv_Obj.getno().equals(Effict_List.getItem_no())) {
                        //fill FACTOR
                   if(     cls_offers_hdrs.get(x).getOffer_Result_Type().toString().equals("1")){ // per
                       if ( Inv_Obj.getProID().toString().equals("")) {
                           Inv_Obj.setProID(cls_offers_hdrs.get(x).getID());
                           Inv_Obj.setPro_dis_Per(cls_offers_hdrs.get(x).getOffer_Dis().toString());
                           Rowtotal =  Double.parseDouble(Inv_Obj.getQty()) * Double.parseDouble(Inv_Obj.getprice());
                           Inv_Obj.setPro_amt(String.valueOf(Rowtotal * ((min *Double.parseDouble(Inv_Obj.getPro_dis_Per()) / 100))));
                           Inv_Obj.setPro_dis_Per(String.valueOf(( min *(Double.parseDouble(cls_offers_hdrs.get(x).getOffer_Dis())))));



                           Calc_Dis_Hdr();


                       }
                   }


                      else  if(     cls_offers_hdrs.get(x).getOffer_Result_Type().toString().equals("2")){ // Amt
                            if ( Inv_Obj.getProID().toString().equals("")) {
                                // Set Factor

                                Inv_Obj.setProID(cls_offers_hdrs.get(x).getID());
                                Inv_Obj.setPro_amt(String.valueOf(min  * Double.parseDouble(cls_offers_hdrs.get(x).getOffer_Amt().toString())));
                                Rowtotal =  Double.parseDouble(Inv_Obj.getQty()) * Double.parseDouble(Inv_Obj.getprice());
                                String  per = String.valueOf(100*( min *( Double.parseDouble(cls_offers_hdrs.get(x).getOffer_Amt().toString())) / Rowtotal));
                                Inv_Obj.setPro_dis_Per(String.valueOf(SToD(per)));

                                Calc_Dis_Hdr();


                            }

                        }


                    }
                }

            }

        }

    }
    private  void   Calc_LowPriPro(){
       int f = 1;
        sqlHandler = new SqlHandler(this);
        final   TextView et_Total = (TextView)findViewById(R.id.et_Total);
        final   TextView et_dis = (TextView)findViewById(R.id.et_dis);
        int f1 = 0 ;
        int  f2 = 0 ;

        String q = "Select distinct Offer_Dis from Offers_Hdr where  Offer_Type = 1 and   TotalValue = 50  ";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                f2 = 1;

            }
        }
            c1.close();


              q = "Select distinct Offer_Dis from Offers_Hdr where  Offer_Type = 1 and   TotalValue = 100  ";
              c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    f1 = 1;

                }
            }
                c1.close();

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getResources().getText(R.string.promotion));
        alertDialog.setIcon(R.drawable.tick);

        Double Re=0.0;


        Cls_Sal_InvItems Inv_Obj;
                for(int i = 0 ; i<contactList.size();i++){
                                Inv_Obj = new Cls_Sal_InvItems();
                                Inv_Obj = contactList.get(i);
                                     if(Inv_Obj.getProID().equals("")){
                                            Re = Re + SToD(Inv_Obj.getTax_Amt()) + (SToD(Inv_Obj.getQty()) *  SToD(Inv_Obj.getprice()) )   ;
                                        }

        }

        if (  Re>=100 && f1==1){
            alertDialog.setMessage("لقد حصلت على خصم 2 %");
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Cls_Sal_InvItems Inv_Obj;
                    Double Rowtotal;
                    for (int j = 1; j < contactList.size(); j++) {
                        Inv_Obj = new Cls_Sal_InvItems();
                        Inv_Obj = contactList.get(j);
                        if (Inv_Obj.getProID().toString().equals("")) {

                            if (Inv_Obj.getProID().equals("")) {
                                Inv_Obj.setProID("-99");
                                Rowtotal = Double.parseDouble(Inv_Obj.getQty()) * Double.parseDouble(Inv_Obj.getprice());
                                Inv_Obj.setPro_dis_Per("2" + "");
                                Inv_Obj.setPro_amt(String.valueOf(Rowtotal * (0.02)));

                            /*    Inv_Obj.setDisAmtFromHdr("0");
                                Inv_Obj.setDisPerFromHdr("0");
                                Inv_Obj.setDis_Amt("0");
                                Inv_Obj.setDiscount("0");*/
                                Calc_Dis_Hdr();
                                CalcTotal();
                                showList();

                            }
                        }

                    }

            }
        });


            alertDialog.show();

        }

        else if (  Re>=50 && f2==1){
            alertDialog.setMessage("لقد حصلت على خصم 1 %");
            f =1;
             alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                     Cls_Sal_InvItems Inv_Obj;
                     Double Rowtotal;
                     for (int j = 1; j < contactList.size(); j++) {
                         Inv_Obj = new Cls_Sal_InvItems();
                         Inv_Obj = contactList.get(j);


                         if (Inv_Obj.getProID().toString().equals("")) {
                             Inv_Obj.setProID("-99");
                             Rowtotal = Double.parseDouble(Inv_Obj.getQty()) * Double.parseDouble(Inv_Obj.getprice());
                                 Inv_Obj.setPro_dis_Per("1" + "");
                                 Inv_Obj.setPro_amt(String.valueOf(Rowtotal * (0.01)));


                            /*    Inv_Obj.setDisAmtFromHdr("0");
                                Inv_Obj.setDisPerFromHdr("0");
                                Inv_Obj.setDis_Amt("0");
                                Inv_Obj.setDiscount("0");*/
                             Calc_Dis_Hdr();

                             CalcTotal();
                             showList();
                     }

                     }

                 }
             });
            alertDialog.show();

        }



    }
    private  void  ResetPromotion(){
        cls_offers_hdrs.clear();
        Cls_Sal_InvItems Inv_Obj;
        for (int j = 1; j < contactList.size(); j++) {
            Inv_Obj = new Cls_Sal_InvItems();
            Inv_Obj = contactList.get(j);
            Inv_Obj.setProID("");
            Inv_Obj.setPro_dis_Per("0");
            Inv_Obj.setPro_amt("0");
            Inv_Obj.setPro_Total("0");
            Inv_Obj.setPro_bounce("0");

            if(Inv_Obj.getProType().equals("3") ){
                contactList.remove(Inv_Obj);
                j = 0;
            }


        }
        CalcTotal();
        showList();
    }
    private void Gf_Calc_Promotion(){
    ResetPromotion();

    //cls_offers_hdrs.clear();
    Cls_Offers_Groups Offer_Group ;
    Cls_Sal_InvItems Inv_Obj ;
    for (int x = 0; x < offer_groups_List.size(); x++) {
    Offer_Group = new Cls_Offers_Groups();
    Offer_Group = offer_groups_List.get(x);

    if (Offer_Group.getSerNo().equals("1")) {
    for (int j = 1; j < contactList.size(); j++) {
    Inv_Obj = new Cls_Sal_InvItems();
    Inv_Obj = contactList.get(j);
    // Inv_Obj.setProID("");
    //Inv_Obj.setPro_bounce("0");
    //Inv_Obj.setPro_dis_Per("0");
    //Inv_Obj.setPro_amt("0");
    if ( Inv_Obj.getProID().toString().equals("") &&  (Inv_Obj.getno().equals(Offer_Group.getItem_no()))) {

    Check_All_Promotion_Items(Offer_Group.getGrv_no());
    Apply_Promotion();
    }//  end Inv_Obj.getno()==Offer_Group.getItem_no()

    }


    } // end If (obj.getSerNo().equals("1"))
    }

    /*

    String msg ="";
    Cls_Offers_Hdr cls_offers_hdr;
    int x;
    for (x = 0; x < cls_offers_hdrs.size(); x++) {

    msg = msg + " \n\r" +   cls_offers_hdrs.get(x).getOffer_Name().toString() ;

    }
    msg = msg + " \n\r" ;
    msg = msg + " \n\r" ;
    msg = msg + " \n\r" +  "مجموع العروض هو : " + x +"";



    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle("العروض التي وافقت الشروط");
    alertDialog.setMessage(msg);
    alertDialog.setIcon(R.drawable.tick);


    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
    Apply_Promotion();

    }
    });
    // if ( x>0)
    alertDialog.show();

    */



    Calc_LowPriPro();


    }
    public void btn_Calc_Promotion( View view ) {
    Gf_Calc_Promotion();
    }
    private int Check_All_Promotion_Items(String groupNo){
    //   groupNo = "2";
    int result = 0 ;
    Cls_Offers_Groups Offer_Group ;
    Cls_Sal_InvItems Inv_Obj ;
    boolean flg = true;
    cls_offers_hdrs.clear();
    for (int x = 0; x < offer_groups_List.size(); x++) {
    Offer_Group = new Cls_Offers_Groups();
    Offer_Group = offer_groups_List.get(x);

    if (Offer_Group.getGrv_no().equals( groupNo) ) {



    if (flg == true) {
    flg = false;
    for (int j = 1; j < contactList.size(); j++) {
    Inv_Obj = new Cls_Sal_InvItems();
    Inv_Obj = contactList.get(j);

    //  if ( Inv_Obj.getProID().toString().equals("")&&(Inv_Obj.getno().equals(Offer_Group.getItem_no()))) {
    if ( Inv_Obj.getProID().toString().equals("")&&(Offer_Group.getItem_no().equals(Inv_Obj.getno()))) {
    if(Offer_Group.getGro_type().equals("1") ){
    if ( (  Integer.valueOf(Inv_Obj.getQty()) >= Integer.valueOf( Offer_Group.getQty()))  && (Inv_Obj.getUnite().equals(Offer_Group.getUnit_no())))  {
    flg = true;
    break;
    }
    }
    else {
    flg = true;
    break;
    }
    }//  end Inv_Obj.getno()==Offer_Group.getItem_no()

    }
    }
    /* else {
    result = -1;
    return result;
    }*/
    } // end If (obj.getSerNo().equals("1"))


    }
    if (flg == true) {
    String q = "   SELECT  distinct oh.TotalValue,oh.ID,oh.Offer_No,oh.Offer_Name,oh.Offer_Date,oh.Offer_Type,oh.Offer_Status,oh.Offer_Begin_Date" +
    " ,oh.Offer_Exp_Date,oh.Offer_Result_Type,oh.Offer_Dis , oh.Offer_Amt FROM Offers_Hdr oh left join    " +
    " Offers_Dtl_Cond odc on odc.Trans_ID  = oh.ID where odc.Gro_Num = '"+groupNo+"'";


    Cursor c1 = sqlHandler.selectQuery(q);
    Cls_Offers_Hdr cls_offers_hdr = new Cls_Offers_Hdr();

    if (c1 != null && c1.getCount() != 0) {
    if (c1.getCount() > 0) {
    c1.moveToFirst();
    cls_offers_hdr.setOffer_Name(c1.getString(c1.getColumnIndex("Offer_Name")));
    cls_offers_hdr.setID(c1.getString(c1.getColumnIndex("ID")));
    cls_offers_hdr.setOffer_Type(c1.getString(c1.getColumnIndex("Offer_Type")));
    cls_offers_hdr.setOffer_Dis(c1.getString(c1.getColumnIndex("Offer_Dis")));
    cls_offers_hdr.setOffer_Amt(c1.getString(c1.getColumnIndex("Offer_Amt")));
    cls_offers_hdr.setOffer_Result_Type(c1.getString(c1.getColumnIndex("Offer_Result_Type")));
    cls_offers_hdr.setTotalValue(c1.getString(c1.getColumnIndex("TotalValue")));

    cls_offers_hdrs.add(cls_offers_hdr);

    }
    }
    //  Bundle bundle = new Bundle();
    // bundle.putParcelableArrayList("Scr", cls_offers_hdrs);
    // FragmentManager Manager =  getFragmentManager();
    /// PromotionEffict obj = new PromotionEffict();
    //  obj.setArguments(bundle);
    //  obj.show(Manager, null);





    result = Integer.valueOf(groupNo);
    //Toast.makeText(this,groupNo,Toast.LENGTH_SHORT).show();

    }
    return result;
    }
    public void btn_share(View view) {
    //RemoveAnmation();
//    ImageButton imageButton9= (ImageButton)findViewById(R.id.imageButton9);
   // imageButton9.startAnimation(shake);
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



    Cls_Sal_InvItems Inv_Obj ;

    for (int j = 1; j < contactList.size(); j++) {
    Inv_Obj = new Cls_Sal_InvItems();
    Inv_Obj = contactList.get(j);

    Inv_Obj.setDis_Amt(String.valueOf(Double.parseDouble(Inv_Obj.getDis_Amt()) + Double.parseDouble(Inv_Obj.getPro_amt()) + Double.parseDouble(Inv_Obj.getDisAmtFromHdr())));
    Inv_Obj.setDiscount(String.valueOf(Double.parseDouble(Inv_Obj.getDiscount()) + Double.parseDouble(Inv_Obj.getPro_dis_Per()) + Double.parseDouble(Inv_Obj.getDisPerFromHdr())));
    Inv_Obj.setBounce(String.valueOf(Double.parseDouble(Inv_Obj.getBounce()) + Double.parseDouble(Inv_Obj.getPro_bounce()) ));

    }



    final  String str;




    String  query = "SELECT distinct OrderNo, acc,date,UserID, COALESCE(hdr_dis_per,0) as hdr_dis_per  , COALESCE(hdr_dis_value ,0) as  hdr_dis_value , COALESCE(Total ,0) as  Total , COALESCE(Net_Total ,0) as Net_Total , COALESCE( Tax_Total ,0) as Tax_Total , COALESCE(bounce_Total ,0) as bounce_Total , COALESCE( include_Tax ,0) as include_Tax" +
    " ,COALESCE( disc_Total ,0) as  disc_Total , COALESCE(inovice_type ,0)  as inovice_type  ,V_OrderNo  FROM Sal_invoice_Hdr where OrderNo  ='" +pono.getText().toString()+"'";



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
    jsonObject.put("CashCustNm",custNm.getText().toString());
    jsonObject.put("V_OrderNo",c1.getString(c1.getColumnIndex("V_OrderNo")));
    }
    catch ( JSONException ex){
    ex.printStackTrace();
    }

    }
    c1.close();
    String json = new Gson().toJson(contactList);
    str = jsonObject.toString()+ json;


    loadingdialog = ProgressDialog.show(Sale_InvoiceActivity.this,getResources().getText(R.string.PleaseWait),getResources().getText(R.string.PostUnderProccess), true);
    loadingdialog.setCancelable(false);
    loadingdialog.setCanceledOnTouchOutside(false);
    loadingdialog.show();
    final Handler _handler = new Handler();


    // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

    new Thread(new Runnable() {
    @Override
    public void run() {

    CallWebServices ws = new CallWebServices(Sale_InvoiceActivity.this);
    ws.Save_Sal_Invoice(str);
    try {

    if (We_Result.ID > 0) {
    ContentValues cv = new ContentValues();
    TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
    cv.put("Post", We_Result.ID);
    long i;
    i = sql_Handler.Update("Sal_invoice_Hdr", cv, "OrderNo='"+ DocNo.getText().toString()+"'");

    _handler.post(new Runnable() {
    public void run() {
    AlertDialog alertDialog = new AlertDialog.Builder(
    Sale_InvoiceActivity.this).create();
    alertDialog.setTitle(getResources().getText(R.string.Sale_invoice));
    alertDialog.setMessage(getResources().getText(R.string.PostCompleteSuccfully));
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
    Sale_InvoiceActivity.this).create();
    alertDialog.setTitle( getResources().getText(R.string.PostNotCompleteSuccfully) + "   " +We_Result.ID+"");
    alertDialog.setMessage(We_Result.Msg.toString());
    alertDialog.setIcon(R.drawable.tick);
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
    }
    });
    alertDialog.show();

    alertDialog.setIcon(R.drawable.delete);
    alertDialog.setMessage(getResources().getText(R.string.PostNotCompleteSuccfully) +"    " );
    }
    });
    }

    } catch (final Exception e) {
    loadingdialog.dismiss();
    _handler.post(new Runnable() {
    public void run() {
    AlertDialog alertDialog = new AlertDialog.Builder(
    Sale_InvoiceActivity.this).create();
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
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

  /*  View view  = null ;
    ExistAfterSacve = 1 ;
    if (contactList.size()>0  && IsChange == true) {
    btn_save_po(view);
    }else {
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater=getMenuInflater();
    inflater.inflate(R.menu.sal_invoice_menu, menu);
    return super.onCreateOptionsMenu(menu);



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId())
    {
    case R.id.BalanceQty:
    Bundle bundle = new Bundle();
    bundle.putString("Scr", "SalInvoice");
    FragmentManager Manager =  getFragmentManager();
    SearchManBalanceQty obj = new SearchManBalanceQty();
    obj.setArguments(bundle);
    obj.show(Manager, null);
    break;
    case R.id.Exist:
    View view = null ;
    btn_back(view);
    break;

    }

    return true;
    }
    public void InsertBalanceQty(String No, String Nm) {
    FillAdapterFromBalanceQty(No);
    showList();
    BalanceQtyTrans = true;
    }
    private void FillAdapterFromBalanceQty(String OrderNo) {
    contactList.clear();


    Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;
    sqlHandler = new SqlHandler(this);

    NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
    DecimalFormat df = (DecimalFormat)nf;
    CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);

    /* query = "  select ifnull(pod.Operand,0) as Operand  ,  pod.bounce_qty,pod.dis_per , pod.dis_Amt , pod.OrgPrice , pod.tax_Amt , pod.total ,Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo  " +
    " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt    " +
    "   from Sal_invoice_Det pod left join invf on invf.Item_No =  pod.itemno    " +
    "   left join Unites on Unites.Unitno=  pod.unitNo  Where pod.OrderNo='" + Order_no.getText().toString() + "'";

    */

    String   query      = "  select distinct Unites.UnitName,  invf.Item_Name, pod.Item_No,pod.Qty,pod.ActQty,pod.Diff ,pod.Unit_No " +
    " ,UnitItems.Operand ,UnitItems.price , invf.tax  from BalanceQty pod left join invf on invf.Item_No =  pod.Item_No    " +
    "left join Unites on Unites.Unitno=  pod.Unit_No " +
    " Left join UnitItems on UnitItems.item_no =  pod.Item_No and UnitItems.unitno = pod.Unit_No" +
    " Where    ifnull( cast( pod.Diff as double) ,0) > 0     and pod.OrderNo='" +OrderNo + "'";


    Cursor c1 = sqlHandler.selectQuery(query);
    if (c1 != null && c1.getCount() != 0) {
    if (c1.moveToFirst()) {
    do {
    //    Save_List(c1.getString(c1.getColumnIndex("Item_No")), Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString(),Operand);
    //Save_List(c1.getString(c1.getColumnIndex("Item_No")), "0", c1.getString(c1.getColumnIndex("Diff")), "16", c1.getString(c1.getColumnIndex("Unit_No")), "0", "0", c1.getString(c1.getColumnIndex("Item_Name")), c1.getString(c1.getColumnIndex("UnitName")), "0", "1");
    try {
    Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();

    contactListItems.setno(c1.getString(c1.getColumnIndex("Item_No")));
    contactListItems.setName(c1.getString(c1.getColumnIndex("Item_Name")));

    Price = SToD(c1.getString(c1.getColumnIndex("price")));
    Tax = SToD(c1.getString(c1.getColumnIndex("tax")));
    Item_Total = SToD(c1.getString(c1.getColumnIndex("Diff"))) * Price;

    Item_Total = Double.parseDouble(Item_Total.toString());

    if (Tax_Include.isChecked()) {
    contactListItems.setprice(String.valueOf(Price / ((Tax / 100) + 1)));
    } else {
    contactListItems.setprice(String.valueOf(Price));

    }

    contactListItems.setItemOrgPrice(String.valueOf(Price));
    contactListItems.setQty(c1.getString(c1.getColumnIndex("Diff")));
    contactListItems.setTax(String.valueOf(Tax));
    contactListItems.setUnite(c1.getString(c1.getColumnIndex("Unit_No")));
    contactListItems.setBounce("0");
    contactListItems.setDiscount("0");
    contactListItems.setProID("");
    contactListItems.setDis_Amt("0");
    contactListItems.setUniteNm(c1.getString(c1.getColumnIndex("UnitName")));
    contactListItems.setPro_amt("0");
    contactListItems.setPro_dis_Per("0");
    contactListItems.setPro_bounce("0");
    contactListItems.setPro_Total("0");
    contactListItems.setDisAmtFromHdr("0");
    contactListItems.setDisPerFromHdr("0");
    contactListItems.setTax_Amt("0");
    contactListItems.setProType("0");
    contactListItems.setOperand(c1.getString(c1.getColumnIndex("Operand")));
    contactListItems.setTotal(String.valueOf(df.format(Item_Total)));
    contactList.add(contactListItems);

    /*  CalcTotal();
    showList();*/

    }
    catch ( Exception ex) {
    Toast.makeText(this,getResources().getText(R.string.ConnectError),Toast.LENGTH_SHORT).show();
    }
    } while (c1.moveToNext());



    }
    c1.close();
    CalcTotal();
    showList();
    }

    }



}
