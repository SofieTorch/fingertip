package com.picateclas.fingertip.Models;

public class DataSensor {
    //atributtes
    private  String hr;
    private String spo2;

    //cosntructor
    public DataSensor(String hr, String spo2)
    {
        this.hr = hr;
        this.spo2 = spo2;
    }

    //Setters and Getters
    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }
}
