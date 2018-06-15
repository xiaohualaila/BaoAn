package com.hz.junxinbaoan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;


/**
 * 通用自定义顶部栏
 * 
 * @author llj 14/8/30
 * 
 */
public class CommonTitlebar extends FrameLayout {
	private LinearLayout mContainLi;
	private TextView mLeftTv;
	private TextView mCenterTv;
	private TextView mRightTv;
	private View mLineV;

	public CommonTitlebar(Context context) {
		super(context);
		initViews(context, null);
	}

	public CommonTitlebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context, attrs);
	}

	public CommonTitlebar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context, attrs);
	}

	private void initViews(Context context, AttributeSet attrs) {
		LayoutInflater.from(getContext()).inflate(R.layout.common_titlebar_layout, this);
		mContainLi = (LinearLayout) findViewById(R.id.li_contain);
		mLeftTv = (TextView) findViewById(R.id.tv_left_text);
		mCenterTv = (TextView) findViewById(R.id.tv_center_text);
		mRightTv = (TextView) findViewById(R.id.tv_right_text);
		mLineV = findViewById(R.id.v_line);
		initAttrs(context, attrs);
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonTitlebar);
			// 设置比重值
			LinearLayout.LayoutParams mLeftLiParams = (LinearLayout.LayoutParams) mLeftTv.getLayoutParams();
			LinearLayout.LayoutParams mRightLiParams = (LinearLayout.LayoutParams) mRightTv.getLayoutParams();
			LinearLayout.LayoutParams mCenterTvParams = (LinearLayout.LayoutParams) mCenterTv.getLayoutParams();

			mLeftLiParams.weight = typedArray.getFloat(R.styleable.CommonTitlebar_titlebar_lefttext_weight, 1);
			mRightLiParams.weight = typedArray.getFloat(R.styleable.CommonTitlebar_titlebar_righttext_weight, 1);
			mCenterTvParams.weight = typedArray.getFloat(R.styleable.CommonTitlebar_titlebar_centertext_weight, 4);

			mContainLi.getLayoutParams().height = typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_li_contain_height, 88);

			mLeftTv.setLayoutParams(mLeftLiParams);
			mRightTv.setLayoutParams(mRightLiParams);
			mCenterTv.setLayoutParams(mCenterTvParams);
			// 设置左右textpadding值
			mLeftTv.setPadding(typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_lefttext_padding_left, 16), 0, 0, 0);
			mRightTv.setPadding(typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_righttext_padding_left, 16), 0, 0, 0);
			mRightTv.setPadding(0, 0, typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_righttext_padding_right, 16), 0);
			//
			mLeftTv.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_lefttext_drawable_padding, 0));
			mRightTv.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_righttext_drawable_padding, 0));
			// 设置text文字
			mLeftTv.setText(typedArray.getString(R.styleable.CommonTitlebar_titlebar_lefttext));
			mCenterTv.setText(typedArray.getString(R.styleable.CommonTitlebar_titlebar_centertext));
			mRightTv.setText(typedArray.getString(R.styleable.CommonTitlebar_titlebar_righttext));
			// 设置text大小
			// 默认传入的是sp单位，由于这里textSize已经的px单位，所以需要制定传入的单位为px
			mLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_lefttext_size, 24));
			mCenterTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_centertext_size, 24));
			mRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.CommonTitlebar_titlebar_righttext_size, 24));
			// 设置textcolor
			if (typedArray.getColorStateList(R.styleable.CommonTitlebar_titlebar_lefttext_color) != null)
				mLeftTv.setTextColor(typedArray.getColorStateList(R.styleable.CommonTitlebar_titlebar_lefttext_color));
			if (typedArray.getColorStateList(R.styleable.CommonTitlebar_titlebar_centertext_color) != null)
				mCenterTv.setTextColor(typedArray.getColorStateList(R.styleable.CommonTitlebar_titlebar_centertext_color));
			if (typedArray.getColorStateList(R.styleable.CommonTitlebar_titlebar_righttext_color) != null)
				mRightTv.setTextColor(typedArray.getColorStateList(R.styleable.CommonTitlebar_titlebar_righttext_color));
			// 设置左右drawable和中间text的background
			if (typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_lefttext_drawable_left) != null) {
				Drawable leftDrawable = typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_lefttext_drawable_left);
				mLeftTv.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
			}
			//
			if (typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_centertext_background_drawable) != null)
				mCenterTv.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_centertext_background_drawable));
			//
			if (typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_righttext_drawable_right) != null) {
				Drawable rightDrawable = typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_righttext_drawable_right);
				mRightTv.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
			}
			//
			if (typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_righttext_drawable_left) != null) {
				Drawable leftDrawable = typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_righttext_drawable_left);
				mRightTv.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
			}
			//
			if (typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_line_background) != null)
				mLineV.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonTitlebar_titlebar_line_background));
			// 设置text显示隐藏
			mLeftTv.setVisibility(typedArray.getInt(R.styleable.CommonTitlebar_titlebar_lefttext_visibility, 0));
			mCenterTv.setVisibility(typedArray.getInt(R.styleable.CommonTitlebar_titlebar_centertext_visibility, 0));
			mRightTv.setVisibility(typedArray.getInt(R.styleable.CommonTitlebar_titlebar_righttext_visibility, 0));
			mLineV.setVisibility(typedArray.getInt(R.styleable.CommonTitlebar_titlebar_line_visibility, 0));
			typedArray.recycle();
		}

	}

	public LinearLayout getLinearLayout() {
		return mContainLi;
	}

	public TextView getLeftTextView() {
		return mLeftTv;
	}

	public TextView getCenterTextView() {
		return mCenterTv;
	}

	public TextView getRightTextView() {
		return mRightTv;
	}

	public void setLeftTextOnClickListener(OnClickListener onClickListener) {
		mLeftTv.setOnClickListener(onClickListener);
	}

	public void setRightTextOnClickListener(OnClickListener onClickListener) {
		mRightTv.setOnClickListener(onClickListener);
	}

	public void setCenterTextOnClickListener(OnClickListener onClickListener) {
		mCenterTv.setOnClickListener(onClickListener);
	}

	public void setAllText(String left, String center, String right) {
		mLeftTv.setText(left);
		mCenterTv.setText(center);
		mRightTv.setText(right);
	}

	public void setCenterText(String center) {
		mCenterTv.setText(center);
	}
}
