package com.cds_jo.pharmacyGI.assist;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.itextpdf.text.PageSize.A4;

public class Convert_RecVouch_To_Img extends AppCompatActivity {
    private ESCPOSPrinter posPtr;
    private View mView;
    SqlHandler sqlHandler ;
    ListView lvCustomList;
    private Button mButton;
    private Context context;
    ImageView img_Logo,imgSig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String Company = sharedPreferences.getString("CompanyID", "1") ;

            setContentView(R.layout.activity_convert__rec_vouch__to__img);
        File SigFile = new  File("//sdcard/Android/Cv_Images/logo.jpg");
        imgSig = (ImageView) findViewById(R.id.imgSig);
        SigFile = new  File("//sdcard//Android/Cv_Images/Vo_Sig/"+getIntent().getStringExtra("OrderNo").toString() +".png");
        try {
            if (SigFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(SigFile.getAbsolutePath());
                imgSig.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}
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

        mBluetoothAdapter.enable();
    }

    @SuppressLint("Range")
    public  void ShowRecord(String OrdNo){
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

    private void pdf1()  {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(A4,0,0,0,0);
        try {
            String targetPdf = "/sdcard/Receipt.pdf";

            PdfWriter.getInstance(document, new FileOutputStream(targetPdf)); //  Change pdf's name.
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
        com.itextpdf.text.Image img1 = null;
        try {
            img1 = com.itextpdf.text.Image.getInstance("//sdcard//z1.jpg");

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(img1!=null  ) {
                float scaler1 = ((document.getPageSize().getWidth() - 0
                        - document.rightMargin() - 0) / img1.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                img1.scalePercent(60);
                img1.setAlignment(com.itextpdf.text.Image.BOTTOM | com.itextpdf.text.Image.ALIGN_CENTER);
                //  img1.scaleToFit(700,3000);
                document.add(img1);

            }




        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();


    }
    public void Send_Email(View view) {
        try {
            LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);


            PrintReport_Zepra520 obj = new PrintReport_Zepra520(Convert_RecVouch_To_Img.this,
                    Convert_RecVouch_To_Img.this, lay, 550, 1);
            obj.StoreImage();
            pdf1();
            String targetPdf = "/sdcard/Receipt.pdf";
            ArrayList<Uri> uris = new ArrayList<Uri>();
            File fileIn = new File(targetPdf);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
            String email, subject, message, attachmentFile;
            // Customer_email = "maen.naamneh@yahoo.com";
            email = "";//; "maen.naamneh@yahoo.com";
            subject ="سند قبض";
            message = "";// tv_sig.getText().toString();
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, email);
            intent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    new String[]{email});
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, message);

            intent.setType("text/plain");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") ||
                        info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
            if (best != null)
                intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

            //   startActivity(intent);
            startActivity(Intent.createChooser(intent, "الرجاء اختيار البرنامج "));

        } catch (Throwable t) {
            Toast.makeText(this,
                    "Request failed try again: " + t.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
    public void btn_back(View view) {
        super.onBackPressed();
    }

    public void btn_share(View view) {


        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);


        PrintReport_Zepra520 obj = new PrintReport_Zepra520(Convert_RecVouch_To_Img.this,
                Convert_RecVouch_To_Img.this, lay, 550, 1);
        try {
            obj.StoreImage();
        } catch (IOException e) {
            e.printStackTrace();
        }


        PackageManager pm = this.getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            File imageFileToShare = new File("//sdcard/gg.jpg");
            Uri uri2 = Uri.fromFile(imageFileToShare);
            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.setPackage("com.whatsapp");
            // waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            // waIntent.putExtra("jid", "962796892140" + "@s.whatsapp.net"); //phone number witho
            waIntent.putExtra(Intent.EXTRA_TEXT, "تمت الطباعة من خلال نظام المبيعات المحمول");
            waIntent.putExtra(Intent.EXTRA_STREAM, uri2);
            this.startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(this, "الرجاء تثبيت الواتس اب من المتجر", Toast.LENGTH_SHORT).show();
        }
    }

}
