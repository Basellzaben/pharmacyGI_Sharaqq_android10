package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cds_jo.pharmacyGI.assist.LoginActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReturnQty extends AppCompatActivity {
    SqlHandler sqlHandler;
    ListView lv_Items;
    ArrayList<ContactListItems> contactList ;
     String UserID= "";

public   String json;
    Boolean IsNew;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.returnqty);
        lv_Items = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);
        IsNew = true;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID= sharedPreferences.getString("UserID", "");

        GetMaxPONo();

        contactList = new ArrayList<ContactListItems>();
        contactList.clear();

        showList(0);

        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
        Tax_Include.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CalcTotal();
                showList(0);

            }
        });
      /*  lv_Items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                EditText Order_no = (EditText) findViewById(R.id.et_OrdeNo);
                ContactListItems contactListItems = (ContactListItems) arg0.getItemAtPosition(arg2);
                String slno = contactListItems.getno();
                String delQuery = "DELETE FROM ReturnQtydetl where itemno='" + slno.toString() + "' and  orderno ='" + Order_no.getText().toString() + "'";
                sqlHandler.executeQuery(delQuery);
                showList(1);

                return false;
            }
        });*/


        ImageButton back=(ImageButton)findViewById(R.id.imageButton7);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(ReturnQty.this, GalaxyMainActivity.class);
                startActivity(k);
            }
        });


        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);

        TextView accno = (TextView)findViewById(R.id.tv_acc);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));

    }
    public  void GetMaxPONo() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");
        if(Login.toString().equals("No")){
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }

        String query = "SELECT  COALESCE(MAX(orderno), 0) +1 AS no FROM ReturnQtyhdr where userid ='"+UserID+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";
        EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
        if (c1.getCount() > 0) {
            c1.moveToFirst();
            max= String.valueOf(c1.getInt(0));
        }

        if (max.length()==1) {
            Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {
            Maxpo.setText(max);

        }

       // Maxpo.setText(max);

    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
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
                ReturnQty.this, contactList);
        lv_Items.setAdapter(contactListAdapter);
        //  json = new Gson().toJson(contactList);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public  void showPop()  {

      TextView OrderNo = (TextView)findViewById(R.id.et_OrdeNo);

      String query = "SELECT   orderno FROM ReturnQtyhdr Where orderno = '"+OrderNo.getText().toString()+"'";
      Cursor c1 = sqlHandler.selectQuery(query);

      EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);


      Bundle bundle = new Bundle();
      bundle.putString("Scr", "RetrnQty");
      bundle.putString("OrderNo",OrderNo.getText().toString());
      FragmentManager Manager =  getFragmentManager();
      PopCus_Qty_Items obj = new PopCus_Qty_Items();
      obj.setArguments(bundle);
      obj.show(Manager, null);

  }
    public void btn_back(View view) {
        Intent k= new Intent(this, Main2Activity.class);
        startActivity(k);

    }
    public void Save_Method(String ItemNo , String p , String q ,String t,String u ,String dis ,String bounce) {

        TextView OrderNo = (TextView)findViewById(R.id.et_OrdeNo);
        String bounce_unitno,bounce_qty,dis_per,dis_value;
        bounce_unitno=bounce_qty=dis_per=dis_value="";
        String  query = "INSERT INTO ReturnQtydetl(orderno,itemno,price,qty,tax,unitNo,dis_Amy,dis_per,bounce_qty,bounce_unitno) values " +
                "('"+ OrderNo.getText().toString()+"','"+ItemNo +"',"+p+","+q+","+t+",'"+u+"','"+dis_value+"','"+dis_value+"','"+bounce_qty+"','"+bounce_unitno+"')";

        sqlHandler.executeQuery(query);
        showList(1);
    }
    public void Save_List(String ItemNo   , String q ,  String   u ,  String   ItemNm , String UnitName ) {






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



        //Item_Total =Double.valueOf(q) * Double.valueOf(p);
        //Price= Double.valueOf(p);

       // Item_Total = Double.valueOf(Item_Total);


        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        ContactListItems contactListItems = new ContactListItems();
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        contactListItems.setprice("0");
        contactListItems.setQty(q);
        contactListItems.setTax("0");
        contactListItems.setUnite(u);
        contactListItems.setBounce("0");
        contactListItems.setDiscount("0");
        contactListItems.setDis_Amt("0");
        contactListItems.setUniteNm(UnitName);
        contactListItems.setPro_amt("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_Total("0");
        contactListItems.setProID("");
        contactListItems.setTax_Amt("0");
        contactListItems.setTotal("0");
        contactListItems.setItemOrgPrice("0");

        contactList.add(contactListItems);
        CalcTotal();

        showList(1);
    }
    private void CalcTotal(){
     Double Total,Tax_Total,Dis_Amt,Po_Total;
      ContactListItems contactListItems = new ContactListItems();
      NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
      DecimalFormat df = (DecimalFormat)nf;

      Total = 0.0;
      Tax_Total = 0.0;
      Dis_Amt = 0.0;
      Po_Total = 0.0;
      TextView Subtotal = (TextView)findViewById(R.id.et_Total);
      TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);
      TextView dis = (TextView)findViewById(R.id.et_dis);
      TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
      CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);



      for (int x = 0; x < contactList.size(); x++) {
          contactListItems = new ContactListItems();
          contactListItems = contactList.get(x);
          Total =  Total + (Double.valueOf (contactListItems.getTotal()));
          Tax_Total = Tax_Total + (Double.valueOf (contactListItems.getTax_Amt()));
          Dis_Amt = Dis_Amt + (Double.valueOf (contactListItems.getDis_Amt()));

      }

      TotalTax.setText(String.valueOf(df.format(Tax_Total)));
      Subtotal.setText(String.valueOf(df.format(Total)));
      dis.setText(String.valueOf(df.format(Dis_Amt)));
      if (Tax_Include.isChecked()){
          Po_Total = Po_Total + ((Double.valueOf(Subtotal.getText().toString()) - Double.valueOf(dis.getText().toString()) )    );
      }
      else{
          Po_Total = Po_Total + ((Double.valueOf(Subtotal.getText().toString()) - Double.valueOf(dis.getText().toString()) )   + Double.valueOf(TotalTax.getText().toString()) );
      }
      NetTotal.setText(String.valueOf(df.format(Po_Total)));
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
        TextView Order_no = (TextView)findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView)findViewById(R.id.tv_acc);
        /*TextView Total = (TextView)findViewById(R.id.et_Total);
        TextView dis = (TextView)findViewById(R.id.et_dis);
        TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);
        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);
*/
        Order_no.setText(No);
        CustNm.setText(Nm);
        accno.setText(acc);
        contactList.clear();
        showList(0);

       sqlHandler = new SqlHandler(this);

       /* Tax_Include.setChecked(false);
        String query = "  select  *  from ReturnQtyhdr  where orderno ='" + Order_no.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                Total.setText(c1.getString(c1.getColumnIndex("Total")).toString());
                dis.setText(c1.getString(c1.getColumnIndex("disc_Total")).toString());
                NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")).toString());
                TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")).toString());



            }
        }
        c1.close();*/

       String   query = "  select Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.unitNo " +
               "   from ReturnQtydetl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + Order_no.getText().toString() + "'";
        Cursor   c1 = sqlHandler.selectQuery(query);
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
                    contactListItems.setTax("0");
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setBounce("0");
                    contactListItems.setDiscount("0");
                    contactListItems.setDis_Amt("0");

                    contactListItems.setDis_Amt("0");

                    contactListItems.setUnite("0");

                    contactListItems.setTax_Amt("0");

                    contactListItems.setTotal("0");

                    contactListItems.setProID("0");

                    contactListItems.setPro_bounce("0");

                    contactListItems.setPro_dis_Per("0");

                    contactListItems.setPro_amt("0");

                    contactListItems.setPro_Total("0");
                    contactListItems.setItemOrgPrice("0");
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
        bundle.putString("Scr", "RetnQty");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_show_Pop(View view) {
        showPop();
    }
    public void btn_print(View view) {

   /*  Intent k = new Intent(this,Convert_Layout_Img.class);
    k.putExtra("Scr", "po");
    TextView   CustNm =(TextView)findViewById(R.id.tv_cusnm);
    TextView   OrdeNo = (TextView)findViewById(R.id.et_OrdeNo);
    TextView   accno = (TextView)findViewById(R.id.tv_acc);
    k.putExtra("cusnm", CustNm.getText().toString());
    k.putExtra("OrderNo", OrdeNo.getText().toString());
    k.putExtra("accno", accno.getText().toString());

    startActivity(k);*/
}
    public void Save_Recod_Po(){


        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView)findViewById(R.id.tv_acc);

        TextView Total = (TextView)findViewById(R.id.et_Total);
        TextView dis = (TextView)findViewById(R.id.et_dis);
        TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);

        CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());



        Long i;
        ContentValues cv =new ContentValues();
        cv.put("orderno", pono.getText().toString());
        cv.put("acc",acc.getText().toString());
        cv.put("date",currentDateandTime);
        cv.put("userid",UserID);



        if (IsNew==true) {
            i = sqlHandler.Insert("ReturnQtyhdr", null, cv);
        }
        else {
            i = sqlHandler.Update("ReturnQtyhdr", cv, "orderno ='"+pono.getText().toString()+"'");
        }



        if(i>0){
            String q ="Delete from  ReturnQtydetl where orderno ='"+ pono.getText().toString()+"'";
            sqlHandler.executeQuery(q);

            for (int x = 0; x < contactList.size(); x++) {
                ContactListItems contactListItems = new ContactListItems();
                contactListItems = contactList.get(x);


                cv = new ContentValues();
                cv.put("orderno", pono.getText().toString());
                cv.put("itemno",contactListItems.getNo());
                cv.put("price", contactListItems.getPrice().toString());
                cv.put("qty", contactListItems.getQty().toString());
                cv.put("unitNo", contactListItems.getUnite().toString());
                cv.put("total", contactListItems.getTotal().toString());




                i = sqlHandler.Insert("ReturnQtydetl", null, cv);
            }
        }

            if (i> 0 ) {
                GetMaxPONo();
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("Galaxy");
                alertDialog.setMessage("تمت عملية الحفظ بنجاح");
                contactList.clear();
                showList(0);
                custNm.setText("");
                acc.setText("");
                Total.setText("");
                dis.setText("");
                NetTotal.setText("");
                TotalTax.setText("");
                alertDialog.setIcon(R.drawable.tick);

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // Showing Alert Message
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("حذف طلب بيع");
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




        String query ="Delete from  ReturnQtyhdr where orderno ='"+ pono.getText().toString()+"'";
        sqlHandler.executeQuery(query);


        query ="Delete from  ReturnQtydetl where orderno ='"+ pono.getText().toString()+"'";
        sqlHandler.executeQuery(query);
        contactList.clear();
        GetMaxPONo();
        showList(0);
        IsNew = true;
       custNm.setText("");
       acc.setText("");

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
        SearchRetnQtyOrder obj = new SearchRetnQtyOrder();
        obj.setArguments(bundle);
        obj.show(Manager, null);
       }
    public void btn_share(View view) {

       /*final  SqlHandler sql_Handler = new SqlHandler(this);
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

        final  String str;





        String  query = "SELECT acc,  date ,Delv_day_count FROM ReturnQtyhdr where orderno  ='" +pono.getText().toString()+"'";
         Cursor c1 = sqlHandler.selectQuery(query);
        String  Date,Cust_No,Delv_day_count;
        Date=Cust_No=Delv_day_count="";

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            Cust_No=c1.getString(c1.getColumnIndex("acc"));
            Delv_day_count   =c1.getString(c1.getColumnIndex("Delv_day_count"));
            Date  =c1.getString(c1.getColumnIndex("date"));

        }
        c1.close();
        JSONObject  jsonObject = new JSONObject();
        try {
           jsonObject.put("Cust_No", Cust_No.toString());
           jsonObject.put("day_Count",Delv_day_count==null ? "null" : Delv_day_count.toString());
           jsonObject.put("Date",Date.toString());
           jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));
           jsonObject.put("OrderNo",pono.getText().toString());

            jsonObject.put("Total",Total.getText().toString());
            jsonObject.put("Net_Total",NetTotal.getText().toString());
            jsonObject.put("Tax_Total",TotalTax.getText().toString());
            jsonObject.put("bounce_Total","0");

            jsonObject.put("disc_Total",dis.getText().toString());



            if (Tax_Include.isChecked()){
                jsonObject.put("include_Tax","1");
            }
            else
            {
                jsonObject.put("include_Tax","0");
            }

        }
        catch ( JSONException ex){
            ex.printStackTrace();
        }
        String json = new Gson().toJson(contactList);
         str = jsonObject.toString()+ json;


        loadingdialog = ProgressDialog.show(ReturnQty.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد طلب البيع", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices();
                ws.Save_po(str, "Insert_PurshOrder");
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("posted", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("ReturnQtyhdr", cv, "orderno='"+ DocNo.getText().toString()+"'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        ReturnQty.this).create();
                                alertDialog.setTitle("اعتماد طلب بيع");
                                alertDialog.setMessage("تمت عملية اعتماد طلب البيع بنجاح" + We_Result.ID +"");
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
                                custNm.setText("");
                                acc.setText("");
                                Total.setText("");
                                dis.setText("");
                                NetTotal.setText("");
                                TotalTax.setText("");
                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        ReturnQty.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " +We_Result.ID.toString() );
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
                                    ReturnQty.this).create();
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
        }).start();*/
        }
    public void btn_new(View view) {
        TextView custNm = (TextView)findViewById(R.id.tv_cusnm);
        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        TextView Total = (TextView)findViewById(R.id.et_Total);
        TextView dis = (TextView)findViewById(R.id.et_dis);
        TextView TotalTax = (TextView)findViewById(R.id.et_TotalTax);
        TextView NetTotal = (TextView)findViewById(R.id.tv_NetTotal);


        Total.setText("0.0");
        dis.setText("0.0");
        TotalTax.setText("0.0");
        NetTotal.setText("0.0");

        IsNew = true ;
        custNm.setText("");
        pono.setText("");
        acc.setText("");
        GetMaxPONo();
        contactList.clear();
        showList(0);
    }
    public void btn_Delete_Item( final View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("طلب البيع");
        alertDialog.setMessage("هل انت متاكد من عملية الحذف");
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int position =lv_Items.getPositionForView(view);
                contactList.remove(position);
                CalcTotal();
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
}
