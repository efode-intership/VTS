package vn.efode.vts.model;

import io.realm.RealmObject;

/**
 * Created by Tuan on 09/05/2017.
 */

public class ScheduleActive extends RealmObject {
    private String scheduleId;
    private String deviceId;
    private double locationLat;
    private double loccationLong;
    private int speed;

    public ScheduleActive() {
    }

    public ScheduleActive(String scheduleId, String deviceId, double locationLat, double loccationLong, int speed) {
        this.scheduleId = scheduleId;
        this.deviceId = deviceId;
        this.locationLat = locationLat;
        this.loccationLong = loccationLong;
        this.speed = speed;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLoccationLong() {
        return loccationLong;
    }

    public void setLoccationLong(double loccationLong) {
        this.loccationLong = loccationLong;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
