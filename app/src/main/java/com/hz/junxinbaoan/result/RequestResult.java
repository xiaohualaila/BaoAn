package com.hz.junxinbaoan.result;

import java.util.List;

/**
 * Created by linzp on 2017/10/31.
 */

public class RequestResult extends BaseResult {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * requestId : 487d7be3-8481-4a58-bd29-f67c48ce5ee7
         * requestType : 病假
         * requestReason : 测一下
         * requestApplyTimestamp : 1509501706158
         * requestApplyTimeStr : 2017-11-01 10:01:46
         * requestBeginTimestamp : 1509552000000
         * requestBeginTimeStr : 2017-11-2
         * requestEndTimestamp : 1509724800000
         * requestEndTimeStr : 2017-11-4
         * requestApproval : 0
         * requestRejectionReason : null
         * requestEmployeeId : 1
         * requestEmployeeName : 张先生
         * requestStatus : 0
         * gmtCreate : 1509501706158
         * gmtModified : 1509501706158
         */

        private String requestId;
        private String requestType;
        private String requestReason;
        private long requestApplyTimestamp;
        private String requestApplyTimeStr;
        private long requestBeginTimestamp;
        private String requestBeginTimeStr;
        private long requestEndTimestamp;
        private String requestEndTimeStr;
        private int requestApproval;
        private Object requestRejectionReason;
        private String requestEmployeeId;
        private String requestEmployeeName;
        private int requestStatus;
        private long gmtCreate;
        private long gmtModified;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
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

        public long getRequestApplyTimestamp() {
            return requestApplyTimestamp;
        }

        public void setRequestApplyTimestamp(long requestApplyTimestamp) {
            this.requestApplyTimestamp = requestApplyTimestamp;
        }

        public String getRequestApplyTimeStr() {
            return requestApplyTimeStr;
        }

        public void setRequestApplyTimeStr(String requestApplyTimeStr) {
            this.requestApplyTimeStr = requestApplyTimeStr;
        }

        public long getRequestBeginTimestamp() {
            return requestBeginTimestamp;
        }

        public void setRequestBeginTimestamp(long requestBeginTimestamp) {
            this.requestBeginTimestamp = requestBeginTimestamp;
        }

        public String getRequestBeginTimeStr() {
            return requestBeginTimeStr;
        }

        public void setRequestBeginTimeStr(String requestBeginTimeStr) {
            this.requestBeginTimeStr = requestBeginTimeStr;
        }

        public long getRequestEndTimestamp() {
            return requestEndTimestamp;
        }

        public void setRequestEndTimestamp(long requestEndTimestamp) {
            this.requestEndTimestamp = requestEndTimestamp;
        }

        public String getRequestEndTimeStr() {
            return requestEndTimeStr;
        }

        public void setRequestEndTimeStr(String requestEndTimeStr) {
            this.requestEndTimeStr = requestEndTimeStr;
        }

        public int getRequestApproval() {
            return requestApproval;
        }

        public void setRequestApproval(int requestApproval) {
            this.requestApproval = requestApproval;
        }

        public Object getRequestRejectionReason() {
            return requestRejectionReason;
        }

        public void setRequestRejectionReason(Object requestRejectionReason) {
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

        public int getRequestStatus() {
            return requestStatus;
        }

        public void setRequestStatus(int requestStatus) {
            this.requestStatus = requestStatus;
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

        public String getHowlong() {
            return "100";
        }
    }
}
