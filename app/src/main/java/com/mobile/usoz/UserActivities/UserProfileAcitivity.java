package com.mobile.usoz.UserActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.CalendarActivity;
import com.mobile.usoz.ForumActivity;
import com.mobile.usoz.LecturersActivity;
import com.mobile.usoz.LogInActivity;
import com.mobile.usoz.MapsActivity;
import com.mobile.usoz.NotesActivity;
import com.mobile.usoz.R;

public class UserProfileAcitivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;

    //Layouts
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);

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
    private void setupNavigation(){

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
    private void updateLogOutUI() {
        Toast.makeText(UserProfileAcitivity.this, "You're logged out", Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(UserProfileAcitivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void logOut() {
        //FIREBASE LOG OUT
        mAuth.signOut();

        // FB LOG OUT
        LoginManager.getInstance().logOut();
        updateLogOutUI();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                intent = new Intent(UserProfileAcitivity.this, UserProfileAcitivity.class);
                break;
            case R.id.nav_calendar:
                intent = new Intent(UserProfileAcitivity.this, CalendarActivity.class);
                break;
            case R.id.nav_forum:
                intent = new Intent(UserProfileAcitivity.this, ForumActivity.class);
                break;
            case R.id.nav_lecturers:
                intent = new Intent(UserProfileAcitivity.this, LecturersActivity.class);
                break;
            case R.id.nav_maps:
                intent = new Intent(UserProfileAcitivity.this, MapsActivity.class);
                break;
            case R.id.nav_notes:
                intent = new Intent(UserProfileAcitivity.this, NotesActivity.class);
                break;
            case R.id.nav_log_out:
                logOut();
                break;
             //TODO: ADD  USER PROFILE TO NAVIGATION
        }
        if(intent!=null) {
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

