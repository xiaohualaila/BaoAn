package com.hz.junxinbaoan.result;

/**
 * Created by Administrator on 2018/3/3.
 */

public class RolePermissionResult extends BaseResult {

    private RolePermissionBean data;

    public RolePermissionBean getData() {
        return data;
    }

    public void setData(RolePermissionBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RolePermissionResult{" +
                "data=" + data +
                '}';
    }

    public class RolePermissionBean {

        private String roleId;
        private String roleName;//角色名称
        private Integer isDel;//是否删除 0-否1-是
        private Integer isAppLogin;  //保安通app登录权限 0-无1-有
        private Integer isAppCertificate;//保安通app电子保安证 0-无1-有
        private Integer isAppAttendance;//保安通app考勤签到 0-无1-有
        private Integer isAppReport;//保安通app求助爆料 0-无1-有
        private Integer isAppSchedule;
        private Integer isAppRequest;
        private Integer isAppLearn;
        private Integer isAppMessage;
        private Integer isAppPhonelist;
        private Integer isAppPhonecall;
        private Integer isCompanyAppLogin;
        private Integer isCompanyAppReport;//求助爆料
        private Integer isCompanyAppRequest;
        private Integer isCompanyAppLearn;//公司端app学习中心 0-无1-有
        private Integer isCompanyAppMessage;
        private Integer isCompanyAppPhonelist;
        private Integer isCompanyAppCertificate;//公司端app电子保安证 0-无1-有
        private Integer isCompanyAppSchedule;//公司端app通讯录排班 0-无1-有
        private Integer isCompanyAppAttendance;
        private Integer isCompanyAppPhonecall;
        private Integer isCompanyAppLocation;
        private Integer isCompanyAppTrajectory;//公司端app通讯录轨迹 0-无1-有
        private Integer isCompanyAppQrcode;
        private Integer isPcBasicLogin;
        private Integer isPcBasicHome;
        private Integer isPcBasicMap;
        private Integer isPcCompanyCominfo;
        private Integer isPcCompanyComedit;
        private Integer isPcCompanyOrginfo;
        private Integer isPcCompanyOrgedit;
        private Integer isPcCompanyEmplist;
        private Integer isPcCompanyEmpinfo;
        private Integer isPcCompanyEmpedit;
        private Integer isPcCompanyEmpoperate;
        private Integer isPcCompanyRole;
        private Integer isPcAttendanceMacinfo;
        private Integer isPcAttendanceSchinfo;
        private Integer isPcAttendanceSchedit;
        private Integer isPcAttendanceViewinfo;
        private Integer isPcAttendanceViewedit;
        private Integer isPcAttendanceSign;
        private Integer isPcAttendanceTrajectory;

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public Integer getIsDel() {
            return isDel;
        }

        public void setIsDel(Integer isDel) {
            this.isDel = isDel;
        }

        public Integer getIsAppLogin() {
            return isAppLogin;
        }

        public void setIsAppLogin(Integer isAppLogin) {
            this.isAppLogin = isAppLogin;
        }

        public Integer getIsAppCertificate() {
            return isAppCertificate;
        }

        public void setIsAppCertificate(Integer isAppCertificate) {
            this.isAppCertificate = isAppCertificate;
        }

        public Integer getIsAppAttendance() {
            return isAppAttendance;
        }

        public void setIsAppAttendance(Integer isAppAttendance) {
            this.isAppAttendance = isAppAttendance;
        }

        public Integer getIsAppReport() {
            return isAppReport;
        }

        public void setIsAppReport(Integer isAppReport) {
            this.isAppReport = isAppReport;
        }

        public Integer getIsAppSchedule() {
            return isAppSchedule;
        }

        public void setIsAppSchedule(Integer isAppSchedule) {
            this.isAppSchedule = isAppSchedule;
        }

        public Integer getIsAppRequest() {
            return isAppRequest;
        }

        public void setIsAppRequest(Integer isAppRequest) {
            this.isAppRequest = isAppRequest;
        }

        public Integer getIsAppLearn() {
            return isAppLearn;
        }

        public void setIsAppLearn(Integer isAppLearn) {
            this.isAppLearn = isAppLearn;
        }

        public Integer getIsAppMessage() {
            return isAppMessage;
        }

        public void setIsAppMessage(Integer isAppMessage) {
            this.isAppMessage = isAppMessage;
        }

        public Integer getIsAppPhonelist() {
            return isAppPhonelist;
        }

        public void setIsAppPhonelist(Integer isAppPhonelist) {
            this.isAppPhonelist = isAppPhonelist;
        }

        public Integer getIsAppPhonecall() {
            return isAppPhonecall;
        }

        public void setIsAppPhonecall(Integer isAppPhonecall) {
            this.isAppPhonecall = isAppPhonecall;
        }

        public Integer getIsCompanyAppLogin() {
            return isCompanyAppLogin;
        }

        public void setIsCompanyAppLogin(Integer isCompanyAppLogin) {
            this.isCompanyAppLogin = isCompanyAppLogin;
        }

        public Integer getIsCompanyAppReport() {
            return isCompanyAppReport;
        }

        public void setIsCompanyAppReport(Integer isCompanyAppReport) {
            this.isCompanyAppReport = isCompanyAppReport;
        }

        public Integer getIsCompanyAppRequest() {
            return isCompanyAppRequest;
        }

        public void setIsCompanyAppRequest(Integer isCompanyAppRequest) {
            this.isCompanyAppRequest = isCompanyAppRequest;
        }

        public Integer getIsCompanyAppLearn() {
            return isCompanyAppLearn;
        }

        public void setIsCompanyAppLearn(Integer isCompanyAppLearn) {
            this.isCompanyAppLearn = isCompanyAppLearn;
        }

        public Integer getIsCompanyAppMessage() {
            return isCompanyAppMessage;
        }

        public void setIsCompanyAppMessage(Integer isCompanyAppMessage) {
            this.isCompanyAppMessage = isCompanyAppMessage;
        }

        public Integer getIsCompanyAppPhonelist() {
            return isCompanyAppPhonelist;
        }

        public void setIsCompanyAppPhonelist(Integer isCompanyAppPhonelist) {
            this.isCompanyAppPhonelist = isCompanyAppPhonelist;
        }

        public Integer getIsCompanyAppCertificate() {
            return isCompanyAppCertificate;
        }

        public void setIsCompanyAppCertificate(Integer isCompanyAppCertificate) {
            this.isCompanyAppCertificate = isCompanyAppCertificate;
        }

        public Integer getIsCompanyAppSchedule() {
            return isCompanyAppSchedule;
        }

        public void setIsCompanyAppSchedule(Integer isCompanyAppSchedule) {
            this.isCompanyAppSchedule = isCompanyAppSchedule;
        }

        public Integer getIsCompanyAppAttendance() {
            return isCompanyAppAttendance;
        }

        public void setIsCompanyAppAttendance(Integer isCompanyAppAttendance) {
            this.isCompanyAppAttendance = isCompanyAppAttendance;
        }

        public Integer getIsCompanyAppPhonecall() {
            return isCompanyAppPhonecall;
        }

        public void setIsCompanyAppPhonecall(Integer isCompanyAppPhonecall) {
            this.isCompanyAppPhonecall = isCompanyAppPhonecall;
        }

        public Integer getIsCompanyAppLocation() {
            return isCompanyAppLocation;
        }

        public void setIsCompanyAppLocation(Integer isCompanyAppLocation) {
            this.isCompanyAppLocation = isCompanyAppLocation;
        }

        public Integer getIsCompanyAppTrajectory() {
            return isCompanyAppTrajectory;
        }

        public void setIsCompanyAppTrajectory(Integer isCompanyAppTrajectory) {
            this.isCompanyAppTrajectory = isCompanyAppTrajectory;
        }

        public Integer getIsCompanyAppQrcode() {
            return isCompanyAppQrcode;
        }

        public void setIsCompanyAppQrcode(Integer isCompanyAppQrcode) {
            this.isCompanyAppQrcode = isCompanyAppQrcode;
        }

        public Integer getIsPcBasicLogin() {
            return isPcBasicLogin;
        }

        public void setIsPcBasicLogin(Integer isPcBasicLogin) {
            this.isPcBasicLogin = isPcBasicLogin;
        }

        public Integer getIsPcBasicHome() {
            return isPcBasicHome;
        }

        public void setIsPcBasicHome(Integer isPcBasicHome) {
            this.isPcBasicHome = isPcBasicHome;
        }

        public Integer getIsPcBasicMap() {
            return isPcBasicMap;
        }

        public void setIsPcBasicMap(Integer isPcBasicMap) {
            this.isPcBasicMap = isPcBasicMap;
        }

        public Integer getIsPcCompanyCominfo() {
            return isPcCompanyCominfo;
        }

        public void setIsPcCompanyCominfo(Integer isPcCompanyCominfo) {
            this.isPcCompanyCominfo = isPcCompanyCominfo;
        }

        public Integer getIsPcCompanyComedit() {
            return isPcCompanyComedit;
        }

        public void setIsPcCompanyComedit(Integer isPcCompanyComedit) {
            this.isPcCompanyComedit = isPcCompanyComedit;
        }

        public Integer getIsPcCompanyOrginfo() {
            return isPcCompanyOrginfo;
        }

        public void setIsPcCompanyOrginfo(Integer isPcCompanyOrginfo) {
            this.isPcCompanyOrginfo = isPcCompanyOrginfo;
        }

        public Integer getIsPcCompanyOrgedit() {
            return isPcCompanyOrgedit;
        }

        public void setIsPcCompanyOrgedit(Integer isPcCompanyOrgedit) {
            this.isPcCompanyOrgedit = isPcCompanyOrgedit;
        }

        public Integer getIsPcCompanyEmplist() {
            return isPcCompanyEmplist;
        }

        public void setIsPcCompanyEmplist(Integer isPcCompanyEmplist) {
            this.isPcCompanyEmplist = isPcCompanyEmplist;
        }

        public Integer getIsPcCompanyEmpinfo() {
            return isPcCompanyEmpinfo;
        }

        public void setIsPcCompanyEmpinfo(Integer isPcCompanyEmpinfo) {
            this.isPcCompanyEmpinfo = isPcCompanyEmpinfo;
        }

        public Integer getIsPcCompanyEmpedit() {
            return isPcCompanyEmpedit;
        }

        public void setIsPcCompanyEmpedit(Integer isPcCompanyEmpedit) {
            this.isPcCompanyEmpedit = isPcCompanyEmpedit;
        }

        public Integer getIsPcCompanyEmpoperate() {
            return isPcCompanyEmpoperate;
        }

        public void setIsPcCompanyEmpoperate(Integer isPcCompanyEmpoperate) {
            this.isPcCompanyEmpoperate = isPcCompanyEmpoperate;
        }

        public Integer getIsPcCompanyRole() {
            return isPcCompanyRole;
        }

        public void setIsPcCompanyRole(Integer isPcCompanyRole) {
            this.isPcCompanyRole = isPcCompanyRole;
        }

        public Integer getIsPcAttendanceMacinfo() {
            return isPcAttendanceMacinfo;
        }

        public void setIsPcAttendanceMacinfo(Integer isPcAttendanceMacinfo) {
            this.isPcAttendanceMacinfo = isPcAttendanceMacinfo;
        }

        public Integer getIsPcAttendanceSchinfo() {
            return isPcAttendanceSchinfo;
        }

        public void setIsPcAttendanceSchinfo(Integer isPcAttendanceSchinfo) {
            this.isPcAttendanceSchinfo = isPcAttendanceSchinfo;
        }

        public Integer getIsPcAttendanceSchedit() {
            return isPcAttendanceSchedit;
        }

        public void setIsPcAttendanceSchedit(Integer isPcAttendanceSchedit) {
            this.isPcAttendanceSchedit = isPcAttendanceSchedit;
        }

        public Integer getIsPcAttendanceViewinfo() {
            return isPcAttendanceViewinfo;
        }

        public void setIsPcAttendanceViewinfo(Integer isPcAttendanceViewinfo) {
            this.isPcAttendanceViewinfo = isPcAttendanceViewinfo;
        }

        public Integer getIsPcAttendanceViewedit() {
            return isPcAttendanceViewedit;
        }

        public void setIsPcAttendanceViewedit(Integer isPcAttendanceViewedit) {
            this.isPcAttendanceViewedit = isPcAttendanceViewedit;
        }

        public Integer getIsPcAttendanceSign() {
            return isPcAttendanceSign;
        }

        public void setIsPcAttendanceSign(Integer isPcAttendanceSign) {
            this.isPcAttendanceSign = isPcAttendanceSign;
        }

        public Integer getIsPcAttendanceTrajectory() {
            return isPcAttendanceTrajectory;
        }

        public void setIsPcAttendanceTrajectory(Integer isPcAttendanceTrajectory) {
            this.isPcAttendanceTrajectory = isPcAttendanceTrajectory;
        }

        @Override
        public String toString() {
            return "RolePermissionBean{" +
                    "roleId='" + roleId + '\'' +
                    ", roleName='" + roleName + '\'' +
                    ", isDel=" + isDel +
                    ", isAppLogin=" + isAppLogin +
                    ", isAppCertificate=" + isAppCertificate +
                    ", isAppAttendance=" + isAppAttendance +
                    ", isAppReport=" + isAppReport +
                    ", isAppSchedule=" + isAppSchedule +
                    ", isAppRequest=" + isAppRequest +
                    ", isAppLearn=" + isAppLearn +
                    ", isAppMessage=" + isAppMessage +
                    ", isAppPhonelist=" + isAppPhonelist +
                    ", isAppPhonecall=" + isAppPhonecall +
                    ", isCompanyAppLogin=" + isCompanyAppLogin +
                    ", isCompanyAppReport=" + isCompanyAppReport +
                    ", isCompanyAppRequest=" + isCompanyAppRequest +
                    ", isCompanyAppLearn=" + isCompanyAppLearn +
                    ", isCompanyAppMessage=" + isCompanyAppMessage +
                    ", isCompanyAppPhonelist=" + isCompanyAppPhonelist +
                    ", isCompanyAppCertificate=" + isCompanyAppCertificate +
                    ", isCompanyAppSchedule=" + isCompanyAppSchedule +
                    ", isCompanyAppAttendance=" + isCompanyAppAttendance +
                    ", isCompanyAppPhonecall=" + isCompanyAppPhonecall +
                    ", isCompanyAppLocation=" + isCompanyAppLocation +
                    ", isCompanyAppTrajectory=" + isCompanyAppTrajectory +
                    ", isCompanyAppQrcode=" + isCompanyAppQrcode +
                    ", isPcBasicLogin=" + isPcBasicLogin +
                    ", isPcBasicHome=" + isPcBasicHome +
                    ", isPcBasicMap=" + isPcBasicMap +
                    ", isPcCompanyCominfo=" + isPcCompanyCominfo +
                    ", isPcCompanyComedit=" + isPcCompanyComedit +
                    ", isPcCompanyOrginfo=" + isPcCompanyOrginfo +
                    ", isPcCompanyOrgedit=" + isPcCompanyOrgedit +
                    ", isPcCompanyEmplist=" + isPcCompanyEmplist +
                    ", isPcCompanyEmpinfo=" + isPcCompanyEmpinfo +
                    ", isPcCompanyEmpedit=" + isPcCompanyEmpedit +
                    ", isPcCompanyEmpoperate=" + isPcCompanyEmpoperate +
                    ", isPcCompanyRole=" + isPcCompanyRole +
                    ", isPcAttendanceMacinfo=" + isPcAttendanceMacinfo +
                    ", isPcAttendanceSchinfo=" + isPcAttendanceSchinfo +
                    ", isPcAttendanceSchedit=" + isPcAttendanceSchedit +
                    ", isPcAttendanceViewinfo=" + isPcAttendanceViewinfo +
                    ", isPcAttendanceViewedit=" + isPcAttendanceViewedit +
                    ", isPcAttendanceSign=" + isPcAttendanceSign +
                    ", isPcAttendanceTrajectory=" + isPcAttendanceTrajectory +
                    '}';
        }
    }
}
