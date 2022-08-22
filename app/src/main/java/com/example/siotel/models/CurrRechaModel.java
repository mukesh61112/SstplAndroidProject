package com.example.siotel.models;

public class CurrRechaModel {
    String h_num;
    CurrentRechargeResponse currentRechargeResponse;

    public CurrRechaModel(String string) {
    }

    public CurrRechaModel(String h_num, CurrentRechargeResponse currentRechargeResponse) {
        this.h_num = h_num;
        this.currentRechargeResponse = currentRechargeResponse;
    }

    public String getH_num() {
        return h_num;
    }

    public void setH_num(String h_num) {
        this.h_num = h_num;
    }

    public CurrentRechargeResponse getCurrentRechargeResponse() {
        return currentRechargeResponse;
    }

    public void setCurrentRechargeResponse(CurrentRechargeResponse currentRechargeResponse) {
        this.currentRechargeResponse = currentRechargeResponse;
    }
}
