package it.polito.bigdata.spark.example;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RegisterPercentGeo implements Serializable {
    private int station;
    private String day_of_week;
    private int hour;
    private double criticality;
    private String  longitude_ds;
    private String latitude_ds;

    public RegisterPercentGeo() {
    }

    public RegisterPercentGeo(int station, String day_of_week, int hour, double criticality, String longitude_ds, String latitude_ds) {
        this.station = station;
        this.day_of_week = day_of_week;
        this.hour = hour;
        this.criticality = criticality;
        this.longitude_ds = longitude_ds;
        this.latitude_ds = latitude_ds;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public String getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(String day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public double getCriticality() {
        return criticality;
    }

    public void setCriticality(double criticality) {
        this.criticality = criticality;
    }

    public String getLongitude_ds() {
        return longitude_ds;
    }

    public void setLongitude_ds(String longitude_ds) {
        this.longitude_ds = longitude_ds;
    }

    public String getLatitude_ds() {
        return latitude_ds;
    }

    public void setLatitude_ds(String latitude_ds) {
        this.latitude_ds = latitude_ds;
    }
}

