package com.mokkikodit.cottagereservation.model;

public class Review {

    private int reviewId;
    private int reservationId;
    private double score;
    private String comment;
    private String date;


    public Review(int reviewId, int reservationId, double score, String comment, String date) {
        this.reviewId = reviewId;
        this.reservationId = reservationId;
        this.score = score;
        this.comment = comment;
        this.date = date;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewID) {
        this.reviewId = reviewID;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

