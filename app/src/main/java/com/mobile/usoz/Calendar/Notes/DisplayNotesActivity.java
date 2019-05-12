package com.mobile.usoz.Calendar.Notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Calendar.Calendar.RecyclerViewAdapter;
import com.mobile.usoz.Interfaces.NotesDatabaseKeyValuesInterface;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class DisplayNotesActivity extends AppCompatActivity implements NotesDatabaseKeyValuesInterface {

    private ArrayList<String> mNotes= new ArrayList<>();
    private String month;
    private String day;
    private Button mAddNoteButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db ;

    static final public String NOTES_DAYS_EXTRA_TEXT = "com.mobile.usoz.Calendar.DisplayNotes.EXTRA_TEXT_DAYS";
    static final public String NOTES_MONTHS_EXTRA_TEXT = "com.mobile.usoz.Calendar.DisplayNotes.EXTRA_TEXT_MONTHS";

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

        initDates();
        setupRecyclerView();


        //month = preparePassedMonth(month);

        mAddNoteButton = findViewById(R.id.addNoteButton);
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayNotesActivity.this, AddNewNoteActivity.class);
                intent.putExtra(NOTES_MONTHS_EXTRA_TEXT, month);
                intent.putExtra(NOTES_DAYS_EXTRA_TEXT, day);
                startActivity(intent);
            }
        });
    }


    // ------------------------- Notes ---------------------------------
    public void initDates(){

    }

    private void retreiveDataFromPreviousActivity(){
        Intent intent = getIntent();
        month = intent.getStringExtra(RecyclerViewAdapter.DATES_EXTRA_TEXT);
        day = intent.getStringExtra(RecyclerViewAdapter.DATES_DAYS_EXTRA_TEXT);
    }

    // ----------------------------- Fetch data from database ---------------------


    private void fetchDataFromFirebase(){
        db.collection (NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        mNotes = (ArrayList<String>) document.get(KEY_NOTE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    // ---------------------------- Recycler View -----------------------

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.notes_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,mNotes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
