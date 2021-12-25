package com.example.drassit.ui.model;

public class Account {
    private String id;
    private String tokenID;

    private float gas_expired, oil_expired;

    public Account() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getGas_expired() {
        return gas_expired;
    }

    public void setGas_expired(float gas_expired) {
        this.gas_expired = gas_expired;
    }

    public float getOil_expired() {
        return oil_expired;
    }

    public void setOil_expired(float oil_expired) {
        this.oil_expired = oil_expired;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }
}
