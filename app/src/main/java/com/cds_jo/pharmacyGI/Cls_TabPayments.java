package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 08/03/2016.
 */
public class Cls_TabPayments {
    String custNm;
    String DocNo;
    String Date;
    String Notes;
    String Acc;
    String Amt;


    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    String Post;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    public String getAcc() {
        return Acc;
    }

    public void setAcc(String acc) {
        Acc = acc;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDocNo() {
        return DocNo;
    }

    public void setDocNo(String docNo) {
        DocNo = docNo;
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    String Type;
}
