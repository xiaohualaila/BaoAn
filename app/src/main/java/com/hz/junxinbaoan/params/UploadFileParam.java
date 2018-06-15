package com.hz.junxinbaoan.params;

import java.io.File;

/**
 * Created by liy on 2017/3/29.
 */

public class UploadFileParam extends BaseParam{
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
