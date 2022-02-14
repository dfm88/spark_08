package it.polito.bigdata.spark.example;

import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class Register implements Serializable {
    private int station;
    private Timestamp timestamp;
    private int used_slots;
    private int free_slots;

    public Register(int station, Timestamp timestamp, int used_slots, int free_slots) {
        this.station = station;
        this.timestamp = timestamp;
        this.used_slots = used_slots;
        this.free_slots = free_slots;
    }
    public Register() {
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    public Timestamp getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getUsed_slots() {
        return used_slots;
    }

    public void setUsed_slots(int used_slots) {
        this.used_slots = used_slots;
    }

    public int getFree_slots() {
        return free_slots;
    }

    public void setFree_slots(int free_slots) {
        this.free_slots = free_slots;
    }
}
