package com.hz.junxinbaoan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.os.Looper;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.bumptech.glide.request.target.ViewTarget;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.common.Settings;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.DensityUtils;
import com.hz.junxinbaoan.utils.FileUtils;
import com.hz.junxinbaoan.utils.GlideImageLoader;
import com.hz.junxinbaoan.utils.UserInfo;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.jpush.android.api.JPushInterface;

//import android.support.multidex.MultiDex;
//import android.support.multidex.MultiDex;


public class MyApplication extends MultiDexApplication {
    private static MyApplication mApplication;
    /**
     * 存放activity的集合
     */
    public static List<Activity> mActivityList;
    public static UserInfo mUserInfo;
    public static int time = 30;
    public static String code;
    //    public LocationService locationService;
    public Vibrator mVibrator;
    private static String version;
//    public static BriteDatabase mDB;

//    public static IWXAPI wxapi;
//    public static Tencent mTencent;
//    private IUiListener loginListener; //授权登录监听器
//    private IUiListener userInfoListener; //获取用户信息监听器
//    private String scope; //获取信息的范围参数
//    public static UploadManager uploadManager;
//
//    public static AuthInfo mAuthInfo;
//    public static IWeiboShareAPI weiboAPI;

    public static double latitude = 0;//定位经度
    public static double longitude = 0;//定位纬度

    public static int latetime = 0;//  往后推迟的小时数  单位为半个小时    例如:1表示30分钟  2表示一个小时

    public static int visitMainCount = 0; // 访问MainActivity次数

//    public static List<byte[]> result = new ArrayList<>();//图片

    public static String errorMessage = "";//错误信息

