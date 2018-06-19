package com.hz.junxinbaoan.activity.help;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.czt.mp3recorder.MP3Recorder;
import com.google.gson.Gson;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.dialog.RecordDialog;
import com.hz.junxinbaoan.adapter.PhotoRecyclerAdapter;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.common.Settings;
import com.hz.junxinbaoan.params.CommitHelpParam;
import com.hz.junxinbaoan.params.FileParams;
import com.hz.junxinbaoan.result.ErrorResult;
import com.hz.junxinbaoan.utils.AudioPlayerUtil;
import com.hz.junxinbaoan.utils.BitmapUtil;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.HelpGlideImageLoader;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.PopupWindowFactory;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.utils.TimeUtils;
import com.hz.junxinbaoan.view.MyLayout;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import com.autonavi.ae.gmap.maploader.ERROR_CODE;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class HelpCommitActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    private static final String TAG = "HelpCommitActivity";
    @BindView(R.id.record)
    Button record;
    @BindView(R.id.photo)
    Button photo;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.photo_rv)
    RecyclerView photo_rv;
    @BindView(R.id.record_rl)
    RelativeLayout record_rl;
    @BindView(R.id.record_play)
    Button record_play;
    @BindView(R.id.del)
    ImageView del;
    @BindView(R.id.list_fl)
    FrameLayout list_fl;
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.map)
    TextureMapView mapView;
    @BindView(R.id.myLayout)
    MyLayout myLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.address_tv)
    TextView address_tv;
    @BindView(R.id.commit_btn)
    net.qiujuer.genius.ui.widget.Button commit_btn;
    @BindView(R.id.content_et)
    EditText content_et;
    @BindView(R.id.howlong)
    TextView howlong;
    //录音相关
    private PopupWindowFactory mPop;
    private ImageView mImageView;
    private TextView mTextView;
    private AudioPlayerUtil audioPlayerUtil;
    private MP3Recorder mRecorder;

    private String mRecordPath;// 录音的存储名称
    private float mRecord_Time;// 录音的时间
    private final int MIN_TIME = 2;// 最短录音时间
    private String videoPath;//录音地址
    private boolean mPlayState = false;//播放状态

    //图片相关
    private ArrayList<String> path = new ArrayList<>();
    private PhotoRecyclerAdapter adapter;
    private int uploadCount = 0;

    //地图相关
    private AMap aMap;
    private AMapLocationClient mlocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private LatLonPoint searchLatlonPoint;
    private GeocodeSearch geocoderSearch;
    private AMapLocationClient locationClient;
    private String lat;
    private String lon;
    private Marker locationMarker;
    private GeocodeSearch geocodeSearch;

    private String upLoadVideoPath;//上传录音文件路径
    private List<String> picPath;//上传图片路径
    private ArrayList<PermissionItem> permissionItems;


    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
//    private GalleryPhotoAdapter photoAdapter;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_help_commit );
        mapView = (TextureMapView) findViewById( R.id.map );
        Log.i( TAG, "findViews: "+mapView );
        mapView.onCreate( savedInstanceState );
        //权限列表
        permissionItems = new ArrayList<PermissionItem>();
        requestPermission();
        initGallery();
    }

    private void requestPermission() {
        Log.e( TAG, "requestPermission: -----" );
        permissionItems.clear();
        permissionItems.add( new PermissionItem( Manifest.permission.ACCESS_COARSE_LOCATION, "Location", R
                .drawable.permission_ic_location ) );
        permissionItems.add( new PermissionItem( Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage", R
                .drawable.permission_ic_storage ) );
        permissionItems.add( new PermissionItem( Manifest.permission.READ_EXTERNAL_STORAGE ) );
        HiPermission.create( mBaseActivity )
                .permissions( permissionItems )
                .animStyle( R.style.PermissionAnimModal )//设置动画
                .style( R.style.PermissionDefaultBlueStyle )//设置主题
                .checkMutiPermission( new PermissionCallback() {
                    @Override
                    public void onClose() {
                    }

                    @Override
                    public void onFinish() {
                        initMap();
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                    }
                } );

    }

    /**
     * 图片的初始化
     */
    private void initGallery() {
        Log.e( TAG, "initGallery: ----" );
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i( TAG, "onStart: 开启" );
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i( TAG, "onSuccess: 返回数据" );
                path.clear();
                for (String s : photoList) {
                    Log.i( TAG, s );
                    path.add( s );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                Log.i( TAG, "onCancel: 取消" );
            }

            @Override
            public void onFinish() {
                Log.i( TAG, "onFinish: 结束" );
            }

            @Override
            public void onError() {
                Log.i( TAG, "onError: 出错" );
            }
        };

    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.clear();
            setUpMap();
        }
        //地理搜索类
        geocodeSearch = new GeocodeSearch( this );
        geocodeSearch.setOnGeocodeSearchListener( this );
