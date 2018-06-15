package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.data.SignTopBean;
import com.hz.junxinbaoan.utils.DensityUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by linzp on 2017/10/23.
 */

public class WorkTimeAdapter extends RecyclerView.Adapter<WorkTimeAdapter.MyViewHolder> {
    int max = 10 * 60;

    public void setMax(int max) {
        if (max <= 10 * 60) {
            this.max = 10 * 60;
        } else if (max > 10 * 60 && max <= 15 * 60) {
            this.max = 15 * 60;
        } else if (max <= 20 * 60 && max > 15 * 60) {
            this.max = 20 * 60;
        } else if (max > 20) {
            this.max = 25 * 60;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View time_work, time_rule;
        TextView day;
        LinearLayout box, pipe;

        public MyViewHolder(View view) {
            super( view );
            time_rule = view.findViewById( R.id.ruletime_v );
            time_work = view.findViewById( R.id.worktime_v );
            day = (TextView) view.findViewById( R.id.days );
            box = (LinearLayout) view.findViewById( R.id.box );
            pipe = (LinearLayout) view.findViewById( R.id.pipe );
        }
    }

    Context context;
    List<SignTopBean> topdata;

    public WorkTimeAdapter(Context context, List<SignTopBean> topdata) {
        this.context = context;
        this.topdata = topdata;
    }

    @Override
    public int getItemCount() {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get( Calendar.YEAR );
        int month = c.get( Calendar.MONTH ) + 1;
        int size = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                size = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                size = 30;
                break;
            case 2:
                if (year % 4 == 0 && year % 400 != 0) {
                    size = 29;
                } else {
                    size = 28;
                }
                break;
        }

        return size;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder( LayoutInflater.from( context ).inflate( R
                .layout.work_time_item, parent, false ) );

        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        WindowManager wm = ((BaseActivity) context).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams boxparams = holder.box.getLayoutParams();
        boxparams.width = (int) (0.95 * (width - DensityUtils.dp2px( context, 30 )) / (9));   //屏幕显示9等分
        holder.box.setLayoutParams( boxparams );


        if (position > topdata.size() - 1) {   //29
            holder.day.setText( (position + 1) + "" );
            //改变深蓝色高度
            ViewGroup.LayoutParams workParams = holder.time_work.getLayoutParams();
            workParams.height = 0;
            holder.time_work.setLayoutParams( workParams );
            //改变浅蓝色高度
            ViewGroup.LayoutParams ruleParams = holder.time_rule.getLayoutParams();
            ruleParams.height = 0;
            holder.time_rule.setLayoutParams( ruleParams );
        } else {
            int hight = 130;


            holder.day.setText( (position + 1) + "" );
            //改变深蓝色高度
            ViewGroup.LayoutParams workParams = holder.time_work.getLayoutParams();
            if (topdata.get( position ).getHourActual() > 20 * 60) {//20以后4等分
                workParams.height = DensityUtils.dp2px( context, (float) (hight * (((float) topdata.get( position )
                        .getHourActual() - 20 * 60) / 20 * 60 + 0.8)) );
            } else {
                workParams.height = DensityUtils.dp2px( context, (float) (hight * ((double) topdata.get( position )
                        .getHourActual() / (double) max)) );
            }
            holder.time_work.setLayoutParams( workParams );

            //改变浅蓝色高度
            ViewGroup.LayoutParams ruleParams = holder.time_rule.getLayoutParams();
            if (topdata.get( position ).getHourExpect() > 20 * 60) {//20以后4等分
                ruleParams.height = DensityUtils.dp2px( context, (float) (hight * (((double) (topdata.get( position )
                        .getHourExpect() - 20 * 60)) / 20 * 60 + 0.8)) );
            } else {
                ruleParams.height = DensityUtils.dp2px( context, (float) (hight * ((double) topdata.get( position )
                        .getHourExpect() / (double) max)) );
            }
            holder.time_rule.setLayoutParams( ruleParams );

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}