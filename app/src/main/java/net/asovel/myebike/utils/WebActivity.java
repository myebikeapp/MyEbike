package net.asovel.myebike.utils;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import net.asovel.myebike.R;

public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pagina_web);

        Bundle bundle = getIntent().getExtras();
        String web = bundle.getString(Constants.URL) + "?utm_source=MyeBike&utm_campaign=Asovel&utm_medium=App";
        WebView webView = (WebView) findViewById(R.id.pagina_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(web);
    }
}
