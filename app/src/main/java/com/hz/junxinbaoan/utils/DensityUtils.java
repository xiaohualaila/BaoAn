package com.hz.junxinbaoan.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by jinl on 2016/10/8.
 */
public class DensityUtils {
    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public final static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public final static int px2dp(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }

    /**
     * 获得系统状态栏高度
     */
    public final static int getStatusBarHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            Object obj = cls.newInstance();
            Field field = cls.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
        }
        return 0;
    }
}
