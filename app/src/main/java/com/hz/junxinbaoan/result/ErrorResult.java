package com.hz.junxinbaoan.result;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class ErrorResult extends BaseResult{
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ErrorResult{" +
                "data='" + data + '\'' +
                '}';
    }
}
