package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/11/1.
 */

public class CommitReasonParams extends BaseParam {

//    requestType	申请类型
//    requestReason	申请内容
//    requestBeginTime	申请开始时间
//    requestEndTime	申请结束时间
    private String requestType,requestReason,requestBeginTime,requestEndTime;


    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public String getRequestBeginTime() {
        return requestBeginTime;
    }

    public void setRequestBeginTime(String requestBeginTime) {
        this.requestBeginTime = requestBeginTime;
    }

    public String getRequestEndTime() {
        return requestEndTime;
    }

    public void setRequestEndTime(String requestEndTime) {
        this.requestEndTime = requestEndTime;
    }
}
