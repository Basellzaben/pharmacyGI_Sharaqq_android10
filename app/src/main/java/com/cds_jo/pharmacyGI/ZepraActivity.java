package com.cds_jo.pharmacyGI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.internal.ZebraPrinterCpcl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ZepraActivity extends AppCompatActivity {
    private static int TAKE_PICTURE = 1;
    private static int PICTURE_FROM_GALLERY = 2;
    private boolean sendData = true;
    private static File file = null;
    Connection printerConnection = null;
    private EditText printStoragePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zepra);
        printStoragePath = (EditText) findViewById(R.id.printerStorePath);
        printerConnection = new BluetoothConnection("00:17:E9:D7:6F:72");
    }

    public void btn_print(View view) {
        //sendData = false;
       //performTestWithManyJobs();

        //Bitmap myBitmap = null;

        //printPhotoFromExternal(myBitmap);
      //  getPhotoFromCamera();
        getPhotosFromGallery();
      //  print();
    }
    private void getPhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "tempPic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, TAKE_PICTURE);
    }
    private void getPhotosFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_FROM_GALLERY);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PICTURE) {
                printStoragePath.setText(file.getAbsolutePath().toString());
                printPhotoFromExternal(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
            if (requestCode == PICTURE_FROM_GALLERY) {
                Uri imgPath = data.getData();
                printStoragePath.setText(imgPath.toString());
                Bitmap myBitmap = null;
                try {
                    myBitmap = Media.getBitmap(getContentResolver(), imgPath);
                } catch (FileNotFoundException e) {
                 //   helper.showErrorDialog(e.getMessage());
                } catch (IOException e) {
                   // helper.showErrorDialog(e.getMessage());
                }
              printPhotoFromExternal(myBitmap);

            }
        }
    }

    public void performTestWithManyJobs() {
      //  executeTest(true);
    }



    private void saveSettings() {

    }
    private void printPhotoFromExternal(final Bitmap bitmap) {
        new Thread(new Runnable() {
            public void run() {
                try {


                    Looper.prepare();

                     Connection connection =  new BluetoothConnection("00:17:E9:D7:6F:72");
                     connection.open();
                     ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.CPCL, connection);

                     connection.write("! U1 JOURNAL\r\n! U1 SETFF 20 2\r\n".getBytes());
                   //  connection.write("! U1 JOURNAL\r\n".getBytes());



                    printer.printImage(new ZebraImageAndroid(bitmap), 20, 0, 790, bitmap.getHeight(), false);
                  //  connection.write("! U1 SETIF 20 2\r\n".getBytes());
                    connection.close();

                  //  }


                    if (file != null) {
                        file.delete();
                        file = null;
                    }
                } catch (ConnectionException e) {





              /*  } catch (ZebraPrinterLanguageUnknownException e) {*/
                    e.printStackTrace();
                } finally {
                   // bitmap.recycle();

                    Looper.myLooper().quit();
                }
            }
        }).start();

    }


   /* private void printPhotoFromExternal(final Bitmap bitmap) {
        new Thread(new Runnable() {
            public void run() {
                try {
                  //  getAndSaveSettings();

                    Looper.prepare();
                   // helper.showLoadingDialog("Sending image to printer");


                    Connection connection =  new BluetoothConnection("AC:3F:A4:1B:33:0B");
                    connection.open();
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);

                   PrinterLanguage s=   PrinterLanguage.CPCL;
                    File graphicFile = new   File(Environment.getExternalStorageDirectory(), "v2i.jpg");



                    ZebraImageI zebraImageI = ZebraImageFactory.getImage(graphicFile.getAbsolutePath());
                    printer.printImage(zebraImageI,250,0,0,-1,false);





                  //  ZebraPrinter zp = new ZebraPrinterCpcl(connection,s);
                    //zp.getGraphicsUtil().printImage(path, 0, 0);
                 //  zp.printImage(path, 0, 0);


                    // ZebraImageAndroid zebraImageToPrint = new ZebraImageAndroid(bitmap);
                    // printer.printImage(zebraImageToPrint, 0, 0, -1, -1,false );


                  // if (((CheckBox) findViewById(R.id.checkBox)).isChecked()) {
                      //  printer.storeImage(printStoragePath.getText().toString(), new ZebraImageAndroid(bitmap), 550, 412);
                  //  } else {
                       // printer.printImage(new ZebraImageAndroid(bitmap), 0, 0, 550, 412, false);
                   // }
                    connection.close();

                    if (file != null) {
                        file.delete();
                        file = null;
                    }
                } catch (ConnectionException e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ZepraActivity.this).create();
                    alertDialog.setTitle("Zepra");

                    alertDialog.setMessage(e.getMessage());
                    alertDialog.setIcon(R.drawable.tick);

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
              *//*  catch (ZebraPrinterLanguageUnknownException e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ZepraActivity.this).create();
                    alertDialog.setTitle("Zepra");

                    alertDialog.setMessage(e.getMessage());
                    alertDialog.setIcon(R.drawable.tick);

                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();

`
                } *//*

                  catch (ZebraPrinterLanguageUnknownException e) {
                      e.printStackTrace();
                  } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    bitmap.recycle();
                   // helper.dismissLoadingDialog();
                    Looper.myLooper().quit();
                }
            }
        }).start();

    }*/
    public void print() {


       // ZebraPrinterConnection printerCo = new BluetoothPrinterConnection("AC:3F:A4:1B:33:0B");
        new Thread(new Runnable() {
            public void run() {
                try {

                    final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tempPic.jpg";

                    Connection connection = new BluetoothConnection("00:17:E9:D7:6F:72");
                 connection.open();

                PrinterLanguage s=   PrinterLanguage.CPCL;


               ZebraPrinter zp = new ZebraPrinterCpcl(connection,s);
                 //zp.getGraphicsUtil().printImage(path, 0, 0);
                zp.printImage(path, 0, 0);
                Thread.sleep(500);


        connection.close();
        }

          catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

            }
        }).start();

    }

}
