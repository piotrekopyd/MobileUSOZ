package com.mobile.usoz.Calendar.Calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Interfaces.NoteDataDatabaseKeyValues;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import java.util.ArrayList;
import java.util.Date;

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
        retreiveDatesFromFirebase();
        setupRecyclerView();
    }



    private void getDataFromCalendarActivity() {
        Intent intent = getIntent();
        model.month = intent.getStringExtra(RecyclerViewAdapter.CALENDAR_EXTRA_TEXT);
    }

    private void retreiveDatesFromFirebase(){
        db.collection (KEY_COLLECTION_NAME).document(user.getUid()).collection(model.month).document(KEY_DOCUMENT).get()
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
                        Toast.makeText(DatesActivity.this,"Folder is empty", Toast.LENGTH_SHORT).show();
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
