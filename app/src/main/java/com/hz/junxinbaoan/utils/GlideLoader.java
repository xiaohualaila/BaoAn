package com.hz.junxinbaoan.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class GlideLoader implements com.yancy.imageselector.ImageLoader {
    @Override
    public void displayImage(Context context, String path, final ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        Bitmap bitmap= BitmapFactory.decodeFile(path);
//        imageView.setImageBitmap(bitmap);
        ImageDisplayer.getInstance(context).displayBmp(imageView,
                null, path);
//        Glide.with(context)
//                .load(bitmap)
//                .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
//                .centerCrop()
//                .into(imageView);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        Glide.with(context).load(path).asBitmap().placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//
//                imageView.setImageBitmap(resource);
//            }
//        });
    }
}
