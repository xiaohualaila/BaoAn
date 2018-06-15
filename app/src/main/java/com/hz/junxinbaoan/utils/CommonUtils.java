package com.hz.junxinbaoan.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.OutputFileParam;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 通用工具类
 * Created by zhaohh on 2016/5/3.
 */
public class CommonUtils {
    private static final String[] COMMON_FIELD = new String[]{"appid", "accesstime", "accesskey", "accesstoken","serialVersionUID",
            "action", "$change", "partner", "notify_url", "subject", "seller_id", "total_fee", "body", "private_key"};

    public static String getAccessKey(String accesstime, List<String> params) {

        String str = "";
        str += accesstime;
        str += getAppId();
        str += Constants.COMPANY_APPKEY_ANDROID;
        // str += Constants.APP_KEY;
        if (params != null && params.size() > 0) {
            Collections.sort(params, new Comparator<String>() {

                @Override
                public int compare(String str1, String str2) {

                    int result = str1.compareTo(str2);
                    return result;

                }

            });
            for (int i = 0; i < params.size(); i++) {
                // System.out.println("util+++++++++++++"+ params.get(i));
                // DSLog.d("util", params.get(i));
                str += params.get(i);
            }
        }
        Log.i("str", str);
        str = MD5(str);
        return str;
    }

