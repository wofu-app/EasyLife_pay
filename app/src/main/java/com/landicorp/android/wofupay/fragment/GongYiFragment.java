package com.landicorp.android.wofupay.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.landicorp.android.wofupay.R;
import com.landicorp.android.wofupay.utils.JLog;
import com.landicorp.android.wofupay.widget.WaveBgProgress;
import com.yanzhenjie.fragment.NoFragment;

/**
 * Created by Administrator on 2017/3/28.
 */

public class GongYiFragment extends NoFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private WebView wv;
    private WaveBgProgress p;
    public GongYiFragment() {
    }

    public static GongYiFragment newInstance(String param1, String param2) {
        GongYiFragment fragment = new GongYiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_gongyi, null);
        wv = (WebView)mInflate. findViewById(R.id.wb_piaowu);
        p = (WaveBgProgress) mInflate.findViewById(R.id.pr_piaowu);
        p.setMax(100);
        initData();
        return mInflate;
    }

    private void initData() {
        WebSettings mWebSettings = wv.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("GBK");
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

            };

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                p.setVisibility(View.VISIBLE);
            }
        });

        // 设置网页加载进度
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                JLog.i(newProgress + "");
                p.setProgress(newProgress);
                if (newProgress == 100) {
                    p.setVisibility(View.GONE);
                }
            }
        });
        if (Build.VERSION.SDK_INT > 15) {
            wv.loadUrl("http://gongyi.qq.com/");
        } else {
            wv.loadUrl("http://gongyi.baidu.com/");
        }

    }

    @Override
    public void onDestroy() {
        wv.stopLoading();
        super.onDestroy();
    }
}

