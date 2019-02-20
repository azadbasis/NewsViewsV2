package com.newsviewsv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.newsviewsv2.utils.App;
import com.newsviewsv2.utils.Operation;

import java.util.Arrays;

public class SigninActivity extends AppCompatActivity {

    public static final String USER_ID_KEY = "user_id_key";

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String EMAIL = "email";
    private String userId;
    private ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        profileImg=(ImageView)findViewById(R.id.profileImg);


        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        SharedPreferences prefs =
                getSharedPreferences(MainActivity.MY_GLOBAL_PREFS, MODE_PRIVATE);
        String userIdSp = prefs.getString(USER_ID_KEY, "");
        if (!TextUtils.isEmpty(userIdSp)) {

            Bitmap bitmap= Operation.getFacebookProfilePicture(userIdSp);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            profileImg.setImageDrawable(drawable);
        }
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        userId=loginResult.getAccessToken().getUserId();
                        App.userId=userId;


                        if (!TextUtils.isEmpty(userId)) {

                            Bitmap bitmap= Operation.getFacebookProfilePicture(userId);
                            Drawable d = new BitmapDrawable(getResources(), bitmap);
                            profileImg.setImageDrawable(d);
                        }
                        getIntent().putExtra(USER_ID_KEY, userId);
                        setResult(RESULT_OK, getIntent());
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }




    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }



}
