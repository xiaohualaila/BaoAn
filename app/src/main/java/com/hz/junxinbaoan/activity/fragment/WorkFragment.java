package com.hz.junxinbaoan.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseFragment;
import com.hz.junxinbaoan.activity.sign.SignActivity;
import com.hz.junxinbaoan.adapter.SquareAdapter;
import com.hz.junxinbaoan.adapter.WorkTimeAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.SignTopBean;
import com.hz.junxinbaoan.data.SquareBean;
import com.hz.junxinbaoan.params.BaseParam;
import com.hz.junxinbaoan.params.XYParams;
import com.hz.junxinbaoan.result.BaseResult;
import com.hz.junxinbaoan.result.GetStatisticsResult;
import com.hz.junxinbaoan.result.MainPageResult;
import com.hz.junxinbaoan.result.RolePermissionResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.view.AnimPtrFrameLayout;
import com.hz.junxinbaoan.view.MyGridView;

import net.qiujuer.genius.ui.widget.Button;

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

public class WorkFragment extends BaseFragment {
    private MyGridView square_x4;
    private RecyclerView workTimeChart;
    private WorkTimeAdapter worktimeAdapter;
    private List<SignTopBean> workData = new ArrayList<>();
    private SquareBean squareBean;
    private RelativeLayout sign_box;
    private AnimPtrFrameLayout ptr;
    private Button sign_box_btn;
    private TextView tag;
    private TextView tosign;
    private ScrollView scrollView;

    private TextView two, four, six, eight, ten;

    private RolePermissionResult.RolePermissionBean roleData;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate( R.layout.fragment_work, container, false );
        workTimeChart = (RecyclerView) view.findViewById( R.id.work_time_chart );
        square_x4 = (MyGridView) view.findViewById( R.id.square_x4 );
        sign_box = ((RelativeLayout) view.findViewById( R.id.sign_box ));
        sign_box_btn = (Button) view.findViewById( R.id.sign_box_btn );
        ptr = (AnimPtrFrameLayout) view.findViewById( R.id.ptrframlayout );
        tag = (TextView) view.findViewById( R.id.tag );
        tosign = (TextView) view.findViewById( R.id.tosign );
        scrollView = (ScrollView) view.findViewById( R.id.scroll );
        two = (TextView) view.findViewById( R.id.two );
        four = (TextView) view.findViewById( R.id.four );
        six = (TextView) view.findViewById( R.id.six );
        eight = (TextView) view.findViewById( R.id.eight );
        ten = (TextView) view.findViewById( R.id.ten );

        return view;
    }

    @Override
    protected void initViews(View view) {
        super.initViews( view );
        workTimeChart.setLayoutManager( new StaggeredGridLayoutManager( 1, StaggeredGridLayoutManager
                .HORIZONTAL ) );
        worktimeAdapter = new WorkTimeAdapter( getActivity(), workData );
        workTimeChart.setAdapter( worktimeAdapter );
    }

    @Override
    public void onResume() {
        super.onResume();
        getRole();//获取权限
        getSquareData();
        getTop();
//        push();
    }

    @Override
    protected void addListeners() {
        super.addListeners();

        //考勤 签到

        sign_box_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != roleData && null != roleData.getIsAppAttendance() && 1 == roleData.getIsAppAttendance())
                    startActivity( new Intent( getContext(), SignActivity.class ) );
                else
                    MyToast.showToast( mBaseActivity, getResources().getText( R.string.noRolePermission ) +
                            "" );
            }
        } );
