package com.hz.junxinbaoan.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseFragment;
import com.hz.junxinbaoan.adapter.MessageAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.PageTimeParams;
import com.hz.junxinbaoan.result.MessageResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.view.AnimPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * Created by linzp on 2017/10/23.
 */

public class MessageFragment extends BaseFragment {

    private ListView messageList;

    private List<MessageResult.MessageBean>list;
    private MessageAdapter messageAdapter;
    private int page=1;
    boolean haveMore = false;
    private AnimPtrFrameLayout ptr;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_message, container, false);
        messageList = (ListView) view.findViewById(R.id.message_list);
        ptr = (AnimPtrFrameLayout) view.findViewById(R.id.ptrframlayout);
        list=new ArrayList<>();
        messageAdapter=new MessageAdapter(getActivity(),list);
        messageList.setAdapter(messageAdapter);
        return view;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        getMessageListData();
    }
    //获取消息列表接口
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.MESSAGELIST)
        Call<MessageResult> getData(@FieldMap Map<String, Object> map);
    }
    //获取消息列表
    private void getMessageListData(){
        mBaseActivity.showDialog(true);haveMore=false;
        GetData getData = CommonUtils.buildRetrofit(Constants.BASE_URL, mBaseActivity).create(GetData.class);
        PageTimeParams params = new PageTimeParams();
        params.setAccess_token(MyApplication.mUserInfo.getAccess_token());
        params.setPageSize(20);
        params.setPageIndex(page);
        Call<MessageResult> call = getData.getData(CommonUtils.getPostMap(params));
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(final Call<MessageResult> call, final Response<MessageResult> response) {
                mBaseActivity.showDialog(false);ptr.refreshComplete();
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<MessageResult>() {
                    @Override
                    public void onSuccess(MessageResult result) {
                        Log.e("TAG MessageFragment :",result.getData().toString());
                        if (result!=null){
                            if (page == 1) {
                                list.clear();
                            }
                            list.addAll(result.getData());
                            messageAdapter.notifyDataSetChanged();
                            if (result.getCount()>20*page){
                                haveMore=true;
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
            public void onFailure(Call<MessageResult> call, Throwable t) {
                mBaseActivity.showDialog(false);ptr.refreshComplete();
                MyToast.showToast(mBaseActivity, "  网络连接失败，请稍后再试  ");
            }
        });
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame,messageList,header);
            }
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getMessageListData();
            }
        });
        messageList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("czx",firstVisibleItem+","+visibleItemCount+","+totalItemCount);
                if (firstVisibleItem+visibleItemCount>=totalItemCount && haveMore){
                    page++;
                    getMessageListData();
                }
            }
        });
    }

}
