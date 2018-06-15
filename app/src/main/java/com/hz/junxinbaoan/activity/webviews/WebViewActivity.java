package com.hz.junxinbaoan.activity.webviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.bumptech.glide.Glide;
import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.params.GetHelpDetailParam;
import com.hz.junxinbaoan.result.BaseResult;
import com.hz.junxinbaoan.utils.CommonUtils;
import com.hz.junxinbaoan.utils.ImageViewPlus;
import com.hz.junxinbaoan.utils.ResultHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class WebViewActivity extends BaseActivity {

    @InjectView(R.id.back)
    FrameLayout back;
    @InjectView(R.id.title_name)
    TextView titleName;
    @InjectView(R.id.title_lay)
    RelativeLayout titleLay;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.dTitle_tv)
    TextView dTitle_tv;
    @InjectView(R.id.time_tv)
    TextView time_tv;
    @InjectView(R.id.name_tv)
    TextView name_tv;
    private String company;
    @InjectView(R.id.ava_iv)
    ImageViewPlus ava_iv;
    String id = "";
    String title = "";
    private String content;
    private String dTitle;
    private String addTime;
    private String url;

    @Override
    protected void getIntentData() {
        super.getIntentData();
        id = getIntent().getStringExtra( "id" );
        title = getIntent().getStringExtra( "title" );
        content = getIntent().getStringExtra( "content" );
        dTitle = getIntent().getStringExtra( "dTitle" );
        addTime = getIntent().getStringExtra( "addTime" );
        company = getIntent().getStringExtra( "company" );

    }

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews( savedInstanceState );
        setContentView( R.layout.activity_webview );
    }

    @Override
    protected void initViews() {
        super.initViews();
        url = getIntent().getStringExtra( "url" );
        if (!TextUtils.isEmpty( url )) {
            try {
                Glide.with( mBaseActivity ).load( url ).asBitmap()
                        .centerCrop().error( R.mipmap.webview_ava )
                        .into( ava_iv );
            } catch (Exception ignore) {
            }
        } else {
            ava_iv.setImageResource( R.mipmap.webview_ava );
        }

        if (!TextUtils.isEmpty( company )) {
            name_tv.setText( company );
        }

        if (!CommonUtils.isEmpty( content ) && !content.contains( "body" )) {
            content = "<body style = 'font-size: 18px; line-height: 27px;  text-align:justify; font-family:helvetica;" +
                    " color: #4f4f4f'>"
                    + content + "</body>";
        }
        String picUrl = "";
        String pics = "";
        if (!TextUtils.isEmpty( content )) {
            Pattern p = Pattern.compile( "<img\\s*([^>]*)>" );
            Matcher m = p.matcher( content );
            while (m.find()) {
                String s0 = m.group();
                if (!s0.contains( "width:100%" )) {
                    String string2 = s0.replace( "/>",
                            " style='width:100%;  min-height: 105px;'/>" );
                    content = content.replace( m.group(), string2 );
                }
            }
        }

        if (title != null) {
            titleName.setText( title );
        }
        if (!CommonUtils.isEmpty( content ))
            content = content.replace( "<img", "<img style='max-width:90%;height:auto;'" );

        if (!CommonUtils.isEmpty( content )) {
            webview.loadDataWithBaseURL( null, content, "text/html", "utf-8", null );
        }
        if (!CommonUtils.isEmpty( dTitle )) {
            dTitle_tv.setText( dTitle );
        }
        if (!CommonUtils.isEmpty( addTime )) {
            time_tv.setText( addTime );
        }
        geturl();
    }

    //根据id获取url加载进webview
    private void geturl() {
        //假数据
//        webview.loadUrl("http://www.baidu.com/");
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
    }

    @Override
    protected void requestOnCreate() {
        super.requestOnCreate();

//        switch (title){
//            case "帮助详情":
//                getHelpDetail();
//                break;
//        }
    }

    private interface GetHelpDetail {
        @FormUrlEncoded
        @POST(Constants.HELP_DETAIL)
        Call<BaseResult> getHelpDetail(@FieldMap Map<String, Object> map);
    }

    private void getHelpDetail() {
        showDialog( true );
        GetHelpDetail getHelpDetail = CommonUtils.buildRetrofit( Constants.BASE_URL, mBaseActivity ).create(
                GetHelpDetail.class );
        GetHelpDetailParam param = new GetHelpDetailParam();
        param.setHelpId( id );
        Call<BaseResult> call = getHelpDetail.getHelpDetail( CommonUtils.getPostMap( param ) );
        call.enqueue( new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                showDialog( false );
                ResultHandler.Handle( mBaseActivity, response.body(), new ResultHandler.OnHandleListener<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {

                    }

                    @Override
                    public void onNetError() {

                    }

                    @Override
                    public void onError(String code, String message) {

                    }
                } );
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {
                showDialog( false );
            }
        } );
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading( view, url );
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled( true );

            super.onPageFinished( view, url );
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled( true );

            super.onPageStarted( view, url, favicon );
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError( view, errorCode, description, failingUrl );

        }
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webview.loadUrl( "javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()" );
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
//            startActivity(new Intent(NewsDetailActivity.this, BroserImageActivity.class).putExtra("from","pic")
// .putExtra("url",img));
        }
    }

    //隐藏webview的缩放按钮 适用于3.0和以后
    public void setZoomControlGoneX(WebSettings view, Object[] args) {
        Class classType = view.getClass();
        try {
            Class[] argsClass = new Class[args.length];

            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
            Method[] ms = classType.getMethods();
            for (int i = 0; i < ms.length; i++) {
                if (ms[i].getName().equals( "setDisplayZoomControls" )) {
                    try {
                        ms[i].invoke( view, false );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                //Log.e("test", ">>"+ms[i].getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //隐藏webview的缩放按钮 适用于3.0以前
    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField( "mZoomButtonsController" );
            field.setAccessible( true );
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController( view );
            mZoomButtonsController.getZoomControls().setVisibility( View.GONE );
            try {
                field.set( view, mZoomButtonsController );
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
