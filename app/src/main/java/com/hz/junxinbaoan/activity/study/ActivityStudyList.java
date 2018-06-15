package com.hz.junxinbaoan.activity.study;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.adapter.StudyAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.LearnParams;
import com.hz.junxinbaoan.result.StudyResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.view.AnimPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
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

public class ActivityStudyList extends BaseActivity {

    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.title_name)
    TextView titleName;
    @InjectView(R.id.date_fl)
    FrameLayout share;//分享
    @InjectView(R.id.title_lay)
    RelativeLayout titleLay;
    @InjectView(R.id.approval_list)
    ListView studyList;
    List<StudyResult.DataBean> list = new ArrayList<>();
    @InjectView(R.id.ptrframlayout)
    AnimPtrFrameLayout ptr;
    private StudyAdapter adapter;
    int page = 1;
    boolean haveMore = false;


    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_study_list);
        adapter = new StudyAdapter(list, mBaseActivity);
    }

    @Override
    protected void initViews() {
        super.initViews();
        studyList.setAdapter(adapter);
        getStudyData();
    }



    //获取消息列表接口
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.STUDYLIST)
        Call<StudyResult> getData(@FieldMap Map<String, Object> map);
    }

    //获取学习列表数据
    private void getStudyData() {
        showDialog(true);
        GetData getData = CommonUtils.buildRetrofit(Constants.BASE_URL, mBaseActivity).create(GetData.class);
        LearnParams params = new LearnParams();
        params.setPageSize(20);
        params.setPageIndex(page);
        params.setAccess_token(MyApplication.mUserInfo.getAccess_token());
        Call<StudyResult> call = getData.getData(CommonUtils.getPostMap(params));
        call.enqueue(new Callback<StudyResult>() {
            @Override
            public void onResponse(final Call<StudyResult> call, final Response<StudyResult> response) {
                showDialog(false);ptr.refreshComplete();
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<StudyResult>() {
                    @Override
                    public void onSuccess(StudyResult result) {
                        if (result != null) {
                            if (page == 1) {
                                list.clear();
                            }
                            list.addAll(result.getData());
                            adapter.notifyDataSetChanged();
                            if (result.getCount()==20){
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
            public void onFailure(Call<StudyResult> call, Throwable t) {
                if (loadingDialog.isShowing()) {
                    showDialog(false);
                    ptr.refreshComplete();
                }
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

        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,studyList,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getStudyData();
            }
        });
        studyList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem+visibleItemCount>=totalItemCount-1 && haveMore){
                    page++;
                    getStudyData();
                }
            }
        });



    }
}
