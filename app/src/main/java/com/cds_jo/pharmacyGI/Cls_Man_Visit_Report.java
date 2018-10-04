package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 27/02/2017.
 */

public class Cls_Man_Visit_Report {

    String Day;
    String Date;

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCustInLoctCount() {
        return CustInLoctCount;
    }

    public void setCustInLoctCount(String custInLoctCount) {
        CustInLoctCount = custInLoctCount;
    }

    public String getCustVisitedCount() {
        return CustVisitedCount;
    }

    public void setCustVisitedCount(String custVisitedCount) {
        CustVisitedCount = custVisitedCount;
    }

    public String getPrecent() {
        return Precent;
    }

    public void setPrecent(String precent) {
        Precent = precent;
    }

    public String getTotalPerMonth() {
        return TotalPerMonth;
    }

    public void setTotalPerMonth(String totalPerMonth) {
        TotalPerMonth = totalPerMonth;
    }

    String CustInLoctCount;
    String CustVisitedCount;
    String Precent;
    String TotalPerMonth;

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getPharmacyNm() {
        return PharmacyNm;
    }

    public void setPharmacyNm(String pharmacyNm) {
        PharmacyNm = pharmacyNm;
    }

    String CityName;
    String PharmacyNm;
}
