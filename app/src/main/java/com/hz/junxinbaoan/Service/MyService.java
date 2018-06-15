package com.hz.junxinbaoan.Service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.BaseParam;
import com.hz.junxinbaoan.params.XYParams;
import com.hz.junxinbaoan.result.BaseResult;
import com.hz.junxinbaoan.result.TimeResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ResultHandler;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyService extends Service {
    double x, y;
    int mum;
    private AMapLocationClient mlocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient locationClient;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


        locationClient = new AMapLocationClient( getApplicationContext() );
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        locationClient.setLocationListener( new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        y = aMapLocation.getLatitude();//获取纬度
                        x = aMapLocation.getLongitude();//获取经度
                        aMapLocation.getAccuracy();//获取精度信息
                        if (!TextUtils.isEmpty( MyApplication.mUserInfo.getAccess_token() )) {
                            if (x != 0 && y != 0) {
                                if (MyApplication.time > 0) {
                                    push();
                                }
                            }

                        }
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e( "AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo() );
                    }
                }
            }
        } );
        //设置为高精度定位模式
        mLocationOption.setOnceLocation( true );
        mLocationOption.setLocationMode( AMapLocationClientOption.AMapLocationMode.Hight_Accuracy );
        //设置定位参数
        locationClient.setLocationOption( mLocationOption );
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        locationClient.startLocation();

        if (MyApplication.time > 0) {
            new MyCountDownTimer( 300000000, MyApplication.time * 1000 ).start();
        }
        if (MyApplication.time > 0) {
            new MyCountDownTimer2( 300000000, 60000 ).start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand( intent, flags, startId );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class MyCountDownTimer2 extends CountDownTimer {
        public MyCountDownTimer2(long millisInFuture, long countDownInterval) {
            super( millisInFuture, countDownInterval );
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getTime();
        }

        @Override
        public void onFinish() {
            new MyCountDownTimer2( 300000000, MyApplication.time * 1000 ).start();
        }
    }


    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super( millisInFuture, countDownInterval );
        }

        @Override
        public void onTick(long millisUntilFinished) {
            try {
                if (locationClient != null) {
                    locationClient.startLocation();
                }
            } catch (OutOfMemoryError e) {
            }
        }

        @Override
        public void onFinish() {
            new MyCountDownTimer( 300000000, MyApplication.time * 1000 ).start();
        }
    }


    private interface Finish {
        @FormUrlEncoded
        @POST(Constants.PUSHLOCATION)
        Call<BaseResult> getVcodeResult(@FieldMap Map<String, Object> map);
    }

    private void push() {
        Finish finish = CommonUtils.buildRetrofit( Constants.BASE_URL, getApplicationContext() ).create( Finish.class );
        final XYParams param = new XYParams();
        param.setLocationLatitude( y + "" );
        param.setLocationLongitude( x + "" );
        param.setAccess_token( MyApplication.mUserInfo.getAccess_token() );

        Call<BaseResult> call = finish.getVcodeResult( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<BaseResult>() {
            @Override
            public void onResponse(final Call<BaseResult> call, final Response<BaseResult> response) {
                ResultHandler.Handle( getApplicationContext(), response.body(), false, new ResultHandler
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


    //获取个人信息接口
    private interface Frequency {
        @FormUrlEncoded
        @POST(Constants.FREQUENCY)
        Call<TimeResult> getData(@FieldMap Map<String, Object> map);
    }

    //获取个人信息
    private void getTime() {
        Frequency getData = CommonUtils.buildRetrofit( Constants.BASE_URL, getApplicationContext() ).create(
                Frequency.class );
        BaseParam params = new BaseParam();
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        Call<TimeResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<TimeResult>() {
            @Override
            public void onResponse(final Call<TimeResult> call, final Response<TimeResult> response) {
                ResultHandler.Handle( getApplicationContext(), response.body(), false, new ResultHandler
                        .OnHandleListener<TimeResult>() {
                    @Override
                    public void onSuccess(TimeResult result) {
                        MyApplication.time = result.getData();
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
            public void onFailure(Call<TimeResult> call, Throwable t) {
            }
        } );
    }


}
