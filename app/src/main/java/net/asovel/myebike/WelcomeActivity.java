package net.asovel.myebike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.asovel.myebike.main.MainActivity;
import net.asovel.myebike.utils.AnalyticsApplication;

public class WelcomeActivity extends Activity
{
    public static final String TAG = WelcomeActivity.class.getSimpleName();

    private Tracker tracker;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();

        SharedPreferences prefs = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");

        //tracker.set("&uid", email);

        if (!email.equals("")) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        initUI();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        tracker.setScreenName("Image~" + TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initUI()
    {
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button sinLoginButton = (Button) findViewById(R.id.button_comenzar);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        sinLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView textView = (TextView) findViewById(R.id.welcome_text);
        String text = getString(R.string.welcome_text);
        textView.setText(Html.fromHtml(text));
    }
}
