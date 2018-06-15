package com.hz.junxinbaoan.data;

import com.hz.junxinbaoan.utils.CommonUtils;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class SignTopBean {
    private String starttime;
    private String endtime;
    private String month;
    private String day;
    private String restarttime;
    private String reendtime;

    /*  [{"hourExpect":0,"hourActual":11,"date":"2017-11-02"}]  */
    private int hourExpect;
    private int hourActual;
    private String date;

    private boolean isChoose;

    public int[] getTime(){
        if (!CommonUtils.isEmpty(date)){
            String[] split = date.split("-");
            return new int[]{Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2])};
        }
        return null;
    }

    public SignTopBean() {
    }

    public SignTopBean(int hourExpect, int hourActual, String date) {
        this.hourExpect = hourExpect;
        this.hourActual = hourActual;
        this.date = date;
    }

    //    public SignTopBean(String restarttime, String reendtime, String starttime, String endtime, String month, String day) {
//        this.starttime = starttime;
//        this.endtime = endtime;
//        this.month = month;
//        this.day = day;
//        this.restarttime = restarttime;
//        this.reendtime = reendtime;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHourExpect() {
        return hourExpect;
    }

    public void setHourExpect(int hourExpect) {
        this.hourExpect = hourExpect;
    }

    public int getHourActual() {
        return hourActual;
    }

    public void setHourActual(int hourActual) {
        this.hourActual = hourActual;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getRestarttime() {
        return restarttime;
    }

    public void setRestarttime(String restarttime) {
        this.restarttime = restarttime;
    }

    public String getReendtime() {
        return reendtime;
    }

    public void setReendtime(String reendtime) {
        this.reendtime = reendtime;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    @Override
    public String toString() {
        return "SignTopBean{" +
                "starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", restarttime='" + restarttime + '\'' +
                ", reendtime='" + reendtime + '\'' +
                ", hourExpect=" + hourExpect +
                ", hourActual=" + hourActual +
                ", date='" + date + '\'' +
                ", isChoose=" + isChoose +
                '}';
    }
}
