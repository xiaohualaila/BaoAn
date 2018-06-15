package com.hz.junxinbaoan.activity.mine;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ZoomButtonsController;

import com.hz.junxinbaoan.R;
import com.hz.junxinbaoan.activity.base.BaseActivity;
import com.hz.junxinbaoan.common.Constants;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.InjectView;

/**
 * Created by Administrator on 2017/11/13 0013.
 */

public class HelpWebActivity extends BaseActivity{
    @InjectView(R.id.webview)
    WebView webView;
    @InjectView(R.id.back)
    FrameLayout back;

    @Override
    protected void findViews(Bundle savedInstanceState) {
        super.findViews(savedInstanceState);
        setContentView(R.layout.activity_help_web);
    }

    @Override
    protected void initViews() {
        super.initViews();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);

        //隐藏缩放按钮
        int sysVersion = Integer.parseInt(Build.VERSION.SDK);
        if(sysVersion>=11){
            setZoomControlGoneX(webView.getSettings(),new Object[]{false});
        }else{
            setZoomControlGone(webView);
        }
        webView.loadUrl(Constants.HELP_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                showDialog(false);
                super.onPageFinished(view, url);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showDialog(true);
                super.onPageStarted(view, url, favicon);
            }
        });
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //隐藏webview的缩放按钮 适用于3.0和以后
    public void setZoomControlGoneX(WebSettings view ,Object[] args){
        Class classType = view.getClass();
        try {
            Class[] argsClass = new Class[args.length];

            for (int i = 0, j = args.length; i < j; i++) {
                argsClass[i] = args[i].getClass();
            }
            Method[] ms= classType.getMethods();
            for (int i = 0; i < ms.length; i++) {
                if(ms[i].getName().equals("setDisplayZoomControls")){
                    try {
                        ms[i].invoke(view, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                //Log.e("test", ">>"+ms[i].getName());
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    //隐藏webview的缩放按钮 适用于3.0以前
    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
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
