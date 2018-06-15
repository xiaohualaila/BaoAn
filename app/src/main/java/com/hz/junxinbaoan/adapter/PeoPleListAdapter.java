package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.dialog.PhoneDialog;
import com.hz.junxinbaoan.result.PeopleResult;
import com.hz.junxinbaoan.utils.ImageViewPlus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by linzp on 2017/10/25.
 */

public class PeoPleListAdapter extends BaseAdapter {
    private Context context;
    private List<PeopleResult.PeopleBean> list = new ArrayList<>();

    public PeoPleListAdapter(Context context, List<PeopleResult.PeopleBean> peopleBeanList) {
        this.context = context;
        this.list = peopleBeanList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.people_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(list.get(position).getEmployeeName());
        if (list.get(position).getEmployeeNameFirstCharInPinYin().equals("管")){
            viewHolder.type.setVisibility(View.GONE);
            viewHolder.guan.setVisibility(View.VISIBLE);
            if (position>0&&list.get(position).getEmployeeNameFirstCharInPinYin().equals(list.get(position-1).getEmployeeNameFirstCharInPinYin())){
                viewHolder.guan.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.guan.setVisibility(View.VISIBLE);
            }
        }else {
            viewHolder.guan.setVisibility(View.GONE);
            viewHolder.type.setText(list.get(position).getEmployeeNameFirstCharInPinYin());
            if (position>0&&list.get(position).getEmployeeNameFirstCharInPinYin().equals(list.get(position-1).getEmployeeNameFirstCharInPinYin())){
                viewHolder.type.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.type.setVisibility(View.VISIBLE);
            }
        }
        if (!TextUtils.isEmpty(list.get(position).getEmployeePhoto())){
            viewHolder.pic.setVisibility(View.VISIBLE);
            viewHolder.firstname.setText("");
            try {
                Glide.with(context).load(list.get(position).getEmployeePhoto()).asBitmap()
                        .centerCrop().error(R.mipmap.mine_avbg)
                        .into(viewHolder.pic);
            } catch (Exception ignore) {
            }
//            CommonUtils.loadPic(context, list.get(position).getEmployeePhoto(), new CommonUtils.LoadPic() {
//                @Override
//                public void loadPic(byte[] bytes) {
//                    try {
//                        Glide.with(context).load(bytes).asBitmap()
//                                .centerCrop().error(R.mipmap.mine_avbg)
//                                .into(finalViewHolder.pic);
//                    } catch (Exception ignore) {
//                    }
//                }
//            });
        }else {
            viewHolder.pic.setImageResource(R.color.picback);
            viewHolder.firstname.setText("" + list.get(position).getEmployeeName().charAt(list.get(position).getEmployeeName().length()-1));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhoneDialog(context,list.get(position)).show();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.type)
        TextView type;
        @InjectView(R.id.pic)
        ImageViewPlus pic;
        @InjectView(R.id.name)
        TextView name;
        @InjectView(R.id.guan)
        TextView guan;
        @InjectView(R.id.firstname)
        TextView firstname;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
