package com.hz.junxinbaoan.result;

import java.util.List;

/**
 * Created by linzp on 2017/11/17.
 */

public class CodeResult extends BaseResult{
// "appInfoId":"2",
//         "appInfoType":"app",
//         "appInfoClient":"ios",
//         "appInfoVersion":"1.0",
//         "appInfoUrl":"http://www.baidu.com",
//         "gmtCreate":11,
//         "gmtModified":11
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean {
        String appInfoId;
        String appInfoType;
        String appInfoClient;
        String appInfoVersion;
        String appInfoUrl;
        int gmtCreate;
        int gmtModified;



        public String getAppInfoId() {
            return appInfoId;
        }

        public void setAppInfoId(String appInfoId) {
            this.appInfoId = appInfoId;
        }

        public String getAppInfoType() {
            return appInfoType;
        }

        public void setAppInfoType(String appInfoType) {
            this.appInfoType = appInfoType;
        }

        public String getAppInfoClient() {
            return appInfoClient;
        }

        public void setAppInfoClient(String appInfoClient) {
            this.appInfoClient = appInfoClient;
        }

        public String getAppInfoVersion() {
            return appInfoVersion;
        }

        public void setAppInfoVersion(String appInfoVersion) {
            this.appInfoVersion = appInfoVersion;
        }

        public String getAppInfoUrl() {
            return appInfoUrl;
        }

        public void setAppInfoUrl(String appInfoUrl) {
            this.appInfoUrl = appInfoUrl;
        }

        public int getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(int gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public int getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(int gmtModified) {
            this.gmtModified = gmtModified;
        }
    }
}
