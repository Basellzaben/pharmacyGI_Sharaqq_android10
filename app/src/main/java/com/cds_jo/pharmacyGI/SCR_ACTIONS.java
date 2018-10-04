package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 14/06/2016.
 */
public enum SCR_ACTIONS {
    open (1),
    Save (2);

    private final int value;
    SCR_ACTIONS(int type)
    {
        this.value = type;
    }


    public int getValue() {

        return this.value;

    }

}
