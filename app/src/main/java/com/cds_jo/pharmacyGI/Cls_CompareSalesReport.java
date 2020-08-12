package com.cds_jo.pharmacyGI;

public class Cls_CompareSalesReport {

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getDocType() {
        return DocType;
    }

    public void setDocType(String docType) {
        DocType = docType;
    }

    public String getDocFlg() {
        return DocFlg;
    }

    public void setDocFlg(String docFlg) {
        DocFlg = docFlg;
    }

    String Total,bill,BillDate,cusname,DocType,DocFlg;
}
