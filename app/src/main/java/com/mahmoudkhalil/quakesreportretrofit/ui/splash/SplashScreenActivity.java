package com.mahmoudkhalil.quakesreportretrofit.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mahmoudkhalil.quakesreportretrofit.R;
import com.mahmoudkhalil.quakesreportretrofit.ui.main.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent HomeIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(HomeIntent);
                finish();
            }
        },SPLASH_TIME);
    }
}