package net.asovel.myebike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends Activity
{
    public static final String EMAIL = "EMAIL";

    private Button facebookButton;
    private Button twitterButton;
    private Button googleButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        initUI();

        Backendless.UserService.isValidLogin(new DefaultCallback<Boolean>(this)
        {
            @Override
            public void handleResponse(Boolean isValidLogin)
            {
                if (isValidLogin && Backendless.UserService.CurrentUser() == null)
                {
                    String currentUserId = Backendless.UserService.loggedInUser();

                    if (!currentUserId.equals(""))
                    {
                        Backendless.UserService.findById(currentUserId, new DefaultCallback<BackendlessUser>(LoginActivity.this, "Logging in...")
                        {
                            @Override
                            public void handleResponse(BackendlessUser user)
                            {
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
        });
    }

    private void initUI()
    {
        facebookButton = (Button) findViewById(R.id.loginFacebookButton);
        twitterButton = (Button) findViewById(R.id.loginTwitterButton);
        googleButton = (Button) findViewById(R.id.loginGoogleButton);

        facebookButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onLoginWithFacebookButtonClicked();
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onLoginWithTwitterButtonClicked();
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

    }

    public void onLoginWithFacebookButtonClicked()
    {
        Map<String, String> facebookFieldsMapping = new HashMap<>();
        facebookFieldsMapping.put("name", "name");
        facebookFieldsMapping.put("gender", "gender");
        //facebookFieldsMapping.put("age", "birthday");
        facebookFieldsMapping.put("email", "email");

        List<String> facebookPermissions = new ArrayList<>();
        facebookPermissions.add("email");

        Backendless.UserService.loginWithFacebook(LoginActivity.this, null, facebookFieldsMapping, facebookPermissions,
                new DefaultCallback<BackendlessUser>(LoginActivity.this)
                {
                    @Override
                    public void handleResponse(BackendlessUser user)
                    {
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

    public void onLoginWithTwitterButtonClicked()
    {
        Map<String, String> twitterFieldsMapping = new HashMap<>();
        twitterFieldsMapping.put("name", "name");

        Backendless.UserService.loginWithTwitter(LoginActivity.this, twitterFieldsMapping,
                new DefaultCallback<BackendlessUser>(LoginActivity.this)
                {
                    @Override
                    public void handleResponse(BackendlessUser user)
                    {
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

    public void onLoginWithGoogleButtonClicked()
    {
        Map<String, String> googleFieldsMapping = new HashMap<>();
        googleFieldsMapping.put("name", "name");
        googleFieldsMapping.put("gender", "gender");
        googleFieldsMapping.put("email", "email");

        List<String> googlePermissions = new ArrayList<>();

        Backendless.UserService.loginWithGooglePlus(LoginActivity.this, null, googleFieldsMapping, googlePermissions,
                new DefaultCallback<BackendlessUser>(LoginActivity.this)
                {
                    @Override
                    public void handleResponse(BackendlessUser user)
                    {
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