package vn.efode.vts.model;

import java.io.Serializable;

/**
 * Created by CongVu on 4/4/2017.
 */

public class Schedule implements Serializable{
    private int scheduleId;
    private int driverId;
    private int vehicleId;
    private String startPointAddress;
    private String endPointAddress;
    private String intendStartTime;
    private String intendEndTime;
    private int scheduleStatusTypeId;
    private String locationLatStart;
    private String locationLongStart;
    private String locationLatEnd;
    private String locationLongEnd;
    private String realStartTime;
    private String realEndTime;
    private String deviceId;
    private String scheduleStatusName;
    private String scheduleStatusDescription;

    public Schedule() {
    }


    public Schedule(int scheduleId, int driverId, int vehicleId, String startPointAddress, String endPointAddress, String intendStartTime, String intendEndTime, int scheduleStatusTypeId, String locationLatStart, String locationLongStart, String locationLatEnd, String locationLongEnd, String realStartTime, String realEndTime, String deviceId, String scheduleStatusName, String scheduleStatusDescription) {
        this.scheduleId = scheduleId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.startPointAddress = startPointAddress;
        this.endPointAddress = endPointAddress;
        this.intendStartTime = intendStartTime;
        this.intendEndTime = intendEndTime;
        this.scheduleStatusTypeId = scheduleStatusTypeId;
        this.locationLatStart = locationLatStart;
        this.locationLongStart = locationLongStart;
        this.locationLatEnd = locationLatEnd;
        this.locationLongEnd = locationLongEnd;
        this.realStartTime = realStartTime;
        this.realEndTime = realEndTime;
        this.deviceId = deviceId;
        this.scheduleStatusName = scheduleStatusName;
        this.scheduleStatusDescription = scheduleStatusDescription;
    }

//    public Schedule(String locationLatStart) {
//
//    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getStartPointAddress() {
        return startPointAddress;
    }

    public void setStartPointAddress(String startPointAddress) {
        this.startPointAddress = startPointAddress;
    }

    public String getEndPointAddress() {
        return endPointAddress;
    }

    public void setEndPointAddress(String endPointAddress) {
        this.endPointAddress = endPointAddress;
    }

    public String getIntendStartTime() {
        return intendStartTime;
    }

    public void setIntendStartTime(String intendStartTime) {
        this.intendStartTime = intendStartTime;
    }

    public String getIntendEndTime() {
        return intendEndTime;
    }

    public void setIntendEndTime(String intendEndTime) {
        this.intendEndTime = intendEndTime;
    }

    public int getScheduleStatusTypeId() {
        return scheduleStatusTypeId;
    }

    public void setScheduleStatusTypeId(int scheduleStatusTypeId) {
        this.scheduleStatusTypeId = scheduleStatusTypeId;
    }

    public String getLocationLatStart() {
        return locationLatStart;
    }

    public void setLocationLatStart(String locationLatStart) {
        this.locationLatStart = locationLatStart;
    }

    public String getLocationLongStart() {
        return locationLongStart;
    }

    public void setLocationLongStart(String locationLongStart) {
        this.locationLongStart = locationLongStart;
    }

    public String getLocationLatEnd() {
        return locationLatEnd;
    }

    public void setLocationLatEnd(String locationLatEnd) {
        this.locationLatEnd = locationLatEnd;
    }

    public String getLocationLongEnd() {
        return locationLongEnd;
    }

    public void setLocationLongEnd(String locationLongEnd) {
        this.locationLongEnd = locationLongEnd;
    }

    public String getRealStartTime() {
        return realStartTime;
    }

    public void setRealStartTime(String realStartTime) {
        this.realStartTime = realStartTime;
    }

    public String getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(String realEndTime) {
        this.realEndTime = realEndTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getScheduleStatusName() {
        return scheduleStatusName;
    }

    public void setScheduleStatusName(String scheduleStatusName) {
        this.scheduleStatusName = scheduleStatusName;
    }

    public String getScheduleStatusDescription() {
        return scheduleStatusDescription;
    }

    public void setScheduleStatusDescription(String scheduleStatusDescription) {
        this.scheduleStatusDescription = scheduleStatusDescription;
    }
}
