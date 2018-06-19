package com.hz.junxinbaoan;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hz.junxinbaoan.Service.MyService;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.dialog.RecordDialog;
import com.hz.junxinbaoan.activity.fragment.ListFragment;
import com.hz.junxinbaoan.activity.fragment.MessageFragment;
import com.hz.junxinbaoan.activity.fragment.WorkFragment;
import com.hz.junxinbaoan.activity.login.LoginActivity;
import com.hz.junxinbaoan.activity.mine.AdviceActivity;
import com.hz.junxinbaoan.activity.mine.ChangeMineInfo;
import com.hz.junxinbaoan.activity.mine.HelpWebActivity;
import com.hz.junxinbaoan.activity.mine.InfoActivity;
import com.hz.junxinbaoan.activity.setting.SettingActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.YuYinBean;
import com.hz.junxinbaoan.params.UserParams;
import com.hz.junxinbaoan.result.UserInfoResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ImageViewPlus;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 2;
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_lay)
    RelativeLayout titleLay;
    @BindView(R.id.icon_search)
    ImageView iconSearch;
    @BindView(R.id.icon_voice)
    ImageView iconVoice;
    @BindView(R.id.box_search)
    FrameLayout boxSearch;
    @BindView(R.id.tab1_work)
    TextView tab1Work;
    @BindView(R.id.tab1_line)
    View tab1Line;
    @BindView(R.id.tab1)
    LinearLayout tab1;
    @BindView(R.id.tab2_message)
    TextView tab2Message;
    @BindView(R.id.tab2_line)
    View tab2Line;
    @BindView(R.id.tab2)
    LinearLayout tab2;
    @BindView(R.id.tab3_list)
    TextView tab3List;
    @BindView(R.id.tab3_line)
    View tab3Line;
    @BindView(R.id.tab3)
    LinearLayout tab3;
    @BindView(R.id.container_fl)
    FrameLayout containerFl;
    @BindView(R.id.name_phone)
    TextView namePhone;
    @BindView(R.id.my_message)
    LinearLayout myMessage;
    @BindView(R.id.change_mine)
    LinearLayout changeMine;
    @BindView(R.id.outlogin)
    LinearLayout outlogin;
    @BindView(R.id.setting)
    LinearLayout setting;
    @BindView(R.id.talk)
    LinearLayout talk;
    @BindView(R.id.help)
    LinearLayout help;
    @BindView(R.id.drawer_layout)
    LinearLayout drawerLayout;
    @BindView(R.id.box_drawer)
    DrawerLayout box_drawer;
    @BindView(R.id.head_pic)
    ImageViewPlus head_pic;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.out)
    TextView out;
    @BindView(R.id.firstname)
    TextView firstname;

    RecordDialog recordDialog;
    EventManager asr;
    Intent startIntent;

    //fragment管理
    private FragmentManager mFragmentManager;
    //fragment列表
    private List<Fragment> mFragmentList = new ArrayList<>();
    //选择的fragment
    private int mSelected = 0;
    //是否因意外销毁而重建
    private Bundle mSavedInstance;
    private ArrayList<PermissionItem> permissionItems;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_main );
        Log.e( TAG, "--findViews " + savedInstanceState );
        //权限列表
        permissionItems = new ArrayList<PermissionItem>();

        asr = EventManagerFactory.create( mBaseActivity, "asr" ); // this是Activity或其它Context类
        asr.registerListener( yourListener );
        recordDialog = new RecordDialog( mBaseActivity, new RecordDialog.Showing() {
            @Override
            public void isShow() {
                String json = "{\"accept-audio-data\":false,\"disable-punctuation\":false," +
                        "\"accept-audio-volume\":true,\"pid\":1536}";
                asr.send( SpeechConstant.ASR_START, json, null, 0, 0 );//开始
                Log.i( "BaiDuYuYin", "进入语音识别" );
            }

            @Override
            public void isDis() {
//                iconVoice.setEnabled(false);//不让再按
//                        new MyCountDownTimer(1500, 500).start();
                asr.send( SpeechConstant.ASR_STOP, null, null, 0, 0 ); // 发送停止录音事件，提前结束录音等待识别结果
                Log.i( "BaiDuYuYin", "语音识别完成" );
            }
        } );


        Log.i( "sha1", CommonUtils.sHA1( mBaseActivity ) );
