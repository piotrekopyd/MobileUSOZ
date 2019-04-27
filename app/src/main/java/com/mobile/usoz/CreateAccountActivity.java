package com.mobile.usoz;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.UserActivities.EditUserDataActivity;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_NAME = "name";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_UNIVERSITY = "university";
    private static final String KEY_DATEOFBIRTH = "date";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSIONS = "passions";

    private Button registerButton ;
    private TextView emailTextView, pswdTextView;
    private static final String TAG = "EmailPassword";
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        registerButton = (Button) findViewById(R.id.registerButton);
        emailTextView = (TextView) findViewById(R.id.createEmailTextView);
        pswdTextView = (TextView) findViewById(R.id.PswdTextView);
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
                            user = mAuth.getCurrentUser();
                            db = FirebaseFirestore.getInstance();
                            saveDefaultData();
                            updateUI();
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
    private void updateUI(){
        Intent mainIntent = new Intent(CreateAccountActivity.this, UserProfileAcitivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void saveDefaultData() {
        Map<String, Object> newUserData = new HashMap<>();
        newUserData.put(KEY_NAME, "Name");
        newUserData.put(KEY_LASTNAME, "Last name");
        newUserData.put(KEY_UNIVERSITY, "University");
        newUserData.put(KEY_EMAIL,"E-mail");
        newUserData.put(KEY_DATEOFBIRTH, "Birthday");
        newUserData.put(KEY_PASSIONS, "Describe your passions");


        db.collection("User data").document(user.getUid().toString()).set(newUserData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateAccountActivity.this, "Error while saving data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
