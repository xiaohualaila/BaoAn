package com.hz.junxinbaoan.params;

/**
 * Created by linzp on 2017/11/27.
 */

public class ReadParams extends BaseParam {

    private String messageId,learnId;

    public String getDetail() {
        return messageId;
    }

    public void setDetail(String detail) {
        this.messageId = detail;
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId;
    }

    @Override
    public String toString() {
        return "ReadParams{" +
                "messageId='" + messageId + '\'' +
                ", learnId='" + learnId + '\'' +
                '}';
    }
}
