package com.hz.junxinbaoan.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.adapter.UseHelpRVAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.UseHelpBean;
import com.hz.junxinbaoan.params.BaseParam;
import com.hz.junxinbaoan.result.HelpListResult;
import com.hz.junxinbaoan.result.UserHelpListResult;
import com.hz.junxinbaoan.utils.CommonUtils;
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


/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class HelpActivity extends BaseActivity {
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.ptrframlayout)
    AnimPtrFrameLayout ptrframlayout;

    LinearLayoutManager linearLayoutManager;
    private int page = 1;
    private boolean isLoading,isMore;


    private UseHelpRVAdapter adapter;
    private List<UseHelpBean> datas;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    @Override
    protected void initViews() {
        super.initViews();

        adapter = new UseHelpRVAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);



        datas = new ArrayList<>();

        adapter.setDatas(datas);

    }

    @Override
    protected void addListeners() {
        super.addListeners();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ptrframlayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,recyclerView,header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getList(true);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == linearLayoutManager.getItemCount() - 1 && dy > 0 && isMore && !isLoading){
                    page++;
                    getList(false);
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
        getList(true);
    }

    private interface GetList {
        @FormUrlEncoded
        @POST(Constants.HELPLIST)
        Call<UserHelpListResult> getList(@FieldMap Map<String, Object> map);
    }
    private void getList(final boolean refresh) {
        showDialog(true);
        GetList getList = CommonUtils.buildRetrofit(Constants.BASE_URL,mBaseActivity).create(GetList.class);
        BaseParam param = new BaseParam();
        Call<UserHelpListResult> call = getList.getList(CommonUtils.getPostMap(param));
        call.enqueue(new Callback<UserHelpListResult>() {
            @Override
            public void onResponse(Call<UserHelpListResult> call, Response<UserHelpListResult> response) {
                showDialog(false);
                ptrframlayout.refreshComplete();
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<UserHelpListResult>() {
                    @Override
                    public void onSuccess(UserHelpListResult result) {
                        if (refresh){
                            datas.clear();
                        }
                        datas.addAll(result.getData());
                        isMore = datas.size() < result.getCount();
                        adapter.notifyDataSetChanged();
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
            public void onFailure(Call<UserHelpListResult> call, Throwable t) {
                showDialog(false);
                ptrframlayout.refreshComplete();
            }
        });
    }
}
