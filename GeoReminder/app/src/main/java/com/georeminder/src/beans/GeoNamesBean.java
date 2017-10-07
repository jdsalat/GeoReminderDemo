package com.georeminder.src.beans;

import java.io.Serializable;

/**
 * Created by Javed.Salat on 13-Sep-16.
 */
public class GeoNamesBean implements Serializable {

    private static final long serialVersionUID = -5132291594604907940L;
    private String id;
    private String geoFencingName;
    private float geoFencingRadius;
    private double latitude;
    private double longitude;
    private String geoAddress;
    private String notes;

    public String getGeoFencingName() {
        return geoFencingName;
    }

    public void setGeoFencingName(String geoFencingName) {
        this.geoFencingName = geoFencingName;
    }

    public float getGeoFencingRadius() {
        return geoFencingRadius;
    }

    public void setGeoFencingRadius(float geoFencingRadius) {
        this.geoFencingRadius = geoFencingRadius;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGeoAddress() {
        return geoAddress;
    }


    public void setGeoAddress(String geoAddress) {
        this.geoAddress = geoAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "GeoNamesBean{" +
                "id='" + id + '\'' +
                ", geoFencingName='" + geoFencingName + '\'' +
                ", geoFencingRadius=" + geoFencingRadius +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", geoAddress='" + geoAddress + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
