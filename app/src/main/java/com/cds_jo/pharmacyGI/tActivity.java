package com.cds_jo.pharmacyGI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.cds_jo.pharmacyGI.assist.LoginActivity;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

import java.util.ArrayList;

public class tActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<Items> gridArray = new ArrayList<Items>();

    CustomGridAdapter customGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t);
        this.setTitle("Jordan University");

        Bitmap Equivalence = BitmapFactory.decodeResource(this.getResources(), R.drawable.galaxy01);
        Bitmap Authentication = BitmapFactory.decodeResource(this.getResources(), R.drawable.galaxy01);
        Bitmap logout = BitmapFactory.decodeResource(this.getResources(), R.mipmap.logout);
        Bitmap Sitting  = BitmapFactory.decodeResource(this.getResources(), R.drawable.galaxy01);
        Bitmap inq  = BitmapFactory.decodeResource(this.getResources(), R.drawable.galaxy01);




        ImageButton Next = (ImageButton) findViewById(R.id.Next);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent k = new Intent(v.getContext(),Instract_ECActivity.class);
              //  startActivity(k);
            }
        });


        ImageButton Prv = (ImageButton) findViewById(R.id.Prv);
        Prv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                startActivity(k);
            }
        });


        ImageButton Home = (ImageButton) findViewById(R.id.home);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                startActivity(k);
            }
        });

            gridArray.add(new

                            Items(Equivalence, "معادلة الشهادات")

            );
            gridArray.add(new

                            Items(Authentication, "تصديق الشهادات")

            );

           gridArray.add(new

                           Items(Sitting, "إعدادات عامة")

        );

        gridArray.add(new

                        Items(inq, "استعلام عن الطلبات")

        );
            gridArray.add(new

                            Items(logout, "الخروج")

            );


            gridView=(GridView)

            findViewById(R.id.gridView1);

            customGridAdapter=new    CustomGridAdapter(this,R.layout.row_grid, gridArray);

            gridView.setAdapter(customGridAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                            {
                                                @Override
                                                public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                                                    //  Toast.makeText(getApplicationContext(), gridArray.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                                                    if (position == 0) {
                                                        Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                                                        startActivity(k);
                                                        // Handle the camera action
                                                    } else if (position == 1) {
                                                        Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                                                        startActivity(k);
                                                        // Handle the camera action
                                                    } else if (position == 2) {
                                                        Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                                                        startActivity(k);

                                                }   else if (position == 3) {
                                                    Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                                                     startActivity(k);

                                                    }   else if (position == 4) {
                                                        Intent k = new Intent(v.getContext(), LoginActivity.class);
                                                        startActivity(k);
                }
                                                }
                                            }

            );
        this.setTitle("وزارة التعليم العالي والبحث العلمي");
        }


    }
