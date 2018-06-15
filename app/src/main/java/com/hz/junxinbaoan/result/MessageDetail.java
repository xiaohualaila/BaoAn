package com.hz.junxinbaoan.result;

/**
 * 消息详情
 * <p>
 * Created by Administrator on 2017/12/21.
 */

public class MessageDetail extends BaseResult {
    private MessageBean data;

    public MessageBean getData() {
        return data;
    }

    public void setData(MessageBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MessageDetail{" +
                "data=" + data +
                '}';
    }


    public class MessageBean {
        private String messageId, messageType, messageTitle, messageContent, messageAdminId, messageApplicationFlag,
                messageShowPicture, messageReadCount, messageIsPublic, messageCompanyId, messageShowName;
        private long gmtCreate, gmtModified;
        private String messageSummary;
        private String addTime;

        public String getMessageShowName() {
            return messageShowName;
        }

        public void setMessageShowName(String messageShowName) {
            this.messageShowName = messageShowName;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getMessageSummary() {
            return messageSummary;
        }

        public void setMessageSummary(String messageSummary) {
            this.messageSummary = messageSummary;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
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

        public String getMessageAdminId() {
            return messageAdminId;
        }

        public void setMessageAdminId(String messageAdminId) {
            this.messageAdminId = messageAdminId;
        }

        public String getMessageApplicationFlag() {
            return messageApplicationFlag;
        }

        public void setMessageApplicationFlag(String messageApplicationFlag) {
            this.messageApplicationFlag = messageApplicationFlag;
        }

        public String getMessagePictures() {
            return messageShowPicture;
        }

        public void setMessagePictures(String messagePictures) {
            this.messageShowPicture = messagePictures;
        }

        public String getMessageReadCount() {
            return messageReadCount;
        }

        public void setMessageReadCount(String messageReadCount) {
            this.messageReadCount = messageReadCount;
        }

        public String getMessageIsPublic() {
            return messageIsPublic;
        }

        public void setMessageIsPublic(String messageIsPublic) {
            this.messageIsPublic = messageIsPublic;
        }

        public String getMessageCompanyId() {
            return messageCompanyId;
        }

        public void setMessageCompanyId(String messageCompanyId) {
            this.messageCompanyId = messageCompanyId;
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
            return "MessageDetail{" +
                    "messageId='" + messageId + '\'' +
                    ", messageType='" + messageType + '\'' +
                    ", messageTitle='" + messageTitle + '\'' +
                    ", messageContent='" + messageContent + '\'' +
                    ", messageAdminId='" + messageAdminId + '\'' +
                    ", messageApplicationFlag='" + messageApplicationFlag + '\'' +
                    ", messageShowPicture='" + messageShowPicture + '\'' +
                    ", messageReadCount='" + messageReadCount + '\'' +
                    ", messageIsPublic='" + messageIsPublic + '\'' +
                    ", messageCompanyId='" + messageCompanyId + '\'' +
                    ", messageShowName='" + messageShowName + '\'' +
                    ", gmtCreate=" + gmtCreate +
                    ", gmtModified=" + gmtModified +
                    ", messageSummary='" + messageSummary + '\'' +
                    ", addTime='" + addTime + '\'' +
                    '}';
        }
    }
}
