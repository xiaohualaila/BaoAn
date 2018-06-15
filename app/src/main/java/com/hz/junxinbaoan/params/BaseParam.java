package com.hz.junxinbaoan.params;


import com.hz.junxinbaoan.MyApplication;

/**
 * Created by zhangxl on 2017/3/28.
 */

public class BaseParam {

//    http://218.108.31.2:8882/oauth/token?username=admin&password=123456&client_id=app&client_secret=junxin&grant_type=password


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;
    private String access_token = MyApplication.mUserInfo.getAccess_token();
//    private String access_token = "8ea41eda-f39b-44aa-b7d9-a4c38aeb046a";


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public String toString() {
        return "BaseParam{" +
                "user_id='" + user_id + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}
