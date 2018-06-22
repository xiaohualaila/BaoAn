package com.hz.junxinbaoan.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.dialog.LoadingDialog;
import com.hz.junxinbaoan.activity.login.WelcomeActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.utils.BitmapUtil;
import com.hz.junxinbaoan.utils.DensityUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;


/**
 * Activity基类
 */
public class BaseActivity extends FragmentActivity {
    public BaseActivity mBaseActivity;
    public MyApplication myApplication;
    private Toast mToast;
    //    public LoadingDialog mLoadingDialog;
    public static boolean isForeground = false;
    //推送广播接收
    private MyReceiver mReceiver;
    public LoadingDialog loadingDialog;
    //    private LogWriter mLogWriter;
    public MANService manService;
//    private ArrayList<PermissionItem> permissionItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Log.e( "TAG", "BaseActivity savedInstanceState " + savedInstanceState );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        mBaseActivity = this;
//
//        if (null!= savedInstanceState){
//
//            Log.i( "TAG", "onCreate savedInstanceState:   " + (null!= savedInstanceState) );
//            Intent intent = new Intent( mBaseActivity, WelcomeActivity.class );
//            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//            startActivity( intent );
//            MyApplication.getInstance().finish();
//        }
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        myApplication = MyApplication.getInstance();

