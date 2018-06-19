package com.hz.junxinbaoan.activity.mine;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.login.LoginActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.common.Settings;
import com.hz.junxinbaoan.params.ChangeUserInfoParams;
import com.hz.junxinbaoan.params.FileParams;
import com.hz.junxinbaoan.params.SendCodeParams;
import com.hz.junxinbaoan.result.ErrorResult;
import com.hz.junxinbaoan.result.VcodeResult;
import com.hz.junxinbaoan.utils.BitmapUtil;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.Commonutil;
import com.hz.junxinbaoan.utils.GlideLoader;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import net.qiujuer.genius.ui.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ChangeMineInfo extends BaseActivity {

    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_lay)
    RelativeLayout titleLay;
    @BindView(R.id.pic_head_icon)
    ImageView picHeadIcon;
    @BindView(R.id.pic_head)
    ImageView picHead;
    @BindView(R.id.change_pic)
    ImageView changePic;
    @BindView(R.id.mine_icon)
    ImageView mineIcon;
    @BindView(R.id.mine_et)
    EditText mineEt;
    @BindView(R.id.id_icon)
    ImageView idIcon;
    @BindView(R.id.id_et)
    EditText idEt;
    @BindView(R.id.workid_icon)
    ImageView workidIcon;
    @BindView(R.id.workid_et)
    EditText workidEt;
    @BindView(R.id.oldhome_icon)
    ImageView oldhomeIcon;
    @BindView(R.id.oldhome_et)
    EditText oldhomeEt;
    @BindView(R.id.home_icon)
    ImageView homeIcon;
    @BindView(R.id.home_et)
    EditText homeEt;
    @BindView(R.id.phone_icon)
    ImageView phoneIcon;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.vcode_icon)
    ImageView vcodeIcon;
    @BindView(R.id.vcode_et)
    EditText vcodeEt;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.sendCheckcode)
    TextView sendCheckcode;
    @BindView(R.id.commit_btn)
    Button commitBtn;

    @BindView(R.id.firstname)
    TextView firstname;
    //图片相关
    private ArrayList<String> path = new ArrayList<>();

    private TimeCount myTimeCount = new TimeCount( 60000, 1000 );
    private File pic_file;
    //管理线程
    private List<Call> mTasks;
    //管理上传
    private CompositeSubscription mSubscription;
    private ArrayList<PermissionItem> permissionItems;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_change_mine_info );
        mSubscription = new CompositeSubscription();
        mTasks = new ArrayList<>();
        //权限列表
        permissionItems = new ArrayList<PermissionItem>();
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        sendCheckcode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Commonutil.isMobileNum( phoneEt.getText().toString().replace( " ", "" ) )) {
                    sendCheckcode();
                } else {
                    MyToast.showToast( mBaseActivity, "手机号码输入有误" );
                }
            }
        } );
        phoneEt.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0) {//代表用户在输入
                    if (s.length() == 3 || s.length() == 8) {
                        s = s + " ";
                        phoneEt.setText( s );
                        phoneEt.setSelection( s.length() );
                    }
                } else if (before == 1) {//代表用户在删除
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        } );
        changePic.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionItems.clear();
                permissionItems.add( new PermissionItem( Manifest.permission.CAMERA, "Camera", R.drawable
                        .permission_ic_camera ) );
                HiPermission.create( mBaseActivity )
                        .permissions( permissionItems )
                        .animStyle( R.style.PermissionAnimModal )//设置动画
                        .style( R.style.PermissionDefaultBlueStyle )//设置主题
                        .checkMutiPermission( new PermissionCallback() {
                            @Override
                            public void onClose() {
                                Log.e( "TAG", "close" );
//                                finish();
                            }

                            @Override
                            public void onFinish() {
                                ImageConfig imageConfig
                                        = new ImageConfig.Builder( new GlideLoader() )
                                        .steepToolBarColor( getResources().getColor( R.color.app_style_blue ) )
                                        // 标题的背景颜色 （默认黑色）
                                        .titleBgColor( getResources().getColor( R.color.app_style_blue ) )
                                        // 提交按钮字体的颜色  （默认白色）
                                        .titleSubmitTextColor( getResources().getColor( R.color.white ) )
                                        // 标题颜色 （默认白色）
                                        .titleTextColor( getResources().getColor( R.color.white ) )
                                        // 开启单选   （默认为多选）
                                        .singleSelect()
                                        // 开启拍照功能 （默认关闭）
                                        .showCamera()
                                        // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                                        .filePath( Settings.PIC_PATH )
                                        .requestCode( 1000 )
                                        .build();
                                ImageSelector.open( mBaseActivity, imageConfig );   // 开启图片选择器
                            }

                            @Override
                            public void onDeny(String permission, int position) {
                                Log.e( "TAG",permission+  "close" );
                            }

                            @Override
                            public void onGuarantee(String permission, int position) {
                            }
                        } );

