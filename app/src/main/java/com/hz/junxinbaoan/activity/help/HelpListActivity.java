package com.hz.junxinbaoan.activity.help;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.codbking.calendar.CalendarBean;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.dialog.ChooseDateDialog;
import com.hz.junxinbaoan.adapter.HelpListRVAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.HelpListBean;
import com.hz.junxinbaoan.params.ReportListParam;
import com.hz.junxinbaoan.result.HelpListResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.view.AnimPtrFrameLayout;

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

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class HelpListActivity extends BaseActivity {
    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.date_fl)
    FrameLayout date_fl;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerview;
    @InjectView(R.id.ptrframlayout)
    AnimPtrFrameLayout ptrframlayout;

    private HelpListRVAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private int page = 1;
    private boolean isLoading,isMore;

    private CalendarBean curDate;

    private List<HelpListBean> data;

    ChooseDateDialog dialog;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_help_list);


    }

    @Override
    protected void initViews() {
        super.initViews();

//        final Calendar calendar = Calendar.getInstance();
//        curDate = new CalendarBean(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DATE));
        dialog = new ChooseDateDialog(mBaseActivity, new ChooseDateDialog.OnClickOkListener() {
            @Override
            public void onClickOk(CalendarBean calendarBean) {
                curDate = calendarBean;
                page = 1;
                getHelpList(true);
                dialog.dismiss();
            }
        });
        linearLayoutManager = new LinearLayoutManager(mBaseActivity);
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new HelpListRVAdapter(this);
        recyclerview.setAdapter(adapter);
    }

    @Override
    protected void addListeners() {
        super.addListeners();

        date_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ptrframlayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,recyclerview,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getHelpList(true);
            }
        });

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == linearLayoutManager.getItemCount() - 1 && dy > 0 && isMore && !isLoading){
                    page++;
                    getHelpList(false);
                }
            }
        });
    }

    @Override
    protected void requestOnCreate() {
        super.requestOnCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getHelpList(true);
    }

    //求助爆料列表
    private interface GetList {
        @FormUrlEncoded
        @POST(Constants.REPORTLIST)
        Call<HelpListResult> getList(@FieldMap Map<String, Object> map);
    }

    private void getHelpList(final boolean refresh) {
        showDialog(true);
        isLoading = true;
        GetList getList = CommonUtils.buildRetrofit(Constants.BASE_URL,mBaseActivity).create(GetList.class);
        ReportListParam param = new ReportListParam();
        param.setPageIndex(page);
        if (curDate != null)
            param.setTime(curDate.year+"-"+curDate.moth+"-"+curDate.day);
        param.setPageSize(10);
        Call<HelpListResult> call = getList.getList(CommonUtils.getPostMap(param));
        call.enqueue(new Callback<HelpListResult>() {
            @Override
            public void onResponse(Call<HelpListResult> call, Response<HelpListResult> response) {
                ptrframlayout.refreshComplete();
                showDialog(false);
                isLoading = false;
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<HelpListResult>() {
                    @Override
                    public void onSuccess(HelpListResult result) {
                        if (refresh){
                            data = result.getData();
                        }else {
                            data.addAll(result.getData());
                        }
                        isMore = data.size() < result.getCount();
                        adapter.setData(data);
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
            public void onFailure(Call<HelpListResult> call, Throwable t) {
                ptrframlayout.refreshComplete();
                showDialog(false);
                isLoading = false;
            }
        });
    }
}
