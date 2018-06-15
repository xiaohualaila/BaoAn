package com.hz.junxinbaoan.data;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class HelpListBean {

    /**
     * reportId : 1
     * reportTitle : 这是一个求助爆料
     * reportContent : 这是一个求助爆料信息
     * reportPictures : null
     * reportAudios : null
     * reportVideos : null
     * reportLon : null
     * reportLat : null
     * reportTimestamp : 1508210398684
     * reportTimeStr : 2017-10-20 11:11:11
     * reportEmployeeId : d653df7d-4e45-4b86-9b64-d03a2f3c9a70
     * reportEmployeeName : 测试
     * reportToPolice : 0
     * reportIgnored : 0
     * gmtCreate : 1508210398684
     * gmtModified : 1508210398684
     */

    private String reportId;
    private String reportTitle;
    private String reportContent;
    private String reportPictures;
    private String reportAudios;
    private String reportVideos;
    private String reportLon;
    private String reportLat;
    private long reportTimestamp;
    private String reportTimeStr;
    private String reportEmployeeId;
    private String reportEmployeeName;
    private int reportToPolice;
    private int reportIgnored;
    private long gmtCreate;
    private long gmtModified;
    private String reportShowPicture;
    private String reportAddress;

    public String getReportAddress() {
        return reportAddress;
    }

    public void setReportAddress(String reportAddress) {
        this.reportAddress = reportAddress;
    }

    public String getReportShowPicture() {
        return reportShowPicture;
    }

    public void setReportShowPicture(String reportShowPicture) {
        this.reportShowPicture = reportShowPicture;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReportPictures() {
        return reportPictures;
    }

    public void setReportPictures(String reportPictures) {
        this.reportPictures = reportPictures;
    }

    public String getReportAudios() {
        return reportAudios;
    }

    public void setReportAudios(String reportAudios) {
        this.reportAudios = reportAudios;
    }

    public String getReportVideos() {
        return reportVideos;
    }

    public void setReportVideos(String reportVideos) {
        this.reportVideos = reportVideos;
    }

    public String getReportLon() {
        return reportLon;
    }

    public void setReportLon(String reportLon) {
        this.reportLon = reportLon;
    }

    public String getReportLat() {
        return reportLat;
    }

    public void setReportLat(String reportLat) {
        this.reportLat = reportLat;
    }

    public long getReportTimestamp() {
        return reportTimestamp;
    }

    public void setReportTimestamp(long reportTimestamp) {
        this.reportTimestamp = reportTimestamp;
    }

    public String getReportTimeStr() {
        return reportTimeStr;
    }

    public void setReportTimeStr(String reportTimeStr) {
        this.reportTimeStr = reportTimeStr;
    }

    public String getReportEmployeeId() {
        return reportEmployeeId;
    }

    public void setReportEmployeeId(String reportEmployeeId) {
        this.reportEmployeeId = reportEmployeeId;
    }

    public String getReportEmployeeName() {
        return reportEmployeeName;
    }

    public void setReportEmployeeName(String reportEmployeeName) {
        this.reportEmployeeName = reportEmployeeName;
    }

    public int getReportToPolice() {
        return reportToPolice;
    }

    public void setReportToPolice(int reportToPolice) {
        this.reportToPolice = reportToPolice;
    }

    public int getReportIgnored() {
        return reportIgnored;
    }

    public void setReportIgnored(int reportIgnored) {
        this.reportIgnored = reportIgnored;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "HelpListBean{" +
                "reportId='" + reportId + '\'' +
                ", reportTitle='" + reportTitle + '\'' +
                ", reportContent='" + reportContent + '\'' +
                ", reportPictures='" + reportPictures + '\'' +
                ", reportAudios='" + reportAudios + '\'' +
                ", reportVideos='" + reportVideos + '\'' +
                ", reportLon='" + reportLon + '\'' +
                ", reportLat='" + reportLat + '\'' +
                ", reportTimestamp=" + reportTimestamp +
                ", reportTimeStr='" + reportTimeStr + '\'' +
                ", reportEmployeeId='" + reportEmployeeId + '\'' +
                ", reportEmployeeName='" + reportEmployeeName + '\'' +
                ", reportToPolice=" + reportToPolice +
                ", reportIgnored=" + reportIgnored +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", reportShowPicture='" + reportShowPicture + '\'' +
                ", reportAddress='" + reportAddress + '\'' +
                '}';
    }
}
