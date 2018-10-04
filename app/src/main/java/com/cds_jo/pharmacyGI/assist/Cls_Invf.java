package com.cds_jo.pharmacyGI.assist;

/**
 * Created by Hp on 16/03/2016.
 */
public class Cls_Invf {
    String  Item_No  ;
    String  Item_Name ;
    String  Ename;
    String  Unit;
    String  Price;
    String  OL;
    String  OQ1;
    String  Type_No;
    String  Pack;
    String  Place;
    String  dno;

    public int getRowNumber() {
        return RowNumber;
    }

    public void setRowNumber(int rowNumber) {
        RowNumber = rowNumber;
    }

    int RowNumber;
    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getBounce() {
        return Bounce;
    }

    public void setBounce(String bounce) {
        Bounce = bounce;
    }

    String Qty;
    String Bounce;

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getDno() {
        return dno;
    }

    public void setDno(String dno) {
        this.dno = dno;
    }

    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public String getItem_No() {
        return Item_No;
    }

    public void setItem_No(String item_No) {
        Item_No = item_No;
    }

    public String getOL() {
        return OL;
    }

    public void setOL(String OL) {
        this.OL = OL;
    }

    public String getOQ1() {
        return OQ1;
    }

    public void setOQ1(String OQ1) {
        this.OQ1 = OQ1;
    }

    public String getPack() {
        return Pack;
    }

    public void setPack(String pack) {
        Pack = pack;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getType_No() {
        return Type_No;
    }

    public void setType_No(String type_No) {
        Type_No = type_No;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    String  tax ;
}
