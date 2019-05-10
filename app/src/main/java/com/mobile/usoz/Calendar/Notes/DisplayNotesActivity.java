package com.mobile.usoz.Calendar.Notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mobile.usoz.Calendar.Calendar.RecyclerViewAdapter;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class DisplayNotesActivity extends AppCompatActivity {

    private ArrayList<String> mNotes= new ArrayList<>();

    private Button mAddNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);
        setupActivity();
    }

    private void setupActivity(){
        initDates();
        setupRecyclerView();

        mAddNoteButton = findViewById(R.id.addNoteButton);
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayNotesActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });
    }


    // ------------------------- Notes ---------------------------------
    public void initDates(){
        mNotes.add("Siema, dziaiaj byłem u kolegi i graliśmy  w słoneczko");
        mNotes.add("Hej dzisiaj zrobile" +
                "m" +
                "Pierwszy raz" +
                "djfsifjdsf");



    }

    // ---------------------------- Recycler View -----------------------

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.notes_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,mNotes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
