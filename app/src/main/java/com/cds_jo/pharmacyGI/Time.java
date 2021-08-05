package com.cds_jo.pharmacyGI;

public class Time {




    private String Time;

    private String week;

    private String month;

    private String year;


    public Time(String time, String week, String month, String year) {
        Time = time;
        this.week = week;
        this.month = month;
        this.year = year;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
