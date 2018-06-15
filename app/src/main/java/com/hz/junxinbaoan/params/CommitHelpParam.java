package com.hz.junxinbaoan.params;

/**
 * Created by Administrator on 2017/10/30 0030.
 */

public class CommitHelpParam extends BaseParam {
    private String reportTitle;
    private String reportContent;
    private String reportPictures;
    private String reportAudios;
    private String reportVideos;
    private String reportLon;
    private String reportLat;
    private String reportAddress;

    public String getReportAddress() {
        return reportAddress;
    }

    public void setReportAddress(String reportAddress) {
        this.reportAddress = reportAddress;
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
}
