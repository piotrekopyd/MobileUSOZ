package com.mobile.usoz.SplashScreen;

import android.content.Intent;
import android.os.Bundle;

import com.mobile.usoz.R;
import com.mobile.usoz.UserAccount.LogInActivity;
import android.support.v7.app.AppCompatActivity;

public class SplashActivty extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(getApplicationContext(),
                LogInActivity.class);
        startActivity(intent);
        finish();
    }
}