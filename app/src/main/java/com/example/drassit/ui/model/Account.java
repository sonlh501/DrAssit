package com.example.drassit.ui.model;

import java.util.HashMap;
import java.util.Map;

public class Account {
    private String name;
    private String phone;
    private String address;
    private String card;
    private float gas_expired, oil_expired;

    private String id;
    private String tokenID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }



    public Account() {

    }
    public Account(String id, String tokenID, String name, String phone, String address, String card, float gas_expired, float oil_expired) {
        this.id = id;
        this.tokenID = tokenID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.card = card;
        this.gas_expired = gas_expired;
        this.oil_expired = oil_expired;
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


    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("tokenID", tokenID);
        result.put("name", name);
        result.put("phone", phone);
        result.put("address", address);
        result.put("card", card);
        result.put("gas_expired", gas_expired);
        result.put("oil_expired", oil_expired);
        return result;
    }

}
