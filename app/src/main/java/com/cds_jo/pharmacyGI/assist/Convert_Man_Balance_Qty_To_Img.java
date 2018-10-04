package com.cds_jo.pharmacyGI.assist;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.Cls_Man_Balanc;

import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Convert_Man_Balance_Qty_To_Img extends AppCompatActivity {
    SqlHandler sqlHandler;
    ListView lvCustomList;
    private Button mButton;
    private View mView;
    private Button mButton2;
    TextView tv_orderno ;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print=new ESCPSample3();
    ImageView img_Logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_convert_man_balance_qty);
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
        Intent i = getIntent();
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();
        mBluetoothAdapter.disable();
        tv_orderno = (TextView)findViewById(R.id.tv_orderno);
        tv_orderno.setText( i.getStringExtra("OrderNo").toString());

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

    private void showList() {

        ArrayList<Cls_Man_Balanc> contactList = new ArrayList<Cls_Man_Balanc>();
        contactList.clear();
        Intent i = getIntent();

        String query      = "  select Unites.UnitName,  invf.Item_Name, pod.Item_No,pod.Qty,pod.ActQty,pod.Diff ,pod.Unit_No from BalanceQty pod left join invf on invf.Item_No =  pod.Item_No    left join Unites on Unites.Unitno=  pod.Unit_No " +
                " Where pod.OrderNo='"  +  i.getStringExtra("OrderNo").toString()+ "'";




        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Man_Balanc contactListItems = new Cls_Man_Balanc();

                    contactListItems.setItemno(c1.getString(c1
                            .getColumnIndex("Item_No")));
                    contactListItems.setItem_Name(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setUnitName(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("Qty")));
                    contactListItems.setAct_Aty(c1.getString(c1
                            .getColumnIndex("ActQty")));
                    contactListItems.setDiff(c1.getString(c1
                            .getColumnIndex("Diff")));

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
        TextView tv_Unit ,tv_ActQty ,tv_Diff;



        for (Cls_Man_Balanc Obj : contactList) {
            view = inflater.inflate(R.layout.man_balance_row, null);
             tv_no = (TextView) view.findViewById(R.id.tv_no);
            tv_name = (TextView) view.findViewById(R.id.tv_name);

            tv_Qty = (TextView) view.findViewById(R.id.tv_Qty);
            tv_Unit = (TextView) view.findViewById(R.id.tv_Unit);

            tv_ActQty = (TextView) view.findViewById(R.id.tv_ActQty);
            tv_Diff = (TextView) view.findViewById(R.id.tv_Diff);
            tv_no.setText(Obj.getItemno());
            tv_name.setText(Obj.getItem_Name());
            tv_Qty.setText(Obj.getQty());
            tv_Unit.setText(Obj.getUnitName() + "");
            tv_ActQty.setText(Obj.getAct_Aty());
            tv_Diff.setText(Obj.getDiff());

            ChecksLayout.addView(view);

        }
    }


    public void btn_back(View view) {

         Intent k = new Intent(Convert_Man_Balance_Qty_To_Img.this, GalaxyMainActivity.class);
        startActivity(k);
    }


    public void Do_P(View view) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String PrinterType = sharedPreferences.getString("PrinterType", "1") ;
        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);
        if  (PrinterType.equals("1")) {

            PrintReport_SEWOO_ESCPOS ObjPrint = new PrintReport_SEWOO_ESCPOS(Convert_Man_Balance_Qty_To_Img.this,
                    Convert_Man_Balance_Qty_To_Img.this,lay,570,1);
            ObjPrint.ConnectToPrinter();
        }
        if  (PrinterType.equals("2")) {

            PrintReport_Zepra520 obj =  new PrintReport_Zepra520(Convert_Man_Balance_Qty_To_Img.this,
                    Convert_Man_Balance_Qty_To_Img.this,lay,570,1);
            obj.DoPrint();


        }


    }




}
