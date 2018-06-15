package com.hz.junxinbaoan.data;

/**
 * Created by linzp on 2017/10/24.
 */

public class ApprovalListBean {
//    data.requestId	id
//    data.requestType	申请类型
//    data.requestReason	申请原因
//    data.requestApplyTimestamp	申请时间戳
//    data.requestApplyTimeStr	申请时间
//    data.requestBeginTimestamp	申请起始时间
//    data.requestBeginTimeStr	申请起始时间
//    data.requestEndTimestamp	申请结束时间
//    data.requestEndTimeStr	申请结束时间
//    data.requestApproval	是否审批通过
//    data.requestToAdminId	指定审批人
//    data.requestRejectionReason	拒绝原因
//    data.requestEmployeeId	申请人id
//    data.requestEmployeeName	申请人姓名
//    data.requestStatus	审批状态 0-未处理1-通过2-未通过
//    data.gmtCreate	添加时间
//    data.gmtModified	修改时间
    private long id,requestApplyTimestamp,requestBeginTimestamp,requestEndTimestamp,
        howlong;//请假天数，接口吗，没有
    private String requestType,requestReason,requestApplyTimeStr,requestApproval,requestToAdminId,requestRejectionReason,
            requestEmployeeId,requestEmployeeName,requestStatus,requestBeginTimeStr,requestEndTimeStr;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRequestApplyTimestamp() {
        return requestApplyTimestamp;
    }

    public void setRequestApplyTimestamp(long requestApplyTimestamp) {
        this.requestApplyTimestamp = requestApplyTimestamp;
    }

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

    public String getRequestApplyTimeStr() {
        return requestApplyTimeStr;
    }

    public void setRequestApplyTimeStr(String requestApplyTimeStr) {
        this.requestApplyTimeStr = requestApplyTimeStr;
    }



    public String getRequestApproval() {
        return requestApproval;
    }

    public void setRequestApproval(String requestApproval) {
        this.requestApproval = requestApproval;
    }

    public String getRequestToAdminId() {
        return requestToAdminId;
    }

    public void setRequestToAdminId(String requestToAdminId) {
        this.requestToAdminId = requestToAdminId;
    }

    public String getRequestRejectionReason() {
        return requestRejectionReason;
    }

    public void setRequestRejectionReason(String requestRejectionReason) {
        this.requestRejectionReason = requestRejectionReason;
    }

    public String getRequestEmployeeId() {
        return requestEmployeeId;
    }

    public void setRequestEmployeeId(String requestEmployeeId) {
        this.requestEmployeeId = requestEmployeeId;
    }

    public String getRequestEmployeeName() {
        return requestEmployeeName;
    }

    public void setRequestEmployeeName(String requestEmployeeName) {
        this.requestEmployeeName = requestEmployeeName;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestBeginTimeStr() {
        return requestBeginTimeStr;
    }

    public void setRequestBeginTimeStr(String requestBeginTimeStr) {
        this.requestBeginTimeStr = requestBeginTimeStr;
    }

    public String getRequestEndTimeStr() {
        return requestEndTimeStr;
    }

    public void setRequestEndTimeStr(String requestEndTimeStr) {
        this.requestEndTimeStr = requestEndTimeStr;
    }

    public long getRequestBeginTimestamp() {
        return requestBeginTimestamp;
    }

    public void setRequestBeginTimestamp(long requestBeginTimestamp) {
        this.requestBeginTimestamp = requestBeginTimestamp;
    }

    public long getRequestEndTimestamp() {
        return requestEndTimestamp;
    }

    public void setRequestEndTimestamp(long requestEndTimestamp) {
        this.requestEndTimestamp = requestEndTimestamp;
    }

    public long getHowlong() {
        return howlong;
    }

    public void setHowlong(long howlong) {
        this.howlong = howlong;
    }
}
