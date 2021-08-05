package com.cds_jo.pharmacyGI.assist;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.Cls_Offers_Hdr;
import com.cds_jo.pharmacyGI.ContactListItems;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Convert_Sal_Invoice_To_ImgActivity extends AppCompatActivity {
    private ESCPOSPrinter posPtr;
    SqlHandler sqlHandler;
    ListView lvCustomList;
    private Button mButton;
    private View mView;
    String ShowTax = "0";
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    private static final DecimalFormat oneDecimal = new DecimalFormat("#,##0.0");
    ImageView img_Logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1") ;
        final String PrinterType = sharedPreferences.getString("PrinterType", "1") ;


        if  (Company.equals("1") ||Company.equals("2") ){
            setContentView(R.layout.activity_convert__sal__invoice__to__img);
        }

       /* if  (Company.equals("2")){
            setContentView(R.layout.activity_convert__sal__invoice__to__img);
        }*/


        img_Logo = (ImageView) findViewById(R.id.img_Logo);
        File imgFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_Logo.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}

        Intent i = getIntent();
        mView = findViewById(R.id.f_view);
        TextView OrderNo = (TextView) findViewById(R.id.tv_OrderNo);
        OrderNo.setText(getIntent().getStringExtra("OrderNo"));


        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();


        mBluetoothAdapter.disable();

        String u =  sharedPreferences.getString("UserName", "");

        TextView tv_TaxAcc =(TextView)findViewById(R.id.tv_TaxAcc);
        tv_TaxAcc.setText(sharedPreferences.getString("TaxAcc1", ""));


        TextView tv_SupervisorMobile =(TextView)findViewById(R.id.tv_SupervisorMobile);
        tv_SupervisorMobile.setText(sharedPreferences.getString("SuperVisorMobile", ""));

        TextView tv_CompName =(TextView)findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));

        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        TextView Time = (TextView)findViewById(R.id.tv_time);
        Time.setText(StringTime);

       TextView tv_footer =(TextView)findViewById(R.id.tv_footer);
        TextView tv_footer1 =(TextView)findViewById(R.id.tv_footer1);

        tv_footer.setText(sharedPreferences.getString("CompanyNm", "")+" - "+sharedPreferences.getString("Address", "")+" - "+sharedPreferences.getString("Notes", ""));
        tv_footer1.setText(" هاتف الشركة  "+ (sharedPreferences.getString("CompanyMobile", "")));


        ShowTax=getIntent().getStringExtra("ShowTax");


        TextView    tv_UserNm  = (TextView)findViewById(R.id.tv_UserNm);
        tv_UserNm.setText(u);

        TextView    textView10  = (TextView)findViewById(R.id.textView10);

        TextView    textView12  = (TextView)findViewById(R.id.textView12);
        TextView    tv_TotalTax  = (TextView)findViewById(R.id.tv_TotalTax);

        TextView    tv_Tax_Caption  = (TextView)findViewById(R.id.tv_Tax_Caption);



        TextView textView11 =(TextView)findViewById(R.id.textView11);
        TextView tv_Total =(TextView)findViewById(R.id.tv_Total);
        TextView textView86 =(TextView)findViewById(R.id.textView86);
        TextView tv_Disc =(TextView)findViewById(R.id.tv_Disc);


        textView12.setVisibility(View.VISIBLE);
        tv_TotalTax.setVisibility(View.VISIBLE);
        textView11.setVisibility(View.VISIBLE);
        tv_Total.setVisibility(View.VISIBLE);
        textView86.setVisibility(View.VISIBLE);
        tv_Disc.setVisibility(View.VISIBLE);

         if (ShowTax.equals("0"))
        {
            textView10.setText("");
            textView12.setText("");
            tv_TotalTax.setText("");
           // tv_TaxAcc.setText("");
          //  tv_Tax_Caption.setText("");



        }


        sqlHandler = new SqlHandler(this);
        ShowRecord (getIntent().getStringExtra("OrderNo"));
        mButton = (Button) findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                /*if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                }*/

                // mBluetoothAdapter.enable();

                LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);
                if (PrinterType.equals("1")) {

                    if (Company.equals("1") || Company.equals("2")) {
                        PrintReport_SEWOO_ESCPOS ObjPrint = new PrintReport_SEWOO_ESCPOS(Convert_Sal_Invoice_To_ImgActivity.this,
                                Convert_Sal_Invoice_To_ImgActivity.this, lay, 570, 1);
                        ObjPrint.ConnectToPrinter();

                    }
                    /*if (Company.equals("2")) {
                        PrintReport_SEWOO_ESCPOS ObjPrint = new PrintReport_SEWOO_ESCPOS(Convert_Sal_Invoice_To_ImgActivity.this,
                                Convert_Sal_Invoice_To_ImgActivity.this, lay, 200, 1);
                        ObjPrint.ConnectToPrinter();
                    }*/
                }

                if (PrinterType.equals("2")) {
                    PrintReport_Zepra520 obj =  new PrintReport_Zepra520(Convert_Sal_Invoice_To_ImgActivity.this,
                            Convert_Sal_Invoice_To_ImgActivity.this,lay,560,1);
                    obj.DoPrint();
                }

            //    ObjPrint.CopyCount = 2 ;


            //mBluetoothAdapter.disable();

        }
    });
        mBluetoothAdapter.enable();
 }

    @SuppressLint("Range")
    public  void ShowRecord(String OrdNo){


        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.BLACK);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(2);


        // Assign the created border to EditText widget


        TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusname);
        TextView et_Date =(TextView)findViewById(R.id.ed_date);
        TextView tv_Disc =(TextView)findViewById(R.id.tv_Disc);
        TextView tv_NetTotal =(TextView)findViewById(R.id.tv_NetTotal);
        TextView tv_TotalTax =(TextView)findViewById(R.id.tv_TotalTax);
        TextView tv_Total =(TextView)findViewById(R.id.tv_Total);

        TextView textView11 =(TextView)findViewById(R.id.textView11);
        TextView textView12 =(TextView)findViewById(R.id.textView12);
        TextView textView86 =(TextView)findViewById(R.id.textView86);
        TextView textView88 =(TextView)findViewById(R.id.textView88);




        TextView textView5 =(TextView)findViewById(R.id.textView5);
        TextView textView7 =(TextView)findViewById(R.id.textView7);
        TextView textView8 =(TextView)findViewById(R.id.textView8);
        TextView textView9 =(TextView)findViewById(R.id.textView9);
        TextView textView43 =(TextView)findViewById(R.id.textView43);
        TextView textView10 =(TextView)findViewById(R.id.textView10);
        TextView tv_invoc_Type =(TextView)findViewById(R.id.tv_invoc_Type);
        TextView textView58 =(TextView)findViewById(R.id.textView58);
        TextView tv_itemCount =(TextView)findViewById(R.id.tv_itemCount);
        TextView textView107 =(TextView)findViewById(R.id.textView107);
        TextView tv_includetax =(TextView)findViewById(R.id.tv_includetax);




        textView5.setBackground(shape);
        textView7.setBackground(shape);
        textView8.setBackground(shape);
        textView9.setBackground(shape);
        textView43.setBackground(shape);
        textView10.setBackground(shape);


        textView11.setBackground(shape);
        textView12.setBackground(shape);
        textView86.setBackground(shape);
        textView88.setBackground(shape);
        textView58.setBackground(shape);

        tv_Total.setBackground(shape);
        tv_TotalTax.setBackground(shape);
        tv_NetTotal.setBackground(shape);
        tv_Disc.setBackground(shape);


        if (ShowTax.equals("0")) {
            textView12.setVisibility(View.INVISIBLE);
            tv_TotalTax.setVisibility(View.INVISIBLE);
            textView11.setVisibility(View.INVISIBLE);
            tv_Total.setVisibility(View.INVISIBLE);
            textView86.setVisibility(View.INVISIBLE);
            tv_Disc.setVisibility(View.INVISIBLE);

        }
        /*tv_itemCount.setBackground(shape);
        tv_itemCount.setBackground(shape);
        tv_itemCount.setBackground(shape);
        tv_itemCount.setBackground(shape);

        textView107.setBackground(shape);
        textView107.setBackground(shape);
        textView107.setBackground(shape);
        textView107.setBackground(shape);*/



        String q = "Select distinct s.include_Tax, s.inovice_type , s.Total , s.Nm,  s.disc_Total, s.OrderNo,s.Net_Total,s.Tax_Total ,s.acc ,s.date , c.name  " +
                "    from  Sal_invoice_Hdr s left join Customers c on c.no =s.acc   where s.OrderNo = '"+OrdNo+"'";

        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                et_Date.setText(c1.getString(c1.getColumnIndex("date")));
                tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));
                if(c1.getString(c1.getColumnIndex("inovice_type")).equals("0")){
                    tv_cusnm.setText(c1.getString(c1.getColumnIndex("Nm")).toString());

                }
                else
                {
                    tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));
                }
                tv_Disc.setText(c1.getString(c1.getColumnIndex("disc_Total")));
                tv_NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")));
                tv_TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")));

                if (c1.getString(c1.getColumnIndex("inovice_type")).toString().equals("0"))
                {
                    tv_invoc_Type.setText("نقدية");

                }
                else
                {
                    tv_invoc_Type.setText("ذمم");
                }

                if (ShowTax.equals("0")) {
                    tv_TotalTax.setText("");

                }
                if (c1.getString(c1.getColumnIndex("include_Tax")).toString().equals("-1")) {
                    tv_includetax.setText("");


                }


            /*    //if (c1.getString(c1.getColumnIndex("include_Tax")).toString().equals("0")) {
                ShowTax=getIntent().getStringExtra("ShowTax");
              //  if (ShowTax.equals("0")) {
                        textView12.setVisibility(View.INVISIBLE);
                        tv_TotalTax.setVisibility(View.INVISIBLE);
                        textView11.setVisibility(View.INVISIBLE);
                        tv_Total.setVisibility(View.INVISIBLE);
                        textView86.setVisibility(View.INVISIBLE);
                      tv_Disc.setVisibility(View.INVISIBLE);

             //   }
*/

                tv_Total.setText(c1.getString(c1.getColumnIndex("Total")));
            }
        }
      showList(OrdNo);
        c1.close();
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
    @SuppressLint("Range")
    private void showList(String OrderNo) {
        Intent i = getIntent();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1") ;

        ArrayList<ContactListItems> contactList = new ArrayList<ContactListItems>();
        contactList.clear();
        sqlHandler = new SqlHandler(this);
        String query = "SELECT  distinct sid.itemNo,sid.OrgPrice ,sid.price,sid.tax,u.UnitName  , sid.tax_Amt,  ( ifnull(sid.qty,0) +  ifnull(sid.Pro_bounce,0) + ifnull(bounce_qty,0) )  as qty  ,invf.Item_Name  ,  sid.total    " +
                " , ifnull(sid.Pro_dis_Per,0) as Pro_dis_Per ,ifnull(sid.dis_Amt,0) as dis_Amt     FROM Sal_invoice_Det   sid    Left Join Unites u on u.Unitno =sid.unitNo " +
                "Left Join invf on   invf.Item_No=sid.itemNo  where sid.OrderNo =  '"+  i.getStringExtra("OrderNo").toString()+"'";

        Double Pro = 0.0 ;
        Double Dis_Amt = 0.0 ;
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemNo")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    if (ShowTax.equals("0")) {
                        contactListItems.setprice(c1.getString(c1
                                .getColumnIndex("OrgPrice")));
                    }else {
                        contactListItems.setprice(c1.getString(c1
                                .getColumnIndex("price")));

                    }

                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax_Amt")));
                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));

                    contactListItems.setPro_dis_Per(c1.getString(c1
                            .getColumnIndex("Pro_dis_Per")));


                    Pro = Pro + (SToD(c1.getString(c1.getColumnIndex("Pro_dis_Per"))) / 100) * (SToD(c1.getString(c1.getColumnIndex("price"))) * SToD(c1.getString(c1.getColumnIndex("qty"))));
                    Dis_Amt = Dis_Amt + SToD(c1.getString(c1.getColumnIndex("dis_Amt")));
                    contactList.add(contactListItems);


                } while (c1.moveToNext());


            }



            c1.close();
        }

            TextView tv_Dis_Pro_amt =(TextView)findViewById(R.id.tv_Dis_Pro_amt);
            tv_Dis_Pro_amt.setText(SToD(Pro.toString()) + "");

        TextView tv_Dis_amt =(TextView)findViewById(R.id.tv_Dis_amt);
        tv_Dis_amt.setText(SToD(Dis_Amt.toString()) + "");



            LinearLayout Sal_ItemSLayout = (LinearLayout) findViewById(R.id.Sal_ItemSLayout);

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;

            TextView tv_no;
            TextView tv_name;
            TextView tv_Price;
            TextView tv_Qty;
            TextView tv_Unit;
            TextView tv_tax;
            TextView tv_total;



            TextView tv_itemCount =(TextView)findViewById(R.id.tv_itemCount);
              tv_itemCount.setText(contactList.size()+"");
            for (ContactListItems Obj : contactList){

                    view = inflater.inflate(R.layout.sal_invoce_row, null);

                /*if  (Company.equals("2")) {
                    view = inflater.inflate(R.layout.sal_invoce_row_1, null);
                }*/
                tv_no = (TextView) view.findViewById(R.id.tv_no);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_Qty = (TextView) view.findViewById(R.id.tv_Qty);
                tv_Price = (TextView) view.findViewById(R.id.tv_Price);
                tv_Unit = (TextView) view.findViewById(R.id.tv_Unit);
                tv_tax = (TextView) view.findViewById(R.id.tv_tax);
                tv_total = (TextView) view.findViewById(R.id.tv_total);




                tv_no.setText(Obj.getno());
                tv_name.setText(Obj.getName());
                tv_Price.setText(Obj.getprice());
                tv_Qty.setText(Obj.getQty());
                tv_Unit.setText(Obj.getUnite() );
                tv_tax.setText(Obj.getTax());
                tv_total.setText(Obj.getTotal());
                tv_tax.setText(Obj.getTax());
                tv_tax.setVisibility(View.VISIBLE);
                if (ShowTax.equals("0")) {
                    tv_tax.setText("");
                    tv_tax.setVisibility(View.INVISIBLE);

                }
                Sal_ItemSLayout.addView(view);

            }



           TextView textView130 = (TextView)findViewById(R.id.textView130);


            ArrayList<Cls_Offers_Hdr> Offer_Header_List = new ArrayList<Cls_Offers_Hdr>();
            Offer_Header_List.clear();

              query = " Select  distinct  Offer_Name, Offer_Date, Offer_Type  from  Offers_Hdr  ";

            c1 = sqlHandler.selectQuery(query);
        textView130.setText("");
            if (c1 != null && c1.getCount() != 0) {
                textView130.setText("العروض");
                if (c1.moveToFirst()) {
                    do {

                        Cls_Offers_Hdr obj = new Cls_Offers_Hdr();

                        obj.setOffer_Name(c1.getString(c1
                                .getColumnIndex("Offer_Name")));
                        obj.setOffer_Date(c1.getString(c1
                                .getColumnIndex("Offer_Date")));
                        obj.setOffer_Type(c1.getString(c1
                                .getColumnIndex("Offer_Type")));
                        Offer_Header_List.add(obj);

                    } while (c1.moveToNext());
                }
                c1.close();
            }

        LinearLayout Promotion_ItemSLayout = (LinearLayout) findViewById(R.id.Promotion_ItemSLayout);

        LayoutInflater Promotion_inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Promotion_view;



        for (Cls_Offers_Hdr Obj : Offer_Header_List){

            Promotion_view = Promotion_inflater.inflate(R.layout.sal_inv_pro_row ,null);
            tv_name = (TextView) Promotion_view.findViewById(R.id.tv_name);

            tv_name.setText(Obj.getOffer_Name());

            Promotion_ItemSLayout.addView(Promotion_view);
        }
    }

    public void btn_back(View view) {
        Intent i =  new Intent(this,Sale_InvoiceActivity.class);
         startActivity(i);
    }
}
