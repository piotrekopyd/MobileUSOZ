package com.mobile.usoz.UserActivities.EditUserDataActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobile.usoz.R;

public class EditDataMenu extends AppCompatActivity {

    private Button changeProfilePhotoButton, changeBackgroundPhotoButton,changePersonalDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data_menu);
        setupActivity();
    }

    private void setupActivity(){
        changeProfilePhotoButton = findViewById(R.id.clickToChangeProfilePhotoButton);
        changeBackgroundPhotoButton = findViewById(R.id.clickToChangeBackgroundPhotoButton);
        changePersonalDataButton = findViewById(R.id.clickToChangePersonalDataButton);

        changeProfilePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditActivity(ChangeUserProfilePhoto.class);
            }
        });
        changeBackgroundPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditActivity(ChangeUserBackgroundPhoto.class);
            }
        });
        changePersonalDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditActivity(EditUserDataActivity.class);
            }
        });
    }
    private void goToEditActivity(Class v){
        Intent intent = new Intent(EditDataMenu.this, v);
        startActivity(intent);
        finish();
    }
}
