package com.hz.junxinbaoan.data;

/**
 * Created by linzp on 2017/10/24.
 */

public class MessageBean {
//    code	0000-成功，9999-失败
//    data	数据/错误信息
//    data.messageId	消息id
//    data.messageType	消息类型
//    data.messageTitle	标题
//    data.messageContent	内容
//    data.messageTimestamp	发布时间(时间戳)
//    data.messageTimeStr	发布时间(字符串)
//    data.messageEmployeeId	员工id
//    data.messageEmployeeName	员工名称
//    data.messagePictures	图片
//    data.messageReadCount	阅读统计
//    data.gmtCreate	添加时间
//    data.gmtModified	修改时间
    long messageId,messageTimestamp;
    String messageType,messageTitle,messageContent,messageTimeStr,messageEmployeeId,messageEmployeeName,messagePictures,messageReadCount,gmtCreate,gmtModified;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public long getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(long messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public String getMessageTimeStr() {
        return messageTimeStr;
    }

    public void setMessageTimeStr(String messageTimeStr) {
        this.messageTimeStr = messageTimeStr;
    }

    public String getMessageEmployeeId() {
        return messageEmployeeId;
    }

    public void setMessageEmployeeId(String messageEmployeeId) {
        this.messageEmployeeId = messageEmployeeId;
    }

    public String getMessageEmployeeName() {
        return messageEmployeeName;
    }

    public void setMessageEmployeeName(String messageEmployeeName) {
        this.messageEmployeeName = messageEmployeeName;
    }

    public String getMessagePictures() {
        return messagePictures;
    }

    public void setMessagePictures(String messagePictures) {
        this.messagePictures = messagePictures;
    }

    public String getMessageReadCount() {
        return messageReadCount;
    }

    public void setMessageReadCount(String messageReadCount) {
        this.messageReadCount = messageReadCount;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }
}
