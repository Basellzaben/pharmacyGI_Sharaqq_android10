package com.cds_jo.pharmacyGI.assist;

/**
 * Created by Hp on 08/01/2017.
 */

public class Cls_TransQtyItems {

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    public String getItemNm() {
        return ItemNm;
    }

    public void setItemNm(String itemNm) {
        ItemNm = itemNm;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getUnit_Rate() {
        return Unit_Rate;
    }

    public void setUnit_Rate(String unit_Rate) {
        Unit_Rate = unit_Rate;
    }

    String ItemNo;
    String ItemNm;
    String Qty;
    String Unit;
    String Unit_Rate;

    public String getUnitNm() {
        return UnitNm;
    }

    public void setUnitNm(String unitNm) {
        UnitNm = unitNm;
    }

    String UnitNm;
}

