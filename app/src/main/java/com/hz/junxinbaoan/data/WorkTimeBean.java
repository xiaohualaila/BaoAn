package com.hz.junxinbaoan.data;

/**
 * Created by linzp on 2017/10/23.
 */

public class WorkTimeBean {
    double time_work;
    double time_rule;
    int day;

    public WorkTimeBean(double time_work, double time_rule, int day) {
        this.time_work = time_work;
        this.time_rule = time_rule;
        this.day = day;
    }

    public double getTime_work() {
        return time_work;
    }

    public void setTime_work(double time_work) {
        this.time_work = time_work;
    }

    public double getTime_rule() {
        return time_rule;
    }

    public void setTime_rule(double time_rule) {
        this.time_rule = time_rule;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
