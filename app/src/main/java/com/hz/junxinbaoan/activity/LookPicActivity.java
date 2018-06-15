package com.hz.junxinbaoan.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class LookPicActivity extends BaseActivity {
    private static final String TAG = "LookPicActivity";

    ViewPager vp;
    private int number;
    private PaAd2 paAd2;
    private List<String> pic_path;
    FrameLayout back;
    TextView title_name;
    LinearLayout box;

    @Override
    protected void getIntentData() {
//        pic_path = MyApplication.result;
        pic_path = (List<String>) getIntent().getSerializableExtra( "Pic_paths" );
        number = getIntent().getIntExtra( "number", 0 );
//        number = 0;
        Log.i( TAG, "getIntentData: " + pic_path );
    }


    @Override
    public void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_look_pic );
        vp = (ViewPager) findViewById( R.id.vp );
        back = (FrameLayout) findViewById( R.id.back );
        title_name = (TextView) findViewById( R.id.title_name );
        box = (LinearLayout) findViewById( R.id.box );
    }

    @Override
    public void initViews() {
        paAd2 = new PaAd2( pic_path );
        title_name.setText( number + 1 + "/" + pic_path.size() );
//        if (pic_path.size() == 1)    //只有1张图片时控制标题的隐藏
//            title_name.setVisibility( View.GONE );
//        else
            title_name.setVisibility( View.VISIBLE );
        vp.setAdapter( paAd2 );
        vp.setCurrentItem( number );

    }

    @Override
    public void addListeners() {
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
//        box.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        vp.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                number = i;
                title_name.setText( number + 1 + "/" + pic_path.size() );
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        } );
    }


    class PaAd2 extends PagerAdapter {
        List<ImageView> list = new ArrayList<>();
        List<String> images;

        public PaAd2(List<String> images) {
            this.images = images;
            for (int i = 0; i < images.size(); i++) {
                list.add( new ImageView( mBaseActivity ) );
            }
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View view, int position, Object object)                       //销毁Item
        {
            ImageView x = (ImageView) list.get( position );
            x.setScaleType( ImageView.ScaleType.FIT_CENTER );
            ((ViewPager) view).removeView( x );
        }

        @Override
        public Object instantiateItem(View view, int position)                                //实例化Item
        {
            ImageView x = (ImageView) list.get( position );
            x.setScaleType( ImageView.ScaleType.FIT_CENTER );

            Glide.with( mBaseActivity )
                    .load( images.get( position ) )
                    .fitCenter()
                    .into( x );


//            imageLoader.displayImage(images.get(position).toString(), x,options);
            ((ViewPager) view).addView( x, 0 );
//            x.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });

            return list.get( position );
        }
    }
}
