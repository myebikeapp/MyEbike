package net.asovel.myebike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.asovel.myebike.backendless.common.Defaults;
import net.asovel.myebike.main.FragmentListMarca;
import net.asovel.myebike.main.MainActivity;
import net.asovel.myebike.main.FragmentAsistente;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;
import net.asovel.myebike.utils.AnalyticsApplication;
import net.asovel.myebike.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends Activity
{
    public static final String TAG = LoginActivity.class.getSimpleName();

    private Tracker tracker;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        initUI();

       /* Backendless.UserService.isValidLogin(new DefaultCallback<Boolean>(this) {
            @Override
            public void handleResponse(Boolean isValidLogin) {
                if (isValidLogin && Backendless.UserService.CurrentUser() == null) {
                    String currentUserId = Backendless.UserService.loggedInUser();

                    if (!currentUserId.equals("")) {
                        Backendless.UserService.findById(currentUserId, new DefaultCallback<BackendlessUser>(LoginActivity.this, "Logging in...") {
                            @Override
                            public void handleResponse(BackendlessUser user) {
                                super.handleResponse(user);
                                Backendless.UserService.setCurrentUser(user);

                                Intent intent = new Intent(getBaseContext(), MainActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putString(EMAIL, user.getEmail());
                                intent.putExtras(bundle);

                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
                super.handleResponse(isValidLogin);
            }
        });*/
    }

    @Override
    public void onResume()
    {
        super.onResume();
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initUI()
    {
        Button facebookButton = (Button) findViewById(R.id.login_facebook_button);
        Button googleButton = (Button) findViewById(R.id.login_google_button);

        facebookButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onLoginWithFacebookButtonClicked();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onLoginWithGoogleButtonClicked();
            }
        });


        TextView textView = (TextView) findViewById(R.id.login_text_links);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String links = getString(R.string.login_links);
        textView.setText(Html.fromHtml(links));
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    private void onLoginWithFacebookButtonClicked()
    {
        Map<String, String> facebookFieldsMapping = new HashMap<>();
        facebookFieldsMapping.put("name", "name");
        facebookFieldsMapping.put("gender", "gender");
        //facebookFieldsMapping.put("age", "birthday");
        facebookFieldsMapping.put("email", "email");

        List<String> facebookPermissions = new ArrayList<>();
        facebookPermissions.add("email");

        Backendless.UserService.loginWithFacebook(LoginActivity.this, null, facebookFieldsMapping, facebookPermissions,
                new AsyncCallback<BackendlessUser>()
                {
                    @Override
                    public void handleResponse(BackendlessUser user)
                    {
                        onLogin(user);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        Log.d(TAG, fault.getMessage());
                        Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void onLoginWithGoogleButtonClicked()
    {
        Map<String, String> googleFieldsMapping = new HashMap<>();
        googleFieldsMapping.put("name", "name");
        googleFieldsMapping.put("gender", "gender");
        googleFieldsMapping.put("email", "email");

        List<String> googlePermissions = new ArrayList<>();

        Backendless.UserService.loginWithGooglePlus(LoginActivity.this, null, googleFieldsMapping, googlePermissions,
                new AsyncCallback<BackendlessUser>()
                {
                    @Override
                    public void handleResponse(BackendlessUser user)
                    {
                        onLogin(user);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        Log.d(TAG, fault.getMessage());
                        Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void onLogin(BackendlessUser user)
    {
        Backendless.UserService.setCurrentUser(user);
        String email = user.getEmail();

        tracker.set("&uid", user.getUserId());

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Usuario")
                .setAction("Sesión iniciada")
                .setLabel(email)
                .build());

        SharedPreferences prefs = getSharedPreferences(Constants.LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.EMAIL, email);
        editor.putString(Constants.USER_ID, user.getUserId());
        editor.commit();

        Bundle receiver = getIntent().getExtras();

        String caller = "";
        if (receiver != null)
            caller = receiver.getString(Constants.CALLER, "");

        Intent intent;

        if (caller.equals(FragmentAsistente.TAG) || caller.equals(FragmentListMarca.TAG)) {

            intent = new Intent(getBaseContext(), EBikeListActivity.class);
            Bundle sender = new Bundle();

            ArrayList<String> listClauses = receiver.getStringArrayList(Constants.WHERECLAUSE);
            sender.putStringArrayList(Constants.WHERECLAUSE, listClauses);

            sender.putString(Constants.CALLER, caller);

            intent.putExtras(sender);
            startActivity(intent);
            finish();

        } else if (caller.equals(MainActivity.TAG)) {
            intent = new Intent(getBaseContext(), MainActivity.class);
            NavUtils.navigateUpTo(this, intent);

        } else {
            intent = new Intent(getBaseContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}