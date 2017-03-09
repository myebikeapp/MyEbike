package net.asovel.myebike.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.asovel.myebike.R;
import net.asovel.myebike.utils.AnalyticsApplication;
import net.asovel.myebike.utils.Constants;

public class FragmentWeb extends Fragment
{
    private static final String TAG = FragmentWeb.class.getSimpleName();

    private Tracker tracker;

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

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();

        Bundle bundle = getArguments();
        String web = bundle.getString(Constants.URL) + Constants.UTM;
        WebView webView = (WebView) getView().findViewById(R.id.pagina_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(web);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}