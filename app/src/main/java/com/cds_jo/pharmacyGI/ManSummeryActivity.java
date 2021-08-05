package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import Methdes.MyTextView;
import hearder.main.Header_Frag;

public class ManSummeryActivity extends FragmentActivity {

    private Context context=ManSummeryActivity.this;
    private MyTextView T1,T2,T3,T4,T5;
    private Fragment frag;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_man_summery);
        Initi();
        Click();

        frag=new Header_Frag();
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

        frag=new Tab_Visits_Summery();
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag11,frag).commit();
    }

    private void Click() {
        T1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setBackgroundColor(getResources().getColor(R.color.Blue));
                T1.setTextColor(Color.WHITE);

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                frag=new Tab_Visits_Summery();
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11,frag).commit();
            }
        });

        //////////////////////////////////////////////////////////////

        T2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackgroundColor(getResources().getColor(R.color.Blue));
                T2.setTextColor(Color.WHITE);


                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                frag=new Tab_Payments();
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11,frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////

        T3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackgroundColor(getResources().getColor(R.color.Blue));
                T3.setTextColor(Color.WHITE);

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                frag=new Tab_SalesOrders();
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11,frag).commit();
            }
        });

        //////////////////////////////////////////////////////////////

        T4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackgroundColor(getResources().getColor(R.color.Blue));
                T4.setTextColor(Color.WHITE);

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                frag=new Tab_UsedCode();
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11,frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////

        T5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackgroundColor(getResources().getColor(R.color.Blue));
                T5.setTextColor(Color.WHITE);

                frag=new Tab_UnpostedTransaction();
                fragmentManager=getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11,frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////
    }

    private void Initi() {
        T1=(MyTextView)findViewById(R.id.T1);
        T2=(MyTextView)findViewById(R.id.T2);
        T3=(MyTextView)findViewById(R.id.T3);
        T4=(MyTextView)findViewById(R.id.T4);
        T5=(MyTextView)findViewById(R.id.T5);
    }
    @Override
    public void onBackPressed() {
        Intent k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);
    }


}
