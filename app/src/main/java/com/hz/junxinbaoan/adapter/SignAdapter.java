package com.hz.junxinbaoan.adapter;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ExifInterface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.common.Settings;
import com.hz.junxinbaoan.data.FindIbeaconBean;
import com.hz.junxinbaoan.data.SignDetailBean;
import com.hz.junxinbaoan.params.FileParams;
import com.hz.junxinbaoan.params.FindIbeaconParam;
import com.hz.junxinbaoan.params.SignParam;
import com.hz.junxinbaoan.result.ErrorResult;
import com.hz.junxinbaoan.result.FindIbeaconResult;
import com.hz.junxinbaoan.utils.BitmapUtil;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.DensityUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;
import com.hz.junxinbaoan.utils.bluetooth.IBeacon;
import com.hz.junxinbaoan.view.MultiValueMap;
import com.hz.junxinbaoan.view.MyListView;
import com.hz.junxinbaoan.view.TimeTextView;

import net.qiujuer.genius.ui.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 *
 * 考勤的日常,加班,临时 的adapter
 * Created by Administrator on 2017/10/24 0024.
 */

public class SignAdapter extends RecyclerView.Adapter implements LocationSource, AMapLocationListener {

    private static final String TAG = "SignAdapter";
    private final boolean isFromSchedue;
    private boolean uploadSign;//是否已经签到成功
    private List<SignDetailBean> datas;
    private List<IBeacon> iBeacons;
    private MultiValueMap<Integer, String> photoType1Path;
    private MultiValueMap<Integer, String> photoType2Path;
    private List<String> picPath;//已经上传的图片路径
    private Context context;

    private final int TYPE_ZERO = 0;
    private final int TYPE_ONE = 1;
    private final int TYPE_TWO = 2;
    private final int TYPE_THREE = 3;
    private final int TYPE_FOUR = 4;

//    private AMap aMap;
//    private TextureMapView map;

    private Map<Integer, AMap> aMaps = new Hashtable<>();
    private Map<Integer, TextureMapView> maps = new Hashtable<>();

    private String mAddress;
    private Map<Integer, BlueToothAdapter> adapters = new Hashtable<>();

    private ArrayList<PermissionItem> permissionItems;
    //    BlueToothAdapter adapter;
    private TextView location_tv;
    private double latitude;//获取纬度
    private double longitude;//获取经度

//    private double newLongitude;//获取经度
//    private double nowLatitude;//获取纬度

    private IBeacon curiBeacon;

    private RefreshUIInterface refreshUIInterface;
    private TakePhotoUIInterface takePhotoUIInterface;
    private PhotoRecyclerSignAdapter photoAdapter;
    private int uploadCount = 0;
    private int failCount = 0;
    //    private TextureMapView map;
//    private SeePhotoRecyclerAdapter photo3Adapter;
    private SeePhotoRecyclerOverAdapter photo3Adapter;
    private Map<Integer, ArrayList<String>> pathMap = new HashMap<>();
//    private SeePhotoRecyclerAdapter photo1Adapter;//上传成功的展示
    private ArrayList<String> path2 = new ArrayList<>();
    private int takePhotoIbeaconPos = -1;

    public interface RefreshUIInterface {
        void refresh(boolean b);

        void showLoading(boolean show);
    }

    public interface TakePhotoUIInterface {
        void takePhotos(String holder, int position);
    }


    public SignAdapter(Context context, boolean uploadSign, RefreshUIInterface refreshUIInterface, boolean
            isFromSchedue, TakePhotoUIInterface takePhotoUIInterface, MultiValueMap<Integer, String> photoType1Path,
                       MultiValueMap<Integer, String> photoType2Path) {
        this.context = context;
        datas = new ArrayList<>();
        iBeacons = new ArrayList<>();
        permissionItems = new ArrayList<>();
        picPath = new ArrayList<>();
        this.uploadSign = uploadSign;
        this.refreshUIInterface = refreshUIInterface;
        this.isFromSchedue = isFromSchedue;
        this.takePhotoUIInterface = takePhotoUIInterface;
        this.photoType1Path = photoType1Path;
        this.photoType2Path = photoType2Path;
        Log.e( TAG, "SignAdapter: ====" );
    }

    public void clearData() {
        datas = new ArrayList<>();
//        notifyDataSetChanged();
    }

    public void add(IBeacon iBeacon) {
        if (iBeacon == null)
            return;

        boolean isExist = false;
        for (int i = 0; i < iBeacons.size(); i++) {
            if (iBeacons.get( i ).getMajor() == iBeacon.getMajor()) {
                isExist = true;
                break;
            }
//            String btAddress = iBeacons.get(i).getBluetoothAddress();
//            if (btAddress.equals(iBeacon.getBluetoothAddress()) && iBeacons.get(i).getProximityUuid().startsWith
// ("99999999")) {
//                iBeacons.add(i + 1, iBeacon);
//                iBeacons.remove(i);
//                return;
//            }
        }
        if (!isExist) {
            findIbeacon( iBeacon.getMajor() );
        }
//        iBeacons.add(iBeacon);
        for (Integer key : adapters.keySet()) {
            adapters.get( key ).notifyDataSetChanged();
        }
//        if (adapter != null){
//            adapter.notifyDataSetChanged();
//        }

    }

    public void setDatas(List<SignDetailBean> datas, boolean uploadSign) {
        this.uploadSign = uploadSign;
        this.datas = datas;
        Log.e( TAG, datas.size() + " setDatas: " + maps.size() + "--" + aMaps.size() );
        Log.e( "TAG", photoType1Path.size() + "++++++44444++++++++" + uploadSign );
        notifyDataSetChanged();
    }

    public void setPhotoType1Datas(MultiValueMap<Integer, String> photoType1, int takePhotoPosition) {
        this.photoType1Path = photoType1;
        Log.e( TAG, "---" + takePhotoPosition + " setPhotoType1Datas: 照片返回" );
        photoAdapter.notifyDataSetChanged();
    }

