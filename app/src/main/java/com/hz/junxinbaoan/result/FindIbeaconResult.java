package com.hz.junxinbaoan.result;

import com.hz.junxinbaoan.data.FindIbeaconBean;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class FindIbeaconResult extends BaseResult{
    private FindIbeaconBean data;

    public FindIbeaconBean getData() {
        return data;
    }

    public void setData(FindIbeaconBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FindIbeaconResult{" +
                "data=" + data +
                '}';
    }
}
