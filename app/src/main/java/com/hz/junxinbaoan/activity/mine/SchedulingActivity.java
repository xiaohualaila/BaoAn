package com.hz.junxinbaoan.activity.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarView;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.activity.fragment.NormalFragment;
import com.hz.junxinbaoan.activity.fragment.OvertimeFragment;
import com.hz.junxinbaoan.activity.fragment.TemporaryFragment;
import com.hz.junxinbaoan.activity.map.PeoPleMap;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.SignDetailBean;
import com.hz.junxinbaoan.params.SingleViewParam;
import com.hz.junxinbaoan.result.SingleViewResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.DensityUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import net.qiujuer.genius.ui.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * 我的排班
 * Created by Administrator on 2017/10/24 0024.
 */

public class SchedulingActivity extends BaseActivity implements NormalFragment.CallBackValue, OvertimeFragment
        .CallBackValue,TemporaryFragment.CallBackValue {
    private static final String TAG = "SchedulingActivity";

    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.calendarDateView)
    CalendarDateView calendarDateView;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @BindView(R.id.left_iv)
    ImageView left_iv;
    @BindView(R.id.right_iv)
    ImageView right_iv;
    @BindView(R.id.date_tv)
    TextView date_tv;


    @BindView(R.id.road)
    Button road;

    @BindView(R.id.tab1)
    RelativeLayout tab1;
    @BindView(R.id.tab1_line)
    View tab1Line;
    @BindView(R.id.oval_tab1)
    ImageView ovalTab1;
    @BindView(R.id.tab2)
    RelativeLayout tab2;
    @BindView(R.id.tab2_line)
    View tab2Line;
    @BindView(R.id.oval_tab2)
    ImageView ovalTab2;
    @BindView(R.id.tab3)
    RelativeLayout tab3;
    @BindView(R.id.tab3_line)
    View tab3Line;
    @BindView(R.id.oval_tab3)
    ImageView ovalTab3;

    @BindView(R.id.container_fl)
    FrameLayout containerFl;

    private CalendarBean curDate;
    private CalendarBean nowDate;
    //    private SignAdapter signAdapter;
    private List<SignDetailBean> signDetailList;

    private boolean isByClick = true;
    private int monthDef = 0;
    //fragment管理
    private FragmentManager mFragmentManager;
    //fragment列表
    private ArrayList<Fragment> mFragmentList;
    //是否因意外销毁而重建
    private Bundle mSavedInstance;
    //选择的fragment
    private int mSelected = 0;

    @Override
    protected void getIntentData() {
        super.getIntentData();
    }

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_scheduling );
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        if (savedInstanceState != null) {
            mSavedInstance = savedInstanceState;
            if (mFragmentManager.findFragmentByTag( "normal" ) != null)
                mFragmentList.add( mFragmentManager.findFragmentByTag( "normals" ) );
            else
                mFragmentList.add( new NormalFragment() );

            if (mFragmentManager.findFragmentByTag( "overtime" ) != null)
                mFragmentList.add( mFragmentManager.findFragmentByTag( "overtimes" ) );
            else
                mFragmentList.add( new OvertimeFragment() );

            if (mFragmentManager.findFragmentByTag( "temporary" ) != null)
                mFragmentList.add( mFragmentManager.findFragmentByTag( "temporarys" ) );
            else
                mFragmentList.add( new TemporaryFragment() );

            mSelected = savedInstanceState.getInt( "selected" );
        } else {
            mFragmentList.add( new NormalFragment() );   //日常
            mFragmentList.add( new OvertimeFragment() );   //加班
            mFragmentList.add( new TemporaryFragment() );   //临时
        }
    }

    @Override
    protected void initViews() {
        super.initViews();

        if (mSavedInstance == null) {
            mFragmentManager.beginTransaction()
                    .add( R.id.container_fl, mFragmentList.get( 0 ), "normal" )
                    .add( R.id.container_fl, mFragmentList.get( 1 ), "overtime" ).hide( mFragmentList.get( 1 ) )
                    .add( R.id.container_fl, mFragmentList.get( 2 ), "temporary" ).hide( mFragmentList.get( 2 ) )
                    .commit();

        } else {
            for (int i = 0; i < mFragmentList.size(); i++) {
                if (mFragmentList.get( i ) != null && mFragmentList.get( i ).isAdded() && i != mSelected)
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( i ) ).commit();
            }
        }
        tab1.setSelected( true );
        tab2.setSelected( false );
        tab3.setSelected( false );
        changeTab( 1 );

        final Calendar calendar = Calendar.getInstance();
        curDate = new CalendarBean( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1, calendar.get(
                Calendar.DATE ) );
        nowDate = curDate;
        Log.e( TAG, curDate.toString() );
        time_tv.setText( CommonUtils.numToString( calendar.get( Calendar.MONTH ) + 1 ) + "月 " + calendar.get(
                Calendar.YEAR ) );

        calendarDateView.setAdapter( new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if (convertView == null) {
                    convertView = LayoutInflater.from( parentView.getContext() ).inflate( R.layout.item_calendar,
                            null );
                }

                TextView chinaText = (TextView) convertView.findViewById( R.id.chinaText );
                TextView text = (TextView) convertView.findViewById( R.id.text );
                RelativeLayout background = (RelativeLayout) convertView.findViewById( R.id.background );
                ImageView bg = (ImageView) convertView.findViewById( R.id.bg );

                text.setText( "" + bean.day );

                //mothFlag 0是当月，-1是月前，1是月后
                if (bean.mothFlag != 0) {
                    chinaText.setVisibility( View.GONE );
                    text.setTextColor( Color.parseColor( "#BAC2C7" ) );
                    background.setBackgroundResource( R.drawable.scheduling_other_bg );
                    bg.setVisibility( View.GONE );
                } else {
//                    chinaText.setVisibility(View.VISIBLE);
                    text.setTextColor( Color.BLACK );
                    background.setBackgroundColor( Color.WHITE );
                    bg.setVisibility( View.VISIBLE );

//                    chinaText.setText("白");
//                    chinaText.setTextColor(Color.parseColor("#3EA4F4"));
                }

                //判断是否是今天
                Calendar c = new GregorianCalendar();
                int y = c.get( Calendar.YEAR );
                int m = c.get( Calendar.MONTH ) + 1;
                int d = c.get( Calendar.DAY_OF_MONTH );
                if (bean.mothFlag == 0 && bean.year == y && bean.moth == m && bean.day == d) {
                    text.setTextColor( Color.parseColor( "#2195F2" ) );
                    TextPaint tp = text.getPaint();
                    tp.setFakeBoldText( true );
                    text.setTextSize( TypedValue.COMPLEX_UNIT_SP, 15 );
                    ((LinearLayout.LayoutParams) chinaText.getLayoutParams()).setMargins( 0, 0, 0, DensityUtils.dp2px
                            ( mBaseActivity, (float) 2.5 ) );
                } else {
                    TextPaint tp = text.getPaint();
                    tp.setFakeBoldText( false );
                    text.setTextSize( TypedValue.COMPLEX_UNIT_SP, 13 );
                    ((LinearLayout.LayoutParams) chinaText.getLayoutParams()).setMargins( 0, 0, 0, 0 );
                }

                if (bean.mothFlag == 0 && !CommonUtils.isEmpty( bean.empSchScheduleShortName )) {
                    chinaText.setVisibility( View.VISIBLE );
                    chinaText.setText( bean.empSchScheduleShortName );
                    if (!CommonUtils.isEmpty( bean.empSchScheduleColor )) {
                        chinaText.setTextColor( Color.parseColor( bean.empSchScheduleColor ) );
                    } else {
                        chinaText.setTextColor( Color.parseColor( "#2195F2" ) );
                    }
                } else if (bean.mothFlag == 0) {//班次简称为空  设置为休息
                    chinaText.setText( "休" );
                    chinaText.setTextColor( Color.parseColor( "#ff0000" ) );
//                    chinaText.setVisibility( View.INVISIBLE );
                } else {
                    chinaText.setVisibility( View.GONE );
                }

                if (bean.mothFlag == 0) {
                    //判断是否点击
                    if (bean.toString().equals( curDate.toString() )) {
                        text.setTextColor( Color.WHITE );
                        chinaText.setTextColor( Color.WHITE );
                        bg.setBackgroundResource( R.drawable.shape_calendar );
                    } else {
                        bg.setBackgroundColor( Color.WHITE );
                        background.setBackgroundColor( Color.WHITE );
                    }
                }
                return convertView;
            }
        } );

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

        calendarDateView.setOnItemClickListener( new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
//                mTitle.setText(bean.year + "/" + bean.moth + "/" + bean.day);
                if (isByClick && bean.mothFlag == 0) {
//                    year_tv.setText(bean.year+"");
//                    date_tv.setText(bean.getDisplayWeek()+","+bean.moth+"月"+bean.day+"日");

//                    if (bean.year > nowDate.year || bean.moth > nowDate.moth
//                            || (bean.year == nowDate.year && bean.moth == nowDate.moth && bean.day > nowDate.day)){
//                        MyToast.showToast(mBaseActivity,"查看日期不能大于当前日期");
//                    }else {
                    curDate = bean;
                    date_tv.setText( curDate.moth + "月" + curDate.day + "日排班" );
                    try {
                        ((NormalFragment) mFragmentList.get( 0 )).normalItem( curDate );
                        ((OvertimeFragment) mFragmentList.get( 1 )).normalItem( curDate );
                        ((TemporaryFragment) mFragmentList.get( 2 )).normalItem( curDate );
                    } catch (Exception e) {
                    }
//                    }
                } else {
                    isByClick = true;
                }
                if (bean.mothFlag == 0)
                    time_tv.setText( CommonUtils.numToString( bean.moth ) + "月 " + bean.year );
                calendarDateView.getViews().get( calendarDateView.getCurrentItem() ).notifyDate();
            }
        } );


        left_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isByClick = false;
                monthDef--;
                int month = curDate.moth + monthDef;
                if (month < 10) {
                    getTop( curDate.year + "-0" + month );
                } else {
                    getTop( curDate.year + "-" + month );
                }
                calendarDateView.setCurrentItem( calendarDateView.getCurrentItem() - 1 );

            }
        } );
        right_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isByClick = false;
                monthDef++;
                int month = curDate.moth + monthDef;
                if (month < 10) {
                    getTop( curDate.year + "-0" + month );
                } else {
                    getTop( curDate.year + "-" + month );
                }
                calendarDateView.setCurrentItem( calendarDateView.getCurrentItem() + 1 );

            }
        } );

        //轨迹
        road.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( mBaseActivity, PeoPleMap.class );
