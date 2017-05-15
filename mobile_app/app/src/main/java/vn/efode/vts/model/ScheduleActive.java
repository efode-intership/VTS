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
    private float speed;

    public ScheduleActive() {
    }

    public ScheduleActive(String scheduleId, String deviceId, double locationLat, double loccationLong, float speed) {
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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
