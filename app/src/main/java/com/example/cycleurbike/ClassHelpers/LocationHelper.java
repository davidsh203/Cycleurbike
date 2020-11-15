package com.example.cycleurbike.ClassHelpers;

import java.io.Serializable;

public class LocationHelper implements Serializable {

    private double longitude;
    private double latitude;


    public LocationHelper() {
    }

    public LocationHelper(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    @Override
    public String toString() {
        return "LocationHelper{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
