package com.hz.junxinbaoan.activity.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hz.junxinbaoan.MainActivity;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.dialog.Apk_dialog;
import com.hz.junxinbaoan.activity.dialog.LoadingDialog;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.BaseParam;
import com.hz.junxinbaoan.params.LoginParam;
import com.hz.junxinbaoan.params.UserParams;
import com.hz.junxinbaoan.result.BaseResult;
import com.hz.junxinbaoan.result.CodeResult;
import com.hz.junxinbaoan.result.UserInfoResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.Commonutil;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import net.qiujuer.genius.ui.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.BindView;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CODE = 1;
    @BindView(R.id.icon_mine)
    ImageView iconMine;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.icon_password)
    ImageView iconPassword;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.seePassword)
    FrameLayout seePassword;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.forget)
    TextView forget;
    @BindView(R.id.to_register)
    Button toRegister;

    Apk_dialog apk_dialog;
    SeekBar seek;
    private ArrayList<PermissionItem> permissionItems;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_login );
        loadingDialog = new LoadingDialog( mBaseActivity );
        apk_dialog = new Apk_dialog( mBaseActivity );
        Log.e( TAG, "findViews: " + Environment.getExternalStorageDirectory() + "----" + Environment
                .getExternalStoragePublicDirectory( DIRECTORY_DOWNLOADS ) );
//        //权限列表
//        permissionItems = new ArrayList<PermissionItem>();
//        permissionItems.add( new PermissionItem( Manifest.permission.CAMERA, "Camera", R.drawable
//                .permission_ic_camera ) );
//        permissionItems.add( new PermissionItem( Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage", R
//                .drawable.permission_ic_storage ) );
//        permissionItems.add( new PermissionItem( Manifest.permission.ACCESS_COARSE_LOCATION, "Location", R
//                .drawable.permission_ic_location ) );
//        permissionItems.add( new PermissionItem( Manifest.permission.READ_PHONE_STATE, "Phone", R
//                .drawable.permission_ic_contacts ) );
//        permissionItems.add( new PermissionItem( Manifest.permission.ACCESS_FINE_LOCATION) );
//        permissionItems.add( new PermissionItem( Manifest.permission.READ_EXTERNAL_STORAGE));
//        HiPermission.create( mBaseActivity )
//                .permissions( permissionItems )
//                .animStyle( R.style.PermissionAnimModal )//设置动画
//                .style( R.style.PermissionDefaultBlueStyle )//设置主题
//                .checkMutiPermission( new PermissionCallback() {
//                    @Override
//                    public void onClose() {
//                        Log.e( TAG, "close" );
//                        finish();
//                    }
//
//                    @Override
//                    public void onFinish() {
//                    }
//
//                    @Override
//                    public void onDeny(String permission, int position) {
//                        Log.e( TAG,permission+  "close" );
//                    }
//
//                    @Override
//                    public void onGuarantee(String permission, int position) {
//                    }
//                } );
    }

    private boolean isPasswordType = true;


    @Override
    protected void addListeners() {
        super.addListeners();
        seePassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().length() > 0) {
                    isPasswordType = !isPasswordType;
//                pa.setImageResource(isPasswordType ? R.mipmap.login_watch_icon : R.mipmap
// .login_password_can_watch_icon);
                    password.setInputType( isPasswordType ? (InputType.TYPE_CLASS_TEXT | InputType
                            .TYPE_TEXT_VARIATION_PASSWORD) : InputType
                            .TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                }
//                password.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.pwd_filter)));
            }
        } );
        toRegister.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去注册
                Intent intent = new Intent( LoginActivity.this, RegisterActivity.class );
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity( intent );
            }
        } );
        forget.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( mBaseActivity, ForgetPassword.class ) );
            }
        } );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CodeDialog codeDialog=new CodeDialog(mBaseActivity);
