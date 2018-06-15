package com.hz.junxinbaoan.result;

import com.hz.junxinbaoan.data.GetInfoBean;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class GetInfoResult extends BaseResult {
    private GetInfoBean data;

    public GetInfoBean getData() {
        return data;
    }

    public void setData(GetInfoBean data) {
        this.data = data;
    }
}
