package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/10/30.
 */

public class RegisterParams {
    //    userName	姓名	是	code	0000-成功，9999-失败
//    userIdCard	身份证号	是	data	数据/错误信息
//    userPhone	手机号	是
//    code	验证码	是
//    userPassword	密码	是
    private String userName,userIdCard,userPhone,code,userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdCard() {
        return userIdCard;
    }

    public void setUserIdCard(String userIdCard) {
        this.userIdCard = userIdCard;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
