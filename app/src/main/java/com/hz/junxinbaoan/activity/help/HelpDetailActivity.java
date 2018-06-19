package com.hz.junxinbaoan.activity.help;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.adapter.SeePhotoHelpDetailAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.HelpListBean;
import com.hz.junxinbaoan.params.ReportDetailParam;
import com.hz.junxinbaoan.result.HelpDetailResult;
import com.hz.junxinbaoan.utils.AudioPlayerUtil;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.PopupWindowFactory;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.utils.ThreadPool;
import com.hz.junxinbaoan.view.MyLayout;
import com.yancy.imageselector.ImageSelectorActivity;

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

//import com.autonavi.ae.gmap.maploader.ERROR_CODE;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class HelpDetailActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener {
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.myLayout)
    MyLayout myLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.photo_rv)
    RecyclerView photo_rv;
    @BindView(R.id.record_play)
    Button record_play;
    @BindView(R.id.name_tv)
    TextView name_tv;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.content_tv)
    TextView content_tv;
    @BindView(R.id.address_tv)
    TextView address_tv;

    @BindView(R.id.howlong)
    TextView howlong;

    private final String TAG = "HelpDetailActivity";

    //录音相关
    private PopupWindowFactory mPop;
    private AudioPlayerUtil audioPlayerUtil;

    private float mRecord_Time;// 录音的时间
    private final int MIN_TIME = 2;// 最短录音时间
    private String videoPath;//录音地址
    private boolean mPlayState = false;//播放状态

    //图片相关
//    private ArrayList<byte[]> path = new ArrayList<>();
    private ArrayList<String > path = new ArrayList<>();
    private SeePhotoHelpDetailAdapter adapter;

    //地图相关
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private AMapLocationClient locationClient;
    private Marker locationMarker;
    private String id;
    private int size;
    private ThreadPool threadPool;

    @Override
    protected void getIntentData() {
        super.getIntentData();
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_help_detail);
        mapView = (TextureMapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        initMap();
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.clear();
            setUpMap();
        }
