package com.cds_jo.pharmacyGI.assist;
 ;

/**
 * Created by Hp on 08/03/2016.
 */
public class Cls_Check {

    String DocNo ;
    String CheckNo ;
    String CheckDate ;
    String BankNo ;

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    String BankName ;
    String Amnt ;
    String UserID ;
    Integer Post ;

    public Integer getSer() {
        return ser;
    }

    public void setSer(Integer ser) {
        this.ser = ser;
    }

    Integer ser ;


    public String getBankNo() {
        return BankNo;
    }

    public void setBankNo(String bankNo) {
        BankNo = bankNo;
    }

    public String getDocNo() {
        return DocNo;
    }

    public void setDocNo(String docNo) {
        DocNo = docNo;
    }

    public String getCheckNo() {
        return CheckNo;
    }

    public void setCheckNo(String checkNo) {
        CheckNo = checkNo;
    }

    public String getCheckDate() {
        return CheckDate;
    }

    public void setCheckDate(String checkDate) {
        CheckDate = checkDate;
    }

    public String getAmnt() {
        return Amnt;
    }

    public void setAmnt(String amnt) {
        Amnt = amnt;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public Integer getPost() {
        return Post;
    }

    public void setPost(Integer post) {
        Post = post;
    }




}
