package com.example.drassit.ui.model;

public class MyLocation {
    private String id_location;
    private String location;

    public MyLocation() {
    }

    public MyLocation(String id_location, String location) {
        this.id_location = id_location;
        this.location = location;
    }

    public String getId_location() {
        return id_location;
    }

    public void setId_location(String id_location) {
        this.id_location = id_location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
