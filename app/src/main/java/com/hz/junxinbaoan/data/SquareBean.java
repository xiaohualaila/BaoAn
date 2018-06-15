package com.hz.junxinbaoan.data;

/**
 * Created by linzp on 2017/10/24.
 */

public class SquareBean {
    String help,myproject,check,study;

    public String getHelp() {
        return "本月已上报 "+help+" 条";
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getMyproject() {
        if (myproject.equals("1")){
            return "今日有排班";
        }
        return "今日无排班";
    }

    public void setMyproject(String myproject) {
        this.myproject = myproject;
    }

    public String getCheck() {
        return check+" 条待通过";
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getStudy() {
        return study+" 条新资源";
    }

    public void setStudy(String study) {
        this.study = study;
    }
}
