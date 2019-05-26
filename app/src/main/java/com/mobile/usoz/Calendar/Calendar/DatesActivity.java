package com.mobile.usoz.Calendar.Calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Calendar.Notes.AddNewNoteActivity;
import com.mobile.usoz.Interfaces.NoteDataDatabaseKeyValues;
import com.mobile.usoz.Interfaces.NotesDatabaseKeyValues;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mobile.usoz.Interfaces.NoteDataDatabaseKeyValues.KEY_DOCUMENT;

public class DatesActivity extends AppCompatActivity implements NotesDatabaseKeyValues {

    private Toolbar toolbar;

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

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        model = new DatesModel();
        getDataFromCalendarActivity();
        retreiveDatesFromFirebase();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(RecyclerViewAdapter.formatNumberToMonth(Integer.valueOf(model.month)));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        model.month = intent.getStringExtra(RecyclerViewAdapter.CALENDAR_EXTRA_TEXT);
    }

    // ------------------ Get data from firebase ----------------------------------

    private void retreiveDatesFromFirebase() {
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            model.mDates = (ArrayList<String>) document.get(KEY_NOTE);
                            if (model.mDates.isEmpty())
                                Toast.makeText(DatesActivity.this, "Folder is empty", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DatesActivity.this, "Folder is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                retreiveEventsFromFirebase();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                retreiveEventsFromFirebase();
            }
        });
    }

    // ------------------ Get events from firebase ----------------------------------

    private void retreiveEventsFromFirebase() {

        /** pobiera ogolne eventy z firebase
         */

        db.collection(NOTES_COLLECTION_PATH).document(EVENTS_COLLECTION_PATH).collection(model.month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            model.mEvents = (ArrayList<String>)document.get(KEY_NOTE);
                            if(model.mEvents.isEmpty())
                                Toast.makeText(DatesActivity.this,"Folder is empty321", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DatesActivity.this,"Folder is empty3", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setupRecyclerView();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                setupRecyclerView();
            }
        });
    }

    // ---------------------- Recycler View -----------------------------------

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.dates_recycle_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(DatesActivity.this, model.mDates, model.mEvents, model.month);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DatesActivity.this));
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
