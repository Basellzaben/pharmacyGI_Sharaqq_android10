package com.cds_jo.pharmacyGI.assist;

public class Cls_Post_Monthly_Schedule {

    public String getToday_Date() {
        return Today_Date;
    }

    public void setToday_Date(String today_Date) {
        Today_Date = today_Date;
    }

    public String getPeriod_No() {
        return Period_No;
    }

    public void setPeriod_No(String period_No) {
        Period_No = period_No;
    }

    public String getArea_No() {
        return Area_No;
    }

    public void setArea_No(String area_No) {
        Area_No = area_No;
    }

    public String getUser_No() {
        return User_No;
    }

    public void setUser_No(String user_No) {
        User_No = user_No;
    }

    public String getPosted() {
        return Posted;
    }

    public void setPosted(String posted) {
        Posted = posted;
    }

    public String getTrYear() {
        return TrYear;
    }

    public void setTrYear(String trYear) {
        TrYear = trYear;
    }

    public String getTrMonth() {
        return TrMonth;
    }

    public void setTrMonth(String trMonth) {
        TrMonth = trMonth;
    }

    String Today_Date  , Period_No  ,Area_No  ,User_No   ,Posted ,TrYear,TrMonth;
}