    public void setPhotoType2Datas(MultiValueMap<Integer, String> photoType2, int takePhotoPosition) {
        this.photoType2Path = photoType2;
        Log.e( TAG, "---" + takePhotoPosition + " setPhotoType1Datas: 照片返回" );
        photoAdapter.notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        int type = datas.get( position ).getAttendanceStatusType();
        switch (type) {
            case 1:
                return TYPE_ONE;
            case 2:
                return TYPE_TWO;
            case 3:
                return TYPE_THREE;
            case 4:
                return TYPE_FOUR;
            default:
                return TYPE_ZERO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ZERO:
                view = LayoutInflater.from( context ).inflate( R.layout.item_sign_type0, parent, false );
                return new VH0( view );
            case TYPE_ONE:
                view = LayoutInflater.from( context ).inflate( R.layout.item_sign_type1, parent, false );
                return new VH1( view );
            case TYPE_TWO:
                view = LayoutInflater.from( context ).inflate( R.layout.item_sign_type2, parent, false );
                return new VH2( view );
            case TYPE_THREE:
                view = LayoutInflater.from( context ).inflate( R.layout.item_sign_type3, parent, false );
                return new VH3( view );
            case TYPE_FOUR:
                view = LayoutInflater.from( context ).inflate( R.layout.item_sign_type4, parent, false );
                return new VH4( view );
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SignDetailBean bean = datas.get( position );
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( bean.getAttendanceTimestampExpect() );
        if (holder instanceof VH0) {
            VH0 vh = (VH0) holder;
            setCommonUI( bean, vh.address_tv, vh.time_tv, bean.getAttendanceTimeExpect(), vh.title_tv, position );
            String attendanceSecondsLeft = bean.getAttendanceSecondsLeft();
            if (isFromSchedue || CommonUtils.isEmpty( attendanceSecondsLeft )) {
                vh.sign_time_tv.setVisibility( View.GONE );
                ViewGroup.LayoutParams params = vh.line.getLayoutParams();
                params.height = DensityUtils.dp2px( context, 50 );
                vh.line.setLayoutParams( params );
            } else {
                int time = Integer.parseInt( attendanceSecondsLeft );
                if (time != 0) {
                    vh.sign_time_tv.setVisibility( View.VISIBLE );
                    vh.sign_time_tv.setTimes( time );
                } else {//可以进行签到了,重新刷新界面
                    refreshUIInterface.refresh( false );
                }
            }
//            ViewGroup.LayoutParams params = vh.line.getLayoutParams();
//            ViewGroup.LayoutParams box = vh.ll.getLayoutParams();
//            params.height= (int) ((double)vh.ll.getHeight()*0.8);
//            Log.i("czx","height0:"+ params.height+"?"+vh.ll.getHeight());
//            vh.line.setLayoutParams(params);
            if (0 == datas.get( position ).getEndType())
                vh.line0_sep_start.setVisibility( View.VISIBLE );
            else
                vh.line0_sep_start.setVisibility( View.GONE );

            if (2 == datas.get( position ).getEndType()) {
                vh.line.setVisibility( View.GONE );
                if (position == getItemCount() - 1)
                    vh.line0_sep_end.setVisibility( View.VISIBLE );
                else
                    vh.line0_sep_end.setVisibility( View.GONE );
            } else {
                vh.line0_sep_end.setVisibility( View.GONE );
                vh.line.setVisibility( View.VISIBLE );
            }

        } else if (holder instanceof VH1) {
            Log.e( "TAG", isFromSchedue + "VH1 onBindViewHolder:  " + (position == getItemCount() - 1) );
            final VH1 vh = (VH1) holder;
            location_tv = vh.location_tv;

            if (false == isFromSchedue) {
                vh.map.setVisibility( View.VISIBLE );
                vh.photo_lv_type1.setVisibility( View.VISIBLE );
                vh.fl_btn.setVisibility( View.VISIBLE );
                TextureMapView map = maps.get( position );
                AMap aMap = aMaps.get( position );
                if (map == null) {
                    map = vh.map;
                    map.onDestroy();
                    map.onCreate( null );
                }
                map.onResume();
                initMap( map, position );
            } else {
                vh.map.setVisibility( View.GONE );
                vh.fl_btn.setVisibility( View.GONE );
            }

            calculateHeight( vh.rl_root, vh.line );

            Log.e( "TAG", uploadSign + "VH1 : " + isFromSchedue + "初始化照片===" + photoType1Path.size() );
            //初始化照片
            ArrayList<String> values = photoType1Path.getValues( position );
            if (values == null || values.size() == 0)
                values = new ArrayList<>();
            initPhotoAdapter( vh.photo_lv_type1, values, "VH1", position );
            vh.photo_lv_type1.setAdapter( photoAdapter );

            setCommonUI( bean, vh.address_tv, vh.time_tv, bean.getAttendanceTimeExpect(), vh.title_tv, position );
            if (0 == bean.getAttendanceType() && locationClient != null) {//gps 定位  需要判断位置
                locationClient.setLocationListener( new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        Log.i( TAG, "onBindViewHolder: " + bean.getAttendanceLatExpect() + " --" + bean
                                .getAttendanceLonExpect() + "--" + latitude + "---" + longitude + "-nowLatitude-" );
                        Double R = 6370996.81;  //地球的半径
                        Double x = (longitude - bean.getAttendanceLonExpect()) * Math.PI * R * Math.cos( (
                                (bean.getAttendanceLatExpect() + latitude) / 2) * Math.PI / 180 ) / 180;
                        Double y = (latitude - bean.getAttendanceLatExpect()) * Math.PI * R / 180;
                        Double distance = Math.hypot( x, y );   //得到两点之间的直线距离
                        Log.i( TAG, distance + " initMap: " + bean.getDistanceGps() );
                        if (distance > bean.getDistanceGps()) {
                            Log.i( TAG, "onBindViewHolder: " + "不在签到位置" );
                            vh.sign_btn.setText( "不在签到位置" );//需要判断是否在签到位置
                            vh.sign_btn.setEnabled( false );
                            vh.fl_btn.setBackgroundResource( com.hz.junxinbaoan.R.mipmap.sign_gray_bg );
                        } else {
                            vh.sign_btn.setEnabled( true );
                            //签到按钮
                            vh.sign_btn.setText( "签到" );
                            vh.fl_btn.setBackgroundResource( com.hz.junxinbaoan.R.mipmap.btn );
                            vh.sign_btn.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.i( TAG, "gps onClick: " + position + "===" + photoType1Path.containsKey(
                                            position ) );
//                                    photoAdapter.notifyDataSetChanged();
//                                    ArrayList<String> photoList = photoType1Path.getValues( position );
//                                    Log.i( TAG, photoList.size() + " ,onClick: " + photoList.toString() );
                                    ArrayList<String> values = photoType1Path.getValues( position );
                                    if (values == null || values.size() == 0) {
                                        Log.i( TAG, "onClick: 没有图片" );
                                        sign( bean.getAttendanceId(), 0, position );
                                    } else {
                                        Log.i( TAG, position + "onClick: 有图片" + values.size() + "===" + values.toString
                                                () );
                                        doUploadImgsFile( new File( values.get( 0 ) ), values.get( 0 ),
                                                bean, values, position );
                                    }
                                }
                            } );
                        }
                    }
                } );
            } else {
                vh.sign_btn.setText( "签到" );
                vh.fl_btn.setBackgroundResource( R.mipmap.btn );
                vh.sign_btn.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i( TAG, "onClick: " + position + "===" + photoType1Path.containsKey( position ) );
                        ArrayList<String> values = photoType1Path.getValues( position );
                        if (values == null || values.size() == 0) {
                            Log.i( TAG, "onClick: 没有图片" );
                            sign( bean.getAttendanceId(), 0, position );
                        } else {
                            Log.i( TAG, "onClick: 有图片" + values.size() + "===" + values.toString() );
                            doUploadImgsFile( new File( values.get( 0 ) ), values.get( 0 ),
                                    bean, values, position );
                        }
                    }
                } );
            }
