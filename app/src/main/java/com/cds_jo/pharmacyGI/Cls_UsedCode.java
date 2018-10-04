package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 14/11/2016.
 */
public class Cls_UsedCode {

   String  No;
    String Code;
    String Status;
    String CustomerNo;
    String ItemNo;
    String Tr_Date;
    String Tr_Time;
    String UserNo;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getPosted() {
        return Posted;
    }

    public void setPosted(String posted) {
        Posted = posted;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTr_Date() {
        return Tr_Date;
    }

    public void setTr_Date(String tr_Date) {
        Tr_Date = tr_Date;
    }

    public String getTr_Time() {
        return Tr_Time;
    }

    public void setTr_Time(String tr_Time) {
        Tr_Time = tr_Time;
    }

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    String Posted;
}
