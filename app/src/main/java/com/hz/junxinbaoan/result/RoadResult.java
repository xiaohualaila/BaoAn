package com.hz.junxinbaoan.result;

import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * Created by linzp on 2017/11/2.
 */

public class RoadResult extends BaseResult {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * locationId : 057c82a2-2e70-4967-8225-d53f750157eb
         * locationEmployeeId : d653df7d-4e45-4b86-9b64-d03a2f3c9a70
         * locationLongitude : 120.106209
         * locationLatitude : 30.337113
         * gmtCreate : 1509596735364
         * gmtModified : 1509596735364
         */

        private String locationId;
        private String locationEmployeeId;
        private double locationLongitude;
        private double locationLatitude;
        private long gmtCreate;
        private long gmtModified;

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getLocationEmployeeId() {
            return locationEmployeeId;
        }

        public void setLocationEmployeeId(String locationEmployeeId) {
            this.locationEmployeeId = locationEmployeeId;
        }

        public double getLocationLongitude() {
            return locationLongitude;
        }

        public void setLocationLongitude(double locationLongitude) {
            this.locationLongitude = locationLongitude;
        }

        public double getLocationLatitude() {
            return locationLatitude;
        }

        public void setLocationLatitude(double locationLatitude) {
            this.locationLatitude = locationLatitude;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public long getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(long gmtModified) {
            this.gmtModified = gmtModified;
        }

        public LatLng getLl() {
            return new LatLng(locationLatitude,locationLongitude);
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "locationId='" + locationId + '\'' +
                    ", locationEmployeeId='" + locationEmployeeId + '\'' +
                    ", locationLongitude=" + locationLongitude +
                    ", locationLatitude=" + locationLatitude +
                    ", gmtCreate=" + gmtCreate +
                    ", gmtModified=" + gmtModified +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RoadResult{" +
                "data=" + data +
                '}';
    }
}
