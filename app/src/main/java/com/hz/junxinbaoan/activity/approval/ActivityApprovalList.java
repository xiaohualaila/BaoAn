package com.hz.junxinbaoan.activity.approval;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codbking.calendar.CalendarBean;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.dialog.ChooseDateDialog;
import com.hz.junxinbaoan.adapter.ApprovalListAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.PageTimeParams;
import com.hz.junxinbaoan.result.RequestResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.view.AnimPtrFrameLayout;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ActivityApprovalList extends BaseActivity {

    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.title_name)
    TextView titleName;
    @InjectView(R.id.date_fl)
    FrameLayout dateFl;
    @InjectView(R.id.title_lay)
    RelativeLayout titleLay;
    @InjectView(R.id.approval_list)
    ListView approvalList;
    private List<RequestResult.DataBean> list;
    private ApprovalListAdapter approvalListAdapter;
    private ChooseDateDialog dialog_rili;
    @InjectView(R.id.ptrframlayout)
    AnimPtrFrameLayout ptr;
    CalendarBean mCalendarBean;
    int page = 1;
    boolean haveMore = false;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_approval_list);
        list=new ArrayList<>();
        approvalListAdapter=new ApprovalListAdapter(mBaseActivity,list);
//        approvalListAdapter.setRefresh(new ApprovalListAdapter.Refresh() {
//            @Override
//            public void refresh() {
//                page=1;
//                getApprovalListData();
//            }
//        });
        dialog_rili=new ChooseDateDialog(mBaseActivity, new ChooseDateDialog.OnClickOkListener() {
            @Override
            public void onClickOk(CalendarBean calendarBean) {
                //回调日期~\
                mCalendarBean=calendarBean;
                dialog_rili.dismiss();
                getApprovalListData();
            }
        });
    }

    @Override
    protected void initViews() {
        super.initViews();
        approvalList.setAdapter(approvalListAdapter);
        getApprovalListData();
    }

    //获取请假列表接口
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.REQUESTLIST)
        Call<RequestResult> getData(@FieldMap Map<String, Object> map);
    }
    private void getApprovalListData() {
        showDialog(true);haveMore=false;
        GetData getData = CommonUtils.buildRetrofit(Constants.BASE_URL, mBaseActivity).create(GetData.class);
        PageTimeParams params = new PageTimeParams();
//        params.setStatus("0");
        params.setAccess_token(MyApplication.mUserInfo.getAccess_token());
        params.setPageSize(20);
        params.setPageIndex(page);
        if (mCalendarBean!=null){
            params.setTime(mCalendarBean.getParams());
        }
        Call<RequestResult> call = getData.getData(CommonUtils.getPostMap(params));
        call.enqueue(new Callback<RequestResult>() {
            @Override
            public void onResponse(final Call<RequestResult> call, final Response<RequestResult> response) {
                showDialog(false);ptr.refreshComplete();
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<RequestResult>() {
                    @Override
                    public void onSuccess(RequestResult result) {
                        if (result != null) {
                            if (page == 1) {
                                list.clear();
                            }
                            list.addAll(result.getData());
                            approvalListAdapter.notifyDataSetChanged();
                            if (result.getCount()>20*page){
                                haveMore=true;
                            }
                        }
                    }
                    @Override
                    public void onNetError() {
                        showDialog(false);
                    }
                    @Override
                    public void onError(String code, String message) {
                        showDialog(false);
                    }
                });
            }
            @Override
            public void onFailure(Call<RequestResult> call, Throwable t) {
                showDialog(false);
                ptr.refreshComplete();
                MyToast.showToast(mBaseActivity, "  网络连接失败，请稍后再试  ");
            }
        });
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dateFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_rili.show();
            }
        });

        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,approvalList,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getApprovalListData();
            }
        });
        approvalList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("czx",firstVisibleItem+","+visibleItemCount+","+totalItemCount);
                if (firstVisibleItem+visibleItemCount>=totalItemCount && haveMore){
                    page++;
                    getApprovalListData();
                }
            }
        });

    }
}
