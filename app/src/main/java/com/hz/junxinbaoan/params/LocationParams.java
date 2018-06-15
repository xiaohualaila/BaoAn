package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/11/2.
 */

public class LocationParams extends BaseParam {

//    locationEmployeeId	员工id
//    timeFrom	起始时间
//    timeTo	结束时间

    private String locationEmployeeId,timeFrom,timeTo,time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocationEmployeeId() {
        return locationEmployeeId;
    }

    public void setLocationEmployeeId(String locationEmployeeId) {
        this.locationEmployeeId = locationEmployeeId;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    @Override
    public String toString() {
        return "LocationParams{" +
                "locationEmployeeId='" + locationEmployeeId + '\'' +
                ", timeFrom='" + timeFrom + '\'' +
                ", timeTo='" + timeTo + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
