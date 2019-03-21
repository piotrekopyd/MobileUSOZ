package com.mobile.usoz;

import android.app.ProgressDialog;
import android.inputmethodservice.Keyboard;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerButton ;
    private TextView emailTextView, pswdTextView;
    private static final String TAG = "EmailPassword";
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        registerButton = (Button) findViewById(R.id.registerButton);
        emailTextView = (TextView) findViewById(R.id.createEmailTextView);
        pswdTextView = (TextView) findViewById(R.id.createPswdTextView);
        progressBar = (ProgressBar) findViewById(R.id.circleProgressBar);
        registerButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }


    private void createAccount(String email, String password){

        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(CreateAccountActivity.this, "Account has been created successfully!.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //TODO: GO TO MAIN WINDOW
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //TODO: DISPLAY ERROR MESSAGE TO USER
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailTextView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailTextView.setError("Required.");
            valid = false;
        } else {
            emailTextView.setError(null);
        }

        String password = pswdTextView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pswdTextView.setError("Required.");
            valid = false;
        } else {
            pswdTextView.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                if(!emailTextView.getText().toString().equals("")  && !pswdTextView.getText().toString().equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    createAccount(emailTextView.getText().toString(), pswdTextView.getText().toString());
                }else{
                    Toast.makeText(CreateAccountActivity.this, "E-mail or password field is empty!",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }


}
