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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cds_jo.pharmacyGI.assist.Cls_Man_Qty_Adapter;
import com.cds_jo.pharmacyGI.assist.Convert_Man_Balance_Qty_To_Img;
import com.cds_jo.pharmacyGI.assist.LoginActivity;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hearder.main.Header_Frag;

public class ManBalanceQtyActivity extends FragmentActivity {
    ListView lst_Items ;
    ArrayList<Cls_Man_Balanc>   cls_trans_qties ;
    TextView tv_RowCount ;
    SqlHandler sqlHandler;
    String UserID= "";
    Boolean IsNew;
    EditText Maxpo;
    String MaxSaleInvoiceNo = "";
    int AllowSalInvMinus ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_man_balance_qty);
        sqlHandler = new SqlHandler(this);
        lst_Items =(ListView)findViewById(R.id.lst_Items);
        Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
        cls_trans_qties = new ArrayList<Cls_Man_Balanc>();
        tv_RowCount = (TextView)findViewById(R.id.tv_RowCount);
        IsNew = true;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID= sharedPreferences.getString("UserID", "");
      /*  btn_GetData();
        GetMaxPONo();

*/
        Fragment frag=new Header_Frag();
       FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

    }
    @SuppressLint("Range")
    public  void GetMaxPONo(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");
        if(Login.toString().equals("No")){
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }

        String query = "SELECT  COALESCE(MAX(OrderNo), 0) +1 AS no FROM BalanceQty where userid ='"+UserID+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if ( c1!=null&&c1.getCount() > 0 ) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

    /*    String max1="0";
        max1 = sharedPreferences.getString("m7", "");
        if (max1==""){
            max1 ="0";
        }
        max1 =String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max))
        {
            max = max1 ;
        }*/

        if (max.length()==1) {
            Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {
            Maxpo.setText(intToString(Integer.valueOf(max), 7)  );

        }


        Maxpo.setFocusable(false);
        Maxpo.setEnabled(false);
        Maxpo.setCursorVisible(false);

        // Maxpo.setText(max);

    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
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
    public void btn_GetData() {
        SqlHandler sqlHandler = new SqlHandler(ManBalanceQtyActivity.this);
        String query = "SELECT   distinct *   from  Sal_invoice_Hdr  where    Post = -1 And UserID  ='"+UserID+"'";


        Cursor c1 = sqlHandler.selectQuery(query);

        Double Sal_Qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    ManBalanceQtyActivity.this).create();

            alertDialog.setMessage("يوجد فاتير غير مرحلة ، الرجاء ترحيل الفواتير ومن عمل تسوية جرد  مستودع المندوب");

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
    public void btn__Remove_Item( final View view) {
        position = lst_Items.getPositionForView(view);
        cls_trans_qties.remove(position);
        lst_Items.invalidateViews();
      /*  Cls_Man_Qty_Adapter cls_trans_qty_adapter = new Cls_Man_Qty_Adapter(
                ManBalanceQtyActivity.this, cls_trans_qties);
        lst_Items.setAdapter(cls_trans_qty_adapter);*/
        tv_RowCount.setText("" + (cls_trans_qties.size()));

    }
    int position ;
    public void btn_Edite_Qty( final View view) {
        position = lst_Items.getPositionForView(view);
        Cls_Man_Balanc cls_trans_qty = new Cls_Man_Balanc();
        cls_trans_qty = cls_trans_qties.get(position);
               /* registerForContextMenu(view);
        openContextMenu(view);*/
        Bundle bundle = new Bundle();
        bundle.putString("Qty", cls_trans_qty.getQty());
        bundle.putString("ItemName", cls_trans_qty.getItem_Name());
        android.app.FragmentManager Manager = getFragmentManager();
        PopEnterActQty obj = new PopEnterActQty();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Cls_Sal_InvItems contactListItems =new Cls_Sal_InvItems();


        menu.setHeaderTitle("contactList.get(position).getName()");
        menu.add(Menu.NONE, 1, Menu.NONE, "تعديل");
        menu.add(Menu.NONE, 2, Menu.NONE, "حذف");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        TextView pono = (TextView)findViewById(R.id.et_OrdeNo);
        // TODO Auto-generated method stub
        switch(item.getItemId())
        {
            case 1:
            {

               /* ArrayList<Cls_Sal_InvItems> Itemlist = new ArrayList <Cls_Sal_InvItems> ();

                Itemlist.add(contactList.get(position))  ;
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "Sal_inv");
                bundle.putString("OrderNo", pono.getText().toString());
                bundle.putSerializable("List", Itemlist);
                FragmentManager Manager =  getFragmentManager();
                PopSal_Inv_Select_Items obj = new PopSal_Inv_Select_Items();
                obj.setArguments(bundle);
                obj.show(Manager, null);*/
            }
            break;
            case 2:
            {
               /* AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
                alertDialog.show();*/

            }
            break;

        }

        return super.onContextItemSelected(item);
    }
    private void ShowData(){

        AllowSalInvMinus =Integer.parseInt( DB.GetValue(this,"ComanyInfo","AllowSalInvMinus","1=1"));

        final ProgressDialog progressDialog;
        final Handler _handler = new Handler();
        progressDialog = new ProgressDialog(ManBalanceQtyActivity.this);
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
            @SuppressLint("Range")
            @Override
            public void run() {

                try {
                    Integer i;
                    String q = "";


                    cls_trans_qties.clear();

                    Cls_Man_Balanc cls_trans_qty = new Cls_Man_Balanc();

                    SqlHandler sqlHandler = new SqlHandler(ManBalanceQtyActivity.this);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ManBalanceQtyActivity.this);
                    String u =  sharedPreferences.getString("UserID", "");

                    if(AllowSalInvMinus==1){
                        q = " Select distinct  ifnull(Unites.Unitno,0)as  Unitno,  ifnull(ms.ser,0) as ser, ifnull(ms.docno,0) as docno ,ifnull(ms.StoreName,0)  as StoreName, ifnull(invf.Item_No,0) as itemno  , ifnull(invf.Item_Name,0) as Item_Name" +
                                ",ifnull(Unites.UnitName ,0)  as  UnitName ,ifnull(ms.qty,0) as qty   ,ifnull(ms.des,0)as des ,  ifnull(ms.date  ,0)as date" +
                                "  from  invf left join ManStore  ms   on invf.Item_No =  ms.itemno    left join Unites on Unites.Unitno=  ms.UnitNo";

                    }else {
                        q = "Select distinct   ifnull(Unites.Unitno,0)as  Unitno, ms.ser, ms.docno ,ms.StoreName , ms.itemno , invf.Item_Name   ,Unites.UnitName     ,ms.qty  ,ms.des ,  ms.date " +
                                "   from  ManStore  ms left join invf on invf.Item_No =  ms.itemno    left join Unites on Unites.Unitno=  ms.UnitNo " +
                                "   and ms.SManNo ='" +u.toString()+"'";
                    }

                    Double qty = 0.0;
                    Double SaledQtyNotPosted = 0.0 ;
                    Cursor c =  sqlHandler.selectQuery(q);
                    i = 0;
                    if (c != null && c.getCount() != 0) {
                        if (c.moveToFirst()) {
                            do {


                                cls_trans_qty = new Cls_Man_Balanc();


                                cls_trans_qty.setItemno(c.getString(c
                                        .getColumnIndex("itemno")));
                                cls_trans_qty.setItem_Name(c.getString(c
                                        .getColumnIndex("Item_Name")));
                                cls_trans_qty.setUnitNo(c.getString(c
                                        .getColumnIndex("Unitno")));
                                cls_trans_qty.setQtyAcc(SToD(c.getString(c
                                        .getColumnIndex("qty"))).toString());
                                cls_trans_qty.setUnitName(c.getString(c
                                        .getColumnIndex("UnitName")));
                                SaledQtyNotPosted = GetSaledQtyNotPosted(c.getString(c.getColumnIndex("itemno")));
                                cls_trans_qty.setQtySaled(SaledQtyNotPosted.toString());
                                qty = ( (Double.parseDouble(c.getString(c.getColumnIndex("qty"))) - SaledQtyNotPosted));
                                SaledQtyNotPosted=0.0;
                                cls_trans_qty.setQty((SToD(qty.toString())).toString());


                                cls_trans_qty.setAct_Aty("0");
                                if (qty<0) {
                                    cls_trans_qty.setDiff(( (-1)*qty)+"");
                                }

                                if (qty!=0){
                                    cls_trans_qties.add(cls_trans_qty);
                                }
                                qty = 0.0;


                                progressDialog.setMax(c.getCount());

                                progressDialog.incrementProgressBy(1);

                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }

                                i = i+1;

                            } while (c.moveToNext());

                        }
                        c.close();
                    }
                    _handler.post(new Runnable() {
                        public void run() {
                            Cls_Man_Qty_Adapter cls_trans_qty_adapter = new Cls_Man_Qty_Adapter(
                                    ManBalanceQtyActivity.this, cls_trans_qties);
                            lst_Items.setAdapter(cls_trans_qty_adapter);


                        }
                    });

                    final int total = i;
                    _handler.post(new Runnable() {
                        public void run() {
                            tv_RowCount.setText(""+(total));
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ManBalanceQtyActivity.this).create();

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
                                    ManBalanceQtyActivity.this).create();
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
    private Double GetSaledQtyNotPosted(String ItemNo ){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        SqlHandler sqlHandler = new SqlHandler(ManBalanceQtyActivity.this);
        String query = "SELECT  distinct    (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                "    where   sih.Post = -1  and ui.item_no ='"+ItemNo+"'   and sih.UserID ='" +u.toString()+"'"  ;


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

    public void btn_new(View view) {
        cls_trans_qties.clear();
        lst_Items.invalidateViews();
        GetMaxPONo();
        btn_GetData();
        IsNew = true;
    }
    public void btn_back(View view) {
        Intent i = new Intent(this,GalaxyMainActivity.class);
        startActivity(i);
    }

    public  void SaveQty(String ActQty , String Diff){
        cls_trans_qties.get(position).setAct_Aty(ActQty);
        cls_trans_qties.get(position).setDiff(Diff);

        Cls_Man_Qty_Adapter cls_trans_qty_adapter = new Cls_Man_Qty_Adapter(
                ManBalanceQtyActivity.this, cls_trans_qties);
        cls_trans_qty_adapter.notifyDataSetChanged() ;
     /* lst_Items.setAdapter(cls_trans_qty_adapter);
        lst_Items.scrollBy(0,0);*/
        lst_Items.invalidateViews();
    }
    public void btn_save_po(View view) {

        if (  cls_trans_qties.size()==0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("تسوية جرد المندوب");
            alertDialog.setMessage("لا يمكن التخزين دون وجود مواد");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();

            return;
        }




        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("جرد كميات مندوب");
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
    public void   Save_Recod_Po()    {

        ContentValues cv;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        Long i  = Long.parseLong("0");

        String q ="Delete from  BalanceQty where OrderNo ='"+ Maxpo.getText().toString()+"'";
        sqlHandler.executeQuery(q);

        for (int x = 0; x < cls_trans_qties.size(); x++) {
            Cls_Man_Balanc cls_trans_qty = new Cls_Man_Balanc();
            cls_trans_qty = cls_trans_qties.get(x);

            cv = new ContentValues();
            cv.put("OrderNo", Maxpo.getText().toString());
            cv.put("Item_No",cls_trans_qty.getItemno());
            cv.put("Unit_No", cls_trans_qty.getUnitNo().toString());
            cv.put("Qty", cls_trans_qty.getQty().toString());
            cv.put("ActQty", cls_trans_qty.getAct_Aty().toString());
            cv.put("Diff",cls_trans_qty.getDiff()==null?"0": cls_trans_qty.getDiff().toString());
            cv.put("UserID", UserID);
            cv.put("date", currentDateandTime);
            cv.put("posted", "-1");
            if ( cls_trans_qty.getDiff()!= null && SToD(cls_trans_qty.getDiff().toString())>0) {
                i = sqlHandler.Insert("BalanceQty", null, cv);
            }

        }


        if (i> 0 ) {
            //  GetMaxPONo();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("تسوية جرد المندوب");
            alertDialog.setMessage("تمت عملية الحفظ بنجاح ");
            IsNew=false;

            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    View view = null;
                    GetRecord();
                }
            });


            alertDialog.show();
        }else{
            if (i> 0 ) {
                //  GetMaxPONo();
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("تسوية جرد المندوب");
                alertDialog.setMessage("عملية  تسوية الجرد لم تتم بنجاح");
                IsNew=false;

                alertDialog.setIcon(R.drawable.tick);

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                alertDialog.show();
            }
        }


    }



    public void btn_Search_Orders(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "ManBalance");
        android.app.FragmentManager Manager = getFragmentManager();

        SearchManBalanceQty obj = new SearchManBalanceQty();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void Set_Order(String No, String Nm) {
        Maxpo.setText(No);
        GetRecord();
    }
    @SuppressLint("Range")
    private void GetRecord(){
        cls_trans_qties.clear();
        lst_Items.invalidateViews();
        cls_trans_qties.clear();
        Cls_Man_Balanc cls_trans_qty;

        SqlHandler sqlHandler = new SqlHandler(ManBalanceQtyActivity.this);

        String q      = "  select distinct  Unites.UnitName,  invf.Item_Name, pod.Item_No,pod.Qty,pod.ActQty,pod.Diff ,pod.Unit_No from BalanceQty pod left join invf on invf.Item_No =  pod.Item_No    left join Unites on Unites.Unitno=  pod.Unit_No  Where pod.OrderNo='" + Maxpo.getText().toString() + "'";


        Cursor c =  sqlHandler.selectQuery(q);

        if (c != null && c.getCount() != 0) {
            if (c.moveToFirst()) {
                do {
                    cls_trans_qty = new Cls_Man_Balanc();
                    cls_trans_qty.setItemno(c.getString(c
                            .getColumnIndex("Item_No")));
                    cls_trans_qty.setItem_Name(c.getString(c
                            .getColumnIndex("Item_Name")));
                    cls_trans_qty.setUnitNo(c.getString(c
                            .getColumnIndex("Unit_No")));
                    cls_trans_qty.setUnitName(c.getString(c
                            .getColumnIndex("UnitName")));
                    cls_trans_qty.setQty(c.getString(c
                            .getColumnIndex("Qty")));
                    cls_trans_qty.setAct_Aty(c.getString(c
                            .getColumnIndex("ActQty")));
                    cls_trans_qty.setDiff(c.getString(c
                            .getColumnIndex("Diff")));
                    cls_trans_qties.add(cls_trans_qty);


                } while (c.moveToNext());

            }
            c.close();
        }
        Cls_Man_Qty_Adapter cls_trans_qty_adapter = new Cls_Man_Qty_Adapter(
                ManBalanceQtyActivity.this, cls_trans_qties);
        lst_Items.setAdapter(cls_trans_qty_adapter);

        tv_RowCount.setText("" + (cls_trans_qties.size()));


    }

    public void btn_delete(View view) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("تسوية جرد المندوب");
        alertDialog.setMessage("هل انت متاكد من عملية الحذف");
        alertDialog.setIcon(R.drawable.delete);
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


        alertDialog.show();

    }

    public void Delete_Record_PO(){

        String query ="Delete from  BalanceQty where OrderNo  ='"+ Maxpo.getText().toString()+"'";
        sqlHandler.executeQuery(query);
        cls_trans_qties.clear();
        lst_Items.invalidateViews();
        GetMaxPONo();
        btn_GetData();
        IsNew = true;
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("تسوية جرد المندوب");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public void btn_print(View view) {

        Intent i = new Intent(this,Convert_Man_Balance_Qty_To_Img.class);
        i.putExtra("OrderNo", Maxpo.getText().toString());
        startActivity(i);
    }


    public void btn_ConvertToInvoice(View view) {
        Intent i = new Intent(getBaseContext(), Sale_InvoiceActivity.class);
        i.putExtra("BalanceQtyOrderNo", Maxpo.getText().toString());
        startActivity(i);


    }
}
