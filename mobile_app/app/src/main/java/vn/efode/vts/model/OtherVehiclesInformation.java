package vn.efode.vts.model;

import java.io.Serializable;

/**
 * Other Vehicles Information
 */

public class OtherVehiclesInformation implements Serializable {
    private int scheduleId;
    private String locationLat;
    private String locationLong;
    private String maxDate;
    private double distance;
    private String driverName;
    private String driverPhone;

    public OtherVehiclesInformation() {
    }

    public OtherVehiclesInformation(int scheduleId, String locationLat, String locationLong, String maxDate, double distance, String driverName, String driverPhone) {
        this.scheduleId = scheduleId;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.maxDate = maxDate;
        this.distance = distance;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }
}

