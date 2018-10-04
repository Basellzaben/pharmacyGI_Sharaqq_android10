package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 18/03/2016.
 */
public class Cls_ItemCost {

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnitcost() {
        return unitcost;
    }

    public void setUnitcost(String unitcost) {
        this.unitcost = unitcost;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getVenname() {
        return venname;
    }

    public void setVenname(String venname) {
        this.venname = venname;
    }

    String  bill_no ;
    String bill_date ;
    String venname ;
    String qty ;
    String cost ;
    String UnitName ;
    String   unitcost ;

}
