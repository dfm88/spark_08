package it.polito.bigdata.spark.example;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Station implements Serializable {
    private int id;
    private String longitude;
    private String latitude;
    private String name;

    public Station(int id, String longitudine, String latitudine, String name) {
        this.id = id;
        this.longitude = longitudine;
        this.latitude = latitudine;
        this.name = name;
    }
    public Station() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
