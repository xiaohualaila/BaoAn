package com.hz.junxinbaoan.activity.dialog;

import android.content.Context;
import android.view.Gravity;
import android.widget.SeekBar;

import com.hz.junxinbaoan.R;

import java.util.List;

/**
 * Created by linzp on 2017/11/20.
 */

public class Apk_dialog extends BaseDialog {
    SeekBar seekBar;


    public Apk_dialog(Context context) {
        super(context, R.style.dim_dialog);
        seekBar= (SeekBar) findViewById(R.id.seek);
        seekBar.setEnabled(false);
    }


    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.apk_dialog;
    }

    @Override
    protected void findViews() {

        addListeners();
    }

    private void addListeners() {


    }

    @Override
    protected void setWindowParam() {
        setWindowParams(-1, -2, Gravity.CENTER, 0);
    }

    public void setList(List<String> listType) {

    }

    public interface GetString{
        void getString(String s);
    }
}