//                codeDialog.show();
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
                                Log.e( TAG, "close" );
                                finish();
                            }

                            @Override
                            public void onFinish() {
                                if (checktoLogin()) {
                                    doLogin();
                                }
                            }

                            @Override
                            public void onDeny(String permission, int position) {
                                Log.e( TAG, permission + "close" );
                            }

                            @Override
                            public void onGuarantee(String permission, int position) {
                            }
                        } );


            }
        } );
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (getIntent().getIntExtra( "type", 0 ) == 9997) {
                phone.setText( getIntent().getStringExtra( "name" ) );
                password.setText( getIntent().getStringExtra( "password" ) );
            }
        } catch (Exception e) {
        }

        if (Build.VERSION.SDK_INT >= 23) {
            Log.i( TAG, "onActivityResult write settings " );
            checkPermissions();
        }

        getVCode();
    }

    //6.0以上才能调用
    @TargetApi(23)
    void checkPermissions() {
        if (!Settings.System.canWrite( this )) {
            Intent intent = new Intent( Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse( "package:" + getPackageName() ) );
            startActivityForResult( intent, REQUEST_CODE );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (Settings.System.canWrite( mBaseActivity )) {
                Log.i( TAG, "onActivityResult write settings granted" );

            } else {
                finish();
            }
        }
    }


    //登录接口
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.LOGIN)
        Call<BaseResult> getData(@FieldMap Map<String, Object> map);
    }

    //登录
    //15574914779
    private void doLogin() {
        showDialog( true );
        GetData getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetData.class );
        final LoginParam params = new LoginParam();
        params.setPassword( password.getText().toString() );
        params.setUsername( phone.getText().toString() );
        Log.e( TAG, params.toString() );
        Call<BaseResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<BaseResult>() {
            @Override
            public void onResponse(final Call<BaseResult> call, final Response<BaseResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), false, new ResultHandler
                        .OnHandleListener<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        Log.e( TAG, result.toString() );
                        if (result != null) {
                            if (result.getError() != null) {
                                MyToast.showToast( mBaseActivity, result.getError_description() );
                            } else {
                                MyApplication.mUserInfo.saveUserInfo( result );
                                MyApplication.mUserInfo.saveUserInfoLocal( params );
                                MyToast.showToast( mBaseActivity, "  登录成功  " );
                                getUserInfo();
                            }
                        }
                    }

                    @Override
                    public void onNetError() {
                        Log.e( TAG, "+====" );
                        MyToast.showToast( mBaseActivity, TextUtils.isEmpty( MyApplication.errorMessage ) ? "  " +
                                "网络连接失败，请稍后再试  " : MyApplication.errorMessage );
                        MyApplication.errorMessage = null;
                    }

                    @Override
                    public void onError(String code, String message) {
                        Log.e( TAG, code + "===" + message );
                        MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
                    }
                } );

