package com.hz.junxinbaoan.result;

/**
 * 学习中心详情
 * Created by linzp on 2017/10/31.
 */

public class StudyDetailResult extends BaseResult {

    /**
     * data : {"learnId":"1","learnType":"系统","learnTitle":"测试标题1","learnContent":"测试内容","learnPictures":null,
     * "gmtCreate":1508210398684,"gmtModified":1508210398684},{"learnId":"2","learnType":"公司","learnTitle":"测试标题2",
     * "learnContent":"测试内容2","learnPictures":null,"gmtCreate":1508210398684,"gmtModified":1508210398684},
     * {"learnId":"3","learnType":"业务","learnTitle":"测试标题3","learnContent":"测试内容3","learnPictures":null,
     * "gmtCreate":1508210398684,"gmtModified":1508210398684}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StudyResult{" +
                "data=" + data +
                '}';
    }

    public class DataBean {
        /**
         * learnId : 1
         * learnType : 系统
         * learnTitle : 测试标题1
         * learnContent : 测试内容
         * learnPictures : null
         * gmtCreate : 1508210398684
         * gmtModified : 1508210398684
         */

        private String learnId;
        private String learnType;
        private String learnTitle;
        private String learnContent;
        private String learnShowPicture;
        private long gmtCreate;
        private long gmtModified;
        private String learnSummary;

        private String addTime;

        private String learnShowName;

        public String getLearnShowName() {
            return learnShowName;
        }

        public void setLearnShowName(String learnShowName) {
            this.learnShowName = learnShowName;
        }


        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getLearnSummary() {
            return learnSummary;
        }

        public void setLearnSummary(String learnSummary) {
            this.learnSummary = learnSummary;
        }

        public String getLearnId() {
            return learnId;
        }

        public void setLearnId(String learnId) {
            this.learnId = learnId;
        }

        public String getLearnType() {
            return learnType;
        }

        public void setLearnType(String learnType) {
            this.learnType = learnType;
        }

        public String getLearnTitle() {
            return learnTitle;
        }

        public void setLearnTitle(String learnTitle) {
            this.learnTitle = learnTitle;
        }

        public String getLearnContent() {
            return learnContent;
        }

        public void setLearnContent(String learnContent) {
            this.learnContent = learnContent;
        }

        public String getLearnPictures() {
            return learnShowPicture;
        }

        public void setLearnPictures(String learnPictures) {
            this.learnShowPicture = learnPictures;
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
            return "DataBean{" +
                    "learnId='" + learnId + '\'' +
                    ", learnType='" + learnType + '\'' +
                    ", learnTitle='" + learnTitle + '\'' +
                    ", learnContent='" + learnContent + '\'' +
                    ", learnShowPicture='" + learnShowPicture + '\'' +
                    ", gmtCreate=" + gmtCreate +
                    ", gmtModified=" + gmtModified +
                    ", learnSummary='" + learnSummary + '\'' +
                    ", addTime='" + addTime + '\'' +
                    ", learnShowName='" + learnShowName + '\'' +
                    '}';
        }
    }
}
