package com.hz.junxinbaoan.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理类
 *
 * @author llj
 */
public class BitmapUtil {
    /**
     * 1.回收bitmap
     *
     * @param bitmap
     */
    private static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 2.获得TransitionDrawable
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static final Drawable getAlphDrawable(Context context, Bitmap bitmap) {
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(0x00000000), new BitmapDrawable(context.getResources(), bitmap)});
        return td;
    }

    /**
     * 3.将bitmap转化为数组
     *
     * @param bmp
     * @return
     */
    public static final byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        recycleBitmap(bmp);
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 3.按宽度放大缩小图片
     *
     * @param bitmap
     * @param width
     * @return
     */
    public static final Bitmap scaleBitmap3(Bitmap bitmap, float width) {
        float sourceWidth = bitmap.getWidth();
        float sourceHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = (width / sourceWidth);
        matrix.postScale(scaleWidth, scaleWidth);
        // 当进行的不只是平移操作的时候，最后的参数为true，可以进行滤波处理，有助于改善新图像质量
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, (int) sourceWidth, (int) sourceHeight, matrix, true);
        recycleBitmap(bitmap);
        return newbmp;
    }

    /**
     * 4.按宽高创建缩放后的图片
     *
     * @param bitmap 源位图
     * @param width  缩放后位图的宽px
     * @param height 缩放后位图的高px
     * @return
     */
    public static final Bitmap scaleBitmap(Bitmap bitmap, float width, float height) {
        float sourceWidth = bitmap.getWidth();
        float sourceHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = (width / sourceWidth);
        float scaleHeight = (height / sourceHeight);
        matrix.postScale(scaleWidth, scaleHeight);
        // 当进行的不只是平移操作的时候，最后的参数为true，可以进行滤波处理，有助于改善新图像质量
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, (int) sourceWidth, (int) sourceHeight, matrix, true);
        recycleBitmap(bitmap);
        return newbmp;
    }

    /**
     * 5.按缩放比例创建缩放后的图片
     *
     * @param bitmap 源位图
     * @param wScale 宽缩放的比例f
     * @param hScale 高缩放的比例f
     * @return
     */
    public static final Bitmap scaleBitmap2(Bitmap bitmap, float wScale, float hScale) {
        Matrix matrix = new Matrix();
        matrix.postScale(wScale, hScale);
        // 当进行的不只是平移操作的时候，最后的参数为true，可以进行滤波处理，有助于改善新图像质量
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        recycleBitmap(bitmap);
        return newbmp;
    }

    /**
     * 6.创建旋转后的图片
     *
     * @param bitmap
     * @param degrees 旋转的角度
     * @return
     */
    public static final Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        int sourceWidth = bitmap.getWidth();
        int sourceHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        // 当进行的不只是平移操作的时候，最后的参数为true，可以进行滤波处理，有助于改善新图像质量
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, sourceWidth, sourceHeight, matrix, true);
        recycleBitmap(bitmap);
        return newbmp;
    }

    /**
     * 7.创建带圆角的bitmap图片
     *
     * @param bitmap 源位图
     * @param round  圆角的弧度
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap roundCorner(Bitmap bitmap, int round) {
        // 创建画笔
        Paint paint = new Paint();
        // 消除锯齿
        paint.setAntiAlias(true);
        // 创建和位图宽高一样的矩形对象
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 由rect对象创建一个rectF对象，该对象的精度为float
        RectF rectF = new RectF(rect);
        // 创建和原位图一样大小的位图
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // 根据创建的位图新建画布，上面这样创建的位图是可改变的
        Canvas canvas = new Canvas(output);
        // 将画布填充为无色
        canvas.drawARGB(0, 0, 0, 0);
        // 画一个透明的圆角矩形
        canvas.drawRoundRect(rectF, round, round, paint);
        // 设置和画笔画上去后显示画笔的颜色
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // 将源bitmap填充到上面圆角矩形的画布中，就呈现出一个新的圆角的bitmap
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 7.创建带圆角的bitmap图片
     *
     * @param bitmap 源位图
     * @param round  圆角的弧度
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap roundTopCorner(Bitmap bitmap, int round) {
        // 创建画笔
        Paint paint = new Paint();
        // 消除锯齿
        paint.setAntiAlias(true);
        // 创建和位图宽高一样的矩形对象
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 由rect对象创建一个rectF对象，该对象的精度为float
        RectF rectF = new RectF(rect);
        // 创建和原位图一样大小的位图
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // 根据创建的位图新建画布，上面这样创建的位图是可改变的
        Canvas canvas = new Canvas(output);
        // 将画布填充为无色
        canvas.drawARGB(0, 0, 0, 0);
        // 画一个透明的圆角矩形
        canvas.drawRoundRect(rectF, round, 0, paint);
        // 设置和画笔画上去后显示画笔的颜色
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // 将源bitmap填充到上面圆角矩形的画布中，就呈现出一个新的圆角的bitmap
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 8. 创建圆形图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            top = 0;
            left = clip;
            right = width - clip;
            bottom = height;

            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        final int color = 0xff424242;
        final Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);

        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        // 创建一个计算好后的长宽相等的bitmap
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        // 创建一个画布，将绘制到该bitmap上
        Canvas canvas = new Canvas(output);
        // 设置画布颜色，全透明
        canvas.drawARGB(0, 0, 0, 0);
        //
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        //
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        //
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 9.将drawable转化成bitmap
     *
     * @param drawable 源drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽,colorbitmap返回-1的
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        // 建立对应 大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立一个canvas，将会绘制到bitmap上
        Canvas canvas = new Canvas(bitmap);
        // 指定一个矩形区域，放在drawable得Rect中
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中，将会调用上面的方法
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 10.获取图片文件的宽高
     *
     * @param bitmapFile 图片文件
     * @return 宽、高组成的数组
     */
    public static int[] getBitmapFileSize(File bitmapFile) {
        int[] hw = new int[]{0, 0};
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapFile.getPath(), opts);
        hw[0] = opts.outWidth;
        hw[1] = opts.outHeight;
        return hw;
    }

    /**
     * 11.从文件中解析bitmap,如果没有指定解析出Bitmap的大小，则直接decodeFile，但是有些像素太大的解析不出，一定要设置bitmap大小 ，由于拍照后照片返回会颠倒 这个方法会帮你矫正照片
     *
     * @param dst    bitmap文件
     * @param width  生成bitmap宽度
     * @param height 生成bitmap高度
     * @return 是bitmap文件
     */
    @SuppressWarnings("deprecation")
    public static Bitmap getBitmapFromFileAdjustDegree(File dst, int width, int height) {
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
                bitmap = rotateBitmap(bitmap, rotate);
            }
            return bitmap;
        }
        return null;
    }

    /**
     * 12.从文件中解析bitmap,如果没有指定解析出Bitmap的大小，则直接decodeFile，但是有些像素太大的解析不出，一定要设置bitmap大小 ，如果是拍照后设置用上面的方法
     *
     * @param dst    bitmap文件
     * @param width  生成bitmap宽度
     * @param height 生成bitmap高度
     * @return 是bitmap文件
     */
    @SuppressWarnings("deprecation")
    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                // 不去真是解析图片，只是获取宽高
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            // 获取图片文件的角度
            int degree = getBitmapDegree(dst);
            if (degree != 0)
                return rotateBitmap(BitmapFactory.decodeFile(dst.getPath(), opts), degree);
            else
                return BitmapFactory.decodeFile(dst.getPath(), opts);
        }
        return null;
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
     * 返回图片文件的角度
     *
     * @param file 图片文件
     * @return 需要偏转的角度
     */
    private static int getBitmapDegree(File file) {
        int rotate = 0;
        int result = 0;
        try {
            // 文件信息
            ExifInterface exifInterface = new ExifInterface(file.getPath());
            // 获取图片的角度
            result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        }
        return rotate;
    }

    /**
     * 13.获取缩略图图片
     *
     * @param imagePath 图片的路径
     * @param width     图片的宽度
     * @param height    图片的高度
     * @return 缩略图图片
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 14.获得带倒影的图片方法
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 15.设置bitmap的水印
     *
     * @param src
     * @return
     */
    public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        // draw src into
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 16.设置高斯模糊的bitmap
     *
     * @param context
     * @param sentBitmap
     * @param radius
     * @return
     */
    @SuppressLint("NewApi")
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        if (VERSION.SDK_INT > 16) {
            // 用系统api实现好像有问题
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    /**
     * 17.保存位图至图片文件
     *
     * @param bitmap 将要位图文件
     * @param file
     * @return
     */
    public static final boolean saveBitmapToFile(Bitmap bitmap, File file) {
        BufferedOutputStream bos = null;
        try {
            if (file != null) {
                file.getParentFile().mkdirs();
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(CompressFormat.PNG, 90, bos);

                bos.flush();
                bos.close();
                bos = null;
                if (file.exists() && file.isDirectory()) {
                    return true;
                } else {
                    return false;
                }
            }
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

    /**
     * 18.按正方形裁切图片
     *
     * @param bitmap 源bitmap
     * @return 剪切后返回的图片
     */
    public static final Bitmap cropSquareBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;
        // 下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    /**
     * 图片按比例大小压缩方法（宽高1200）
     *
     * @param file
     * @return
     */
    public static File compressImageFromFile(File file) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 1200f;// 这里设置高度为800f
        float ww = 1200f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1; // be=1表示不缩放
        if (w > h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(file.getPath(), newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        // bitmap = compressImage(bitmap);//压缩好比例大小后再进行质量压缩

        return getFileFromBitmap(bitmap, file);
    }

    /**
     * 压缩到固定大小（100k）
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * bitmap转file，压缩85%
     *
     * @param bmp
     * @param file
     * @return
     */
    private static File getFileFromBitmap(Bitmap bmp, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            // 直接压缩80%
            bmp.compress(CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 17.保存位图至图片文件
     *
     * @param bitmap 将要位图文件
     * @param file
     * @return
     */
    public static final boolean saveToFile(Bitmap bitmap, File file) {
        BufferedOutputStream bos = null;
        try {
            if (file != null) {
                file.getParentFile().mkdirs();
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(CompressFormat.PNG, 90, bos);

                bos.flush();
                bos.close();
                bos = null;
                if (file.exists() && file.isDirectory()) {
                    return true;
                } else {
                    return false;
                }
            }
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
}
