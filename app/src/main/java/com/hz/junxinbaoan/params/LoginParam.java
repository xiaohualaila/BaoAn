package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/10/27.
 */

public class LoginParam extends BaseParam {
    //    http://218.108.31.2:8882/oauth/token?username=admin&password=123456&client_id=app&client_secret=junxin&grant_type=password
    private String username,password,token;
    private String client_id="app";
    private String client_secret="junxin";
    private String grant_type="password";


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginParam{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", client_id='" + client_id + '\'' +
                ", client_secret='" + client_secret + '\'' +
                ", grant_type='" + grant_type + '\'' +
                '}';
    }
}
