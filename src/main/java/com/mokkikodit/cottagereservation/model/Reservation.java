package com.mokkikodit.cottagereservation.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {

    private int reservationId;
    private int userId;
    private int cottageId;
    private int guestAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String additionalInfo;
    private String reservationStatus;
    private boolean paymentStatus;

    public Reservation(int reservationId, int userId, int cottageId, int guestAmount, LocalDate startDate, LocalDate endDate, String reservationStatus, boolean paymentStatus, String additionalInfo) {
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

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getStartDate() { return startDate; }

    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public LocalDate getEndDate() { return endDate; }


    public String getSpanOfReservation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}

