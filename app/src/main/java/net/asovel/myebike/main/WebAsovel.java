package net.asovel.myebike.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.asovel.myebike.R;
import net.asovel.myebike.utils.Constants;

public class WebAsovel extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_pagina_web, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state)
    {
        super.onActivityCreated(state);

        Bundle bundle = getArguments();
        String web = bundle.getString(Constants.URL);
        WebView webView = (WebView) getView().findViewById(R.id.pagina_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(web);
    }
}