    public static List<String> getParams(Class cls) {
        // DSLog.d("util", "getParams");
        List<String> params = new ArrayList<String>();
        for (; cls != Object.class; cls = cls.getSuperclass()) {

            Field[] fields = cls.getDeclaredFields();
            Field.setAccessible(fields, true);
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].getName();
                if (isCommonField(fieldName))
                    continue;
                // 得到属性的类名
                String className = fields[i].getType().getSimpleName();
                // 得到属性值
                if (className.equalsIgnoreCase("File"))
                    continue;

                params.add(fieldName);
            }
        }
        return params;
    }

    public static List<String> getParams(Class cls,Object object) {
        // DSLog.d("util", "getParams");
        List<String> params = new ArrayList<String>();
        for (; cls != Object.class; cls = cls.getSuperclass()) {

            Field[] fields = cls.getDeclaredFields();
            Field.setAccessible(fields, true);
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i].getName();
                if (isCommonField(fieldName))
                    continue;
                // 得到属性的类名
                String className = fields[i].getType().getSimpleName();
                // 得到属性值
                if (className.equalsIgnoreCase("File"))
                    continue;
                try {
                    Object val = fields[i].get(object);
                    if(val == null){
                        continue;
                    }
                    if(CommonUtils.isEmpty(val.toString())){
                        continue;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                params.add(fieldName);
            }
        }
        return params;
    }


    private static boolean isCommonField(String field) {

        boolean result = false;
        for (int i = 0; i < COMMON_FIELD.length; i++) {
            if (COMMON_FIELD[i].equals(field)) {
                result = true;
            }
        }
        return result;

    }

    /**
     * 手机号格式check 第一位须为1,长度须为11
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断字符串是否为空
     *
     * @param text 被判断的字符串
     * @return true:字符串为null或者为空字符串
     */
    public static boolean isEmpty(String text) {
        if (text == null || text.trim().equals("")) {
            return true;
        }
        return false;

    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit;
        try {
            bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            throw new IllegalArgumentException("Bank card code must be number!");
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * MD5加密(小写)
     *
     * @return 加密后String
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = s.getBytes();
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }

            // return new String(str).toUpperCase();
            return new String(str).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Post用map
     */
    public static Map<String, Object> getPostMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        List<Field> fields = getFields(object.getClass(), Object.class);
        for (Field temp : fields) {
            temp.setAccessible(true);
            try {
                if (temp.get(object) != null && !"serialVersionUID".equals(temp.getName())) {
                    map.put(temp.getName(), String.valueOf(temp.get(object)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static Map<String, String> getMapParams(Object object) {
        Map<String, String> map = new HashMap<>();
        List<Field> fields = getFields(object.getClass(), Object.class);
        for (Field temp : fields) {
            temp.setAccessible(true);
            try {
                if (temp.get(object) != null && !String.valueOf(temp.get(object)).equals("")) {
                    map.put(temp.getName(), String.valueOf(temp.get(object)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String createSign(Map<String, String> packageParams,String app_secert) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = sortMapByKey(packageParams).entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"app_secert".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        System.out.println("md5 sb:" + sb.toString());
        sb.append("app_secert=" + app_secert);
        System.out.println("md5 sb:" + sb.toString());
        String sign = Commonutil.MD5(sb.toString()).toUpperCase();
        System.out.println("packge签名:" + sign);
        return sign;
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }


    /**
     * 将实体类转换成请求参数,以map<k,v>形式返回
     *
     * @return
     */
   /* public Map<String, String> getMapParams(Object obj) {
        Class<? extends BaseParam> clazz = obj.getClass();
        Class<? extends Object> superclass = clazz.getSuperclass();

        Field[] fields = clazz.getDeclaredFields();
        Field[] superFields = superclass.getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return Collections.emptyMap();
        }

        Map<String, String> params = new HashMap<String, String>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (!TextUtils.isEmpty(String.valueOf(field.get(this)))){
                    params.put(field.getName(), String.valueOf(field.get(this)));
                }
            }

            for (Field superField : superFields) {
                superField.setAccessible(true);
                params.put(superField.getName(), String.valueOf(superField.get(this)));
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return params;
    }*/

    private static List<Field> getFields(Class<?> cls, Class<?> end) {
        ArrayList list = new ArrayList();
        if (!cls.equals(end)) {
            Field[] fields = cls.getDeclaredFields();
            Field[] var7 = fields;
            int var6 = fields.length;
            for (int var5 = 0; var5 < var6; ++var5) {
                Field superClass = var7[var5];
                list.add(superClass);
            }
            Class var8 = (Class) cls.getGenericSuperclass();
            list.addAll(getFields(var8, end));
        }
        return list;
    }

    /**
     * 加入网络插值器的方法
     * @param baseUrl
     * @param context
     * @param
     * @return
     */
    public static Retrofit buildRetrofit(String baseUrl, final Context context) {
        //Log
        MHttpLoggingInterceptor logging = new MHttpLoggingInterceptor();
        //设置Log显示的内容
        if (com.hz.junxinbaoan.common.Settings.DEBUG)
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        else
//            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //给okhtttpclient添加log

        //网络插值器
//        Interceptor connectInterceptor=new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                boolean connected = isNetworkAvailable(context);
//                if (connected) {
//                    return chain.proceed(chain.request());
//                } else {
//
//                    throw new NoNetworkException();
//                }
//            }
//        };

        int[] certificates = {R.raw.tomcat};
        String hosts[] = {Constants.BASE_URL};
        OkHttpClient okHttpClient = new OkHttpClient.Builder().socketFactory(getSSLSocketFactory(context, certificates)).addInterceptor(logging).build();
//        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(context, );

//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        //生成retrofit
        Retrofit retrofit = new Retrofit.Builder().client(getUnsafeOkHttpClient(logging))
//                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl).build();
        return retrofit;
    }

    public static String doTime(long messageTimestamp) {
        Long time_now=System.currentTimeMillis()/1000;
        messageTimestamp=messageTimestamp/1000;
        if (time_now-messageTimestamp<60){
            return "now";
        }
        if (time_now-messageTimestamp<3600){
            return (time_now-messageTimestamp)/60 +"min前";
        }
        if (time_now-messageTimestamp<86400){
            return (time_now-messageTimestamp)/3600 +"h前";
        }
        if (time_now-messageTimestamp<2592000){
            return (time_now-messageTimestamp)/86400 +"day前";
        }
        if (time_now-messageTimestamp<31536000){
            return (time_now-messageTimestamp)/2592000 +"month前";
        }
        else {
            return (time_now-messageTimestamp)/31536000 +"year前";
        }
    }
    public static String doTime2(long messageTimestamp) {
        Long time_now=System.currentTimeMillis()/1000;
        messageTimestamp=messageTimestamp/1000;
        if (time_now-messageTimestamp<60){
            return "刚刚";
        }
        if (time_now-messageTimestamp<3600){
            return (time_now-messageTimestamp)/60 +"分钟前";
        }
        if (time_now-messageTimestamp<86400){
            return (time_now-messageTimestamp)/3600 +"小时前";
        }
        if (time_now-messageTimestamp<2592000){
            return (time_now-messageTimestamp)/86400 +"天前";
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sdf.format(new Date(messageTimestamp*1000));
            return date;
        }
    }

    public static String getPinYinHeadChar(String name) {
        String DefaultLetter = "#";
        if (TextUtils.isEmpty(name)) {
            return DefaultLetter;
        }
        char char0 = name.toUpperCase().charAt(0);
        if (Character.isDigit(char0)) {
            return DefaultLetter;
        }
        ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
        if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
            HanziToPinyin.Token token = l.get(0);
            // toLowerCase()返回小写， toUpperCase()返回大写
            String letter = token.target.substring(0, 1).toUpperCase();
            char c = letter.charAt(0);
            // 这里的 'a' 和 'z' 要和letter的大小写保持一直。
            if (c < 'A' || c > 'Z') {
                return DefaultLetter;
            }
            return letter;
        }
        return DefaultLetter;
    }

    /**
     * 网络未连接是抛出的异常
     */
    public static final class NoNetworkException extends RuntimeException {
    }



//    /**
//     * 未加入网络插值器的方法
//     * @param baseUrl
//     * @param context
//     * @return
//     */
//    public static Retrofit buildRetrofit(String baseUrl, Context context) {
//        //Log
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        //设置Log显示的内容
//        if (Settings.DEBUG)
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        else
//            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        //给okhtttpclient添加log
//
//
//
//        int[] certificates = {R.raw.tomcat};
//        String hosts[] = {Constants.BASE_URL};
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().socketFactory(getSSLSocketFactory(context, certificates)).addInterceptor(logging).build();
////        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(context, );
//
////        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//
//        //生成retrofit
//        Retrofit retrofit = new Retrofit.Builder().client(getUnsafeOkHttpClient(logging)).addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl).build();
//        return retrofit;
//    }

    public static boolean isNetworkAvailable(final Context context) {

        return isNetworkReady(context)||isMobileConnected(context);
    }



    public final static boolean isNetworkReady(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null) {
            return ni.isConnected();
        }

        return false;
    }

    public final static boolean isMobileConnected(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if ((null != ni) && ni.isConnected()) {
            return ConnectivityManager.TYPE_MOBILE == ni.getType();
        }
        return false;
    }

    //
    /**
     * 取得文件大小
     *
     * @param file 文件
     * @return 文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                size += getFileSize(subFile);
            } else {
                size += subFile.length();
            }
        }
        return size;
    }

    /**
     * 清理缓存
     */
    public static final void cleanCache(Context context) {
        File cacheFolder = new File(com.hz.junxinbaoan.common.Settings.PIC_PATH);

        // 清理所有子文件
        for (File file : cacheFolder.listFiles()) {
            if (!file.isDirectory())
                file.delete();
        }
        cacheFolder = new File(com.hz.junxinbaoan.common.Settings.TEMP_PATH);

        // 清理所有子文件
        for (File file : cacheFolder.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS 文件大小
     * @return
     */
    public static String FormatFileSize(long fileS) {
        // DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }

        if (fileSizeString.equals(".00B")) {
            fileSizeString = "0" + fileSizeString;
        }
        return fileSizeString;
    }

    /**
     * 判断程序是否在后台
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                            return isInBackground;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    /**
     * 确认app是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
   /* public static boolean appIsInstall(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }*/

    /**
     * 跳转到别的app
     *
     * @param context
     * @param packageName
     */
    public static void toOtherApplication(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
        return htmlStr;

    }

    public static void main(String[] args) {
        String str = "<div style='text-align:center;'>&nbsp;整治“四风”&nbsp;&nbsp;&nbsp;清弊除垢<br/><span style='font-size:14px;'>&nbsp;</span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会。</span><br/></div>";
        System.out.println(getTextFromHtml(str));
    }

    //    public static String getFiledValue(String paramName, Pay pay){
//
//        Class payClass = (Class)pay.getClass();
//        try {
//            Field filed = payClass.getDeclaredField(paramName);
//            filed.setAccessible(true);
//            Object valueObj = filed.get(pay);
//            String value = (String)valueObj;
//            return value;
//        }catch (NoSuchFieldException no){
//            no.printStackTrace();
//            return null;
//        }catch (IllegalAccessException ill){
//            ill.printStackTrace();
//            return null;
//        }
//
//    }
    protected static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            KeyStore keyStore = KeyStore.getInstance("BKS");

            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                InputStream certificate = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));

                if (certificate != null) {
                    certificate.close();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (CertificateException e) {

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {

        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        };

        return TRUSTED_VERIFIER;
    }

    public static OkHttpClient getUnsafeOkHttpClient(HttpLoggingInterceptor logging, Interceptor connectInterceptor) {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).addInterceptor(logging).addNetworkInterceptor(connectInterceptor).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static OkHttpClient getUnsafeOkHttpClient(MHttpLoggingInterceptor logging) {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).addInterceptor(logging).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static OkHttpClient getUnsafeOkHttpClient(HttpLoggingInterceptor logging) {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).addInterceptor(logging).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static String getAppId(){
        String appid = "";
        appid += Constants.COMPANY_APPKEY_ANDROID+"_"+ MyApplication.getInstance().getVersion();
        return appid;
    }

    /**
     * 格式化时�?- String转换Date "yyyy-MM-dd HH:mm:ss"
     *
     * @param �?格式化的时间
     * @return 格式化后的时�?
     */
    public static Date getDateFormat(String date, String format) {
        if (isEmpty(date))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param date
     * @param string
     * @return
     */
    public static String getTimeFormat(Date date, String string) {
        SimpleDateFormat sdFormat;
        if (isEmpty(string)) {
            sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdFormat = new SimpleDateFormat(string);
        }
        try {
            return sdFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将秒数转化成时分秒格式
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
//            return "00:00";
            return "";//秒数小于0，不显示倒计时
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
//                timeStr = unitFormat(minute) + ":" + unitFormat(second);
                timeStr = unitFormat(minute) + "" + unitFormat(second);//方便截取
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
//                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
                timeStr = unitFormat(hour) + "" + unitFormat(minute) + "" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }



    public static String gsecToDay(long sec){
        if (sec<60*60*24){
            return "1";
        }else {
            long day = sec / (60 * 60 * 24);
            long yushu = sec % (60 * 60 * 24);
            if (yushu>0){
                return  ""+(day++);
            }else {
                return ""+day;
            }
        }
    }

    /**
     * 将秒数转化成时分秒格式
     * @param time
     * @return
     */
    public static String secToTime(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time == 0)
//            return "00:00";
            return "";//秒数小于0，不显示倒计时
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
//                timeStr = unitFormat(minute) + ":" + unitFormat(second);
                timeStr = "00"+unitFormat(minute) + "" + unitFormat(second);//方便截取
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "995959";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
//                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
                timeStr = unitFormat(hour) + "" + unitFormat(minute) + "" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(long i) {
        String retStr = null;
        if (i >=0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
    //取的SHA1
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    //下载文件
    private interface GetRetrofitService {
        @FormUrlEncoded
        @POST(Constants.OUTPUT)
        Call<ResponseBody> downloadFile(@FieldMap Map<String, Object> map);
    }
    public interface GetPic{
        void getPic(File pic);
    }
    public interface LoadPic{
        void loadPic(byte[] bytes);
    }
    /*   type: 0:jpg 1:amr 2:mp3  3:html   */
    public static void getPic(Context context, final String path, final GetPic getPic, final int type){
        GetRetrofitService getRetrofitService = CommonUtils.buildRetrofit(Constants.BASE_URL,context).create(GetRetrofitService.class);
        OutputFileParam param = new OutputFileParam();
        param.setPath(path);
        Call<ResponseBody> call = getRetrofitService.downloadFile(CommonUtils.getPostMap(param));
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file;
                    if (type == 1){
                        file = new File(com.hz.junxinbaoan.common.Settings.VIDEO_PATH, new Date().getTime()+".amr");
                    }else if (type == 2){
                        file = new File(com.hz.junxinbaoan.common.Settings.PIC_PATH, new Date().getTime()+".mp3");
                    }else if (type == 3){
                        file = new File(com.hz.junxinbaoan.common.Settings.TEMP_PATH, new Date().getTime()+".html");
                    }else {
                        file = new File(com.hz.junxinbaoan.common.Settings.PIC_PATH, new Date().getTime()+".jpg");
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    bis.close();
                    is.close();
                    getPic.getPic(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static void loadPic(final Context context, final String path, final LoadPic loadPic){
        GetRetrofitService getRetrofitService = CommonUtils.buildRetrofit(Constants.BASE_URL,context).create(GetRetrofitService.class);
        OutputFileParam param = new OutputFileParam();
        param.setPath(path);
        Call<ResponseBody> call = getRetrofitService.downloadFile(CommonUtils.getPostMap(param));
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap btp = BitmapFactory.decodeStream(bis);
//                    Drawable bd= new BitmapDrawable(context.getResources(), btp);
//                    loadPic.loadPic(bd);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    btp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytes=baos.toByteArray();
                    loadPic.loadPic(bytes);
                    baos.close();
                    bis.close();
                    is.close();
                } catch (Exception ignore) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static String numToString(int num){
        String s = "";
        switch (num){
            case 1:
                s = "一";
                break;
            case 2:
                s = "二";
                break;
            case 3:
                s = "三";
                break;
            case 4:
                s = "四";
                break;
            case 5:
                s = "五";
                break;
            case 6:
                s = "六";
                break;
            case 7:
                s = "七";
                break;
            case 8:
                s = "八";
                break;
            case 9:
                s = "九";
                break;
            case 10:
                s = "十";
                break;
            case 11:
                s = "十一";
                break;
            case 12:
                s = "十二";
                break;
        }

        return s;
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager  设置RecyclerView对应的manager
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }

    public static String changePhoneString(String s){
        String t = "";
        for (int i = 0; i <s.length() ; i++) {
            t=t+s.charAt(i)+"";
            if (i==2 || i==6){
                t=t+" ";
            }
        }
        return t;
    }

    public static String getPushId(){
        return JPushInterface.getRegistrationID(MyApplication.getInstance());
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenGPS(final Context context) {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }
    /**
     * 强制帮用户打开GPS
     * @param context
     */
    public static final void openGPS(final Context context) {
        //没有打开则弹出对话框
        new AlertDialog.Builder(context)
                .setTitle("您还未打开GPS")
                .setMessage("是否打开GPS")
                // 拒绝, 退出应用
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((BaseActivity)context).finish();
                                dialog.dismiss();
                            }
                        })

                .setPositiveButton("去设置",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //跳转GPS设置界面
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(intent);
                            }
                        })

                .setCancelable(false)
                .show();

    }

}