package com.example.siotel.models;

public class CurrAmountRequestModel {
    String email;
    String devEui;

    public CurrAmountRequestModel() {
    }

    public CurrAmountRequestModel(String email, String devEui) {
        this.email = email;
        this.devEui = devEui;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevEui() {
        return devEui;
    }

    public void setDevEui(String devEui) {
        this.devEui = devEui;
    }
}
