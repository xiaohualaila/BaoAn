package com.hz.junxinbaoan.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codbking.calendar.CalendarBean;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.dialog.ApprovalTypeDialog;
import com.hz.junxinbaoan.activity.dialog.ChooseDateDialog;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.BaseParam;
import com.hz.junxinbaoan.params.CommitReasonParams;
import com.hz.junxinbaoan.result.ApprovalTypeResult;
import com.hz.junxinbaoan.result.ErrorResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import net.qiujuer.genius.ui.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ActivityApprovalCommit extends BaseActivity {
    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.title_name)
    TextView titleName;
    @InjectView(R.id.list_fl)
    FrameLayout listFl;
    @InjectView(R.id.title_lay)
    RelativeLayout titleLay;
    @InjectView(R.id.type_tv)
    TextView typeTv;
    @InjectView(R.id.type_et)
    TextView typeEt;
    @InjectView(R.id.start_tv)
    TextView startTv;
    @InjectView(R.id.start_et)
    TextView startEt;
    @InjectView(R.id.end_tv)
    TextView endTv;
    @InjectView(R.id.end_et)
    TextView endEt;
    @InjectView(R.id.days_tv)
    TextView daysTv;
    @InjectView(R.id.days_et)
    EditText daysEt;
    @InjectView(R.id.reason_tv)
    TextView reasonTv;
    @InjectView(R.id.reason_et)
    EditText reasonEt;
    @InjectView(R.id.commit_btn)
    Button commit_btn;

    private ChooseDateDialog dialog_start;
    private ChooseDateDialog dialog_end;
    private CalendarBean calendarBean_start;
    private CalendarBean calendarBean_end;
    ApprovalTypeDialog dialog;

    List<String> listType = new ArrayList<>();

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_approval_commit );
        dialog_start = new ChooseDateDialog( mBaseActivity, new ChooseDateDialog.OnClickOkListener() {
            @Override
            public void onClickOk(CalendarBean calendarBean) {
                //判断是否大于今天，是否大于开始时间
                Calendar calendar = new GregorianCalendar();

                if (calendar.get( Calendar.YEAR ) > calendarBean.year) {
                    MyToast.showToast( mBaseActivity, "开始时间需晚于当前时间" );
                    return;
                } else if (calendar.get( Calendar.YEAR ) == calendarBean.year && calendar.get( Calendar.MONTH ) + 1 >
                        calendarBean.moth) {
                    MyToast.showToast( mBaseActivity, "开始时间需晚于当前时间" );
                    return;
                } else if (calendar.get( Calendar.MONTH ) + 1 == calendarBean.moth && calendar.get( Calendar.DATE ) >
                        calendarBean.day) {
                    MyToast.showToast( mBaseActivity, "开始时间需晚于当前时间" );
                    return;
                }


                if (calendarBean_end != null) {//判断是否大于今天
                    if (calendarBean_end.year < calendarBean.year) {
                        MyToast.showToast( mBaseActivity, "开始时间需早于结束时间" );
                        return;
                    } else if (calendarBean_end.year == calendarBean.year && calendarBean_end.moth < calendarBean
                            .moth) {
                        MyToast.showToast( mBaseActivity, "开始时间需早于结束时间" );
                        return;
                    } else if (calendarBean_end.moth == calendarBean.moth && calendarBean_end.day < calendarBean.day) {
                        MyToast.showToast( mBaseActivity, "开始时间需早于结束时间" );
                        return;
                    }
                } else {//是否大于开始时间

                }
                calendarBean_start = calendarBean;
                dialog_start.dismiss();
                String moth = calendarBean_start.moth < 10 ? "0" + calendarBean_start.moth : "" + calendarBean_start
                        .moth;
                if (calendarBean_start.day >= 10) {
                    startEt.setText( calendarBean_start.year + "-" + moth + "-" + calendarBean_start.day + "" );
                } else {
                    startEt.setText( calendarBean_start.year + "-" + moth + "-0" + calendarBean_start.day + "" );
                }

            }
        } );
        dialog_end = new ChooseDateDialog( mBaseActivity, new ChooseDateDialog.OnClickOkListener() {
            @Override
            public void onClickOk(CalendarBean calendarBean) {

                //判断是否大于今天，是否大于开始时间
                if (calendarBean_start != null) {//判断是否大于今天
                    if (calendarBean_start.year > calendarBean.year) {
                        MyToast.showToast( mBaseActivity, "结束时间需晚于开始时间" );
                        return;
                    } else if (calendarBean_start.year == calendarBean.year && calendarBean_start.moth > calendarBean
                            .moth) {
                        MyToast.showToast( mBaseActivity, "结束时间需晚于开始时间" );
                        return;
                    } else if (calendarBean_start.moth == calendarBean.moth && calendarBean_start.day > calendarBean
                            .day) {
                        MyToast.showToast( mBaseActivity, "结束时间需晚于开始时间" );
                        return;
                    }

                } else {//是否大于开始时间
                    Calendar calendar = new GregorianCalendar();

                    if (calendar.get( Calendar.YEAR ) > calendarBean.year) {
                        MyToast.showToast( mBaseActivity, "结束时间需晚于当前时间" );
                        return;
                    } else if (calendar.get( Calendar.YEAR ) == calendarBean.year && calendar.get( Calendar.MONTH ) +
                            1 > calendarBean.moth) {
                        MyToast.showToast( mBaseActivity, "结束时间需晚于当前时间" );
                        return;
                    } else if (calendar.get( Calendar.MONTH ) + 1 == calendarBean.moth && calendar.get( Calendar.DATE
                    ) + 1 > calendarBean.day) {
                        MyToast.showToast( mBaseActivity, "结束时间需晚于当前时间" );
                        return;
                    }
                }
                calendarBean_end = calendarBean;
                dialog_end.dismiss();
                String moth = calendarBean_end.moth < 10 ? "0" + calendarBean_end.moth : "" + calendarBean_end.moth;
                if (calendarBean_end.day < 10) {
                    endEt.setText( calendarBean_end.year + "-" + moth + "-0" + calendarBean_end.day + "" );
                } else {
                    endEt.setText( calendarBean_end.year + "-" + moth + "-" + calendarBean_end.day + "" );
                }

            }
        } );
    }


    @Override
    protected void addListeners() {
        super.addListeners();
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
        startEt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dialog_start.isShowing()){
                    dialog_start.show();
                }

            }
        } );
        endEt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_end.show();
            }
        } );
        typeEt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null)
                    dialog.show();
            }
        } );
        commit_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeEt.getText().length() == 0) {
                    MyToast.showToast( mBaseActivity, "请选择类型" );
                    return;
                }
                if (startEt.getText().length() == 0) {
                    MyToast.showToast( mBaseActivity, "请选择开始时间" );
                    return;
                }
                if (endEt.getText().length() == 0) {
                    MyToast.showToast( mBaseActivity, "请选择结束时间" );
                    return;
                }
