package net.asovel.myebike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.common.Defaults;
import net.asovel.myebike.main.MainActivity;
import net.asovel.myebike.main.MyEBike;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;
import net.asovel.myebike.utils.Constants;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    private void initUI() {
        Button facebookButton = (Button) findViewById(R.id.login_facebook_button);
        Button googleButton = (Button) findViewById(R.id.login_google_button);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginWithFacebookButtonClicked();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginWithGoogleButtonClicked();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void onLoginWithFacebookButtonClicked() {
        Map<String, String> facebookFieldsMapping = new HashMap<>();
        facebookFieldsMapping.put("name", "name");
        facebookFieldsMapping.put("gender", "gender");
        //facebookFieldsMapping.put("age", "birthday");
        facebookFieldsMapping.put("email", "email");

        List<String> facebookPermissions = new ArrayList<>();
        facebookPermissions.add("email");

        Backendless.UserService.loginWithFacebook(LoginActivity.this, null, facebookFieldsMapping, facebookPermissions,
                new DefaultCallback<BackendlessUser>(LoginActivity.this) {
                    @Override
                    public void handleResponse(BackendlessUser user) {
                        super.handleResponse(user);
                        onLogin(user);
                    }
                });
    }

    private void onLoginWithGoogleButtonClicked() {
        Map<String, String> googleFieldsMapping = new HashMap<>();
        googleFieldsMapping.put("name", "name");
        googleFieldsMapping.put("gender", "gender");
        googleFieldsMapping.put("email", "email");

        List<String> googlePermissions = new ArrayList<>();

        Backendless.UserService.loginWithGooglePlus(LoginActivity.this, null, googleFieldsMapping, googlePermissions,
                new DefaultCallback<BackendlessUser>(LoginActivity.this) {
                    @Override
                    public void handleResponse(BackendlessUser user) {
                        super.handleResponse(user);
                        onLogin(user);
                    }
                });
    }

    private void onLogin(BackendlessUser user) {
        Backendless.UserService.setCurrentUser(user);
        String email = user.getEmail();

        SharedPreferences prefs = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.commit();

        Bundle bundle = getIntent().getExtras();

        String caller = "";
        if (bundle != null)
            caller = bundle.getString(Constants.CALLER, "");

        Intent intent;

        if (caller.equals(MyEBike.NAME)) {
            intent = new Intent(getBaseContext(), EBikeListActivity.class);
            Bundle bundletransmitter = new Bundle();
            ArrayList<String> listClauses = bundle.getStringArrayList(EBikeListActivity.WHERECLAUSE);
            bundletransmitter.putStringArrayList(EBikeListActivity.WHERECLAUSE, listClauses);
            intent.putExtras(bundletransmitter);
            startActivity(intent);
            finish();

        } else if (caller.equals(MainActivity.NAME)){
            intent = new Intent(getBaseContext(), MainActivity.class);
            NavUtils.navigateUpTo(this, intent);

        } else {
            intent = new Intent(getBaseContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}