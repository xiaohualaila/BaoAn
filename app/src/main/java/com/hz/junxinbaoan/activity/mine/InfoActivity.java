package com.hz.junxinbaoan.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.dialog.CodeDialog;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.GetInfoBean;
import com.hz.junxinbaoan.params.BaseParam;
import com.hz.junxinbaoan.params.GetInfoParam;
import com.hz.junxinbaoan.params.UserParams;
import com.hz.junxinbaoan.result.GetInfoResult;
import com.hz.junxinbaoan.result.UserInfoResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ImageViewPlus;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import java.io.File;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class InfoActivity extends BaseActivity {
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.code_iv)
    ImageView code_iv;
    @BindView(R.id.ava_iv)
    ImageViewPlus ava_iv;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.sex_tv)
    TextView sex_tv;
    @BindView(R.id.birthday_tv)
    TextView birthday_tv;
    @BindView(R.id.num_tv)
    TextView num_tv;
    @BindView(R.id.add_tv)
    TextView add_tv;
    @BindView(R.id.id_tv)
    TextView id_tv;
    @BindView(R.id.phone_tv)
    TextView phone_tv;
    @BindView(R.id.company_tv)
    TextView company_tv;
    @BindView(R.id.liveadd_tv)
    TextView liveadd_tv;

    CodeDialog codeDialog;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.list_fl)
    FrameLayout listFl;
    @BindView(R.id.title_lay)
    RelativeLayout titleLay;
    @BindView(R.id.info_text)
    TextView infoText;
    @BindView(R.id.name_text)
    TextView nameText;
    @BindView(R.id.sex_text)
    TextView sexText;
    @BindView(R.id.birthday_text)
    TextView birthdayText;
    @BindView(R.id.num_text)
    TextView numText;
    @BindView(R.id.add_text)
    TextView addText;
    @BindView(R.id.phone_text)
    TextView phoneText;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.company_text)
    TextView companyText;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.liveadd_text)
    TextView liveaddText;
    @BindView(R.id.line3)
    View line3;

    @BindView(R.id.firstname)
    TextView firstname;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    @Override
    protected void initViews() {
        super.initViews();
        codeDialog = new CodeDialog(mBaseActivity);

    }

    @Override
    protected void addListeners() {
        super.addListeners();

        listFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mBaseActivity,ChangeMineInfo.class));
            }
        });
        code_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                codeDialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
        getUserInfo();
    }

    private interface GetInfo {
        @FormUrlEncoded
        @POST(Constants.CERTIFICATE)
        Call<GetInfoResult> getInfo(@FieldMap Map<String, Object> map);
    }

    private void getInfo() {
        GetInfo getInfo = CommonUtils.buildRetrofit(Constants.BASE_URL, mBaseActivity).create(GetInfo.class);
        GetInfoParam param = new GetInfoParam();
//        param.setEmployeeId(MyApplication.mUserInfo.getUserId());
        Call<GetInfoResult> call = getInfo.getInfo(CommonUtils.getPostMap(param));
        call.enqueue(new Callback<GetInfoResult>() {
            @Override
            public void onResponse(Call<GetInfoResult> call, Response<GetInfoResult> response) {
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<GetInfoResult>() {
                    @Override
                    public void onSuccess(GetInfoResult result) {
                        updateUI(result.getData());
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
            public void onFailure(Call<GetInfoResult> call, Throwable t) {

            }
        });
    }

    private void updateUI(GetInfoBean bean) {
        if (!CommonUtils.isEmpty(bean.getCertificateName())) {
            name_tv.setText(bean.getCertificateName());
        }
        if (!CommonUtils.isEmpty(bean.getCertificateSex())) {
            sex_tv.setText(bean.getCertificateSex());
        }
        if (!CommonUtils.isEmpty(bean.getCertificateBirthday())) {
            birthday_tv.setText(bean.getCertificateBirthday());
        }
        if (!CommonUtils.isEmpty(bean.getCertificateNo())) {
            num_tv.setText(bean.getCertificateNo());
            codeDialog.setNum(bean.getCertificateNo());
        }
        if (!CommonUtils.isEmpty(bean.getCertificateResidenceAddress())) {
            add_tv.setText(bean.getCertificateResidenceAddress());
        }
        String certificateIdCard = bean.getCertificateIdCard();
        if (!CommonUtils.isEmpty(certificateIdCard)) {
//            phone_tv.setText(certificateIdCard);
            char[] idNoArray = certificateIdCard.toCharArray();
            String idNum = "";
            for (int i = 0; i < idNoArray.length; i++) {
                if (i % 4 == 0 && i > 0) {
                    idNum += " ";
                }
                idNum += idNoArray[i];
            }
            id_tv.setText(idNum);
        }
        String certificatePhone = bean.getCertificatePhone();
        if (!CommonUtils.isEmpty(certificatePhone)) {
//            phone_tv.setText(certificatePhone);
            char[] phoneArray = certificatePhone.toCharArray();
            String phoneNum = "";
            for(int i=0;i<phoneArray.length;i++){
                if(i == 3 || i == 7){
                    phoneNum+=" ";
                }
                phoneNum += phoneArray[i];
            }
            phone_tv.setText(phoneNum);
        }
        if (!CommonUtils.isEmpty(bean.getCertificateCompanyName())) {
            company_tv.setText(bean.getCertificateCompanyName());
        }
        if (!CommonUtils.isEmpty(bean.getCertificateLivingAddress())) {
            liveadd_tv.setText(bean.getCertificateLivingAddress());
        }
        if (!CommonUtils.isEmpty(bean.getCertificatePhoto())) {
            firstname.setText("");
            try {
                Glide.with(mBaseActivity).load(bean.getCertificatePhoto()).asBitmap()
                        .centerCrop().error(R.mipmap.info_icon).placeholder(R.mipmap.info_icon).into(ava_iv);
            } catch (Exception ignore) {
            }
//            CommonUtils.loadPic(mBaseActivity, bean.getCertificatePhoto(), new CommonUtils.LoadPic() {
//                @Override
//                public void loadPic(byte[] bytes) {
//                    try {
//                        Glide.with(mBaseActivity).load(bytes).asBitmap()
//                                .centerCrop().error(R.mipmap.info_icon).placeholder(R.mipmap.info_icon).into(ava_iv);
//                    } catch (Exception ignore) {
//                    }
//                }
//            });
        }else {
            firstname.setText(bean.getCertificateName().charAt(bean.getCertificateName().length()-1)+"");
            ava_iv.setImageResource(R.color.picback);
        }
    }

    //获取个人信息接口
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.GETUSERINFO)
        Call<UserInfoResult> getData(@FieldMap Map<String, Object> map);
    }
    //获取个人信息
    private void getUserInfo() {
        showDialog(true);
        GetData getData = CommonUtils.buildRetrofit(Constants.BASE_URL, mBaseActivity).create(GetData.class);
        UserParams params = new UserParams();
        params.setPushId(CommonUtils.getPushId());
        params.setAccess_token(MyApplication.mUserInfo.getAccess_token());
        Call<UserInfoResult> call = getData.getData(CommonUtils.getPostMap(params));
        call.enqueue(new Callback<UserInfoResult>() {
            @Override
            public void onResponse(final Call<UserInfoResult> call, final Response<UserInfoResult> response) {
                showDialog(false);
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<UserInfoResult>() {
                    @Override
                    public void onSuccess(UserInfoResult result) {
                        if (result.getData() != null) {
                            MyApplication.mUserInfo.saveUserInfo(result);
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
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
                showDialog(false);
                MyToast.showToast(mBaseActivity, "  网络连接失败，请稍后再试  ");
            }
        });
    }

}
