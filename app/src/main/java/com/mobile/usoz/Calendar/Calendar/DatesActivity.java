package com.mobile.usoz.Calendar.Calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Calendar.Notes.AddNewNoteActivity;
import com.mobile.usoz.DatabaseManager.DatesDatabaseManager;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.Protocols.DatesDatabaseManagerInterface;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class DatesActivity extends AppCompatActivity implements NotesDatabaseKeyValues {

    private Toolbar toolbar;
    private DatesDatabaseManager databaseManager = new DatesDatabaseManager();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db ;

    private DatesModel model;
    private Button addNoteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);
        setupActivity();
    }

    private void setupActivity(){
        model = new DatesModel();
        setupView();
        getDataFromCalendarActivity();
        retrieveData();
        setupToolbar();

    }

    private void setupView(){
        addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatesActivity.this, AddNewNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromCalendarActivity() {
        Intent intent = getIntent();
        model.month = intent.getStringExtra(CalendarRecyclerViewAdapter.CALENDAR_EXTRA_TEXT);
    }

    // ------------------ Get data from firebase ----------------------------------

    private void retrieveData(){
        databaseManager.retrieveDatesFromFirebase(model.month, new DatesDatabaseManagerInterface() {
            @Override
            public void prepareArray(ArrayList<String> arrayList) {
                if(arrayList != null)
                {
                    model.mDates = arrayList;
                }
                setupRecyclerView();
            }
        });

        databaseManager.retrieveEventsFromFirebase(model.month, new DatesDatabaseManagerInterface() {
            @Override
            public void prepareArray(ArrayList<String> arrayList) {
                if(arrayList != null)
                {
                    model.mEvents = arrayList;
                }
                setupRecyclerView();
            }
        });
    }


    // ---------------------- Recycler View -----------------------------------

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.dates_recycle_view);
        CalendarRecyclerViewAdapter adapter = new CalendarRecyclerViewAdapter(DatesActivity.this, model.mDates, model.mEvents, model.month);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DatesActivity.this));
    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(CalendarRecyclerViewAdapter.formatNumberToMonth(Integer.valueOf(model.month)));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
