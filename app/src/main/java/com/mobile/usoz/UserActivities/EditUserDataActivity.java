package com.mobile.usoz.UserActivities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.CreateAccountActivity;
import com.mobile.usoz.NotesActivity;
import com.mobile.usoz.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditUserDataActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String KEY_NAME = "name";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_UNIVERSITY = "university";
    private static final String KEY_DATEOFBIRTH = "date";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSIONS = "passions";

    private TextView nameTextView,lastNameTextView,emailTextView,birthdayTextView,universityTextView,passionsTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button saveButton,uploadProfileImageButton, uploadBackgroundImageButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Uri mImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_data);
        setupActivity();
    }
    private void setupActivity(){
        nameTextView = findViewById(R.id.editUserNameTextView);
        lastNameTextView = findViewById(R.id.editUserLastNameTextView);
        emailTextView = findViewById(R.id.editUserEmailTextView);
        birthdayTextView = findViewById(R.id.editUserBirthdayDateTextView);
        universityTextView = findViewById(R.id.editUserUniversityTextView);
        passionsTextView = findViewById(R.id.editUserPassionsTextView);
        saveButton = (Button) findViewById(R.id.saveUserDataButton);
        uploadBackgroundImageButton = findViewById(R.id.upload_background_image_button);
        uploadProfileImageButton = findViewById(R.id.upload_profile_image_button);
        saveButton.setOnClickListener(this);
        uploadProfileImageButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerButton:
                if(isCorrect()) {
                    saveData();
                    //TODO: DATA IS CORRECT
//                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(EditUserDataActivity.this, "Data is incorrect!",
                            Toast.LENGTH_SHORT).show();
                }
            case R.id.upload_profile_image_button:
                openFileChooser();
        }
    }

    public void saveData() {
        Map<String, Object> newUserData = new HashMap<>();
        newUserData.put(KEY_NAME, nameTextView.getText().toString());
        newUserData.put(KEY_LASTNAME, lastNameTextView.getText().toString());
        newUserData.put(KEY_UNIVERSITY, universityTextView.getText().toString());
        newUserData.put(KEY_EMAIL, emailTextView.getText().toString());
        newUserData.put(KEY_DATEOFBIRTH, birthdayTextView.getText().toString());
        newUserData.put(KEY_PASSIONS, passionsTextView.getText().toString());


        db.collection("User data").document(user.toString()).set(newUserData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditUserDataActivity.this, "Your new data has been saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditUserDataActivity.this, "Error while saving data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isCorrect(){
        if(nameTextView.getText().toString().equals(""))
            return false;
        if(lastNameTextView.getText().toString().equals(""))
            return false;
        if(universityTextView.getText().toString().equals(""))
            return false;
        //TODO: END THIS
        return true;
    }

    // MARK: - UPLOAD IMAGE
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && requestCode == RESULT_OK && data!= null && data.getData() != null){
            mImageUri = data.getData();

            //Picasso.with(this).load(mImageUri).into();
        }
    }
}
