package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/11/2.
 */

public class PeopleParams extends BaseParam {

//    quit	是否离职 null-全部Y-是N-否
//    inSchedule	是否排班 null-全部Y-是N-否
//    search	检索值
//    timeFrom	起始时间
//    timeTo	截止时间
//    pageIndex	页码
//    pageSize	页大小
    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
