package com.cds_jo.pharmacyGI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.ESCPSample3;
import com.sewoo.jpos.printer.ESCPOSPrinter;

import java.io.IOException;

public class PrintImageActivity extends AppCompatActivity {
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print=new ESCPSample3();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_image);

    }

    public void DoPrint(View view) {
        try {
            obj_print.imageTest();
            // posPtr.printBitmap("//sdcard//v2i.jpg", LKPrint.LK_ALIGNMENT_CENTER);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
}
