package com.hunegroup.hune.util;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tskil on 8/1/2017.
 */

public class SessionLocation {
    LatLng location;
    LatLng locationCurrent;
    int type;
    double radius;
    private static SessionLocation instance;

    public boolean isLatLng()
    {
        if(location==null)
            return false;
        return true;
    }
    public boolean isLatLng_Current()
    {
        if(locationCurrent==null)
            return false;
        return true;
    }
    public LatLng getLocationCurrent() {
        return locationCurrent;
    }

    public void setLocationCurrent(LatLng locationCurrent) {
        this.locationCurrent = locationCurrent;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public SessionLocation(LatLng location, int type, double radius) {
        this.location = location;
        this.type = type;
        this.radius = radius;
    }
    public static SessionLocation getInstance(){
        if(instance==null)
            instance=new SessionLocation();
        return instance;
    }
    public SessionLocation() {
    }
}
