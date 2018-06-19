package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.approval.ActivityApprovalCommit;
import com.hz.junxinbaoan.activity.help.HelpCommitActivity;
import com.hz.junxinbaoan.activity.mine.SchedulingActivity;
import com.hz.junxinbaoan.activity.study.ActivityStudyList;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.data.SquareBean;
import com.hz.junxinbaoan.params.BaseParam;
import com.hz.junxinbaoan.result.RolePermissionResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

import net.qiujuer.genius.ui.widget.Button;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by linzp on 2017/10/24.
 */

public class SquareAdapter extends BaseAdapter {
    private static final String TAG = "SquareAdapter";
    private Context context;
    private SquareBean squareBean;
    private RolePermissionResult.RolePermissionBean roleData;

    public SquareAdapter(Context context, SquareBean squareBean) {
        this.context = context;
        this.squareBean = squareBean;
        getRole();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from( context ).inflate( R.layout.square_item, null );
            viewHolder = new ViewHolder( convertView );
            convertView.setTag( viewHolder );
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams params = viewHolder.box.getLayoutParams();
        params.height = params.width;
        viewHolder.box.setLayoutParams( params );

        if (squareBean != null) {
            switch (position) {
                case 0:
                    viewHolder.box_btn.setBackgroundResource( R.mipmap.back_qzbl );
                    viewHolder.icon.setImageResource( R.mipmap.icon_qzbl );
                    viewHolder.title.setText( "求助爆料" );
                    viewHolder.body.setText( squareBean.getHelp() );
                    break;
                case 1:
                    viewHolder.box_btn.setBackgroundResource( R.mipmap.back_wdpb );
                    viewHolder.icon.setImageResource( R.mipmap.icon_wdpb );
                    viewHolder.title.setText( "我的排班" );
                    viewHolder.body.setText( squareBean.getMyproject() );
                    break;
                case 2:
                    viewHolder.box_btn.setBackgroundResource( R.mipmap.back_sp );
                    viewHolder.icon.setImageResource( R.mipmap.icon_sp );
                    viewHolder.title.setText( "审批申请" );
                    viewHolder.body.setText( squareBean.getCheck() );
                    break;
                case 3:
                    viewHolder.box_btn.setBackgroundResource( R.mipmap.back_xxzx );
                    viewHolder.icon.setImageResource( R.mipmap.icon_xxzx );
                    viewHolder.title.setText( "学习中心" );
                    viewHolder.body.setText( squareBean.getStudy() );
                    break;
            }
        }
        viewHolder.box_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0://求助爆料
                        if (null != roleData && null != roleData.getIsAppReport() && 1 == roleData
                                .getIsAppReport())
                            context.startActivity( new Intent( context, HelpCommitActivity.class ) );
                        else
                            MyToast.showToast( context, context.getResources().getText( R.string.noRolePermission ) +
                                    "" );
//                        Intent intent=new Intent(context, HelpCommitActivity.class);
//                        context.startActivity(intent);
                        break;
                    case 1://我的排班
                        if (null != roleData && null != roleData.getIsAppSchedule() && 1 == roleData.getIsAppSchedule())
                            context.startActivity( new Intent( context, SchedulingActivity.class ) );
                        else
                            MyToast.showToast( context, context.getResources().getText( R.string.noRolePermission ) +
                                    "" );
                        break;
                    case 2://审批
                        if (null != roleData && null != roleData.getIsAppRequest() && 1 == roleData.getIsAppRequest())
                            context.startActivity( new Intent( context, ActivityApprovalCommit.class ) );
                        else
                            MyToast.showToast( context, context.getResources().getText( R.string.noRolePermission ) +
                                    "" );
//                        Intent intent3=new Intent(context, ActivityApprovalCommit.class);
//                        context.startActivity(intent3);
                        break;
                    case 3://学习中心
                        if (null != roleData && null != roleData.getIsAppLearn() && 1 == roleData.getIsAppLearn())
                            context.startActivity( new Intent( context, ActivityStudyList.class ) );
                        else
                            MyToast.showToast( context, context.getResources().getText( R.string.noRolePermission ) +
                                    "" );
//                        Intent intent4=new Intent(context, ActivityStudyList.class);
//                        context.startActivity(intent4);
                        break;
                }

            }
        } );

        return convertView;
    }

    //获取角色权限
    private interface GetRole {
        @FormUrlEncoded
        @POST(Constants.ROLE_PERMISSION)
        Call<RolePermissionResult> getRoles(@FieldMap Map<String, Object> map);
    }

    private void getRole() {
        GetRole getData = CommonUtils.buildRetrofit( Constants.BASE_URL, context ).create( GetRole.class );
        BaseParam params = new BaseParam();
        params.setAccess_token( MyApplication.mUserInfo.getAccess_token() );
        Call<RolePermissionResult> call = getData.getRoles( CommonUtils.getPostMap( params ) );
        call.enqueue( new Callback<RolePermissionResult>() {
            @Override
            public void onResponse(final Call<RolePermissionResult> call, final Response<RolePermissionResult>
                    response) {
                ResultHandler.Handle( context, response.body(), new ResultHandler
                        .OnHandleListener<RolePermissionResult>() {

                    @Override
                    public void onSuccess(RolePermissionResult result) {
                        Log.e( TAG, "onSuccess: " + result.toString() );
                        if (result != null && result.getCode().equals( "0000" ) && result.getData() != null) {
                            Log.e( TAG, "onSuccess: " + result.toString() );
                            roleData = result.getData();
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
            public void onFailure(Call<RolePermissionResult> call, Throwable t) {
                MyToast.showToast( context, "  网络连接失败，请稍后再试  " );
            }
        } );
    }


    class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.body)
        TextView body;
        @BindView(R.id.box)
        RelativeLayout box;
        @BindView(R.id.box_btn)
        Button box_btn;

        ViewHolder(View view) {
            ButterKnife.bind( this, view );
        }
    }
}
