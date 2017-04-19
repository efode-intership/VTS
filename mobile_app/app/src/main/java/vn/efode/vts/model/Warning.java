package vn.efode.vts.model;

import java.util.Date;

/**
 * Created by Nam on 19/04/2017.
 */

public class Warning {
    private int driverId;
    private int warningTypeId;
    private int warningId;
    private Double locationLat;
    private Double LocationLong;
    private String description;
    private Date startTime;
    private Date endTime;

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getWarningTypeId() {
        return warningTypeId;
    }

    public void setWarningTypeId(int warningTypeId) {
        this.warningTypeId = warningTypeId;
    }

    public int getWarningId() {
        return warningId;
    }

    public void setWarningId(int warningId) {
        this.warningId = warningId;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLong() {
        return LocationLong;
    }

    public void setGetLocationLong(Double getLocationLong) {
        this.LocationLong = getLocationLong;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
