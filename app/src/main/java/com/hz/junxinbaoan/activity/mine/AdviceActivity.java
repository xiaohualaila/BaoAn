package com.hz.junxinbaoan.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.AdviceParam;
import com.hz.junxinbaoan.result.ErrorResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import net.qiujuer.genius.ui.widget.Button;

import java.util.Map;

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

public class AdviceActivity extends BaseActivity {
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.content_et)
    EditText content_et;
    @BindView(R.id.text_num_tv)
    TextView text_num_tv;
    @BindView(R.id.commit_btn)
    Button commit_btn;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_advice);
    }

    @Override
    protected void addListeners() {
        super.addListeners();

        commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isEmpty(content_et.getText().toString().trim())){
                    MyToast.showToast(mBaseActivity,"请输入反馈内容");
                }else {
                    addSuggest();
                }
            }
        });
        content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Editable editable = content_et.getText();
                int len = editable.length();
                text_num_tv.setText(len+" / 120");

                if(len > 120) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0,120);
                    content_et.setText(newStr);
                    editable = content_et.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if(selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private interface AddSuggest {
        @FormUrlEncoded
        @POST(Constants.SUGGESTADD)
        Call<ErrorResult> addSuggest(@FieldMap Map<String, Object> map);
    }
    private void addSuggest() {
        showDialog(true);
        AddSuggest addSuggest = CommonUtils.buildRetrofit(Constants.BASE_URL,mBaseActivity).create(AddSuggest.class);
        AdviceParam param = new AdviceParam();
        param.setSuggestionContent(content_et.getText().toString());
        Call<ErrorResult> call = addSuggest.addSuggest(CommonUtils.getPostMap(param));
        call.enqueue(new Callback<ErrorResult>() {
            @Override
            public void onResponse(Call<ErrorResult> call, Response<ErrorResult> response) {
                showDialog(false);
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<ErrorResult>() {
                    @Override
                    public void onSuccess(ErrorResult result) {
                        if (result.getCode().equals("0000")){
                            MyToast.showToast(mBaseActivity,"提交成功，感谢您的宝贵意见");
                            finish();
                        }else if (result.getCode().equals("9999")){
                            MyToast.showToast(mBaseActivity,result.getData());
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
            }
        });

    }
}
