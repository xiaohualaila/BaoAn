package com.hz.junxinbaoan;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.hz.junxinbaoan.data.YuYinBean;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ImageViewPlus;
import com.hz.junxinbaoan.utils.MyToast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


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
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    RecordDialog recordDialog;
    EventManager asr;
    Intent startIntent;

    //fragment列表
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();
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
                asr.send( SpeechConstant.ASR_STOP, null, null, 0, 0 ); // 发送停止录音事件，提前结束录音等待识别结果
                Log.i( "BaiDuYuYin", "语音识别完成" );
            }
        } );


        Log.i( "sha1", CommonUtils.sHA1( mBaseActivity ) );
        startIntent = new Intent( getApplicationContext(), MyService.class );
        startService( startIntent );
        mFragmentList = new ArrayList<>();

    }

    @Override
    protected void addListeners() {
        super.addListeners();
        Log.e( TAG, "--addListeners" );
        help.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                MyToast.showToast( mBaseActivity, "拒绝使用权限可能导致应用无法正常使用！" );
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
        tabs.add("工作");
        tabs.add("消息");
        tabs.add("通讯录");
        mFragmentList.add( new WorkFragment() );
        mFragmentList.add( new MessageFragment() );
        mFragmentList.add( new ListFragment() );

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        view_pager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        //关联ViewPager和TabLayout
        tabLayout.setupWithViewPager(view_pager);
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==2){
                    boxSearch.setVisibility(View.VISIBLE);
                    titleLay.setVisibility(View.GONE);
                }else {
                    boxSearch.setVisibility(View.GONE);
                    titleLay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
    }

    class TabAdapter extends FragmentPagerAdapter {
        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        //显示标签上的文字
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 23) {
            Log.i( TAG, "onActivityResult write settings " );
            checkPermissions();
        }
//        getUserInfo();
        changeUI();
        Log.e( TAG, "--onResume" + getIntent().getStringExtra( "type" ) );
        if (getIntent().getStringExtra( "type" ) != null && getIntent().getStringExtra( "type" ).equals( "message" )) {
            view_pager.setCurrentItem(1);
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
