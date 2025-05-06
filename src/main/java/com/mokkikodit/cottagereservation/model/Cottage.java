package com.mokkikodit.cottagereservation.model;

import java.io.Serializable;

public class Cottage implements Serializable {

    private int cottageId;
    private boolean reserved;
    private int ownerId;
    private double area;
    private double price;
    private int capacity;
    private String cottageName;
    private String location;
    private String description;

    public Cottage(int cottageId, boolean reserved, int ownerId, String cottageName, String location, double area, double price, int capacity, String description) {
        this.cottageId = cottageId;
        this.reserved = reserved;
        this.ownerId = ownerId;
        this.cottageName = cottageName;
        this.location = location;
        this.price = price;
        this.area = area;
        this.capacity = capacity;
        this.description = description;
    }

    public void setCottageId(int cottageId) { this.cottageId = cottageId; }
    public int getCottageId() { return cottageId;
    }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public int getOwnerId() { return ownerId;
    }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
    public boolean getReserved() { return reserved;
    }
    public void setCottageName(String cottageName) { this.cottageName = cottageName; }
    public String getCottageName() { return cottageName;
    }
    public void setLocation(String location) { this.location = location; }
    public String getLocation() { return location;
    }
    public void setPrice(double price) { this.price = price; }
    public double getPrice() { return price;
    }
    public void setArea(double area) { this.area = area; }
    public double getArea() { return area;
    }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getCapacity() { return capacity;
    }
    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description;
    }

}