//        square_x4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0://求助爆料
//                        Intent intent=new Intent(getActivity(), HelpCommitActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 1://我的排班
//                        startActivity(new Intent(getActivity(), SchedulingActivity.class));
//                        break;
//                    case 2://审批
//                        Intent intent3=new Intent(getActivity(), ActivityApprovalCommit.class);
//                        startActivity(intent3);
//                        break;
//                    case 3://学习中心
//                        Intent intent4=new Intent(getActivity(), ActivityStudyList.class);
//                        startActivity(intent4);
//                        break;
//
//                }
//            }
//        });

        ptr.setPtrHandler( new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown( frame, scrollView, header );
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getSquareData();
                getTop();
            }
        } );

    }

    //获取四个方块的数据
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.MAINPAGEDATA)
        Call<MainPageResult> getData(@FieldMap Map<String, Object> map);
    }

    //获取四个方块的数据
    private void getSquareData() {
        mBaseActivity.showDialog( true );
        GetData getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetData.class );
        BaseParam params = new BaseParam();
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        Call<MainPageResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<MainPageResult>() {
            @Override
            public void onResponse(final Call<MainPageResult> call, final Response<MainPageResult> response) {
                mBaseActivity.showDialog( false );
                ptr.refreshComplete();
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                        .OnHandleListener<MainPageResult>() {
                    @Override
                    public void onSuccess(MainPageResult result) {
                        if (result != null) {
                            squareBean = new SquareBean();
                            squareBean.setMyproject( result.getData().getSchedule() + "" );
                            squareBean.setCheck( result.getData().getRequest() + "" );//审批
                            squareBean.setHelp( result.getData().getReport() + "" );//求助爆料
                            squareBean.setStudy( result.getData().getLearn() + "" );
                            square_x4.setAdapter( new SquareAdapter( mBaseActivity, squareBean ) );
                            if (!TextUtils.isEmpty( result.getData().getScheduleName() )) {
                                tag.setText( "今日：" + result.getData().getScheduleName() );
                            }
                        }
                    }

                    @Override
                    public void onNetError() {
                        squareBean = new SquareBean();
                        squareBean.setMyproject( 0 + "" );
                        squareBean.setCheck( 0 + "" );//审批
                        squareBean.setHelp( 0 + "" );//求助爆料
                        squareBean.setStudy( 0 + "" );
                        square_x4.setAdapter( new SquareAdapter( mBaseActivity, squareBean ) );
                    }

                    @Override
                    public void onError(String code, String message) {
                        squareBean = new SquareBean();
                        squareBean.setMyproject( 0 + "" );
                        squareBean.setCheck( 0 + "" );//审批
                        squareBean.setHelp( 0 + "" );//求助爆料
                        squareBean.setStudy( 0 + "" );
                        square_x4.setAdapter( new SquareAdapter( mBaseActivity, squareBean ) );
                    }
                } );
            }

            @Override
            public void onFailure(Call<MainPageResult> call, Throwable t) {
                mBaseActivity.showDialog( false );
                ptr.refreshComplete();
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
                squareBean = new SquareBean();
                squareBean.setMyproject( 0 + "" );
                squareBean.setCheck( 0 + "" );//审批
                squareBean.setHelp( 0 + "" );//求助爆料
                squareBean.setStudy( 0 + "" );
                square_x4.setAdapter( new SquareAdapter( mBaseActivity, squareBean ) );

            }
        } );
    }

    //    //获取表格数据
