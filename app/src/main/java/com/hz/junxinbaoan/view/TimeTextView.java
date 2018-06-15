package com.hz.junxinbaoan.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hz.junxinbaoan.adapter.SignAdapter;
import com.hz.junxinbaoan.adapter.SignScheAdapter;

/**
 * Created by zhangxl on 2017/4/5.
 */
public class TimeTextView extends AppCompatTextView {
    //    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
    private TextView textView1;
    private TextView textView2;
    private SignAdapter.RefreshUIInterface refreshOverUIInterface;
    private SignScheAdapter.RefreshUIInterface refreshScheUIInterface;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 在控件被销毁时移除消息
        handler.removeMessages( 0 );
    }

    public void setRefreshUIInterface(SignAdapter.RefreshUIInterface refreshOverUIInterface) {
        this.refreshOverUIInterface = refreshOverUIInterface;
    }

    public void setRefreshUIInterface(SignScheAdapter.RefreshUIInterface refreshScheUIInterface) {
        this.refreshScheUIInterface = refreshScheUIInterface;
    }

    long Time;
    private boolean run = true; // 是否启动了
    @SuppressLint("NewApi")
    private Handler handler = new Handler( Looper.getMainLooper() ) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (run) {
                        long mTime = Time;
                        if (mTime >= 0) {
                            TimeTextView.this.setText( dealCountTime( mTime ) );
                            Time = Time - 1;
//                            if (Time==-1){
//                                textView1.setTextColor(Color.RED);
//                                textView2.setTextColor(Color.RED);
//                            }
                            handler.sendEmptyMessageDelayed( 0, 1000 );
                        } else {
                            if (refreshScheUIInterface != null)
                                refreshScheUIInterface.refresh( false );
                            if (refreshOverUIInterface != null)
                                refreshOverUIInterface.refresh( false );
                            handler.removeMessages( 0 );
                        }
                    } else {
                        TimeTextView.this.setText( dealCountTime( Time ) );
                    }
                    break;
            }
        }
    };


    public TimeTextView(Context context) {
        super( context );
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super( context, attrs );
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super( context, attrs, defStyle );
    }

    @SuppressLint("NewApi")
    public void setTimes(long mT) {
        run = true;
        Time = mT;
//        Time=3;
        // 标示已经启动

//        if (mT > 0) {
        handler.removeMessages( 0 );
        handler.sendEmptyMessage( 0 );
//        } else {
//        TimeTextView.this.setVisibility(View.GONE);
//        }
    }

    public void stop() {
        run = false;
    }

    private String dealCountTime(long time) {
        String str = "";
        if (time < 0) {
//            this.setTextColor(Color.RED);
//            if (time/86400!=0){
//                str=time/86400+"天"+(-time%86400/3600)+"时"+(-time%3600/60)+"分"+(-time%60)+"秒";
//            }else if (time/3600!=0){
//                str=time/3600+"时"+(-time%3600/60)+"分"+(-time%60)+"秒";
//            }else if (time/60!=0){
//                str=time/60+"分"+(-time%60)+"秒";
//            }else{
//                str=time+"秒";
//            }
        } else {
//            this.setTextColor(0xffe1a042);
//            if (time/86400!=0){
//                str=time/86400+"天"+(time%86400/3600)+"时"+(time%3600/60)+"分"+(time%60)+"秒";
//            }else if (time/3600!=0){
//                str=time/3600+"时"+time%3600/60+"分"+time%60+"秒";
//            }else if (time/60!=0){
//                str=time/60+"分"+time%60+"秒";
//            }else{
//                str=time+"秒";
//            }

            if (time / 3600 != 0) {
                str = time / 3600 + "时" + time % 3600 / 60 + "分" + time % 60 + "秒";
            } else if (time / 60 != 0) {
                str = "还有" + time / 60 + "分" + time % 60 + "秒" + "可签到";
            } else {
                str = "还有" + time + "秒" + "可签到";
            }
//            if (time / 60 == 0){
//                str=time+"秒";
//            }else {
//                str=time/60+"分"+time%60+"秒";
//            }

        }
        return str;
    }

    public void setViews(TextView view1, TextView view2) {
        this.textView1 = view1;
        this.textView2 = view2;
    }
}
