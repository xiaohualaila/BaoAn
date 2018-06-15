package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.data.ApprovalListBean;
import com.hz.junxinbaoan.result.RequestResult;
import com.hz.junxinbaoan.utils.CommonUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by linzp on 2017/10/24.
 */

public class ApprovalListAdapter extends BaseAdapter {
    private List<RequestResult.DataBean> list;
    private Context context;

    public ApprovalListAdapter(Context context, List<RequestResult.DataBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.approval_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //    data.requestId	id
//    data.requestType	申请类型
//    data.requestReason	申请原因
//    data.requestApplyTimestamp	申请时间戳
//    data.requestApplyTimeStr	申请时间
//    data.requestBeginTimestamp	申请起始时间
//    data.requestBeginTimeStr	申请起始时间
//    data.requestEndTimestamp	申请结束时间
//    data.requestEndTimeStr	申请结束时间
//    data.requestApproval	是否审批通过
//    data.requestToAdminId	指定审批人
//    data.requestRejectionReason	拒绝原因
//    data.requestEmployeeId	申请人id
//    data.requestEmployeeName	申请人姓名
//    data.requestStatus	审批状态 0-未处理1-通过2-未通过
//    data.gmtCreate	添加时间
//    data.gmtModified	修改时间

        viewHolder.type.setText("请假");

        viewHolder.title.setText(list.get(position).getRequestType()+":"+list.get(position).getRequestBeginTimeStr()+"~"+list.get(position).getRequestEndTimeStr()
                );
        viewHolder.body.setText("事由:"+list.get(position).getRequestReason());
        switch (list.get(position).getRequestStatus()){
            case 0:viewHolder.iSee.setText("未处理");viewHolder.iSee.setTextColor(context.getResources().getColor(R.color.text_gray2));
                break;
            case 1:viewHolder.iSee.setText("已通过");viewHolder.iSee.setTextColor(context.getResources().getColor(R.color.text_blue));
                break;
            case 2:viewHolder.iSee.setText("已驳回");viewHolder.iSee.setTextColor(context.getResources().getColor(R.color.text_red));
                break;
        }
        String time_now= CommonUtils.doTime2(list.get(position).getRequestApplyTimestamp());
        viewHolder.time.setText(time_now);
        return convertView;
    }



    class ViewHolder {
        @InjectView(R.id.type)
        TextView type;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.linearLayout)
        LinearLayout linearLayout;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.body)
        TextView body;
        @InjectView(R.id.i_see)
        TextView iSee;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
