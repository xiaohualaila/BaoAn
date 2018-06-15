package com.hz.junxinbaoan.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hz.junxinbaoan.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * 用法：setPtrHandler,如果里面包含ListView或者ScrollView,重写的checkCanDoRefresh中请return
 * PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header)（酌情使用，如果顶部有其他请另作判断）,
 * 否则return true即可，true即为可刷新
 * refreshComplete()，停止刷新
 * 带有动画的下拉刷新FrameLayout。
 * 里面只能包含一个View。
 * Created by zhaohh on 2016/5/4.
 */
public class AnimPtrFrameLayout extends PtrFrameLayout {
    private LayoutInflater mInflater;

    public AnimPtrFrameLayout(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        initView();
    }

    public AnimPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        initView();
    }

    public AnimPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        disableWhenHorizontalMove(true);
        View view = mInflater.inflate(R.layout.refresh_view, this, false);
        ImageView img = (ImageView) view.findViewById(R.id.iv);
        final TextView tv = (TextView) view.findViewById(R.id.tv);

      /*  Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        animation.setInterpolator(new LinearInterpolator());
        img.startAnimation(animation);*/
        AnimationDrawable anim = new AnimationDrawable();
        for (int i = 0; i < 2; i++) {
            int id = getResources().getIdentifier("refresh_" + i, "mipmap", getContext().getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            anim.addFrame(drawable, 1000/12);
        }
        anim.setOneShot(false);
        img.setImageDrawable(anim);
        anim.start();

        setRatioOfHeaderHeightToRefresh(0.8f);

        setHeaderView(view);
        // 刷新时的UI变动处理
        addPtrUIHandler(new PtrUIHandler() {
            //Content 重新回到顶部， Header 消失，整个下拉刷新过程完全结束以后，重置 View 。
            @Override
            public void onUIReset(PtrFrameLayout frame) {

            }

            //准备刷新，Header 将要出现时调用。
            @Override
            public void onUIRefreshPrepare(PtrFrameLayout frame) {


                tv.setText("下拉刷新");
            }

            //开始刷新，Header 进入刷新状态之前调用。
            @Override
            public void onUIRefreshBegin(PtrFrameLayout frame) {
                Log.d("TAG","onUIRefreshBegin");
                tv.setText("正在刷新");
            }

            //刷新结束，Header 开始向上移动之前调用。
            @Override
            public void onUIRefreshComplete(PtrFrameLayout frame) {

                Log.d("TAG","onUIRefreshComplete");
                tv.setText("刷新完成");
            }

            //下拉过程中位置变化回调。
            @Override
            public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
                final int mOffsetToRefresh = frame.getOffsetToRefresh();
                final int currentPos = ptrIndicator.getCurrentPosY();
                final int lastPos = ptrIndicator.getLastPosY();
                if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
                    if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {

                        tv.setText("下拉刷新");
                    }
                } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
                    if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                        Log.d("TAG","释放刷新");
                        tv.setText("释放刷新");
                    }
                }
            }
        });
    }
}
