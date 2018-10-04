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

import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Convert_RecVouch_To_Img extends AppCompatActivity {
    private ESCPOSPrinter posPtr;
    private View mView;
    SqlHandler sqlHandler ;
    ListView lvCustomList;
    private Button mButton;
    private Context context;
    ImageView img_Logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String Company = sharedPreferences.getString("CompanyID", "1") ;
        // 1 Dell
        // 2 Lenovo
        // 3 Samsung

       if  (Company.equals("1")){
            setContentView(R.layout.activity_convert__rec_vouch__to__img);
        }

        if  (Company.equals("2")){
            setContentView(R.layout.activity_convert__rec_vouch__to__img);
        }

        img_Logo = (ImageView) findViewById(R.id.img_Logo);
        File imgFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
       try {
           if (imgFile.exists()) {
               Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
               img_Logo.setImageBitmap(myBitmap);
           }
       }
       catch (Exception ex){}

        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();
        mBluetoothAdapter.disable();
        Intent i = getIntent();
        View test1View = findViewById(R.id.f_view);

        String u =  sharedPreferences.getString("UserName", "");
        TextView    tv_UserNm  = (TextView)findViewById(R.id.tv_UserNm);
        tv_UserNm.setText(u);


        TextView tv_CompName =(TextView)findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));

        final String PrinterType = sharedPreferences.getString("PrinterType", "1") ;
        String OrderNo = getIntent().getStringExtra("OrderNo");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


       ShowRecord (OrderNo);

        sqlHandler = new SqlHandler(this);

        mButton = (Button) findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);

                if (PrinterType.equals("1")) {
                    if (Company.equals("1")) {
                        PrintReport_SEWOO_ESCPOS ObjPrint = new PrintReport_SEWOO_ESCPOS(Convert_RecVouch_To_Img.this,
                                Convert_RecVouch_To_Img.this, lay, 570, 1);
                        ObjPrint.ConnectToPrinter();
                    }

                    if (Company.equals("2")) {
                        PrintReport_SEWOO_ESCPOS ObjPrint = new PrintReport_SEWOO_ESCPOS(Convert_RecVouch_To_Img.this,
                                Convert_RecVouch_To_Img.this, lay, 200, 1);
                        ObjPrint.ConnectToPrinter();
                    }

                }

                if (PrinterType.equals("2")) {
                    PrintReport_Zepra520 obj =  new PrintReport_Zepra520(Convert_RecVouch_To_Img.this,
                            Convert_RecVouch_To_Img.this,lay,570,1);
                    obj.DoPrint();
                }
        }
    });
        mBluetoothAdapter.enable();
    }

    public  void ShowRecord( String OrdNo){
         TextView OrderNo = (TextView)findViewById(R.id.et_OrdeNo);
        TextView amt =(TextView)findViewById(R.id.et_Amt);
        TextView etNote =(TextView)findViewById(R.id.et_notes);
        TextView et_Date =(TextView)findViewById(R.id.et_Date);
        TextView tv_acc =(TextView)findViewById(R.id.tv_acc);
        TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusnm);
        TextView cash =(TextView)findViewById(R.id.tv_Cash);
        TextView tv_Cur =(TextView)findViewById(R.id.tv_Cur);
        TextView tv_type =(TextView)findViewById(R.id.tv_type);
        TextView tv_Checktotal =(TextView)findViewById(R.id.tv_Checktotal);


        OrderNo.setText(OrdNo);


        String q ="Select  ifnull(CheckTotal,0) as CheckTotal ,   ifnull( rc.cash ,0) as Cash, rc.VouchType,rc.curno  ,curf.cur    ,  rc.Desc,rc.Amnt,rc.TrDate,rc.CustAcc  ,c.name from RecVoucher rc   " +
                "left join Customers c on c.no = rc.CustAcc   Left join curf on curf.cur_no = rc.curno " +
                "where rc.DocNo = '"+ OrdNo +"'";
        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                etNote.setText(c1.getString(c1.getColumnIndex("Desc")));
                amt.setText(c1.getString(c1.getColumnIndex("Amnt")));
                et_Date.setText(c1.getString(c1.getColumnIndex("TrDate")));
                tv_acc.setText(c1.getString(c1.getColumnIndex("CustAcc")));
                tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));
                cash.setText(c1.getString(c1.getColumnIndex("Cash")));
                tv_Cur.setText(c1.getString(c1.getColumnIndex("cur")));
                tv_Checktotal.setText(c1.getString(c1.getColumnIndex("CheckTotal")));


                 if (c1.getString(c1.getColumnIndex("VouchType")).equals("1")){
                    tv_type.setText("نقدي");
                }
               else if (c1.getString(c1.getColumnIndex("VouchType")).equals("2")){
                    tv_type.setText("شيكات");
                }

                else if (c1.getString(c1.getColumnIndex("VouchType")).equals("3")){
                    tv_type.setText(" نقدي + شيكات");
                }


            }


        c1.close();
        }
        showList();
    }

    private void showList( ) {

        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        ArrayList<Cls_Check> contactList = new ArrayList<Cls_Check>();
        contactList.clear();
        sqlHandler = new SqlHandler(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1") ;


        String query ="Select rc.CheckNo,rc.CheckDate,rc.BankNo, rc.Amnt  , b.Bank from  RecCheck rc  left join banks b on b.bank_num = rc.BankNo where DocNo ='"+ DocNo.getText().toString()+"'";
        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Check cls_check_obj = new Cls_Check();
                    cls_check_obj.setSer(i);
                    cls_check_obj.setCheckNo(c1.getString(c1
                            .getColumnIndex("CheckNo")));
                    cls_check_obj.setCheckDate(c1.getString(c1
                            .getColumnIndex("CheckDate")));
                    cls_check_obj.setBankName(c1.getString(c1
                            .getColumnIndex("Bank")));
                    cls_check_obj.setAmnt(c1.getString(c1
                            .getColumnIndex("Amnt")));

                    contactList.add(cls_check_obj);
                    i++;

                } while (c1.moveToNext());


            }


        c1.close();
    }

        LinearLayout ChecksLayout = (LinearLayout) findViewById(R.id.ChecksLayout);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        TextView tv_Amnt;
        TextView tv_BankNm;
        TextView tv_CheckDate;
        TextView tv_Checkno;
        TextView Chkser;

        for (Cls_Check Obj : contactList){


            view = inflater.inflate(R.layout.check_print_list_row ,null);
            if  (Company.equals("2")) {
                view = inflater.inflate(R.layout.check_print_list_row_1, null);
            }



            tv_Amnt = (TextView) view.findViewById(R.id.tv_Amnt);
            tv_BankNm = (TextView) view.findViewById(R.id.tv_BankNm);
            tv_CheckDate = (TextView) view.findViewById(R.id.tv_CheckDate);
            tv_Checkno = (TextView) view.findViewById(R.id.tv_Checkno);
            Chkser = (TextView) view.findViewById(R.id.ser);
        try {
            tv_Amnt.setText(Obj.getAmnt());
            tv_BankNm.setText(Obj.getBankName());
            tv_CheckDate.setText(Obj.getCheckDate());
            tv_Checkno.setText(Obj.getCheckNo());
            Chkser.setText(Obj.getSer() + "");
             ChecksLayout.addView(view);
            }

            catch (Exception ex){

            }

        }

    }


    public void btn_back(View view) {
        super.onBackPressed();
    }
}
