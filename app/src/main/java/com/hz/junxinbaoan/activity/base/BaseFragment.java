package com.hz.junxinbaoan.activity.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hz.junxinbaoan.R;

import java.util.List;

/**
 * Fragment基类
 * Created by zhaohh on 2016/5/3.
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity mBaseActivity;
    public SharedPreferences my_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // FIXME: 2017/4/5
//        ToastUtil.showToast(getClass().getSimpleName());
        return createView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBaseActivity = (BaseActivity) getActivity();
        my_save=getActivity().getApplicationContext().getSharedPreferences("ADDRESS", Context.MODE_PRIVATE);
        getIntentData();
        initViews(view);


        addListeners();
        requestOnViewCreated();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 传值
     */
    protected void getIntentData() {
    }

    /**
     * 初始化本地数据
     */
    protected void initViews(View view) {
    }

    /**
     * 添加监听器
     */
    protected void addListeners() {
    }


    /**
     * 在onCreate中请求服务
     */
    protected void requestOnViewCreated() {
    }

    /**
     * 判断list是否为空
     *
     * @param list
     * @return true：list为空；false：list不为空
     */
    public static <T> boolean listNull(List<T> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public void openSetting(final Context context, final String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( R.string.notifyTitle );
        builder.setMessage( R.string.notifyMsg );
        // 拒绝, 退出应用
        builder.setNegativeButton( R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            int notifyAlertMsg = R.string.notifyAlertMsg;
                            Toast.makeText( context, context.getResources().getText( notifyAlertMsg ) + content,
                                    Toast.LENGTH_SHORT ).show();
                        }
                } );

        builder.setPositiveButton( R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                } );
        builder.setCancelable( false );
        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     * @param context
     */
    public void startAppSettings(Context context) {
        Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
        intent.setData( Uri.parse( "package:" + context.getPackageName() ) );
        startActivity( intent );
    }

}
