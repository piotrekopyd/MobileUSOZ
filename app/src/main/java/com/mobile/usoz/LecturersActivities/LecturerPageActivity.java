package com.mobile.usoz.LecturersActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mobile.usoz.R;


public class LecturerPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private Spinner spinner;
    private int mSelectedIndex = 0;

    private TextView universityTextView;
    private TextView lecturesTextView;
    private TextView textViewRate;
    private Button saveButton;
    private Button rateButton;

    private Lecturer lecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_page);

        mAuth = FirebaseAuth.getInstance();

        universityTextView = findViewById(R.id.lecturer_page_text_university);
        lecturesTextView = findViewById(R.id.lecturer_page_text_lectures);
        saveButton = findViewById(R.id.lecturer_page_button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lecturer.setUniversity(universityTextView.getText().toString());
                TextView tv = findViewById(R.id.textViewUniversity);
                String[] strArray = lecturesTextView.getText().toString().split("\n");
                lecturer.setLectures(strArray);
                setTextViewLectures(strArray);
                hideEditField();
            }
        });
        hideEditField();

        Intent intent = getIntent();

        lecturer = (Lecturer) intent.getSerializableExtra("serialized_lecturer");

        TextView textView1 = findViewById(R.id.textViewLecturerName);
        TextView textView2 = findViewById(R.id.textViewUniversity);
        String text = lecturer.getFirstName() + " " + lecturer.getSurname();
        textView1.setText(text);
        textView2.setText(lecturer.getUniversity());
        String[] strArray = lecturer.getLectures();
        if(strArray!=null) {
            setTextViewLectures(strArray);
        }

        spinner = findViewById(R.id.spinnerLecturerGrade);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(LecturerPageActivity.this,
                R.layout.map_spinner, getResources().getTextArray(R.array.lecturers_grades)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.BLACK);
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.GRAY);
                if(position == mSelectedIndex) {
                    tv.setTextColor(Color.BLACK);
                }
                return tv;
            }
        };
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedIndex = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection((int) (lecturer.getGradeUID(mAuth.getUid())*2 - 4));


        textViewRate = findViewById(R.id.textViewRate);
        if(lecturer.getGrade()==0) {
            textViewRate.setText("--");
        } else {
            textViewRate.setText(String.valueOf(lecturer.getGrade()));
        }

        rateButton = findViewById(R.id.rateButton);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        lecturer.updateGrade(mAuth.getUid(), 2);
                        break;
                    case 1:
                        lecturer.updateGrade(mAuth.getUid(), 2.5);
                        break;
                    case 2:
                        lecturer.updateGrade(mAuth.getUid(), 3);
                        break;
                    case 3:
                        lecturer.updateGrade(mAuth.getUid(), 3.5);
                        break;
                    case 4:
                        lecturer.updateGrade(mAuth.getUid(), 4);
                        break;
                    case 5:
                        lecturer.updateGrade(mAuth.getUid(), 4.5);
                        break;
                    case 6:
                        lecturer.updateGrade(mAuth.getUid(), 5);
                        break;
                }
                textViewRate.setText(String.valueOf(lecturer.getGrade()));
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        getIntent().putExtra("updateLecturerOnBackPressed", true);
        getIntent().putExtra("lecturer1", lecturer);
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mAuth.getUid().equals("86dXmf6RNwRPsoD3nm982tJfDzl1")) {
            inflater.inflate(R.menu.lecturer_page_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            case (R.id.lecturer_page_delete):
                Intent intent1 = new Intent();
                intent1.putExtra("deleteLecturer", true);
                intent1.putExtra("lecturerName", lecturer.getFirstName());
                intent1.putExtra("lecturerSurname", lecturer.getSurname());
                setResult(RESULT_OK, intent1);
                this.finish();
                break;
            case (R.id.lecturer_page_edit):
                showEditField();
                universityTextView.setText(lecturer.getUniversity());
                if(lecturer.getLectures()!=null) {
                    setLecturerPageTextLectures(lecturer.getLectures());
                }
                break;
            case (R.id.lecturer_page_save):
                Intent intent2 = new Intent();
                intent2.putExtra("updateLecturer", true);
                intent2.putExtra("lecturer", lecturer);
                setResult(RESULT_OK, intent2);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLecturerPageTextLectures(String[] array) {
        lecturesTextView.setText("");
        for(int i=0; i<array.length; i++) {
            lecturesTextView.append(array[i]);
            if(i!=(array.length-1)) {
                lecturesTextView.append("\n");
            }

        }
    }

    private void setTextViewLectures(String[] array) {
        TextView tv = findViewById(R.id.textViewLectures);
        tv.setText("");
        for(int i=0; i<array.length; i++) {
            tv.append("* "+ array[i]);
            if(i!=(array.length-1)) {
                tv.append("\n");
            }
        }
    }

    private void showEditField() {
        universityTextView.setVisibility(View.VISIBLE);
        lecturesTextView.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }

    private void hideEditField() {
        universityTextView.setVisibility(View.INVISIBLE);
        lecturesTextView.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.lecturers_frame_layout).getWindowToken(), 0);
    }
}
