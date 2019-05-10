package com.mobile.usoz.LecturersActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.ForumActivity;
import com.mobile.usoz.LogInActivity;
import com.mobile.usoz.MapsActivity;
import com.mobile.usoz.Calendar.Notes.NotesActivity;
import com.mobile.usoz.R;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import org.apache.commons.lang3.SerializationUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LecturersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Zmienne do layoutu
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView universityTextView;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private LinkedList<Lecturer> lectutersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lectutersCollection = new LinkedList<Lecturer>();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Lecturers").child("lecturersCollection");
        storageReference.getBytes(100*1028*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                lectutersCollection = SerializationUtils.deserialize(bytes);
                for (Lecturer l:
                        lectutersCollection) {
                    addLecturer(l.getFirstName(), l.getSurname(), l.getUniversity());
                }
            }
        });

        setContentView(R.layout.activity_lecturers);

        mAuth = FirebaseAuth.getInstance();

        nameTextView = findViewById(R.id.lecturers_text_name);
        surnameTextView = findViewById(R.id.lecturers_text_surname);
        universityTextView = findViewById(R.id.lecturers_text_university);
        saveButton = findViewById(R.id.lecturers_button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameTextView.getText().toString();
                String surname = surnameTextView.getText().toString();
                String university = universityTextView.getText().toString();
                if(name.equals("") || university.equals("") || surname.equals("")) {
                    Toast.makeText(LecturersActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    lectutersCollection.add(new Lecturer(name, surname, university));
                    addLecturer(name, surname, university);
                    nameTextView.setText("");
                    surnameTextView.setText("");
                    universityTextView.setText("");
                    hideEditField();
                }
             }
        });

        hideEditField();

        /** inicjalizacja layoutu
         * */

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideEditField();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_lecturers);

        RelativeLayout relativeLayout = findViewById(R.id.lecturers_relative_layout1);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEditField();
            }
        });
}

    private void addLecturer(String name, String surname, String university) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayoutParams.setMargins(4,3,3,4);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setBackgroundResource(R.drawable.lecturers_linear_layout_border);

        TextView textView1 = new TextView(this);
        textView1.setText(name + " " + surname);
        textView1.setTextSize(25);

        SpannableString spannableString = new SpannableString(university);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);

        TextView textView2 = new TextView(this);
        textView2.setText(spannableString);
        textView2.setTextSize(16);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(textView1);
        linearLayout.addView(textView2);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LecturersActivity.this, LecturerPageActivity.class);

                TextView tv1 = (TextView) linearLayout.getChildAt(0);

                Lecturer lecturer;

                String str =  tv1.getText().toString();

                for(int i=0; i<lectutersCollection.size(); i++) {
                    lecturer = lectutersCollection.get(i);
                    if((lecturer.getFirstName() + " " + lecturer.getSurname()).equals(str)) {
                        intent.putExtra("serialized_lecturer", lecturer);
                        startActivityForResult(intent,1);
                    }
                }
            }
        });

        LinearLayout linearLayout1 = findViewById(R.id.lecturers_linear_layout);
        linearLayout1.addView(linearLayout);

    }

    private void showEditField() {
        nameTextView.setVisibility(View.VISIBLE);
        surnameTextView.setVisibility(View.VISIBLE);
        universityTextView.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }

    private void hideEditField() {
        nameTextView.setVisibility(View.INVISIBLE);
        surnameTextView.setVisibility(View.INVISIBLE);
        universityTextView.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.lecturers_frame_layout).getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mAuth.getUid().equals("86dXmf6RNwRPsoD3nm982tJfDzl1")) {
            inflater.inflate(R.menu.lecturers_edit_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.lecturers_settings_add) :
                showEditField();
                break;
            case (R.id.lecturers_settings_send) :
                sendDataToFirebase();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendDataToFirebase() {
        try {
            byte[] myBytes = SerializationUtils.serialize(lectutersCollection);
            firebaseStorage = FirebaseStorage.getInstance();
            storageReference = firebaseStorage.getReference("Lecturers").child("lecturersCollection");
            storageReference.putBytes(myBytes);
        } catch (Exception e) {}
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if(data.getBooleanExtra("deleteLecturer", false)) {
                    String name = data.getStringExtra("lecturerName");
                    String surname = data.getStringExtra("lecturerSurname");
                    int i = deleteLecturer(name, surname);
                    if(i!=(-1)) {
                        LinearLayout linearLayout1 = findViewById(R.id.lecturers_linear_layout);
                        linearLayout1.removeViewAt(i);
                    }
                } else if(data.getBooleanExtra("updateLecturer", false)) {
                    Lecturer lecturer = (Lecturer) data.getSerializableExtra("lecturer");
                    String name = lecturer.getFirstName();
                    String surname = lecturer.getSurname();
                    Lecturer l;
                    for (int i=0; i<lectutersCollection.size(); i++) {
                        l = lectutersCollection.get(i);
                        if(l.getFirstName().equals(name) && l.getSurname().equals(surname)) {
                            l.setUniversity(lecturer.getUniversity());
                            l.setLectures(lecturer.getLectures());
                            l.setGradesMap(lecturer.getGradesMap());

                            LinearLayout linearLayout1 = findViewById(R.id.lecturers_linear_layout);

                            for (int j=i; j<lectutersCollection.size(); j++) {
                                linearLayout1.removeViewAt(i);
                            }

                            for (int j=i; j<lectutersCollection.size(); j++) {
                                l = lectutersCollection.get(j);
                                addLecturer(l.getFirstName(), l.getSurname(), l.getUniversity());
                            }

                            break;
                        }
                    }
                } else if(data.getBooleanExtra("updateLecturerOnBackPressed", false)) {
                    Lecturer lecturer = (Lecturer) data.getSerializableExtra("lecturer1");
                    String name = lecturer.getFirstName();
                    String surname = lecturer.getSurname();
                    Lecturer l;
                    for (int i=0; i<lectutersCollection.size(); i++) {
                        l = lectutersCollection.get(i);
                        if (l.getFirstName().equals(name) && l.getSurname().equals(surname)) {
                            l.setGradesMap(lecturer.getGradesMap());
                            l.setGrade(lecturer.getGrade());
                            sendDataToFirebase();
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                intent = new Intent(LecturersActivity.this, UserProfileAcitivity.class);
                break;
            case R.id.nav_calendar:
                intent = new Intent(LecturersActivity.this, CalendarActivity.class);
                break;
            case R.id.nav_forum:
                intent = new Intent(LecturersActivity.this, ForumActivity.class);
                break;
            case R.id.nav_lecturers:
                intent = new Intent(LecturersActivity.this, LecturersActivity.class);
                break;
            case R.id.nav_maps:
                intent = new Intent(LecturersActivity.this, MapsActivity.class);
                break;
            case R.id.nav_notes:
                intent = new Intent(LecturersActivity.this, NotesActivity.class);
                break;
            case R.id.nav_log_out:
                logOut();
                break;
        }
        if(intent!=null) {
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        //FIREBASE LOG OUT
        mAuth.signOut();

        // FB LOG OUT
        LoginManager.getInstance().logOut();
        UpdateUI();
    }

    private void UpdateUI() {
        Toast.makeText(LecturersActivity.this, "You're logged out", Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(LecturersActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private int deleteLecturer(String name, String surname) {
        Lecturer l;
        for (int i=0; i<lectutersCollection.size(); i++) {
            l = lectutersCollection.get(i);
            if(l.getFirstName().equals(name) && l.getSurname().equals(surname)) {
                lectutersCollection.remove(i);
                return i;
            }
        }
        return -1;
    }
}

class Lecturer implements Serializable {
    private String firstName;
    private String surname;
    private String university;
    private String[] lectures;
    private Map<String, Double> gradesMap;
    private double grade;
    public Lecturer(String firstName, String surname, String university) {
        this.firstName = firstName;
        this.surname = surname;
        this.university = university;
        this.grade = 0;
        this.gradesMap = new HashMap<String, Double>();
    }
    public String getFirstName() {
        return firstName;
    }
    public String getSurname() {
        return surname;
    }
    public String getUniversity() {
        return university;
    }
    public String[] getLectures() {
        return lectures;
    }
    public void setUniversity(String university) {
        this.university = university;
    }
    public void setLectures(String[] lectures) {
        this.lectures = lectures;
    }
    public void updateGrade(String UID, double g) {
        if(gradesMap==null) {
            gradesMap = new HashMap<>();
        }
        if(gradesMap.get(UID)==null) {
            double d = gradesMap.size()*grade;
            d+=g;
            gradesMap.put(UID, g);
            grade = d/gradesMap.size();
        } else {
            double d = gradesMap.size()*grade;
            d -= gradesMap.get(UID);
            d+=g;
            grade = d/gradesMap.size();
            gradesMap.remove(UID);
            gradesMap.put(UID, g);
        }
    }

    public Map<String, Double> getGradesMap() {
        return gradesMap;
    }
    public void setGradesMap(Map<String, Double> gradesMap) {
        this.gradesMap = gradesMap;
    }
    public double getGrade() {
        return grade;
    }
    public double getGradeUID(String UID) {
        if(gradesMap==null) return 0;
        if(gradesMap.get(UID)==null) return 0;
        return gradesMap.get(UID);
    }
    public void setGrade(double grade) {
        this.grade = grade;
    }
}
