package com.hz.junxinbaoan.activity.sign;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.calendar.CalendarBean;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.fragment.NormalSignFragment;
import com.hz.junxinbaoan.activity.fragment.OvertimeSignFragment;
import com.hz.junxinbaoan.activity.fragment.TemSignFragment;
import com.hz.junxinbaoan.activity.mine.SchedulingActivity;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.bluetooth.IBeacon;
import com.hz.junxinbaoan.utils.bluetooth.ScanResultAnalysis;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 考勤 签到
 * Created by Administrator on 2017/10/24 0024.
 */

public class SignActivity extends BaseActivity implements NormalSignFragment.CallBackValue, OvertimeSignFragment
        .CallBackValue, TemSignFragment.CallBackValue {
    private static final String TAG = "SignActivity";

    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.title_name)
    TextView titleName;
    @InjectView(R.id.date_fl)
    FrameLayout dateFl;
    @InjectView(R.id.title_lay)
    RelativeLayout titleLay;
    @InjectView(R.id.iv_tab1)
    ImageView ivTab1;
    @InjectView(R.id.tv_tab1)
    TextView tvTab1;
    @InjectView(R.id.oval_tab1)
    ImageView ovalTab1;
    @InjectView(R.id.tab1_line)
    View tab1Line;
    @InjectView(R.id.tab1)
    RelativeLayout tab1;
    @InjectView(R.id.iv_tab2)
    ImageView ivTab2;
    @InjectView(R.id.tv_tab2)
    TextView tvTab2;
    @InjectView(R.id.oval_tab2)
    ImageView ovalTab2;
    @InjectView(R.id.tab2_line)
    View tab2Line;
    @InjectView(R.id.tab2)
    RelativeLayout tab2;
    @InjectView(R.id.iv_tab3)
    ImageView ivTab3;
    @InjectView(R.id.tv_tab3)
    TextView tvTab3;
    @InjectView(R.id.oval_tab3)
    ImageView ovalTab3;
    @InjectView(R.id.tab3_line)
    View tab3Line;
    @InjectView(R.id.tab3)
    RelativeLayout tab3;
    @InjectView(R.id.container_fl)
    FrameLayout containerFl;


    //fragment管理
    private FragmentManager mFragmentManager;
    //fragment列表
    private ArrayList<Fragment> mFragmentList;
    //是否因意外销毁而重建
    private Bundle mSavedInstance;
    //选择的fragment
    private int mSelected = 0;

    private CalendarBean curDate;
    private CalendarBean nowDate;


    private int normalSize = 0;
    private int overtimeSize = 0;
    private int temSize = 0;

    private int tabSelect = 1;//选择的tab fragment


    //----------------蓝牙相关--------------------------------
    private IBeacon iBeacon;
    private BluetoothAdapter mBluetoothAdapter;
    private long mStartTime;
    private IntentFilter mIntentFilter;
    private boolean mBluetoothState = true;//record bluetooth state when launch app

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i( TAG, "Receive broadcast" + intent.getAction() + ":" + mBluetoothAdapter.isEnabled() );
            if (intent.getAction().equals( BluetoothAdapter.ACTION_STATE_CHANGED )) {
                if (mBluetoothAdapter.isEnabled()) {
                    Log.i( TAG, "time:" + String.valueOf( System.currentTimeMillis() - mStartTime ) );
                    //TODO:Thread
                    new Thread( new Runnable() {
                        @Override
                        public void run() {
                            scanDevice();
                        }
                    } ).start();
                    mStartTime = 0;
                }
            }
        }
    };


    @SuppressLint("NewApi")
    private void scanDevice() {
        boolean startLeScan = mBluetoothAdapter.startLeScan( mLeScanCallback );
        Log.i( TAG, "startSacn " + startLeScan );
    }

    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    final IBeacon ibeacon = ScanResultAnalysis.formatScanData( device, rssi, scanRecord );
                    if (ibeacon == null)
                        return;
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            //在子布局 里面添加蓝牙坐标点
//                            signAdapter.add( ibeacon );
//                            Log.e( TAG, normalSize+ "--" + overtimeSize+" OvertimeFragment 传递回来的值 : " + temSize );
//                            Log.e( TAG, tabSelect + "run: mLeScanCallback " + ibeacon );
                            if (normalSize > 0 && tabSelect == 1)
                                ((NormalSignFragment) mFragmentList.get( 0 )).add( ibeacon );
