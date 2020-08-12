package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
import com.cds_jo.pharmacyGI.assist.CustomerReturnQtyActivity;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import hearder.main.Header_Frag;





public class GalaxyMainActivity extends FragmentActivity {
    private Context context;
    private LinearLayout RR1,RR2,RR3,R_Content,R_Update,R_Summery,R_Balance_Q,R_Trans_R,R_Visit;
    private LinearLayout R_Edit_Tran,R_Customer_Q,R_Question;
    private RelativeLayout R_Back;
    private FloatingActionMenu actionMenu1,actionMenu2;
    private ImageView Img_Menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy_main);

        Initi();
        Cir1();
        Cir2();
        Layout_Click();
        Fragment frag=new Header_Frag();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
    }

    private void Layout_Click() {

        RR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ItemGalleryActivity.class));
            }
        });

        R_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,UpdateDataToMobileActivity.class));
            }
        });

        R_Summery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ManSummeryActivity.class));
            }
        });

        R_Balance_Q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ManBalanceQtyActivity.class));
            }
        });

        R_Trans_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,TransQtyReportActivity.class));
            }
        });

        R_Visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ScheduleManActivity.class));
            }
        });

        R_Edit_Tran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MainActivity.class));
            }
        });

        R_Customer_Q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,CustomerQty.class));
            }
        });

        R_Question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,QuestneerActivity.class));
            }
        });
    }

    private void Initi() {
        context=GalaxyMainActivity.this;
        RR1= (LinearLayout) findViewById(R.id.RR1);
        RR2= (LinearLayout) findViewById(R.id.RR2);
        R_Update= (LinearLayout) findViewById(R.id.R_Update);
        R_Summery= (LinearLayout) findViewById(R.id.R_Summery);
        R_Balance_Q= (LinearLayout) findViewById(R.id.R_Balance_Q);
        R_Trans_R= (LinearLayout) findViewById(R.id.R_Trans_R);
        R_Visit= (LinearLayout) findViewById(R.id.R_Visit);
        R_Edit_Tran= (LinearLayout) findViewById(R.id.R_Edit_Tran);
        R_Customer_Q= (LinearLayout) findViewById(R.id.R_Customer_Q);
        R_Question= (LinearLayout) findViewById(R.id.R_Question);
        R_Content= (LinearLayout) findViewById(R.id.R_Content);
        R_Back= (RelativeLayout) findViewById(R.id.R_Back);
        Img_Menu=(ImageView)findViewById(R.id.imageView6);


    }

    /////////////////////////////////////////////////////////////////////////////////////
    private void Cir1() {
        RelativeLayout.LayoutParams params;
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.cir_green));


        ImageView itemIcon = new ImageView(this);
        ImageView itemIcon2 = new ImageView(this);
        ImageView itemIcon3 = new ImageView(this);
        ImageView itemIcon4 = new ImageView(this);

            itemIcon.setImageResource(R.mipmap.aa1);
            itemIcon2.setImageResource(R.mipmap.aa2);
            itemIcon3.setImageResource(R.mipmap.aa3);
            itemIcon4.setImageResource(R.mipmap.aa4);

        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();
        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();




        params = new RelativeLayout.LayoutParams(200, 200);
        button4.setLayoutParams(params);
        button3.setLayoutParams(params);
        button2.setLayoutParams(params);
        button1.setLayoutParams(params);

        actionMenu1 = new FloatingActionMenu.Builder(this)
                .setStartAngle(0) // A whole circle!
                .setEndAngle(180)
                .setRadius(250)
                .addSubActionView(button1)
                .addSubActionView(button2)
                //.addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(RR1)
                .setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
                    @Override
                    public void onMenuOpened(FloatingActionMenu floatingActionMenu) {

                            R_Content.setBackgroundResource(R.mipmap.homeback_blure2);

                        R_Back.setBackgroundColor(getResources().getColor(R.color.Main_Gray_Blure));
                        RR2.setEnabled(false);
                        R_Update.setEnabled(false);
                        R_Summery.setEnabled(false);
                        R_Balance_Q.setEnabled(false);
                        R_Trans_R.setEnabled(false);
                        R_Visit.setEnabled(false);
                        /*if(actionMenu2.isOpen())
                        {
                            actionMenu2.close(true);
                        }*/
                    }

                    @Override
                    public void onMenuClosed(FloatingActionMenu floatingActionMenu) {

                            R_Content.setBackgroundResource(R.mipmap.homeback1);

                        R_Back.setBackgroundColor(getResources().getColor(R.color.Main_Gray_Non_Blure));
                        RR2.setEnabled(true);
                        R_Update.setEnabled(true);
                        R_Summery.setEnabled(true);
                        R_Balance_Q.setEnabled(true);
                        R_Trans_R.setEnabled(true);
                        R_Visit.setEnabled(true);
                    }
                })
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, OrdersItems.class));

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Acc_ReportActivity.class));

            }
        });

       /* button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Sale_InvoiceActivity.class));

            }
        });
*/
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RecvVoucherActivity.class));

            }
        });


    }

    /////////////////////////////////////////////////////////////////////////////////
    private void Cir2() {
        RelativeLayout.LayoutParams params;
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.cir_blue));

        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.mipmap.bb1 );
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.mipmap.bb2 );
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.mipmap.bb3 );
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();


        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageResource(R.mipmap.bb4 );
        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();


        params = new RelativeLayout.LayoutParams(200, 200);
        button4.setLayoutParams(params);
        button3.setLayoutParams(params);
        button2.setLayoutParams(params);
        button1.setLayoutParams(params);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,PreapareManQty.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,ItemCostActivity.class));
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,CustomerReturnQtyActivity.class));
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,ItemGalleryActivity.class));
            }
        });

/*        actionMenu2 = new FloatingActionMenu.Builder(this)
                .setStartAngle(180) // A whole circle!
                .setEndAngle(0)
                .setRadius(250)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(RR2)
                .setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
                    @Override
                    public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                        R_Content.setBackgroundResource(R.mipmap.homeback_blure1);
                        R_Back.setBackgroundColor(getResources().getColor(R.color.Main_Gray_Blure));
                        RR1.setEnabled(false);
                        R_Update.setEnabled(false);
                        R_Summery.setEnabled(false);
                        R_Balance_Q.setEnabled(false);
                        R_Trans_R.setEnabled(false);
                        R_Visit.setEnabled(false);
                        if(actionMenu1.isOpen())
                        {
                            actionMenu1.close(true);
                        }
                    }

                    @Override
                    public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                        R_Content.setBackgroundResource(R.mipmap.homeback1);
                        R_Back.setBackgroundColor(getResources().getColor(R.color.Main_Gray_Non_Blure));
                        RR1.setEnabled(true);
                        R_Update.setEnabled(true);
                        R_Summery.setEnabled(true);
                        R_Balance_Q.setEnabled(true);
                        R_Trans_R.setEnabled(true);
                        R_Visit.setEnabled(true);
                    }
                })
                .build();*/

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String Login = sharedPreferences.getString("Login", "No");
        if (Login.toString().equals("No")) {
            Intent i = new Intent(this, NewLoginActivity.class);
            startActivity(i);
        }
        return;
    }

}
