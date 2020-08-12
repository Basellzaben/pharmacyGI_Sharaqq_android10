package com.cds_jo.pharmacyGI.PostTransActions;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;


import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Cls_Post_Monthly_Schedule;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PostMonthlyScedule {
    Context context;
    String query;
    SqlHandler sqlHandler;
    ArrayList<Cls_Post_Monthly_Schedule> PoList;
    public PostMonthlyScedule(Context conText) {
        context = conText;
        sqlHandler = new SqlHandler(conText);

    }

    public long Post_Scedule(String Month,String Year) {
        long Result = -1;

        PoList = new ArrayList<Cls_Post_Monthly_Schedule>();
        PoList.clear();

        Fill_Po_Adapter(Month,Year);
        String json = "[{''}]";
        try {
            if (PoList.size() > 0) {
                json = new Gson().toJson(PoList);

            }else  {
                Result= -1; ;
            }
        } catch (Exception ex) {
            Result= -1; ;
        }

        final String str;




        if(json.length()<10 ){
            Result= -1;
        }
        str = json;
         CallWebServices ws = new CallWebServices(context);
        Result = ws.Save_MonthlySedule(str );

                try {
                    if (Result > 0) {
                        sqlHandler.executeQuery("Update Monthly_Schedule set Posted ='"+Result+"' Where TrYear ='"+Year+"' and TrMonth='"+Month+"'");
                  }
                } catch (final Exception e) {
                    Result= -1;
                }
     return  Result ;
    }
    private void Fill_Po_Adapter(String Month, String Yrar) {
        String query = "";
        PoList = new ArrayList<Cls_Post_Monthly_Schedule>();
        PoList.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
      String  UserID = sharedPreferences.getString("UserID", "");
        query = "  Select  Distinct Today_Date  , Period_No  ,Area_No  ,User_No   ,Posted ,TrYear,TrMonth from Monthly_Schedule" +
                "      Where TrYear ='"+Yrar+"' and TrMonth='"+Month+"'  and User_No='"+UserID+"'" ;
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Post_Monthly_Schedule contactListItems = new Cls_Post_Monthly_Schedule();
                    contactListItems.setToday_Date(c1.getString(c1
                            .getColumnIndex("Today_Date")));
                    contactListItems.setPeriod_No(c1.getString(c1
                            .getColumnIndex("Period_No")));
                    contactListItems.setArea_No(c1.getString(c1
                            .getColumnIndex("Area_No")));
                    contactListItems.setUser_No(c1.getString(c1
                            .getColumnIndex("User_No")));
                    contactListItems.setTrYear(c1.getString(c1
                            .getColumnIndex("TrYear")));
                    contactListItems.setTrMonth(c1.getString(c1
                            .getColumnIndex("TrMonth")));

                    PoList.add(contactListItems);
                } while (c1.moveToNext());

            }
            c1.close();
        }


    }
}
