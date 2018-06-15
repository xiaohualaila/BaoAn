package com.hz.junxinbaoan.activity.dialog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.result.PeopleResult;
import com.hz.junxinbaoan.utils.MyToast;

import java.util.ArrayList;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by linzp on 2017/10/26.
 */

public class PhoneDialog extends BaseDialog {


    private final ArrayList<PermissionItem> permissionItems;
    private TextView call;
    private TextView cancel;
    private Context context;
    private String phone;
    private TextView name;
    private String name_str;

    public PhoneDialog(Context context, PeopleResult.PeopleBean bean) {
        super(context, R.style.dim_dialog);
        this.context = context;
        this.phone = bean.getEmployeePhone();
        name_str=bean.getEmployeeName();
        name = (TextView) findViewById(R.id.name);
        name.setText(name_str);

        permissionItems = new ArrayList<PermissionItem>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_callphone;
    }

    @Override
    protected void findViews() {
        call = (TextView) findViewById(R.id.call);
        cancel = (TextView) findViewById(R.id.cancel);
        addListeners();
    }

    private void addListeners() {
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionItems.clear();
                permissionItems.add( new PermissionItem( Manifest.permission.CALL_PHONE, "Phone", R.drawable
                        .permission_ic_phone ) );

                    dismiss();
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(phone)) {
                    String number = phone;
                    //用intent启动拨打电话
                    final Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phone);
                    intent.setData(data);
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
                                    context.startActivity( intent );
                                }

                                @Override
                                public void onDeny(String permission, int position) {

                                }

                                @Override
                                public void onGuarantee(String permission, int position) {

                                }
                            } );
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    context.startActivity(intent);
                }else {
                    MyToast.showToast(context, "  该用户没有电话信息  ");
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void setWindowParam() {
        setWindowParams(-1, -2, Gravity.BOTTOM,0);
    }
}
