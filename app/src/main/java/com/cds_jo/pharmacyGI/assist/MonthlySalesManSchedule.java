package com.cds_jo.pharmacyGI.assist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cds_jo.pharmacyGI.DB;
import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.Pop_Update_Month;
import com.cds_jo.pharmacyGI.Pop_Update_Year;
import com.cds_jo.pharmacyGI.PostTransActions.PostMonthlyScedule;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.We_Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hearder.main.Header_Frag;

public class MonthlySalesManSchedule extends FragmentActivity {
    ListView AllDays_Lst, Lst_Country_Day, Lst_Country_All, LstView_Week_days, LstWeekNo;

    ArrayList<Cls_Monthly_Schedule> AddDays;
    ArrayList<Cls_WeekDays> weekDaysList;
    ArrayList<Cls_WeekDays> weekNoList;
    ArrayList<Cls_Country> CountryOfDay;
    ArrayList<Cls_Country> AllCountry;
    long PostResult = 0;
    TextView tv,tv_Status;
    SharedPreferences sharedPreferences;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    SqlHandler sqlHandler;
    String q;
    String UserID = "";
    String WeekNo, DayNo, PeriodNo, Enter_Status;
    CheckBox chk_Period2, chk_Period1, chk_all, chk_Enter_all, chk_Enter, chk_Not_Enter;
    Cls_Monthly_Schedule SelectedDate;
    Methdes.MyTextView tv_Month, ed_Year;
    public ProgressDialog loadingdialog;
    String PostedPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_sales_man_schedule);
        AllDays_Lst = (ListView) findViewById(R.id.AllDays);
        Lst_Country_Day = (ListView) findViewById(R.id.Lst_Country_Day);
        Lst_Country_All = (ListView) findViewById(R.id.Lst_Country_All);
        LstView_Week_days = (ListView) findViewById(R.id.LstWeekDays);
        LstWeekNo = (ListView) findViewById(R.id.LstWeekNo);


        tv_Month = (Methdes.MyTextView) findViewById(R.id.tv_Month);
        ed_Year = (Methdes.MyTextView) findViewById(R.id.ed_Year);
        SimpleDateFormat MonthFormt = new SimpleDateFormat("MM", Locale.ENGLISH);
        String currentMonth = MonthFormt.format(new Date());

        tv_Month.setText(Integer.parseInt(currentMonth)+"");

        SimpleDateFormat YearFormt = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String currentYear = YearFormt.format(new Date());

        ed_Year.setText(currentYear);

        chk_Period2 = (CheckBox) findViewById(R.id.chk_Period2);
        chk_Period1 = (CheckBox) findViewById(R.id.chk_Period1);
        chk_all = (CheckBox) findViewById(R.id.chk_all);

        chk_Enter_all = (CheckBox) findViewById(R.id.chk_Enter_all);
        chk_Enter = (CheckBox) findViewById(R.id.chk_Enter);
        chk_Not_Enter = (CheckBox) findViewById(R.id.chk_Not_Enter);
        tv_Status = (TextView) findViewById(R.id.tv_Status);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        SelectedDate = new Cls_Monthly_Schedule();
        SelectedDate.setDate("");
        chk_all.setChecked(true);
        chk_Enter_all.setChecked(true);


        chk_Enter_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Enter_Status = "-1";
                    FillAllDays();
                    chk_Enter.setChecked(false);
                    chk_Not_Enter.setChecked(false);
                }

            }

        });
        chk_Enter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Enter_Status = "1";
                    FillAllDays();
                    chk_Enter_all.setChecked(false);
                    chk_Not_Enter.setChecked(false);
                }

            }

        });

        chk_Not_Enter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Enter_Status = "2";
                    FillAllDays();
                    chk_Enter_all.setChecked(false);
                    chk_Enter.setChecked(false);
                }

            }

        });


        chk_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    PeriodNo = "-1";
                    FillAllDays();
                    chk_Period2.setChecked(false);
                    chk_Period1.setChecked(false);
                }

            }

        });
        chk_Period2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    PeriodNo = "2";
                    FillAllDays();
                    chk_all.setChecked(false);
                    chk_Period1.setChecked(false);
                }

            }

        });

        chk_Period1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    PeriodNo = "1";
                    FillAllDays();
                    chk_all.setChecked(false);
                    chk_Period2.setChecked(false);
                }

            }

        });

        sqlHandler = new SqlHandler(this);
        AllDays_Lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View argu, int position, long arg3) {
                try {
                    SelectedDate = (Cls_Monthly_Schedule) AllDays_Lst.getItemAtPosition(position);
                    FillAllCountryOfDay();
                    argu.setBackgroundColor(Color.GRAY);
                    for (int i = 0; i < AllDays_Lst.getChildCount(); i++) {
                        View listItem = AllDays_Lst.getChildAt(i);
                        if (i % 2 == 0)
                            listItem.setBackgroundColor(Color.WHITE);
                        if (i % 2 == 1)
                            listItem.setBackgroundColor(MonthlySalesManSchedule.this.getResources().getColor(R.color.Gray2));
                    }
                    argu.setBackgroundColor(Color.GRAY);

                } catch (Exception ex) {
                    Toast.makeText(MonthlySalesManSchedule.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        LstWeekNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View argu, int position, long arg3) {
                try {
                    Cls_WeekDays o = (Cls_WeekDays) LstWeekNo.getItemAtPosition(position);
                    WeekNo = o.getNo();
                    FillAllDays();
                    argu.setBackgroundColor(Color.GRAY);
                    for (int i = 0; i < LstWeekNo.getChildCount(); i++) {
                        View listItem = LstWeekNo.getChildAt(i);
                        if (i % 2 == 0)
                            listItem.setBackgroundColor(Color.WHITE);
                        if (i % 2 == 1)
                            listItem.setBackgroundColor(MonthlySalesManSchedule.this.getResources().getColor(R.color.Gray2));
                    }
                    argu.setBackgroundColor(Color.GRAY);
                } catch (Exception ex) {
                    Toast.makeText(MonthlySalesManSchedule.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        LstView_Week_days.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View argu, int position, long arg3) {
                try {
                    Cls_WeekDays o = (Cls_WeekDays) LstView_Week_days.getItemAtPosition(position);
                    DayNo = o.getNo();
                    FillAllDays();
                    argu.setBackgroundColor(Color.GRAY);
                    for (int i = 0; i < LstView_Week_days.getChildCount(); i++) {
                        View listItem = LstView_Week_days.getChildAt(i);
                        if (i % 2 == 0)
                            listItem.setBackgroundColor(Color.WHITE);
                        if (i % 2 == 1)
                            listItem.setBackgroundColor(MonthlySalesManSchedule.this.getResources().getColor(R.color.Gray2));
                    }
                    argu.setBackgroundColor(Color.GRAY);

                } catch (Exception ex) {
                    Toast.makeText(MonthlySalesManSchedule.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }


            }
        });
        AddDays = new ArrayList<Cls_Monthly_Schedule>();
        CountryOfDay = new ArrayList<Cls_Country>();
        AllCountry = new ArrayList<Cls_Country>();
        weekDaysList = new ArrayList<Cls_WeekDays>();
        weekNoList = new ArrayList<Cls_WeekDays>();

        AddDays.clear();

        Fragment frag = new Header_Frag();
      FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();

        WeekNo = "-1";
        DayNo = "-1";
        PeriodNo = "-1";
        Enter_Status = "-1";
        FillWeekDays();
        FillWeekNo();
        FillAllDays(); // Select All Date For This Month
        FillAllCountryOfDay();
        FillAllCountry();
        month_Dates();

    }

    private void month_Dates() {

        tv = new TextView(getApplicationContext());
        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


        final Handler _handler = new Handler();

        final ProgressDialog custDialog = new ProgressDialog(MonthlySalesManSchedule.this);
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText(" الخطة الشهرية");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(MonthlySalesManSchedule.this);
                ws.GetMonth_Dates(UserID, tv_Month.getText().toString(), ed_Year.getText().toString());
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_TodayDate = js.getJSONArray("TodayDate");
                    JSONArray js_Day_Nm_En = js.getJSONArray("Day_Nm_En");
                    JSONArray js_Day_No = js.getJSONArray("Day_No");
                    JSONArray js_Day_Nm_Ar = js.getJSONArray("Day_Nm_Ar");
                    JSONArray js_PeriodNo = js.getJSONArray("PeriodNo");
                    JSONArray js_PeriodDesc = js.getJSONArray("PeriodDesc");
                    JSONArray js_Week_No = js.getJSONArray("Week_No");
                    JSONArray js_CurrentMonth = js.getJSONArray("CurrentMonth");
                    JSONArray js_CurrentYear = js.getJSONArray("CurrentYear");
                    JSONArray js_Day_No_Sort = js.getJSONArray("Day_No_Sort");
                    JSONArray js_AreaNo = js.getJSONArray("AreaNo");
                    JSONArray js_Posted = js.getJSONArray("Posted");

                    q = "Delete from Month_Dates";
                    sqlHandler.executeQuery(q);
                    q = " delete from sqlite_sequence where name='Month_Dates'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_TodayDate.length(); i++) {
                        q = "INSERT INTO Month_Dates (  TodayDate,Day_Nm_En,Day_No,Day_Nm_Ar,PeriodNo,PeriodDesc,Week_No,CurrentMonth,CurrentYear,Day_No_Sort,AreaNo,Posted ) values ('"
                                + js_TodayDate.get(i).toString()
                                + "','" + js_Day_Nm_En.get(i).toString()
                                + "','" + js_Day_No.get(i).toString()
                                + "','" + js_Day_Nm_Ar.get(i).toString()
                                + "','" + js_PeriodNo.get(i).toString()
                                + "','" + js_PeriodDesc.get(i).toString()
                                + "','" + js_Week_No.get(i).toString()
                                + "','" + js_CurrentMonth.get(i).toString()
                                + "','" + js_CurrentYear.get(i).toString()
                                + "','" + js_Day_No_Sort.get(i).toString()
                                + "','" + js_AreaNo.get(i).toString()
                                + "','" + js_Posted.get(i).toString()
                                + "' )";
                        sqlHandler.executeQuery(q);

                        custDialog.setMax(js_TodayDate.length());
                        custDialog.incrementProgressBy(1);
                        if (custDialog.getProgress() == custDialog.getMax()) {

                            custDialog.dismiss();
                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {
                            FillAllDays(); // Select All Date For This Month
                            custDialog.dismiss();

                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();

                        }
                    });
                }
            }
        }).start();


    }

    private void FillWeekNo() {
        weekNoList.clear();
        Cls_WeekDays obj = new Cls_WeekDays();
        obj.setNo("-1");
        obj.setDesc("الكل");
        weekNoList.add(obj);

        obj = new Cls_WeekDays();
        obj.setNo("1");
        obj.setDesc("الأسبوع 1");
        weekNoList.add(obj);

        obj = new Cls_WeekDays();
        obj.setNo("2");
        obj.setDesc("الأسبوع 2");
        weekNoList.add(obj);


        obj = new Cls_WeekDays();
        obj.setNo("3");
        obj.setDesc("الأسبوع 3");
        weekNoList.add(obj);

        obj = new Cls_WeekDays();
        obj.setNo("4");
        obj.setDesc("الأسبوع 4");
        weekNoList.add(obj);


        obj = new Cls_WeekDays();
        obj.setNo("5");
        obj.setDesc("الأسبوع 5");
        weekNoList.add(obj);


        Cls_Week_No_adapter cls_week_no_adapter = new Cls_Week_No_adapter(
                this, weekNoList);
        LstWeekNo.setAdapter(cls_week_no_adapter);

    }

    private void FillWeekDays() {
        weekDaysList.clear();
        Cls_WeekDays obj = new Cls_WeekDays();
        obj.setNo("-1");
        obj.setDesc("الكل");
        weekDaysList.add(obj);

        obj = new Cls_WeekDays();
        obj.setNo("7");
        obj.setDesc("السبت");
        weekDaysList.add(obj);

        obj = new Cls_WeekDays();
        obj.setNo("1");
        obj.setDesc("الأحد");
        weekDaysList.add(obj);


        obj = new Cls_WeekDays();
        obj.setNo("2");
        obj.setDesc("الأثنين");
        weekDaysList.add(obj);

        obj = new Cls_WeekDays();
        obj.setNo("3");
        obj.setDesc("الثلاثاء");
        weekDaysList.add(obj);


        obj = new Cls_WeekDays();
        obj.setNo("4");
        obj.setDesc("الأربعاء");
        weekDaysList.add(obj);

        obj = new Cls_WeekDays();
        obj.setNo("5");
        obj.setDesc("الخميس");
        weekDaysList.add(obj);


        Cls_Week_Days_adapter cls_monthly_sechedule_adapter = new Cls_Week_Days_adapter(
                this, weekDaysList);
        LstView_Week_days.setAdapter(cls_monthly_sechedule_adapter);


    }

    @SuppressLint("Range")
    private void FillAllDays() {

        Lst_Country_Day.setAdapter(null);
        AddDays.clear();

        PostedPlan = DB.GetValue(this,"Month_Dates","Posted","CurrentMonth='"+ tv_Month.getText() + "' and CurrentYear='" +ed_Year.getText()+"'");
        tv_Status.setText("غير معتمدة");
       if(PostedPlan.toString().equalsIgnoreCase("1")){
            sqlHandler.executeQuery( " delete from Monthly_Schedule where TrMonth ='"+tv_Month.getText().toString()+"' and TrYear='"+ed_Year.getText().toString()+"'");
           q = " Insert into Monthly_Schedule (  Today_Date, Period_No, Area_No,  User_No,   TrYear,  TrMonth,Posted) " +
                   " SELECT  TodayDate, PeriodNo,AreaNo,"+ UserID+",CurrentYear,CurrentMonth,'1' from Month_Dates ";

           sqlHandler.executeQuery(q);
           tv_Status.setText("تم الإعتماد");
       }

           q = " Select distinct TodayDate,Day_Nm_En,Day_No,Day_Nm_Ar,PeriodNo,PeriodDesc,Week_No,CurrentMonth" +
                   " ,CurrentYear,Day_No_Sort  from  Month_Dates  " +
                   " left  join Monthly_Schedule on Monthly_Schedule.Today_Date= Month_Dates.TodayDate and Monthly_Schedule.Period_No = Month_Dates.PeriodNo " +
                   " Where ('" + WeekNo + "'='-1' or Week_No='" + WeekNo + "') " +
                   "  And ('" + DayNo + "'='-1' or Day_No='" + DayNo + "') " +
                   "  And ('" + PeriodNo + "'='-1' or PeriodNo='" + PeriodNo + "') " +
                   "  And ('" + Enter_Status + "'='-1' or ('" + Enter_Status + "'='1'  and Monthly_Schedule.Area_No is not null )       or ('" + Enter_Status + "'='2'  and Monthly_Schedule.Area_No is null )  ) ";


       Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Monthly_Schedule obj = new Cls_Monthly_Schedule();

                    obj.setDayNum(c1.getString(c1.getColumnIndex("Day_No")));
                    obj.setDayNm(c1.getString(c1.getColumnIndex("Day_Nm_Ar")));
                    obj.setDate(c1.getString(c1.getColumnIndex("TodayDate")));
                    obj.setCountryId("1");
                    obj.setCountryNm(GetTodayAllArea(c1.getString(c1.getColumnIndex("TodayDate")), c1.getString(c1.getColumnIndex("PeriodNo"))));
                    obj.setPeriodNo(c1.getString(c1.getColumnIndex("PeriodNo")));
                    obj.setPeriodDesc(c1.getString(c1.getColumnIndex("PeriodDesc")));

                    AddDays.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }




        Cls_Monthly_Sechedule_adapter cls_monthly_sechedule_adapter = new Cls_Monthly_Sechedule_adapter(
                this, AddDays);
        AllDays_Lst.setAdapter(cls_monthly_sechedule_adapter);
    }

    @SuppressLint("Range")
    private String GetTodayAllArea(String Date, String PeriodNo) {
        String All_Area = "";
        q = " Select  Area_No, Area.Name from   Monthly_Schedule  left join Area on  Area.No=Monthly_Schedule.Area_No   where Today_Date='" + Date + "' and Period_No='" + PeriodNo + "' and User_No='" + UserID + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    All_Area = All_Area + c1.getString(c1.getColumnIndex("Name")) + "   ";
                } while (c1.moveToNext());

            }
            c1.close();
        } else {
            All_Area = "غير مدخلة";
        }
        return All_Area;
    }

    @SuppressLint("Range")
    private void FillAllCountryOfDay() {

        CountryOfDay.clear();

        q = " Select  Area_No, Area.Name from   Monthly_Schedule  left join Area on  Area.No=Monthly_Schedule.Area_No   where Today_Date='" + SelectedDate.getDate() + "' and Period_No='" + SelectedDate.getPeriodNo() + "' and User_No='" + UserID + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Country obj = new Cls_Country();
                    obj.setID(c1.getString(c1.getColumnIndex("Area_No")));
                    obj.setNm(c1.getString(c1.getColumnIndex("Name")));

                    CountryOfDay.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }





     /*   Cls_Country obj = new Cls_Country();
        obj.setID("1");
        obj.setNm("الشميساني");
        CountryOfDay.add(obj);



        obj = new Cls_Country();
        obj.setID("2");
        obj.setNm("عبدون");
        CountryOfDay.add(obj);

*/
        Cls_Country_Day_adapter cls_monthly_sechedule_adapter = new Cls_Country_Day_adapter(
                this, CountryOfDay);
        Lst_Country_Day.setAdapter(cls_monthly_sechedule_adapter);
    }

    @SuppressLint("Range")
    private void FillAllCountry() {

        AllCountry.clear();

        q = " Select   No , Name from Area  where    cast(No as INTEGER ) >0";

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Country obj = new Cls_Country();

                    obj.setID(c1.getString(c1.getColumnIndex("No")));
                    obj.setNm(c1.getString(c1.getColumnIndex("Name")));

                    AllCountry.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }







        /*Cls_Country obj = new Cls_Country();
        obj.setID("1");
        obj.setNm("الشميساني");
        AllCountry.add(obj);



        obj = new Cls_Country();
        obj.setID("2");
        obj.setNm("عبدون");
        AllCountry.add(obj);


        obj = new Cls_Country();
        obj.setID("3");
        obj.setNm("اربد");
        AllCountry.add(obj);


        obj = new Cls_Country();
        obj.setID("4");
        obj.setNm("السلط");
        AllCountry.add(obj);


        obj = new Cls_Country();
        obj.setID("5");
        obj.setNm("الطفيلة");
        AllCountry.add(obj);


        obj = new Cls_Country();
        obj.setID("6");
        obj.setNm("معان");
        AllCountry.add(obj);



        obj = new Cls_Country();
        obj.setID("7");
        obj.setNm("الكرك");
        AllCountry.add(obj);


        obj = new Cls_Country();
        obj.setID("8");
        obj.setNm("جبل الحسين");
        AllCountry.add(obj);*/


        Cls_Country_All_adapter cls_monthly_sechedule_adapter = new Cls_Country_All_adapter(
                this, AllCountry);
        Lst_Country_All.setAdapter(cls_monthly_sechedule_adapter);
    }

    public void btn_GetData(View view) {
        month_Dates();
    }

    int position;

    public int btn_Insert_City(View view) {




        if(PostedPlan.equalsIgnoreCase("1")){
            Toast.makeText(this,"تم اعتماد الخطة ،لا يمكن الإضافة",Toast.LENGTH_SHORT).show();
            return -1;
        }


        position = Lst_Country_All.getPositionForView(view);
        if (SelectedDate.getDate().equalsIgnoreCase("")) {
            Toast.makeText(this, "الرجاء تحديد التاريخ", Toast.LENGTH_SHORT).show();
            return 0;
        }

        boolean Exist = true;
        Exist = CheckArea(SelectedDate.getDate().toString(), SelectedDate.getPeriodNo().toString(), AllCountry.get(position).getID().toString());
        if (Exist == false) {
            return 0;
        }

            Long i;
            ContentValues cv = new ContentValues();
            cv.put("Today_Date", SelectedDate.getDate().toString());
            cv.put("Period_No", SelectedDate.getPeriodNo().toString());
            cv.put("Area_No", AllCountry.get(position).getID().toString());
            cv.put("User_No", UserID);
            cv.put("TrYear", ed_Year.getText().toString());
            cv.put("TrMonth", tv_Month.getText().toString());
            cv.put("Posted", "0");
            i = sqlHandler.Insert("Monthly_Schedule", null, cv);
            if (i > 0) {
                FillAllDays();
                FillAllCountryOfDay();

            }
            //   Toast.makeText(this, SelectedDate.getDate(), Toast.LENGTH_SHORT).show();

        return 1;
    }

    public void btn_Delete_Area(View view) {
        position = Lst_Country_Day.getPositionForView(view);

        if(PostedPlan.equalsIgnoreCase("1")){
         Toast.makeText(this,"تم اعتماد الخطة ، لا يمكن الحذف",Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("الخطة الشهرية");
        alertDialog.setMessage("هل انت متاكد من عملية الحذف");
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                q = "Delete From Monthly_Schedule where Area_No='" + CountryOfDay.get(position).getID().toString() + "'And Today_Date='" + SelectedDate.getDate() + "' and Period_No='" + SelectedDate.getPeriodNo() + "' and User_No='" + UserID + "'";
                sqlHandler.executeQuery(q);
                FillAllDays();
                FillAllCountryOfDay();


            }
        });

        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();


    }

    public void btn_Collections(View view) {

        boolean Exist = true;
        Exist = CheckArea(SelectedDate.getDate().toString(), SelectedDate.getPeriodNo().toString(),"-1");
        if (Exist == false) {
            return  ;
        }

        if(PostedPlan.equalsIgnoreCase("1")){
            Toast.makeText(this,"تم اعتماد الخطة ،لا يمكن الإضافة",Toast.LENGTH_SHORT).show();
            return ;
        }


        if (SelectedDate.getDate().equalsIgnoreCase("")) {
            Toast.makeText(this, "الرجاء تحديد التاريخ", Toast.LENGTH_SHORT).show();
        } else {
            Long i;
            ContentValues cv = new ContentValues();
            cv.put("Today_Date", SelectedDate.getDate().toString());
            cv.put("Period_No", SelectedDate.getPeriodNo().toString());
            cv.put("Area_No", "-1");
            cv.put("User_No", UserID);
            cv.put("TrYear", ed_Year.getText().toString());
            cv.put("TrMonth", tv_Month.getText().toString());
            cv.put("Posted", "0");
            i = sqlHandler.Insert("Monthly_Schedule", null, cv);
            if (i > 0) {
                FillAllDays();
                FillAllCountryOfDay();

            }
            //   Toast.makeText(this, SelectedDate.getDate(), Toast.LENGTH_SHORT).show();
        }
    }

    public void btn_Others(View view) {

        boolean Exist = true;
        Exist = CheckArea(SelectedDate.getDate().toString(), SelectedDate.getPeriodNo().toString(),"-2");
        if (Exist == false) {
            return  ;
        }

        if(PostedPlan.equalsIgnoreCase("1")){
            Toast.makeText(this,"تم اعتماد الخطة ،لا يمكن الإضافة",Toast.LENGTH_SHORT).show();
            return ;
        }


        if (SelectedDate.getDate().equalsIgnoreCase("")) {
            Toast.makeText(this, "الرجاء تحديد التاريخ", Toast.LENGTH_SHORT).show();
        } else {
            Long i;
            ContentValues cv = new ContentValues();
            cv.put("Today_Date", SelectedDate.getDate().toString());
            cv.put("Period_No", SelectedDate.getPeriodNo().toString());
            cv.put("Area_No", "-2");
            cv.put("User_No", UserID);
            cv.put("TrYear", ed_Year.getText().toString());
            cv.put("TrMonth", tv_Month.getText().toString());
            cv.put("Posted", "0");
            i = sqlHandler.Insert("Monthly_Schedule", null, cv);
            if (i > 0) {
                FillAllDays();
                FillAllCountryOfDay();

            }
            //   Toast.makeText(this, SelectedDate.getDate(), Toast.LENGTH_SHORT).show();
        }
    }

    public int btn_Holiday(View view) {

        boolean Exist = true;

        if(PostedPlan.equalsIgnoreCase("1")){
            Toast.makeText(this,"تم اعتماد الخطة ،لا يمكن الإضافة",Toast.LENGTH_SHORT).show();
            return -1;
        }


        Exist = CheckArea(SelectedDate.getDate().toString(), SelectedDate.getPeriodNo().toString(), "-4");
        if (Exist == false) {
            return 0;
        }


        if (SelectedDate.getDate().equalsIgnoreCase("")) {
            Toast.makeText(this, "الرجاء تحديد التاريخ", Toast.LENGTH_SHORT).show();
        } else {
            Long i;
            ContentValues cv = new ContentValues();
            cv.put("Today_Date", SelectedDate.getDate().toString());
            cv.put("Period_No", SelectedDate.getPeriodNo().toString());
            cv.put("Area_No", "-4");
            cv.put("User_No", UserID);
            cv.put("TrYear", ed_Year.getText().toString());
            cv.put("TrMonth", tv_Month.getText().toString());
            cv.put("Posted", "0");
            i = sqlHandler.Insert("Monthly_Schedule", null, cv);
            if (i > 0) {
                FillAllDays();
                FillAllCountryOfDay();

            }
            //   Toast.makeText(this, SelectedDate.getDate(), Toast.LENGTH_SHORT).show();
        }
        return 1;
    }

    public int btn_Vacations(View view) {


        if(PostedPlan.equalsIgnoreCase("1")){
            Toast.makeText(this,"تم اعتماد الخطة ،لا يمكن الإضافة",Toast.LENGTH_SHORT).show();
            return -1;
        }


        boolean Exist = true;
        Exist = CheckArea(SelectedDate.getDate().toString(), SelectedDate.getPeriodNo().toString(), "-3");
        if (Exist == false) {
            return 0;
        }


        if (SelectedDate.getDate().equalsIgnoreCase("")) {
            Toast.makeText(this, "الرجاء تحديد التاريخ", Toast.LENGTH_SHORT).show();
        } else {
            Long i;
            ContentValues cv = new ContentValues();
            cv.put("Today_Date", SelectedDate.getDate().toString());
            cv.put("Period_No", SelectedDate.getPeriodNo().toString());
            cv.put("Area_No", "-3");
            cv.put("User_No", UserID);
            cv.put("TrYear", ed_Year.getText().toString());
            cv.put("TrMonth", tv_Month.getText().toString());
            cv.put("Posted", "0");
            i = sqlHandler.Insert("Monthly_Schedule", null, cv);
            if (i > 0) {
                FillAllDays();
                FillAllCountryOfDay();

            }
            //   Toast.makeText(this, SelectedDate.getDate(), Toast.LENGTH_SHORT).show();
        }
        return 0;
    }
    private boolean CheckArea(String Date, String Period, String AreaNo) {
        boolean f = true;


        if (AreaNo.equalsIgnoreCase("-3") || AreaNo.equalsIgnoreCase("-4")) {
            q = " Select * from  Monthly_Schedule where Today_Date='" + Date + "'   and Period_No ='" + Period + "' and User_No='" + UserID + "'";
            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {


                Toast.makeText(this, "الرجاء حذف المدخلات الموجودة اولا", Toast.LENGTH_SHORT).show();
                f = false;
                c1.close();
                return f;
            }

        }


        q = " Select * from  Monthly_Schedule where Today_Date='" + Date + "'   and Period_No ='" + Period + "' and Area_No ='" + AreaNo + "' and User_No='" + UserID + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {


            Toast.makeText(this, "تم إضافة المنطقة سابقا", Toast.LENGTH_SHORT).show();
            f = false;
            c1.close();
            return f;
        }

        q = " Select * from  Monthly_Schedule where Today_Date='" + Date + "'   and Period_No ='" + Period + "' and Area_No ='-4' and User_No='" + UserID + "'  ";
        c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {


            Toast.makeText(this, "لا يمكن اضافة اي منطقة  مع العطلة الرسمية", Toast.LENGTH_SHORT).show();
            f = false;
            c1.close();
            return f;
        }

        q = " Select * from  Monthly_Schedule where Today_Date='" + Date + "'   and Period_No ='" + Period + "' and Area_No ='-3' and User_No='" + UserID + "'  ";
        c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {


            Toast.makeText(this, "لا يمكن اضافة اي منطقة  مع الإجازة", Toast.LENGTH_SHORT).show();
            f = false;
            c1.close();
            return f;
        }


        q = " Select * from  Monthly_Schedule where Today_Date='" + Date + "'   and Period_No ='" + Period + "' and Area_No ='-1' and User_No='" + UserID + "'  ";
        c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            Toast.makeText(this, "لا يمكن اضافة اي منطقة  مع التحصيل", Toast.LENGTH_SHORT).show();
            f = false;
            c1.close();
            return f;
        }
        return f;
    }
    public void btn_back(View view) {
        Intent k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);
    }
    @Override
    public void onBackPressed() {
        Intent k;
        k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);
    }
    public void btn_SelectMonth(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "SalesOrder");
        bundle.putString("Nm", "اختيار الشهر");
        bundle.putString("Month", tv_Month.getText().toString());

        android.app.FragmentManager Manager = getFragmentManager();

        Pop_Update_Month obj = new Pop_Update_Month();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void UpdateMonth(String m ) {
        tv_Month.setText(m);
        month_Dates();
    }
    public void btn_SelectYear(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "SalesOrder");
        bundle.putString("Nm", "اختيار السنة");
        bundle.putString("Year", ed_Year.getText().toString());
        android.app.FragmentManager Manager = getFragmentManager();

        Pop_Update_Year obj = new Pop_Update_Year();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void UpdateYear(String y ) {
        ed_Year.setText(y);
        month_Dates();
    }
    public void btn_share(View view) {


        loadingdialog = ProgressDialog.show(MonthlySalesManSchedule.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد الخطة الشهرية", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                PostMonthlyScedule obj = new PostMonthlyScedule(MonthlySalesManSchedule.this);
                PostResult = obj.Post_Scedule(tv_Month.getText().toString(),ed_Year.getText().toString());
                try {
                    if (PostResult > 0) {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        MonthlySalesManSchedule.this).create();
                                alertDialog.setTitle("اعتماد الخطة الشهرية");
                                alertDialog.setMessage("تمت عملية الاعتماد بنجاح" + We_Result.ID + "");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();



                            }
                        });



                    } else if (PostResult == -3) {
                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        MonthlySalesManSchedule.this).create();
                                alertDialog.setTitle("عملية الاعتماد لم تتم بنجاح ");
                                alertDialog.setMessage("تم اعتماد الخطة من قبل المشرف");
                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setButton("رجـــوع", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);

                            }
                        });




                    } else {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        MonthlySalesManSchedule.this).create();
                                alertDialog.setTitle("اعتماد الخطة الشهرية");
                                alertDialog.setMessage(" فشل في عملية الاعتماد" + PostResult + "");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                            }
                        });
                    }
                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            loadingdialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    MonthlySalesManSchedule.this).create();
                            alertDialog.setTitle("فشل في عمليه الاتصال");
                            alertDialog.setMessage(e.getMessage().toString());
                            alertDialog.setIcon(R.drawable.delete);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertDialog.show();
                        }
                    });
                }
            }
        }).start();

    }

}
