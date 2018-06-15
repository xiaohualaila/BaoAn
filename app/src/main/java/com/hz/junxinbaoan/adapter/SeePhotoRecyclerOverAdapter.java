package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.LookPicActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * 排班 考勤的大图浏览
 * Created by Administrator on 2017/10/20 0020.
 */

public class SeePhotoRecyclerOverAdapter extends RecyclerView.Adapter<SeePhotoRecyclerOverAdapter.ViewHolder> {

    private final int position;
    private Context context;
    private LayoutInflater mLayoutInflater;
    //    private List<byte[]> result;
    private Map<Integer, ArrayList<String>> result;

    public SeePhotoRecyclerOverAdapter(Context context, Map<Integer, ArrayList<String>> result, int position) {
        mLayoutInflater = LayoutInflater.from( context );
        this.context = context;
        this.result = result;
        this.position = position;
    }

    public void setData(Map<Integer, ArrayList<String>> result) {
        this.result = result;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder( mLayoutInflater.inflate( R.layout.seeimage, null ) );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int pos) {
//        Log.i( "TAG", "onBindViewHolder: "+pos );
        Log.i( "TAG", result.get( position ).size() + "---" + position + "pic  onBindViewHolder: " + pos + "===" +
                result
                        .toString() );
        if (null != result && result.get( position ).size() > 0) {
            Log.i( "TAG", "onBindViewHolder: " + result.get( position ).toString() );
            Glide.with( context )
                    .load( result.get( position ).get( pos ) )
                    .centerCrop()
                    .into( holder.image );
        }

        holder.image.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyApplication.result.clear();
//                MyApplication.result = result;
//                List<Bitmap > list = new ArrayList<Bitmap>();
//                for (int i = 0; i < result.size(); i++) {
//                    list.add(BitmapFactory.decodeByteArray(result.get(i), 0, result.get(i).length));
////                    Drawable bd= new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(b, 0, b
/// .length));
//                }
                Log.i( "TAG", position + "pic  onClick: " + pos + "===" + result.toString() );
                context.startActivity( new Intent( context, LookPicActivity.class )
                        .putExtra( "number", pos )
                        .putExtra( "Pic_paths", (Serializable) result.get( position ) )
                );
            }
        } );

    }


    @Override
    public int getItemCount() {
//        return result.size();
        if (null != result && null != result.get( position ) && result.get( position ).size() > 0)
            return result.get( position ).size();
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View itemView) {
            super( itemView );
            image = (ImageView) itemView.findViewById( R.id.image );
        }
    }
}
