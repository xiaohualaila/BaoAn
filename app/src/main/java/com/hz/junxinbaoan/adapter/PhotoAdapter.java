package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/8.
 */

public class PhotoAdapter extends BaseAdapter {
    private final ArrayList<String> photoPath;
    private final Context context;
    private TakePhoto takePhoto;

    public PhotoAdapter(Context context, ArrayList<String> photoPath, TakePhoto takePhoto) {
        this.context = context;
        this.photoPath = photoPath;
        this.takePhoto = takePhoto;
    }

    @Override
    public int getCount() {
        return photoPath.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e( "TAG","photoPath : "+photoPath.size() );
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from( context ).inflate( R.layout.image, null );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position != 9 && position == photoPath.size()){
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
                    .load(photoPath.get(position))
                    .centerCrop()
                    .into(holder.image);


            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePhoto.delPhoto(position,photoPath.get(position));
                }
            });
            holder.del.setVisibility(View.VISIBLE);
        }

        return convertView;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image,del;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById( R.id.image);
            del = (ImageView) itemView.findViewById(R.id.del);
        }
    }

    public interface TakePhoto{
        void takePhoto();
        void delPhoto(int position,String url);
    }
}
