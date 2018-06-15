package com.hz.junxinbaoan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hz.junxinbaoan.params.LoginParam;
import com.hz.junxinbaoan.result.BaseResult;
import com.hz.junxinbaoan.result.UserInfoResult;


/**
 * Created by jinl on 2016/10/8.
 */
public class UserInfo {

    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mUserInfo;

    private String UserOrgName;
    private String UserIdCard;
    private String UserLivingAddress;
    private String userResidenceAddress;


    public String getUserResidenceAddress() {
        return userResidenceAddress;
    }

    public void setUserResidenceAddress(String userResidenceAddress) {
        this.userResidenceAddress = userResidenceAddress;
    }

    public String getUserOrgName() {
        return UserOrgName;
    }

    public void setUserOrgName(String userOrgName) {
        UserOrgName = userOrgName;
    }

    public String getUserIdCard() {
        return UserIdCard;
    }

    public void setUserIdCard(String userIdCard) {
        UserIdCard = userIdCard;
    }

    public String getUserLivingAddress() {
        return UserLivingAddress;
    }

    public void setUserLivingAddress(String userLivingAddress) {
        UserLivingAddress = userLivingAddress;
    }

    public UserInfo(Context mContext) {
        this.mContext = mContext;
//        mUserInfo = mContext.getSharedPreferences(Constants.USER_PREFS_NAME, Context.MODE_PRIVATE);//mContex下的SharedPreferences能被程序下应用调用
//        userid = mUserInfo.getInt(Constants.PREF_KEY_USERID, 0);// String 用户id

    }




//    public void saveUserInfo(UseInfoBean bean) {
//        mEditor = mUserInfo.edit();
////        mEditor.putInt(Constants.PREF_KEY_USERID, bean.getDriverinfo().getDriver_id());
//        mEditor.commit();
//    }



    public void resetUserInfo(){
    }


    /**
     * 清空用户信息
     */
    public void delUserInfo() {
        UserId=null;
        UserCompanyId=null;
        UserCompanyName=null;
        UserUsername=null;
        UserRole=null;
        UserName=null;
        UserPhone=null;
        UserPhoto=null;
        UserId=null;
        gmtCreate=0;
        gmtModified=0;

        UserLivingAddress=null;
//    UserOrgName=loginresult.getData().getUserO();
        UserIdCard=null;
        userResidenceAddress=null;

        access_token=null;
//        mEditor.commit();

        if (my_save==null)
            my_save = getmContext().getSharedPreferences("my_save", Context.MODE_PRIVATE);
        editor= my_save.edit();
//        editor.putString("username",null);//存
//        editor.putString("password",null);//存
        editor.clear().commit();//提交

    }

    private String access_token,token_type,expires_in;
    public void saveUserInfo(BaseResult loginresult) {
        access_token=loginresult.getAccess_token();
        token_type=loginresult.getToken_type();
        expires_in=loginresult.getExpires_in();
    }

    public String getAccess_token() {

        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }


    private String UserId;
    private String UserCompanyId;
    private String UserCompanyName;
    private String UserUsername;
    private String UserPassword;
    private String UserRole;
    private String UserName;
    private String UserPhone;
    private String UserPhoto;
    private long gmtCreate;
    private long gmtModified;


    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public SharedPreferences.Editor getmEditor() {
        return mEditor;
    }

    public void setmEditor(SharedPreferences.Editor mEditor) {
        this.mEditor = mEditor;
    }

    public SharedPreferences getmUserInfo() {
        return mUserInfo;
    }

    public void setmUserInfo(SharedPreferences mUserInfo) {
        this.mUserInfo = mUserInfo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserCompanyId() {
        return UserCompanyId;
    }

    public void setUserCompanyId(String UserCompanyId) {
        this.UserCompanyId = UserCompanyId;
    }

    public String getUserCompanyName() {
        return UserCompanyName;
    }

    public void setUserCompanyName(String UserCompanyName) {
        this.UserCompanyName = UserCompanyName;
    }

    public String getUserUsername() {
        return UserUsername;
    }

    public void setUserUsername(String UserUsername) {
        this.UserUsername = UserUsername;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String UserPassword) {
        this.UserPassword = UserPassword;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String UserRole) {
        this.UserRole = UserRole;
    }

    public String getUserName() {
        if (UserName==null||UserName.equals("")){
            return " ";
        }
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String UserPhone) {
        this.UserPhone = UserPhone;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String UserPhoto) {
        this.UserPhoto = UserPhoto;
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
//    data.UserId	id
//    data.UserCompanyId	公司id
//    data.UserCompanyName	公司名称
//    data.UserUsername	用户名
//    data.UserPassword	密码
//    data.UserRole	角色
//    data.UserName	姓名
//    data.UserPhone	手机号
//    data.UserPhoto	照片路径
//    data.gmtCreate	添加时间戳
//    data.gmtModified	修改时间戳
//存用户信息
public void saveUserInfo(UserInfoResult loginresult) {
    if (!TextUtils.isEmpty(loginresult.getAccess_token())){
        access_token=loginresult.getAccess_token();
    }

//    UserId=loginresult.getData().getUserId();
    UserCompanyId=loginresult.getData().getUserCompanyId();
    UserCompanyName=loginresult.getData().getUserCompanyName();
    UserUsername=loginresult.getData().getUserUsername();
    UserRole=loginresult.getData().getUserRole();
    UserName=loginresult.getData().getUserName();
    UserPhone=loginresult.getData().getUserPhone();
    UserPhoto=loginresult.getData().getUserPhoto();
    UserId=loginresult.getData().getUserId();
    gmtCreate=loginresult.getData().getGmtCreate();
    gmtModified=loginresult.getData().getGmtModified();

    UserLivingAddress=loginresult.getData().getUserLivingAddress();
//    UserOrgName=loginresult.getData().getUserO();
    UserIdCard=loginresult.getData().getUserIdCard();
    userResidenceAddress=loginresult.getData().getUserResidenceAddress();
}


    SharedPreferences my_save;
    SharedPreferences.Editor editor;
    public void saveUserInfoLocal(LoginParam params) {
        if (my_save==null)
            my_save = getmContext().getSharedPreferences("my_save", Context.MODE_PRIVATE);
        editor= my_save.edit();
        editor.putString("token",access_token);
        editor.putString("username",params.getUsername());//存
        editor.putString("password",params.getPassword());//存
        editor.commit();//提交
    }

    public LoginParam getLoginInfo() {
        if (my_save==null)
            my_save = getmContext().getSharedPreferences("my_save", Context.MODE_PRIVATE);
        editor= my_save.edit();
        LoginParam loginparam=new LoginParam();
        loginparam.setUsername(my_save.getString("username",""));
        loginparam.setPassword(my_save.getString("password",""));
        loginparam.setToken(my_save.getString("token",""));
        return loginparam;
    }


}
