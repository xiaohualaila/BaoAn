package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.LookPicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 求助爆料  的图片大图浏览
 * Created by Administrator on 2017/10/20 0020.
 */

public class SeePhotoHelpDetailAdapter extends RecyclerView.Adapter<SeePhotoHelpDetailAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
//    private List<byte[]> result;
    private List<String > result;

    public SeePhotoHelpDetailAdapter(Context context, ArrayList<String> result) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.result = result;
    }

    public void setData(ArrayList<String> result) {
        this.result = result;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.image, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int pos) {
        Glide.with(context)
                .load(result.get(pos))
                .centerCrop()
                .into(holder.image);
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.remove(pos);
                notifyDataSetChanged();
            }
        });
        holder.del.setVisibility(View.GONE);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyApplication.result.clear();
//                MyApplication.result = result;
//                List<Bitmap > list = new ArrayList<Bitmap>();
//                for (int i = 0; i < result.size(); i++) {
//                    list.add(BitmapFactory.decodeByteArray(result.get(i), 0, result.get(i).length));
////                    Drawable bd= new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(b, 0, b.length));
//                }
                context.startActivity(new Intent(context, LookPicActivity.class)
                        .putExtra("number",pos)
                        .putExtra("Pic_paths", (Serializable) result)
                );
            }
        });


    }

    @Override
    public int getItemCount() {
        return result.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image,del;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            del = (ImageView) itemView.findViewById(R.id.del);
        }
    }
}
