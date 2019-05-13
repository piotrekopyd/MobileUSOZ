package com.mobile.usoz.Calendar.Calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Interfaces.NoteDataDatabaseKeyValues;
import com.mobile.usoz.R;

import java.util.ArrayList;

import static com.mobile.usoz.Interfaces.NoteDataDatabaseKeyValues.KEY_DOCUMENT;

public class DatesActivity extends AppCompatActivity implements NoteDataDatabaseKeyValues {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db ;

    private DatesModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setupActivity();
    }


    private void setupActivity(){
        mAuth = FirebaseAuth.getInstance();
        model = new DatesModel();
        getDataFromCalendarActivity();
        setupRecyclerView();
    }



    private void getDataFromCalendarActivity() {
        Intent intent = getIntent();
        model.month = intent.getStringExtra(RecyclerViewAdapter.CALENDAR_EXTRA_TEXT);
    }

    private void retreiveDataFromFirebase(){
        db.collection ("NoteData").document(user.getUid()).collection(model.month).document(KEY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        model.mDates = (ArrayList<String>) document.get("dates");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    // ---------------------- Recycler View -----------------------------------

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.calendar_recycle_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, model.mDates);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
