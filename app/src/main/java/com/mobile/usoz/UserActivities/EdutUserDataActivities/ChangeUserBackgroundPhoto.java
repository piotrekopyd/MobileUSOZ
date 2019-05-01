package com.mobile.usoz.UserActivities.EdutUserDataActivities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.usoz.R;
import com.squareup.picasso.Picasso;

public class ChangeUserBackgroundPhoto extends AppCompatActivity  {
    private Button choosePhotoButton;
    private ImageView backgroundPhotoImageView;
    private TextView choosenImageNameTextView;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_background_photo);
        setupActivity();
    }
    private void setupActivity(){
        choosePhotoButton = findViewById(R.id.changeUserBackgroundPhotoChooseButton);
        backgroundPhotoImageView = findViewById(R.id.changeUserBackgroundPhotoImageView);
        choosenImageNameTextView = findViewById(R.id.choosenBackgroundImageNameTextView);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorageRef= FirebaseStorage.getInstance().getReference("backgroundPicture");


        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(backgroundPhotoImageView);
        }
    }

    private void uploadFile(final String name){
        if(mImageUri != null){
            StorageReference fileReference = mStorageRef.child(name + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ChangeUserBackgroundPhoto.this, "Upload succesful", Toast.LENGTH_LONG).show();
                            //Upload upload = new Upload(name, taskSnapshot.getUploadSessionUri().toString());
                            //choosenImageNameTextView.setText(taskSnapshot.getUploadSessionUri().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangeUserBackgroundPhoto.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void goToEditUserDataMenuActivity(){
        Intent intent = new Intent(ChangeUserBackgroundPhoto.this, EditDataMenu.class);
        startActivity(intent);
        finish();
    }
}
