package com.hz.junxinbaoan.params;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class SignParam extends BaseParam {
    private String attendanceId;
    private double attendanceLon;
    private double attendanceLat;
    private String attendanceAddress;
    private String attendanceIbeaconId;
    private String attendanceIbeaconName;

    //新增功能 增加图片attendancePictures
    private String attendancePictures;

    public String getAttendancePictures() {
        return attendancePictures;
    }

    public void setAttendancePictures(String attendancePictures) {
        this.attendancePictures = attendancePictures;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public double getAttendanceLon() {
        return attendanceLon;
    }

    public void setAttendanceLon(double attendanceLon) {
        this.attendanceLon = attendanceLon;
    }

    public double getAttendanceLat() {
        return attendanceLat;
    }

    public void setAttendanceLat(double attendanceLat) {
        this.attendanceLat = attendanceLat;
    }

    public String getAttendanceAddress() {
        return attendanceAddress;
    }

    public void setAttendanceAddress(String attendanceAddress) {
        this.attendanceAddress = attendanceAddress;
    }

    public String getAttendanceIbeaconId() {
        return attendanceIbeaconId;
    }

    public void setAttendanceIbeaconId(String attendanceIbeaconId) {
        this.attendanceIbeaconId = attendanceIbeaconId;
    }

    public String getAttendanceIbeaconName() {
        return attendanceIbeaconName;
    }

    public void setAttendanceIbeaconName(String attendanceIbeaconName) {
        this.attendanceIbeaconName = attendanceIbeaconName;
    }

    @Override
    public String toString() {
        return "SignParam{" +
                "attendanceId='" + attendanceId + '\'' +
                ", attendanceLon=" + attendanceLon +
                ", attendanceLat=" + attendanceLat +
                ", attendanceAddress='" + attendanceAddress + '\'' +
                ", attendanceIbeaconId='" + attendanceIbeaconId + '\'' +
                ", attendanceIbeaconName='" + attendanceIbeaconName + '\'' +
                ", attendancePictures='" + attendancePictures + '\'' +
                '}';
    }
}