    /**
     * 程序启动时的处理
     *
     * @see Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e( "TAG","-----应用重启" );
//        LeakCanary.install(this);
//        CrashHandler.getInstance().init(this);
        // 获取当前包名
        String packageName = getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程 只在主进程下上报数据
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(getApplicationContext(), "6fa6d7d616", true,strategy);
//        CrashReport.initCrashReport(getApplicationContext(), "6fa6d7d616", false);

        ViewTarget.setTagId(R.id.tag_glide);
        mApplication = this;
        mActivityList = new ArrayList<Activity>();
        creatAliyun();

        //第三方登录等
//       initQq();
//        initWeiBo();
//        initUpload();
        regToWx();
        initJPush();

        initData();

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext

        // 出现应用级异常时的处理
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            String errMsg = "";

            @Override
            public void uncaughtException(Thread thread, final Throwable throwable) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                errMsg = sw.toString();
                writeLogToFile(errMsg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        MobclickAgent.reportError(getApplicationContext(),throwable);
                        Looper.prepare();
                        if (mActivityList.size() > 0) {
                            new AlertDialog.Builder(getCurrentActivity()).setTitle(R.string.app_name).setMessage(Log
                                    .getStackTraceString(throwable))
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 强制退出程序
                                            finish();
                                        }
                                    }).setCancelable(false).show();

                        } else {
                            if (Settings.DEBUG) {
                                Log.e(Settings.TAG, errMsg);
                            }
                            finish();
                        }
                        Looper.loop();
                    }
                }).start();
                if (Settings.DEBUG) {
                    // 错误LOG
                    Log.e(Settings.TAG, throwable.getMessage(), throwable);
                }
            }
        });
        regToWx();
//        locationService = new LocationService(getApplicationContext());
//        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        SDKInitializer.initialize(getApplicationContext());
//        AMapNavi.setApiKey(this, "0cb18c9e906033dcb48c4f6d69ce1e1e");

    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    //创建阿里云
    private void creatAliyun() {
        MANService manService = MANServiceProvider.getService();
        // 打开调试日志，线上版本建议关闭
        manService.getMANAnalytics().turnOnDebug();
//        manService.getMANAnalytics().setChannel("某渠道");
        // MAN初始化方法之一，从AndroidManifest.xml中获取appKey和appSecret初始化
        manService.getMANAnalytics().init(this, getApplicationContext());
        // 若需要关闭 SDK 的自动异常捕获功能可进行如下操作,详见文档5.4
//        manService.getMANAnalytics().turnOffCrashReporter();
        // 通过此接口关闭页面自动打点功能，详见文档4.2
        manService.getMANAnalytics().turnOffAutoPageTrack();
        // 若AndroidManifest.xml 中的 android:versionName 不能满足需求，可在此指定
        // 若在上述两个地方均没有设置appversion，上报的字段默认为null
        manService.getMANAnalytics().setAppVersion("3.1.1");
    }


    private void initJPush() {

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
//        JPushInterface.resumePush(getApplicationContext());
//        JPushInterface.getRegistrationID(MyApplication.getInstance());

    }


//    private void initUpload() {
//        Configuration config = new Configuration.Builder()
//                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
//                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
//                .connectTimeout(10) // 链接超时。默认10秒
//                .responseTimeout(60) // 服务器响应超时。默认60秒
////                .recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
////                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
//                .build();
//// 重用uploadManager。一般地，只需要创建一个uploadManager对象
//        uploadManager = new UploadManager(config);
//    }

//    private void initWeiBo() {
//        mAuthInfo = new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
//
//        weiboAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
//
//
//    }

    //    private void initQq() {
//        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
//        // 其中APP_ID是分配给第三方应用的appid，类型为String。
//        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
//        // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
//        scope = "all";
//
//        loginListener = new IUiListener() {
//
//            /**
//             * 返回json数据样例
//             *
//             * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
//             * "pf":"desktop_m_qq-10000144-android-2002-",
//             * "query_authority_cost":448,
//             * "authority_cost":-136792089,
//             * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
//             * "expires_in":7776000,
//             * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
//             * "msg":"",
//             * "access_token":"A2455F491478233529D0106D2CE6EB45",
//             * "login_cost":499}
//             */
//
//            @Override
//            public void onComplete(Object o) {
//                if (o == null) {
//                    return;
//                }
//                JSONObject jo = (JSONObject) o;
//
//                int ret = 0;
//                try {
//                    ret = jo.getInt("ret");
//                    if (ret == 0) {
//                        String openID = jo.getString("openid");
//                        String accessToken = jo.getString("access_token");
//                        String expires = jo.getString("expires_in");
//                        mTencent.setOpenId(openID);
//                        mTencent.setAccessToken(accessToken, expires);
//                        Log.d("lwj","json=" + String.valueOf(jo));
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//
//
//
//
//
//            @Override
//            public void onError(UiError uiError) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        };
//
//        userInfoListener = new IUiListener() {
//            /**
//             * 返回用户信息样例
//             *
//             * {"is_yellow_year_vip":"0","ret":0,
//             * "figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/40",
//             * "figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
//             * "nickname":"攀爬←蜗牛","yellow_vip_level":"0","is_lost":0,"msg":"",
//             * "city":"黄冈","
//             * figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/50",
//             * "vip":"0","level":"0",
//             * "figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
//             * "province":"湖北",
//             * "is_yellow_vip":"0","gender":"男",
//             * "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/30"}
//             */
//            @Override
//            public void onComplete(Object o) {
//                if(o == null){
//                    return;
//                }
//                try {
//                    JSONObject jo = (JSONObject) o;
//                    int ret = jo.getInt("ret");
//                    System.out.println("json=" + String.valueOf(jo));
//                    String nickName = jo.getString("nickname");
//                    String gender = jo.getString("gender");
//
//
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//            }
//
//            @Override
//            public void onError(UiError uiError) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        };
//
//
//    }
//
//
    private void regToWx() {
//        wxapi = WXAPIFactory.createWXAPI(this,Constants.WEIXIN_APP_ID,true);
//        wxapi.registerApp(Constants.WEIXIN_APP_ID);
    }


