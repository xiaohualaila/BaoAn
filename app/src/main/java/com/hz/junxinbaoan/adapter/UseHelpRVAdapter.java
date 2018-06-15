package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.webviews.WebViewActivity;
import com.hz.junxinbaoan.data.UseHelpBean;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ImageViewPlus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class UseHelpRVAdapter extends RecyclerView.Adapter {
    private List<UseHelpBean> datas;
    private Context context;

    public UseHelpRVAdapter(Context context) {
        this.context = context;
        this.datas = new ArrayList<>();
    }

    public void setDatas(List<UseHelpBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usehelp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final UseHelpBean bean = datas.get(position);

        if (!CommonUtils.isEmpty(bean.getHelpContent())){
            viewHolder.content.setText(bean.getHelpSummary());
        }

        if (!CommonUtils.isEmpty(bean.getHelpTitle())){
            viewHolder.title.setText(bean.getHelpTitle());
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, WebViewActivity.class)
                        .putExtra("title","帮助详情")
                        .putExtra("content",bean.getHelpContent())
                        .putExtra("dTitle",bean.getHelpTitle())
                        .putExtra("addTime",bean.getAddTime())
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title,content;
        private ImageViewPlus avaIv;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_tv);
            content = (TextView) itemView.findViewById(R.id.content_tv);
            avaIv = (ImageViewPlus) itemView.findViewById(R.id.ava_iv);
        }
    }
}
