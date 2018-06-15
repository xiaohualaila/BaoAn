package com.hz.junxinbaoan.activity.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import com.hz.junxinbaoan.MainActivity;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.BaseParam;
import com.hz.junxinbaoan.params.LoginParam;
import com.hz.junxinbaoan.params.UserParams;
import com.hz.junxinbaoan.result.CodeResult;
import com.hz.junxinbaoan.result.UserInfoResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import java.util.ArrayList;
import java.util.Map;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class WelcomeActivity extends BaseActivity {
    private static final String TAG = "WelcomeActivity";
    private boolean haveCode = false;
    private ArrayList<PermissionItem> permissionItems;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_welcome );

    }

    @Override
    protected void onResume() {
        super.onResume();

        //权限列表
        permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add( new PermissionItem( Manifest.permission.CAMERA, "Camera", R.drawable
                .permission_ic_camera ) );
        permissionItems.add( new PermissionItem( Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage", R
                .drawable.permission_ic_storage ) );
        permissionItems.add( new PermissionItem( Manifest.permission.ACCESS_COARSE_LOCATION, "Location", R
                .drawable.permission_ic_location ) );
        permissionItems.add( new PermissionItem( Manifest.permission.READ_PHONE_STATE, "Phone", R
                .drawable.permission_ic_contacts ) );
        permissionItems.add( new PermissionItem( Manifest.permission.ACCESS_FINE_LOCATION ) );
        permissionItems.add( new PermissionItem( Manifest.permission.READ_EXTERNAL_STORAGE ) );
        HiPermission.create( mBaseActivity )
                .permissions( permissionItems )
                .animStyle( R.style.PermissionAnimModal )//设置动画
                .style( R.style.PermissionDefaultBlueStyle )//设置主题
                .checkMutiPermission( new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.i( "TAG", "close" );
                        //用户关闭权限申请
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        //版本检测
                        getVCode();
//                        myTimer.start();
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        Log.i( "TAG", permission + "close" );
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                    }
                } );

    }

    //倒计时
    private CountDownTimer myTimer = new CountDownTimer( 1000, 1000 ) {
        @Override
        public void onTick(long l) {
//            .setText((l - 1000) / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            if (haveCode) {
                startActivity( new Intent( mBaseActivity, LoginActivity.class ) );
            } else {
                LoginParam param = MyApplication.mUserInfo.getLoginInfo();
                if (!TextUtils.isEmpty( param.getUsername() ) && !TextUtils.isEmpty( param.getPassword() )) {
                    getUserInfo( MyApplication.mUserInfo.getLoginInfo().getToken() );
                } else {
                    startActivity( new Intent( WelcomeActivity.this, LoginActivity.class ) );
                    finish();
                }
            }
        }
    };

    //获取个人信息接口
    private interface GetData2 {
        @FormUrlEncoded
        @POST(Constants.GETUSERINFO)
        Call<UserInfoResult> getData(@FieldMap Map<String, Object> map);
    }

    //获取个人信息
    private void getUserInfo(final String token) {
        showDialog( true );
        GetData2 getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetData2.class );
        UserParams params = new UserParams();
        params.setPushId( CommonUtils.getPushId() );
        params.setAccess_token( token );
        Call<UserInfoResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<UserInfoResult>() {
            @Override
            public void onResponse(final Call<UserInfoResult> call, final Response<UserInfoResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                        .OnHandleListener<UserInfoResult>() {
                    @Override
                    public void onSuccess(UserInfoResult result) {
                        if (result.getData() != null) {
                            result.setAccess_token( token );
                            MyApplication.mUserInfo.saveUserInfo( result );
                            //用户登录及注销埋点
                            manService.getMANAnalytics().updateUserAccount( result.getData().getUserName(), result
                                    .getData().getUserId() );
                            startActivity( new Intent( WelcomeActivity.this, MainActivity.class ) );
                            finish();
                        }
                    }

                    @Override
                    public void onNetError() {
                        startActivity( new Intent( WelcomeActivity.this, LoginActivity.class ) );
                        finish();
                    }

                    @Override
                    public void onError(String code, String message) {
                        startActivity( new Intent( WelcomeActivity.this, LoginActivity.class ) );
                        finish();
                    }
                } );
            }

            @Override
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
                startActivity( new Intent( WelcomeActivity.this, LoginActivity.class ) );
                finish();
            }
        } );
    }


    //版本
    private interface GetData3 {
        @FormUrlEncoded
        @POST(Constants.VCODE)
        Call<CodeResult> getData(@FieldMap Map<String, Object> map);
    }

    //版本
    private void getVCode() {
        GetData3 getData = CommonUtils.buildRetrofit( "http://101.37.136.249:82/", mBaseActivity ).create(
                GetData3.class );
        BaseParam param = new BaseParam();
        Call<CodeResult> call = getData.getData( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<CodeResult>() {
            @Override
            public void onResponse(final Call<CodeResult> call, final Response<CodeResult> response) {
                ResultHandler.Handle( mBaseActivity, response.body(), false, new ResultHandler
                        .OnHandleListener<CodeResult>() {
                    @Override
                    public void onSuccess(final CodeResult result) {
                        if (result != null) {
                            Log.i( TAG, "onSuccess: getVCode" );
                            for (int j = 0; j < result.getData().size(); j++) {
                                if (result.getData().get( j ).getAppInfoType().equals( "app" ) &&
                                        result.getData().get( j ).getAppInfoClient().equals( "andriod" )) {
                                    Log.i( TAG, "onSuc: getVCode :" + result.getData().get( j ).getAppInfoVersion() );
                                    if (!result.getData().get( j ).getAppInfoVersion().equals( getResources()
                                            .getString( R.string.vcodes ) )) {
                                        haveCode = true;
                                    } else {
                                    }
                                    break;
                                }
                            }
                        } else {
                        }
                        myTimer.start();
                    }

                    @Override
                    public void onNetError() {
                        myTimer.start();
                    }

                    @Override
                    public void onError(String code, String message) {
                        myTimer.start();
                    }
                } );
            }

            @Override
            public void onFailure(Call<CodeResult> call, Throwable t) {
                myTimer.start();
            }
        } );
    }


}
