package com.example.webtest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*WebView theWeb = findViewById(R.id.webview2);
        theWeb.loadUrl("http://www.baidu.com");//加载url
        theWeb.requestFocus();*/
        init();
    }
    WebView webView;
    private void init() {
        webView = (WebView) findViewById(R.id.webview2);
        //webView加载web资源
        webView.loadUrl("file:///android_asset/plus/camera.html");
       
        //webView.loadUrl("http://192.168.0.15:8060/plus/controller.html");
        //webView.loadUrl("https://www.baidu.com/");
        //覆盖WebView默认通过第三方或者是系统打开网页的行为，使网页可以在WebView中打开
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制网页在WebView中打开，
                //为false的时候，调用系统浏览器或者第三方浏览器打开
                view.loadUrl(url);
                return false;
            }
            //webViewCilent是帮助WebView去处理一些页面控制和请求通知

            //public void onPageStarted(WebView view, String url, Bitmap favicon)
            //是处理页面开启时的操作
        });
        //启用支持JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                //newProgress 1-100之间的证书
                if(newProgress == 100){
                    //加载完毕,关闭ProgressDialog
                    //closeDialog();
                }else{
                    //正在加载，打开ProgressDialog
                    //openDialog(newProgress);
                }
            }
            Dialog dialog;
            private void closeDialog() {
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                    dialog = null;
                }
            }

            private void openDialog(int newProgress) {
                if(dialog == null){
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("正在加载");
                    //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    //dialog.setProgress(newProgress);
                    dialog.show();
                }
                else{
                    //dialog.setProgress(newProgress);
                }
            }
        });


    }

    //改写物理按键返回的逻辑
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack()){
                webView.goBack();//返回上一页面
                return true;
            }
            else{
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}