    /**
     * 打印错误日志到文件
     *
     * @param errMsg
     */
    private void writeLogToFile(String errMsg) {
        File file = new File(Settings.TEMP_PATH, "log.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.write("\n" + errMsg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 本身实例
     */
    public static MyApplication getInstance() {
        return mApplication;
    }

    /**
     * 初始化信息
     */
    private void initData() {
        // 获得屏幕高度（像素）
        Settings.DISPLAY_HEIGHT = getResources().getDisplayMetrics().heightPixels;
        // 获得屏幕宽度（像素）
        Settings.DISPLAY_WIDTH = getResources().getDisplayMetrics().widthPixels;
        // 获得系统状态栏高度（像素）
        Settings.STATUS_BAR_HEIGHT = DensityUtils.getStatusBarHeight(mApplication);
        // 文件路径设置
        String parentPath = null;
        // SD卡正常挂载（可读写）
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            parentPath = Environment.getExternalStorageDirectory().getPath() + File.separator + getPackageName();
        } else {
            parentPath = Environment.getDataDirectory().getPath() + "/data/" + getPackageName();
        }
        Log.e( "MyApplication",parentPath );
        // 临时文件路径设置
        Settings.TEMP_PATH = parentPath + "/tmp";
        // 录音文件路径设置
        Settings.VIDEO_PATH = parentPath + "/video";
        // 图片缓存路径设置
        Settings.PIC_PATH = parentPath + "/pic";
        // 更新APK路径设置
        Settings.APK_PATH = parentPath + "/apk";
        // 创建各目录
        new File(Settings.TEMP_PATH).mkdirs();
        new File(Settings.VIDEO_PATH).mkdirs();
        new File(Settings.PIC_PATH).mkdirs();
        new File(Settings.APK_PATH).mkdirs();
        version = CommonUtils.getVersionName(this);
        initUserInfo();
        initGallery();
        initDB();

    }

    private void initGallery() {
        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#639ddb"))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setCheckSelectedColor(Color.parseColor("#639ddb"))
                .setFabNornalColor(Color.parseColor("#639ddb"))
                .setFabPressedColor(Color.parseColor("#639ddb"))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(false)
                .setEnableCamera(true)
                .setEnablePreview(true)
                .build();
        //配置imageloader
        GlideImageLoader imageLoader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setEditPhotoCacheFolder(new File(Settings.TEMP_PATH))
                .setTakePhotoFolder(new File(Settings.PIC_PATH))
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * 初始化数据库
     */
    private void initDB() {
//        if (mDB == null) {
//            SQLOpenHelper helper = new SQLOpenHelper(getApplicationContext());
//            SqlBrite brite = SqlBrite.create();
//            mDB = brite.wrapDatabaseHelper(helper, Schedulers.io());
//        }
    }


    /**
     * 清空Activity列表
     */
    public static void clearActivityList() {
        for (int i = 0; i < mActivityList.size(); i++) {
            Activity activity = mActivityList.get(i);
            activity.finish();
        }
        mActivityList.clear();
    }

    public static void clearActivityListOnlyOne() {
        for (int i = 0; i < mActivityList.size() - 1; i++) {
            Activity activity = mActivityList.get(i);
            activity.finish();
        }
    }

    /**
     * 获得当前最顶层的activity
     *
     * @return 当前最顶层的activity
     */
    public Activity getCurrentActivity() {
        if (mActivityList != null && mActivityList.size() >= 1) {
            return mActivityList.get(mActivityList.size() - 1);
        }
        return null;
    }

    public Activity getBelowCurrentActivity() {
        if (mActivityList.size() > 1) {
            return mActivityList.get(mActivityList.size() - 2);
        }
        return null;
    }

    /**
     * 生成Activity存入列表
     *
     * @param activity
     */
    public void addCurrentActivity(Activity activity) {
        if (activity != null && mActivityList != null)
            mActivityList.add(activity);
    }

    /**
     * 从列表移除activity
     *
     * @param activity
     */
    public void removeCurrentActivity(Activity activity) {
        if (activity != null && mActivityList != null)
            mActivityList.remove(activity);
    }

    /**
     * 清除所有的activity
     */
    public void removeAllActivity() {
        for (int i = 0; i < mActivityList.size(); i++) {
            Activity activity = mActivityList.get(i);
            if (activity != null)
                activity.finish();
        }
        mActivityList.clear();
    }


    private void initUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo(getApplicationContext());
        }
    }

