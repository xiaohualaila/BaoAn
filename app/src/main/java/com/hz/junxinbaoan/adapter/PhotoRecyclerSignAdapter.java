package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;

import java.util.List;

/**
 * 可编辑可删除的图片
 * Created by Administrator on 2017/10/20 0020.
 */

public class PhotoRecyclerSignAdapter extends RecyclerView.Adapter<PhotoRecyclerSignAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<String> result;
    private TakePhoto takePhoto;

    public PhotoRecyclerSignAdapter(Context context, List<String> result, TakePhoto takePhoto) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.result = result;
        this.takePhoto = takePhoto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.image, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e( "TAG","onBindViewHolder --- position: " +position+ "result.size : "+result.size());
        if (position != 9 && position == result.size()){
//            holder.image.setImageResource(R.mipmap.pick_phone);
            Glide.with(context)
                    .load(R.mipmap.pick_phone)
                    .into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePhoto.takePhoto();
                }
            });
            holder.del.setVisibility(View.GONE);
        }else {
            Glide.with(context)
                    .load(result.get(position))
                    .centerCrop()
                    .into(holder.image);

//            ImageDisplayer.getInstance(context).displayBmp(holder.image,
//                    null, result.get(position));
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    result.remove(position);
                    takePhoto.delPhoto(position,result.get(position));
//                    notifyDataSetChanged();
                }
            });
            holder.del.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return result.size() == 9 ? 9 : result.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image,del;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            del = (ImageView) itemView.findViewById(R.id.del);
        }
    }

    public interface TakePhoto{
        void takePhoto();
        void delPhoto(int position, String url);
    }
}
