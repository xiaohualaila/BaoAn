package com.hz.junxinbaoan.activity.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.ForgetPasswordParams;
import com.hz.junxinbaoan.params.SendCodeParams;
import com.hz.junxinbaoan.result.ErrorResult;
import com.hz.junxinbaoan.result.VcodeResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.Commonutil;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ForgetPassword extends BaseActivity {

    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_lay)
    RelativeLayout titleLay;
    @BindView(R.id.icon_phone)
    ImageView iconPhone;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.icon_checkcode)
    ImageView iconCheckcode;
    @BindView(R.id.checkcode)
    EditText checkcode;
    @BindView(R.id.sendCheckcode)
    TextView sendCheckcode;
    @BindView(R.id.icon_password)
    ImageView iconPassword;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.seePassword)
    FrameLayout seePassword;
    @BindView(R.id.icon_password_again)
    ImageView iconPasswordAgain;
    @BindView(R.id.password_again)
    EditText passwordAgain;
    @BindView(R.id.seePassword_again)
    FrameLayout seePasswordAgain;
    @BindView(R.id.yes)
    TextView yes;

    private TimeCount myTimeCount = new TimeCount(60000, 1000);
    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    private boolean isPasswordType = true;
    private boolean isPasswordType_again = true;
    @Override
    protected void addListeners() {
        super.addListeners();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().length()>0){
                    isPasswordType = !isPasswordType;
//                pa.setImageResource(isPasswordType ? R.mipmap.login_watch_icon : R.mipmap.login_password_can_watch_icon);
                    password.setInputType(isPasswordType ? (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) : InputType
                            .TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
//                password.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.pwd_filter)));
            }
        });
        seePasswordAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordAgain.getText().length()>0){
                    isPasswordType_again = !isPasswordType_again;
//                pa.setImageResource(isPasswordType ? R.mipmap.login_watch_icon : R.mipmap.login_password_can_watch_icon);
                    passwordAgain.setInputType(isPasswordType_again ? (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) : InputType
                            .TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
//                passwordAgain.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.pwd_filter)));

            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRegister()){
                    //修改密码逻辑
                    forget();
                }
            }
        });


        sendCheckcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Commonutil.isMobileNum(phone.getText().toString())) {
                    sendCheckcode();
                } else {
                    MyToast.showToast(mBaseActivity, "手机号码输入有误");
                }
            }
        });
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            sendCheckcode.setEnabled(false);
            sendCheckcode.setText(  l / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            sendCheckcode.setEnabled(true);
            sendCheckcode.setText("获取验证码");
        }
    }

    //检查是否可以去注册
    private boolean checkRegister() {
        if (phone.getText().length()==0){
            MyToast.showToast(mBaseActivity,"请输入手机号");
            return false;
        }
        if (!Commonutil.isMobileNum(phone.getText().toString())){
            MyToast.showToast(mBaseActivity,"手机号码不正确");
            return false;
        }
        if (checkcode.getText().length()==0){
            MyToast.showToast(mBaseActivity,"请输入验证码");
            return false;
        }
        if (password.getText().length()==0){
            MyToast.showToast(mBaseActivity,"请输入密码");
            return false;
        }
        if (passwordAgain.getText().length()==0){
            MyToast.showToast(mBaseActivity,"请再次输入密码");
            return false;
        }
        if (!password.getText().toString().equals(passwordAgain.getText().toString())){
            MyToast.showToast(mBaseActivity,"两次输入密码不一样");
            return false;
        }
        return true;
    }

    //获取短信验证码
    private interface GetVcode {
        @FormUrlEncoded
        @POST(Constants.GetVcode)
        Call<VcodeResult> getData(@FieldMap Map<String, Object> map);
    }
    //发送验证码
    private void sendCheckcode() {
        showDialog(true);
        GetVcode getData = CommonUtils.buildRetrofit(Constants.BASE_URL, mBaseActivity).create(GetVcode.class);
        SendCodeParams params = new SendCodeParams();
        params.setPhone(phone.getText().toString());
        Call<VcodeResult> call = getData.getData(CommonUtils.getPostMap(params));
        call.enqueue(new Callback<VcodeResult>() {
            @Override
            public void onResponse(final Call<VcodeResult> call, final Response<VcodeResult> response) {
                showDialog(false);
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<VcodeResult>() {
                    @Override
                    public void onSuccess(VcodeResult result) {
                        if (result!=null){

                            MyToast.showToast(mBaseActivity, result.getData());
                            //开启倒计时
                            if (result.getCode().equals("0000")) {
                                if (myTimeCount != null) {
                                    myTimeCount.start();
                                }
                            }

                        }
                    }
                    @Override
                    public void onNetError() {
                    }
                    @Override
                    public void onError(String code, String message) {
                    }
                });
            }
            @Override
            public void onFailure(Call<VcodeResult> call, Throwable t) {
                showDialog(false);
                MyToast.showToast(mBaseActivity, "  网络连接失败，请稍后再试  ");
            }
        });
    }
    //修改密码
    private interface Forget {
        @FormUrlEncoded
        @POST(Constants.RESET)
        Call<ErrorResult> getData(@FieldMap Map<String, Object> map);
    }

    private void forget() {
        showDialog(true);
        Forget getData = CommonUtils.buildRetrofit(Constants.BASE_URL, mBaseActivity).create(Forget.class);
        ForgetPasswordParams params = new ForgetPasswordParams();
        params.setPasswordNew(password.getText().toString());
        params.setPasswordRep(passwordAgain.getText().toString());
        params.setPhone(phone.getText().toString());
        params.setCode(checkcode.getText().toString());
        Call<ErrorResult> call = getData.getData(CommonUtils.getPostMap(params));
        call.enqueue(new Callback<ErrorResult>() {
            @Override
            public void onResponse(final Call<ErrorResult> call, final Response<ErrorResult> response) {
                showDialog(false);
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<ErrorResult>() {
                    @Override
                    public void onSuccess(ErrorResult result) {
                        if (result!=null){
                            if (result.getCode().equals("0000")){
                                MyToast.showToast(mBaseActivity,result.getData());
                                finish();
                            }else {
                                MyToast.showToast(mBaseActivity,result.getData());
                            }
                        }
                    }
                    @Override
                    public void onNetError() {
                    }
                    @Override
                    public void onError(String code, String message) {
                    }
                });
            }
            @Override
            public void onFailure(Call<ErrorResult> call, Throwable t) {
                showDialog(false);
                MyToast.showToast(mBaseActivity, "  网络连接失败，请稍后再试  ");
            }
        });
    }

}
