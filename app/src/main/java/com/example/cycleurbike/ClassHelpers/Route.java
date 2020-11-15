package com.example.cycleurbike.ClassHelpers;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {

    String routeName;
    String time;
    String distance;
    String avgSpeed;

    String dateOfRoute;
    String hourOfRoute;

    String imageMapUri;

    ArrayList<LocationHelper> locationHelpers;

    public Route() {
        locationHelpers = new ArrayList<>();
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getDateOfRoute() {
        return dateOfRoute;
    }

    public void setDateOfRoute(String dateOfRoute) {
        this.dateOfRoute = dateOfRoute;
    }

    public String getHourOfRoute() {
        return hourOfRoute;
    }

    public void setHourOfRoute(String hourOfRoute) {
        this.hourOfRoute = hourOfRoute;
    }

    public String getImageMapUri() {
        return imageMapUri;
    }

    public void setImageMapUri(String imageMapUri) {
        this.imageMapUri = imageMapUri;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public ArrayList<LocationHelper> getLocationHelpers() {

        if (locationHelpers == null){
            locationHelpers = new ArrayList<>();
        }
        return locationHelpers;
    }

    public void setLocationHelpers(ArrayList<LocationHelper> locationHelpers) {
        this.locationHelpers = locationHelpers;
    }


    @Override
    public String toString() {
        return "Route{" +
                "routeName='" + routeName + '\'' +
                ", time='" + time + '\'' +
                ", distance='" + distance + '\'' +
                ", avgSpeed='" + avgSpeed + '\'' +
                ", dateOfRoute='" + dateOfRoute + '\'' +
                ", hourOfRoute='" + hourOfRoute + '\'' +
                ", imageMapUri='" + imageMapUri + '\'' +
                '}';
    }
}
