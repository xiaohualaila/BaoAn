package com.hz.junxinbaoan.activity.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
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
import com.hz.junxinbaoan.adapter.SignAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.SignDetailBean;
import com.hz.junxinbaoan.params.GetSignDetailParam;
import com.hz.junxinbaoan.result.GetSignDetailResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.utils.bluetooth.IBeacon;
import com.hz.junxinbaoan.view.AnimPtrFrameLayout;
import com.hz.junxinbaoan.view.LinkedMultiValueMap;
import com.hz.junxinbaoan.view.MultiValueMap;
import com.hz.junxinbaoan.view.RecyclerViewNoBugLinearLayoutManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * Created by Administrator on 2018/2/9.
 */

public class TemSignFragment extends BaseFragment {
    private static final String TAG = "TemSignFragment";
    private static final int REQUEST_ORIGINAL = 1;

    private TextView noOnline;
    private TextView time_tv;
    private RecyclerView sign_rv;
    private AnimPtrFrameLayout ptr;
    private NestedScrollView scrollView;

    private CalendarBean curDate;
    private CalendarBean nowDate;
    private List<SignDetailBean> signDetailList;
    private SignAdapter signAdapter;

    private File tempFile;
    private MultiValueMap<Integer, String> photoTyp1Map = new LinkedMultiValueMap<>();
    private MultiValueMap<Integer, String> photoTyp2Map = new LinkedMultiValueMap<>();
    private ArrayList<String> photoType1Path = new ArrayList<>();
    private ArrayList<String> photoType2Path = new ArrayList<>();
    private String cropImagePath;
    private String holderType;
    private CallBackValue callBackValue;
    private boolean uploadSign = false;  //是否已经签到上传
    private int takePhotoPosition;
    private boolean bluetoothOrOpen = false;//是否开启蓝牙
    private String scheduleId;//排班id

