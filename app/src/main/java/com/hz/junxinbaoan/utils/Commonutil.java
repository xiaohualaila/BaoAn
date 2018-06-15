package com.hz.junxinbaoan.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.hz.junxinbaoan.common.Settings;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zhangxl on 2017/3/27.
 */
public class Commonutil {

    /**
     * 清理临时文件
     */
    public static final void deleteTempFile() {
        File tempFolder = new File(Settings.TEMP_PATH);
        if (tempFolder.exists()) {
            File[] tempFiles = tempFolder.listFiles();
            for (File tempFile : tempFiles) {
                tempFile.delete();
            }
        }
    }

    /**
     * 取得图片缓存大小
     */
    public static final long getCacheSize() {
        File cacheFolder = new File(Settings.PIC_PATH);
        // 文件夹是否存在
        if (cacheFolder.exists()) {
            return getFileSize(cacheFolder);
        } else {
            return 0;
        }
    }

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


    public static long getFileSizes(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
        }
        return s;
    }

    /**
     * 清理缓存
     */
    public static final void cleanCache(Context context) {
        File cacheFolder = new File(Settings.PIC_PATH);

        // 清理所有子文件
        for (File file : cacheFolder.listFiles()) {
            if (!file.isDirectory())
                file.delete();
        }
        cacheFolder = new File(Settings.TEMP_PATH);

        // 清理所有子文件
        for (File file : cacheFolder.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
        Toast.makeText(context, "缓存清理完成", Toast.LENGTH_SHORT).show();
    }

    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }

            int result = ExifInterface.ORIENTATION_UNDEFINED;
            try {
                ExifInterface exifInterface = new ExifInterface(dst.getPath());
                result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int rotate = 0;
            switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    break;
            }

            Bitmap bitmap = BitmapFactory.decodeFile(dst.getPath(), opts);
            if (rotate > 0) {
                bitmap = BitmapUtil.rotateBitmap(bitmap, rotate);
            }

            return bitmap;
        }
        return null;
    }

    /**
     * 保存位图至图片文件
     */
    public static final boolean saveBitmapToFile(Bitmap bitmap, File file) {
        BufferedOutputStream bos = null;
        try {
            File tempPicFile = new File(Settings.TEMP_PATH, FileUtils.getFileNameByPath(file.getPath()) + Settings.PICTURE_TEMP_EXTENSION);
            tempPicFile.delete();
            file.delete();

            tempPicFile.getParentFile().mkdirs();
            tempPicFile.createNewFile();

            bos = new BufferedOutputStream(new FileOutputStream(tempPicFile));
            bitmap.compress(CompressFormat.JPEG, 80, bos);

            bos.flush();
            bos.close();
            bos = null;

            return tempPicFile.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bos = null;
            }
        }
        return false;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * MD5加密
     * 需要加密的String
     * @param
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

            return new String(str).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    // 检查网络状态
    public static boolean checkNetworkState(Context context) throws SocketException {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);

        State mobileState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        State wifiState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        // 未连接网络
        if (!mobileState.equals(State.CONNECTED) && !mobileState.equals(State.CONNECTING) && !wifiState.equals(State.CONNECTED) && !wifiState
                .equals(State.CONNECTING)) {
            return false;
        }

        return true;
    }

    public static final String getTimeDiff2(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = dateFormat.parse(strDate);
            return getTimeDiff(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiff1(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(strDate);
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiff3(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(strDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currentDate = Calendar.getInstance();// 获取当前时间
            return format.format(date).replace(currentDate.get(Calendar.YEAR) + "-", "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiff5(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(strDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM月dd日 HH:mm");
            Calendar currentDate = Calendar.getInstance();// 获取当前时间
            return format.format(date).replace(currentDate.get(Calendar.YEAR) + "-", "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiff6(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(strDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currentDate = Calendar.getInstance();// 获取当前时间
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiffByFormat(String strDate, String formatS) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(strDate);
            SimpleDateFormat format = new SimpleDateFormat(formatS);
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getTimeDiff(Date date) {
        Calendar currentDate = Calendar.getInstance();// 获取当前时间
        String year = currentDate.get(Calendar.YEAR) + "";// 获取当前年份
        long diff = currentDate.getTimeInMillis() - date.getTime();
        if (diff < 0)
            return 0 + "秒钟前";
        else if (diff < 60000)
            return diff / 1000 + "秒钟前";
        else if (diff < 3600000)
            return diff / 60000 + "分钟前";
        else if (diff < 86400000)
            return diff / 3600000 + "小时前";
        else {
            String newdate = DateFormat.format("yyyy-MM-dd kk:mm", date).toString();
            if (newdate.contains(year)) {
                return newdate.substring(5);
            } else {
                return newdate;

            }
        }
    }

    /**
     * 计算两个时间的时间差
     *
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @return 间隔时间（秒）
     */
    public static long getTimeDiff3(String starttime, String endtime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sdate, edate;
        long diff = 0;
        try {
            if (starttime != null && endtime != null) {
                sdate = dateFormat.parse(starttime);
                edate = dateFormat.parse(endtime);
                diff = edate.getTime() - sdate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Math.abs(diff) / 1000;
    }

    public static final boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        State mobileState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        State wifiState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (!mobileState.equals(State.CONNECTED) && !mobileState.equals(State.CONNECTING) && !wifiState.equals(State.CONNECTED) && !wifiState
                .equals(State.CONNECTING)) {
            return false;
        }
        return true;
    }

    public static String getDisplayName(String username, String loginname) {

        if (username != null) {
            return username;
        } else {
            return loginname;
        }
    }

    /**
     * 判断来的是哪一天
     *
     * @param pushtime 输入时间
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getday(String pushtime) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date oldTime = null;
        Date today;
        try {
            oldTime = dateFormat.parse(pushtime);
            // 将下面的 理解成 yyyy-MM-dd 00：00：00 更好理解点
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = format.format(now);
            today = format.parse(todayStr);
            long a = today.getTime() - oldTime.getTime();
            if (a > 0 && a <= 86400000) {
                // 昨天 86400000=24*60*60*1000 一天
                return "昨天";
            } else if (a <= 0) {
                // 至少是今天
                return "今天";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 至少是前天
        return dateFormat.format(oldTime).substring(5, 11).replace("-", "月");

    }

    // 星座算法

    /**
     * @param month 月份
     * @param day   天
     * @return
     */
    public static final String GetConstellation(int month, int day) {

        String constellation = "";

        if ((month == 3 && day > 20) || (month == 4 && day < 20)) {
            constellation = "白羊座";
        }
        if ((month == 4 && day > 19) || (month == 5 && day < 21)) {
            constellation = "金牛座";
        }
        if ((month == 5 && day > 20) || (month == 6 && day < 20)) {
            constellation = "双子座";
        }
        if ((month == 6 && day > 21) || (month == 7 && day < 23)) {
            constellation = "巨蟹座";
        }
        if ((month == 7 && day > 22) || (month == 8 && day < 23)) {
            constellation = "狮子座";
        }
        if ((month == 8 && day > 22) || (month == 9 && day < 23)) {
            constellation = "处女座";
        }
        if ((month == 9 && day > 20) || (month == 10 && day < 23)) {
            constellation = "天秤座";
        }
        if ((month == 10 && day > 22) || (month == 11 && day < 22)) {
            constellation = "天蝎座";
        }
        if ((month == 11 && day > 21) || (month == 12 && day < 22)) {
            constellation = "射手座";
        }
        if ((month == 12 && day > 21) || (month == 1 && day < 20)) {
            constellation = "摩羯座";
        }
        if ((month == 1 && day > 19) || (month == 2 && day < 19)) {
            constellation = "水瓶座";
        }
        if ((month == 2 && day > 18) || (month == 3 && day < 21)) {
            constellation = "双鱼座";
        }
        return constellation;
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
     * 文件转化为字节数组 有问题 慎重使用！！！！
     *
     * @EditTime 2007-8-13 上午11:45:28
     */
    // public static byte[] getBytesFromFile(File f) {
    // if (f == null) {
    // return null;
    // }
    // try {
    // FileInputStream stream = new FileInputStream(f);
    // ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
    // byte[] b = new byte[1000];
    // int n;
    // while ((n = stream.read(b)) != -1) {
    // out.write(b, 0, n);
    // }
    // stream.close();
    // byte[] b1 = out.toByteArray();
    // out.close();
    // return b1;
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return null;
    // }
    public static byte[] convertFileToBytes(File f) {
        if (f == null) {
            return null;
        }
        byte[] bytes = null;
        if (f != null) {
            try {
                InputStream is = new FileInputStream(f);
                int length = (int) f.length();
                if (length > Integer.MAX_VALUE) // 当文件的长度超过了int的最大值
                {
                    return null;
                }
                bytes = new byte[length];
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }
                // 如果得到的字节长度和file实际的长度不一致就可能出错了
                if (offset < bytes.length) {
                    return null;
                }
                is.close();
            } catch (Exception e) {
            }

        }
        return bytes;

    }

    public static int getViewHeightByWidth(Bitmap bitmap, int width) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scaleWidth = ((float) width / w);
        int viewHeight = (int) (h * scaleWidth);
        return viewHeight;
    }

    public void switchFilterIntent(Intent intent) {
        if (intent != null && intent.getStringExtra("data") != null) {
            String filter = intent.getStringExtra("data");
            if (filter.equals("com://photodetail?id=10086")) {
                // 照片详情

            } else if (filter.equals("com://photodetail?id=10086")) {

            }

        }
    }

    /**
     * StrToStr时间格式转化
     *
     * @param time 被转化的时间字符串
     * @return
     */
    public static String getTimeStrToStr(String time, int preFormateType, int formateType) {
        SimpleDateFormat preFormat = getFormateType(preFormateType);
        SimpleDateFormat format = getFormateType(formateType);
        String result = "";
        try {
            result = format.format(preFormat.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取日期转化类型
     *
     * @param formateType 1：yyyy-MM-dd HH:mm:ss；2：yyyy-MM-dd；3：HH:mm:ss；4：HH:mm；5：yyyy.MM.dd；6：MM.dd；7：yyyy.MM.dd HH:mm
     * @return
     */
    public static SimpleDateFormat getFormateType(int formateType) {
        switch (formateType) {
            case 1:
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            case 2:
                return new SimpleDateFormat("yyyy-MM-dd");
            case 3:
                return new SimpleDateFormat("HH:mm:ss");
            case 4:
                return new SimpleDateFormat("HH:mm");
            case 5:
                return new SimpleDateFormat("yyyy.MM.dd");
            case 6:
                return new SimpleDateFormat("MM.dd");
            case 7:
                return new SimpleDateFormat("yyyy.MM.dd HH:mm");
            case 8:
                return new SimpleDateFormat("yyyy-MM-dd HH:mm");
            case 9:
                return new SimpleDateFormat("MM-dd HH:mm");
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 地址转换
     * @param context
     * @param uri
     * @return
     */
    public static String getPhotoPathFromContentUri(Context context, Uri uri) {
        String photoPath = "";
        if(context == null || uri == null) {
            return photoPath;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if(isExternalStorageDocument(uri)) {
                String [] split = docId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    if("primary".equalsIgnoreCase(type)) {
                        photoPath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            }
            else if(isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                photoPath = getDataColumn(context, contentUri, null, null);
            }
            else if(isMediaDocument(uri)) {
                String[] split = docId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    Uri contentUris = null;
                    if("image".equals(type)) {
                        contentUris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if("video".equals(type)) {
                        contentUris = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }
                    else if("audio".equals(type)) {
                        contentUris = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[] { split[1] };
                    photoPath = getDataColumn(context, contentUris, selection, selectionArgs);
                }
            }
        }
        else if("file".equalsIgnoreCase(uri.getScheme())) {
            photoPath = uri.getPath();
        }
        else {
            photoPath = getDataColumn(context, uri, null, null);
        }

        return photoPath;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }
    /**
     * 手机号格式check 第一位须为1,长度须为11
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}
