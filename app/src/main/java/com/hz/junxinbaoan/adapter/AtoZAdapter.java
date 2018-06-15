package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hz.junxinbaoan.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by linzp on 2017/10/25.
 */

public class AtoZAdapter extends BaseAdapter {
    Context context;
    List<String> list;


    public AtoZAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.a_to_z_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item.setText(list.get(position));


        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.item)
        TextView item;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
    public interface Touch{
        void doTouch(View v, MotionEvent event,int position);
    }
}
