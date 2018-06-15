package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/10/30.
 */

public class ChangeUserInfoParams extends BaseParam{
//    userName	姓名	是
//    userPhone	手机号	是
//    userResidenceAddress	户籍地址
//    userLivingAddress	居住地址
//    userPhoto	照片
//    codeNow	验证码	是
    private String userName,userPhone,userResidenceAddress,userLivingAddress,userPhoto,code;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getCodeNow() {
        return code;
    }

    public void setCodeNow(String codeNow) {
        this.code = codeNow;
    }
}