//            ViewGroup.LayoutParams params = vh.line.getLayoutParams();
//            ViewGroup.LayoutParams box = vh.ll.getLayoutParams();
//            params.height= (int) ((double)box.height*0.8);
//            Log.i("czx","height:"+ params.height+"?"+box.height);
//            vh.line.setLayoutParams(params);
            if (0 == datas.get( position ).getEndType())
                vh.line1_sep_start.setVisibility( View.VISIBLE );
            else
                vh.line1_sep_start.setVisibility( View.GONE );

            if (2 == datas.get( position ).getEndType()) {
                vh.line.setVisibility( View.GONE );
                if (position == getItemCount() - 1)
                    vh.line1_sep_end.setVisibility( View.VISIBLE );
                else
                    vh.line1_sep_end.setVisibility( View.GONE );
            } else {
                vh.line.setVisibility( View.VISIBLE );
                vh.line1_sep_end.setVisibility( View.GONE );
            }
        } else if (holder instanceof VH2) {
            final VH2 vh = (VH2) holder;
            //动态计算子布局的总高度
            calculateHeight( vh.rl_root, vh.line );
            setCommonUI( bean, vh.address_tv, vh.time_tv, bean.getAttendanceTimeExpect(), vh.title_tv, position );

            if (false == isFromSchedue) {
                vh.fl_btn.setVisibility( View.VISIBLE );
                vh.rl_refresh.setVisibility( View.VISIBLE );
                vh.bluetooth_lv.setVisibility( View.VISIBLE );

                ArrayList<String> values2 = photoType2Path.getValues( position );
                if (values2 == null || values2.size() == 0)
                    values2 = new ArrayList<>();
                initPhotoAdapter( vh.photo_lv_type2, values2, "VH2", position );
                vh.photo_lv_type2.setAdapter( photoAdapter );

                //相机拍照
//                initPhotoAdapter( vh.photo_lv_type2, photoType2Path, "VH2", position );
                BlueToothAdapter adapter = new BlueToothAdapter( iBeacons, context, new BlueToothAdapter
                        .GetBlueToothInfoListener() {
                    @Override
                    public void getBlutToothInfo(IBeacon iBeacon) {
                        if (!CommonUtils.isEmpty( iBeacon.getIbeaconComment() )) {
                            vh.cur_tv.setText( "当前：" + iBeacon.getIbeaconComment() );
                        }
                        curiBeacon = iBeacon;
                    }
                } );
                vh.bluetooth_lv.setAdapter( adapter );
                adapters.put( position, adapter );
            } else {
                vh.fl_btn.setVisibility( View.GONE );
                vh.rl_refresh.setVisibility( View.GONE );
                vh.bluetooth_lv.setVisibility( View.GONE );
            }

            vh.refresh_iv.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iBeacons = new ArrayList<IBeacon>();
                    vh.cur_tv.setText( "当前：" );
                    curiBeacon = null;
//                    adapter.setiBeacons(iBeacons);
                    adapters.get( position ).setiBeacons( iBeacons );
                }
            } );

            //签到按钮
            vh.sign_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (curiBeacon != null) {
                        ArrayList<String> values = photoType2Path.getValues( position );
                        if (values == null || values.size() == 0) {
                            Log.i( TAG, "onClick: 没有图片" );
                            sign( bean.getAttendanceId(), 1, position );
                        } else {
                            Log.i( TAG, position + "onClick: 有图片" + values.size() + "===" + values.toString
                                    () );
                            doUploadImgsFile( new File( values.get( 0 ) ), values.get( 0 ),
                                    bean, values, position );
                        }
//                        if (photo2Map.size() > 0) {
//                            for (Integer key : photo2Map.keySet()) {
//                                Log.e( TAG, key + " onBindViewHolder: " + position + "===" );
//                                if (key == position) {
//                                    ArrayList<String> list = photo2Map.get( key );
//                                    if (list.size() > 0)  //先进行图片的上传
//                                        doUploadImgsFile( new File( photoType2Path.get( 0 ) ), photoType2Path.get( 0
//                                                ), bean,
//                                                photoType2Path );
//                                    else //没有上传图片
//                                        sign( bean.getAttendanceId(), 1 );
//                                }
//                            }
//                        } else {
//                            sign( bean.getAttendanceId(), 0 );
//                        }
                    }
