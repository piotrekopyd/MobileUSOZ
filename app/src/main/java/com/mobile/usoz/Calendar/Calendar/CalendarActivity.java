package com.mobile.usoz.Calendar.Calendar;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mobile.usoz.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class CalendarActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener
    {


    //Zmienne do layoutu
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;

    private static final String TAG = "CalendarActivity";
    private ArrayList<String> mMonths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        setupActivity();
    }

    private void setupActivity(){

        mAuth = FirebaseAuth.getInstance();

        //setupNavigation();

        // recycle view
        initDates();
        setupRecycleView();
    }


    // ------------- CALENDAR ----------------------------

    public void initDates(){
        Log.d(TAG, "initDates");
        mMonths.add("Styczeń");
        mMonths.add("Luty");
        mMonths.add("Marzec");
        mMonths.add("Kwiecień");
        mMonths.add("Maj");
        mMonths.add("Czerwiec");
        mMonths.add("Lipiec");
        mMonths.add("Sierpień");
        mMonths.add("Wrzesień");
        mMonths.add("Październik");
        mMonths.add("Listopad");
        mMonths.add("Grudzień");
    }

    private void setupRecycleView(){
        Log.d(TAG, "start setup recycle view");
        RecyclerView recyclerView = findViewById(R.id.calendar_recycle_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mMonths);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }








    // --------------- NAVIGATION ----------------------

//    public void setupNavigation(){
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.nav_calendar);
//    }
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        Intent intent = null;
//        switch (menuItem.getItemId()) {
//            case R.id.nav_profile:
//                intent = new Intent(CalendarActivity.this, UserProfileAcitivity.class);
//                break;
//            case R.id.nav_calendar:
//                intent = new Intent(CalendarActivity.this, CalendarActivity.class);
//                break;
//            case R.id.nav_forum:
//                intent = new Intent(CalendarActivity.this, ForumActivity.class);
//                break;
//            case R.id.nav_lecturers:
//                intent = new Intent(CalendarActivity.this, LecturersActivity.class);
//                break;
//            case R.id.nav_maps:
//                intent = new Intent(CalendarActivity.this, MapsActivity.class);
//                break;
//            case R.id.nav_notes:
//                intent = new Intent(CalendarActivity.this, NotesActivity.class);
//                break;
//            case R.id.nav_log_out:
//                logOut();
//                break;
//        }
//        if(intent!=null) {
//            startActivity(intent);
//        }
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    private void logOut() {
//        //FIREBASE LOG OUT
//        mAuth.signOut();
//
//        // FB LOG OUT
//        LoginManager.getInstance().logOut();
//        UpdateUI();
//    }
//
//    private void UpdateUI() {
//        Toast.makeText(CalendarActivity.this, "You're logged out", Toast.LENGTH_LONG).show();
//        Intent loginIntent = new Intent(CalendarActivity.this, LogInActivity.class);
//        startActivity(loginIntent);
//        finish();
//    }
}