//                            else
//                                stopScan();
                            if (overtimeSize > 0 && tabSelect == 2)
                                ((OvertimeSignFragment) mFragmentList.get( 1 )).add( ibeacon );
//                            else
//                                stopScan();
                            if (temSize > 0 && tabSelect == 3)
                                ((TemSignFragment) mFragmentList.get( 2 )).add( ibeacon );
//                            else
//                                stopScan();
                        }
                    } );
                }
            };

    @SuppressLint("NewApi")
    private void stopScan() {
        mBluetoothAdapter.stopLeScan( mLeScanCallback );
    }

    private boolean checkVersion() {
        if (Build.VERSION.SDK_INT < 18) {
            Toast.makeText( this, "Android版本过低", Toast.LENGTH_LONG ).show();
            return false;
        }
        return true;
    }

    @SuppressLint("NewApi")
    private boolean checkBluetooth() {
        if (!getPackageManager().hasSystemFeature( PackageManager.FEATURE_BLUETOOTH_LE )) {
            Toast.makeText( this, "蓝牙设备不支持", Toast.LENGTH_LONG ).show();
            return false;
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService( Context.BLUETOOTH_SERVICE );
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText( this, "蓝牙设备不支持", Toast.LENGTH_LONG ).show();
            return false;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Log.i( TAG, "BT not enabled yet" );
            mStartTime = System.currentTimeMillis();
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            mBluetoothState = false;
            mBluetoothAdapter.enable();
            return false;
        }
        return true;
    }
    //----------------蓝牙相关--------------------------------

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_sign );

        mIntentFilter = new IntentFilter( BluetoothAdapter.ACTION_STATE_CHANGED );
        registerReceiver( mBroadcastReceiver, mIntentFilter );

        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        if (savedInstanceState != null) {
            mSavedInstance = savedInstanceState;
//            if (mFragmentManager.findFragmentByTag( "signnormal" ) != null)
                mFragmentList.add( mFragmentManager.findFragmentByTag( "signnormals" ) );
//            else
//                mFragmentList.add( new NormalSignFragment() );
//
//            if (mFragmentManager.findFragmentByTag( "signovertime" ) != null)
                mFragmentList.add( mFragmentManager.findFragmentByTag( "signovertimes" ) );
//            else
//                mFragmentList.add( new OvertimeSignFragment() );

//            if (mFragmentManager.findFragmentByTag( "signtemporary" ) != null)
                mFragmentList.add( mFragmentManager.findFragmentByTag( "signtemporarys" ) );
//            else
//                mFragmentList.add( new TemSignFragment() );
//
//            mSelected = savedInstanceState.getInt( "signselected" );
        } else {
            mFragmentList.add( new NormalSignFragment() );   //日常
            mFragmentList.add( new OvertimeSignFragment() );   //加班
            mFragmentList.add( new TemSignFragment() );   //临时
        }
    }

    @Override
    protected void initViews() {
        super.initViews();

        final Calendar calendar = Calendar.getInstance();
        curDate = new CalendarBean( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1, calendar.get(
                Calendar.DATE ) );
        nowDate = curDate;

        Log.i( TAG, "initViews:  " + mSavedInstance );
        if (mSavedInstance == null) {
            mFragmentManager.beginTransaction()
                    .add( R.id.container_fl, mFragmentList.get( 0 ), "signnormal" )
//                    .add( R.id.container_fl, mFragmentList.get( 1 ), "signovertime" ).hide( mFragmentList.get( 1 ) )
//                    .add( R.id.container_fl, mFragmentList.get( 2 ), "signtemporary" ).hide( mFragmentList.get( 2 ) )
                    .commit();

        } else {
            for (int i = 0; i < mFragmentList.size(); i++) {
                if (mFragmentList.get( i ) != null && mFragmentList.get( i ).isAdded() && i != mSelected)
                    mFragmentManager.beginTransaction().remove( mFragmentList.get( i ) ).commit();
            }
        }
        tab1.setSelected( true );
        tab2.setSelected( false );
        tab3.setSelected( false );
        tabSelect = 1;
        changeTab( tabSelect );

        if (nowDate != null)
            titleName.setText( nowDate.moth + "月" + nowDate.day + "日考勤签到" );
    }

    private void changeTab(int i) {
        //改变上面的按钮显示效果
        tab1Line.setVisibility( View.INVISIBLE );
        tab2Line.setVisibility( View.INVISIBLE );
        tab3Line.setVisibility( View.INVISIBLE );
        if (i == 1) {
            tab1Line.setVisibility( View.VISIBLE );
        }
        if (i == 2) {
            tab2Line.setVisibility( View.VISIBLE );
        }
        if (i == 3) {
            tab3Line.setVisibility( View.VISIBLE );
        }
    }

    @Override
    protected void requestOnCreate() {
        super.requestOnCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
    }

    @Override
    protected void onResume() {
        if (!CommonUtils.isOPenGPS( this )) {//判断gps是否开启，如果没开启，让用户开启
            CommonUtils.openGPS( this );
        }
        if (checkVersion() && checkBluetooth())
            scanDevice();

//        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(broadcastReceiver, intentFilter);
//        if (!mBluetoothAdapter.isEnabled()){ //未打开蓝牙，才需要打开蓝牙
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, 300);
////会以Dialog样式显示一个Activity ， 我们可以在onActivityResult()方法去处理返回值
//        }
        mBluetoothAdapter.startDiscovery();

        super.onResume();
    }

    @Override
    protected void onPause() {
        stopScan();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mBluetoothAdapter != null && mBluetoothState == false)
            mBluetoothAdapter.disable();
        unregisterReceiver( mBroadcastReceiver );
        super.onDestroy();
    }

    @Override
    protected void addListeners() {
        super.addListeners();

        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        } );

        dateFl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( mBaseActivity, SchedulingActivity.class ) );
            }
        } );


        tab1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 != mSelected)
                    if (mFragmentList.get( 0 ).isAdded())
                        mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                                .get( 0 ) ).commit();
                    else
                        mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                                .container_fl, mFragmentList.get( 0 ), "signnormal" ).commit();
                else
                    mFragmentManager.beginTransaction().show( mFragmentList.get( 0 ) ).commit();
                mSelected = 0;
                tabSelect = 1;
                changeTab( tabSelect );
