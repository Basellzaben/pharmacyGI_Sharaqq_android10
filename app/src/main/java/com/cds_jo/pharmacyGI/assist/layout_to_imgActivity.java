package com.cds_jo.pharmacyGI.assist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cds_jo.pharmacyGI.Main2Activity;
import com.cds_jo.pharmacyGI.R;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.jpos.printer.LKPrint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class layout_to_imgActivity extends AppCompatActivity {

    private ESCPOSPrinter posPtr;


    private Button mButton;

    private View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mView = findViewById(R.id.f_view);




        mButton = (Button) findViewById(R.id.button1);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap b = Bitmap.createBitmap(mView.getDrawingCache());
                mView.setDrawingCacheEnabled(false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "v2i.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (Exception e) {
                }
                finish();
                //obj_print.imageTest();
                posPtr = new ESCPOSPrinter();
                try {
                    posPtr.printBitmap("//sdcard//v2i.jpg", LKPrint.LK_ALIGNMENT_CENTER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i= new Intent(layout_to_imgActivity.this, Main2Activity.class);
                startActivity(i);

            }
        });

        mView.setDrawingCacheEnabled(true);
        mView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
        mView.buildDrawingCache(true);
    }


    //   @Override
    /*public void onClick(View v) {

        if (v.getId() == R.id.button1) {

        }
    }*/
}