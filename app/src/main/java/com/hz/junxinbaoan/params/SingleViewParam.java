package com.hz.junxinbaoan.params;

/**
 * Created by Administrator on 2017/11/8 0008.
 */

public class SingleViewParam extends BaseParam {
    private String employeeId;
    private String month;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "SingleViewParam{" +
                "employeeId='" + employeeId + '\'' +
                ", month='" + month + '\'' +
                '}';
    }
}
