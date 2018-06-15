package com.hz.junxinbaoan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by linzp on 2017/10/20.
 */

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.MyViewHolder> {
    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }


    }
    @Override
    public int getItemCount() {
       return 0;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getActivity()).inflate(R
//                .layout.info_title_gridview_item, parent, false));
        return null;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}