    //定义一个回调接口
    public interface CallBackValue {
        public void SendSignTemValue(int size);
    }

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach( activity );
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue = (CallBackValue) getActivity();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate( R.layout.fragment_sign_normal, container, false );
        noOnline = (TextView) view.findViewById( R.id.no_online );
        time_tv = (TextView) view.findViewById( R.id.time_tv );
        sign_rv = (RecyclerView) view.findViewById( R.id.sign_rv );
        ptr = (AnimPtrFrameLayout) view.findViewById( R.id.ptrframlayout );
        scrollView = (NestedScrollView) view.findViewById( R.id.scrollView );
        return view;
    }

    public void add(IBeacon ibeacon) {
        Log.i( "TAG", " ,ibeacon" + ibeacon );
        signAdapter.add( ibeacon );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i( TAG, "onStart: " );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i( TAG, " --id onResume" + nowDate.moth + "月" + nowDate.day + "日考勤签到" );
        //测试先不进行 真是数据获取
        getDetail( uploadSign );

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i( TAG, "onPause: " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i( TAG, "onStop: " );
    }


    @Override
    protected void addListeners() {
        super.addListeners();

        ptr.setPtrHandler( new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown( frame, scrollView, header );
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Log.e( TAG, "onRefreshBegin:  --- begin -- " );
//                signAdapter.deactivate();
//                signAdapter.clearData();
//                signAdapter.setDatas( new ArrayList<SignDetailBean>(), uploadSign );
                getDetail( uploadSign );
            }
        } );
    }

    @Override
    protected void initViews(View view) {
        super.initViews( view );

        final Calendar calendar = Calendar.getInstance();
        curDate = new CalendarBean( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1, calendar.get(
                Calendar.DATE ) );
        nowDate = curDate;
        Log.e( "TAG", "initViews : " + curDate.toString() + "--" + nowDate );

        signAdapter = new SignAdapter( mBaseActivity, uploadSign, new SignAdapter.RefreshUIInterface() {
            @Override
            public void refresh(boolean b) {
                Log.e( TAG, signDetailList.size() + "" );
                if (b)
                    uploadSign = true;
                else
                    uploadSign = false;
                getDetail( uploadSign );
            }

            @Override
            public void showLoading(boolean show) {
                mBaseActivity.showDialog( show );
            }
        }, false, new SignAdapter.TakePhotoUIInterface() {
            @Override
            public void takePhotos(String holder, int po) {
                takePhotoPosition = po;
//                Intent intent = new Intent( "android.media.action.IMAGE_CAPTURE" );
//                tempFile = new File( Settings.PIC_PATH, getPhotoFileName() );
//                Log.e( TAG,takePhotoPosition + " , " +  holder + " -- tempFile:" + tempFile.toString() );
//                // 从文件中创建uri
//                Uri uri = Uri.fromFile( tempFile );
//                //为拍摄的图片指定一个存储的路径
//                intent.putExtra( MediaStore.EXTRA_OUTPUT, uri );
//                startActivityForResult( intent, REQUEST_ORIGINAL );
//                holderType = holder;

                Uri uri;
                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
                    ContentValues contentValues = new ContentValues( 1 );
                    String temp = Environment.getExternalStorageDirectory().getPath() + File.separator + mBaseActivity
                            .getPackageName() + File.separator + "pic";
                    tempFile = new File( temp, getPhotoFileName() );
                    contentValues.put( MediaStore.Images.Media.DATA, tempFile.getAbsolutePath() );
                    uri = mBaseActivity.getContentResolver().insert( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            contentValues );
                } else {
                    String temp = Environment.getExternalStorageDirectory().getPath() + File.separator + mBaseActivity
                            .getPackageName() + File.separator + "pic";
                    tempFile = new File( temp, getPhotoFileName() );
                    uri = Uri.fromFile( tempFile );
                }
                intent.putExtra( MediaStore.EXTRA_OUTPUT, uri );
                startActivityForResult( intent, REQUEST_ORIGINAL );
                holderType = holder;


            }
        }, photoTyp1Map, photoTyp2Map );
        RecyclerViewNoBugLinearLayoutManager linearLayoutManager = new RecyclerViewNoBugLinearLayoutManager(
                mBaseActivity );
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( mBaseActivity );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        sign_rv.setLayoutManager( linearLayoutManager );
        sign_rv.setItemAnimator( null );
        sign_rv.setNestedScrollingEnabled( false );
//        ((SimpleItemAnimator)sign_rv.getItemAnimator()).setSupportsChangeAnimations(false);
        sign_rv.setAdapter( signAdapter );
        signDetailList = new ArrayList<>();
        //  假数据
//        signDetailList.add( new SignDetailBean( "2月9日", "签到时间9:03 (上班时间 9:00)", "乐富智汇园", 3, 3 ) );
//        signDetailList.add( new SignDetailBean( "10月23日", "签到时间18:00 (下班时间 18:00)", "乐富智汇园", 0, 0 ) );
//        signDetailList.add( new SignDetailBean( "2月8日", "上班时间 18:00", "", 1, 0 ) );
//        signDetailList.add( new SignDetailBean( "10月25日", "上班时间 18:00", "", 2, 0 ) );
//        signDetailList.add( new SignDetailBean( "10月25日", "上班时间 18:00", "", 4, 0 ) );

        Log.e( TAG, "-- " + signDetailList + ", --id onResume" + nowDate.moth + "月" + nowDate.day + "日考勤签到" );
        signAdapter.setDatas( signDetailList, uploadSign );

