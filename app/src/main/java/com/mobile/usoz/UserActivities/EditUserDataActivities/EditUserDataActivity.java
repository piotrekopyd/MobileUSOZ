package com.mobile.usoz.UserActivities.EditUserDataActivities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.usoz.CollectiveMethods.CollectiveMethods;
import com.mobile.usoz.DatabaseManager.UserProfileDatabaseManager;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

public class EditUserDataActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String KEY_NAME = "name";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_UNIVERSITY = "university";
    private static final String KEY_DATEOFBIRTH = "date";
    private static final String KEY_PASSIONS = "passions";

    private TextView nameTextView,lastNameTextView,birthdayTextView,universityTextView,passionsTextView;

    private UserProfileDatabaseManager dataDatabaseManager;
    private Button saveButton;


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
        dataDatabaseManager = new UserProfileDatabaseManager();

        retrieveDataFromPreviousActivity();

    }

    /* When we click on save button database manager function 'save data' is triggered */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveUserDataButton:
                    if(CollectiveMethods.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        dataDatabaseManager.saveData(nameTextView.getText().toString(), lastNameTextView.getText().toString(), universityTextView.getText().toString(),
                                birthdayTextView.getText().toString(), passionsTextView.getText().toString(), (boolean isSuccess) -> {
                                    if(!isSuccess){
                                        Toast.makeText(EditUserDataActivity.this, "Błąd podczas zapisywania informacji o twoim profilu do bazy danych", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditUserDataActivity.this, "Dane Twojego profilu zostały zapisane", Toast.LENGTH_SHORT).show();
                                        goToEditUserProfileActivity();
                                    }
                                });
                    } else {
                        Toast.makeText(EditUserDataActivity.this, "Błąd podczas zapisywania informacji o twoim profilu do bazy danych", Toast.LENGTH_SHORT).show();
                    }
        }
    }


    public void retrieveDataFromPreviousActivity(){
        Intent intent = getIntent();
        nameTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_NAME));
        lastNameTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_LASTNAME));
        birthdayTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_BIRTHDAY));
        universityTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_UNIVERSITY));
        passionsTextView.setText(intent.getStringExtra(UserProfileAcitivity.USER_PROFILE_EXTRA_PASSIONS));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void goToEditUserProfileActivity(){
        Intent intent = new Intent(EditUserDataActivity.this, UserProfileAcitivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(EditUserDataActivity.this, UserProfileAcitivity.class);
        startActivity(i);
    }
}