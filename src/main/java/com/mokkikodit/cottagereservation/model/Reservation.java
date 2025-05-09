package com.mokkikodit.cottagereservation.model;

public class Reservation {

    private int reservationId;
    private int userId;
    private int cottageId;
    private int guestAmount;
    private String startDate;
    private String endDate;
    private String additionalInfo;
    private String reservationStatus;
    private boolean paymentStatus;

    public Reservation(int reservationId, int userId, int cottageId, int guestAmount, String startDate, String endDate, String reservationStatus, boolean paymentStatus, String additionalInfo) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.cottageId = cottageId;
        this.guestAmount = guestAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationStatus = reservationStatus;
        this.paymentStatus = paymentStatus;
        this.additionalInfo = additionalInfo;
    }

    public int getReservationId() {
        return reservationId;
    }
    public void setReservationId(int reservationID) {
        this.reservationId = reservationID;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userID) {
        this.userId = userID;
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

    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getStartDate() { return startDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getEndDate() { return endDate; }

    public String getSpanOfReservation() { return startDate + " - " + endDate; }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}

