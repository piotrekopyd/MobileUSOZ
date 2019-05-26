package com.mobile.usoz.Calendar.Notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Calendar.Calendar.DatesActivity;
import com.mobile.usoz.Calendar.Calendar.RecyclerViewAdapter;
import com.mobile.usoz.Interfaces.NotesDatabaseKeyValues;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class DisplayNotesActivity extends AppCompatActivity implements NotesDatabaseKeyValues {

    private DisplayNotesModel model;
    private Button mAddNoteButton;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

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
    }

    private void setupActivity(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        model = new DisplayNotesModel();
        retreiveDataFromPreviousActivity();
        fetchNotesFromFirebase();
        fetchEventsFromFirebase();
        mAddNoteButton = findViewById(R.id.addNoteButton);
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayNotesActivity.this, AddNewNoteActivity.class);
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(RecyclerViewAdapter.formatNumberToMonth(Integer.valueOf(model.month)) + ", " +
                RecyclerViewAdapter.formatDateToDayOfWeek(model.month, model.day) + " " + model.day);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // ------------------------- Notes ---------------------------------



    private void retreiveDataFromPreviousActivity(){
        Intent intent = getIntent();
        model.month = intent.getStringExtra(RecyclerViewAdapter.DATES_MONTH_EXTRA_TEXT);
        model.day = intent.getStringExtra(RecyclerViewAdapter.DATES_DAY_EXTRA_TEXT);
    }

    // ----------------------------- Fetch data from database ---------------------


    private void fetchNotesFromFirebase(){
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(model.day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            model.mNotes = (ArrayList<String>)document.get(KEY_NOTE);
                            fetchEventsFromFirebase();
                        } else {
                            Toast.makeText(DisplayNotesActivity.this,"Error1", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                setupRecyclerView();
            }
        });
    }

    // ----------------------------- Fetch events from database ---------------------

    private void fetchEventsFromFirebase(){
        db.collection(NOTES_COLLECTION_PATH).document(EVENTS_COLLECTION_PATH).collection(model.month).document(model.day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            model.mEvents = (ArrayList<String>)document.get(KEY_NOTE);
                            fetchEventsFromFirebase();
                        } else {
                            Toast.makeText(DisplayNotesActivity.this,"Error2", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                setupRecyclerView2();
            }
        });
    }

    // ---------------------------- Recycler View -----------------------

    private void setupRecyclerView(){
        nRecyclerView = findViewById(R.id.notes_recycler_view);
        nAdapter = new NotesRecyclerViewAdapter(this, model.mNotes, model.day, model.month);
        nRecyclerView.setAdapter(nAdapter);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerView2(){
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
