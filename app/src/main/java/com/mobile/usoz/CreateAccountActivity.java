package com.mobile.usoz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerButton ;
    private TextView emailTextView, pswdTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        registerButton = (Button) findViewById(R.id.registerButton);
        emailTextView = (TextView) findViewById(R.id.createEmailTextView);
        pswdTextView = (TextView) findViewById(R.id.createPswdTextView);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                //TODO: Create new account in firebase
        }
    }
    private void createUserWithEmailAndPswd(){
        createUserWithEmailAndPswd();
    }
}
