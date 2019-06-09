package com.mobile.usoz.UserActivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.UserDataDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.UserProfileDatabaseManager;
import com.mobile.usoz.LecturersActivities.LecturersActivity;
import com.mobile.usoz.UserAccount.LogInActivity;
import com.mobile.usoz.Maps.MapsActivity;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.EditUserDataActivities.EditUserDataActivity;

public class UserProfileAcitivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, UserDataDatabaseKeyValues {
    private FirebaseAuth mAuth;

    //Layouts
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;



    private ImageView profilePicture;
    private ImageView backgroundImage;
    private TextView birthdayTextView;
    private TextView emailTextView;
    private TextView passionsTextView;
    private TextView nameTextView;
    private TextView universityTextView;
    private ImageView editUserDataIV;

    private UserProfileDatabaseManager databaseManager;

    //Extras
    private String name, lastName;
    static final public String USER_PROFILE_EXTRA_NAME = "com.mobile.usoz.UserActivities.UserProfileActivity.USER_PROFILE_EXTRA_NAME";
    static final public String USER_PROFILE_EXTRA_LASTNAME = "com.mobile.usoz.UserActivities.UserProfileActivity.USER_PROFILE_EXTRA_LASTNAME";
    static final public String USER_PROFILE_EXTRA_UNIVERSITY = "com.mobile.usoz.UserActivities.UserProfileActivity.USER_PROFILE_EXTRA_UNIVERSITY";
    static final public String USER_PROFILE_EXTRA_BIRTHDAY = "com.mobile.usoz.UserActivities.UserProfileActivity.USER_PROFILE_EXTRA_BIRTHDAY";
    static final public String USER_PROFILE_EXTRA_PASSIONS = "com.mobile.usoz.UserActivities.UserProfileActivity.USER_PROFILE_EXTRA_PASSIONS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        findViewById(R.id.included_exit_layout).setVisibility(View.INVISIBLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                // Pass data to next Activity
                newIntent.putExtra(USER_PROFILE_EXTRA_NAME, name);
                newIntent.putExtra(USER_PROFILE_EXTRA_LASTNAME, lastName);
                newIntent.putExtra(USER_PROFILE_EXTRA_UNIVERSITY, universityTextView.getText().toString());
                newIntent.putExtra(USER_PROFILE_EXTRA_BIRTHDAY, birthdayTextView.getText().toString());
                newIntent.putExtra(USER_PROFILE_EXTRA_PASSIONS, passionsTextView.getText().toString());

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
        editUserDataIV = findViewById(R.id.editProfile_image);
        databaseManager = new UserProfileDatabaseManager();
        mAuth = FirebaseAuth.getInstance();

        updateUI();

    }

    // Fill text views with retrieved data
    private void updateUI(){
        databaseManager.retrieveUserData((String name, String lastName, String university, String birthday, String email, String passions) -> {
                nameTextView.setText(name + " " + lastName);
                universityTextView.setText(university);
                birthdayTextView.setText(birthday);
                emailTextView.setText(email);
                passionsTextView.setText(passions);
                this.name = name;
                this.lastName = lastName;
            }, (boolean isSuccess) -> {
                Toast.makeText(UserProfileAcitivity.this,"Nie udało się pobrać danych twojego profilu!", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateLogOutUI() {
        Toast.makeText(UserProfileAcitivity.this, "Wylogowano", Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        findViewById(R.id.user_profile_relative_layout_1).setForeground(new ColorDrawable(Color.BLACK));
        findViewById(R.id.user_profile_relative_layout_1).getForeground().setAlpha(180);

        findViewById(R.id.included_exit_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.included_exit_layout).setClickable(true);

        Button button = findViewById(R.id.exit_reject_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.user_profile_relative_layout_1).setForeground(new ColorDrawable(Color.TRANSPARENT));

                findViewById(R.id.included_exit_layout).setVisibility(View.INVISIBLE);
                findViewById(R.id.included_exit_layout).setClickable(false);
            }
        });

        button = findViewById(R.id.exit_confirm_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeApplication();
            }
        });
    }

    private void closeApplication() {
        finishAffinity();
        System.exit(0);
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
            case R.id.nav_lecturers:
                intent = new Intent(UserProfileAcitivity.this, LecturersActivity.class);
                break;
            case R.id.nav_maps:
                intent = new Intent(UserProfileAcitivity.this, MapsActivity.class);
                break;
            case R.id.nav_log_out:
                logOut();
                break;
        }
        if(intent != null) {
            startActivity(intent);
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


