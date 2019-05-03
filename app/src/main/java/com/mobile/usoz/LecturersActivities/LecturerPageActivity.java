package com.mobile.usoz.LecturersActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mobile.usoz.R;

public class LecturerPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_page);

        Intent intent = getIntent();

        TextView textView = findViewById(R.id.textView1);
        textView.setText(intent.getStringExtra(LecturersActivity.EXTRA_STRING1));

    }
}
