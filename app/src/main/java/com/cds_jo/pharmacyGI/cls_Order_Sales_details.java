package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Order_Sales_details {
    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getPosted() {
        return Posted;
    }

    public void setPosted(String posted) {
        Posted = posted;
    }

    public String getABPrint() {
        return ABPrint;
    }

    public void setABPrint(String ABPrint) {
        this.ABPrint = ABPrint;
    }

    public String getABOrder() {
        return ABOrder;
    }

    public void setABOrder(String ABOrder) {
        this.ABOrder = ABOrder;
    }

    public String getABill() {
        return ABill;
    }

    public void setABill(String ABill) {
        this.ABill = ABill;
    }

    String custNm;
    String custNo;
    String Date;
    String Notes;
    String OrderNo;
    String Posted;
    String ABPrint;
    String ABOrder;
    String ABill;

    public String getTot() {
        return Tot;
    }

    public void setTot(String tot) {
        Tot = tot;
    }

    String Tot;


}