//                if ((ContextCompat.checkSelfPermission( mBaseActivity,
//                        Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED)) {
//
//                } else {
//                    ActivityCompat.requestPermissions( mBaseActivity,
//                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            1 );
//                }
            }
        } );
        commitBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty( mineEt.getText().toString() )) {
                    MyToast.showToast( mBaseActivity, "请输入姓名" );
                    return;
                }
//                if (TextUtils.isEmpty(idEt.getText().toString())){
//                    MyToast.showToast(mBaseActivity,"请输入身份证号码");
//                    return;
//                }
//                if (TextUtils.isEmpty(workidEt.getText().toString())){
//                    MyToast.showToast(mBaseActivity,"请输入保安证号");
//                    return;
//                }
//                if (TextUtils.isEmpty(oldhomeEt.getText().toString())){
//                    MyToast.showToast(mBaseActivity,"请输入户籍所在地");
//                    return;
//                }
//                if (TextUtils.isEmpty(homeEt.getText().toString())){
//                    MyToast.showToast(mBaseActivity,"请输入现居地");
//                    return;
//                }
                if (TextUtils.isEmpty( phoneEt.getText().toString().replace( " ", "" ) )) {
                    MyToast.showToast( mBaseActivity, "请输入电话号码" );
                    return;
                }
                if (TextUtils.isEmpty( vcodeEt.getText().toString() )) {
                    MyToast.showToast( mBaseActivity, "请输入验证码" );
                    return;
                }
                if (pic_file != null) {
                    sendFile();
                } else {
                    changeUserInFo( MyApplication.mUserInfo.getUserPhoto() );
                }

            }
        } );
    }

    @Override
    protected void initViews() {
        super.initViews();
        mineEt.setText( MyApplication.mUserInfo.getUserName() );
        if (!TextUtils.isEmpty( MyApplication.mUserInfo.getUserPhoto() )) {
            firstname.setText( "" );
            try {
                Glide.with( mBaseActivity ).load( MyApplication.mUserInfo.getUserPhoto() ).asBitmap()
                        .centerCrop()
                        .error( R.color.line_gray )
                        .into( picHead );
            } catch (Exception ignore) {
            }
        } else {
            picHead.setImageResource( R.color.picback );
            firstname.setText( MyApplication.mUserInfo.getUserName().charAt( MyApplication.mUserInfo.getUserName()
                    .length() - 1 ) + "" );
        }
        phoneEt.setText( CommonUtils.changePhoneString( MyApplication.mUserInfo.getUserPhone() ) );

        homeEt.setText( MyApplication.mUserInfo.getUserLivingAddress() );//居住地
        oldhomeEt.setText( MyApplication.mUserInfo.getUserResidenceAddress() );
    }

    private class TimeCount extends CountDownTimer {

        TimeCount(long millisInFuture, long countDownInterval) {
            super( millisInFuture, countDownInterval );
        }

        @Override
        public void onTick(long l) {
            sendCheckcode.setEnabled( false );
            sendCheckcode.setText( l / 1000 + "秒" );
        }

        @Override
        public void onFinish() {
            sendCheckcode.setEnabled( true );
            sendCheckcode.setText( "获取验证码" );
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra( ImageSelectorActivity.EXTRA_RESULT );
            path.clear();
            path.addAll( pathList );

            Glide.with( mBaseActivity ).load( path.get( path.size() - 1 ) ).asBitmap()
                    .centerCrop()
                    .error( R.color.line_gray )
                    .into( picHead );
            firstname.setText( "" );
            pic_file = new File( path.get( path.size() - 1 ) );
        }
    }

    //上传图片
    private interface SendPic {
        @Multipart
        @POST(Constants.UploadFile)
        Call<ErrorResult> getData(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> map);
    }

    //发送图片
    private void sendFile() {
        showDialog( true );
        Subscription subscribe = rx.Observable.create( new rx.Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                if (pic_file.length() > 200 * 1024) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;//设为真时 获取图片实际宽高
                    BitmapFactory.decodeFile( pic_file.getAbsolutePath(), options );
                    final int height = options.outHeight;
                    final int width = options.outWidth;
                    //缩放比例
                    int ratio = 1;
                    if (height > 1280 || width > 720) {
                        final int heightRation = Math.round( (float) height / (float) 1280 );
                        final int widthRation = Math.round( (float) width / (float) 720 );
                        ratio = heightRation < widthRation ? heightRation : widthRation;
                    }
                    options.inSampleSize = ratio;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile( pic_file.getPath(), options );
                    //是否有旋转角度
                    int degree = 0;
                    try {
                        ExifInterface exifInterface = new ExifInterface( pic_file.getAbsolutePath() );
                        int orientation = exifInterface.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL );
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                degree = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                degree = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                degree = 270;
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (degree != 0)
                        bitmap = BitmapUtil.rotateBitmap( bitmap, degree );
                    File resultFile = new File( Settings.TEMP_PATH, UUID.randomUUID() + ".jpg" );
                    try {
                        FileOutputStream fos = new FileOutputStream( resultFile );
                        bitmap.compress( Bitmap.CompressFormat.JPEG, 60, fos );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext( resultFile );
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext( pic_file );
                    subscriber.onCompleted();
                }
            }
        } )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Action1<File>() {
                    @Override
                    public void call(File file) {
                        // System.out.println("file legnth: "+ file.length());
                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        //设置log显示的内容
                        if (Settings.DEBUG)
                            logging.setLevel( HttpLoggingInterceptor.Level.HEADERS );
                        else
                            logging.setLevel( HttpLoggingInterceptor.Level.BASIC );
                        //给HttpLoggingInterceptor添加log
                        //OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
                        OkHttpClient okHttpClient = CommonUtils.getUnsafeOkHttpClient( logging );
                        //生成retrofit
                        Retrofit retrofit = new Retrofit.Builder().client( okHttpClient )
                                .addConverterFactory( GsonConverterFactory.create() )
                                .baseUrl( Constants.BASE_URL ).build();
                        SendPic upload = retrofit.create( SendPic.class );
                        FileParams param = new FileParams();
                        param.setType( "photo" );
                        Map<String, Object> postMap = CommonUtils.getPostMap( param );
                        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
                        for (Map.Entry<String, Object> temp : postMap.entrySet()) {
                            if (temp.getKey() != "file")
                                map.put( temp.getKey(),
                                        RequestBody.create( MediaType.parse( "multipart/form-data" ), (String) temp
                                                .getValue() ) );
                        }

                        final RequestBody requestFile = RequestBody.create( MediaType.parse( "multipart/form-data" ),
                                file );
                        MultipartBody.Part body = MultipartBody.Part.createFormData( "file", file.getName(),
                                requestFile );
                        final Call<ErrorResult> call = upload.getData( body, map );
                        mTasks.add( call );
                        final File localfile = file;
                        call.enqueue( new Callback<ErrorResult>() {
                            @Override
                            public void onResponse(Call<ErrorResult> call, Response<ErrorResult> response) {
                                showDialog( false );
                                ResultHandler.Handle( getApplicationContext(), response.body(), new ResultHandler
                                        .OnHandleListener<ErrorResult>() {
                                    @Override
                                    public void onSuccess(ErrorResult outResult) {
                                        Log.e( "fileurl", outResult.getData() );
//                                       /root/helga-lovekaty-naked-lana-light-svetlana-nikitenko-17.jpg
                                        changeUserInFo( outResult.getData() );
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
                            public void onFailure(Call<ErrorResult> call, Throwable t) {
                                showDialog( false );
                                try {
                                    Log.e( "net", t.getMessage() );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
                            }
                        } );

                    }

                } );
        mSubscription.add( subscribe );
    }

    //修改个人信息
    private interface ChangeUser {
        @FormUrlEncoded
        @POST(Constants.CHANGEUSERINFO)
        Call<ErrorResult> getData(@FieldMap Map<String, Object> map);
    }

    private void changeUserInFo(String filePath) {
        showDialog( true );
        ChangeUser getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( ChangeUser.class );
        final ChangeUserInfoParams params = new ChangeUserInfoParams();
        params.setUserName( mineEt.getText().toString() );
        params.setCodeNow( vcodeEt.getText().toString() );
        params.setUserPhone( phoneEt.getText().toString().replace( " ", "" ) );
        params.setUserPhoto( filePath );
        params.setUserLivingAddress( homeEt.getText().toString() );
        params.setUserResidenceAddress( oldhomeEt.getText().toString() );
        Call<ErrorResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<ErrorResult>() {
            @Override
            public void onResponse(final Call<ErrorResult> call, final Response<ErrorResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler.OnHandleListener<ErrorResult>
                        () {
                    @Override
                    public void onSuccess(ErrorResult result) {
                        if (result.getCode().equals( "0000" )) {
                            MyToast.showToast( mBaseActivity, "  修改成功  " );
                            if (params.getUserPhone().equals( MyApplication.mUserInfo.getUserPhone().trim() )) {
                                finish();//号码一样
                            } else {
                                MyToast.showToast( mBaseActivity, "  手机号已更换，请重新登录  " );
                                MyApplication.mUserInfo.delUserInfo();
                                manService.getMANAnalytics().updateUserAccount( "", "" );// 用户注销埋点
                                startActivity( new Intent( mBaseActivity, LoginActivity.class ) );
                            }
                        } else if (result.getCode().equals( "9999" )) {
                            MyToast.showToast( mBaseActivity, " " + result.getData() + " " );
                        }

                    }

                    @Override
                    public void onNetError() {
                        showDialog( false );
                    }

                    @Override
                    public void onError(String code, String message) {
                        showDialog( false );
                    }
                } );
            }

            @Override
            public void onFailure(Call<ErrorResult> call, Throwable t) {
                if (loadingDialog.isShowing()) {
                    showDialog( false );
                }
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }


    //获取短信验证码
    private interface GetVcode {
        @FormUrlEncoded
        @POST(Constants.GetVcode)
        Call<VcodeResult> getData(@FieldMap Map<String, Object> map);
    }

    //发送验证码
    private void sendCheckcode() {
        showDialog( true );
        GetVcode getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetVcode.class );
        SendCodeParams params = new SendCodeParams();
        params.setPhone( phoneEt.getText().toString().replace( " ", "" ) );
        Call<VcodeResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<VcodeResult>() {
            @Override
            public void onResponse(final Call<VcodeResult> call, final Response<VcodeResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler.OnHandleListener<VcodeResult>
                        () {
                    @Override
                    public void onSuccess(VcodeResult result) {
                        if (result != null) {
                            MyToast.showToast( mBaseActivity, result.getData() );
                            //开启倒计时
                            if (result.getCode().equals( "0000" )) {
                                //开启倒计时
                                if (myTimeCount != null) {
                                    myTimeCount.start();
                                }
                            }
                        }
                    }

                    @Override
                    public void onNetError() {
                        showDialog( false );
                    }

                    @Override
                    public void onError(String code, String message) {
                        showDialog( false );
                    }
                } );
            }

            @Override
            public void onFailure(Call<VcodeResult> call, Throwable t) {
                if (loadingDialog.isShowing()) {
                    showDialog( false );
                }
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );

    }

}
