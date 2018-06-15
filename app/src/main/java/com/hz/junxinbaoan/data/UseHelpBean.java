package com.hz.junxinbaoan.data;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class UseHelpBean {
    private String helpId;
    private String helpTitle;
    private String helpContent;
    private String helpSummary;
    private String addTime;

    public UseHelpBean() {
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getHelpSummary() {
        return helpSummary;
    }

    public void setHelpSummary(String helpSummary) {
        this.helpSummary = helpSummary;
    }

    public String getHelpId() {
        return helpId;
    }

    public void setHelpId(String helpId) {
        this.helpId = helpId;
    }

    public String getHelpTitle() {
        return helpTitle;
    }

    public void setHelpTitle(String helpTitle) {
        this.helpTitle = helpTitle;
    }

    public String getHelpContent() {
        return helpContent;
    }

    public void setHelpContent(String helpContent) {
        this.helpContent = helpContent;
    }
}
