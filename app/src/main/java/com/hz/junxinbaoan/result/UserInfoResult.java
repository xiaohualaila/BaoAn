package com.hz.junxinbaoan.result;

/**
 * Created by linzp on 2017/10/27.
 */

public class UserInfoResult extends BaseResult {

    @Override
    public String toString() {
        return "UserInfoResult{" +
                "data=" + data +
                '}';
    }

    /**
     * data : {"userId":"b7344b7c-4427-4acf-aabf-70a17853ccd3","userUsername":"admin","userPassword":null,
     * "userRole":"ROLE_SUPER","userIdCard":"430525199001084134","userName":"用户","userSex":"男","userBirthday":null,
     * "userNation":null,"userPoliticsStatus":null,"userPhone":"15888896486","userResidenceAddress":null,
     * "userLivingAddress":null,"userEducationLevel":null,"userFamilyMember":null,"userEducation":null,
     * "userJobExperience":null,"userProfessionLevel":null,"userQualificationId":null,"userCertificateNo":null,
     * "userJobStatus":0,"userCompanyId":"b7344b7c-4427-4acf-aabf-70a17853ccd3","gmtCreate":1508210398684,
     * "gmtModified":1508210398684,"userPhoto":null}
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
         * userId : b7344b7c-4427-4acf-aabf-70a17853ccd3
         * userUsername : admin
         * userPassword : null
         * userRole : ROLE_SUPER
         * userIdCard : 430525199001084134
         * userName : 用户
         * userSex : 男
         * userBirthday : null
         * userNation : null
         * userPoliticsStatus : null
         * userPhone : 15888896486
         * userResidenceAddress : null
         * userLivingAddress : null
         * userEducationLevel : null
         * userFamilyMember : null
         * userEducation : null
         * userJobExperience : null
         * userProfessionLevel : null
         * userQualificationId : null
         * userCertificateNo : null
         * userJobStatus : 0
         * userCompanyId : b7344b7c-4427-4acf-aabf-70a17853ccd3
         * gmtCreate : 1508210398684
         * gmtModified : 1508210398684
         * userPhoto : null
         */

        private String userId;
        private String userUsername;
        private String userPassword;
        private String userRole;
        private String userIdCard;
        private String userName;
        private String userSex;
        private String userBirthday;
        private String userNation;
        private String userPoliticsStatus;
        private String userPhone;
        private String userResidenceAddress;
        private String userLivingAddress;
        private String userEducationLevel;
        private String userFamilyMember;
        private String userEducation;
        private String userJobExperience;
        private String userProfessionLevel;
        private String userQualificationId;
        private String userCertificateNo;
        private int userJobStatus;
        private String userCompanyId;
        private long gmtCreate;
        private long gmtModified;
        private String userPhoto;
        private String userCompanyName;

        public String getUserCompanyName() {
            return userCompanyName;
        }

        public void setUserCompanyName(String userCompanyName) {
            this.userCompanyName = userCompanyName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserUsername() {
            return userUsername;
        }

        public void setUserUsername(String userUsername) {
            this.userUsername = userUsername;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String userRole) {
            this.userRole = userRole;
        }

        public String getUserIdCard() {
            return userIdCard;
        }

        public void setUserIdCard(String userIdCard) {
            this.userIdCard = userIdCard;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        public String getUserBirthday() {
            return userBirthday;
        }

        public void setUserBirthday(String userBirthday) {
            this.userBirthday = userBirthday;
        }

        public String getUserNation() {
            return userNation;
        }

        public void setUserNation(String userNation) {
            this.userNation = userNation;
        }

        public String getUserPoliticsStatus() {
            return userPoliticsStatus;
        }

        public void setUserPoliticsStatus(String userPoliticsStatus) {
            this.userPoliticsStatus = userPoliticsStatus;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserResidenceAddress() {
            return userResidenceAddress;
        }

        public void setUserResidenceAddress(String userResidenceAddress) {
            this.userResidenceAddress = userResidenceAddress;
        }

        public String getUserLivingAddress() {
            return userLivingAddress;
        }

        public void setUserLivingAddress(String userLivingAddress) {
            this.userLivingAddress = userLivingAddress;
        }

        public String getUserEducationLevel() {
            return userEducationLevel;
        }

        public void setUserEducationLevel(String userEducationLevel) {
            this.userEducationLevel = userEducationLevel;
        }

        public String getUserFamilyMember() {
            return userFamilyMember;
        }

        public void setUserFamilyMember(String userFamilyMember) {
            this.userFamilyMember = userFamilyMember;
        }

        public String getUserEducation() {
            return userEducation;
        }

        public void setUserEducation(String userEducation) {
            this.userEducation = userEducation;
        }

        public String getUserJobExperience() {
            return userJobExperience;
        }

        public void setUserJobExperience(String userJobExperience) {
            this.userJobExperience = userJobExperience;
        }

        public String getUserProfessionLevel() {
            return userProfessionLevel;
        }

        public void setUserProfessionLevel(String userProfessionLevel) {
            this.userProfessionLevel = userProfessionLevel;
        }

        public String getUserQualificationId() {
            return userQualificationId;
        }

        public void setUserQualificationId(String userQualificationId) {
            this.userQualificationId = userQualificationId;
        }

        public String getUserCertificateNo() {
            return userCertificateNo;
        }

        public void setUserCertificateNo(String userCertificateNo) {
            this.userCertificateNo = userCertificateNo;
        }

        public int getUserJobStatus() {
            return userJobStatus;
        }

        public void setUserJobStatus(int userJobStatus) {
            this.userJobStatus = userJobStatus;
        }

        public String getUserCompanyId() {
            return userCompanyId;
        }

        public void setUserCompanyId(String userCompanyId) {
            this.userCompanyId = userCompanyId;
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

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "userId='" + userId + '\'' +
                    ", userUsername='" + userUsername + '\'' +
                    ", userPassword='" + userPassword + '\'' +
                    ", userRole='" + userRole + '\'' +
                    ", userIdCard='" + userIdCard + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userSex='" + userSex + '\'' +
                    ", userBirthday='" + userBirthday + '\'' +
                    ", userNation='" + userNation + '\'' +
                    ", userPoliticsStatus='" + userPoliticsStatus + '\'' +
                    ", userPhone='" + userPhone + '\'' +
                    ", userResidenceAddress='" + userResidenceAddress + '\'' +
                    ", userLivingAddress='" + userLivingAddress + '\'' +
                    ", userEducationLevel='" + userEducationLevel + '\'' +
                    ", userFamilyMember='" + userFamilyMember + '\'' +
                    ", userEducation='" + userEducation + '\'' +
                    ", userJobExperience='" + userJobExperience + '\'' +
                    ", userProfessionLevel='" + userProfessionLevel + '\'' +
                    ", userQualificationId='" + userQualificationId + '\'' +
                    ", userCertificateNo='" + userCertificateNo + '\'' +
                    ", userJobStatus=" + userJobStatus +
                    ", userCompanyId='" + userCompanyId + '\'' +
                    ", gmtCreate=" + gmtCreate +
                    ", gmtModified=" + gmtModified +
                    ", userPhoto='" + userPhoto + '\'' +
                    ", userCompanyName='" + userCompanyName + '\'' +
                    '}';
        }
    }
}
