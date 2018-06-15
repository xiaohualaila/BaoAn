package com.hz.junxinbaoan.activity.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.dialog.LoadingDialog;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.RegisterParams;
import com.hz.junxinbaoan.params.SendCodeParams;
import com.hz.junxinbaoan.result.ErrorResult;
import com.hz.junxinbaoan.result.VcodeResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.Commonutil;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import java.util.Map;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RegisterActivity extends BaseActivity {

    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.title_name)
    TextView titleName;
    @InjectView(R.id.title_lay)
    RelativeLayout titleLay;
    @InjectView(R.id.icon_mine)
    ImageView iconMine;
    @InjectView(R.id.name)
    EditText name;
    @InjectView(R.id.icon_idcard)
    ImageView iconIdcard;
    @InjectView(R.id.idcard)
    EditText idcard;
    @InjectView(R.id.icon_phone)
    ImageView iconPhone;
    @InjectView(R.id.phone)
    EditText phone;
    @InjectView(R.id.icon_checkcode)
    ImageView iconCheckcode;
    @InjectView(R.id.checkcode)
    EditText checkcode;
    @InjectView(R.id.sendCheckcode)
    TextView sendCheckcode;
    @InjectView(R.id.icon_password)
    ImageView iconPassword;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.seePassword)
    FrameLayout seePassword;
    @InjectView(R.id.login)
    TextView login;
//    @InjectView(R.id.forget)
//    TextView forget;
    @InjectView(R.id.to_register)
    TextView toRegister;

    private TimeCount myTimeCount = new TimeCount(60000, 1000);
    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadingDialog=new LoadingDialog(mBaseActivity);
    }
    private boolean isPasswordType = true;
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
        
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRegister()){
                    //注册逻辑
                    register();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
//        forget.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mBaseActivity,ForgetPassword.class));
//            }
//        });
    }
//    userName	姓名	是	code	0000-成功，9999-失败
//    userIdCard	身份证号	是	data	数据/错误信息
//    userPhone	手机号	是
//    code	验证码	是
//    userPassword	密码	是

    //注册
    private interface Register {
        @FormUrlEncoded
        @POST(Constants.REGISTER)
        Call<ErrorResult> getData(@FieldMap Map<String, Object> map);
    }

    private void register() {
        showDialog(true);
        final Register getData = CommonUtils.buildRetrofit(Constants.BASE_URL, mBaseActivity).create(Register.class);
        final RegisterParams params = new RegisterParams();
        params.setUserName(name.getText().toString());
        params.setUserIdCard(idcard.getText().toString());
        params.setUserPhone(phone.getText().toString());
        params.setCode(checkcode.getText().toString());
        params.setUserPassword(password.getText().toString());
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
                                manService.getMANAnalytics().userRegister(params.getUserName());//注册埋点
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
                            if (result.getCode().equals("0000")){
                                //开启倒计时
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
        if (name.getText().length()==0){
            MyToast.showToast(mBaseActivity,"请输入姓名");
            return false;
        }
        if (idcard.getText().length()==0){
            MyToast.showToast(mBaseActivity,"请输入身份证号码");
            return false;
        }
//        if (!IdcardValidator.isValidatedAllIdcard(idcard.getText().toString())){
//            MyToast.showToast(mBaseActivity,"身份证号码不正确");
//            return false;
//        }
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
//        if (password.getText().length()<6){
//            MyToast.showToast(mBaseActivity,"密码长度至少6位");
//            return false;
//        }
        return true;
    }
}
