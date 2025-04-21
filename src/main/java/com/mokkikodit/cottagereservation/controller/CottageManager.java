package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.CottageDAO;

public class CottageManager {
    CottageDAO cottageDAO;

    public void addCottage(int ownerID, int reserved, String name, String location, int price, int area, int capacity, String description) {
        cottageDAO.insertCottage(ownerID, reserved, name, location, price, area, capacity, description);
    }



}
