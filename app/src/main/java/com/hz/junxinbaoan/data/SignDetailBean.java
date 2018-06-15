package com.hz.junxinbaoan.data;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class SignDetailBean {
    private int attendanceStatusType;//0:未到签到时间 1:签到进行中（GPS） 2:签到进行中（蓝牙） 3:正常已签到  4:漏签到
    private int endType;//一个班次是不是最后一个 进行分班显示

    private String time;
    private String signtime;
    private String address;
    private int latertime;//迟到分钟
    /* 签到倒计时(分钟) */
    private int attendanceMinutesLeft;



    private String attendanceId;//id
    private String attendanceScheduleId;//排班规则id
    private String attendanceEmployeeId;//员工id
    private double attendanceLon;//签到经度
    private double attendanceLat;//签到纬度
    private double attendanceLonExpect;//期望签到经度
    private double attendanceLatExpect;//期望签到纬度
    private double attendanceLocationAround;//期望位置误差
    private String attendanceIbeaconId;//签到蓝牙基站id
    private String attendanceIbeaconIdExpect;//期望签到蓝牙基站id
    private String attendanceAddress;//实际签到地址
    private String attendanceAddressExpect;//期望签到地址
    private String attendanceIbeaconName;//实际签到蓝牙基站名称
    private String attendanceIbeaconNameExpect;//期望签到蓝牙基站名称
    private String attendanceDateStr;//签到日期 (yyyy-MM-dd)
    private String attendanceMonthStr;//签到月 (yyyy-MM)
    private int attendanceLeave;//是否请假 0-否1-是
    private long attendanceTimestamp;//实际签到时间
    private String attendanceTime;//实际签到时间
    private long attendanceTimestampExpect;//期望签到时间
    private String attendanceTimeExpect;//期望签到时间
    private long attendanceTimestampBegin;
    private long attendanceTimestampEnd;
    /* 考勤状态 0-未到签到时间 1-签到进行中 2-漏签到 3-正常已签到 */
    private int attendanceStatus;
    /* 签到类型 0-GPS 1-蓝牙 2-任意位置 */
    private int attendanceType;
    /* 签到倒计时(秒) */
    private String attendanceSecondsLeft;
    private long gmtCreate;//添加时间
    private long gmtModified;//修改时间
    private String attendancePictures;//图片id ","分隔
    private int distanceGps;//Gps签到范围（米）
    private int type;//类型  0-日常 1-加班 2-临时

    private int attendanceNoRecord;//是否漏打卡 0-否1-是
    private boolean canSignIn;//是否可签到



    private int attendanceIsLate;//
    private int attendanceLateMinutes;//
    private int attendanceIsEarly;
    private String attendanceEarlyMinutes;


    public int getAttendanceStatusType() {
        return attendanceStatusType;
    }

    public void setAttendanceStatusType(int attendanceStatusType) {
        this.attendanceStatusType = attendanceStatusType;
    }

    public int getEndType() {
        return endType;
    }

    public void setEndType(int endType) {
        this.endType = endType;
    }

    public String getAttendanceMonthStr() {
        return attendanceMonthStr;
    }

    public void setAttendanceMonthStr(String attendanceMonthStr) {
        this.attendanceMonthStr = attendanceMonthStr;
    }

    public String getAttendancePictures() {
        return attendancePictures;
    }

    public void setAttendancePictures(String attendancePictures) {
        this.attendancePictures = attendancePictures;
    }

    public int getDistanceGps() {
        return distanceGps;
    }

    public void setDistanceGps(int distanceGps) {
        this.distanceGps = distanceGps;
    }

    public SignDetailBean() {
    }

    public SignDetailBean(String time, String signtime, String address, int type, int latertime) {
        this.time = time;
        this.signtime = signtime;
        this.address = address;
        this.type = type;
        this.latertime = latertime;
    }

    public double getAttendanceLonExpect() {
        return attendanceLonExpect;
    }

    public void setAttendanceLonExpect(double attendanceLonExpect) {
        this.attendanceLonExpect = attendanceLonExpect;
    }

    public double getAttendanceLatExpect() {
        return attendanceLatExpect;
    }

    public void setAttendanceLatExpect(double attendanceLatExpect) {
        this.attendanceLatExpect = attendanceLatExpect;
    }

    public double getAttendanceLocationAround() {
        return attendanceLocationAround;
    }

    public void setAttendanceLocationAround(double attendanceLocationAround) {
        this.attendanceLocationAround = attendanceLocationAround;
    }

    public String getAttendanceSecondsLeft() {
        return attendanceSecondsLeft;
    }

    public void setAttendanceSecondsLeft(String attendanceSecondsLeft) {
        this.attendanceSecondsLeft = attendanceSecondsLeft;
    }

    public String getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSigntime() {
        return signtime;
    }

    public void setSigntime(String signtime) {
        this.signtime = signtime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLatertime() {
        return latertime;
    }

    public void setLatertime(int latertime) {
        this.latertime = latertime;
    }

    public String getAttendanceTimeExpect() {
        return attendanceTimeExpect;
    }

    public void setAttendanceTimeExpect(String attendanceTimeExpect) {
        this.attendanceTimeExpect = attendanceTimeExpect;
    }

    public int getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(int attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public int getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(int attendanceType) {
        this.attendanceType = attendanceType;
    }

    public int getAttendanceMinutesLeft() {
        return attendanceMinutesLeft;
    }

    public void setAttendanceMinutesLeft(int attendanceMinutesLeft) {
        this.attendanceMinutesLeft = attendanceMinutesLeft;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getAttendanceScheduleId() {
        return attendanceScheduleId;
    }

    public void setAttendanceScheduleId(String attendanceScheduleId) {
        this.attendanceScheduleId = attendanceScheduleId;
    }

    public String getAttendanceEmployeeId() {
        return attendanceEmployeeId;
    }

    public void setAttendanceEmployeeId(String attendanceEmployeeId) {
        this.attendanceEmployeeId = attendanceEmployeeId;
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

    public String getAttendanceIbeaconId() {
        return attendanceIbeaconId;
    }

    public void setAttendanceIbeaconId(String attendanceIbeaconId) {
        this.attendanceIbeaconId = attendanceIbeaconId;
    }

    public String getAttendanceIbeaconIdExpect() {
        return attendanceIbeaconIdExpect;
    }

    public void setAttendanceIbeaconIdExpect(String attendanceIbeaconIdExpect) {
        this.attendanceIbeaconIdExpect = attendanceIbeaconIdExpect;
    }

    public String getAttendanceDateStr() {
        return attendanceDateStr;
    }

    public void setAttendanceDateStr(String attendanceDateStr) {
        this.attendanceDateStr = attendanceDateStr;
    }

    public int getAttendanceNoRecord() {
        return attendanceNoRecord;
    }

    public void setAttendanceNoRecord(int attendanceNoRecord) {
        this.attendanceNoRecord = attendanceNoRecord;
    }

    public int getAttendanceLeave() {
        return attendanceLeave;
    }

    public void setAttendanceLeave(int attendanceLeave) {
        this.attendanceLeave = attendanceLeave;
    }

    public long getAttendanceTimestamp() {
        return attendanceTimestamp;
    }

    public void setAttendanceTimestamp(long attendanceTimestamp) {
        this.attendanceTimestamp = attendanceTimestamp;
    }

    public long getAttendanceTimestampExpect() {
        return attendanceTimestampExpect;
    }

    public void setAttendanceTimestampExpect(long attendanceTimestampExpect) {
        this.attendanceTimestampExpect = attendanceTimestampExpect;
    }

    public boolean isCanSignIn() {
        return canSignIn;
    }

    public void setCanSignIn(boolean canSignIn) {
        this.canSignIn = canSignIn;
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

    public String getAttendanceAddress() {
        return attendanceAddress;
    }

    public void setAttendanceAddress(String attendanceAddress) {
        this.attendanceAddress = attendanceAddress;
    }

    public String getAttendanceAddressExpect() {
        return attendanceAddressExpect;
    }

    public void setAttendanceAddressExpect(String attendanceAddressExpect) {
        this.attendanceAddressExpect = attendanceAddressExpect;
    }

    public String getAttendanceIbeaconName() {
        return attendanceIbeaconName;
    }

    public void setAttendanceIbeaconName(String attendanceIbeaconName) {
        this.attendanceIbeaconName = attendanceIbeaconName;
    }

    public String getAttendanceIbeaconNameExpect() {
        return attendanceIbeaconNameExpect;
    }

    public void setAttendanceIbeaconNameExpect(String attendanceIbeaconNameExpect) {
        this.attendanceIbeaconNameExpect = attendanceIbeaconNameExpect;
    }

    public int getAttendanceIsLate() {
        return attendanceIsLate;
    }

    public void setAttendanceIsLate(int attendanceIsLate) {
        this.attendanceIsLate = attendanceIsLate;
    }

    public int getAttendanceLateMinutes() {
        return attendanceLateMinutes;
    }

    public void setAttendanceLateMinutes(int attendanceLateMinutes) {
        this.attendanceLateMinutes = attendanceLateMinutes;
    }

    public int getAttendanceIsEarly() {
        return attendanceIsEarly;
    }

    public void setAttendanceIsEarly(int attendanceIsEarly) {
        this.attendanceIsEarly = attendanceIsEarly;
    }

    public String getAttendanceEarlyMinutes() {
        return attendanceEarlyMinutes;
    }

    public void setAttendanceEarlyMinutes(String attendanceEarlyMinutes) {
        this.attendanceEarlyMinutes = attendanceEarlyMinutes;
    }

    public long getAttendanceTimestampBegin() {
        return attendanceTimestampBegin;
    }

    public void setAttendanceTimestampBegin(long attendanceTimestampBegin) {
        this.attendanceTimestampBegin = attendanceTimestampBegin;
    }

    public long getAttendanceTimestampEnd() {
        return attendanceTimestampEnd;
    }

    public void setAttendanceTimestampEnd(long attendanceTimestampEnd) {
        this.attendanceTimestampEnd = attendanceTimestampEnd;
    }

    @Override
    public String toString() {
        return "SignDetailBean{" +
                "attendanceStatusType=" + attendanceStatusType +
                ", endType=" + endType +
                ", time='" + time + '\'' +
                ", signtime='" + signtime + '\'' +
                ", address='" + address + '\'' +
                ", latertime=" + latertime +
                ", attendanceMinutesLeft=" + attendanceMinutesLeft +
                ", attendanceId='" + attendanceId + '\'' +
                ", attendanceScheduleId='" + attendanceScheduleId + '\'' +
                ", attendanceEmployeeId='" + attendanceEmployeeId + '\'' +
                ", attendanceLon=" + attendanceLon +
                ", attendanceLat=" + attendanceLat +
                ", attendanceLonExpect=" + attendanceLonExpect +
                ", attendanceLatExpect=" + attendanceLatExpect +
                ", attendanceLocationAround=" + attendanceLocationAround +
                ", attendanceIbeaconId='" + attendanceIbeaconId + '\'' +
                ", attendanceIbeaconIdExpect='" + attendanceIbeaconIdExpect + '\'' +
                ", attendanceAddress='" + attendanceAddress + '\'' +
                ", attendanceAddressExpect='" + attendanceAddressExpect + '\'' +
                ", attendanceIbeaconName='" + attendanceIbeaconName + '\'' +
                ", attendanceIbeaconNameExpect='" + attendanceIbeaconNameExpect + '\'' +
                ", attendanceDateStr='" + attendanceDateStr + '\'' +
                ", attendanceMonthStr='" + attendanceMonthStr + '\'' +
                ", attendanceLeave=" + attendanceLeave +
                ", attendanceTimestamp=" + attendanceTimestamp +
                ", attendanceTime='" + attendanceTime + '\'' +
                ", attendanceTimestampExpect=" + attendanceTimestampExpect +
                ", attendanceTimeExpect='" + attendanceTimeExpect + '\'' +
                ", attendanceTimestampBegin=" + attendanceTimestampBegin +
                ", attendanceTimestampEnd=" + attendanceTimestampEnd +
                ", attendanceStatus=" + attendanceStatus +
                ", attendanceType=" + attendanceType +
                ", attendanceSecondsLeft='" + attendanceSecondsLeft + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", attendancePictures='" + attendancePictures + '\'' +
                ", distanceGps=" + distanceGps +
                ", type=" + type +
                ", attendanceNoRecord=" + attendanceNoRecord +
                ", canSignIn=" + canSignIn +
                ", attendanceIsLate=" + attendanceIsLate +
                ", attendanceLateMinutes=" + attendanceLateMinutes +
                ", attendanceIsEarly=" + attendanceIsEarly +
                ", attendanceEarlyMinutes='" + attendanceEarlyMinutes + '\'' +
                '}';
    }
}
