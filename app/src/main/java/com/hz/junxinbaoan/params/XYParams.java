package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/11/3.
 */

public class XYParams extends BaseParam {
//    locationLongitude	经度	是
//    locationLatitude	纬度	是
    String locationLongitude,locationLatitude;

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }
}
