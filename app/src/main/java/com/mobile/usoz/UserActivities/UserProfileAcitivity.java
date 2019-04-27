package com.mobile.usoz.UserActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.R;

public class UserProfileAcitivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private static final String KEY_NAME = "name";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_UNIVERSITY = "university";
    private static final String KEY_DATEOFBIRTH = "date";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSIONS = "passions";

    private ImageView profilePicture;
    private ImageView backgroundImage;
    private TextView birthdayTextView;
    private TextView emailTextView;
    private TextView passionsTextView;
    private TextView nameTextView;
    private TextView universityTextView;
    private ImageView editUserDataIV;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setupActivity();

        editUserDataIV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newIntent = new Intent(UserProfileAcitivity.this, EditUserDataActivity.class);
                startActivity(newIntent);
                finish();
            }
        });

    }

    private void setupActivity(){
        profilePicture = findViewById(R.id.user_profile_image);
        backgroundImage = findViewById(R.id.background_user_image);
        birthdayTextView = findViewById(R.id.userBirthdayTextView);
        emailTextView = findViewById(R.id.userEmailTextView);
        passionsTextView = findViewById(R.id.userPassionsTextView);
        nameTextView = findViewById(R.id.userName_label);
        universityTextView = findViewById(R.id.university_label);
        editUserDataIV = (ImageView) findViewById(R.id.editProfile_image);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        updateUI();

    }
    private void updateUI(){
        retrieveUserData();
    }
    private void retrieveUserData(){
        //TODO: retrieve photos
        db.collection("User data").document(user.getUid().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                        String name = documentSnapshot.getString(KEY_NAME) + " " + documentSnapshot.getString(KEY_LASTNAME);
                        nameTextView.setText(name);
                        universityTextView.setText(documentSnapshot.getString(KEY_UNIVERSITY));
                        birthdayTextView.setText(documentSnapshot.getString(KEY_DATEOFBIRTH));
                        emailTextView.setText(user.getEmail().toString());
                        passionsTextView.setText(documentSnapshot.getString(KEY_PASSIONS));
                        }else{
                            Toast.makeText(UserProfileAcitivity.this,"Document does not exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfileAcitivity.this,"ERROR!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
