package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SaleManRoundsActivity extends AppCompatActivity {
    TextView     TrDate,et_Day,et_StartTime,et_EndTime;
    String weekDay ;
    int dayOfWeek ,OrderNo;
    Button EndRound,StartRound;
    SqlHandler sqlHandler  ;

TextView tv_acc,tv_cusnm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_man_rounds);


                tv_acc=(TextView)findViewById(R.id. tv_acc);
                SqlHandler sqlHandler = new SqlHandler(this);
                EndRound = (Button)findViewById(R.id.btnEndRound);
                StartRound = (Button)findViewById(R.id.btnStartRound);
                et_Day=(TextView)findViewById(R.id.et_Day);



        NewRecord();

        ShowRecord();

    }

    public void NewRecord(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());
        TrDate=(TextView)findViewById(R.id.et_Date);
        TrDate.setText(currentDateandTime);
        tv_acc=(TextView)findViewById(R.id. tv_acc);
        tv_acc.setText("");


        tv_cusnm=(TextView)findViewById(R.id. tv_cusnm);
        tv_cusnm.setText("");




        SqlHandler sqlHandler = new SqlHandler(this);
        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


        EndRound = (Button)findViewById(R.id.btnEndRound);
        StartRound = (Button)findViewById(R.id.btnStartRound);

        EndRound.setVisibility(View.INVISIBLE);
        StartRound.setVisibility(View.VISIBLE);

        ;
       /* if ( dayOfWeek == 2) weekDay = "الاثنين";
        else if (dayOfWeek == 3) weekDay = "الثلاثاء";
        else if (dayOfWeek == 4) weekDay = "الاربعاء";
        else if (dayOfWeek == 5) weekDay = "الخميس";
        else if (dayOfWeek == 6) weekDay = "الجمعة";
        else if (dayOfWeek == 7) weekDay = "السبت";
        else if (dayOfWeek == 1) weekDay = "الاحد";
*/
        et_Day=(TextView)findViewById(R.id.et_Day);
        et_Day.setText(GetDayName(dayOfWeek));



        et_StartTime = (TextView)findViewById(R.id.et_StartTime);
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss");
        String StringTime = StartTime.format(new Date());
        et_StartTime.setText(StringTime);

         et_EndTime = (TextView)findViewById(R.id.et_EndTime);
         et_EndTime.setText("");

    }
    @SuppressLint("Range")
    public  void ShowRecord(){

        sqlHandler=new SqlHandler(this);
        String query = "SELECT  SaleManRounds.no   , SaleManRounds.CusNo ,Customers.name ,Tr_Data,DayNum,Start_Time  " +
                "FROM SaleManRounds Left join Customers on Customers.no =SaleManRounds.CusNo  where Closed = 0";
        Cursor c1 = sqlHandler.selectQuery(query);
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);

        et_EndTime =(TextView)findViewById(R.id.et_EndTime);

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            tv_acc.setText(c1.getString(c1.getColumnIndex("CusNo")));
            CustNm.setText(c1.getString(c1.getColumnIndex("name")));
            TrDate.setText(c1.getString(c1.getColumnIndex("Tr_Data")));
            et_StartTime.setText(c1.getString(c1.getColumnIndex("Start_Time")));
            et_Day.setText(GetDayName(Integer.valueOf(c1.getString(c1.getColumnIndex("DayNum")))));
            EndRound.setVisibility(View.VISIBLE);
            StartRound.setVisibility(View.INVISIBLE);

            OrderNo = c1.getInt(c1.getColumnIndex("no"));


            et_EndTime = (TextView)findViewById(R.id.et_EndTime);
            SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss");
            String StringTime = StartTime.format(new Date());
            et_EndTime.setText(StringTime);


        }





    }
    public  String GetDayName(Integer Day){


        String DayNm ="" ;
        if ( Day == 2) DayNm = "الاثنين";
        else if (Day == 3) DayNm = "الثلاثاء";
        else if (Day == 4) DayNm = "الاربعاء";
        else if (Day == 5) DayNm = "الخميس";
        else if (Day == 6) DayNm = "الجمعة";
        else if (Day == 7) DayNm = "السبت";
        else if (Day == 1) DayNm = "الاحد";


        return  DayNm;

    }
    public void btn_searchCustomer(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "SaleRound");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void Set_Cust(String No, String Nm) {
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }

    public void btn_StartRound(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler=new SqlHandler(this);

        try {
            String query = "INSERT INTO SaleManRounds (CusNo,ManNo,DayNum,Tr_Data,Start_Time,Closed,Posted) values " +
                    "('" + tv_acc.getText().toString() + "','" + sharedPreferences.getString("UserID", "") + "','"
                    + String.valueOf(dayOfWeek) + "','" + TrDate.getText().toString()
                    + "','" + et_StartTime.getText().toString() + "',0 ,-1)";

          sqlHandler.executeQuery(query);


            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Galaxy");

            // Setting Dialog Message
            alertDialog.setMessage("عملية بداية الجولة تمت بنجاح");
                    alertDialog.setIcon(R.drawable.tick);

            // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

            // Showing Alert Message
            alertDialog.show();
            ShowRecord();
        }
        catch (Exception e) {

        }


    }

    public void btn_EndRound(View view) {


        try {
            String query = "Update SaleManRounds Set Closed= 1 ,End_Time ='" + et_EndTime.getText().toString() +"'  Where no = "+ OrderNo;

         sqlHandler.executeQuery(query);


            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Galaxy");

            // Setting Dialog Message
            alertDialog.setMessage("عملية نهاية الجولة تمت بنجاح");
            alertDialog.setIcon(R.drawable.tick);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            NewRecord();
            // Showing Alert Message
            alertDialog.show();

        }
        catch (Exception e) {

        }
    }

    public void btn_Back(View view) {
        Intent k = new Intent(this,GalaxyMainActivity.class);
        startActivity(k);
    }
}
