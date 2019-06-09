package com.mobile.usoz.Calendar.Notes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.CollectiveMethods.CollectiveMethods;
import com.mobile.usoz.DatabaseManager.AddNoteDatabaseManager;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NoteDataDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;
import com.mobile.usoz.R;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewNoteActivity extends AppCompatActivity implements NotesDatabaseKeyValues, NoteDataDatabaseKeyValues{

    private Toolbar toolbar;
    private Button mSaveButton;
    private EditText mNoteContentEditText;
    private Spinner daySpinner, monthSpinner;

    private AddNewNoteModel model;
    private AddNoteDatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);

        setupActivity();
    }

    private void setupActivity(){
        model = new AddNewNoteModel();
        databaseManager = new AddNoteDatabaseManager();
        setupView();
        setupSpinners();

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupView(){
        mNoteContentEditText = findViewById(R.id.saveNoteNoteTextView);
        mSaveButton = findViewById(R.id.saveNoteSaveButton);
        daySpinner = findViewById(R.id.AddNewNoteDaySpinner);
        monthSpinner = findViewById(R.id.AddNewNoteMonthSpinner);

        /** Add listener to button */
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(daySpinner.getSelectedItemPosition() > 0 && monthSpinner.getSelectedItemPosition() > 0 && !mNoteContentEditText.getText().toString().matches("")) {
                    if(CollectiveMethods.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                        databaseManager.saveNote(mNoteContentEditText.getText().toString(), model.day, model.month);
                        Toast.makeText(AddNewNoteActivity.this, "Wpis został dodany", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddNewNoteActivity.this, "Nie udało się zapisać Twojego wpisu!", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(AddNewNoteActivity.this, CalendarActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddNewNoteActivity.this, "Pusta notka lub niepoprawna data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // --------------------------- Setup Spinners ---------------------------------------------------------
    private void setupSpinners(){
        model.months.add(0,"Miesiąc");
        String[] array= new String[model.months.size()];
        model.months.toArray(array);

        model.days.add("Dzień");
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
                    newDays.add("Dzień");
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
