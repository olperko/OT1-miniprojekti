package com.mokkikodit.cottagereservation.model;

public class Payment {

    private int paymentId;
    private int reservationId;
    private int amount;
    private String paymentType;
    private String paymentStatus;
    private String paymentDate;

    public Payment(int paymentId, int reservationId, int amount, String paymentType, String paymentStatus, String confirmatonDate) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.paymentDate = confirmatonDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
