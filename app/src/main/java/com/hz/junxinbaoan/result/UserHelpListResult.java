package com.hz.junxinbaoan.result;

import com.hz.junxinbaoan.data.HelpListBean;
import com.hz.junxinbaoan.data.UseHelpBean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class UserHelpListResult extends BaseResult {

    private List<UseHelpBean> data;

    public List<UseHelpBean> getData() {
        return data;
    }

    public void setData(List<UseHelpBean> data) {
        this.data = data;
    }

}
