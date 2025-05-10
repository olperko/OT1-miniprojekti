package com.mokkikodit.cottagereservation.model;

public class User {

    private int userID;
    private String email;
    private String firstName;
    private String lastName;
    private String ownedCottages;
    private String phoneNumber;
    private boolean isBusiness;
    private String additionalInfo;

    public User(int userID, String firstName, String lastName, String email, String ownedCottages, String phoneNumber, boolean isBusiness, String additionalInfo) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.ownedCottages = ownedCottages;
        this.phoneNumber = phoneNumber;
        this.isBusiness = isBusiness;
        this.additionalInfo = additionalInfo;
    }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOwnedCottages() { return ownedCottages; }
    public void setOwnedCottages(String ownedCottages) { this.ownedCottages = ownedCottages; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String role) { this.phoneNumber = phoneNumber; }

    public boolean getIsBusiness() { return isBusiness; }
    public void setBusiness(boolean business) { isBusiness = business; }

    public String getAdditionalInfo() { return additionalInfo; }

    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

}