//    private void getWorkTimeList(){
//        //调接口，现在是假数据
//        workData.clear();
//        for (int i = 0; i <10 ; i++) {
//            workData.add(new WorkTimeBean(4+i%4,6+i%3,i+1));
//        }
//        worktimeAdapter.notifyDataSetChanged();
//    }
    private interface GetTop {
        @FormUrlEncoded
        @POST(Constants.ATTENDACNE_STATISTICS)
        Call<GetStatisticsResult> getTop(@FieldMap Map<String, Object> map);
    }

    private void getTop() {
        mBaseActivity.showDialog( true );
        GetTop getTop = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetTop.class );
        BaseParam param = new BaseParam();
        ptr.refreshComplete();
        Call<GetStatisticsResult> call = getTop.getTop( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<GetStatisticsResult>() {
            @Override
            public void onResponse(Call<GetStatisticsResult> call, Response<GetStatisticsResult> response) {
                mBaseActivity.showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                        .OnHandleListener<GetStatisticsResult>() {
                    @Override
                    public void onSuccess(GetStatisticsResult result) {
                        Log.i( "TAG", "GetStatisticsResult : " + result.toString() );
                        List<SignTopBean> data = result.getData();
                        workData.clear();
                        int a = 0;
                        if (data != null && data.size() != 0) {
                            Log.i( "getTop TAG", "onSuccess: "+ data.get( data.size() - 1 ).getTime()[2] );
                            for (int i = 0; i < data.get( data.size() - 1 ).getTime()[2]//获取数据最后的天数
                                    ; i++) {
                                if (data.get( a ).getTime()[2] == i + 1) {
                                    workData.add( data.get( a ) );
                                    a++;//如果和数据的第一条匹配，加入真数据，a自加
                                } else {
                                    workData.add( new SignTopBean( 0, 0, "" ) );
                                }
                            }
                            Log.i( "TAG", "onSuccess: " + workData.size() );
                            //取最大502040
                            int max = 0;
                            for (int i = 0; i < workData.size(); i++) {
                                if (max < workData.get( i ).getHourExpect()) {
                                    max = workData.get( i ).getHourExpect();
                                }
                            }
                            if (max > 10 * 60 && max <= 15 * 60) {
                                two.setText( "3" );
                                four.setText( "6" );
                                six.setText( "9" );
                                eight.setText( "12" );
                                ten.setText( "15" );
                            } else if (max > 15 * 60 && max <= 20 * 60) {
                                two.setText( "4" );
                                four.setText( "8" );
                                six.setText( "12" );
                                eight.setText( "16" );
                                ten.setText( "20" );
                            } else if (max > 20 * 60) {
                                two.setText( "5" );
                                four.setText( "10" );
                                six.setText( "15" );
                                eight.setText( "20" );
                                ten.setText( "24" );
                            }
//                        workData.addAll(data);
                            worktimeAdapter.setMax( max );
                            worktimeAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<GetStatisticsResult> call, Throwable t) {
                mBaseActivity.showDialog( false );
                ptr.refreshComplete();
            }
        } );
    }

    private interface Finish {
        @FormUrlEncoded
        @POST(Constants.PUSHLOCATION)
        Call<BaseResult> getVcodeResult(@FieldMap Map<String, Object> map);
    }

    private void push() {
        Finish finish = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( Finish.class );
        final XYParams param = new XYParams();
        param.setLocationLatitude( 0 + "" );
        param.setLocationLongitude( 0 + "" );
        param.setAccess_token( MyApplication.mUserInfo.getAccess_token() );

        Call<BaseResult> call = finish.getVcodeResult( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<BaseResult>() {
            @Override
            public void onResponse(final Call<BaseResult> call, final Response<BaseResult> response) {
                ResultHandler.Handle( mBaseActivity, response.body(), false, new ResultHandler
                        .OnHandleListener<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
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
            public void onFailure(Call<BaseResult> call, Throwable t) {
//                MyToast.showToast(getApplicationContext(), "  网络连接失败，请稍后再试  ");
            }
        } );
    }


    //获取角色权限
    private interface GetRole {
        @FormUrlEncoded
        @POST(Constants.ROLE_PERMISSION)
        Call<RolePermissionResult> getRoles(@FieldMap Map<String, Object> map);
    }

    private void getRole() {
        GetRole getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetRole.class );
        BaseParam params = new BaseParam();
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        Call<RolePermissionResult> call = getData.getRoles( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<RolePermissionResult>() {
            @Override
            public void onResponse(final Call<RolePermissionResult> call, final Response<RolePermissionResult>
                    response) {
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                        .OnHandleListener<RolePermissionResult>() {


                    @Override
                    public void onSuccess(RolePermissionResult result) {
                        Log.i( "TAG", "onSuccess: " + result.toString() );
                        if (result != null && result.getCode().equals( "0000" ) && result.getData() != null) {
                            Log.i( "TAG", "onSuccess: " + result.toString() );
                            roleData = result.getData();
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
            public void onFailure(Call<RolePermissionResult> call, Throwable t) {
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }
}
