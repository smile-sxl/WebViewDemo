package com.sxl.webviewdemo;

import android.webkit.JavascriptInterface;

public class JStoAndroid {

    private JSInterface jsInterface;

    public JStoAndroid(JSInterface jsInterface){
        this.jsInterface=jsInterface;
    }

    /**
     * 定义JS需要调用的方法
     * 被JS调用的方法必须加入@JavascriptInterface注解
     */
    @JavascriptInterface
    public void setValue(String vaule) {
        jsInterface.setVaule(vaule);
    }
}