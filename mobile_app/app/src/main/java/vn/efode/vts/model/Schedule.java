package vn.efode.vts.model;

/**
 * Created by CongVu on 4/4/2017.
 */

public class Schedule {
    private int scheduleId;
    private int driverId;
    private int vehicleId;
    private String addressStart;
    private String addressEnd;
    private String intendStartTime;
    private String intendEndTime;
    private int scheduleStatusTypeId;
    private String locationLatStart;
    private String locationLatEnd;
    private String locationLongEnd;
    private String realStartTime;
    private String realEndTime;
    private String deviceId;

    public Schedule() {
    }

    public Schedule(int scheduleId, int driverId, int vehicleId, String addressStart, String addressEnd, String intendStartTime, String intendEndTime, int scheduleStatusTypeId, String locationLatStart, String locationLatEnd, String locationLongEnd, String realStartTime, String realEndTime, String deviceId) {
        this.scheduleId = scheduleId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.addressStart = addressStart;
        this.addressEnd = addressEnd;
        this.intendStartTime = intendStartTime;
        this.intendEndTime = intendEndTime;
        this.scheduleStatusTypeId = scheduleStatusTypeId;
        this.locationLatStart = locationLatStart;
        this.locationLatEnd = locationLatEnd;
        this.locationLongEnd = locationLongEnd;
        this.realStartTime = realStartTime;
        this.realEndTime = realEndTime;
        this.deviceId = deviceId;
    }

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

    public String getAddressStart() {
        return addressStart;
    }

    public void setAddressStart(String addressStart) {
        this.addressStart = addressStart;
    }

    public String getAddressEnd() {
        return addressEnd;
    }

    public void setAddressEnd(String addressEnd) {
        this.addressEnd = addressEnd;
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
}
