package com.hz.junxinbaoan.params;

import java.io.File;

/**
 * Created by linzp on 2017/10/30.
 */

public class FileParams extends BaseParam {
    private File file;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileParams{" +
                "file=" + file +
                ", type='" + type + '\'' +
                '}';
    }
}
