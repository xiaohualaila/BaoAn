package com.hz.junxinbaoan.result;


import com.hz.junxinbaoan.data.HelpListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class HelpListResult extends BaseResult {
    private List<HelpListBean> data;

    public List<HelpListBean> getData() {
        return data;
    }

    public void setData(List<HelpListBean> data) {
        this.data = data;
    }
}
