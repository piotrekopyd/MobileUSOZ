package com.mobile.usoz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {

    Button logOutButton;
    TextView userTextView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        logOutButton = (Button) findViewById(R.id.logOutButton);
        userTextView = (TextView) findViewById(R.id.usernameTextView);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //FIREBASE LOG OUT
                mAuth.signOut();
                // FB LOG OUT
                LoginManager.getInstance().logOut();
                UpdateUI();
            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            UpdateUI();
        }else{
            userTextView.setText(mAuth.getCurrentUser().toString());
        }
    }

    private void UpdateUI() {
        Toast.makeText(MainActivity.this, "You're logged out", Toast.LENGTH_LONG).show();


        Intent loginIntent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
