package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.data.SignTopBean;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class SignTopAdapter extends RecyclerView.Adapter {
    private final Context context;
    private List<SignTopBean> datas;
    private OnItemClickListener onItemClickListener;

    public SignTopAdapter(Context context,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        datas = new ArrayList<>();
    }

    public void setDatas(List<SignTopBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sign_top,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final SignTopBean signTopBean = datas.get(position);
//        Calendar calendar = Calendar.getInstance();
        String date = signTopBean.getDate();
        if (!CommonUtils.isEmpty(date)){
            String[] time = date.split("-");
            viewHolder.day.setText(Integer.parseInt(time[2])+"");
            viewHolder.month.setText(time[1]+"月");

//            if (Integer.parseInt(time[2]) == calendar.get(Calendar.DAY_OF_MONTH)){
//                signTopBean.setChoose(true);
//            }
        }
//        if (!CommonUtils.isEmpty(signTopBean.getDay())){
//            viewHolder.day.setText(signTopBean.getDay());
//        }
//        if (!CommonUtils.isEmpty(signTopBean.getMonth())){
//            viewHolder.month.setText(signTopBean.getMonth()+"月");
//        }



        if (signTopBean.isChoose()){
            viewHolder.day.setTextColor(Color.parseColor("#3EA3F4"));
            viewHolder.month.setTextColor(Color.parseColor("#3EA3F4"));
            viewHolder.day.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) 21.5);
            ((LinearLayout.LayoutParams) viewHolder.day.getLayoutParams()).setMargins(0,DensityUtils.dp2px(context,3),0,0);
            viewHolder.month.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            ((LinearLayout.LayoutParams) viewHolder.month.getLayoutParams()).setMargins(0,DensityUtils.dp2px(context,15),0,0);
        }else {
            viewHolder.day.setTextColor(Color.BLACK);
            viewHolder.day.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            ((LinearLayout.LayoutParams) viewHolder.day.getLayoutParams()).setMargins(0,DensityUtils.dp2px(context,8),0,0);
            viewHolder.month.setTextColor(Color.BLACK);
            viewHolder.month.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            ((LinearLayout.LayoutParams) viewHolder.month.getLayoutParams()).setMargins(0,DensityUtils.dp2px(context,17),0,0);
        }

//        final int restartTime = Integer.parseInt(signTopBean.getRestarttime());
//        final int reendTime = Integer.parseInt(signTopBean.getReendtime());
//        final int startTime = Integer.parseInt(signTopBean.getStarttime());
//        final int endTime = Integer.parseInt(signTopBean.getEndtime());

        int expect = signTopBean.getHourExpect();
        int actual = signTopBean.getHourActual();

//        viewHolder.line_rl.removeAllViews();

        /*  "data":[{"hourExpect":0,"hourActual":11,"date":"2017-11-02"}]   */
        /*      画日期下方的期望工作时间线（浅蓝色）    */
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.expect_view.getLayoutParams();
        params.width = DensityUtils.dp2px(context, (float) (100 * (((double)expect) / (24*60))));
        viewHolder.expect_view.setLayoutParams(params);

        /*      画日期下方的实际工作时间线（深蓝色）    */
        params = (RelativeLayout.LayoutParams) viewHolder.actual_view.getLayoutParams();
        params.width = DensityUtils.dp2px(context, (float) (100 * (((double)actual) / (24*60))));
//        if (expect == 0){
//            params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(context,100 * actual / 24),RelativeLayout.LayoutParams.MATCH_PARENT);
////            params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
//        }else {
//
//
//        }
        viewHolder.actual_view.setLayoutParams(params);



//        if (startTime < endTime){
//            View view = new View(context);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(context,100 * (endTime - startTime) / 24), ViewGroup.LayoutParams.MATCH_PARENT);
//            params.setMargins(DensityUtils.dp2px(context,100 * startTime / 24),0,0,0);
//            view.setLayoutParams(params);
//            view.setBackgroundResource(R.drawable.sign_both_work);
//            viewHolder.line_rl.addView(view);
//        }else {
//            //跨天 排班
////            datas.add(new SignTopBean("18","2","18","3","10","21"));
//
//            View view = new View(context);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(context,100 * endTime  / 24), ViewGroup.LayoutParams.MATCH_PARENT);
//            view.setLayoutParams(params);
//            view.setBackgroundResource(R.drawable.sign_right_work);
//            viewHolder.line_rl.addView(view);
//
//            View view2 = new View(context);
//            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(DensityUtils.dp2px(context,100 * (24 - startTime)  / 24), ViewGroup.LayoutParams.MATCH_PARENT);
//            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
////            params.setMargins(DensityUtils.dp2px(context,100 * startTime / 24),0,0,0);
//            view2.setLayoutParams(params2);
//            view2.setBackgroundResource(R.drawable.sign_left_work);
//            viewHolder.line_rl.addView(view2);
//
//        }
//
//        if (restartTime < reendTime){
//            View view = new View(context);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(context,100 * (reendTime - restartTime) / 24), ViewGroup.LayoutParams.MATCH_PARENT);
//
//            params.setMargins(DensityUtils.dp2px(context,100 * restartTime / 24),0,0,0);
//            view.setLayoutParams(params);
//            view.setBackgroundResource(R.drawable.sign_both_rework);
//            viewHolder.line_rl.addView(view);
//        }else if (restartTime != -1 && reendTime != -1){
//            //跨天 排班
////            datas.add(new SignTopBean("18","2","18","3","10","21"));
//
//            View view = new View(context);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(context,100 * reendTime  / 24), ViewGroup.LayoutParams.MATCH_PARENT);
//            view.setLayoutParams(params);
//            view.setBackgroundResource(R.drawable.sign_right_rework);
//            viewHolder.line_rl.addView(view);
//
//            View view2 = new View(context);
//            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(DensityUtils.dp2px(context,100 * (24 - restartTime)  / 24), ViewGroup.LayoutParams.MATCH_PARENT);
//            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
////            params.setMargins(DensityUtils.dp2px(context,100 * restartTime / 24),0,0,0);
//            view2.setLayoutParams(params2);
//            view2.setBackgroundResource(R.drawable.sign_left_rework);
//            viewHolder.line_rl.addView(view2);
//        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position, signTopBean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView month,day;
        private RelativeLayout line_rl;
        private View expect_view,actual_view;

        public ViewHolder(View itemView) {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.month_tv);
            day = (TextView) itemView.findViewById(R.id.day_tv);
            line_rl = (RelativeLayout) itemView.findViewById(R.id.line_rl);
            actual_view = itemView.findViewById(R.id.actual_view);
            expect_view = itemView.findViewById(R.id.expect_view);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position,SignTopBean signTopBean);
    }
}
