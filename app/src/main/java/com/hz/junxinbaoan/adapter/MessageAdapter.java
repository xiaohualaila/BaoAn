package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.webviews.WebViewActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.ReadParams;
import com.hz.junxinbaoan.result.MessageDetail;
import com.hz.junxinbaoan.result.MessageResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ImageViewPlus;
import com.hz.junxinbaoan.utils.MyToast;
import com.hz.junxinbaoan.utils.ResultHandler;

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
 * Created by linzp on 2017/10/24.
 */

public class MessageAdapter extends BaseAdapter {
    private List<MessageResult.MessageBean>list;
    private Context context;

    public MessageAdapter(Context context,  List<MessageResult.MessageBean>list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.type.setText(list.get(position).getMessageType());
        if (list.get(position).getMessageType().trim().equals("系统消息")){
            viewHolder.pic.setVisibility(View.GONE);
        }else {
            viewHolder.pic.setVisibility(View.VISIBLE);
        }
        viewHolder.title.setText(list.get(position).getMessageTitle());
        viewHolder.body.setText(list.get(position).getMessageSummary());
        if (!TextUtils.isEmpty(list.get(position).getMessagePictures())){
            try {
                Glide.with(context).load(list.get(position).getMessagePictures()).asBitmap()
                        .centerCrop().error(R.mipmap.webview_ava)
                        .into(viewHolder.pic);
            } catch (Exception ignore) {
            }
//            CommonUtils.loadPic(context, list.get(position).getMessagePictures(), new CommonUtils.LoadPic() {
//                @Override
//                public void loadPic(byte[] bytes) {
//                    try {
//                        Glide.with(context).load(bytes).asBitmap()
//                                .centerCrop().error(R.mipmap.webview_ava)
//                                .into(finalViewHolder.pic);
//                    } catch (Exception ignore) {
//                    }
//                }
//            });
        }else {
            viewHolder.pic.setImageResource(R.mipmap.webview_ava);
        }


        String time_now= CommonUtils.doTime2(list.get(position).getGmtModified());
        viewHolder.time.setText(time_now);


        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.upOrDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.iSeebox.getVisibility()==View.GONE){
                    finalViewHolder.iSeebox.setVisibility(View.VISIBLE);
                    finalViewHolder.upOrDown.setImageResource(R.mipmap.icon_arrow_up);
                }else {
                    finalViewHolder.upOrDown.setImageResource(R.mipmap.icon_arrow_down);
                    finalViewHolder.iSeebox.setVisibility(View.GONE);
                }
            }
        });
        viewHolder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.upOrDown.performClick();
            }
        });
        viewHolder.iSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收到接口
                finalViewHolder.iSee.setText("已收到");
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG----",list.get(position).getMessageId() + "+++" + position);
                doDetail(list.get(position).getMessageId(),position);


            }
        });

        return convertView;
    }

    //详情
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.MESSAGEREAD)
        Call<MessageDetail> getData(@FieldMap Map<String, Object> map);
    }
    private void doDetail(String detail, final int position) {
        GetData getData = CommonUtils.buildRetrofit(Constants.BASE_URL, context).create(GetData.class);
        ReadParams params = new ReadParams();
        params.setDetail(detail);
        Call<MessageDetail> call = getData.getData(CommonUtils.getPostMap(params));
        call.enqueue(new Callback<MessageDetail>() {
            @Override
            public void onResponse(final Call<MessageDetail> call, final Response<MessageDetail> response) {
                ResultHandler.Handle(context, response.body(), new ResultHandler.OnHandleListener<MessageDetail>() {
                    @Override
                    public void onSuccess(MessageDetail result) {
                        MessageDetail.MessageBean data = result.getData();
                        Log.e("TAG+++++++",result.toString() + "----" +list.get(position).getMessageContent());
                        Intent intent=new Intent(context, WebViewActivity.class);
//                        intent.putExtra("id",list.get(position).getMessageId());
//                        intent.putExtra("title","消息详情");
//                        intent.putExtra("company",list.get(position).getMessageShowName());
//                        intent.putExtra("content",list.get(position).getMessageContent());
//                        intent.putExtra("dTitle",list.get(position).getMessageTitle());
//                        intent.putExtra("addTime",list.get(position).getAddTime());
//                        intent.putExtra("url",list.get(position).getMessagePictures());

                        intent.putExtra("id", data.getMessageAdminId());
                        intent.putExtra("title","消息详情");
                        intent.putExtra("company",data.getMessageShowName());
                        intent.putExtra("content",data.getMessageContent());
                        intent.putExtra("dTitle",data.getMessageTitle());
                        intent.putExtra("addTime",data.getAddTime());
                        intent.putExtra("url",data.getMessagePictures());
                        context.startActivity(intent);
                    }
                    @Override
                    public void onNetError() {
                    }
                    @Override
                    public void onError(String code, String message) {
                    }
                });
            }
            @Override
            public void onFailure(Call<MessageDetail> call, Throwable t) {
                MyToast.showToast(context, "  网络连接失败，请稍后再试  ");
            }
        });
    }


    class ViewHolder {
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.up_or_down)
        ImageView upOrDown;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.body)
        TextView body;
        @BindView(R.id.pic)
        ImageViewPlus pic;
        @BindView(R.id.i_see)
        TextView iSee;
        @BindView(R.id.i_seebox)
        LinearLayout iSeebox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
