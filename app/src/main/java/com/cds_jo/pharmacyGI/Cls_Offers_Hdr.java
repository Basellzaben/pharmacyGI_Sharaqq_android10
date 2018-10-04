package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 16/03/2016.
 */
public class Cls_Offers_Hdr {

    String ID  ;
    String  Offer_No;
    String Offer_Name;
    String Offer_Date;
    String Offer_Type;
    String Offer_Status;
    String Offer_Begin_Date;
    String Offer_Exp_Date;
    String Offer_Result_Type;
    String Offer_Dis;





    public String getTotalValue() {
        return TotalValue;
    }

    public void setTotalValue(String totalValue) {
        TotalValue = totalValue;
    }

    String TotalValue ;

    public String getOffer_Amt() {
        return Offer_Amt;
    }

    public void setOffer_Amt(String offer_Amt) {
        Offer_Amt = offer_Amt;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOffer_Begin_Date() {
        return Offer_Begin_Date;
    }

    public void setOffer_Begin_Date(String offer_Begin_Date) {
        Offer_Begin_Date = offer_Begin_Date;
    }

    public String getOffer_Date() {
        return Offer_Date;
    }

    public void setOffer_Date(String offer_Date) {
        Offer_Date = offer_Date;
    }

    public String getOffer_Dis() {
        return Offer_Dis;
    }

    public void setOffer_Dis(String offer_Dis) {
        Offer_Dis = offer_Dis;
    }

    public String getOffer_Exp_Date() {
        return Offer_Exp_Date;
    }

    public void setOffer_Exp_Date(String offer_Exp_Date) {
        Offer_Exp_Date = offer_Exp_Date;
    }

    public String getOffer_Name() {
        return Offer_Name;
    }

    public void setOffer_Name(String offer_Name) {
        Offer_Name = offer_Name;
    }

    public String getOffer_No() {
        return Offer_No;
    }

    public void setOffer_No(String offer_No) {
        Offer_No = offer_No;
    }

    public String getOffer_Result_Type() {
        return Offer_Result_Type;
    }

    public void setOffer_Result_Type(String offer_Result_Type) {
        Offer_Result_Type = offer_Result_Type;
    }

    public String getOffer_Status() {
        return Offer_Status;
    }

    public void setOffer_Status(String offer_Status) {
        Offer_Status = offer_Status;
    }

    public String getOffer_Type() {
        return Offer_Type;
    }

    public void setOffer_Type(String offer_Type) {
        Offer_Type = offer_Type;
    }

    String Offer_Amt;
}
