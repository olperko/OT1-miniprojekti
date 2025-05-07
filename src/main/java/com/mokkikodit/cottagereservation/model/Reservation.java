package com.mokkikodit.cottagereservation.model;

public class Reservation {

    private int reservationID;
    private int userID;
    private int cottageId;
    private int ownerID;
    private int guestAmount;
    private String beginningDate;
    private String beginningTime;
    private String endDate;
    private String endTime;
    private String reservationStatus;
    private boolean paymentStatus;

    public Reservation(int reservationID, int userID, int cottageId, int ownerID, int guestAmount, String beginningDate, String beginningTime, String endDate, String endTime, String reservationStatus, boolean paymentStatus) {
        this.reservationID = reservationID;
        this.userID = userID;
        this.cottageId = cottageId;
        this.ownerID = ownerID;
        this.guestAmount = guestAmount;
        this.beginningDate = beginningDate;
        this.beginningTime = beginningTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.reservationStatus = reservationStatus;
        this.paymentStatus = paymentStatus;

    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCottageId() {
        return cottageId;
    }

    public void setCottageId(int cottageId) {
        this.cottageId = cottageId;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getGuestAmount() {
        return guestAmount;
    }

    public void setGuestAmount(int guestAmount) {
        this.guestAmount = guestAmount;
    }

    public String getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(String beginningDate) {
        this.beginningDate = beginningDate;
    }

    public String getBeginningTime() {
        return beginningTime;
    }

    public void setBeginningTime(String beginningTime) {
        this.beginningTime = beginningTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}

