package com.mokkikodit.cottagereservation.model;

public class Statistics {

    private int totalReservations;
    private int totalSales;
    private int totalUsers;
    private int totalCottages;
    private String month;
    private String year;

    public Statistics() {}

    public Statistics(String month, String year) {
        this.month = month;
        this.year = year;
    }

    public Statistics(int totalReservations, int totalSales, int totalUsers, int totalCottages, String month, String year) {
        this.totalReservations = totalReservations;
        this.totalSales = totalSales;
        this.totalUsers = totalUsers;
        this.totalCottages = totalCottages;
        this.month = month;
        this.year = year;
    }

    public int getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(int totalReservations) {
        this.totalReservations = totalReservations;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalCottages() {
        return totalCottages;
    }

    public void setTotalCottages(int totalCottages) {
        this.totalCottages = totalCottages;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
