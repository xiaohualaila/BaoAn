package com.hz.junxinbaoan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.activity.login.LoginActivity;
import com.hz.junxinbaoan.result.BaseResult;


public class ResultHandler {

    public static class SimpleOnHandleListener<T> implements OnHandleListener<T> {

        @Override
        public void onSuccess(T result) {
        }

        @Override
        public void onNetError() {
        }

        @Override
        public void onError(String code, String message) {
        }
    }

    public static <T extends BaseResult> void Handle(Context context, T result, OnHandleListener<T> resultOk) {
        Handle( context, result, true, resultOk );
    }

    public static <T extends BaseResult> void Handle(Context context, T result, boolean showToast,
                                                     OnHandleListener<T> resultOk) {

        Activity currentActivity = MyApplication.getInstance().getCurrentActivity();
        if (result != null) {
            switch (result.getCode()) {
                case "0000":
                case "9999":
                    if (resultOk != null) {
                        resultOk.onSuccess( result );
                    }
                    break;
                case "9998":
                    if (resultOk != null) {
                        resultOk.onSuccess( result );
                    }
                    MyToast.showToast( context, "用户信息过期!" );
                    Intent i2 = new Intent( currentActivity, LoginActivity.class );
                    i2.putExtra( "type", 9998 );
                    i2.putExtra( "name", MyApplication.mUserInfo.getLoginInfo().getUsername() );
                    i2.putExtra( "password", MyApplication.mUserInfo.getLoginInfo().getPassword() );
                    i2.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    MyApplication.mUserInfo.delUserInfo();
                    MyApplication.getInstance().setExitLoginTag();
                    MyApplication.getInstance().removeAllActivity();
                    MyApplication.getInstance().startActivity( i2 );
                    break;
                case "9997":
                    if (resultOk != null) {
                        resultOk.onSuccess( result );
                    }
                    MyToast.showToast( context, "账号异地登录!" );
                    Intent intent = new Intent( MyApplication.getInstance(), LoginActivity.class );
                    intent.putExtra( "type", 9997 );
                    intent.putExtra( "name", MyApplication.mUserInfo.getLoginInfo().getUsername() );
                    intent.putExtra( "password", MyApplication.mUserInfo.getLoginInfo().getPassword() );
                    MyApplication.mUserInfo.delUserInfo();
                    MyApplication.getInstance().setExitLoginTag();
                    MyApplication.getInstance().removeAllActivity();
                    intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    MyApplication.getInstance().startActivity( intent );
                    break;
                default:
                    resultOk.onError( result.getCode(), "网络连接失败，请稍后再试!" );
//                MyToast.showToast(context, "     网络连接失败，请稍后再试    ");
                    break;
            }
        } else {
            if (resultOk != null) {
                resultOk.onNetError();
            }
            if (MyApplication.errorMessage != null && MyApplication.errorMessage.equals( "账号异地登录!" )) {
                MyToast.showToast( context, "账号异地登录!" );
                Intent i = new Intent( MyApplication.getInstance(), LoginActivity.class );
                i.putExtra( "type", 9997 );
                i.putExtra( "name", MyApplication.mUserInfo.getLoginInfo().getUsername() );
                i.putExtra( "password", MyApplication.mUserInfo.getLoginInfo().getPassword() );
                i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                MyApplication.mUserInfo.delUserInfo();
                MyApplication.getInstance().setExitLoginTag();
                MyApplication.getInstance().removeAllActivity();
                MyApplication.getInstance().startActivity( i );
                MyApplication.errorMessage = null;
            } else if (MyApplication.errorMessage != null && MyApplication.errorMessage.equals( "用户信息过期!" )) {
                MyToast.showToast( context, "用户信息过期!" );
                Intent i2 = new Intent( currentActivity, LoginActivity.class );
                i2.putExtra( "type", 9998 );
                i2.putExtra( "name", MyApplication.mUserInfo.getLoginInfo().getUsername() );
                i2.putExtra( "password", MyApplication.mUserInfo.getLoginInfo().getPassword() );
                i2.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                MyApplication.mUserInfo.delUserInfo();
                MyApplication.getInstance().setExitLoginTag();
                MyApplication.getInstance().removeAllActivity();
                MyApplication.getInstance().startActivity( i2 );
                MyApplication.errorMessage = null;
            } else if (showToast) {
                MyToast.showToast( context, TextUtils.isEmpty( MyApplication.errorMessage ) ? "  网络连接失败，请稍后再试  " :
                        MyApplication.errorMessage );
                MyApplication.errorMessage = null;
            }

        }
    }

    private static void goToLogin(Context context) {

        MyApplication.mUserInfo.delUserInfo();
        MyApplication.getInstance().setExitLoginTag();
        MyApplication.getInstance().removeAllActivity();
        Intent intent = new Intent( MyApplication.getInstance(), LoginActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        MyApplication.getInstance().startActivity( intent );
    }

    public interface OnHandleListener<T> {
        /**
         * 接口调用成功
         *
         * @param result 调用接口返回的数据
         */
        public void onSuccess(T result);

        /**
         * 网络错误
         */
        public void onNetError();


        String access_token = null, token_type = null, expires_in = null;

        /**
         * 接口调用失败
         *
         * @param code
         * @param message
         */
        public void onError(String code, String message);
    }
}
