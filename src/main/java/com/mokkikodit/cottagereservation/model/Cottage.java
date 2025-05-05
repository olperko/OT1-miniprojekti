package com.mokkikodit.cottagereservation.model;

import java.io.Serializable;

public class Cottage implements Serializable {

    private int id;
    private int owner_id;
    private double area;
    private double price;
    private int capacity;
    private boolean isReserved;
    private String cottageName;
    private String location;
    private String description;

    public Cottage() {
    }

    public Cottage(int cottageID, String cottageName, String location) {
        this.id = cottageID;
        this.cottageName = cottageName;
        this.location = location;
    }

    public Cottage(int cottageID, int owner_id, double area, double price, int capacity, String cottageName, boolean isReserved, String location, String description) {
        this.id = cottageID;
        this.owner_id = owner_id;
        this.area = area;
        this.price = price;
        this.capacity = capacity;
        this.cottageName = cottageName;
        this.isReserved = isReserved;
        this.location = location;
        this.description = description;
    }

    public Cottage(int id, int ownerId, String name, String location, double price, double size, int capacity, String description) {
        this.id = id;
        this.owner_id = ownerId;
        this.cottageName = name;
        this.location = location;
        this.price = price;
        this.area = size;
        this.capacity = capacity;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public double getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public double getPrice() {
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
