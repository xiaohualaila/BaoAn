package com.hz.junxinbaoan.activity.dialog;

import android.content.Context;

import com.hz.junxinbaoan.R;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class RecordDialog extends BaseDialog {
    private RecordInterface recordInterface;
    private Showing showing;
    public RecordDialog(Context context) {
        super(context,R.style.dim_dialog);
    }
    public RecordDialog(Context context,Showing showing) {
        super(context,R.style.dim_dialog);
        this.showing = showing;
    }


    public void setRecordInterface(RecordInterface recordInterface) {
        this.recordInterface = recordInterface;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_microphone;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setWindowParam() {

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (recordInterface!=null)
            recordInterface.stop();
        if (showing!=null)
            showing.isDis();

    }

    public interface RecordInterface{
        void stop();
    }

    @Override
    public void show() {
        super.show();
        if (showing!=null)
            showing.isShow();
    }

    public interface Showing{
        void isShow();
        void isDis();
    }

}
