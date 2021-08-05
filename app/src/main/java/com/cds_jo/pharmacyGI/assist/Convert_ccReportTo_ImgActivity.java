package com.cds_jo.pharmacyGI.assist;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cds_jo.pharmacyGI.DB;
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
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.itextpdf.text.PageSize.A4;

public class Convert_ccReportTo_ImgActivity extends AppCompatActivity {
    private ESCPOSPrinter posPtr;
    SqlHandler sqlHandler;

    ListView lvCustomList;

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
        final String Company = sharedPreferences.getString("CompanyID", "1");
        final String PrinterType = sharedPreferences.getString("PrinterType", "1");
        setContentView(R.layout.activity_convert_cc_report_to__img);
        img_Logo = (ImageView) findViewById(R.id.img_Logo);
        File imgFile = new File("//sdcard/Android/Cv_Images/logo.jpg");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_Logo.setImageBitmap(myBitmap);
            }
        } catch (Exception ex) {
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm ", Locale.ENGLISH);
         TextView tv_PrintTime =(TextView)findViewById(R.id.tv_PrintTime);
        tv_PrintTime.setText(  " تاريخ ووقت الطباعة : "+sdf.format(new Date()) +" ");
        Intent i = getIntent();
        mView = findViewById(R.id.f_view);


        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.
                getDefaultAdapter();


        String u = sharedPreferences.getString("UserName", "");
        String UserID = sharedPreferences.getString("UserID", "");


        String Mobile = "";
        Mobile = DB.GetValue(Convert_ccReportTo_ImgActivity.this, "manf", "Mobile1", "man='" + UserID + "'");
        if (Mobile.equalsIgnoreCase("")) {
            Mobile = "";
        }


        TextView tv_CompName = (TextView) findViewById(R.id.tv_CompName);
        tv_CompName.setText(sharedPreferences.getString("CompanyNm", ""));

        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());


        TextView tv_footer = (TextView) findViewById(R.id.tv_footer);
        TextView tv_footer1 = (TextView) findViewById(R.id.tv_footer1);


        sqlHandler = new SqlHandler(this);
        ShowRecord("");




    }

    public void ShowRecord(String OrdNo) {


       /* ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.BLACK);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(2);





        TextView tv_cusnm =(TextView)findViewById(R.id.tv_cusname);
        TextView et_Date =(TextView)findViewById(R.id.ed_date);
        TextView tv_Disc =(TextView)findViewById(R.id.tv_Disc);
        TextView tv_NetTotal =(TextView)findViewById(R.id.tv_NetTotal);
        TextView tv_TotalTax =(TextView)findViewById(R.id.tv_TotalTax);


        String q = "Select distinct s.include_Tax, s.inovice_type , s.Total , s.Nm,  s.disc_Total, s.OrderNo,s.Net_Total,s.Tax_Total ,s.acc ,s.date , c.name  " +
                "    from  Sal_invoice_Hdr s left join Customers c on c.no =s.acc   where s.OrderNo = '"+OrdNo+"'";

        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                et_Date.setText(c1.getString(c1.getColumnIndex("date")));
                tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));
                             tv_cusnm.setText(c1.getString(c1.getColumnIndex("name")));

                tv_Disc.setText(c1.getString(c1.getColumnIndex("disc_Total")));
                tv_NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")));
                tv_TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")));





            }
            c1.close();  }*/
        showList(OrdNo);

    }

    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }

    @SuppressLint("Range")
    private void showList(String OrderNo) {
        Intent i = getIntent();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String Company = sharedPreferences.getString("CompanyID", "1");

        ArrayList<Cls_Acc_Report> contactList = new ArrayList<Cls_Acc_Report>();
        contactList.clear();
        sqlHandler = new SqlHandler(this);
        String query = "SELECT   Tot, Dept,Des,Date, ifnull(Cred,0) as Cred ,FDate,TDate  ,Cust_Nm from ACC_REPORT    ";


        // "Left Join invf on   invf.Item_No=sid.itemNo  where sid.OrderNo =  '"+  i.getStringExtra("OrderNo").toString()+"'";
        TextView tv_cusnm = (TextView) findViewById(R.id.tv_cusname);
        TextView tv_fdate = (TextView) findViewById(R.id.tv_fdate);
        TextView tv_tdate = (TextView) findViewById(R.id.tv_tdate);

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_cusnm.setText(" " +c1.getString(c1
                        .getColumnIndex("Cust_Nm"))+"  المحترمين ");
                tv_fdate.setText(c1.getString(c1
                        .getColumnIndex("FDate")));
                tv_tdate.setText(c1.getString(c1
                        .getColumnIndex("TDate")));
                do {
                    Cls_Acc_Report contactListItems = new Cls_Acc_Report();

                    if (c1.getString(c1.getColumnIndex("Date")).equalsIgnoreCase("عدد الحركات")) {
                        contactListItems.setDes(c1.getString(c1.getColumnIndex("Des")));
                    } else {
                        contactListItems.setDes(c1.getString(c1
                                .getColumnIndex("Des")) + "\n\r   " + c1.getString(c1.getColumnIndex("Date")));
                    }

                    contactListItems.setDate(c1.getString(c1
                            .getColumnIndex("Date")));
                    contactListItems.setDes(c1.getString(c1
                            .getColumnIndex("Des")));
                    contactListItems.setCred(c1.getString(c1
                            .getColumnIndex("Cred")));
                    contactListItems.setDept(c1.getString(c1
                            .getColumnIndex("Dept")));
                    contactListItems.setTot(c1.getString(c1
                            .getColumnIndex("Tot")));

                    contactList.add(contactListItems);


                } while (c1.moveToNext());


            }


            c1.close();
        }


        LinearLayout Sal_ItemSLayout = (LinearLayout) findViewById(R.id.Sal_ItemSLayout);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;

        TextView tv_Des;
        TextView Cred;
        TextView Dept;
        TextView Tot,tv_Date;

        for (Cls_Acc_Report Obj : contactList) {

            view = inflater.inflate(R.layout.report_img_row, null);

            tv_Des = (TextView) view.findViewById(R.id.tv_Des);
            Cred = (TextView) view.findViewById(R.id.tv_Cred);
            Dept = (TextView) view.findViewById(R.id.tv_Dept);
            Tot = (TextView) view.findViewById(R.id.tv_Tot);
            tv_Date = (TextView) view.findViewById(R.id.tv_Date);


            tv_Des.setText(Obj.getDes());
            Cred.setText(Obj.getCred());
            Dept.setText(Obj.getDept());
            Tot.setText(Obj.getTot());
            tv_Date.setText(Obj.getDate());

            Sal_ItemSLayout.addView(view);

        }


    }

    public void btn_share(View view) {


        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);


        PrintReport_Zepra520 obj = new PrintReport_Zepra520(Convert_ccReportTo_ImgActivity.this,
                Convert_ccReportTo_ImgActivity.this, lay, 550, 1);
        obj.StoreImage();



        PackageManager pm = this.getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            File imageFileToShare = new File("//sdcard/z1.jpg");
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

    public void btn_back(View view) {
        Intent i =  new Intent(this,Acc_ReportActivity.class);
        startActivity(i);
    }
    public static Uri saveImageToInternalStorage(Context mContext, Bitmap bitmap){

        String mTimeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());

        String mImageName = "snap_"+mTimeStamp+".jpg";

        ContextWrapper wrapper = new ContextWrapper(mContext);

        File file = wrapper.getDir("Images",MODE_PRIVATE);

        file = new File(file, "snap_"+ mImageName+".jpg");

        try{

            OutputStream stream = null;

            stream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            stream.flush();

            stream.close();

        }catch (IOException e)
        {
            e.printStackTrace();
        }

        Uri mImageUri = Uri.parse(file.getAbsolutePath());

        return mImageUri;
    }


    private  void StoreImage(){
        LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);

        Bitmap b = loadBitmapFromView(lay);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z1.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();
            //  bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Bitmap loadBitmapFromView(View v) {


        v.measure(View.MeasureSpec.makeMeasureSpec(v.getLayoutParams().width,
                View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                v.getLayoutParams().height, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(500, v.getHeight(),
                Bitmap.Config.ARGB_8888);

     /*   Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);*/
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
    private void pdf1()  {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(A4,0,0,0,0);
        try {
            String targetPdf = "/sdcard/Statement_of_Account.pdf";

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


            PrintReport_Zepra520 obj = new PrintReport_Zepra520(Convert_ccReportTo_ImgActivity.this,
                    Convert_ccReportTo_ImgActivity.this, lay, 550, 1);
            obj.StoreImage();
            pdf1();
            String targetPdf = "/sdcard/Statement_of_Account.pdf";
            ArrayList<Uri> uris = new ArrayList<Uri>();
            File fileIn = new File(targetPdf);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
            String email, subject, message, attachmentFile;
            // Customer_email = "maen.naamneh@yahoo.com";
            email = "";//"m.naamneh@gi-group.com";
            subject ="كشف حساب العميل";
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
}
