package com.mokkikodit.cottagereservation.model;

import java.io.Serializable;

public class Cottage implements Serializable {

    private int cottageID;
    private int ownerID;
    private int area;
    private int price;
    private int capacity;
    private boolean isReserved;
    private String cottageName;
    private String location;
    private String description;

    public Cottage() {
    }

    public Cottage(int cottageID, int ownerID, int area, int price, int capacity, String cottageName, boolean isReserved, String location, String description) {
        this.cottageID = cottageID;
        this.ownerID = ownerID;
        this.area = area;
        this.price = price;
        this.capacity = capacity;
        this.cottageName = cottageName;
        this.isReserved = isReserved;
        this.location = location;
        this.description = description;
    }

    public int getCottageID() {
        return cottageID;
    }

    public void setCottageID(int cottageID) {
        this.cottageID = cottageID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public String getCottageName() {
        return cottageName;
    }

    public void setCottageName(String cottageName) {
        this.cottageName = cottageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
