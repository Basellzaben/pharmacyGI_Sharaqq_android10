package com.cds_jo.pharmacyGI;

/**
 * Created by Hp on 11/11/2016.
 */
public class Doctor {
    String No ;
    String Name;
    String Loct;
    String Spec;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLoct() {
        return Loct;
    }

    public void setLoct(String loct) {
        Loct = loct;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    String Status;
}