//        getTime();
        startIntent = new Intent( getApplicationContext(), MyService.class );
        startService( startIntent );

        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        Log.e( TAG, (savedInstanceState != null) + "  ," + (mFragmentManager.findFragmentByTag( "work" ) != null) +
                ", " + (mFragmentManager.findFragmentByTag( "message" ) != null) + " ; " + (mFragmentManager
                .findFragmentByTag( "list" ) != null) );
        if (savedInstanceState != null) {
            mSavedInstance = savedInstanceState;
//            if (mFragmentManager.findFragmentByTag( "work" ) != null)
//                mFragmentList.add( mFragmentManager.findFragmentByTag( "home" ) );
                mFragmentList.add( mFragmentManager.findFragmentByTag( "work" ) );
//            else
//                mFragmentList.add( new WorkFragment() );
//
//            if (mFragmentManager.findFragmentByTag( "message" ) != null)
//                mFragmentList.add( mFragmentManager.findFragmentByTag( "order" ) );
                mFragmentList.add( mFragmentManager.findFragmentByTag( "message" ) );
//            else
//                mFragmentList.add( new MessageFragment() );
//
//            if (mFragmentManager.findFragmentByTag( "list" ) != null)
//                mFragmentList.add( mFragmentManager.findFragmentByTag( "mine" ) );
                mFragmentList.add( mFragmentManager.findFragmentByTag( "list" ) );
//            else
//                mFragmentList.add( new ListFragment() );
//
//            mSelected = savedInstanceState.getInt( "selected" );
//            Log.e( TAG, "mSelected: " + mSelected );
        } else {
            mFragmentList.add( new WorkFragment() );
            mFragmentList.add( new MessageFragment() );
            mFragmentList.add( new ListFragment() );
//            mFragmentManager.beginTransaction()
//                    .add(R.id.container_fl, mFragmentList.get(1), "order")
//                    .add(R.id.container_fl, mFragmentList.get(2), "mine")
//                    .commit();
        }
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        Log.e( TAG, "--addListeners" );
        help.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(mBaseActivity, HelpActivity.class));
                startActivityForResult( new Intent( mBaseActivity, HelpWebActivity.class ), 100 );
            }
        } );
        talk.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult( new Intent( mBaseActivity, AdviceActivity.class ), 100 );
            }
        } );
        myMessage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult( new Intent( mBaseActivity, InfoActivity.class ), 100 );
            }
        } );
        tab1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e( TAG, mSelected + "; " + mFragmentList.get( 0 ).isAdded() + " 0 ," + mFragmentList.get( 1 )
                        .isAdded()
                        + " 1 , " + " , 2 " + mFragmentList.get( 2 ).isAdded() );
                if (mFragmentList.get( 0 ).isAdded())
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                            .get( 0 ) ).commit();
                else
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                            .container_fl, mFragmentList.get( 0 ), "work" ).commit();
                mSelected = 0;
                changeTab( 1 );
            }
        } );
        tab2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e( TAG, mSelected + "; " + mFragmentList.get( 1 ).isAdded() + " 1 ," + mFragmentList.get( 0 )
                        .isAdded() + " 0 , " + " , 2 " + mFragmentList.get( 2 ).isAdded() );
                if (mFragmentList.get( 1 ).isAdded())
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                            .get( 1 ) ).commit();
                else
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                            .container_fl, mFragmentList.get( 1 ), "message" ).commit();

                mSelected = 1;
                changeTab( 2 );
            }
        } );
        tab3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e( TAG, mSelected + "; " + mFragmentList.get( 2 ).isAdded() + " 2 ," + mFragmentList.get( 1 )
                        .isAdded() + " 1 , " + " , 0 " + mFragmentList.get( 0 ).isAdded() );
                if (mFragmentList.get( 2 ).isAdded())
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                            .get( 2 ) ).commit();
                else
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                            .container_fl, mFragmentList.get( 2 ), "list" ).commit();
                mSelected = 2;
                changeTab( 3 );
            }
        } );
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将左侧栏显示出来
                boolean drawerOpen = box_drawer.isDrawerOpen( drawerLayout );
                if (!drawerOpen) {
                    box_drawer.openDrawer( drawerLayout );
                }
            }
        } );
        //跳转设置页面
        setting.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult( new Intent( mBaseActivity, SettingActivity.class ), 100 );
            }
        } );
        changeMine.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult( new Intent( mBaseActivity, ChangeMineInfo.class ), 100 );
            }
        } );

        search.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    ((ListFragment) mFragmentList.get( 2 )).serch( s );
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        } );


        search.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //先隐藏键盘
                    ((InputMethodManager) getSystemService( INPUT_METHOD_SERVICE ))
                            .hideSoftInputFromWindow( MainActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
                    String str = search.getText().toString().trim();
                    try {
                        ((ListFragment) mFragmentList.get( 2 )).serch( str );
                    } catch (Exception e) {
                    }
                    return true;
                } else
                    return false;
            }
        } );
        iconVoice.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                asr.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0); // 取消本次识别，取消后将立即停止不会返回识别结果
                permissionItems.clear();
                permissionItems.add( new PermissionItem( Manifest.permission.RECORD_AUDIO, "麦克风", R.drawable
                        .permission_ic_micro_phone ) );
