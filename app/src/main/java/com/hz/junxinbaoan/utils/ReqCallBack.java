package com.hz.junxinbaoan.utils;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public interface ReqCallBack<T> {
    /**
     * 响应成功
     */
    void onReqSuccess(T result);

    /**
     * 响应失败
     */
    void onReqFailed(String errorMsg);
}