//                intent.putExtra( "id", id );
                intent.putExtra( "name", curDate.year +"-"+ curDate.moth + "-" + curDate.day);
//                intent.putExtra( "type", "考勤" );
                startActivity( intent );
            }
        } );

        tab1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentList.get( 0 ).isAdded())
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                            .get( 0 ) ).commit();
                else
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                            .container_fl, mFragmentList.get( 0 ), "normal" ).commit();
                mSelected = 0;
                changeTab( 1 );
            }
        } );
        tab2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentList.get( 1 ).isAdded())
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                            .get( 1 ) ).commit();
                else
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                            .container_fl, mFragmentList.get( 1 ), "overtime" ).commit();

                mSelected = 1;
                changeTab( 2 );
            }
        } );
        tab3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentList.get( 2 ).isAdded())
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).show( mFragmentList
                            .get( 2 ) ).commit();
                else
                    mFragmentManager.beginTransaction().hide( mFragmentList.get( mSelected ) ).add( R.id
                            .container_fl, mFragmentList.get( 2 ), "temporary" ).commit();
                mSelected = 2;
                changeTab( 3 );
            }
        } );
    }

    @Override
    protected void requestOnCreate() {
        super.requestOnCreate();
        getData();
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

    public void getData() {
        if (curDate.moth < 10) {
            getTop( curDate.year + "-0" + curDate.moth );
        } else {
            getTop( curDate.year + "-" + curDate.moth );
        }
        date_tv.setText( curDate.moth + "月" + curDate.day + "日排班" );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        // TODO: add setContentView(...) invocation
        ButterKnife.bind( this );
    }

    /**
     * 日常 fragment 返回的 排班情况
     *
     * @param size
     */
    @Override
    public void SendNormalValue(int size) {
        Log.e( TAG, "NormalFragment 传递回来的值 : " + size );
        isOvalShow( ovalTab1, size );
    }

    /**
     * 加班 fragment 返回的 排班情况
     *
     * @param size
     */
    @Override
    public void SendOvertimeValue(int size) {
        Log.e( TAG, "OvertimeFragment 传递回来的值 : " + size );
        isOvalShow( ovalTab2, size );
    }

    /**
     * 临时 fragment 返回的 排班情况
     *
     * @param size
     */
    @Override
    public void SendTemValue(int size) {
        Log.e( TAG, "TemFragment 传递回来的值 : " + size );
        isOvalShow( ovalTab3, size );
    }

    /**
     * 日常 加班 临时 小圆点的显示与隐藏
     *
     * @param ovalTab
     * @param size
     */
    private void isOvalShow(ImageView ovalTab, int size) {
        if (size > 0) {
            ovalTab.setVisibility( View.VISIBLE );
        } else {
            ovalTab.setVisibility( View.INVISIBLE );
        }
    }



    private interface GetTop {
        @FormUrlEncoded
        @POST(Constants.SINGLEVIEW)
        Call<SingleViewResult> getTop(@FieldMap Map<String, Object> map);
    }

    /**
     * 获取当月的排班
     * @param month
     */
    private void getTop(String month) {
        showDialog( true );
        GetTop getTop = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create( GetTop.class );
        SingleViewParam param = new SingleViewParam();
        param.setMonth( month );
        Log.e( TAG, "getTop: "+ param.toString()  );
        Call<SingleViewResult> call = getTop.getTop( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<SingleViewResult>() {
            @Override
            public void onResponse(Call<SingleViewResult> call, Response<SingleViewResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler
                        .OnHandleListener<SingleViewResult>() {
                    @Override
                    public void onSuccess(SingleViewResult result) {
                        Log.e( TAG, "SingleViewResult : " + result.toString() );
                        List<SingleViewResult.DataBean> data = result.getData();
                        if (data != null && data.size() != 0) {
                            SingleViewResult.DataBean dataBean = data.get( 0 );
                            //日常排班
                            List<SingleViewResult.DataBean.EmpSchListBean> empSchList = dataBean.getEmpSchList();
                            List<SingleViewResult.DataBean.EmpSchListBean> overSchList = dataBean
                                    .getOvertimeEmpSchList();
                            List<SingleViewResult.DataBean.EmpSchListBean> temSchList = dataBean.getTempEmpSchList();
                            if (empSchList != null) {
                                for (int i = 0; i < empSchList.size(); i++) {
                                    SingleViewResult.DataBean.EmpSchListBean empSchListBean = empSchList.get( i );
                                    String empSchDate = empSchListBean.getEmpSchDate();
                                    Log.e( TAG, "--" + empSchDate + " onSuccess: " + empSchListBean
                                            .getEmpSchScheduleShortName() );
                                    getShortName( empSchListBean, empSchDate );
                                }
                                calendarDateView.getViews().get( calendarDateView.getCurrentItem() ).notifyDate();
                            }

                            if (overSchList != null) {//有日常 简称就显示日常简称  没有则显示加班简称
                                for (int i = 0; i < overSchList.size(); i++) {
                                    int x = 0;
                                    SingleViewResult.DataBean.EmpSchListBean schListBean = overSchList.get( i );
                                    String empSchDate = schListBean.getEmpSchDate();
                                    for (int j = 0; j < empSchList.size(); j++) {
                                        if (empSchDate.equals( empSchList.get( j ).getEmpSchDate() )) {
                                            x += 1;
                                            break;
                                        }
                                    }
                                    if (x == 0)
                                        getShortName( schListBean, empSchDate );
                                }
                                calendarDateView.getViews().get( calendarDateView.getCurrentItem() ).notifyDate();

                            }
                            if (temSchList != null) {//是否有 日常,加班,有就显示前两者 都没有,就显示临时简称
                                for (int i = 0; i < temSchList.size(); i++) {
                                    int x = 0;
                                    SingleViewResult.DataBean.EmpSchListBean schListBean = temSchList.get( i );
                                    String empSchDate = schListBean.getEmpSchDate();
                                    for (int j = 0; j < empSchList.size(); j++) {
                                        if (empSchDate.equals( empSchList.get( j ).getEmpSchDate() )) {
                                            x += 1;
                                            break;
                                        }
                                    }
                                    for (int j = 0; j < overSchList.size(); j++) {
                                        if (empSchDate.equals( overSchList.get( j ).getEmpSchDate() )) {
                                            x += 1;
                                            break;
                                        }
                                    }
                                    if (x == 0)
                                        getShortName( schListBean, empSchDate );
                                }
                                calendarDateView.getViews().get( calendarDateView.getCurrentItem() ).notifyDate();
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
            public void onFailure(Call<SingleViewResult> call, Throwable t) {
                showDialog( false );
                MyToast.showToast( mBaseActivity, "  网络连接失败，请稍后再试  " );
            }
        } );
    }
    /**
     * 生成当月的排班简称 显示在当月日历上
     *
     * @param empSchListBean
     * @param empSchDate
     */
    private void getShortName(SingleViewResult.DataBean.EmpSchListBean empSchListBean, String empSchDate) {
        List<CalendarBean> calendarBeen = calendarDateView.getViews().get(
                calendarDateView.getCurrentItem() ).getData();
        for (int j = 0; j < calendarBeen.size(); j++) {
            if (calendarBeen.get( j ).getParams().equals( empSchDate )) {
                calendarBeen.get( j ).empSchScheduleShortName = empSchListBean
                        .getEmpSchScheduleShortName();
                calendarBeen.get( j ).empSchScheduleColor = empSchListBean
                        .getEmpSchScheduleColor();
                break;
            }
        }
    }

}
