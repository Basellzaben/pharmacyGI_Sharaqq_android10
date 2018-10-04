package com.cds_jo.pharmacyGI.assist;

public class Cls_Monthly_Schedule {

    public String getDayNum() {
        return DayNum;
    }

    public void setDayNum(String dayNum) {
        DayNum = dayNum;
    }

    public String getDayNm() {
        return DayNm;
    }

    public void setDayNm(String dayNm) {
        DayNm = dayNm;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFlg() {
        return Flg;
    }

    public void setFlg(String flg) {
        Flg = flg;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String countryId) {
        CountryId = countryId;
    }

    public String getCountryNm() {
        return CountryNm;
    }

    public void setCountryNm(String countryNm) {
        CountryNm = countryNm;
    }

    public String getOffFlag() {
        return OffFlag;
    }

    public void setOffFlag(String offFlag) {
        OffFlag = offFlag;
    }

    String DayNum;
    String DayNm;
    String Date;
    String Flg;
    String CountryId;
    String CountryNm;
    String OffFlag;

    public String getPeriodNo() {
        return PeriodNo;
    }

    public void setPeriodNo(String periodNo) {
        PeriodNo = periodNo;
    }

    public String getPeriodDesc() {
        return PeriodDesc;
    }

    public void setPeriodDesc(String periodDesc) {
        PeriodDesc = periodDesc;
    }

    String PeriodNo;
    String PeriodDesc;
}