//                asr.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0); // 取消本次识别，取消后将立即停止不会返回识别结果
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
                                recordDialog.show();
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

        out.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mUserInfo.delUserInfo();
                startActivity( new Intent( mBaseActivity, LoginActivity.class ) );
                manService.getMANAnalytics().updateUserAccount( "", "" );// 用户注销埋点
                finish();
            }
        } );
    }


    @Override
    protected void initViews() {
        super.initViews();

        Log.e( TAG, "--initViews  " + mSavedInstance );
        if (mSavedInstance == null) {
            Log.e( TAG, "--initViews  " + "111" );
            mFragmentManager.beginTransaction()
                    .add( R.id.container_fl, mFragmentList.get( 0 ), "work" )
                    .add( R.id.container_fl, mFragmentList.get( 1 ), "message" ).hide( mFragmentList.get( 1 ) )
                    .add( R.id.container_fl, mFragmentList.get( 2 ), "list" ).hide( mFragmentList.get( 2 ) )
                    .commit();
//            mFragmentManager.beginTransaction().add(R.id.container_fl, mFragmentList.get(0), "home").commit();
        } else {
            Log.e( TAG, "--initViews  " + "222 ,, " + mFragmentList.size() );
            for (int i = 0; i < mFragmentList.size(); i++) {
                Log.e( TAG, mFragmentList.get( i ) + "," + mFragmentList.get( i ).isAdded() + "; " + mSelected );
                if (mFragmentList.get( i ) != null && mFragmentList.get( i ).isAdded() && i != mSelected)
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( i ) ).commit();
            }
        }
        tab1.setSelected( true );
        tab2.setSelected( false );
        tab3.setSelected( false );
        changeTab( 1 );
    }

    private void changeTab(int i) {
        //改变上面的按钮显示效果
        if (i == 1) {
            tab1Line.setVisibility( View.VISIBLE );
            tab2Line.setVisibility( View.INVISIBLE );
            tab3Line.setVisibility( View.INVISIBLE );
            tab1Work.setTextColor( getResources().getColor( R.color.white ) );
            tab2Message.setTextColor( getResources().getColor( R.color.text_gray ) );
            tab3List.setTextColor( getResources().getColor( R.color.text_gray ) );
            boxSearch.setVisibility( View.GONE );
            titleLay.setVisibility( View.VISIBLE );
        }
        if (i == 2) {
            tab2Line.setVisibility( View.VISIBLE );
            tab1Line.setVisibility( View.INVISIBLE );
            tab3Line.setVisibility( View.INVISIBLE );
            tab2Message.setTextColor( getResources().getColor( R.color.white ) );
            tab1Work.setTextColor( getResources().getColor( R.color.text_gray ) );
            tab3List.setTextColor( getResources().getColor( R.color.text_gray ) );
            boxSearch.setVisibility( View.GONE );
            titleLay.setVisibility( View.VISIBLE );
        }
        if (i == 3) {
            tab3Line.setVisibility( View.VISIBLE );
            tab2Line.setVisibility( View.INVISIBLE );
            tab1Line.setVisibility( View.INVISIBLE );
            tab3List.setTextColor( getResources().getColor( R.color.white ) );
            tab2Message.setTextColor( getResources().getColor( R.color.text_gray ) );
            tab1Work.setTextColor( getResources().getColor( R.color.text_gray ) );
            boxSearch.setVisibility( View.VISIBLE );
            titleLay.setVisibility( View.GONE );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 23) {
            Log.i( TAG, "onActivityResult write settings " );
            checkPermissions();
        }

        getUserInfo();
//        initPermission();
        Log.e( TAG, "--onResume" + getIntent().getStringExtra( "type" ) );
        if (getIntent().getStringExtra( "type" ) != null && getIntent().getStringExtra( "type" ).equals( "message" )) {
            tab2.performClick();
        }
    }

    //6.0以上才能调用
    @TargetApi(23)
    void checkPermissions() {
        if (!Settings.System.canWrite( this )) {
            Intent intent = new Intent( Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse( "package:" + getPackageName() ) );
            startActivityForResult( intent, REQUEST_CODE );
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e( TAG, "--onPause" );
    }

    //获取个人信息接口
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.GETUSERINFO)
        Call<UserInfoResult> getData(@FieldMap Map<String, Object> map);
    }

    //获取个人信息
    private void getUserInfo() {
        showDialog( true );
        GetData getData = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetData.class );
        UserParams params = new UserParams();
        params.setPushId( CommonUtils.getPushId() );
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        Call<UserInfoResult> call = getData.getData( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<UserInfoResult>() {
            @Override
            public void onResponse(final Call<UserInfoResult> call, final Response<UserInfoResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                        .OnHandleListener<UserInfoResult>() {
                    @Override
                    public void onSuccess(UserInfoResult result) {
                        Log.e( TAG, "UserInfoResult : " + result.toString() );
                        if (result.getData() != null) {
                            MyApplication.mUserInfo.saveUserInfo( result );
                            changeUI();
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
            public void onFailure(Call<UserInfoResult> call, Throwable t) {
                if (loadingDialog.isShowing()) {
                    showDialog( false );
                }
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }

    //修改ui界面
    private void changeUI() {
        namePhone.setText( MyApplication.mUserInfo.getUserName() + " " + MyApplication.mUserInfo.getUserPhone() );
        if (!TextUtils.isEmpty( MyApplication.mUserInfo.getUserPhoto() )) {
            firstname.setText( "" );
            try {
                Glide.with( mBaseActivity ).load( MyApplication.mUserInfo.getUserPhoto() ).asBitmap()
                        .centerCrop().error( R.mipmap.mine_avbg )
                        .into( head_pic );
            } catch (Exception ignore) {
            }
//            CommonUtils.loadPic(mBaseActivity, MyApplication.mUserInfo.getUserPhoto(), new CommonUtils.LoadPic() {
//                @Override
//                public void loadPic(byte[] bytes) {
//                    try {
//                        Glide.with(mBaseActivity).load(bytes).asBitmap()
//                                .centerCrop().error(R.mipmap.mine_avbg)
//                                .into(head_pic);
//                    } catch (Exception ignore) {
//                    }
//                }
//            });
        } else {
            firstname.setText( MyApplication.mUserInfo.getUserName().charAt( MyApplication.mUserInfo.getUserName()
                    .length() - 1 ) + "" );
            head_pic.setImageResource( R.color.picback );
        }
    }

    private long mExitTime;


    @Override
    public void onBackPressed() {
        boolean drawerOpen = box_drawer.isDrawerOpen( drawerLayout );
        if (drawerOpen) {
            box_drawer.closeDrawer( drawerLayout );
        } else {
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            if (timeInMillis - mExitTime > 2000) {
                MyToast.showToast( mBaseActivity, "再按一次退出程序" );
                mExitTime = timeInMillis;
            } else {
                MyApplication.getInstance().finish();
            }
            return;
        }

//        long timeInMillis = Calendar.getInstance().getTimeInMillis();
//        if (timeInMillis - mExitTime > 2000) {
//            MyToast.showToast( mBaseActivity, "再按一次退出程序" );
//            mExitTime = timeInMillis;
//        } else {
//            MyApplication.getInstance().finish();
//        }
//        return;
    }

    String yesName;
    EventListener yourListener = new EventListener() {
        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            if (name.equals( SpeechConstant.CALLBACK_EVENT_ASR_READY )) {
                // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
                MyToast.showToast( mBaseActivity, "可以说话了" );
                Log.i( "BaiDuYuYin", "可以说话了" );
            }
            if (name.equals( SpeechConstant.CALLBACK_EVENT_ASR_FINISH )) {
                // 识别结束
                recordDialog.dismiss();
                MyToast.showToast( mBaseActivity, "识别结束" );
                Log.i( "BaiDuYuYin", "识别结束" );
                search.setText( yesName );
                try {
                    ((ListFragment) mFragmentList.get( 2 )).serch( yesName );
                } catch (Exception e) {
                }
            }
            Log.i( "BaiDuYuYin", "识别结果：" + "name:" + name + "+params" + params + "" + "offset" + offset + "length" +
                    length );
            if (params != null) {
                if (params.contains( "best_result" )) {
                    YuYinBean bean = new Gson().fromJson( params, YuYinBean.class );
                    yesName = bean.best_result;
                }
                if (params.contains( "9001" )) {
                    MyToast.showToast( mBaseActivity, "请打开录音权限" );
                }
                if (params.contains( "7001" )) {
                    MyToast.showToast( mBaseActivity, "没有匹配的识别结果" );
                }
            }

            // ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
        }
    };


    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ArrayList<String> toApplyList = new ArrayList<String>();
        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission( this, perm )) {
                toApplyList.add( perm );
                //进入到这里代表没有权限.
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions( mBaseActivity, toApplyList.toArray( tmpList ), 123 );
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
//        super.onRequestPermissionsResult( requestCode, permissions, paramArrayOfInt );
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (Settings.System.canWrite( mBaseActivity )) {
                Log.i( TAG, "onActivityResult write settings granted" );
            } else {
                finish();
            }
        }
        if (requestCode == 100) {
            box_drawer.closeDrawer( drawerLayout, false );
        }
    }


}
