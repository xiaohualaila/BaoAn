package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/11/3.
 */

public class ForgetPasswordParams  {
//    phone	手机号
//    code	验证码
//    passwordNew	新密码
//    passwordRep	确认密码
    private String phone,code,passwordNew,passwordRep;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getPasswordRep() {
        return passwordRep;
    }

    public void setPasswordRep(String passwordRep) {
        this.passwordRep = passwordRep;
    }
}
