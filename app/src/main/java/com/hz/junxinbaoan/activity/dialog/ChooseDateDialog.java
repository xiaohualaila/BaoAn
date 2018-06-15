package com.hz.junxinbaoan.activity.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarView;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.utils.CommonUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2017/10/23 0023.
 */

public class ChooseDateDialog extends BaseDialog {


    private CalendarDateView calendarDateView;
    private TextView date_tv;
    private TextView year_tv;

    private CalendarBean curDate;
    private CaledarAdapter adapter;
    private ImageView left_iv;
    private ImageView right_iv;
    private TextView time_tv;
    private TextView ok_tv;
    private TextView no_tv;
    private Animation animation;
    private LinearLayout box;

    private OnClickOkListener onClickOkListener;

    private boolean isByClick = true;

    public ChooseDateDialog(Context context,OnClickOkListener onClickOkListener) {
        super(context,R.style.dim_dialog);
        this.onClickOkListener = onClickOkListener;
        animation=  AnimationUtils.loadAnimation(context, R.anim.dialogtotop);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_calendar;
    }

    @Override
    protected void findViews() {
        calendarDateView = ((CalendarDateView) findViewById(R.id.calendarDateView));
        date_tv = ((TextView) findViewById(R.id.date_tv));
        year_tv = ((TextView) findViewById(R.id.year_tv));
        left_iv = ((ImageView) findViewById(R.id.left_iv));
        right_iv = ((ImageView) findViewById(R.id.right_iv));
        time_tv = ((TextView) findViewById(R.id.time_tv));
        ok_tv = ((TextView) findViewById(R.id.ok_tv));
        no_tv = ((TextView) findViewById(R.id.no_tv));
        box = (LinearLayout) findViewById(R.id.box);

        final Calendar calendar = Calendar.getInstance();
        curDate = new CalendarBean(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DATE));
        curDate.week = calendar.get(Calendar.DAY_OF_WEEK);
        date_tv.setText(curDate.getDisplayWeek()+","+curDate.moth+"月"+curDate.day+"日");
        time_tv.setText(calendar.get(Calendar.MONTH) + 1+"月 "+calendar.get(Calendar.YEAR));
        year_tv.setText(curDate.year+"");
        time_tv.setText(CommonUtils.numToString(calendar.get(Calendar.MONTH) + 1)+"月 "+calendar.get(Calendar.YEAR));
        adapter = new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                //判断convertView为null，可以有效利用view的回收重用，左右滑动的效率高
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
                }

                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);
                RelativeLayout background = (RelativeLayout) convertView.findViewById(R.id.background);
                ImageView bg = (ImageView) convertView.findViewById(R.id.bg);

                text.setText("" + bean.day);
                //mothFlag 0是当月，-1是月前，1是月后
                if (bean.mothFlag != 0) {
//                    background.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    convertView.setEnabled(false);
                } else {
                    convertView.setEnabled(true);
                    background.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    text.setTextColor(0xff444444);
                }

                //判断是否是今天
                Calendar c = new GregorianCalendar();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH) + 1;
                int d = c.get(Calendar.DAY_OF_MONTH);
                if (bean.year == y && bean.moth == m && bean.day == d){
                    text.setTextColor(Color.parseColor("#009688"));
                }else {
                    text.setTextColor(Color.BLACK);
                }

                if (bean.mothFlag == 0){
                    if (bean.toString().equals(curDate.toString())){
                        text.setTextColor(Color.WHITE);
                        bg.setBackgroundResource(R.drawable.shape_calendar);
                    }else {
                        bg.setBackgroundColor(Color.WHITE);
                        background.setBackgroundColor(Color.WHITE);
                    }
                }


//                if (bean.isChooseDay){
//                    background.setBackgroundResource(R.drawable.shape_calendar);
//                    text.setTextColor(Color.WHITE);
//                }else {
//                    background.setBackgroundColor(Color.WHITE);
//                }
//                chinaText.setText(bean.chinaDay);

                return convertView;
            }
        };
        calendarDateView.setAdapter(adapter);

        calendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
//                mTitle.setText(bean.year + "/" + bean.moth + "/" + bean.day);
                clickItem(bean);
            }
        });

        left_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isByClick = false;
                calendarDateView.setCurrentItem(calendarDateView.getCurrentItem() - 1);

            }
        });
        right_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isByClick = false;
                calendarDateView.setCurrentItem(calendarDateView.getCurrentItem() + 1);

            }
        });
        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOkListener.onClickOk(curDate);

            }
        });
        no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    protected void setWindowParam() {

    }

    public interface OnClickOkListener{
        void onClickOk(CalendarBean calendarBean);
    }

    @Override
    public void show() {
        box.startAnimation(animation);
        super.show();
    }

    public void clickItem(CalendarBean bean){
        if (isByClick){
            year_tv.setText(bean.year+"");
            date_tv.setText(bean.getDisplayWeek()+","+bean.moth+"月"+bean.day+"日");
            curDate = bean;
        }else {
            isByClick = true;
        }
        time_tv.setText(CommonUtils.numToString(bean.moth)+"月 "+bean.year);
        try {
            calendarDateView.getViews().get(calendarDateView.getCurrentItem()).notifyDate();
        } catch (Exception ignore) {
        }
    }

}
