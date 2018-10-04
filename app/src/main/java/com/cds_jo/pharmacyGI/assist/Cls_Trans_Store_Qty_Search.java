package com.cds_jo.pharmacyGI.assist;

/**
 * Created by Hp on 12/01/2017.
 */

public class Cls_Trans_Store_Qty_Search {
    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getTr_Date() {
        return Tr_Date;
    }

    public void setTr_Date(String tr_Date) {
        Tr_Date = tr_Date;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    String OrderNo , From, To,Tr_Date,Desc;
}
