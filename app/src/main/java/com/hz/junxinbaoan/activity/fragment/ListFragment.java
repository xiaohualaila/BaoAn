package com.hz.junxinbaoan.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseFragment;
import com.hz.junxinbaoan.adapter.AtoZAdapter;
import com.hz.junxinbaoan.adapter.PeoPleListAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.PeopleParams;
import com.hz.junxinbaoan.result.ErrorResult;
import com.hz.junxinbaoan.result.PeopleResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.DensityUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.view.AnimPtrFrameLayout;
import com.hz.junxinbaoan.view.SideBar;

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

public class ListFragment extends BaseFragment {
    private ListView people_list;
    private SideBar a_to_z;
    private AtoZAdapter atoZAdapter;
    private PeoPleListAdapter peoPleListAdapter;
    private List<String> a_to_z_data = new ArrayList<>();
    private List<PeopleResult.PeopleBean> peopleBeanList = new ArrayList<>();
    private String s;
    private AnimPtrFrameLayout ptr;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate( R.layout.fragment_list, container, false );
        people_list = (ListView) view.findViewById( R.id.people_list );
        a_to_z = (SideBar) view.findViewById( R.id.a_to_z );
        atoZAdapter = new AtoZAdapter( getActivity(), a_to_z_data );
        peoPleListAdapter = new PeoPleListAdapter( getActivity(), peopleBeanList);
        ptr = (AnimPtrFrameLayout) view.findViewById( R.id.ptrframlayout );
        return view;
    }

    @Override
    protected void initViews(View view) {
        super.initViews( view );
//        a_to_z.setAdapter(atoZAdapter);
        getUserInfo2();
        people_list.setAdapter( peoPleListAdapter );
        getPeopleListData( null );
    }


    public void serch(String str) {
        getPeopleListData( str );
        s = str;
    }

    public void serch(CharSequence c) {
        s = c.toString();
        getPeopleListData( s );
    }

    //获取员工列表接口
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.EMPLOYEELIST)
        Call<PeopleResult> getData(@FieldMap Map<String, Object> map);
    }

    //获取员工列表
    private void getPeopleListData(final String str) {
//        mBaseActivity.showDialog(true);
        GetData getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetData.class );
        PeopleParams params = new PeopleParams();
        params.setSearch( str );
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        Call<PeopleResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<PeopleResult>() {
            @Override
            public void onResponse(final Call<PeopleResult> call, final Response<PeopleResult> response) {
//                mBaseActivity.showDialog(false);
                ptr.refreshComplete();
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                        .OnHandleListener<PeopleResult>() {
                    @Override
                    public void onSuccess(PeopleResult result) {
                        if (result != null) {
                            peopleBeanList.clear();//清楚元数据

                            if (str != null && !str.equals( "" )) {//搜索
                                a_to_z.setVisibility( View.GONE );
                            } else {
                                a_to_z.setVisibility( View.VISIBLE );
                            }
                            peopleBeanList.addAll( result.getData() );
                            doList();//对管理层的操作，排序
                            peoPleListAdapter.notifyDataSetChanged();
                            doAtoz();
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
            public void onFailure(Call<PeopleResult> call, Throwable t) {
//                mBaseActivity.showDialog(false);
                ptr.refreshComplete();
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }

    private void doList() {
        List<PeopleResult.PeopleBean> guan = new ArrayList<>();
        List<PeopleResult.PeopleBean> noguan = new ArrayList<>();
        for (int i = 0; i < peopleBeanList.size(); i++) {
            if (peopleBeanList.get( i ).getEmployeeIsAdmin() == 1) {
                peopleBeanList.get( i ).setEmployeeNameFirstCharInPinYin( "管" );
                guan.add( peopleBeanList.get( i ) );
            } else {
                noguan.add( peopleBeanList.get( i ) );
            }
        }
        peopleBeanList.clear();
        peopleBeanList.addAll( guan );
        peopleBeanList.addAll( noguan );
    }


    private void doAtoz() {
        a_to_z_data.clear();
        for (int i = 0; i < peopleBeanList.size(); i++) {
            if (i == 0) {
                a_to_z_data.add( peopleBeanList.get( i ).getEmployeeNameFirstCharInPinYin() );
            }
            if (i > 0 && !peopleBeanList.get( i ).getEmployeeNameFirstCharInPinYin().equals( peopleBeanList.get( i -
                    1 ).getEmployeeNameFirstCharInPinYin() ))
                a_to_z_data.add( peopleBeanList.get( i ).getEmployeeNameFirstCharInPinYin() );
        }
        String[] b = new String[a_to_z_data.size()];
        for (int i = 0; i < a_to_z_data.size(); i++) {
            b[i] = a_to_z_data.get( i );
        }
        a_to_z.b = b;
        ViewGroup.LayoutParams params = a_to_z.getLayoutParams();
        params.height = DensityUtils.dp2px( getActivity(), 18 ) * a_to_z_data.size() + DensityUtils.dp2px(
                getActivity(), 18 );
        a_to_z.setLayoutParams( params );

    }

    @Override
    protected void addListeners() {
        super.addListeners();
        a_to_z.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //滑动监听
                return false;
            }
        } );

        ptr.setPtrHandler( new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown( frame, people_list, header );
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                getPeopleListData( s );
            }
        } );
        a_to_z.setOnTouchingLetterChangedListener( new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                for (int i = 0; i < peopleBeanList.size(); i++) {
                    if (peopleBeanList.get( i ).getEmployeeNameFirstCharInPinYin().equals( s )) {
                        people_list.setSelection( i );
                        break;
                    }
                }
            }
        } );
    }

    //检测用户状态
    private interface GetData2 {
        @FormUrlEncoded
        @POST(Constants.EMPLOYEELIST)
        Call<ErrorResult> getData(@FieldMap Map<String, Object> map);
    }

    //检测用户状态
    private void getUserInfo2() {
        final GetData2 getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create(
                GetData2.class );
        PeopleParams params = new PeopleParams();
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        Call<ErrorResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<ErrorResult>() {
            @Override
            public void onResponse(final Call<ErrorResult> call, final Response<ErrorResult> response) {
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler.OnHandleListener<ErrorResult>
                        () {
                    @Override
                    public void onSuccess(ErrorResult result) {
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
            }
        } );
    }

}
