package com.hz.junxinbaoan.result;

import com.hz.junxinbaoan.result.BaseResult;

/**
 * Created by linzp on 2017/11/3.
 */

public class MainPageResult extends BaseResult {

    /**
     * data : {"request":1,"learn":5,"report":11}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * request : 1
         * learn : 5
         * report : 11
         */

        private int request;
        private int learn;
        private int report;
        private int schedule;
        private String scheduleName;

        public String getScheduleName() {
            return scheduleName;
        }

        public void setScheduleName(String scheduleName) {
            this.scheduleName = scheduleName;
        }

        public int getSchedule() {
            return schedule;
        }

        public void setSchedule(int schedule) {
            this.schedule = schedule;
        }

        public int getRequest() {
            return request;
        }

        public void setRequest(int request) {
            this.request = request;
        }

        public int getLearn() {
            return learn;
        }

        public void setLearn(int learn) {
            this.learn = learn;
        }

        public int getReport() {
            return report;
        }

        public void setReport(int report) {
            this.report = report;
        }
    }
}
