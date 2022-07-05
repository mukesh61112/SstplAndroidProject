package com.example.siotel.models;

public class HouseholdsDetailsModel {

      String MeterSN;
      String cum_eb_kwh;
      String balance_amount;
      String date;

    public HouseholdsDetailsModel() {
    }


    public HouseholdsDetailsModel(String meterSN, String cum_eb_kwh, String balance_amount, String date) {
        MeterSN = meterSN;
        this.cum_eb_kwh = cum_eb_kwh;
        this.balance_amount = balance_amount;
        this.date = date;

    }


    public String getMeterSN() {
        return MeterSN;
    }

    public void setMeterSN(String meterSN) {
        MeterSN = meterSN;
    }

    public String getCum_eb_kwh() {
        return cum_eb_kwh;
    }

    public void setCum_eb_kwh(String cum_eb_kwh) {
        this.cum_eb_kwh = cum_eb_kwh;
    }

    public String getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(String balance_amount) {
        this.balance_amount = balance_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
