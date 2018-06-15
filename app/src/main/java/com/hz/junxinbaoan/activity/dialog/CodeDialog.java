package com.hz.junxinbaoan.activity.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.common.Settings;
import com.hz.junxinbaoan.params.GetInfoParam;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.RetrofitCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static android.R.attr.order;

/**
 * Created by linzp on 2017/10/30.
 */

public class CodeDialog extends BaseDialog {
    RelativeLayout box;
    Animation animation;
    private ImageView code_iv;
    private FrameLayout refresh;
    LinearLayout ll;

    private String num;
    private TextView num_tv;


    public void setNum(String num) {
        this.num = num;
        num_tv.setText(num);
    }

    public CodeDialog(Context context) {
        super(context, R.style.dim_dialog);
        animation= AnimationUtils.loadAnimation(context, R.anim.showdialog);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.codedialog;
    }

    @Override
    public void show() {
        super.show();
//        box.startAnimation(animation);
        ll.startAnimation(animation);


    }



    @Override
    protected void findViews() {
        box= (RelativeLayout) findViewById(R.id.box);
        code_iv = ((ImageView) findViewById(R.id.code_iv));
        refresh = ((FrameLayout) findViewById(R.id.refresh));
        num_tv = ((TextView) findViewById(R.id.num_tv));
        ll = (LinearLayout) findViewById(R.id.ll);

        addListener();
        getCode();

    }

    private void addListener() {
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCode();
            }
        });
    }

    @Override
    protected void setWindowParam() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private interface GetCode {
        @FormUrlEncoded
        @POST(Constants.QRCODE)
        Call<ResponseBody> getCode(@FieldMap Map<String, Object> map);
    }

    private void getCode(){
        GetCode getCode = CommonUtils.buildRetrofit(Constants.BASE_URL,mContext).create(GetCode.class);
        GetInfoParam param = new GetInfoParam();
//        param.setEmployeeId(MyApplication.mUserInfo.getUserId());
        Call<ResponseBody> call = getCode.getCode(CommonUtils.getPostMap(param));
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file;
                    file = new File(Settings.PIC_PATH, new Date().getTime()+".jpg");
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    bis.close();
                    is.close();
                    try {
                        Glide.with(mContext).load(file).into(code_iv);
                    } catch (Exception ignore) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