//        mapView.getChildAt(1).setVisibility(View.GONE);


    }

    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_icon)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /** 移动地图结束事件的回调*/
    @Override
    public void onCameraChangeFinish(final CameraPosition cameraPosition) {

        LatLng target = cameraPosition.target;
        Log.d("HelpCommitActivity","移动结束 经度："+ target.latitude+"    纬度："+ target.longitude);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter();
            }
        });
        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.showMyLocation(false);//不显示定位图片
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setZoomControlsEnabled(false);//不显示缩放按钮
        aMap.getUiSettings().setAllGesturesEnabled(false);//不支持所有手势
    }

    @Override
    protected void initViews() {
        super.initViews();

        //解决地图与scrollView滑动冲突
        myLayout.setScrollView(scrollView);
        audioPlayerUtil = new AudioPlayerUtil();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        photo_rv.setLayoutManager(gridLayoutManager);
        adapter = new SeePhotoHelpDetailAdapter(this, path );
        photo_rv.setAdapter(adapter);

    }

    @Override
    protected void addListeners() {
        super.addListeners();

//        ptrframlayout.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame,scrollView,header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                getDetail();
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //播放录音
        record_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mPlayState) {
                    // 修改播放状态
                    mPlayState = true;
                    // 修改播放图标
                    record_play.setBackgroundResource(R.mipmap.icon_stop);
//            seekBar.setVisibility(View.VISIBLE);
                    audioPlayerUtil.play();
                    audioPlayerUtil.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // 停止播放
                            audioPlayerUtil.stop();
                            record_play.setBackgroundResource(R.mipmap.record_play);
                            // 修改播放状态
                            mPlayState = false;
                        }
                    });
                } else {
                    audioPlayerUtil.pause();
                    // 修改播放状态
                    record_play.setBackgroundResource(R.mipmap.record_play);
                    mPlayState = false;
                }
            }
        });
    }

    @Override
    protected void requestOnCreate() {
        super.requestOnCreate();
        getDetail();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000  && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

//            for (String path : pathList) {
//                Log.i("ImagePathList", path);
//            }

//            path.clear();
//            path.addAll(pathList);
//            adapter.notifyDataSetChanged();
        }
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
        if (audioPlayerUtil!=null){
            audioPlayerUtil.pause();
            // 修改播放状态
            record_play.setBackgroundResource(R.mipmap.record_play);
            mPlayState = false;
        }
        //在activity执行onPause时执行mapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayerUtil!=null){
            audioPlayerUtil.stop();
        }
        //在activity执行onDestroy时执行mapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            locationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            locationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//            locationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null && aMapLocation.getErrorCode() == ERROR_CODE.ERROR_NONE) {
        Log.e( TAG, "onLocationChanged: HelpDetailActivity"+aMapLocation.getErrorCode());
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mListener.onLocationChanged(aMapLocation);
            //移动地图中心到当前的定位位置
            moveMapAndRefreshList(aMapLocation.getLatitude(),aMapLocation.getLongitude());
            //获取定位信息
        }
    }
    private  void moveMapAndRefreshList(double Lat,double Lon){
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Lon), 15));
    }

    //求助爆料详情
    private interface GetDetail {
        @FormUrlEncoded
        @POST(Constants.MESSAGEDETAIL)
        Call<HelpDetailResult> getDetail(@FieldMap Map<String, Object> map);
    }
    private void getDetail() {
        showDialog(true);
        GetDetail getDetail = CommonUtils.buildRetrofit(Constants.BASE_URL,mBaseActivity).create(GetDetail.class);
        ReportDetailParam param = new ReportDetailParam();
        param.setReportId(id);
        Call<HelpDetailResult> call = getDetail.getDetail(CommonUtils.getPostMap(param));
        call.enqueue(new Callback<HelpDetailResult>() {
            @Override
            public void onResponse(Call<HelpDetailResult> call, Response<HelpDetailResult> response) {
                showDialog(false);
//                ptrframlayout.refreshComplete();
                ResultHandler.Handle(mBaseActivity, response.body(), new ResultHandler.OnHandleListener<HelpDetailResult>() {
                    @Override
                    public void onSuccess(HelpDetailResult result) {
                        Log.i( TAG, "onSuccess: "  + result.toString());
                        initView(result);
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
            public void onFailure(Call<HelpDetailResult> call, Throwable t) {
                showDialog(false);
//                ptrframlayout.refreshComplete();
            }
        });
    }

    private void initView(HelpDetailResult result) {
        if (result.getData() != null){
            HelpListBean helpListBean = result.getData();
            if (!CommonUtils.isEmpty(helpListBean.getReportEmployeeName())){
                name_tv.setText(helpListBean.getReportEmployeeName());
            }
            if(!CommonUtils.isEmpty(helpListBean.getReportTimeStr())){
                time_tv.setText(helpListBean.getReportTimeStr());
            }
            if (!CommonUtils.isEmpty(helpListBean.getReportContent())){
                content_tv.setText(helpListBean.getReportContent());
            }
            if (!CommonUtils.isEmpty(helpListBean.getReportLat()) && !CommonUtils.isEmpty(helpListBean.getReportLon())){
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(helpListBean.getReportLat()), Double.parseDouble(helpListBean.getReportLon())), 15));
            }
            String reportPictures = helpListBean.getReportPictures();
            if (!CommonUtils.isEmpty(reportPictures) && !reportPictures.equals("null")){
                path.clear();
                List<String > pics = new Gson().fromJson(reportPictures, new TypeToken<List<String >>() {}.getType());
                if (pics != null && pics.size() != 0){
                    path.addAll(pics);
                    adapter.notifyDataSetChanged();
                }
//                if (pics.size() > 0){
//                    loadPic(count,pics);

//                }
            }
            String reportAudios = helpListBean.getReportAudios();
            if (!CommonUtils.isEmpty(reportAudios) && !reportAudios.equals("null")){
//                List<String > video = new Gson().fromJson(reportAudios, new TypeToken<List<String >>() {}.getType());
//                if (video != null && video.size() != 0){
//                    audioPlayerUtil.setUrl(video.get(0));
//                    howlong.setText(audioPlayerUtil.getAudioTime()+"");
//                    record_play.setVisibility(View.VISIBLE);
//                }else {
//                    record_play.setVisibility(View.GONE);
//                }

                try {
                    audioPlayerUtil.setUrl( reportAudios  );
                    howlong.setText( audioPlayerUtil.getAudioTime() + "" );
                    record_play.setVisibility( View.VISIBLE );
                } catch (Exception ignore) {
                }
//                if (video.get(0).contains("amr")){
//                    CommonUtils.getPic(mBaseActivity, video.get(0), new CommonUtils.GetPic() {
//                        @Override
//                        public void getPic(File pic) {
//                            try {
//                                audioPlayerUtil.setUrl(pic.getPath());
//                                record_play.setVisibility(View.VISIBLE);
//                            } catch (Exception ignore) {
//                            }
//                        }
//                    }, 1);
//                }else {
//                    CommonUtils.getPic(mBaseActivity, video.get(0), new CommonUtils.GetPic() {
//                        @Override
//                        public void getPic(File pic) {
//                            try {
//                                audioPlayerUtil.setUrl(pic.getPath());
//                                record_play.setVisibility(View.VISIBLE);
//                            } catch (Exception ignore) {
//                            }
//                        }
//                    }, 2);
//                }

            }else {
                record_play.setVisibility(View.GONE);
            }
            if (!CommonUtils.isEmpty(helpListBean.getReportAddress())){
                address_tv.setText(helpListBean.getReportAddress());
            }

        }
    }

//    private Map<Integer,byte[]> datas = new Hashtable<>();

//    private void loadPic(final int count, final List<String> pics) {
//        size = pics.size();
//        threadPool = ThreadPool.getThreadPool(1);
////        showDialog(true);
////        for (int i = 0; i < runnables.size(); i++) {
////            threadPool.execute(runnables.get(i));
////        }
//
//        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
//        showDialog(true);
//        for (int i = 0; i < pics.size(); i++) {
//            final int index = i;
//            final String s = pics.get(i);
//            singleThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    CommonUtils.loadPic(mBaseActivity, s, new CommonUtils.LoadPic() {
//                        @Override
//                        public void loadPic(byte[] bytes) {
//                            datas.put(index,bytes);
////                            path.add(bytes);
//                            if (datas.size() == size){
//                                for (int j = 0; j < datas.size(); j++) {
//                                    path.add(datas.get(j));
//                                }
//                                showDialog(false);
//                                threadPool.destroy();
//                                adapter.notifyDataSetChanged();
//                            }
//
//                        }
//                    });
//                    String threadName = Thread.currentThread().getName();
//                    Log.v(TAG, "线程："+threadName+",正在执行第" + (index) + "个任务");
//                }
//            });
//        }
//
////        if (count < pics.size()){
////            showDialog(true);
////            CommonUtils.loadPic(mBaseActivity, pics.get(count), new CommonUtils.LoadPic() {
////                @Override
////                public void loadPic(byte[] bytes) {
////                    path.add(bytes);
////                    adapter.notifyDataSetChanged();
////                    int c = count + 1;
////                    if (c < pics.size()){
////                        HelpDetailActivity.this.loadPic(c,pics);
////                    }else {
////                        showDialog(false);
////                    }
////
////                }
////            });
////        }
//
////        CommonUtils.getPic(mBaseActivity, pics.get(count), new CommonUtils.GetPic() {
////            @Override
////            public void getPic(File pic) {
////                try {
////                    path.add(pic.getPath());
////                    adapter.notifyDataSetChanged();
////                    int c = count + 1;
////                    if (c < pics.size()){
////                        loadPic(c,pics);
////                    }
////                } catch (Exception ignore) {
////                }
////            }
////        }, 0);
//    }
}