//                mFragmentManager.beginTransaction().remove( mFragmentList.get( 1 ) ).commit();
//                mFragmentManager.beginTransaction().remove( mFragmentList.get( 2 ) ).commit();
            }
        } );
        tab2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 != mSelected)
                    if (mFragmentList.get( 1 ).isAdded())
                        mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                                .get( 1 ) ).commit();
                    else
                        mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                                .container_fl, mFragmentList.get( 1 ), "signovertime" ).commit();
                else
                    mFragmentManager.beginTransaction().show( mFragmentList.get( 1 ) ).commit();
                mSelected = 1;
                tabSelect = 2;
                changeTab( tabSelect );
//                mFragmentManager.beginTransaction().remove( mFragmentList.get( 0 ) ).commit();
//                mFragmentManager.beginTransaction().remove( mFragmentList.get( 2 ) ).commit();
            }
        } );
        tab3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (2 != mSelected)
                    if (mFragmentList.get( 2 ).isAdded())
                        mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                                .get( 2 ) ).commit();
                    else
                        mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                                .container_fl, mFragmentList.get( 2 ), "signtemporary" ).commit();
                else
                    mFragmentManager.beginTransaction().show( mFragmentList.get( 2 ) ).commit();
                mSelected = 2;
                tabSelect = 3;
                changeTab( tabSelect );
//                mFragmentManager.beginTransaction().remove( mFragmentList.get( 0 ) ).commit();
//                mFragmentManager.beginTransaction().remove( mFragmentList.get( 1 ) ).commit();
            }
        } );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        // TODO: add setContentView(...) invocation
        ButterKnife.inject( this );
    }

    /**
     * 日常 fragment 返回的 考勤情况
     *
     * @param size
     */
    @Override
    public void SendSignNormalValue(int size) {
        normalSize = size;
        isOvalShow( ovalTab1, size );
    }


    /**
     * 加班 fragment 返回的 考勤情况
     *
     * @param size
     */
    @Override
    public void SendSignOvertimeValue(int size) {
        overtimeSize = size;
        isOvalShow( ovalTab2, size );
    }

    /**
     * 临时 fragment 返回的 考勤情况
     *
     * @param size
     */
    @Override
    public void SendSignTemValue(int size) {
        temSize = size;
        isOvalShow( ovalTab3, size );
    }


    /**
     * 日常 加班 临时 小圆点的显示与隐藏
     *
     * @param ovalTab
     * @param size
     */
    private void isOvalShow(ImageView ovalTab, int size) {
        if (size > 0)
            ovalTab.setVisibility( View.VISIBLE );
        else
            ovalTab.setVisibility( View.INVISIBLE );

    }


}
