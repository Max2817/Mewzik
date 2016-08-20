package com.majateam.mewzik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity {

    @BindView(R.id.login_button)
    LoginButton mLoginButton;
    @BindView(R.id.login_text)
    TextView mLoginTextView;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize Facebook SDK. Bust be called before setting the view
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mCallbackManager = CallbackManager.Factory.create();
        //mLoginButton.setReadPermissions(Arrays.asList("public_profile", "user_interests"));
        mLoginButton.setReadPermissions(Arrays.asList("user_status", "email", "user_likes"));
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                /*LoginManager.getInstance().logInWithReadPermissions(
                        LoginActivity.this,
                        Arrays.asList("email"));*/
                mLoginTextView.setText("Auth Token:" + loginResult.getAccessToken());
                Intent intent = new Intent(LoginActivity.this, MusicActivity.class);
                intent.putExtra(MusicActivity.USER_ID, loginResult.getAccessToken().getUserId());
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                mLoginTextView.setText("Login Cancelled");

            }

            @Override
            public void onError(FacebookException error) {
                mLoginTextView.setText("Login Error");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}

