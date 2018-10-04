package com.cds_jo.pharmacyGI.assist;

/**
 * Created by Hp on 08/03/2016.
 */
public class Cls_Trans_Qty {
    public String getItem_Name() {
        return Item_Name;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    String StoreName ;
    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    String Item_Name ;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getFromstore() {
        return fromstore;
    }

    public void setFromstore(String fromstore) {
        this.fromstore = fromstore;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getMyear() {
        return myear;
    }

    public void setMyear(String myear) {
        this.myear = myear;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTostore() {
        return tostore;
    }

    public void setTostore(String tostore) {
        this.tostore = tostore;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getUnitRate() {
        return UnitRate;
    }

    public void setUnitRate(String unitRate) {
        UnitRate = unitRate;
    }

    String  date;
            String fromstore;
            String tostore;
            String des;
            String docno;
            String itemno;
            String qty;
            String UnitNo;
            String UnitRate;
            String myear;

}