        getIntentData();
        findViews( savedInstanceState );
        SystemBarTintManager tintManager = new SystemBarTintManager( this );
        tintManager.setStatusBarTintResource( R.color.black );
        tintManager.setStatusBarTintEnabled( true );
        initButterKnife();
        //修改状态栏
//        initStateBar();
        loadingDialog = new LoadingDialog( this );
        loadingDialog.setCanceledOnTouchOutside( false );
        manService = MANServiceProvider.getService();
        initViews();
        addListeners();
        requestOnCreate();
        myApplication.addCurrentActivity( mBaseActivity );
//        // FIXME: 2017/4/5
//        ToastUtil.showToast(getClass().getSimpleName());
    }

    protected void initButterKnife() {
        ButterKnife.bind( mBaseActivity );
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes( winParams );
    }


    //*************************************// TODO: 2017/3/9 0009 状态栏修改代码分割线

    @Override
    protected void onStart() {
        super.onStart();
        //        mLoadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeCurrentActivity( mBaseActivity );
        isForeground = false;
        if (loadingDialog != null) {
            if (loadingDialog.isShowing())
                loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        String registrationID = JPushInterface.getRegistrationID(MyApplication.getInstance());
//        Log.i("jpush_c", registrationID);
        if (mReceiver == null) {
            mReceiver = new MyReceiver( mBaseActivity );
            IntentFilter filter = new IntentFilter( Constants.GET_PUSH_ACTION );
            registerReceiver( mReceiver, filter );
            isForeground = true;
        }
        manService.getMANPageHitHelper().pageAppear( this );//手动埋点
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        //友盟
        //        MobclickAgent.onPause(mBaseActivity);
        manService.getMANPageHitHelper().pageDisAppear( this );//手动埋点
    }

    @Override
    protected void onStop() {
        super.onStop();
        //        mLoadingDialog.cancel();
        //        mLoadingDialog = null;
        if (mReceiver != null) {
            unregisterReceiver( mReceiver );
            mReceiver = null;
        }

    }


    /**
     * 获取界面传递数据
     */
    protected void getIntentData() {

    }

    /**
     * 初始化布局中的空间，首先要调用setContentView
     */
    protected void findViews(Bundle savedInstanceState) {
        //        ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);

    }

    /**
     * 初始化本地数据
     */
    protected void initViews() {
    }

    /**
     * 添加监听器
     */
    protected void addListeners() {

    }


    /**
     * 在onCreate中请求服务
     */
    protected void requestOnCreate() {

    }

    /**
     * 点击外面取消输入法如果外层包裹了scrollview则事件会被处理，不会传出到activity中，也就无效了
     */
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.getCurrentFocus() != null) {
                if (this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow( this.getCurrentFocus().getWindowToken(), InputMethodManager
                            .HIDE_NOT_ALWAYS );
                }
            }
        }
        return super.onTouchEvent( event );
    }

    /**
     * 验证是否登录
     *
     * @return 已经登录返回true 游客返回false
     */
    public boolean checkLogin() {
//        if (MyApplication.mUserInfo.getUserid() != 0) {
//            return true;
//        }
        return false;
    }

    /**
     * 验证是否登录,如未登录，跳转登录画面，已登录，返回true，不跳任何画面
     */
    public boolean checkGoLogin() {
//        //        boolean islogin = mBaseActivity.getSharedPreferences(Constants.PREF_FILE_NAME, Context
// .MODE_PRIVATE).getBoolean(Constants.PREF_IS_LOGIN, false);
//        boolean islogin = checkLogin();
//
//        if (islogin) {
//            return true;
//        } else
//            goActivity(LoginActivity.class);
        return false;
    }

    public void goActivity(Class<?> cls) {
        Intent intent = new Intent( mBaseActivity, cls );
        intent.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP );
        startActivity( intent );
    }

    /**
     * 设置TextView
     */
    public static void setTextView(TextView tv, String str) {
        if (!TextUtils.isEmpty( str ))
            tv.setText( str );
        else
            tv.setText( str );

    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @return true：list为空；false：list不为空
     */
    public static <T> boolean listNull(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 下载圆形头像图片，已经设置默认的背景，需传入图片地址，宽，高，imageView控件对象，默认图片
     *
     * @param imageUrl
     * @param width
     * @param height
     * @param imageView
     * @param
     */
    public void LoadHeadImage(String imageUrl, int width, int height, ImageView imageView) {
        // 一定要设置centerCrop()才可以真正获得指定width和height的像素图片，如果不设置那么返回的resource并不是指定尺寸
        if (!TextUtils.isEmpty( imageUrl )) {
            final String url = imageUrl;
            Glide.with( this ).load( imageUrl ).asBitmap().override( width, height ).centerCrop().into( new BitmapImageViewTarget( imageView ) {
                protected void setResource(Bitmap resource) {
                    if (resource != null) {
                        view.setScaleType( ImageView.ScaleType.FIT_XY );
                        view.setImageBitmap( BitmapUtil.getRoundBitmap( resource ) );
                    }
                }
            } );
        }
    }

    public void loadRoundCoenerImage(final Context context, String imageUrl, int width, int height,
                                     ImageView imageView, int defaultImg) {
        // 一定要设置centerCrop()才可以真正获得指定width和height的像素图片，如果不设置那么返回的resource并不是指定尺寸
        if (!TextUtils.isEmpty( imageUrl ))
            Glide.with( context ).load( imageUrl ).asBitmap().placeholder( defaultImg ).override( width,
                    height ).centerCrop().into( new BitmapImageViewTarget( imageView ) {
                protected void setResource(Bitmap resource) {
                    if (resource != null) {
                        view.setScaleType( ImageView.ScaleType.CENTER_CROP );
                        view.setImageBitmap( BitmapUtil.roundCorner( resource, DensityUtils.dp2px( context, 10 ) ) );
                    }
                }
            } );
        else
            imageView.setImageResource( defaultImg );
    }

    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return 判断是不是在WIFI条件下
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断gps是否可用
     *
     * @return
     */
    public boolean isGpsAvailable() {
        LocationManager locationManager = (LocationManager) mBaseActivity.
                getSystemService( Context.LOCATION_SERVICE );
        if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            return true;
        else {
            return false;
        }
    }


    /**
     * 判断微信是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages( 0 );// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get( i ).packageName;
                if (pn.equals( "com.tencent.mm" )) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages( 0 );
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get( i ).packageName;
                if (pn.equals( "com.tencent.mobileqq" )) {
                    return true;
                }
            }


        }
        return false;
    }

    /**
     * 推送广播接收
     */
    private static class MyReceiver extends BroadcastReceiver {
        private final WeakReference<BaseActivity> mReference;

        private MyReceiver(BaseActivity activity) {
            mReference = new WeakReference<>( activity );
        }

        @Override
        public void onReceive(Context context, final Intent intent) {
            final BaseActivity activity = mReference.get();

//            AlertDialog dialog = new AlertDialog.Builder(activity)
//                    .setTitle("提示")
//                    .setMessage(intent.getStringExtra(Constants.CONTENT))
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            int id = intent.getIntExtra(Constants.ACTIVEID, -1);
//                            if (id != -1) {
//                                Intent i = new Intent(activity, MainActivity.class);
//                                i.putExtra("activeid", intent.getIntExtra(Constants.ACTIVEID, -1));
//                                activity.startActivity(i);
//                            }
//                            dialog.dismiss();
//                        }
//                    })
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).create();
//            dialog.show();

        }
    }

    public static boolean isFixedPhone(String fixedPhone) {
        String reg = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
        return Pattern.matches( reg, fixedPhone );
    }

    public void showDialog(boolean show) {
        if (loadingDialog != null) {
            if (show && !loadingDialog.isShowing())
                loadingDialog.show();
            else if (!show && loadingDialog.isShowing())
                loadingDialog.dismiss();
        }
    }

    public void openSetting(final Activity activity, final boolean bool, final String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        builder.setTitle( R.string.notifyTitle );
        builder.setMessage( R.string.notifyMsg );
        // 拒绝, 退出应用
        builder.setNegativeButton( R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int notifyAlertMsg = R.string.notifyAlertMsg;
                        Toast.makeText( activity, activity.getResources().getText( notifyAlertMsg ) + content,
                                Toast.LENGTH_SHORT ).show();
                        if (!bool){
                            finish();
                        }
                    }
                } );

        builder.setPositiveButton( R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                } );
        builder.setCancelable( false );
        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    public void startAppSettings() {
        Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
        intent.setData( Uri.parse( "package:" + getPackageName() ) );
        startActivity( intent );
    }

    /**
     * 关闭键盘
     *
     * @param activity
     */
    public static void closeKeyboard(Activity activity) {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService( Context
                        .INPUT_METHOD_SERVICE );
                inputMethodManager.hideSoftInputFromWindow( activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS );
            }
        }
    }

    /**
     * 发出一个短Toast
     *
     * @param text 内容
     */
    public void toastShort(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 发出一个长toast提醒
     *
     * @param text 内容
     */
    public void toastLong(String text) {
        toast(text, Toast.LENGTH_LONG);
    }


    private void toast(final String text, final int duration) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text, duration);
                    } else {
                        mToast.setText(text);
                        mToast.setDuration(duration);
                    }
                    mToast.show();
                }
            });
        }
    }


    protected void openActivity(Class<?> cls) {
        openActivity(this, cls);
    }

    public static void openActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    /**
     * 打开 Activity 的同时传递一个数据
     */
    protected <V extends Serializable> void openActivity(Class<?> cls, String key, V value) {
        openActivity(this, cls, key, value);
    }


    /**
     * 打开 Activity 的同时传递一个数据
     */
    public <V extends Serializable> void openActivity(Context context, Class<?> cls, String key, V value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }


}