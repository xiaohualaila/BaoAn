package com.hz.junxinbaoan.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codbking.calendar.CalendarBean;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseFragment;
import com.hz.junxinbaoan.adapter.SignScheAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.SignDetailBean;
import com.hz.junxinbaoan.params.GetSignDetailParam;
import com.hz.junxinbaoan.result.GetSignDetailResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * 排班列表  临时页面
 */
public class TemporaryFragment extends BaseFragment {

    private static final String TAG = "NormalFragment";
    private RecyclerView sign_rv;
    private SignScheAdapter signScheAdapter;
    private List<SignDetailBean> signDetailList;
    private CalendarBean curDate;
    private TextView noOnline;
    private CallBackValue callBackValue;
    private ArrayList<String> photoType1Path = new ArrayList<>(  );//图片列表 设置最多上传9张
    private ArrayList<String> photoType2Path = new ArrayList<>(  );//图片列表 设置最多上传9张
    private String scheduleId;//班次id

    //定义newInstance()实例
    public static TemporaryFragment newInstance(String id){
        TemporaryFragment myFragment = new TemporaryFragment();
        return myFragment;
    }

    //定义一个回调接口
    public interface CallBackValue{
        public void SendTemValue(int size);
    }

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue =(CallBackValue) getActivity();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate( R.layout.fragment_normal, container, false );
//        View view = inflater.inflate( R.layout.fragment_normal, container, false );
        sign_rv = (RecyclerView) view.findViewById( R.id.sign_rv );
        noOnline = (TextView) view.findViewById( R.id.no_online );
        return view;
    }

    @Override
    protected void initViews(View view) {
        super.initViews( view );
        final Calendar calendar = Calendar.getInstance();
        curDate = new CalendarBean( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1, calendar.get(
                Calendar.DATE ) );

        signScheAdapter = new SignScheAdapter( mBaseActivity,true, new SignScheAdapter.RefreshUIInterface() {
            @Override
            public void refresh(boolean b) {
                signScheAdapter.clearData();

                getDetail();
            }

            @Override
            public void showLoading(boolean show) {

            }
        }, true, new SignScheAdapter.TakePhotoUIInterface() {
            @Override
            public void takePhotos(String holder, int position) {
            }
        }, photoType1Path, photoType2Path );
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager( mBaseActivity );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        sign_rv.setLayoutManager( linearLayoutManager );
        sign_rv.setAdapter( signScheAdapter );
        sign_rv.setNestedScrollingEnabled( false );
        signDetailList = new ArrayList<>();
//             TODO 假数据
//        signDetailList.add(new SignDetailBean("10月23日","签到时间9:03 (上班时间 9:00)","乐富智汇园",3,3));
//        signDetailList.add(new SignDetailBean("10月23日","签到时间18:00 (下班时间 18:00)","乐富智汇园",0,0));
//        signDetailList.add(new SignDetailBean("10月24日","上班时间 18:00","",1,0));
//        signDetailList.add(new SignDetailBean("10月25日","上班时间 18:00","",2,0));
        signScheAdapter.setDatas( signDetailList,true );
    }

    public void normalItem(CalendarBean str) {
        curDate = str;
        getDetail();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDetail();
    }

    @Override
    protected void addListeners() {
        super.addListeners();
    }

    private interface GetDetail {
        @FormUrlEncoded
        @POST(Constants.ATTENDACNE_DETAIL)
        Call<GetSignDetailResult> getDetail(@FieldMap Map<String, Object> map);
    }

    private synchronized void getDetail() {
        signDetailList.clear();
        mBaseActivity.showDialog(true);
        GetDetail getDetail = CommonUtils.buildRetrofit(Constants.BASE_URL,mBaseActivity).create(GetDetail.class);
        GetSignDetailParam param = new GetSignDetailParam();
        param.setAttendanceDate(curDate.getParams());
        Log.e( TAG, "getDetail: "+param.toString()  );
        Call<GetSignDetailResult> call = getDetail.getDetail(CommonUtils.getPostMap(param));
        call.enqueue(new Callback<GetSignDetailResult>() {
            @Override
            public void onResponse(final Call<GetSignDetailResult> call, Response<GetSignDetailResult> response) {
                mBaseActivity.showDialog(false);
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<GetSignDetailResult>() {
                    @Override
                    public void onSuccess(GetSignDetailResult result) {

                        Log.e( "TAG","TemFragment ; "+result.toString() );
                        List<SignDetailBean> data = result.getData();
                        if (data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                if (2 == data.get( i ).getType())  //加班
                                    signDetailList.add( data.get( i ) );
                            }
                        }else{
                            noOnline.setVisibility( View.VISIBLE );
                        }

                        Log.e( TAG, "onSuccess:  "+signDetailList.size()  );
                        if (signDetailList.size() > 0) {
                            noOnline.setVisibility( View.GONE );
                            scheduleId = signDetailList.get( 0 ).getAttendanceScheduleId();
                            Log.i( TAG, "onSuccess: getEndType " + signDetailList.get( 0 ).getEndType() );
                            for (int i = 1; i < signDetailList.size(); i++) {
                                if (scheduleId.equals( signDetailList.get( i ).getAttendanceScheduleId() )) {
                                    signDetailList.get( i ).setEndType( 1 );
                                } else {
                                    signDetailList.get( i-1 ).setEndType( 2 );
                                    scheduleId = signDetailList.get( i ).getAttendanceScheduleId();
                                    signDetailList.get( i ).setEndType( 0 );
                                }
                                if (i == signDetailList.size()-1)
                                    signDetailList.get( i ).setEndType( 2 );
                            }


                            for (int i = 0; i < signDetailList.size(); i++) {
                                SignDetailBean bean = signDetailList.get( i );
                                    /* 考勤状态 0-未到签到时间 1-签到进行中 2-漏签到 3-正常已签到 */
                                switch (bean.getAttendanceStatus()) {
                                    case 0:
                                    case 1:
                                        bean.setAttendanceStatusType( 0 );
                                        break;
                                    case 2:
                                        bean.setAttendanceStatusType( 4 );
                                        break;
                                    case 3:
                                        bean.setAttendanceStatusType( 3 );
                                        break;
                                }
                            }
                        } else {
                            noOnline.setVisibility( View.VISIBLE );
                        }
                        callBackValue.SendTemValue(signDetailList.size());
                        signScheAdapter.setDatas( signDetailList,true );

                    }

                    @Override
                    public void onNetError() {
                        signScheAdapter.setDatas(new ArrayList<SignDetailBean>(),true);
                    }

                    @Override
                    public void onError(String code, String message) {
                        signScheAdapter.setDatas(new ArrayList<SignDetailBean>(),true);
                    }
                });
            }

            @Override
            public void onFailure(Call<GetSignDetailResult> call, Throwable t) {
                mBaseActivity.showDialog(false);
                signScheAdapter.setDatas(new ArrayList<SignDetailBean>(),true);
            }
        });
    }

}
