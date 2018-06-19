package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.webviews.WebViewActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.ReadParams;
import com.hz.junxinbaoan.result.StudyDetailResult;
import com.hz.junxinbaoan.result.StudyResult;
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
 * Created by linzp on 2017/10/25.
 */

public class StudyAdapter extends BaseAdapter {
    List<StudyResult.DataBean> list;
    Context context;

    public StudyAdapter(List<StudyResult.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.studylist_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list.get(position).getLearnTitle());
        viewHolder.body.setText(list.get(position).getLearnSummary());

        if (!TextUtils.isEmpty(list.get(position).getLearnPictures())){
            viewHolder.pic.setVisibility(View.VISIBLE);
            try {
                Glide.with(context).load(list.get(position).getLearnPictures()).asBitmap()
                        .centerCrop().error(R.mipmap.webview_ava)
                        .into(viewHolder.pic);
            } catch (Exception ignore) {
            }
//            CommonUtils.loadPic(context, list.get(position).getLearnPictures(), new CommonUtils.LoadPic() {
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
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG.doDetail:",list.get(position).getLearnId()+ "--"+position);
                doDetail(list.get(position).getLearnId(),position);
            }
        });

        return convertView;
    }


    //详情
    private interface GetData {
        @FormUrlEncoded
        @POST(Constants.LEARNREAD)
        Call<StudyDetailResult> getData(@FieldMap Map<String, Object> map);
    }
    private void doDetail(String detail, final int position) {
        GetData getData = CommonUtils.buildRetrofit(Constants.BASE_URL, context).create(GetData.class);
        ReadParams params = new ReadParams();
        params.setLearnId(detail);
        Log.e("TAG,params:",params.toString());
        Call<StudyDetailResult> call = getData.getData(CommonUtils.getPostMap(params));
        call.enqueue(new Callback<StudyDetailResult>() {
            @Override
            public void onResponse(final Call<StudyDetailResult> call, final Response<StudyDetailResult> response) {
                ResultHandler.Handle(context, response.body(), new ResultHandler.OnHandleListener<StudyDetailResult>() {
                    @Override
                    public void onSuccess(StudyDetailResult result) {

                        Log.e("TAG - 学习中心",result.toString());
                        Intent intent=new Intent(context, WebViewActivity.class);
//                        intent.putExtra("id",list.get(position).getLearnId());
//                        intent.putExtra("title","详情");
//                        intent.putExtra("company",list.get(position).getLearnShowName());
//                        intent.putExtra("content",list.get(position).getLearnContent());
//                        intent.putExtra("dTitle",list.get(position).getLearnTitle());
//                        intent.putExtra("addTime",list.get(position).getAddTime());
//                        intent.putExtra("url",list.get(position).getLearnPictures());
                        StudyDetailResult.DataBean data = result.getData();
                        intent.putExtra("id",data.getLearnId());
                        intent.putExtra("title","详情");
                        intent.putExtra("company",data.getLearnShowName());
                        intent.putExtra("content",data.getLearnContent());
                        intent.putExtra("dTitle",data.getLearnTitle());
                        intent.putExtra("addTime",data.getAddTime());
                        intent.putExtra("url",data.getLearnPictures());
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
            public void onFailure(Call<StudyDetailResult> call, Throwable t) {
                MyToast.showToast(context, "  网络连接失败，请稍后再试  ");
            }
        });
    }



    class ViewHolder {
        @BindView(R.id.pic)
        ImageViewPlus pic;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.body)
        TextView body;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}