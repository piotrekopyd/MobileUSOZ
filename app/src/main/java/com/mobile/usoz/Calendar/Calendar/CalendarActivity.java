package com.mobile.usoz.Calendar.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.mobile.usoz.Calendar.Notes.AddNewNoteActivity;
import com.mobile.usoz.LecturersActivities.LecturersActivity;
import com.mobile.usoz.Maps.MapsActivity;
import com.mobile.usoz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.usoz.UserAccount.LogInActivity;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;


public class CalendarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
    {

        private CalendarModel model;
    //Zmienne do layoutu
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private Button saveNoteButton;
    private FirebaseAuth mAuth;

    private static final String TAG = "CalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        findViewById(R.id.included_exit_layout).setVisibility(View.INVISIBLE);

        setupActivity();
    }

    private void setupActivity() {

        saveNoteButton = findViewById(R.id.addNoteButton);
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, AddNewNoteActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();

        //setupNavigation();

        model = new CalendarModel();

        setupNavigation();

        // recycle view
        setupRecycleView();
    }

    // ------------- CALENDAR ----------------------------

    private void setupRecycleView(){
        Log.d(TAG, "start setup recycle view");
        RecyclerView recyclerView = findViewById(R.id.calendar_recycle_view);
        CalendarRecyclerViewAdapter adapter = new CalendarRecyclerViewAdapter(this, model.mMonths);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // ------------------ Navigation ---------------------------------------

    public void setupNavigation(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_calendar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        findViewById(R.id.calendar_relative_layout_1).setForeground(new ColorDrawable(Color.BLACK));
        findViewById(R.id.calendar_relative_layout_1).getForeground().setAlpha(180);

        findViewById(R.id.included_exit_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.included_exit_layout).setClickable(true);

        Button button = findViewById(R.id.exit_reject_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.calendar_relative_layout_1).setForeground(new ColorDrawable(Color.TRANSPARENT));

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
                intent = new Intent(CalendarActivity.this, UserProfileAcitivity.class);
                break;
            case R.id.nav_calendar:
                intent = new Intent(CalendarActivity.this, CalendarActivity.class);
                break;
            case R.id.nav_lecturers:
                intent = new Intent(CalendarActivity.this, LecturersActivity.class);
                break;
            case R.id.nav_maps:
                intent = new Intent(CalendarActivity.this, MapsActivity.class);
                break;
            case R.id.nav_log_out:
                logOut();
                break;
        }
        if(intent!=null) {
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        //FIREBASE LOG OUT
        mAuth.signOut();

        // FB LOG OUT
        LoginManager.getInstance().logOut();
        UpdateUI();
    }

    private void UpdateUI() {
        Toast.makeText(CalendarActivity.this, getResources().getString(R.string.you_re_logged_out), Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(CalendarActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
