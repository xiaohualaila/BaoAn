package com.hz.junxinbaoan.activity.map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.LocationParams;
import com.hz.junxinbaoan.result.RoadResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class PeoPleMap extends BaseActivity {
    private static final String TAG = "PeoPleMap";
    AMap aMap;
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.title_name)
    TextView titleName;

    @BindView(R.id.title_lay)
    RelativeLayout titleLay;

    private MapView mapView;
    private String date;
//    private CalendarBean mCalendarBean;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_peo_ple_map );
        mapView = (MapView) findViewById( R.id.map );
        mapView.onCreate( savedInstanceState );

    }

    @Override
    protected void initViews() {
        super.initViews();
        initMap();
        changeUI();

    }

    private void changeUI() {
        date = getIntent().getStringExtra( "name" );
        Log.e( TAG, "namer : " + date );
        String[] split = date.split( "-" );
        titleName.setText( split[1]+ "月" + split[2]  + "日排班( " + MyApplication.mUserInfo.getUserName() +" )" );
        getRoad();
    }


    @Override
    protected void addListeners() {
        super.addListeners();
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

    }


    //获取轨迹接口
    private interface Road {
        @FormUrlEncoded
        @POST(Constants.ROAD)
        Call<RoadResult> getData(@FieldMap Map<String, Object> map);
    }

    //轨迹接口  参数
    private void getRoad() {
        showDialog( true );
        Road getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( Road.class );
        LocationParams params = new LocationParams();
//        if (mCalendarBean != null) {
//            params.setTime( mCalendarBean.getParams() );
//        } else {
//            Calendar c = Calendar.getInstance();
//            int year = c.get( Calendar.YEAR );
//            int month = c.get( Calendar.MONTH ) + 1;
//            int day = c.get( Calendar.DAY_OF_MONTH );
//            params.setTime( year + "-" + month + "-" + day );
//        }
        params.setTime( date );
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
//        params.setLocationEmployeeId( getIntent().getStringExtra( "id" ) );
        params.setLocationEmployeeId( MyApplication.mUserInfo.getUserId() );

        Log.e( TAG, "LocationParams : " + params.toString() );
        Call<RoadResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<RoadResult>() {
            @Override
            public void onResponse(final Call<RoadResult> call, final Response<RoadResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler.OnHandleListener<RoadResult>() {
                    @Override
                    public void onSuccess(RoadResult result) {
                        Log.e( TAG, result.toString() );
                        if (result != null && result.getCode().equals( "0000" ) && result.getData() != null && result
                                .getData().size() != 0) {
                            aMap.clear();
                            List<LatLng> latLngs = new ArrayList<LatLng>();
                            for (int i = 0; i < result.getData().size(); i++) {
                                latLngs.add( result.getData().get( i ).getLl() );
//                                if (i!=0){
//                                    setUpMap(result.getData().get(i-1).getLl(), result.getData().get(i).getLl());
//                                }
//                                Log.e( TAG,latLngs.toString() );
                            }

                            aMap.addPolyline( new PolylineOptions().
                                    addAll( latLngs ).geodesic( true ).color( getResources().getColor( R.color
                                    .app_style_blue ) ) );
                            Log.i( "czx", "" + result.getData().size() );
                            aMap.moveCamera( CameraUpdateFactory.newLatLngZoom( result.getData().get( result.getData
                                    ().size() - 1 ).getLl(), 15f ) );
                            aMap.addMarker( new MarkerOptions()
                                    .anchor( 0.5f, 0.5f )
                                    .icon( BitmapDescriptorFactory.fromResource( R.mipmap.road_final ) ) )
                                    .setPosition( result.getData().get( result.getData().size() - 1 ).getLl() );
                        } else {
                            MyToast.showToast( mBaseActivity, "  暂无轨迹  " );
                            aMap.clear();
                        }
                    }

                    @Override
                    public void onNetError() {
                        showDialog( false );
                    }

                    @Override
                    public void onError(String code, String message) {
                        showDialog( false );
                    }
                } );
            }

            @Override
            public void onFailure(Call<RoadResult> call, Throwable t) {
                showDialog( false );
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.clear();

        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle
        // .LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType
        // ，默认也会执行此种模式。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_icon));
        myLocationStyle.showMyLocation( false );//不显示定位图片
        aMap.setMyLocationStyle( myLocationStyle );//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled( false );//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setZoomControlsEnabled( false );//不显示缩放按钮
        aMap.setMyLocationEnabled( true );// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        //在activity执行onSaveInstanceState时执行mapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState( outState );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    private void setUpMap(LatLng oldData, LatLng newData) {
        // 绘制一个大地曲线
        aMap.addPolyline( (new PolylineOptions())
                .add( oldData, newData )
                .geodesic( true ).color( getResources().getColor( R.color.app_style_blue ) ) );
    }
}
