package com.mokkikodit.cottagereservation.model;

import java.time.LocalDate;

public class Reservation {

    public String getSpanOfReservation() {
        return spanOfReservation;
    }

    public void setSpanOfReservation(String spanOfReservation) {
        this.spanOfReservation = spanOfReservation;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    private int reservationID;
    private int userID;
    private int cottageId;
    private int guestAmount;
    private String spanOfReservation;
    private String additionalInfo;
    private String reservationStatus;
    private boolean paymentStatus;

    public Reservation(int reservationID, int userID, int cottageId, int guestAmount, String spanOfReservation, String reservationStatus, boolean paymentStatus, String additionalInfo) {
        this.reservationID = reservationID;
        this.userID = userID;
        this.cottageId = cottageId;
        this.guestAmount = guestAmount;
        this.spanOfReservation = spanOfReservation;
        this.reservationStatus = reservationStatus;
        this.paymentStatus = paymentStatus;
        this.additionalInfo = additionalInfo;
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

    public int getGuestAmount() {
        return guestAmount;
    }
    public void setGuestAmount(int guestAmount) {
        this.guestAmount = guestAmount;
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

