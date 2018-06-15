package com.hz.junxinbaoan.params;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class GetSignDetailParam extends BaseParam {
    private String attendanceDate;
    private String attendanceEmployeeId;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getAttendanceEmployeeId() {
        return attendanceEmployeeId;
    }

    public void setAttendanceEmployeeId(String attendanceEmployeeId) {
        this.attendanceEmployeeId = attendanceEmployeeId;
    }

    @Override
    public String toString() {
        return "GetSignDetailParam{" +
                "attendanceDate='" + attendanceDate + '\'' +
                ", attendanceEmployeeId='" + attendanceEmployeeId + '\'' +
                '}';
    }
}
