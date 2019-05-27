package com.mobile.usoz.Calendar.Notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mobile.usoz.Calendar.Calendar.CalendarRecyclerViewAdapter;
import com.mobile.usoz.DatabaseManager.DisplayNotesDatabaseManager;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.Protocols.DisplayNotesDatabaseManagerInterface;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class DisplayNotesActivity extends AppCompatActivity implements NotesDatabaseKeyValues {

    private DisplayNotesModel model;
    private DisplayNotesDatabaseManager databaseManager;
    private Button mAddNoteButton;
    private Toolbar toolbar;


    static final public String NOTES_DAYS_EXTRA_TEXT = "com.mobile.usoz.Calendar.DisplayNotes.EXTRA_TEXT_DAYS";
    static final public String NOTES_MONTHS_EXTRA_TEXT = "com.mobile.usoz.Calendar.DisplayNotes.EXTRA_TEXT_MONTHS";

    RecyclerView nRecyclerView;
    RecyclerView eRecyclerView;
    NotesRecyclerViewAdapter nAdapter;
    EventsRecyclerViewAdapter eAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_notes);
        setupActivity();
        //retreiveDataFromPreviousActivity();
        fetchNotesFromFirebase();

    }

    private void setupActivity(){
        model = new DisplayNotesModel();
        retreiveDataFromPreviousActivity();
        databaseManager = new DisplayNotesDatabaseManager();
        setupView();
    }

    private void setupView(){
        mAddNoteButton = findViewById(R.id.addNoteButton);
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayNotesActivity.this, AddNewNoteActivity.class);
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(CalendarRecyclerViewAdapter.formatNumberToMonth(Integer.valueOf(model.month)) + ", " +
                CalendarRecyclerViewAdapter.formatDateToDayOfWeek(model.month, model.day) + " " + model.day);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // ------------------------- Notes ---------------------------------



    private void retreiveDataFromPreviousActivity(){
        Intent intent = getIntent();
        model.month = intent.getStringExtra(CalendarRecyclerViewAdapter.DATES_MONTH_EXTRA_TEXT);
        model.day = intent.getStringExtra(CalendarRecyclerViewAdapter.DATES_DAY_EXTRA_TEXT);
    }

    // ----------------------------- Fetch data from database ---------------------
    private void fetchNotesFromFirebase(){
        databaseManager.fetchNotesFromFirebase(model.day, model.month, new DisplayNotesDatabaseManagerInterface() {
            @Override
            public void prepareArray(ArrayList<String> arrayList) {
                if(arrayList != null){
                    model.mNotes = arrayList;
                }
                setupNotesRecyclerView();
            }
        });
        databaseManager.fetchEventsFromFirebase(model.day, model.month, new DisplayNotesDatabaseManagerInterface() {
            @Override
            public void prepareArray(ArrayList<String> arrayList) {
                if(arrayList != null) {
                    model.mEvents = arrayList;
                }
                setupEventRecyclerView();
            }
        });
    }

    // ---------------------------- Recycler View -----------------------

    private void setupNotesRecyclerView(){
        nRecyclerView = findViewById(R.id.notes_recycler_view);
        nAdapter = new NotesRecyclerViewAdapter(this, model.mNotes, model.day, model.month);
        nRecyclerView.setAdapter(nAdapter);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupEventRecyclerView(){
        eRecyclerView = findViewById(R.id.events_recycler_view);
        eAdapter = new EventsRecyclerViewAdapter(this, model.mEvents, model.day, model.month);
        eRecyclerView.setAdapter(eAdapter);
        eRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