//        mapView.getChildAt(1).setVisibility(View.GONE);

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

    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation( latLng );
        locationMarker = aMap.addMarker( new MarkerOptions()
                .anchor( 0.5f, 0.5f )
                .icon( BitmapDescriptorFactory.fromResource( R.mipmap.location_icon ) ) );
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels( screenPosition.x, screenPosition.y );

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * 移动地图结束事件的回调
     */
    @Override
    public void onCameraChangeFinish(final CameraPosition cameraPosition) {

        LatLng target = cameraPosition.target;
        lat = target.latitude + "";
        lon = target.longitude + "";
        Log.d( "HelpCommitActivity", "移动结束 经度：" + lat + "    纬度：" + lon );
        getAddressByLatlng( new LatLng( target.latitude, target.longitude ) );
    }

    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint( latLng.latitude, latLng.longitude );
        RegeocodeQuery query = new RegeocodeQuery( latLonPoint, 500f, GeocodeSearch.AMAP );
        //异步查询
        geocodeSearch.getFromLocationAsyn( query );
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource( this );// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled( false );// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled( true );// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType( AMap.LOCATION_TYPE_LOCATE );

        aMap.setOnMapLoadedListener( new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter();
            }
        } );
        aMap.setOnCameraChangeListener( this );// 对amap添加移动地图事件监听器
    }

    @Override
    protected void initViews() {
        super.initViews();

        galleryConfig = new GalleryConfig.Builder()
                .imageLoader( new HelpGlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack( iHandlerCallBack )     // 监听接口（必填）
                .provider( "com.hz.junxinbaoan.fileProvider" )   // provider(必填)
                .pathList( path )                         // 记录已选的图片
                .multiSelect( true )                      // 是否多选   默认：false
                .multiSelect( true, 9 )                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize( 9 )                             // 配置多选时 的多选数量。    默认：9
                .crop( false )                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop( false, 1, 1, 500, 500 )             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera( true )                     // 是否现实相机按钮  默认：false
                .filePath( "/" + getPackageName() + "/pic" )          // 图片存放路径
                .build();

        picPath = new ArrayList<>();
        //解决地图与scrollView滑动冲突
        myLayout.setScrollView( scrollView );
        audioPlayerUtil = new AudioPlayerUtil();
        GridLayoutManager gridLayoutManager = new GridLayoutManager( this, 3 );
        photo_rv.setLayoutManager( gridLayoutManager );
        adapter = new PhotoRecyclerAdapter( this, path, new PhotoRecyclerAdapter.TakePhoto() {
            @Override
            public void takePhoto() {
                permissionItems.clear();
                permissionItems.add( new PermissionItem( Manifest.permission.CAMERA, "Camera", R.drawable
                        .permission_ic_camera ) );
                permissionItems.add( new PermissionItem( Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage", R
                        .drawable.permission_ic_storage ) );
                permissionItems.add( new PermissionItem( Manifest.permission.READ_EXTERNAL_STORAGE ) );
                HiPermission.create( mBaseActivity )
                        .permissions( permissionItems )
                        .animStyle( R.style.PermissionAnimModal )//设置动画
                        .style( R.style.PermissionDefaultBlueStyle )//设置主题
                        .checkMutiPermission( new PermissionCallback() {
                            @Override
                            public void onClose() {
                                Log.e( "TAG", "close" );
//                                finish();
                            }

                            @Override
                            public void onFinish() {
                                Log.e( "TAG", "onFinish" );
                                galleryConfig.getBuilder().isOpenCamera( false ).build();
                                initPermissions();

//                                // 开启图片选择器
//                                ImageConfig imageConfig
//                                        = new ImageConfig.Builder(
//                                        // GlideLoader 可用自己用的缓存库
//                                        new GlideLoader() )
//                                        // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
//                                        .steepToolBarColor( getResources().getColor( R.color.app_style_blue ) )
//                                        // 标题的背景颜色 （默认黑色）
//                                        .titleBgColor( getResources().getColor( R.color.app_style_blue ) )
//                                        // 提交按钮字体的颜色  （默认白色）
//                                        .titleSubmitTextColor( getResources().getColor( R.color.white ) )
//                                        // 标题颜色 （默认白色）
//                                        .titleTextColor( getResources().getColor( R.color.white ) )
//                                        // 开启多选   （默认为多选）  (单选 为 singleSelect)
//                                        // .singleSelect()
//                                        // .crop()
//                                        // 多选时的最大数量   （默认 9 张）
//                                        .mutiSelectMaxSize( 9 )
//                                        // 已选择的图片路径
//                                        .pathList( path )
//                                        // 拍照后存放的图片路径（默认 /temp/picture）
//                                        .filePath( Settings.PIC_PATH )
//                                        // 开启拍照功能 （默认开启）
//                                        .showCamera()
//                                        .requestCode( 1000 )
//                                        .build();
//                                ImageSelector.open( mBaseActivity, imageConfig );   // 开启图片选择器
                            }

                            @Override
                            public void onDeny(String permission, int position) {
                                Log.e( "TAG", permission + "close" );
                            }

                            @Override
                            public void onGuarantee(String permission, int position) {
                            }
                        } );
            }

            @Override
            public void delPhoto(int position, String url) {
                path.remove( position );
                adapter.notifyDataSetChanged();

            }
        } );
        photo_rv.setAdapter( adapter );


        //PopupWindow的布局文件
        final View view = View.inflate( this, R.layout.layout_microphone, null );
        mPop = new PopupWindowFactory( this, view );
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById( R.id.iv_recording_icon );
        mTextView = (TextView) view.findViewById( R.id.tv_recording_time );

//        mAudioRecoderUtils = new AudioRecoderUtils();
//
//        //录音回调
//        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
//
//            //录音中....db为声音分贝，time为录音时长
//            @Override
//            public void onUpdate(double db, long time) {
////                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
//                mTextView.setText(TimeUtils.long2String(time));
//                mRecord_Time = time;
//            }
//
//            //录音结束，filePath为保存路径
//            @Override
//            public void onStop(String filePath) {
////                Toast.makeText(mBaseActivity, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
////                videoPath = filePath;
////                mTextView.setText(TimeUtils.long2String(0));
////                record.setVisibility(View.GONE);
////                record_rl.setVisibility(View.VISIBLE);
////                audioPlayerUtil.setUrl(filePath);
//
//                doUploadFile(new File(filePath),2,filePath);
//            }
//        });

        //6.0以上需要权限申请
//        requestPermissions();
        StartListener();
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission( mBaseActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE ) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.i( TAG, "需要授权 " );
            if (ActivityCompat.shouldShowRequestPermissionRationale( mBaseActivity, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE )) {
                Log.i( TAG, "拒绝过了" );
                Toast.makeText( mBaseActivity, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT ).show();
            } else {
                Log.i( TAG, "进行授权" );
                ActivityCompat.requestPermissions( mBaseActivity, new String[]{Manifest.permission
                        .WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS );
            }
        } else {
            Log.i( TAG, "不需要授权 " );
            GalleryPick.getInstance().setGalleryConfig( galleryConfig ).open( mBaseActivity );
        }
    }


    @Override
    protected void addListeners() {
        super.addListeners();

        commit_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isEmpty( content_et.getText().toString() )) {
                    MyToast.showToast( mBaseActivity, "爆料内容不能为空" );
                } else if (CommonUtils.isEmpty( lat ) || CommonUtils.isEmpty( lon ) || CommonUtils.isEmpty(
                        address_tv.getText().toString() )) {
                    MyToast.showToast( mBaseActivity, "请开启定位权限并定位" );
//                    aMap.clear();
                    requestPermission();
                } else if (content_et.getText().toString().length() > 300) {
                    MyToast.showToast( mBaseActivity, "上报内容字数不能多于300字" );
                }
//                else if (picPath.size() == 0){
//                    MyToast.showToast(mBaseActivity,"请选择图片");
//                }else if (CommonUtils.isEmpty(upLoadVideoPath)){
//                    MyToast.showToast(mBaseActivity,"请录音");
//                }
                else {
                    showDialog( true );
                    if (path.size() != 0) {
                        failCount = 0;
                        if (curType == 1 && !CommonUtils.isEmpty( curFilePath ) && curFile != null) {
                            doUploadFile( curFile, curType, curFilePath );
                        } else {
                            doUploadFile( new File( path.get( 0 ) ), 1, path.get( 0 ) );
                        }

//                        for (int i = 0; i < path.size(); i++) {
//                            doUploadFile(new File(path.get(i)),1,path.get(i));
//                        if (i == picPath.size() - 1){
//                            commit(content_et.getText().toString());
//                        }
//                        }
                    } else {
                        commit( content_et.getText().toString() );
                    }

                }

            }
        } );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        } );
        //跳转列表
        list_fl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult( new Intent( mBaseActivity, HelpListActivity.class ), 100 );
            }
        } );
        //删除录音
        del.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record_rl.setVisibility( View.GONE );
                record.setVisibility( View.VISIBLE );
                if (CommonUtils.deleteFile( videoPath )) {
                    upLoadVideoPath = "";
                    Toast.makeText( mBaseActivity, "删除成功",
                            Toast.LENGTH_SHORT ).show();
                    audioPlayerUtil.stop();
                    record_play.setBackgroundResource( R.mipmap.record_play );
                    // 修改播放状态
                    mPlayState = false;
                }
            }
        } );
        //播放录音
        record_play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mPlayState) {
                    // 修改播放状态
                    mPlayState = true;
                    record_play.setBackgroundResource( R.mipmap.icon_stop );
                    // 修改播放图标

                    audioPlayerUtil.play();
                    audioPlayerUtil.mediaPlayer.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // 停止播放
                            audioPlayerUtil.stop();
                            record_play.setBackgroundResource( R.mipmap.record_play );
                            // 修改播放状态
                            mPlayState = false;
                        }
                    } );
                } else {
                    audioPlayerUtil.pause();
                    // 修改播放状态
                    record_play.setBackgroundResource( R.mipmap.record_play );
                    mPlayState = false;

                }
            }
        } );
    }

    private interface Commit {
        @FormUrlEncoded
        @POST(Constants.MESSAGEADD)
        Call<ErrorResult> commit(@FieldMap Map<String, Object> map);
    }

    private void commit(String content) {
        showDialog( true );
        Commit commit = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( Commit.class );
        CommitHelpParam param = new CommitHelpParam();
        param.setReportContent( content );
        param.setReportAddress( address_tv.getText().toString() );
        if (!CommonUtils.isEmpty( upLoadVideoPath )) {
            param.setReportAudios( new Gson().toJson( new String[]{upLoadVideoPath} ) );
        }
        if (picPath.size() != 0) {
            param.setReportPictures( new Gson().toJson( picPath.toArray( new String[picPath.size()] ) ) );
        }
        if (picPath.size() > 0)
            picPath.clear();
        param.setReportLat( lat );
        param.setReportLon( lon );
        Call<ErrorResult> call = commit.commit( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<ErrorResult>() {
            @Override
            public void onResponse(Call<ErrorResult> call, Response<ErrorResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler.OnHandleListener<ErrorResult>
                        () {
                    @Override
                    public void onSuccess(ErrorResult result) {
                        if (result.getCode().equals( "0000" )) {
                            MyToast.showToast( mBaseActivity, "提交成功" );
                            startActivity( new Intent( mBaseActivity, HelpCommitActivity.class ) );
                            startActivityForResult( new Intent( mBaseActivity, HelpListActivity.class ), 100 );
                            finish();
                        } else {
                            MyToast.showToast( mBaseActivity, result.getData() );
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
            public void onFailure(Call<ErrorResult> call, Throwable t) {
                showDialog( false );
            }
        } );

    }

    private void requestPermissions() {
        //判断是否开启摄像头权限
        if ((ContextCompat.checkSelfPermission( mBaseActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission( mBaseActivity,
                        Manifest.permission.RECORD_AUDIO ) == PackageManager.PERMISSION_GRANTED)
                ) {


            //判断是否开启语音权限
        } else {
            //请求获取摄像头权限
            ActivityCompat.requestPermissions( (Activity) mBaseActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 66 );
        }

    }

    RecordDialog dialog;

    public void StartListener() {
        record.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permissionItems.size() > 0)
                    permissionItems.clear();
                permissionItems.add( new PermissionItem( Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage", R
                        .drawable.permission_ic_storage ) );
                permissionItems.add( new PermissionItem( Manifest.permission.ACCESS_COARSE_LOCATION, "Location", R
                        .drawable.permission_ic_location ) );
                permissionItems.add( new PermissionItem( Manifest.permission.RECORD_AUDIO, "麦克风", R.drawable
                        .permission_ic_micro_phone ) );
                HiPermission.create( mBaseActivity )
                        .permissions( permissionItems )
                        .animStyle( R.style.PermissionAnimModal )//设置动画
                        .style( R.style.PermissionDefaultBlueStyle )//设置主题
                        .checkMutiPermission( new PermissionCallback() {
                            @Override
                            public void onClose() {

                            }

                            @Override
                            public void onFinish() {
                                dialog = new RecordDialog( mBaseActivity );
                                dialog.setRecordInterface( new RecordDialog.RecordInterface() {
                                    @Override
                                    public void stop() {
                                        try {
                                            mRecorder.stop();
                                            failCount = 0;
                                            doUploadFile( new File( videoPath ), 2, videoPath );
                                            Toast.makeText( mBaseActivity, "录音结束",
                                                    Toast.LENGTH_SHORT ).show();
                                        } catch (Exception ignore) {
                                        }

                                    }
                                } );
                                dialog.show();
                                mRecord_Time = 0;
                                start();
                                // mAudioRecoderUtils.startRecord();
                                Toast.makeText( mBaseActivity, "开始录音",
                                        Toast.LENGTH_SHORT ).show();
                            }

                            @Override
                            public void onDeny(String permission, int position) {

                            }

                            @Override
                            public void onGuarantee(String permission, int position) {

                            }
                        } );

            }
        } );
        //Button的touch监听
//        record.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()){
//
//                    case MotionEvent.ACTION_DOWN:
//                        mRecord_Time = 0;
//                        mPop.showAtLocation(ll, Gravity.CENTER, 0, 0);
//
////                        record.setText("松开保存");
//                        mAudioRecoderUtils.startRecord();
//
//
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//
//                        if (mRecord_Time <= MIN_TIME) {
//                            mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
//                            Toast.makeText(mBaseActivity, "录音时间太短",
//                                    Toast.LENGTH_SHORT).show();
//                        }else {
//                            mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
////                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
////                            record.setText("按住说话");
//                        }
//                        mPop.dismiss();
//
//
//                        break;
//                }
//                return true;
//            }
//        });
    }

    private void start() {

        videoPath = TimeUtils.getCurrentTime() + ".mp3";
        File recordFile = new File( Settings.VIDEO_PATH, videoPath );
        videoPath = recordFile.getPath();
        mRecorder = new MP3Recorder( recordFile );
        try {
            mRecorder.start();
        } catch (IOException ignore) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i( TAG, "同意授权" );
                GalleryPick.getInstance().setGalleryConfig( galleryConfig ).open( mBaseActivity );
            } else {
                Log.i( TAG, "拒绝授权" );
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == 100) {
            clearData();
        }

//        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
//            List<String> pathList = data.getStringArrayListExtra( ImageSelectorActivity.EXTRA_RESULT );
//            path.clear();
//            path.addAll( pathList );
//            adapter.notifyDataSetChanged();
//        }
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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (audioPlayerUtil != null) {
            audioPlayerUtil.stop();
            // 修改播放状态
            record_play.setBackgroundResource( R.mipmap.record_play );
            mPlayState = false;
        }
        //在activity执行onPause时执行mapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayerUtil != null) {
            audioPlayerUtil.stop();
        }
        //在activity执行onDestroy时执行mapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        //在activity执行onSaveInstanceState时执行mapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState( outState );
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient( this );
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            locationClient.setLocationListener( this );
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
        Log.e( TAG, "onLocationChanged: HelpCommit" + aMapLocation.getErrorCode() + "" );
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mListener.onLocationChanged( aMapLocation );
            //移动地图中心到当前的定位位置
            moveMapAndRefreshList( aMapLocation.getLatitude(), aMapLocation.getLongitude() );
            //获取定位信息
            lat = aMapLocation.getLatitude() + "";
            lon = aMapLocation.getLongitude() + "";
        }
    }

    private void moveMapAndRefreshList(double Lat, double Lon) {
        aMap.moveCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( Lat, Lon ), 15 ) );
    }

    //得到逆地理编码异步查询结果
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
//        simpleAddress = formatAddress.substring(9);
        address_tv.setText( formatAddress );
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    private interface UploadFile {
        @Multipart
        @POST(Constants.UploadFile)
        Call<ErrorResult> uploadFile(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> map);
    }

    private int failCount = 0;
    private File curFile;
    private int curType;
    private String curFilePath;

    /*  type: 1：照片上传   2：录音上传   */
    private void doUploadFile(final File file, final int type, final String filepath) {
        showDialog( true );
        curFile = file;
        curType = type;
        curFilePath = filepath;
        Subscription subscribe = rx.Observable.create( new rx.Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                if (type == 1 && file.length() > 200 * 1024) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;//设为真时 获取图片实际宽高
                    BitmapFactory.decodeFile( file.getAbsolutePath(), options );
                    final int height = options.outHeight;
                    final int width = options.outWidth;
                    //缩放比例
                    int ratio = 1;
                    if (height > 1280 || width > 720) {
                        final int heightRation = Math.round( (float) height / (float) 1280 );
                        final int widthRation = Math.round( (float) width / (float) 720 );
                        ratio = heightRation < widthRation ? heightRation : widthRation;
                    }
                    options.inSampleSize = ratio;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile( file.getPath(), options );
                    //是否有旋转角度
                    int degree = 0;
                    try {
                        ExifInterface exifInterface = new ExifInterface( file.getAbsolutePath() );
                        int orientation = exifInterface.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL );
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                degree = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                degree = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                degree = 270;
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (degree != 0)
                        bitmap = BitmapUtil.rotateBitmap( bitmap, degree );
                    File resultFile = new File( Settings.TEMP_PATH, UUID.randomUUID() + ".jpg" );
                    try {
                        FileOutputStream fos = new FileOutputStream( resultFile );
                        bitmap.compress( Bitmap.CompressFormat.JPEG, 60, fos );
                    } catch (Exception e) {

//                       Toast.makeText(mBaseActivity,"录音过长请重新录音",Toast.LENGTH_LONG).show();
                    }
                    subscriber.onNext( resultFile );
                    subscriber.onCompleted();
                } else {
                    subscriber.onNext( file );
                    subscriber.onCompleted();
                }
            }
        } )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Action1<File>() {
                    @Override
                    public void call(final File file) {
                        // System.out.println("file legnth: "+ file.length());
                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        //设置log显示的内容
                        if (Settings.DEBUG)
                            logging.setLevel( HttpLoggingInterceptor.Level.HEADERS );
                        else
                            logging.setLevel( HttpLoggingInterceptor.Level.BASIC );
                        //给HttpLoggingInterceptor添加log
                        //OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
                        OkHttpClient okHttpClient = CommonUtils.getUnsafeOkHttpClient( logging );
                        //生成retrofit
                        Retrofit retrofit = new Retrofit.Builder().client( okHttpClient )
                                .addConverterFactory( GsonConverterFactory.create() )
                                .baseUrl( Constants.BASE_URL ).build();
                        UploadFile upload = retrofit.create( UploadFile.class );
                        FileParams param = new FileParams();
                        param.setType( "report" );
                        Log.e( TAG, "call: " + param.toString() );
                        Map<String, Object> postMap = CommonUtils.getPostMap( param );
                        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
                        for (Map.Entry<String, Object> temp : postMap.entrySet()) {
                            if (temp.getKey() != "file")
                                map.put( temp.getKey(),
                                        RequestBody.create( MediaType.parse( "multipart/form-data" ), (String) temp
                                                .getValue() ) );
                        }
                        final RequestBody requestFile = RequestBody.create( MediaType.parse( "multipart/form-data" ),
                                file );
                        MultipartBody.Part body = MultipartBody.Part.createFormData( "file", file.getName(),
                                requestFile );
                        final Call<ErrorResult> call = upload.uploadFile( body, map );
                        final File localfile = file;
                        call.enqueue( new Callback<ErrorResult>() {
                            @Override
                            public void onResponse(Call<ErrorResult> call, Response<ErrorResult> response) {
                                showDialog( false );
                                ResultHandler.Handle( getApplicationContext(), response.body(), false, new
                                        ResultHandler.OnHandleListener<ErrorResult>() {

                                            @Override
                                            public void onSuccess(ErrorResult outResult) {
                                                Log.e( TAG, "onSuccess: " + outResult.toString() );
                                                String filePath = outResult.getData();
                                                if (!CommonUtils.isEmpty( filePath )) {
                                                    Log.e( "fileurl", filePath );
                                                    if (type == 1) {
                                                        picPath.add( filePath );
                                                        uploadCount++;
                                                        if (uploadCount < path.size())
                                                            doUploadFile( new File( path.get( uploadCount ) ), 1,
                                                                    path.get( uploadCount ) );
                                                        if (picPath.size() == path.size()) {
                                                            commit( content_et.getText().toString() );
                                                        }
//                                                pathToUrl.put(filepath,filePath);
//                                                adapter.notifyDataSetChanged();
                                                    } else if (type == 2) {
                                                        upLoadVideoPath = filePath;
                                                        videoPath = filepath;
                                                        mTextView.setText( TimeUtils.long2String( 0 ) );
                                                        record.setVisibility( View.GONE );
                                                        record_rl.setVisibility( View.VISIBLE );
                                                        audioPlayerUtil.setUrl( filepath );
//                                                howlong.setText(audioPlayerUtil.getAudioTime()+"");
                                                    }
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
                            public void onFailure(Call<ErrorResult> call, Throwable t) {
                                failCount++;
                                if (failCount <= 5) {
                                    doUploadFile( file, type, filepath );
                                } else {
                                    clearData();
                                    showDialog( false );
                                    MyToast.showToast( mBaseActivity, "上传失败，请重试" );
                                }
                            }
                        } );
                    }

                } );
    }

    private void clearData() {
        failCount = 0;
        uploadCount = 0;
        upLoadVideoPath = "";
        picPath.clear();
        curFile = null;
        curFilePath = null;
        curType = 0;
    }

}
