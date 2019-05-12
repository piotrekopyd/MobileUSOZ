package com.mobile.usoz.Calendar.Notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Calendar.Calendar.RecyclerViewAdapter;
import com.mobile.usoz.Interfaces.NoteDataDatabaseKeyValues;
import com.mobile.usoz.Interfaces.NotesDatabaseKeyValuesInterface;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.EdutUserDataActivities.EditUserDataActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class AddNewNoteActivity extends AppCompatActivity implements NotesDatabaseKeyValuesInterface, NoteDataDatabaseKeyValues {

    private Button mSaveButton;
    private EditText mDateEditText;
    private EditText mNoteContentEditText;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db ;

    private String month;
    private String day;
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);
        setup();
    }

    private void setup(){
        mDateEditText = findViewById(R.id.saveNoteDataTextView);
        mNoteContentEditText = findViewById(R.id.saveNoteNoteTextView);
        mSaveButton = findViewById(R.id.saveNoteSaveButton);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
                Intent intent = new Intent(AddNewNoteActivity.this, DisplayNotesActivity.class);
                startActivity(intent);
            }
        });
        setupDatabaseAuth();

        Intent intent = getIntent();
        month = intent.getStringExtra(DisplayNotesActivity.NOTES_MONTHS_EXTRA_TEXT);
        day = intent.getStringExtra(DisplayNotesActivity.NOTES_DAYS_EXTRA_TEXT);
    }

    // ----------------------- Send note to firebase -----------------------------

    private void setupDatabaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    private void saveNote(){
        Map<String, Object> note = new HashMap<>();
        note.put(NOTES_NOTE_DOCUMENT_PATH, mNoteContentEditText.getText().toString());


        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).get();
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNewNoteActivity.this, "Note has been saved!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewNoteActivity.this, "Error while saving note!", Toast.LENGTH_SHORT).show();
                    }
                });
        saveNoteDate();
    }

    private void saveNoteDate(){
        retrieveOldDates();
        if(list != null){
            list.add(day + "-" + month);
            saveNewDate();
        }
    }

    private void retrieveOldDates (){
        db.collection (KEY_COLLECTION_NAME).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        list = (ArrayList<String>) document.get(KEY_VALUE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        list = null;
                    }
                });
    }
    private void saveNewDate(){
        Map<String, Object> date = new HashMap<>();
        date.put(KEY_VALUE, list);
        db.collection(KEY_COLLECTION_NAME). document(user.getUid()).collection(month).document(KEY_DOCUMENT).set(date)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewNoteActivity.this, "Error while saving data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
