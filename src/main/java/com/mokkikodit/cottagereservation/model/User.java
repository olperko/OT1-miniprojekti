package com.mokkikodit.cottagereservation.model;

public class User {

    private int userID;
    private String email;
    private String name;
    private boolean isBusiness;

    public User(int userID, boolean isBusiness, String email, String name) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.isBusiness = isBusiness;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public void setBusiness(boolean business) {
        isBusiness = business;
    }
}
