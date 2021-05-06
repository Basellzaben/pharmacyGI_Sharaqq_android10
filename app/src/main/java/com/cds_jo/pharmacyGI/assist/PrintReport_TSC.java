package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

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
public class PrintReport_TSC {
    Context context;
    android.app.Activity Activity;
    String BPrinter_MAC_ID;
    View ReportView;
    float ImageCountFactor;
    Bitmap bm2=null, bm1=null,bm3 ;

    int h;
    Bitmap Empty_bitmap = null;
    Connection connection;
    public PrintReport_TSC(Context _context, android.app.Activity _Activity,
                           View _ReportView, int _PageWidth, float _ImageCountFactor) {
        context = _context;
        Activity = _Activity;

        BPrinter_MAC_ID = "00:19:0E:A3:5D:58";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");
        ReportView = _ReportView;
        //PageWidth = _PageWidth;
        ImageCountFactor = _ImageCountFactor;

    }
    public  void StoreContent(View v, String file_name){
        Bitmap b = loadBitmapFromView(v);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = file_name ;
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

    public  void StoreFooter(View v){
        Bitmap b = loadBitmapFromView(v);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z3.jpg";
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
    public  void DoPrint(){
        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");


        Toast.makeText(context  ,"العمل جاري على طباعة الملف", Toast.LENGTH_SHORT ).show();

        PrintImage(myBitmap);


    }

    public  void StoreHeader(View v){
        Bitmap b = loadBitmapFromView(v);
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
    public  void DoPrint2Img(){
        StoreImage();
        Bitmap myBitmap = null;
        myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
        Bitmap myBitmap2 = null;
        myBitmap2= BitmapFactory.decodeFile("//sdcard//z2.jpg");
        Bitmap myBitmap21 = null;
        myBitmap21= BitmapFactory.decodeFile("//sdcard//z21.jpg");
         Bitmap myBitmap22 = null;
        myBitmap22= BitmapFactory.decodeFile("//sdcard//z22.jpg");
        Bitmap myBitmap3 = null;
        myBitmap3= BitmapFactory.decodeFile("//sdcard//z3.jpg");
        Toast.makeText(context  ,"العمل جاري على طباعة الملف", Toast.LENGTH_SHORT ).show();


        PrintImage2(myBitmap, myBitmap2,myBitmap21,myBitmap22,myBitmap3);


    }
    public  void DoPrint1(){
        StoreImage1();
        Bitmap myBitmap1 = null;
        myBitmap1= BitmapFactory.decodeFile("//sdcard//Download/Zain.jpg");
        Toast.makeText(context  ,"جاري تحميل الملف  000 ", Toast.LENGTH_SHORT ).show();
       PrintImage_new( myBitmap1);


    }
    private  void StoreEmptyImage(){
        // LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);
        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "e1.jpg";
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
    private  void StoreImage1(){


        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "Zain.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd.getPath()+"/Download", filename);

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
    private  void StoreImage3(){


        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z3.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private  void StoreImage2(){


        Bitmap b = loadBitmapFromView(ReportView);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z2.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

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
  /*  private void PrintImage(final Bitmap bitmap) {




         new Thread(new Runnable() {
           public void run() {
                 try {
                     Looper.prepare();
                     String BPrinter_MAC_ID;
                     SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                     BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");
                     connection = new BluetoothConnection(BPrinter_MAC_ID);

                     connection.open();
                     ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);
                     printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 550, bitmap.getHeight(), false);
                     connection.close();
                 }catch (ConnectionException eq) {
                     Toast.makeText(context  ,eq.getMessage().toString(),Toast.LENGTH_SHORT ).show();

                } catch (Exception e) {
                    Toast.makeText(context  ,e.getMessage().toString(),Toast.LENGTH_SHORT ).show();
                } finally {

                     Looper.myLooper().quit();
                }
            }
        }).start();

    }*/
  public void SendCommand(String txt) {

  }
    public void PrintImage_new(final Bitmap bitmap) {

   try {
     File sd = Environment.getExternalStorageDirectory();
     File dest = new File(sd.getPath()+"/Download", "Zain.jpg");

 }catch ( Exception ex){
     Toast.makeText(context,ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
 }
    }
    private void PrintImage2(final Bitmap bitmap, final Bitmap bitmap2, final Bitmap bitmap21, final Bitmap bitmap22, final Bitmap bitmap3) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Looper.prepare();
                        String BPrinter_MAC_ID;
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                        BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");
                        Connection c2 = new BluetoothConnection(BPrinter_MAC_ID);
                        c2.open();
                        ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, c2);
                        c2.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                        printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 550, bitmap.getHeight(), false);
                        printer.printImage(new ZebraImageAndroid(bitmap2), 20, 0, 550, bitmap2.getHeight(), false);
                        printer.printImage(new ZebraImageAndroid(bitmap21), 20, 0, 550, bitmap21.getHeight(), false);
                        printer.printImage(new ZebraImageAndroid(bitmap22), 20, 0, 550, bitmap22.getHeight(), false);
                        printer.printImage(new ZebraImageAndroid(bitmap3), 20, 0, 550, bitmap3.getHeight(), false);
                        c2.close();

                    } catch (ConnectionException e) {
                        e.printStackTrace();

                    } finally {
                        Looper.myLooper().quit();
                    }
                }
            }).start();

    }
  private void PrintImage(final Bitmap bitmap) {

      if ( bitmap.getHeight()>1707) {
          bm1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), 1707);
          bm2 = Bitmap.createBitmap(bitmap, 0, 1707, bitmap.getWidth(), bitmap.getHeight()-1707  );
          try {

          String filename = "z1.jpg";
          File sd = Environment.getExternalStorageDirectory();
          File dest = new File(sd, filename);


          if(bm1!=null) {
              FileOutputStream out = new FileOutputStream(dest);
              filename = "z1.jpg";
              dest = new File(sd, filename);
              out = new FileOutputStream(dest);
              bm1.compress(Bitmap.CompressFormat.JPEG, 100, out);

          }

          if(bm2!=null) {
              FileOutputStream out = new FileOutputStream(dest);
                  filename = "z1.jpg";
                  dest = new File(sd, filename);
                  out = new FileOutputStream(dest);
                  bm2.compress(Bitmap.CompressFormat.JPEG, 100, out);

           }
          } catch (Exception e) {
              Toast.makeText(context, e.getMessage() + "  Exception   ", Toast.LENGTH_SHORT).show();
          }


          new Thread(new Runnable() {
              public void run() {
                  try {


                      Looper.prepare();
                      String BPrinter_MAC_ID;
                      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                      BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");

                      Connection connection = new BluetoothConnection(BPrinter_MAC_ID);
                      connection.open();

                     // ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);


                     /* printer.printImage(new ZebraImageAndroid(bm1), 20, 0, 550, bm1.getHeight(), false);
                      printer.printImage(new ZebraImageAndroid(bm2), 20, 0, 550, bm2.getHeight(), false);*/
                      //connection.close();

                  } catch (ConnectionException e) {
                      e.printStackTrace();
                  } finally {
                      Looper.myLooper().quit();
                  }
              }
          }).start();

      }else {

          h = bitmap.getHeight();
          if (h > 1800) {
              h = 1800;
          }
          Toast.makeText(context, h + "  h   ", Toast.LENGTH_SHORT).show();
          new Thread(new Runnable() {
              public void run() {
                  try {


                      Looper.prepare();
                      String BPrinter_MAC_ID;
                      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                      BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");

                      Connection connection = new BluetoothConnection(BPrinter_MAC_ID);
                      connection.open();

                      ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);
                      connection.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                      printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 550, bitmap.getHeight(), false);
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
    public void PrintImageEmpty(final Bitmap bitmap ) {

       new Thread(new Runnable() {

            public void run() {
                try {

                     Looper.prepare();
                    String BPrinter_MAC_ID;
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    BPrinter_MAC_ID = sharedPreferences.getString("AddressBT", "");

                    connection = new BluetoothConnection(BPrinter_MAC_ID);

                    connection.open();
                    connection.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                    connection.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);
                    printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 550, bitmap.getHeight(), false);
                     connection.close();
                }catch (ConnectionException eq) {
                    Toast.makeText(context  ,eq.getMessage().toString(), Toast.LENGTH_SHORT ).show();

                } catch (Exception e) {
                    Toast.makeText(context  ,e.getMessage().toString(), Toast.LENGTH_SHORT ).show();
                } finally {

                   Looper.myLooper().quit();
                }
            }
       }).start();

    }
}