//                        if (photoType2Path.size() > 0)  //先进行图片的上传
//                            doUploadImgsFile( new File( photoType2Path.get( 0 ) ), photoType2Path.get( 0 ), bean,
//                                    photoType2Path );
//                        else //没有上传图片
//                            sign( bean.getAttendanceId(), 1 );
                    else
                        MyToast.showToast( context, "请选择蓝牙基站" );
                }
            } );

            if (0 == datas.get( position ).getEndType())
                vh.line2_sep_start.setVisibility( View.VISIBLE );
            else
                vh.line2_sep_start.setVisibility( View.GONE );

            if (2 == datas.get( position ).getEndType()) {
                vh.line.setVisibility( View.GONE );
                if (position == getItemCount() - 1)
                    vh.line2_sep_end.setVisibility( View.VISIBLE );
                else
                    vh.line2_sep_end.setVisibility( View.GONE );
            } else {
                vh.line2_sep_end.setVisibility( View.GONE );
                vh.line.setVisibility( View.VISIBLE );
            }
        } else if (holder instanceof VH3) {
            VH3 vh = (VH3) holder;

            vh.photo_lv_type3.setVisibility( View.GONE );

            GridLayoutManager gridLayoutManager = new GridLayoutManager( context, 3 );
            vh.photo_lv_type3.setLayoutManager( gridLayoutManager );
            photo3Adapter = new SeePhotoRecyclerOverAdapter( context, pathMap,position );
            vh.photo_lv_type3.setAdapter( photo3Adapter );
            setCommonUI( bean, vh.address_tv, vh.time_tv, bean.getAttendanceTimeExpect(), vh.title_tv, position );

            //加载图片
            String reportPictures = bean.getAttendancePictures();
            if (!CommonUtils.isEmpty( reportPictures ) && !reportPictures.equals( "null" )) {
                vh.photo_lv_type3.setVisibility( View.VISIBLE );
                List<String> pics = new Gson().fromJson( reportPictures, new TypeToken<List<String>>() {
                }.getType() );
                if (pics != null && pics.size() != 0) {
                    pathMap.put( position, (ArrayList<String>) pics );
                    Log.i( TAG, "onBindViewHolder VH3: " + pathMap.toString() );
                    photo3Adapter.notifyItemRangeChanged( position,0 );
//                    photo3Adapter.notifyDataSetChanged();
                }
            } else {
                vh.photo_lv_type3.setVisibility( View.GONE );
            }

            //动态计算高度
            calculateHeight( vh.rl_root, vh.line );
            if (!CommonUtils.isEmpty( bean.getAttendanceTime() )) {
                vh.sign_time_tv.setText( "签到时间：" + bean.getAttendanceTime() );
            }
            String place = "";
            switch (bean.getAttendanceType()) {
                case 0:
                    place = "(GPS) " + bean.getAttendanceAddress();
                    break;
                case 1:
                    place = "(蓝牙) " + bean.getAttendanceIbeaconName();
                    break;
                case 2:
                    place = "(任意位置) " + bean.getAttendanceAddress();
                    break;
            }
            vh.sign_address_tv.setText( "签到位置：" + place );

            if (0 == datas.get( position ).getEndType())
                vh.line3_sep_start.setVisibility( View.VISIBLE );
            else
                vh.line3_sep_start.setVisibility( View.GONE );

            if (2 == datas.get( position ).getEndType()) {
                vh.line.setVisibility( View.GONE );
                if (position == getItemCount() - 1)
                    vh.line3_sep_end.setVisibility( View.VISIBLE );
                else
                    vh.line3_sep_end.setVisibility( View.GONE );
            } else {
                vh.line3_sep_end.setVisibility( View.GONE );
                vh.line.setVisibility( View.VISIBLE );
            }
        } else if (holder instanceof VH4) {
            VH4 vh = (VH4) holder;
            setCommonUI( bean, vh.address_tv, vh.time_tv, bean.getAttendanceTimeExpect(), vh.title_tv, position );

            if (0 == datas.get(position).getEndType())
                vh.line4_sep_start.setVisibility( View.VISIBLE );
            else
                vh.line4_sep_start.setVisibility( View.GONE );

            if (2 == datas.get( position ).getEndType()) {
//            if (position == getItemCount() - 1) {
                vh.line.setVisibility( View.GONE );
                if (position == getItemCount() - 1)
                    vh.line4_sep_end.setVisibility( View.VISIBLE );
                else
                    vh.line4_sep_end.setVisibility( View.GONE );
            } else {
                vh.line.setVisibility( View.VISIBLE );
                vh.line4_sep_end.setVisibility( View.GONE );
            }
        }
