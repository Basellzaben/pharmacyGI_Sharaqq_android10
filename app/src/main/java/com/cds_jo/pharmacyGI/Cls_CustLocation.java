package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 17/06/2016.
 */
public class Cls_CustLocation {
    public String getCusNo() {
        return CusNo;
    }

    public void setCusNo(String cusNo) {
        CusNo = cusNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    String    CusNo ,Lat ,Long ,Address, date, UserID ,posted;
}
