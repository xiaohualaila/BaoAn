package com.hz.junxinbaoan;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.adapter.TitleAdapter;

import butterknife.InjectView;

public class SignInActivity extends BaseActivity {

    @InjectView(R.id.my_title_view)
    RecyclerView myTitleView;
    TitleAdapter topadapter;
    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    protected void initViews() {
        super.initViews();
        myTitleView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager
                .HORIZONTAL));
//        myTitleView.setAdapter(topadapter);
    }
}