//        if (holder instanceof CanSignVH){
//            CanSignVH vh = (CanSignVH) holder;
//
//            vh.map.onCreate(null);
//            vh.map.onResume();
//            initMap(vh.map);
//            if (!CommonUtils.isEmpty(bean.getTime())){
//                vh.time_tv.setText(bean.getTime());
//            }
//            if (!CommonUtils.isEmpty(bean.getSigntime())){
//                vh.work_time_tv.setText(bean.getSigntime());
//            }
//            address_tv = vh.address_tv;
//            if (!CommonUtils.isEmpty(mAddress)){
//                address_tv.setText(mAddress);
//            }
//            vh.num_tv.setText(position+1+"");
//
//
//        }else if (holder instanceof CannotSignVh){
//            CannotSignVh vh = (CannotSignVh) holder;
//
//            vh.num_tv.setText(position+1+"");
//            if (!CommonUtils.isEmpty(bean.getTime())){
//                vh.time_tv.setText(bean.getTime());
//            }
//            if (!CommonUtils.isEmpty(bean.getSigntime())){
//                vh.work_time_tv.setText(bean.getSigntime());
//            }
//
//        }else if (holder instanceof AlreadySignVh){
//            AlreadySignVh vh = (AlreadySignVh) holder;
//
//            vh.num_tv.setText(position+1+"");
//            if (!CommonUtils.isEmpty(bean.getTime())){
//                vh.time_tv.setText(bean.getTime());
//            }
//            if (!CommonUtils.isEmpty(bean.getSigntime())){
//                vh.work_time_tv.setText(bean.getSigntime());
//            }
//            if (!CommonUtils.isEmpty(bean.getAddress())){
//                vh.address_tv.setText(bean.getAddress());
//            }
//            if (bean.getLatertime() > 0){
//                vh.type_tv.setText("迟到"+bean.getLatertime()+"分钟");
//                vh.type_tv.setTextColor(Color.parseColor("#F44336"));
//            }else {
//                vh.type_tv.setText("正常");
//                vh.type_tv.setTextColor(Color.parseColor("#2195F2"));
//            }
//
//        }
    }

    /**
     * 初始化照片的adapter
     *
     * @param photo_lv_type
     * @param photoPath
     * @param holder
     */

    private void initPhotoAdapter(final RecyclerView photo_lv_type, final ArrayList<String> photoPath, final String
            holder, final int position) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager( context, 3 );
        photo_lv_type.setLayoutManager( gridLayoutManager );
        photoAdapter = new PhotoRecyclerSignAdapter( context, photoPath, new PhotoRecyclerSignAdapter.TakePhoto() {
            @Override
            public void takePhoto() {
                Log.i( "TAG", position + " takePhoto: 点击相机" );
                permissionItems.clear();
                permissionItems.add( new PermissionItem( Manifest.permission.CAMERA, "Camera", R.drawable
                        .permission_ic_camera ) );
                permissionItems.add( new PermissionItem( Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage", R
                        .drawable.permission_ic_storage ) );
                permissionItems.add( new PermissionItem( Manifest.permission.READ_EXTERNAL_STORAGE ) );
                HiPermission.create( context )
                        .permissions( permissionItems )
                        .animStyle( R.style.PermissionAnimModal )//设置动画
                        .style( R.style.PermissionDefaultBlueStyle )//设置主题
                        .checkMutiPermission( new PermissionCallback() {
                            @Override
                            public void onClose() {

                            }

                            @Override
                            public void onFinish() {
                                //调用摄像机进行拍照
                                takePhotoUIInterface.takePhotos( holder, position );
                            }

                            @Override
                            public void onDeny(String permission, int position) {

                            }

                            @Override
                            public void onGuarantee(String permission, int position) {

                            }
                        } );
            }

            @Override
            public void delPhoto(int po, String url) {
                Log.e( "TAG---", photoPath.size() + "delete======" + position + photoType1Path.values() );
                Log.e( "TAG---", photoPath.size() + "delete======" + photoType1Path.getValues( position ) );
//                //删除map中的数据
                Log.i( TAG, "delPhoto 1: " + photoType1Path.values() );
                if ("VH1".equals( holder )) {
                    File file = new File( photoPath.get( po ) );
                    ArrayList<String> values = photoType1Path.getValues( position );
                    Log.i( TAG, "delPhoto: values" + values.toString() );
                    if (values.size() > 1) {
                        values.remove( po );
                        photoType1Path.set( position, values );
                    } else {
                        file.delete();
                        photoType1Path.remove( position );
                        ArrayList<String> value = photoType1Path.getValues( position );
                        if (value == null || value.size() == 0)
                            value = new ArrayList<>();
                        initPhotoAdapter( photo_lv_type, value, "VH1", position );
                        photo_lv_type.setAdapter( photoAdapter );
                    }
                    Log.i( TAG, "delPhoto 2: " + photoType1Path.values() + "??" + photoType1Path.getValues( position
                    ) );
                } else {
                    File file = new File( photoPath.get( po ) );
                    ArrayList<String> values = photoType2Path.getValues( position );
                    Log.i( TAG, "delPhoto: values" + values.toString() );
                    if (values.size() > 1) {
                        values.remove( po );
                        photoType2Path.set( position, values );
                    } else {
                        file.delete();
                        photoType2Path.remove( position );
                        ArrayList<String> value = photoType2Path.getValues( position );
                        if (value == null || value.size() == 0)
                            value = new ArrayList<>();
                        initPhotoAdapter( photo_lv_type, value, "VH2", position );
                        photo_lv_type.setAdapter( photoAdapter );
                    }
                    Log.i( TAG, "delPhoto 2: " + photoType2Path.values() + "??" + photoType2Path.getValues( position
                    ) );
                }
                photoAdapter.notifyDataSetChanged();
            }
        } );

    }

    private void calculateHeight(final RelativeLayout rl_root, final View line) {
        final ViewTreeObserver treeObserver = rl_root.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_root.getViewTreeObserver().removeGlobalOnLayoutListener( this );
                int height = rl_root.getHeight();
                int width = rl_root.getWidth();
                ViewGroup.LayoutParams params = line.getLayoutParams();
                params.height = height - 15;
                line.setLayoutParams( params );
                rl_root.getViewTreeObserver().removeGlobalOnLayoutListener( this );
            }
        } );
    }


    private interface FindIbeacon {
        @FormUrlEncoded
        @POST(Constants.FINDIBEACON)
        Call<FindIbeaconResult> findIbeacon(@FieldMap Map<String, Object> map);
    }


    private void findIbeacon(int major) {
        FindIbeacon findIbeacon = CommonUtils.buildRetrofit( Constants.BASE_URL, context ).create( FindIbeacon.class );
        FindIbeaconParam param = new FindIbeaconParam();
        param.setMajorId( major );
        Call<FindIbeaconResult> call = findIbeacon.findIbeacon( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<FindIbeaconResult>() {
            @Override
            public void onResponse(Call<FindIbeaconResult> call, Response<FindIbeaconResult> response) {
                ResultHandler.Handle( context, response.body(), new ResultHandler.OnHandleListener<FindIbeaconResult>
                        () {
                    @Override
                    public void onSuccess(FindIbeaconResult result) {
                        FindIbeaconBean data = result.getData();
                        if (data != null) {
                            boolean isExist = false;
                            for (int i = 0; i < iBeacons.size(); i++) {
                                if (iBeacons.get( i ).getMajor() == data.getIbeaconMajorId()) {
                                    isExist = true;
                                    break;
                                }
                            }
                            if (!isExist)
                                iBeacons.add( new IBeacon( data.getIbeaconMajorId(), data.getIbeaconId(), data
                                        .getIbeaconComment() ) );
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
            public void onFailure(Call<FindIbeaconResult> call, Throwable t) {
                Log.d( "xx", "xx" );
            }
        } );
    }

    /**
     * 照片上传
     */
    private interface UploadImg {
        @Multipart
        @POST(Constants.UploadFile)
        Call<ErrorResult> uploadFile(@Part MultipartBody.Part file, @PartMap Map<String, RequestBody> map);
    }

    private File curFile;
    private String curFilePath;

    /* 先进行照片上传  */
    private void doUploadImgsFile(final File file, final String filepath, final SignDetailBean bean,
                                  final ArrayList<String> photoArray, final int position) {
        curFile = file;
        curFilePath = filepath;
        refreshUIInterface.showLoading( true );
        Subscription subscribe = Observable.create( new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                // 判断如果图片大于200K,进行压缩避免在生成图
                if (file.length() > 200 * 1024) {
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
                    Log.e( TAG, "call:resultFile  " + resultFile.length() );
                    try {
                        FileOutputStream fos = new FileOutputStream( resultFile );
                        bitmap.compress( Bitmap.CompressFormat.JPEG, 60, fos );
                    } catch (Exception e) {
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
                        UploadImg upload = retrofit.create( UploadImg.class );
                        FileParams param = new FileParams();
                        param.setType( "sign" );
                        Log.e( "TAG", " UploadImg call: " + param.toString() );
                        Map<String, Object> postMap = CommonUtils.getPostMap( param );
                        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
                        for (Map.Entry<String, Object> temp : postMap.entrySet()) {
                            if (temp.getKey() != "file")
                                map.put( temp.getKey(), RequestBody.create( MediaType.parse( "multipart/form-data" ),
                                        (String) temp.getValue() ) );
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
                                refreshUIInterface.showLoading( false );
                                ResultHandler.Handle( context, response.body(), false, new
                                        ResultHandler.OnHandleListener<ErrorResult>() {

                                            @Override
                                            public void onSuccess(ErrorResult outResult) {
                                                Log.e( "TAG", "UploadImg: " + outResult.toString() );
                                                String filePath = outResult.getData();
                                                if (!CommonUtils.isEmpty( filePath )) {
                                                    picPath.add( filePath );
                                                    uploadCount++;
                                                    if (uploadCount < photoArray.size())
                                                        doUploadImgsFile( new File( photoArray.get( uploadCount ) ),
                                                                photoArray.get( uploadCount ), bean, photoArray,
                                                                position );
                                                    if (picPath.size() == photoArray.size()) {
//                                                        refreshUIInterface.refresh();//进行图片上传成功的删除本地图片数据
                                                        sign( bean.getAttendanceId(), 0, position );
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onNetError() {
                                                MyToast.showToast( context, "上传失败，请重试" );
                                            }

                                            @Override
                                            public void onError(String code, String message) {
                                                MyToast.showToast( context, "上传失败，请重试" );
                                            }
                                        } );
                            }

                            @Override
                            public void onFailure(Call<ErrorResult> call, Throwable t) {
                                failCount++;
                                if (failCount <= 5) {
                                    doUploadImgsFile( file, filepath, bean, photoArray, position );
                                } else {
                                    clearPhotoData();
                                    refreshUIInterface.showLoading( false );
                                    MyToast.showToast( context, "上传失败，请重试" );
                                }
                            }
                        } );
                    }

                } );
    }

    private void clearPhotoData() {
        failCount = 0;
        uploadCount = 0;
        picPath.clear();
        curFile = null;
        curFilePath = null;
    }

    private interface Sign {
        @FormUrlEncoded
        @POST(Constants.SIGN)
        Call<ErrorResult> sign(@FieldMap Map<String, Object> map);
    }

    /*签到接口，  type：0  GPS签到  1：蓝牙签到*/
    private void sign(String attendanceId, int type, final int position) {
        if (!CommonUtils.isOPenGPS( context )) {//判断gps是否开启，如果没开启，让用户开启
            CommonUtils.openGPS( context );
            return;
        }
        refreshUIInterface.showLoading( true );
        Sign sign = CommonUtils.buildRetrofit( Constants.BASE_URL, context ).create( Sign.class );
        SignParam param = new SignParam();

        if (type == 0) {
            param.setAttendanceLat( latitude );
            param.setAttendanceLon( longitude );
            param.setAttendanceAddress( mAddress );
        } else {
            param.setAttendanceIbeaconId( curiBeacon.getIbeaconId() );
            param.setAttendanceIbeaconName( curiBeacon.getIbeaconComment() );
        }

        //是否有图片上传
        if (picPath.size() != 0) {
            param.setAttendancePictures( new Gson().toJson( picPath.toArray( new String[picPath.size()] ) ) );
        }

        param.setAttendanceId( attendanceId );
        Log.e( TAG, "sign: " + param.toString() );
        Call<ErrorResult> call = sign.sign( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<ErrorResult>() {
            @Override
            public void onResponse(Call<ErrorResult> call, Response<ErrorResult> response) {
                refreshUIInterface.showLoading( false );
                ResultHandler.Handle( context, response.body(), new ResultHandler.OnHandleListener<ErrorResult>() {
                    @Override
                    public void onSuccess(ErrorResult result) {
                        if (result.getCode().equals( "0000" )) {
                            clearPhotoData();
                            MyToast.showToast( context, "签到成功" );
                            takePhotoIbeaconPos = -1;
                            refreshUIInterface.refresh( true );
                            //设置图片不可删除 并且拍照图片隐藏
                            ArrayList<String> values = photoType1Path.getValues( position );
                            if (values != null && values.size() > 0) {
                                deletePhotos( values );
                            }
                        } else if (result.getCode().equals( "9999" )) {
                            MyToast.showToast( context, result.getData() );
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
                refreshUIInterface.showLoading( false );
            }
        } );
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

    private void setCommonUI(SignDetailBean bean, TextView place, TextView time, String times, TextView title, int
            position) {
        switch (bean.getAttendanceType()) {
            case 0:
                if (!CommonUtils.isEmpty( bean.getAttendanceAddressExpect() ))
                    place.setText( Html.fromHtml( "要求位置：<font color='#2196f3'>(GPS) </font>" + bean
                            .getAttendanceAddressExpect() ) );
                else
                    place.setText( Html.fromHtml( "要求位置：<font color='#2196f3'>(GPS) </font>" ) );
                break;
            case 1:
                if (!CommonUtils.isEmpty( bean.getAttendanceIbeaconNameExpect() ))
                    place.setText( Html.fromHtml( "要求位置：<font color='#2196f3'>(蓝牙) </font>" + bean
                            .getAttendanceIbeaconNameExpect() ) );
                else
                    place.setText( Html.fromHtml( "要求位置：<font color='#2196f3'>(蓝牙) </font>" ) );
                break;
            case 2:
                if (!CommonUtils.isEmpty( bean.getAttendanceAddressExpect() ))
                    place.setText( "要求位置：" + bean.getAttendanceAddressExpect() );
                else
                    place.setText( "要求位置：" );
                break;
        }

        time.setText( "要求时间：" + times );
        String t;
//        if (position == 0) {
//            t = "上班";
//        } else if (position == datas.size() - 1) {
//            t = "下班";
//        } else {
//            t = "途经点";
//        }
        if (0 == datas.get( position ).getEndType()) {
            t = "上班";
        } else if (2 == datas.get( position ).getEndType()) {
            t = "下班";
        } else {
            t = "途经点";
        }
        title.setText( t );
    }


    private void initMap(TextureMapView map, int position) {
//        TextureSupportMapFragment fragmentById = (TextureSupportMapFragment) ((SignActivity) context)
// .getSupportFragmentManager().findFragmentById(R.id.map);
//        aMap = fragmentById.getMap();
        final AMap aMap = map.getMap();
        aMaps.put( position, aMap );
        aMap.clear();
        aMap.setLocationSource( this );// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled( false );// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled( true );// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType( AMap.LOCATION_TYPE_LOCATE );

        aMap.setOnMapLoadedListener( new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                LatLng latLng = aMap.getCameraPosition().target;
                Point screenPosition = aMap.getProjection().toScreenLocation( latLng );
                Marker locationMarker = aMap.addMarker( new MarkerOptions()
                        .anchor( 0.5f, 0.5f )
                        .icon( BitmapDescriptorFactory.fromResource( R.mipmap.location_icon ) ) );
                //设置Marker在屏幕上,不跟随地图移动
                locationMarker.setPositionByPixels( screenPosition.x, screenPosition.y );
            }
        } );

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle
        // .LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType
        // ，默认也会执行此种模式。
        myLocationStyle.showMyLocation( false );//不显示定位图片
        aMap.setMyLocationStyle( myLocationStyle );//设置定位蓝点的Style
        aMap.getUiSettings().setZoomControlsEnabled( false );//不显示缩放按钮
        aMap.getUiSettings().setAllGesturesEnabled( false );//不支持所有手势
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient( context );
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            locationClient.setLocationListener( this );
            //设置为高精度定位模式
            mLocationOption.setOnceLocation( false );
            mLocationOption.setInterval( 5000 );
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null && aMapLocation.getErrorCode() == ERROR_CODE.ERROR_NONE) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mListener.onLocationChanged( aMapLocation );
            //移动地图中心到当前的定位位置
            for (Integer key : aMaps.keySet()) {
                aMaps.get( key ).moveCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( aMapLocation.getLatitude
                        (), aMapLocation.getLongitude() ), 15 ) );
            }
            //获取定位信息
            mAddress = aMapLocation.getAddress();
            latitude = aMapLocation.getLatitude();
            longitude = aMapLocation.getLongitude();
            if (location_tv != null) {
                location_tv.setText( "当前：" + mAddress );
            }
        }
    }

//    private class CanSignVH extends RecyclerView.ViewHolder{
//        private TextView num_tv,time_tv,work_time_tv,address_tv;
//        private TextureMapView map;
//        private Button sign_btn;
//
//        public CanSignVH(View itemView) {
//            super(itemView);
//            num_tv = (TextView) itemView.findViewById(R.id.num_tv);
//            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
//            work_time_tv = (TextView) itemView.findViewById(R.id.work_time_tv);
//            address_tv = (TextView) itemView.findViewById(R.id.address_tv);
//            map = (TextureMapView) itemView.findViewById(R.id.map);
//            sign_btn = (Button) itemView.findViewById(R.id.sign_btn);
//        }
//    }
//
//    private class CannotSignVh extends RecyclerView.ViewHolder {
//        private TextView num_tv,time_tv,work_time_tv;
//
//        public CannotSignVh(View itemView) {
//            super(itemView);
//            work_time_tv = (TextView) itemView.findViewById(R.id.work_time_tv);
//            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
//            num_tv = (TextView) itemView.findViewById(R.id.num_tv);
//        }
//    }
//
//    private class AlreadySignVh extends RecyclerView.ViewHolder {
//        private TextView num_tv,time_tv,work_time_tv,address_tv,type_tv;
//
//        public AlreadySignVh(View itemView) {
//            super(itemView);
//            num_tv = (TextView) itemView.findViewById(R.id.num_tv);
//            time_tv = (TextView) itemView.findViewById(R.id.time_tv);
//            work_time_tv = (TextView) itemView.findViewById(R.id.work_time_tv);
//            address_tv = (TextView) itemView.findViewById(R.id.address_tv);
//            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
//        }
//    }

    private class VH0 extends RecyclerView.ViewHolder {
        TextView title_tv, time_tv, address_tv;
        TimeTextView sign_time_tv;
        LinearLayout ll;
        View line;
        View line0_sep_start;
        View line0_sep_end;


        public VH0(View itemView) {
            super( itemView );
            title_tv = (TextView) itemView.findViewById( R.id.title_tv );
            time_tv = (TextView) itemView.findViewById( R.id.time_tv );
            address_tv = (TextView) itemView.findViewById( R.id.address_tv );
            sign_time_tv = (TimeTextView) itemView.findViewById( R.id.sign_time_tv );

            sign_time_tv.setRefreshUIInterface( refreshUIInterface );
            line = itemView.findViewById( R.id.line_view );
            line0_sep_start = itemView.findViewById( R.id.line0_sep_start );
            line0_sep_end = itemView.findViewById( R.id.line0_sep_end );
            ll = (LinearLayout) itemView.findViewById( R.id.ll );
        }
    }

    private class VH1 extends RecyclerView.ViewHolder {
        RelativeLayout rl_root;
        TextView title_tv, time_tv, address_tv, location_tv;
        TextureMapView map;
        Button sign_btn;
        LinearLayout ll;
        FrameLayout fl_btn;
        View line;
        View line1_sep_start;
        View line1_sep_end;
        RecyclerView photo_lv_type1;

        public VH1(View itemView) {
            super( itemView );
            title_tv = (TextView) itemView.findViewById( R.id.title_tv );
            time_tv = (TextView) itemView.findViewById( R.id.time_tv );
            address_tv = (TextView) itemView.findViewById( R.id.address_tv );
            location_tv = (TextView) itemView.findViewById( R.id.location_tv );
            map = (TextureMapView) itemView.findViewById( R.id.map );
            photo_lv_type1 = (RecyclerView) itemView.findViewById( R.id.photo_lv_type1 );
            sign_btn = (Button) itemView.findViewById( R.id.sign_btn );
            fl_btn = (FrameLayout) itemView.findViewById( R.id.fl_btn );
            line = itemView.findViewById( R.id.line_view );
            ll = (LinearLayout) itemView.findViewById( R.id.ll );
            line1_sep_start = itemView.findViewById( R.id.line1_sep_start );
            line1_sep_end = itemView.findViewById( R.id.line1_sep_end );
            rl_root = (RelativeLayout) itemView.findViewById( R.id.rl_root );
        }
    }

    private class VH2 extends RecyclerView.ViewHolder {
        RelativeLayout rl_root;
        TextView title_tv, time_tv, address_tv, cur_tv;
        MyListView bluetooth_lv;
        RelativeLayout rl_refresh;
        ImageView refresh_iv;
        RecyclerView photo_lv_type2;
        FrameLayout fl_btn;
        Button sign_btn;
        LinearLayout ll;
        View line;
        View line2_sep_start;
        View line2_sep_end;

        public VH2(View itemView) {
            super( itemView );
            rl_root = (RelativeLayout) itemView.findViewById( R.id.rl_root );
            title_tv = (TextView) itemView.findViewById( R.id.title_tv );
            time_tv = (TextView) itemView.findViewById( R.id.time_tv );
            address_tv = (TextView) itemView.findViewById( R.id.address_tv );
            cur_tv = (TextView) itemView.findViewById( R.id.cur_tv );
            bluetooth_lv = (MyListView) itemView.findViewById( R.id.bluetooth_lv );
            rl_refresh = (RelativeLayout) itemView.findViewById( R.id.rl_refresh );
            refresh_iv = (ImageView) itemView.findViewById( R.id.refresh_iv );
            photo_lv_type2 = (RecyclerView) itemView.findViewById( R.id.photo_lv_type2 );
            fl_btn = (FrameLayout) itemView.findViewById( R.id.fl_btn );
            sign_btn = (Button) itemView.findViewById( R.id.sign_btn );
            line = itemView.findViewById( R.id.line_view );
            line2_sep_start = itemView.findViewById( R.id.line2_sep_start );
            line2_sep_end = itemView.findViewById( R.id.line2_sep_end );
            ll = (LinearLayout) itemView.findViewById( R.id.ll );
        }
    }

    private class VH3 extends RecyclerView.ViewHolder {
        TextView title_tv, time_tv, address_tv, sign_time_tv, sign_address_tv;
        RelativeLayout rl_root;
        LinearLayout ll;
        RecyclerView photo_lv_type3;
        View line;
        View line3_sep_start;
        View line3_sep_end;

        public VH3(View itemView) {
            super( itemView );
            title_tv = (TextView) itemView.findViewById( R.id.title_tv );
            time_tv = (TextView) itemView.findViewById( R.id.time_tv );
            address_tv = (TextView) itemView.findViewById( R.id.address_tv );
            sign_time_tv = (TextView) itemView.findViewById( R.id.sign_time_tv );

            rl_root = (RelativeLayout) itemView.findViewById( R.id.rl_root );
            sign_address_tv = (TextView) itemView.findViewById( R.id.sign_address_tv );
            photo_lv_type3 = (RecyclerView) itemView.findViewById( R.id.photo_lv_type3 );
            line = itemView.findViewById( R.id.line_view );
            line3_sep_start = itemView.findViewById( R.id.line3_sep_start );
            line3_sep_end = itemView.findViewById( R.id.line3_sep_end );
            line = itemView.findViewById( R.id.line_view );
            ll = (LinearLayout) itemView.findViewById( R.id.ll );
        }
    }

    private class VH4 extends RecyclerView.ViewHolder {
        TextView title_tv, time_tv, address_tv;
        LinearLayout ll;
        View line;
        View line4_sep_start;
        View line4_sep_end;

        public VH4(View itemView) {
            super( itemView );
            title_tv = (TextView) itemView.findViewById( R.id.title_tv );
            time_tv = (TextView) itemView.findViewById( R.id.time_tv );
            address_tv = (TextView) itemView.findViewById( R.id.address_tv );
            line = itemView.findViewById( R.id.line_view );
            line4_sep_end = itemView.findViewById( R.id.line4_sep_end );
            line4_sep_start = itemView.findViewById( R.id.line4_sep_start );
            ll = (LinearLayout) itemView.findViewById( R.id.ll );
        }
    }
}