//                if (response.code() != 200){
//                    if (!CommonUtils.isEmpty(MyApplication.errorMessage)){
//                        MyToast.showToast(mBaseActivity, MyApplication.errorMessage);
//                        MyApplication.errorMessage = "";
//                    }
//                }else {
//
//                }
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                showDialog( false );
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }

    //检查是否可以去登录
    private boolean checktoLogin() {

        if (phone.getText().length() == 0) {
            MyToast.showToast( mBaseActivity, "请输入手机号或身份证" );
            return false;
        }
        if (!Commonutil.isMobileNum( phone.getText().toString() )) {
            MyToast.showToast( mBaseActivity, "手机号码不正确" );
            return false;
        }

        if (password.getText().length() == 0) {
            MyToast.showToast( mBaseActivity, "请输入密码" );
            return false;
        }

        return true;
    }

    //获取个人信息接口
    private interface GetData2 {
        @FormUrlEncoded
        @POST(Constants.GETUSERINFO)
        Call<UserInfoResult> getData(@FieldMap Map<String, Object> map);
    }

    //获取个人信息
    private void getUserInfo() {
        showDialog( true );
        GetData2 getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetData2.class );
        UserParams params = new UserParams();
        params.setPushId( CommonUtils.getPushId() );
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
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
                            MyApplication.mUserInfo.saveUserInfo( result );
                            manService.getMANAnalytics().updateUserAccount( result.getData().getUserName(), result
                                    .getData().getUserId() );
                            startActivity( new Intent( LoginActivity.this, MainActivity.class ) );
                            finish();
                        }
                    }

                    @Override
                    public void onNetError() {
                        startActivity( new Intent( LoginActivity.this, MainActivity.class ) );
                        finish();
                    }

                    @Override
                    public void onError(String code, String message) {
                        startActivity( new Intent( LoginActivity.this, MainActivity.class ) );
                        finish();
                    }
                } );
            }

            @Override
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
                startActivity( new Intent( LoginActivity.this, MainActivity.class ) );
                finish();
            }
        } );
    }

    @Override
    public void onBackPressed() {
        MyApplication.getInstance().finish();
    }


    //版本
    private interface GetData3 {
        @FormUrlEncoded
        @POST(Constants.VCODE)
        Call<CodeResult> getData(@FieldMap Map<String, Object> map);
    }

    //版本
    private void getVCode() {
        if (apk_dialog != null && apk_dialog.isShowing()) {
            return;
        }
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
                                    Log.i( TAG, "onSuc: get Internet VCode :" + result.getData().get( j )
                                            .getAppInfoVersion() );
                                    Log.i( TAG, "onSuc: get本地VCode :" + getResources()
                                            .getString( R.string.vcodes ) );
                                    if (!result.getData().get( j ).getAppInfoVersion().equals( getResources()
                                            .getString( R.string.vcodes ) )) {

                                        final int finalJ = j;
                                        final int finalJ1 = j;
//                                        new AlertDialog.Builder( mBaseActivity )
//                                                .setTitle( "" )
//                                                .setMessage( "有新版需要更新" )
//                                                .setPositiveButton( "是", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
////                                                        Intent intent = new Intent();
////                                                        intent.setAction("android.intent.action.VIEW");
////                                                        Uri content_url = Uri.parse(result.getData().get(finalJ)
////                                                          .getAppInfoUrl());
////                                                        intent.setData(content_url);
////                                                        startActivity(intent);
//                                                        vcode = result.getData().get( finalJ ).getAppInfoVersion()
//                                                                + (int) (Math.random() * 100);
//                                                        apk_dialog.show();
//                                                        apk_dialog.setCancelable( false );
//                                                        seek = apk_dialog.getSeekBar();
//                                                        downApk( result.getData().get( finalJ1 ).getAppInfoUrl() );
//                                                    }
//                                                } ).setCancelable( false )
//                                                .show();
                                    } else {
                                    }
                                    break;
                                }
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onNetError() {
                    }

                    @Override
                    public void onError(String code, String message) {
                    }
                } );
            }

            @Override
            public void onFailure(Call<CodeResult> call, Throwable t) {
            }
        } );
    }

    String vcode;
    DownloadManager downloadManager;

    public void downApk(String url) {
//        seek.setVisibility(View.VISIBLE);
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request( Uri.parse( url ) );
//指定下载路径和下载文件名

        request.setDestinationInExternalPublicDir( "/download/", "baoan" + vcode + ".apk" );
//获取下载管理器
        downloadManager = (DownloadManager) mBaseActivity.getSystemService( Context.DOWNLOAD_SERVICE );
//将下载任务加入下载队列，否则不会进行下载
        updateViews( downloadManager.enqueue( request ) );
    }

    private void updateViews(final Long downlaodId) {
        final Timer myTimer = new Timer();
        myTimer.schedule( new TimerTask() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query().setFilterById( downlaodId );
                Cursor cursor = ((DownloadManager) getSystemService( Context.DOWNLOAD_SERVICE )).query( query );
                cursor.moveToFirst();
                float bytes_downloaded = cursor.getInt( cursor.getColumnIndex( DownloadManager
                        .COLUMN_BYTES_DOWNLOADED_SO_FAR ) );
                float bytes_total = cursor.getInt( cursor.getColumnIndex( DownloadManager.COLUMN_TOTAL_SIZE_BYTES ) );
                cursor.close();
                final int dl_progress = (int) (bytes_downloaded * 100 / bytes_total);
                Log.i( "czx", "progress:" + dl_progress );
                if (dl_progress >= 100) {
                    myTimer.cancel();
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            MyToast.showToast( mBaseActivity, "下载完成" );
                            apk_dialog.dismiss();
                            seek.setProgress( dl_progress );
//                           seek.setVisibility(View.GONE);
                            String path = "/storage/emulated/0/download/" + "baoan" + vcode + ".apk";
                            setPermission(path);
                            openFile(path);

                        }
                    } );
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            seek.setProgress( dl_progress );
//                            mDownloadFileBtn.setText(dl_progress + "%");
                        }
                    } );

                }
            }
        }, 0, 800 );
    }

    /**
     * 提升读写权限
     * @return
     * @throws IOException
     */
    private void setPermission(String filePath) {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(String path) {

        File file = new File( path );
        Intent intent = new Intent();
        intent.setAction( Intent.ACTION_VIEW );
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        //判读版本是否在7.0以上
        Uri apkUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
             apkUri = FileProvider.getUriForFile( mBaseActivity, "com.hz.junxinbaoan.fileProvider", file );
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(file);

        }
        intent.setDataAndType( apkUri, "application/vnd.android.package-archive" );
        startActivity( intent );
    }
}
