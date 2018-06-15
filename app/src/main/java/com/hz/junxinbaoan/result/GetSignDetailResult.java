package com.hz.junxinbaoan.result;

import com.hz.junxinbaoan.data.SignDetailBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class GetSignDetailResult extends BaseResult {

    private List<SignDetailBean> data;

    public List<SignDetailBean> getData() {
        return data;
    }

    public void setData(List<SignDetailBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetSignDetailResult{" +
                "data=" + data +
                '}';
    }
}
