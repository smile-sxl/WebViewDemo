package com.sxl.webviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WebView mWebview;
    private WebSettings mWebSettings;
    private TextView tvData;
    private EditText edtTxtData;
    private Button btnSend;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        tvData = (TextView) findViewById(R.id.tv_Data);
        edtTxtData = (EditText) findViewById(R.id.edtTxt_Data);
        btnSend = (Button) findViewById(R.id.btn_Send);
        mWebview = (WebView) findViewById(R.id.webView);

        // 获取mWebSettings 对WebView进行配置和管理
        mWebSettings = mWebview.getSettings();

        // 允许webview 加载js代码
        mWebSettings.setJavaScriptEnabled(true);
        // 给webView 加载js 接口
        mWebview.addJavascriptInterface(new JStoAndroid(new JSInterface() {
            @Override
            public void setVaule(final String vaule) {
                // 将在js 接口中获取到的数据 通过handler 传到主线程更新界面
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvData.setText(vaule);
                    }
                });
            }
        }), "sendData");     // "sendData" 对应 js中 调用的方法名
        // 加载本地html
        mWebview.loadUrl("file:///android_asset/WebViewDemo.html");

        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //设置WebChromeClient类
        mWebview.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                getSupportActionBar().setTitle(title);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edtTxtData.getText().toString();
                mWebview.loadUrl("javascript:if(window.send){window.send('" + str + "')}");
            }
        });
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }

}
