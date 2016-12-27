package net.asovel.myebike.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.asovel.myebike.R;

public class WebAsovel extends Fragment
{
    public static final String PAGINA_WEB = "PAGINA_WEB";

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
        String web = bundle.getString(PAGINA_WEB);
        WebView webView = (WebView) getView().findViewById(R.id.pagina_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(web);
    }
}
//"https://docs.google.com/forms/d/e/1FAIpQLSfeXj1fan4L3Uc2O88EoLkUuwIyW2V4jXPDl1sNuX1a01q27A/viewform?c=0&w=1"