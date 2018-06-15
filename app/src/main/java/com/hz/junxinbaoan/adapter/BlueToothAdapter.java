package com.hz.junxinbaoan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.bluetooth.IBeacon;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class BlueToothAdapter extends BaseAdapter {
    private List<IBeacon> iBeacons;
    private Context context;
    private GetBlueToothInfoListener listener;

    public BlueToothAdapter(List<IBeacon> iBeacons, Context context,GetBlueToothInfoListener listener) {
        this.iBeacons = iBeacons;
        this.context = context;
        this.listener = listener;
    }

    public void setiBeacons(List<IBeacon> iBeacons) {
        this.iBeacons = iBeacons;
        notifyDataSetChanged();
    }

    public BlueToothAdapter(List<IBeacon> iBeacons) {
        this.iBeacons = iBeacons;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return iBeacons == null ? 0 : iBeacons.size();
    }

    @Override
    public IBeacon getItem(int position) {
        return iBeacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bluttooth,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final IBeacon iBeacon = iBeacons.get(position);
        if (!CommonUtils.isEmpty(iBeacon.getIbeaconComment())){
            viewHolder.name_tv.setText(iBeacon.getIbeaconComment());
        }
        if (iBeacon.isChoose()){
            viewHolder.choose_iv.setSelected(true);
        }else {
            viewHolder.choose_iv.setSelected(false);
        }

        viewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!viewHolder.choose_iv.isSelected()){
                    for (int i = 0; i < iBeacons.size(); i++) {
                        if (iBeacon.getMajor() == iBeacons.get(i).getMajor()){
                            iBeacons.get(i).setChoose(true);
                            listener.getBlutToothInfo(iBeacons.get(i));
                        }else {
                            iBeacons.get(i).setChoose(false);
                        }
                        notifyDataSetChanged();
                    }
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        TextView name_tv;
        ImageView choose_iv;
        LinearLayout ll;

        public ViewHolder(View itemView){
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            choose_iv = (ImageView) itemView.findViewById(R.id.choose_iv);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
        }
    }

    public interface GetBlueToothInfoListener{
        void getBlutToothInfo(IBeacon iBeacon);
    }
}