//                if (daysEt.getText().length()==0){
//                    MyToast.showToast(mBaseActivity,"请输入请假天数");
//                    return;
//                }
                if (reasonEt.getText().toString().trim().length() == 0) {
                    MyToast.showToast( mBaseActivity, "请输入请假事由" );
                    return;
                }

                commitReason();


            }
        } );
        listFl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mBaseActivity, ActivityApprovalList.class );
                startActivity( intent );
            }
        } );

        //监听软键盘的enter确定键,不换行隐藏软键盘
        reasonEt.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE || (event !=
                        null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event
                        .getAction())) {
                    //处理事件
                    closeKeyboard( mBaseActivity );
                    return true;
                }
                return false;
            }
        } );


    }


    //提交请假原因
    private interface CommitReason {
        @FormUrlEncoded
        @POST(Constants.ADDREASON)
        Call<ErrorResult> getData(@FieldMap Map<String, Object> map);
    }

    private void commitReason() {
        CommitReason getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( CommitReason
                .class );
        CommitReasonParams params = new CommitReasonParams();
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        params.setRequestReason( reasonEt.getText().toString() );
        params.setRequestType( typeEt.getText().toString() );
//       日期段传  timeFrom=yyyy-MM-dd&timeTo=yyyy-MM-dd
        params.setRequestBeginTime( calendarBean_start.getParams() );
        params.setRequestEndTime( calendarBean_end.getParams() );
        Call<ErrorResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<ErrorResult>() {
            @Override
            public void onResponse(final Call<ErrorResult> call, final Response<ErrorResult> response) {
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler.OnHandleListener<ErrorResult>
                        () {
                    @Override
                    public void onSuccess(ErrorResult result) {
                        if (result != null) {
                            MyToast.showToast( mBaseActivity, "  提交成功，请耐心等待审核！  " );
                            Intent intent = new Intent( mBaseActivity, ActivityApprovalList.class );
                            startActivity( intent );
                            finish();
                        }
                    }

                    @Override
                    public void onNetError() {
                    }

                    @Override
                    public void onError(String code, String message) {
                    }
                } );
            }

            @Override
            public void onFailure(Call<ErrorResult> call, Throwable t) {
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }

    @Override
    protected void initViews() {
        super.initViews();
        getType();
    }

    //获取请假类型
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.TYPELIST)
        Call<ApprovalTypeResult> getData(@FieldMap Map<String, Object> map);
    }

    private void getType() {
        GetData getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetData.class );
        BaseParam params = new BaseParam();
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        Call<ApprovalTypeResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<ApprovalTypeResult>() {
            @Override
            public void onResponse(final Call<ApprovalTypeResult> call, final Response<ApprovalTypeResult> response) {
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                        .OnHandleListener<ApprovalTypeResult>() {
                    @Override
                    public void onSuccess(ApprovalTypeResult result) {
                        if (result != null) {
                            listType = result.getData();
                            dialog = new ApprovalTypeDialog( mBaseActivity, listType, new ApprovalTypeDialog
                                    .GetString() {
                                @Override
                                public void getString(String s) {
                                    typeEt.setText( s );
                                }
                            } );
                        }
                    }

                    @Override
                    public void onNetError() {
                    }

                    @Override
                    public void onError(String code, String message) {
                    }
                } );
            }

            @Override
            public void onFailure(Call<ApprovalTypeResult> call, Throwable t) {
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }
}
