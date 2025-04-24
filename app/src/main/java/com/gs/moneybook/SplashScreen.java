package com.gs.moneybook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreen extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "user_session";
    private SharedPreferences sharedPreferences;
    public static final String KEY_LOGIN_SUCCESS = "loginSuccess";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                boolean loginSuccess = sharedPreferences.getBoolean(KEY_LOGIN_SUCCESS,false);
                if(loginSuccess){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
                else {


                    startActivity(new Intent(SplashScreen.this, WellComeScreen.class));
                }

                finish();

            }
        },2000);
    }
}