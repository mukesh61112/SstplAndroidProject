package com.example.siotel.models;

public class CurrentRechargeResponse {
   int    Amount;
   String kwh_d;

    public CurrentRechargeResponse() {
    }

    public CurrentRechargeResponse(int amount, String kwh_d) {
        Amount = amount;
        this.kwh_d = kwh_d;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getKwh_d() {
        return kwh_d;
    }

    public void setKwh_d(String kwh_d) {
        this.kwh_d = kwh_d;
    }
}
