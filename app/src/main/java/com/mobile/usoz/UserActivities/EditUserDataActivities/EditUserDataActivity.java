package com.mobile.usoz.UserActivities.EditUserDataActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import java.util.HashMap;
import java.util.Map;

public class EditUserDataActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String KEY_NAME = "name";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_UNIVERSITY = "university";
    private static final String KEY_DATEOFBIRTH = "date";
    private static final String KEY_PASSIONS = "passions";

    private TextView nameTextView,lastNameTextView,emailTextView,birthdayTextView,universityTextView,passionsTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);
        setupActivity();
    }
    private void setupActivity(){
        nameTextView = findViewById(R.id.editUserNameTextView);
        lastNameTextView = findViewById(R.id.editUserLastNameTextView);
        birthdayTextView = findViewById(R.id.editUserBirthdayDateTextView);
        universityTextView = findViewById(R.id.editUserUniversityTextView);
        passionsTextView = findViewById(R.id.editUserPassionsTextView);
        saveButton = findViewById(R.id.saveUserDataButton);
        saveButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        retreiveDataFromPreviousActivity();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveUserDataButton:
                /*if(isCorrect()) {*/
                    saveData();
                    goToEditUserProfileActivity();
                /*
                }else{
                    Toast.makeText(EditUserDataActivity.this, "Błędne dane!",
                            Toast.LENGTH_SHORT).show();
                }*/

        }
    }

    public void retreiveDataFromPreviousActivity(){
        Intent intent = getIntent();
        nameTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_NAME));
        lastNameTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_LASTNAME));
        birthdayTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_BIRTHDAY));
        universityTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_UNIVERSITY));
        passionsTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_PASSIONS));
    }

    public void saveData() {
        /*if(isCorrect()) {*/
            Map<String, Object> newUserData = new HashMap<>();
            newUserData.put(KEY_NAME, nameTextView.getText().toString());
            newUserData.put(KEY_LASTNAME, lastNameTextView.getText().toString());
            newUserData.put(KEY_UNIVERSITY, universityTextView.getText().toString());
            newUserData.put(KEY_DATEOFBIRTH, birthdayTextView.getText().toString());
            newUserData.put(KEY_PASSIONS, passionsTextView.getText().toString());

            db.collection("User data").document(user.getUid().toString()).set(newUserData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditUserDataActivity.this, "Dane Twojego profilu zostały zapisane", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditUserDataActivity.this, "Błąd podczas zapisywania informacji o twoim profilu do bazy danych", Toast.LENGTH_SHORT).show();
                        }
                    });
        /*
        } else {
            Toast.makeText(EditUserDataActivity.this, "Błędne dane!", Toast.LENGTH_SHORT).show();
        }*/
    }

    //Useless (no one of fields are necessary)

    /*private boolean isCorrect(){
        if(nameTextView.getText().toString().equals(""))
            return false;
        if(lastNameTextView.getText().toString().equals(""))
            return false;
        if(universityTextView.getText().toString().equals(""))
            return false;
        //TODO: END THIS
        return true;
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: DELETE?
    }

    private void goToEditUserProfileActivity(){
        Intent intent = new Intent(EditUserDataActivity.this, UserProfileAcitivity.class);
        startActivity(intent);
        finish();
    }
}