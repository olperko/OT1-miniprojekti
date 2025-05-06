package com.mokkikodit.cottagereservation.model;

public class Review {

    private int reviewID;
    private int userID;
    private int cottageId;
    private double score;
    private String comment;
    private String date;

    public Review(int userID) {
        this.userID = userID;
    }

    public Review(int reviewID, int userID, int cottageId, double score, String comment, String date) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.cottageId = cottageId;
        this.score = score;
        this.comment = comment;
        this.date = date;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
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

