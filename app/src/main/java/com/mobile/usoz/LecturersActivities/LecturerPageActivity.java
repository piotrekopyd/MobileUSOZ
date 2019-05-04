package com.mobile.usoz.LecturersActivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mobile.usoz.CalendarActivity;
import com.mobile.usoz.ForumActivity;
import com.mobile.usoz.MapsActivity;
import com.mobile.usoz.NotesActivity;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;


public class LecturerPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private Spinner spinner;
    private int mSelectedIndex = 0;

    private Lecturer lecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_page);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        lecturer = (Lecturer) intent.getSerializableExtra("serialized_lecturer");

        TextView textView1 = findViewById(R.id.textViewLecturer);
        TextView textView2 = findViewById(R.id.textViewUniversity);
        String text = lecturer.getFirstName() + " " + lecturer.getSurname();
        textView1.setText(text);
        textView2.setText(lecturer.getUniversity());

        spinner = findViewById(R.id.lecturer_grade);
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                Intent intent = new Intent();
                intent.putExtra("deleteLecturer", true);
                intent.putExtra("lecturerName", lecturer.getFirstName());
                intent.putExtra("lecturerSurname", lecturer.getSurname());
                setResult(RESULT_OK, intent);
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
