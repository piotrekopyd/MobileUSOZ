package com.mobile.usoz.Calendar.Notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.value.FieldValueOptions;
import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.Interfaces.NoteDataDatabaseKeyValues;
import com.mobile.usoz.Interfaces.NotesDatabaseKeyValues;
import com.mobile.usoz.R;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewNoteActivity extends AppCompatActivity implements NotesDatabaseKeyValues, NoteDataDatabaseKeyValues{

    private Button mSaveButton;
    private EditText mNoteContentEditText;
    private Spinner daySpinner, monthSpinner;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db ;

    private AddNewNoteModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);
        setup();
    }

    private void setup(){
        model = new AddNewNoteModel();
        mNoteContentEditText = findViewById(R.id.saveNoteNoteTextView);
        mSaveButton = findViewById(R.id.saveNoteSaveButton);
        daySpinner = findViewById(R.id.AddNewNoteDaySpinner);
        monthSpinner = findViewById(R.id.AddNewNoteMonthSpinner);
        setupSpinners();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(daySpinner.getSelectedItemPosition() > 0 && monthSpinner.getSelectedItemPosition() > 0 && !mNoteContentEditText.getText().toString().matches("")) {
                    saveNote();
                    Intent intent = new Intent(AddNewNoteActivity.this, CalendarActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddNewNoteActivity.this, "Empty note or invalid date!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setupDatabaseAuth();

    }



    // --------------------------- Setup Spinners ---------------------------------------------------------
    private void setupSpinners(){
        model.months.add(0,"Miesiąc");
        String[] array= new String[model.months.size()];
        model.months.toArray(array);

        model.days.add("Day");
        String[] arrayDay = new String[model.days.size()];
        model.days.toArray(arrayDay);

        ArrayAdapter<String> adapterDay = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrayDay);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapterDay);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    model.month = Integer.toString(position);
                    List<String> newDays = new ArrayList<>();
                    newDays.add("Day");
                    YearMonth yearMonthObject = YearMonth.of(2019,position);
                    for(int i=1; i <= yearMonthObject.lengthOfMonth(); i++){
                        newDays.add(Integer.toString(i));
                    }
                    String[] arr= new String[newDays.size()];
                    newDays.toArray(arr);
                    ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(AddNewNoteActivity.this,android.R.layout.simple_spinner_item,arr);
                    newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    daySpinner.setAdapter(newAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)
                    model.day = Integer.toString(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    // ----------------------- Send note to firebase -----------------------------

    private void setupDatabaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    private void saveNote(){
        final Map<String, Object> note = new HashMap<>();
        note.put(KEY_NOTE, mNoteContentEditText.getText().toString());
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(model.day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(model.day).update(KEY_NOTE, FieldValue.arrayUnion(mNoteContentEditText.getText().toString()));
                            } else {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(model.day).set(note);
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(model.day).update(KEY_NOTE, FieldValue.arrayUnion(mNoteContentEditText.getText().toString()));
                            }
                        } else {
                            Toast.makeText(AddNewNoteActivity.this, "DATABASE ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayUnion(model.day));
                            } else {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).set(note);
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(model.month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayUnion(model.day));
                            }
                        } else {
                            Toast.makeText(AddNewNoteActivity.this, "DATABASE ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveNoteDate(){
        retrieveOldDates();
        if(model.listOfNotes != null){
            model.listOfNotes.add(model.day + "-" + model.month);
            saveNewDate();
        }
    }

    private void retrieveOldDates (){
        db.collection (KEY_COLLECTION_NAME).document(user.getUid()).collection(model.month).document(model.day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        model.listOfNotes = (ArrayList<String>) document.get(KEY_VALUE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        model.listOfNotes = null;
                    }
                });
    }
    private void saveNewDate(){
        Map<String, Object> date = new HashMap<>();
        date.put(KEY_VALUE, model.listOfNotes);
        db.collection(KEY_COLLECTION_NAME). document(user.getUid()).collection(model.month).document(KEY_DOCUMENT).set(date)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewNoteActivity.this, "Error while saving data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
