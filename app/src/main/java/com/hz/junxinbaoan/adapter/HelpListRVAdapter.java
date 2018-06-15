package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.help.HelpDetailActivity;
import com.hz.junxinbaoan.data.HelpListBean;
import com.hz.junxinbaoan.utils.CommonUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class HelpListRVAdapter extends RecyclerView.Adapter<HelpListRVAdapter.ViewHolder> {
    private List<HelpListBean> data;
    private Context context;

    public HelpListRVAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setData(List<HelpListBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_help_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HelpListRVAdapter.ViewHolder holder, int position) {
        final HelpListBean helpListBean = data.get(position);
        if (!CommonUtils.isEmpty(helpListBean.getReportContent())){
            holder.content_tv.setText(helpListBean.getReportContent());
        }
//        if (!CommonUtils.isEmpty(helpListBean.getReportPictures())){
//            Glide.with(context).load(helpListBean.getReportPictures()).into(holder.ava_iv);
//        }
        String name = "";
//        if (!CommonUtils.isEmpty(helpListBean.getReportEmployeeName())){
//            name = helpListBean.getReportEmployeeName();
//        }
        if (!CommonUtils.isEmpty(helpListBean.getReportTimeStr())){
            name += helpListBean.getReportTimeStr();
        }
        holder.name_tv.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, HelpDetailActivity.class).putExtra("id",helpListBean.getReportId()));
            }
        });

        if (!CommonUtils.isEmpty(helpListBean.getReportShowPicture())){
            try {
                Glide.with(context).load(helpListBean.getReportShowPicture()).centerCrop().into(holder.ava_iv);
                holder.ava_iv.setAlpha((float) 1.0);
            } catch (Exception ignore) {
            }
//            CommonUtils.loadPic(context, helpListBean.getReportShowPicture(), new CommonUtils.LoadPic() {
//                @Override
//                public void loadPic(byte[] bytes) {
//                    try {
//                        Glide.with(context).load(bytes).into(holder.ava_iv);
//                    } catch (Exception ignore) {
//                    }
//                }
//            });
        }else {
            Glide.with(context).load(R.mipmap.default_icon).into(holder.ava_iv);
            holder.ava_iv.setAlpha((float) 0.6);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ava_iv;
        private TextView name_tv,content_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            ava_iv = (ImageView) itemView.findViewById(R.id.ava_iv);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            content_tv = (TextView) itemView.findViewById(R.id.content_tv);
        }
    }
}
