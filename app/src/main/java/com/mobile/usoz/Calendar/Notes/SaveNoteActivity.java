package com.mobile.usoz.Calendar.Notes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.mobile.usoz.R;

public class SaveNoteActivity extends AppCompatActivity {

    private Button mSaveButton;
    private EditText mDateEditText;
    private EditText mNoteContentEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);
    }
}
