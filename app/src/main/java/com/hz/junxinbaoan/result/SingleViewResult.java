package com.hz.junxinbaoan.result;

import java.util.List;

/**
 * Created by Administrator on 2017/11/8 0008.
 */

public class SingleViewResult extends BaseResult {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SingleViewResult{" +
                "data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         "employeeId": "9d5a4d3e-08ed-4060-9269-39ac45fa3f73",
         "name": "张日晖",
         "orgName": null,
         "absenceCount": 0,
         * empSchList : [{"empSchId":"dff6dcf9-6557-41d2-b028-923c4c3890e4",
         * "empSchEmployeeId":"d653df7d-4e45-4b86-9b64-d03a2f3c9a70",
         * "empSchScheduleId":"c66925f3-8d67-4c37-90e8-cb34226e207d","empSchScheduleShortName":"四",
         * "empSchScheduleColor":null,"empSchMonth":"2017-11","empSchDate":"2017-11-01","gmtCreate":1510048069713,
         * "gmtModified":1510048069713},.....,{"empSchId":"cbc2320c-03eb-4045-8601-38faea1d085d",
         * "empSchEmployeeId":"d653df7d-4e45-4b86-9b64-d03a2f3c9a70",
         * "empSchScheduleId":"c66925f3-8d67-4c37-90e8-cb34226e207d","empSchScheduleShortName":"四",
         * "empSchScheduleColor":null,"empSchMonth":"2017-11","empSchDate":"2017-11-29","gmtCreate":1510048069713,
         * "gmtModified":1510048069713}]
         */
        private String employeeId;//员工id
        private String name;//员工姓名

        private List<EmpSchListBean> empSchList;//排班列表
        private List<EmpSchListBean> tempEmpSchList;//临时班列表
        private List<EmpSchListBean> overtimeEmpSchList;//加班列表

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<EmpSchListBean> getEmpSchList() {
            return empSchList;
        }

        public void setEmpSchList(List<EmpSchListBean> empSchList) {
            this.empSchList = empSchList;
        }

        public List<EmpSchListBean> getTempEmpSchList() {
            return tempEmpSchList;
        }

        public void setTempEmpSchList(List<EmpSchListBean> tempEmpSchList) {
            this.tempEmpSchList = tempEmpSchList;
        }

        public List<EmpSchListBean> getOvertimeEmpSchList() {
            return overtimeEmpSchList;
        }

        public void setOvertimeEmpSchList(List<EmpSchListBean> overtimeEmpSchList) {
            this.overtimeEmpSchList = overtimeEmpSchList;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "employeeId='" + employeeId + '\'' +
                    ", name='" + name + '\'' +
                    ", empSchList=" + empSchList +
                    ", tempEmpSchList=" + tempEmpSchList +
                    ", overtimeEmpSchList=" + overtimeEmpSchList +
                    '}';
        }

        public static class EmpSchListBean {
            /**
             * empSchId : dff6dcf9-6557-41d2-b028-923c4c3890e4
             * empSchEmployeeId : d653df7d-4e45-4b86-9b64-d03a2f3c9a70
             * empSchScheduleId : c66925f3-8d67-4c37-90e8-cb34226e207d
             * empSchScheduleShortName : 四
             * empSchScheduleColor : null
             * empSchMonth : 2017-11
             * empSchDate : 2017-11-01
             * gmtCreate : 1510048069713
             * gmtModified : 1510048069713
             */

            private String empSchId;
            private String empSchEmployeeId;
            private String empSchScheduleId;
            private String empSchScheduleShortName;
            private String empSchScheduleColor;
            private String empSchMonth;
            private String empSchDate;
            private long gmtCreate;
            private long gmtModified;
            private String type;//类型  0-日常 1-加班 2-临时

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getEmpSchId() {
                return empSchId;
            }

            public void setEmpSchId(String empSchId) {
                this.empSchId = empSchId;
            }

            public String getEmpSchEmployeeId() {
                return empSchEmployeeId;
            }

            public void setEmpSchEmployeeId(String empSchEmployeeId) {
                this.empSchEmployeeId = empSchEmployeeId;
            }

            public String getEmpSchScheduleId() {
                return empSchScheduleId;
            }

            public void setEmpSchScheduleId(String empSchScheduleId) {
                this.empSchScheduleId = empSchScheduleId;
            }

            public String getEmpSchScheduleShortName() {
                return empSchScheduleShortName;
            }

            public void setEmpSchScheduleShortName(String empSchScheduleShortName) {
                this.empSchScheduleShortName = empSchScheduleShortName;
            }

            public String getEmpSchScheduleColor() {
                return empSchScheduleColor;
            }

            public void setEmpSchScheduleColor(String empSchScheduleColor) {
                this.empSchScheduleColor = empSchScheduleColor;
            }

            public String getEmpSchMonth() {
                return empSchMonth;
            }

            public void setEmpSchMonth(String empSchMonth) {
                this.empSchMonth = empSchMonth;
            }

            public String getEmpSchDate() {
                return empSchDate;
            }

            public void setEmpSchDate(String empSchDate) {
                this.empSchDate = empSchDate;
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

            @Override
            public String toString() {
                return "EmpSchListBean{" +
                        "empSchId='" + empSchId + '\'' +
                        ", empSchEmployeeId='" + empSchEmployeeId + '\'' +
                        ", empSchScheduleId='" + empSchScheduleId + '\'' +
                        ", empSchScheduleShortName='" + empSchScheduleShortName + '\'' +
                        ", empSchScheduleColor='" + empSchScheduleColor + '\'' +
                        ", empSchMonth='" + empSchMonth + '\'' +
                        ", empSchDate='" + empSchDate + '\'' +
                        ", gmtCreate=" + gmtCreate +
                        ", gmtModified=" + gmtModified +
                        ", type='" + type + '\'' +
                        '}';
            }
        }

    }

}