    public void finish() {
        removeAllActivity();
//        AccessTokenKeeper.clear(this);
        // 清理临时文件
        try {
            FileUtils.deleteFile(Settings.TEMP_PATH);
        } catch (Exception e) {
        }
//        MobclickAgent.onKillProcess(getApplicationContext());
        System.exit(0);
    }


    /**
     * 设置本地所有自动登录标记
     */
    public void setAutoLoginTag() {
        SharedPreferences.Editor et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        et.putBoolean(Constants.PREF_KEY_AUTO_LOGIN, true);
        et.putBoolean(Constants.PREF_IS_LOGIN, true);
        et.commit();
    }


    /**
     * 设置本地所有自动登录标记
     */
    public boolean IsAutoLoginTag() {
        SharedPreferences et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);

        return et.getBoolean(Constants.PREF_KEY_AUTO_LOGIN, false);

    }
//
//    /**
//     * 检查登录
//     */
//    public void checkIsLogining() {
//        boolean loginTag = MyApplication.getInstance().IsAutoLoginTag();
//        if (!loginTag){
//            Intent intent=new Intent(this,LoginActivity.class);
//            startActivity(intent);
//        }
//    }

    /***
     * 设置退出登录
     */

    public void setExitLoginTag() {
        SharedPreferences.Editor et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        et.putBoolean(Constants.PREF_KEY_AUTO_LOGIN, false);
        et.putBoolean(Constants.PREF_IS_LOGIN, false);
        et.commit();
    }

    /**
     * 清空本地所有自动登录标记
     */
    public void clearAutoLoginTag() {
        SharedPreferences.Editor et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        et.putBoolean(Constants.PREF_KEY_AUTO_LOGIN, false);
        et.commit();
    }

//    public void saveFastState(boolean position) {
//        SharedPreferences.Editor et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE)
// .edit();
//        et.putBoolean(Constants.FAST_STATE, position);
//        et.commit();
//    }
//
//    public boolean getFastState() {
//        SharedPreferences et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
//        return et.getBoolean(Constants.FAST_STATE, false);
//    }

//    public void setWXFlag(int flag) {
//        SharedPreferences wx = getSharedPreferences(Constants.WXFLAG, MODE_PRIVATE);
//        SharedPreferences.Editor edit = wx.edit();
//        edit.putInt(Constants.WXFLAG,flag);
//        edit.commit();
//    }
//
//    public int getWXFlag() {
//        SharedPreferences et = this.getSharedPreferences(Constants.WXFLAG, Context.MODE_PRIVATE);
//        return et.getInt(Constants.WXFLAG, 0);
//    }

//    public void setOrderid(int flag) {
//        SharedPreferences wx = getSharedPreferences(Constants.ORDERID, MODE_PRIVATE);
//        SharedPreferences.Editor edit = wx.edit();
//        edit.putInt(Constants.ORDERID,flag);
//        edit.commit();
//    }
//
//    public int getOrderid() {
//        SharedPreferences et = this.getSharedPreferences(Constants.ORDERID, Context.MODE_PRIVATE);
//        return et.getInt(Constants.ORDERID, 0);
//    }


    public void saveLocation(String position) {
        SharedPreferences.Editor et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        et.putString(Constants.AREANAME, position);
        et.commit();
    }

    public String getLocation() {
        SharedPreferences et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);

        return et.getString(Constants.AREANAME, "");

    }

//    public void savePay(String position) {
//        SharedPreferences.Editor et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE)
// .edit();
//        et.putString(Constants.PAYRESULT, position);
//        et.commit();
//    }
//
//    public String getPay() {
//        SharedPreferences et = this.getSharedPreferences(Constants.PREF_FILE_NAME, Context.MODE_PRIVATE);
//        return et.getString(Constants.PAYRESULT, "");
//
//    }

    public String getCity() {
        return this.getSharedPreferences("ADDRESS", Context.MODE_PRIVATE).getString("DetailAddress", "");
    }

    public String getVersion() {
        return version;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
