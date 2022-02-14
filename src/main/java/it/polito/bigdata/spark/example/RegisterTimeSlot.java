package it.polito.bigdata.spark.example;

import java.io.Serializable;
@SuppressWarnings("serial")
public class RegisterTimeSlot implements Serializable {
    private int station_id;
    private String time_slot;
    private long critical_occ;
    private long total_occ;

    public RegisterTimeSlot() {
    }

    public RegisterTimeSlot(int station_id, String time_slot, int critical_occ, int total_occ) {
        this.station_id = station_id;
        this.time_slot = time_slot;
        this.critical_occ = critical_occ;
        this.total_occ = total_occ;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public long getCritical_occ() {
        return critical_occ;
    }

    public void setCritical_occ(long critical_occ) {
        this.critical_occ = critical_occ;
    }

    public long getTotal_occ() {
        return total_occ;
    }

    public void setTotal_occ(long total_occ) {
        this.total_occ = total_occ;
    }
}