package com.hz.junxinbaoan.result;

import com.hz.junxinbaoan.data.SignTopBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3 0003.
 */

public class GetStatisticsResult extends BaseResult {
    private List<SignTopBean> data;

    public List<SignTopBean> getData() {
        return data;
    }

    public void setData(List<SignTopBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetStatisticsResult{" +
                "data=" + data +
                '}';
    }
}
