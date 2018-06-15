package com.hz.junxinbaoan.result;

import java.util.List;

/**
 * Created by linzp on 2017/10/30.
 */

public class PeopleResult extends BaseResult {
    private List<PeopleBean> data;

    public List<PeopleBean> getData() {
        return data;
    }

    public void setData(List<PeopleBean> data) {
        this.data = data;
    }

    //    "employeeId":"1",
//            "employeeIdCard":"430525199001084134",
//            "employeeName":"张先生",
//            "employeeNameFirstCharInPinYin":null,
//            "employeeOrgId":"01",
//            "employeeCertificateNo":"111",
//            "employeeNation":"汉族",
//            "employeePoliticsStatus":"共青团员",
//            "employeePhone":"15888896486",
//            "employeeResidenceAddress":null,
//            "employeeLivingAddress":"hz585588",
//            "employeeQuit":0,
//            "employeeScheduleId":null,
//            "employeeJoinTime":"2017-10-16 11:40:14",
//            "employeeQuitTime":null,
//            "gmtCreate":1234,
//            "gmtModified":1509349507789,
//            "employeePhoto":null

    public class PeopleBean{
        String employeeId;
        String employeeIdCard,employeeName,employeeOrgId,employeeCertificateNo,
                employeeNation,employeePoliticsStatus,employeePhone,employeeResidenceAddress,employeeNameFirstCharInPinYin,
                employeeLivingAddress,employeeQuit,employeeScheduleId,employeeJoinTime,employeeQuitTime,employeePhoto;
        long gmtCreate,gmtModified;

        public int getEmployeeIsAdmin() {
            return employeeIsAdmin;
        }

        public void setEmployeeIsAdmin(int employeeIsAdmin) {
            this.employeeIsAdmin = employeeIsAdmin;
        }

        int employeeIsAdmin;

        public String getEmployeeNameFirstCharInPinYin() {
            return employeeNameFirstCharInPinYin;
        }

        public void setEmployeeNameFirstCharInPinYin(String employeeNameFirstCharInPinYin) {
            this.employeeNameFirstCharInPinYin = employeeNameFirstCharInPinYin;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getEmployeeIdCard() {
            return employeeIdCard;
        }

        public void setEmployeeIdCard(String employeeIdCard) {
            this.employeeIdCard = employeeIdCard;
        }

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public String getEmployeeOrgId() {
            return employeeOrgId;
        }

        public void setEmployeeOrgId(String employeeOrgId) {
            this.employeeOrgId = employeeOrgId;
        }

        public String getEmployeeCertificateNo() {
            return employeeCertificateNo;
        }

        public void setEmployeeCertificateNo(String employeeCertificateNo) {
            this.employeeCertificateNo = employeeCertificateNo;
        }

        public String getEmployeeNation() {
            return employeeNation;
        }

        public void setEmployeeNation(String employeeNation) {
            this.employeeNation = employeeNation;
        }

        public String getEmployeePoliticsStatus() {
            return employeePoliticsStatus;
        }

        public void setEmployeePoliticsStatus(String employeePoliticsStatus) {
            this.employeePoliticsStatus = employeePoliticsStatus;
        }

        public String getEmployeePhone() {
            return employeePhone;
        }

        public void setEmployeePhone(String employeePhone) {
            this.employeePhone = employeePhone;
        }

        public String getEmployeeResidenceAddress() {
            return employeeResidenceAddress;
        }

        public void setEmployeeResidenceAddress(String employeeResidenceAddress) {
            this.employeeResidenceAddress = employeeResidenceAddress;
        }

        public String getEmployeeLivingAddress() {
            return employeeLivingAddress;
        }

        public void setEmployeeLivingAddress(String employeeLivingAddress) {
            this.employeeLivingAddress = employeeLivingAddress;
        }

        public String getEmployeeQuit() {
            return employeeQuit;
        }

        public void setEmployeeQuit(String employeeQuit) {
            this.employeeQuit = employeeQuit;
        }

        public String getEmployeeScheduleId() {
            return employeeScheduleId;
        }

        public void setEmployeeScheduleId(String employeeScheduleId) {
            this.employeeScheduleId = employeeScheduleId;
        }

        public String getEmployeeJoinTime() {
            return employeeJoinTime;
        }

        public void setEmployeeJoinTime(String employeeJoinTime) {
            this.employeeJoinTime = employeeJoinTime;
        }

        public String getEmployeeQuitTime() {
            return employeeQuitTime;
        }

        public void setEmployeeQuitTime(String employeeQuitTime) {
            this.employeeQuitTime = employeeQuitTime;
        }

        public String getEmployeePhoto() {
            return employeePhoto;
        }

        public void setEmployeePhoto(String employeePhoto) {
            this.employeePhoto = employeePhoto;
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
    }


}
