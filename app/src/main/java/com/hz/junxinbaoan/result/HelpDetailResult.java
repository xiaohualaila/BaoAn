package com.hz.junxinbaoan.result;


import com.hz.junxinbaoan.data.HelpListBean;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class HelpDetailResult extends BaseResult {
    private HelpListBean data;

    public HelpListBean getData() {
        return data;
    }

    public void setData(HelpListBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HelpDetailResult{" +
                "data=" + data +
                '}';
    }
}
