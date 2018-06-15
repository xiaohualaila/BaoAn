package com.hz.junxinbaoan.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.common.Settings;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;

import java.io.File;
import java.math.BigDecimal;

import butterknife.InjectView;

public class SettingActivity extends BaseActivity {

    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.title_name)
    TextView titleName;
    @InjectView(R.id.title_lay)
    RelativeLayout titleLay;
    @InjectView(R.id.clear)
    RelativeLayout clear;
    @InjectView(R.id.howbig)
    TextView howbig;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_setting );
    }

    @Override
    protected void initViews() {
        super.initViews();
        File cacheFolder = new File( Settings.PIC_PATH );
        try {
            double a = CommonUtils.getFileSize( cacheFolder );
            String s = "M";
            if (a != 0) {
                if (a >= 1024) {
                    a = a / 1024;
                    s = "KB";
                }
                if (a >= 1024) {
                    a = a / 1024;
                    s = "M";
                }
                if (a >= 1024) {
                    a = a / 1024;
                    s = "G";
                }
            }
            BigDecimal b = new BigDecimal( a );
            double a1 = b.setScale( 1, BigDecimal.ROUND_HALF_UP ).doubleValue();

            howbig.setText( "共 " + a1 + s + " 缓存文件" );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        clear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.cleanCache( mBaseActivity );
                MyToast.showToast( mBaseActivity, "清除成功" );
                initViews();
            }
        } );
    }
}
