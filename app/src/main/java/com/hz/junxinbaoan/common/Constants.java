package com.hz.junxinbaoan.common;

import android.util.Log;

/**
 * 常量
 * Created by jinl on 2016/10/8.
 */
public class Constants {
    public static final String MAIN_BOTTOM = "main_bottom";
    public static class CashActivity {
        //要展示的Fragment
        public static final String CLAZZ = "clazz";
        //传递给Fragment的数据
        public static final String BUNDLE = "bundle";
    }

    // ---保存本地信息的文件名---//


    // 安卓请求接口的appid和appkey
    public static String COMPANY_APPID_ANDROID = "zhihuiwuliu_driver_android";
    public static String COMPANY_APPKEY_ANDROID = "8ada49ce04c94e92bbf17f7366e01684";

    // ---自动登录有关本地key---//
    public static final String PREF_KEY_AUTO_LOGIN = "PREF_KEY_AUTO_LOGIN";// true：可自动登录
    public static final String PREF_IS_LOGIN = "PREF_IS_LOGIN";// true:当前为有用户登录状态

    public static final String PREF_FILE_NAME = "BAOANCOMPANY_FILE";

    public static final String APP_NAME = "保安端";


    /**
     * Intent
     */


    //推送广播
    public static final String GET_PUSH_ACTION = "com.ds.baoan.company.push";
    /**
     * Intent
     */
//    http://218.108.31.2:8882


//    public static final String BASE_URL = "http://218.108.31.2:8884";//ali测试
//    public static final String BASE_URL = "http://192.168.0.25:8884";//本地地址
    public static final String BASE_URL = "http://weixin.baoan.syn-trust.net/";//上架阿里云

    public static final String REGISTER = "/user/register";//注册
    public static final String RESET = "/user/reset";//忘记密码
    public static final String LOGIN = "/oauth/token";
    public static final String MESSAGELIST = "/message/list";
    public static final String STUDYLIST = "/learn/list";
    public static final String REQUESTLIST = "/request/list";
    public static final String GETUSERINFO = "/user/info";
    public static final String FREQUENCY = "/frequency";

    public static final String GetVcode = "/user/code";//获取验证码
    public static final String UploadFile = "/upload";//上传文件
    public static final String OUTPUT = "/output";//输出文件
    public static final String CHANGEUSERINFO = "/user/modify";//修改个人信息
    public static final String TYPELIST = "/request/types";//请假类型
    public static final String MAINPAGEDATA = "/overview";//首页数据
    public static final String VCODE = "common/info";
    public static final String EMPLOYEELIST = "/employee/list";
    public static final String ADDREASON = "/request/add";

    public static final String HELP_URL = "http://help.syn-trust.net:8080/保安通/保安端app";
     /**
     * 常量
     */
    public static final String AREANAME = "AREANAME";
    public static final String PAYSWSTATE = "paypswstate";
    public static final String QQSTATE = "QQSTATE";
    public static final String SINASTATE = "SINASTATE";
    public static final String WXSTATE = "WXSTATE";
    public static final String REVIEWSTATE = "REVIEWSTATE";
    public static int FONT = 2;


    public static final String PREF_FROM_WEIXIN_BIND = "PREF_FROM_WEIXIN_BIND";
    public static final String FROM_WEIXIN_BIND_RESPOND = "FROM_WEIXIN_BIND_RESPOND";
    public static final String SAILID = "SAILID";
    public static final String TAG = "slide";
    public static final boolean DEBUG = Log.isLoggable(TAG, Log.VERBOSE);


    public static final String PUSHLOCATION = "/location/add";//新增位置信息
    public static final String MESSAGEADD = "/report/add";//提交求助爆料
    public static final String REPORTLIST = "/report/list";//求助爆料列表
    public static final String MESSAGEDETAIL = "/report/detail";//求助爆料详情
    public static final String HELPLIST = "/help/list";//获取所有帮助信息
    public static final String HELP_DETAIL = "/help/detail";//获取帮助详情
    public static final String SUGGESTADD = "/suggest/add";//提交意见反馈
    public static final String SCHEDULE = "/schedule/mySchedule";//排班列表
    public static final String CERTIFICATE = "/user/certificate";//电子保安员证
    public static final String QRCODE = "/user/qrcode";//电子保安员证二维码

    public static final String ATTENDACNE_STATISTICS = "/attendance/statistics";//考勤统计
    public static final String SIGN = "/attendance/add";//签到
    public static final String FINDIBEACON = "/ibeacon/findByMajor";//获取蓝牙

    public static final String MESSAGEREAD = "/message/detail";//消息详情
    public static final String LEARNREAD = "/learn/detail";//学习详情

    //1.2
    public static final String ATTENDACNE_DETAIL = "/attendance/detail";//获取指定日期考勤
    public static final String SINGLEVIEW = "/empSch/singleView";//当月的排班列表
    public static final String ROLE_PERMISSION = "/user/userrole"; //获取角色权限

    //------------------------------

    public static final String ROAD = "/location/list";//轨迹  需测试

    //-------------------------------

    /**
     * QQ-登录、分享
     */
    public static final String QQ_APP_ID = "1106150719";
    // 注意修改 配置文件里android:name="com.tencent.tauth.AuthActivity"
    // <data android:scheme="tencent1104625006" />
    public static final String QQ_APP_KEY = "WZAbYDc3gc7O9oe5";
    /**
     * 微信-分享
     */
    public static final String WEIXIN_APP_ID = "wxbcf6e7ee9f533c17";
    public static final String WEIXIN_APP_SECRET = "99cae2d011eb9ebc9ff3011469528b28";
    public static final String WEIXIN_PAY_URL = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
    public static final String WEIXIN_REQ_SCOPE = "snsapi_userinfo";
    public static final String WEIXIN_REQ_STATE = "wechat_zhebao";
    /**
     * 微博分享
     */
    public static final String WEIBO_APP_KEY = "";

    public static String[] atoz={"管","A","B","C","D","E","F","G","H","I","J"
            ,"K","L","M","N","O","P","Q","R","S","T"
            ,"U","V","W","X","Y","Z"
    };

}
