package com.cds_jo.pharmacyGI.assist;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.cds_jo.pharmacyGI.ContactListItems;

import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Convert_TransQty_To_Img extends AppCompatActivity {
    SqlHandler sqlHandler;
    ListView lvCustomList;
    private Button mButton;
    private View mView;
    private Button mButton2;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print=new ESCPSample3();
    ImageView img_Logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_convert_trans_qty);
        img_Logo = (ImageView) findViewById(R.id.img_Logo);

        File imgFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_Logo.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}


        mView = findViewById(R.id.f_view);

        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();
        mBluetoothAdapter.disable();

        Intent i = getIntent();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserName", "");
        TextView    tv_UserNm  = (TextView)findViewById(R.id.tv_UserNm);
        tv_UserNm.setText(u);

        TextView    ed_date  = (TextView)findViewById(R.id.ed_date);

        TextView tv_CompName =(TextView)findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        ed_date.setText(currentDateandTime);



        sqlHandler = new SqlHandler(this);
        showList();
        mBluetoothAdapter.enable();
    }
    private Double GetSaledQtyNotPosted(String ItemNo ){

        SqlHandler sqlHandler = new SqlHandler(Convert_TransQty_To_Img.this);
        String query = "SELECT     (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                "    where   sih.Post = -1  and ui.item_no ='"+ItemNo+"'";


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
    @SuppressLint("Range")
    private void showList() {

        ArrayList<ContactListItems> contactList = new ArrayList<ContactListItems>();
        contactList.clear();

        String  query = "Select ms.ser, ms.docno ,ms.StoreName , ms.itemno , invf.Item_Name   ,Unites.UnitName     ,ms.qty  ,ms.des ,  ms.date    from  ManStore  ms left join invf on invf.Item_No =  ms.itemno    left join Unites on Unites.Unitno=  ms.UnitNo    ";


        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice("0");
                    contactListItems.setQty(String.valueOf(Double.parseDouble(c1.getString(c1.getColumnIndex("qty"))) - GetSaledQtyNotPosted(c1.getString(c1.getColumnIndex("itemno")))));

                    contactListItems.setTax("0");
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));

                    contactListItems.setTotal("0");
                    contactList.add(contactListItems);




                } while (c1.moveToNext());



            }

        c1.close();

    }

        LinearLayout ChecksLayout = (LinearLayout) findViewById(R.id.Sal_ItemSLayout);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        TextView tv_no;
        TextView tv_name;

        TextView tv_Qty;
        TextView tv_Unit;



        for (ContactListItems Obj : contactList) {
            view = inflater.inflate(R.layout.view_trans_qty_row, null);
             tv_no = (TextView) view.findViewById(R.id.tv_no);
            tv_name = (TextView) view.findViewById(R.id.tv_name);

            tv_Qty = (TextView) view.findViewById(R.id.tv_Qty);
            tv_Unit = (TextView) view.findViewById(R.id.tv_Unit);



            tv_no.setText(Obj.getno());
            tv_name.setText(Obj.getName());
            tv_Qty.setText(Obj.getQty());
            tv_Unit.setText(Obj.getUniteNm() + "");

            ChecksLayout.addView(view);

        }
    }


    public void btn_back(View view) {

         Intent k = new Intent(Convert_TransQty_To_Img.this, GalaxyMainActivity.class);
        startActivity(k);
    }


    public void Do_P(View view) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String PrinterType = sharedPreferences.getString("PrinterType", "1") ;
        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);
        if  (PrinterType.equals("1")) {

            PrintReport_SEWOO_ESCPOS ObjPrint = new PrintReport_SEWOO_ESCPOS(Convert_TransQty_To_Img.this,
                    Convert_TransQty_To_Img.this,lay,570,1);
            ObjPrint.ConnectToPrinter();
        }
        if  (PrinterType.equals("2")) {

            PrintReport_Zepra520 obj =  new PrintReport_Zepra520(Convert_TransQty_To_Img.this,
                    Convert_TransQty_To_Img.this,lay,570,1);
            obj.DoPrint();


        }


    }




}