//        if (nowDate != null)
//            titleName.setText( nowDate.moth + "月" + nowDate.day + "日考勤签到" );
    }

    /**
     * 上传图片成功 或退出当前activity时 进行图片缓存的清除
     *
     * @param photo
     */
    private void deletePhotos(ArrayList<String> photo) {
        if (photo.size() > 0) {
            // 清理所有子文件
            for (int i = 0; i < photo.size(); i++) {
                File file = new File( photo.get( i ) );
                file.delete();
            }
        }
    }

    /**
     * 用时间戳生成照片名称
     *
     * @return
     */
    private String getPhotoFileName() {
        Date date = new Date( System.currentTimeMillis() );
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss" );
        return dateFormat.format( date ) + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == REQUEST_ORIGINAL && resultCode == Activity.RESULT_OK) {
            // 从相机返回的数据
//                crop( Uri.fromFile( tempFile ) );
            cropImagePath = tempFile.getAbsolutePath();
            Log.e( TAG, holderType + "cropImagePath : " + cropImagePath );
            if (holderType != null && "VH1".equals( holderType )) {
                photoTyp1Map.add( takePhotoPosition, cropImagePath );
//                photoType1Path.add( cropImagePath );
                signAdapter.setPhotoType1Datas( photoTyp1Map, takePhotoPosition );
                Log.e( TAG, holderType + takePhotoPosition + ", position点击相机 cropImagePath : " + photoTyp1Map.values
                        () );
            } else if (holderType != null && "VH2".equals( holderType )) {
                photoTyp2Map.add( takePhotoPosition, cropImagePath );
                signAdapter.setPhotoType2Datas( photoTyp2Map, takePhotoPosition );
            }
//            signAdapter.notifyItemRangeChanged( takePhotoPosition, 0 );
//            ((LinearLayoutManager) sign_rv.getLayoutManager()).scrollToPositionWithOffset( takePhotoPosition, 0 );
        }
    }

    private interface GetDetail {
        @FormUrlEncoded
        @POST(Constants.ATTENDACNE_DETAIL)
        Call<GetSignDetailResult> getDetail(@FieldMap Map<String, Object> map);
    }

    private synchronized void getDetail(final boolean uploadSign) {
        Log.e( TAG, "getDetail: ----" );
        signDetailList.clear();
        try {
            mBaseActivity.showDialog( true );
            time_tv.setText( curDate.moth + "月" + curDate.day + "日排班" );
            GetDetail getDetail = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetDetail
                    .class );
            GetSignDetailParam param = new GetSignDetailParam();
            param.setAttendanceDate( curDate.getParams() );
            Log.e( TAG, "getDetail: " + param.toString() );
            Call<GetSignDetailResult> call = getDetail.getDetail( CommonUtils.getPostMap( param ) );
            call.enqueue( new Callback<GetSignDetailResult>() {
                @Override
                public void onResponse(final Call<GetSignDetailResult> call, Response<GetSignDetailResult> response) {

                    mBaseActivity.showDialog( false );
                    ptr.refreshComplete();
                    ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                            .OnHandleListener<GetSignDetailResult>() {
                        @Override
                        public void onSuccess(GetSignDetailResult result) {
                            Log.e( TAG, "GetStatisticsResult : " + result.toString() );
                            List<SignDetailBean> data = result.getData();
                            int datasize = data.size();
                            if (datasize > 0) {
                                Log.i( TAG, "onSuccess: " + datasize );
                                int sum = 0;
                                for (int i = 0; i < datasize; i++) {
                                    if (2 == data.get( i ).getType()) { //加班
                                        sum+=1;
                                        int size = signDetailList.size();
                                        if (size == 0)
                                            signDetailList.add( data.get( i ) );
                                        else {
                                            boolean index = false;
                                            for (int j = 0; j < size; j++) {
                                                if (data.get( i ).getAttendanceTimestampExpect() == signDetailList.get(
                                                        j ).getAttendanceTimestampExpect()) {
                                                    index = false;
                                                    break;
                                                } else
                                                    index = true;
                                            }
                                            if (index)
                                                signDetailList.add( data.get( i ) );
                                        }
                                    }
                                }
                                Log.i( TAG, "onSuccess: " + sum + " --- " + signDetailList.size() );
                            } else {
                                noOnline.setVisibility( View.VISIBLE );
                            }

                            if (signDetailList.size() > 0) {
                                noOnline.setVisibility( View.GONE );
                                scheduleId = signDetailList.get( 0 ).getAttendanceScheduleId();
                                Log.i( TAG, "onSuccess: getEndType " + signDetailList.get( 0 ).getEndType() );
                                for (int i = 1; i < signDetailList.size(); i++) {
                                    if (scheduleId.equals( signDetailList.get( i ).getAttendanceScheduleId() )) {
                                        signDetailList.get( i ).setEndType( 1 );
                                    } else {
                                        signDetailList.get( i - 1 ).setEndType( 2 );
                                        scheduleId = signDetailList.get( i ).getAttendanceScheduleId();
                                        signDetailList.get( i ).setEndType( 0 );
                                    }
                                    if (i == signDetailList.size() - 1)
                                        signDetailList.get( i ).setEndType( 2 );
                                }

                                for (int i = 0; i < signDetailList.size(); i++) {
                                    SignDetailBean bean = signDetailList.get( i );
                                    /* 考勤状态 0-未到签到时间 1-签到进行中 2-漏签到 3-正常已签到 */
                                    switch (bean.getAttendanceStatus()) {
                                        case 0:
                                            bean.setAttendanceStatusType( 0 );
                                            break;
                                        case 1:
                                            /* 签到类型 0-GPS 1-蓝牙 2-任意位置 */
                                            switch (bean.getAttendanceType()) {
                                                case 0:
                                                case 2:
                                                    bean.setAttendanceStatusType( 1 );
                                                    break;
                                                case 1:
                                                    bean.setAttendanceStatusType( 2 );
                                                    break;
                                            }
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
//                            signAdapter = new SignAdapter( mBaseActivity, new SignAdapter.RefreshUIInterface() {
//                                @Override
//                                public void refresh() {
//                                    signAdapter.clearData();
//                                    getDetail();
//                                }
//
//                                @Override
//                                public void showLoading(boolean show) {
//                                    mBaseActivity.showDialog( show );
//                                }
//                            }, false, new SignAdapter.TakePhotoUIInterface() {
//                                @Override
//                                public void takePhotos(String holder) {
//                                }
//                            }, photoType1Path, photoType2Path );
                            Log.i( TAG, "onSuccess:   " + signDetailList.toString() );
                            callBackValue.SendSignTemValue( signDetailList.size() );
                            signAdapter.setDatas( signDetailList, uploadSign );
//                            sign_rv.setAdapter( signAdapter );
                        }

                        @Override
                        public void onNetError() {
                            Log.e( TAG, "getDetail ----- onNetError" );
                            setErrorData();
                        }

                        @Override
                        public void onError(String code, String message) {
                            Log.e( TAG, "getDetail ----- onError" );
                            setErrorData();
                        }
                    } );
                }

                @Override
                public void onFailure(Call<GetSignDetailResult> call, Throwable t) {
                    mBaseActivity.showDialog( false );
                    Log.e( TAG, "getDetail ----- onFailure" );
                    ptr.refreshComplete();
                    setErrorData();
                }
            } );

        } catch (Exception e) {

        }

    }

    private void setErrorData() {
//        signAdapter = new SignAdapter( mBaseActivity, new SignAdapter.RefreshUIInterface() {
//            @Override
//            public void refresh() {
//                signAdapter.clearData();
//                getDetail();
//            }
//
//            @Override
//            public void showLoading(boolean show) {
//                mBaseActivity.showDialog( show );
//            }
//        }, false, new SignAdapter.TakePhotoUIInterface() {
//            @Override
//            public void takePhotos(String holder) {
//            }
//        }, photoType1Path, photoType2Path );
        signAdapter.setDatas( new ArrayList<SignDetailBean>(), uploadSign );
//        sign_rv.setAdapter( signAdapter );
    }

}
