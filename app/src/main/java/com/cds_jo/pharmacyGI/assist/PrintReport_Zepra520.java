package com.cds_jo.pharmacyGI.assist;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


/**
 * Created by Hp on 30/05/2016.
 */
public class PrintReport_Zepra520 {
    Context context;
    Activity Activity;

    View ReportView;
    float ImageCountFactor;

    PrintReport_Zepra520(Context _context, Activity _Activity,
                             View _ReportView, int _PageWidth, float _ImageCountFactor) {
        context = _context;
        Activity = _Activity;


        ReportView = _ReportView;

        ImageCountFactor = _ImageCountFactor;
    }

    public  void DoPrint(){
        try {
            StoreImage();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(8);
        char tempChar;
        for (int i = 0; i < 5; i++){
            tempChar = (char) (generator.nextInt(96) + 75);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    public void StoreImage() throws IOException {
        boolean saved;

        String name =random();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NameImage",name);
        editor.apply();


        OutputStream fos;
        Bitmap bitmap = loadBitmapFromView(ReportView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "sharq");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "sharq";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }
   /* public  void StoreImage(){
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

    }*/
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





}
