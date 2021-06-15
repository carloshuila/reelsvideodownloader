package com.app.reelsdownloader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.reelsdownloader.R;
import com.app.reelsdownloader.util.SharePrefs;

public class LoginActivity extends AppCompatActivity {
    LoginActivity activity;

    public String cookies;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        this.activity = this;
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        webView = findViewById(R.id.webView);

        LoadPage();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage();
            }
        });
    }

    public void LoadPage() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new MyBrowser());
        CookieSyncManager.createInstance(this.activity);
        CookieManager.getInstance().removeAllCookie();
        webView.loadUrl("https://www.instagram.com/accounts/login/");
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                if (i == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
    }

    public String getCookie(String str, String str2) {
        String cookie = CookieManager.getInstance().getCookie(str);
        if (cookie != null && !cookie.isEmpty()) {
            for (String str3 : cookie.split(";")) {
                if (str3.contains(str2)) {
                    return str3.split("=")[1];
                }
            }
        }
        return null;
    }

    private class MyBrowser extends WebViewClient {
        private MyBrowser() {
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }
        @Override
        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }
        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            LoginActivity.this.cookies = CookieManager.getInstance().getCookie(str);
            try {
                String cookie = getCookie(str, "sessionid");
                String cookie2 = getCookie(str, "csrftoken");
                String cookie3 = getCookie(str, "ds_user_id");
                if (cookie != null && cookie2 != null && cookie3 != null) {
                    SharePrefs.getInstance(activity).putString(SharePrefs.COOKIES, LoginActivity.this.cookies);
                    SharePrefs.getInstance(activity).putString(SharePrefs.CSRF, cookie2);
                    SharePrefs.getInstance(activity).putString(SharePrefs.SESSIONID, cookie);
                    SharePrefs.getInstance(activity).putString(SharePrefs.USERID, cookie3);
                    SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISINSTALOGIN, true);
                    webView.destroy();
                    Intent intent = new Intent();
                    intent.putExtra("result", "result");
                    LoginActivity.this.setResult(-1, intent);
                    LoginActivity.this.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    }
}
