package com.cds_jo.pharmacyGI.PrintCls;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Hp on 30/05/2016.
 */
public class PrintReport_Zepra520 {
    Context context;
    android.app.Activity Activity;
    String BPrinter_MAC_ID;
    View ReportView;
    float ImageCountFactor;

    PrintReport_Zepra520(Context _context, android.app.Activity _Activity,
                         View _ReportView, int _PageWidth, float _ImageCountFactor) {
        context = _context;
        Activity = _Activity;

        BPrinter_MAC_ID = "00:12:6F:36:7C:B8";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");
        ReportView = _ReportView;
        //PageWidth = _PageWidth;
        ImageCountFactor = _ImageCountFactor;
    }

    public  void DoPrint(){
        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
        PrintImage(myBitmap);


    }

    private  void StoreImage(){
       // LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);

        Bitmap b = loadBitmapFromView(ReportView);
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
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
    private void PrintImage(final Bitmap bitmap) {



        new Thread(new Runnable() {
            public void run() {
                try {
                    Looper.prepare();
                    String BPrinter_MAC_ID ;
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");
                    Connection connection =  new BluetoothConnection(BPrinter_MAC_ID);
                    connection.open();
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);
                    connection.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                     printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 790, bitmap.getHeight(), false);
                    connection.close();

                } catch (ConnectionException e) {
                    e.printStackTrace();
                } finally {
                    Looper.myLooper().quit();
                }
            }
        }).start();

    }

}
