package com.mobile.usoz.LecturersActivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.firestore.Transaction;
import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.MapsActivity;
import com.mobile.usoz.R;
import com.mobile.usoz.UserAccount.LogInActivity;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import org.apache.commons.lang3.SerializationUtils;
import java.util.Collection;
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

    private static LinkedList<Lecturer> lectutersCollection;
    private double grade;
    private double gradeUID;
    private int gradesMapSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lecturers);

        findViewById(R.id.included_exit_layout).setVisibility(View.INVISIBLE);

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
    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isNetworkAvailable()) {
                if(lectutersCollection==null) {
                    Toast.makeText(LecturersActivity.this, "Sieć znów działa", Toast.LENGTH_SHORT).show();
                    downloadLecturers();
                }
            } else if(lectutersCollection==null) {
                Toast.makeText(LecturersActivity.this, "Wystąpił błąd podczas pobierania listy wykładowców. Spróbuj ponownie za chwilę", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        if (lectutersCollection != null) {
            for (Lecturer l:
                    lectutersCollection) {
                addLecturer(l.getFirstName(), l.getSurname(), l.getUniversity());
            }
        } else if(isNetworkAvailable()) {
            downloadLecturers();
        }
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void downloadLecturers() {
        lectutersCollection = new LinkedList<Lecturer>();

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference("Lecturers").child("lecturersCollection");
        storageReference.getBytes(100*1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                lectutersCollection = SerializationUtils.deserialize(bytes);
                for (Lecturer l:
                        lectutersCollection) {
                    addLecturer(l.getFirstName(), l.getSurname(), l.getUniversity());
                }
            }
        });
    }

    public static synchronized void updateGrade(final String UID, final String lecturer, final double grade1) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("Lecturers").document(lecturer);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(documentReference);

                Map<String, Number> map;

                if(!snapshot.exists()) {
                    map = new HashMap<String, Number>();
                    map.put(UID, grade1);

                    Map<String, Map<String, Number>> tmpMap = new HashMap<>();
                    tmpMap.put("grades", map);

                    db.collection("Lecturers").document(lecturer).set(tmpMap);
                } else {
                    map = (Map<String, Number>) snapshot.get("grades");

                    if(map == null) {
                        map = new HashMap<String, Number>();
                        map.put(UID, grade1);
                    } else {
                        Number n = map.get(UID);

                        if(n!=null) {
                            if(n.doubleValue()!=0) {
                                return null;
                            }
                        }

                        map.put(UID, grade1);
                    }

                    transaction.update(documentReference, "grades", map);
                }
                // Success
                return null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
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

                TextView tv1 = (TextView) linearLayout.getChildAt(0);

                final String lecturerName =  tv1.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference documentReference = db.collection("Lecturers").document(lecturerName);

                grade = 0;
                gradeUID = 0;
                gradesMapSize = 0;

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                Map<String, Number> gradesMap = new HashMap<>();

                                gradesMap = (Map<String, Number>) document.get("grades");

                                grade = 0;
                                gradeUID = 0;
                                gradesMapSize = 0;

                                if(gradesMap!=null) {
                                    gradesMapSize = gradesMap.size();

                                    if(gradesMap.containsKey(mAuth.getUid())) {
                                        gradeUID = gradesMap.get(mAuth.getUid()).doubleValue();
                                    }

                                    Collection<Number> collection = gradesMap.values();

                                    for(Number n : collection) {
                                        grade += n.doubleValue();
                                    }
                                    grade /= gradesMap.size();
                                }
                            }
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent intent = new Intent(LecturersActivity.this, LecturerPageActivity.class);

                        Lecturer lecturer;

                        for(int i=0; i<lectutersCollection.size(); i++) {
                            lecturer = lectutersCollection.get(i);
                            if((lecturer.getFirstName() + " " + lecturer.getSurname()).equals(lecturerName)) {
                                intent.putExtra("serialized_lecturer", lecturer);
                                intent.putExtra("lecturer_grade", grade);
                                intent.putExtra("grade_UID", gradeUID);
                                intent.putExtra("gradesMapSize", gradesMapSize);

                                startActivityForResult(intent,1);
                            }
                        }
                    }
                });
            }
        });

        LinearLayout linearLayout1 = findViewById(R.id.lecturers_linear_layout);
        linearLayout1.addView(linearLayout);

    }

    private void showEditField() {
        findViewById(R.id.lecturers_edit_relative_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.lecturers_edit_relative_layout).setClickable(true);
        findViewById(R.id.lecturers_edit_relative_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEditField();
            }
        });

        findViewById(R.id.lecturers_relative_layout1).setForeground(new ColorDrawable(Color.BLACK));
        findViewById(R.id.lecturers_relative_layout1).getForeground().setAlpha(180);
    }

    private void hideEditField() {
        findViewById(R.id.lecturers_edit_relative_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.lecturers_edit_relative_layout).setClickable(false);

        findViewById(R.id.lecturers_relative_layout1).setForeground(new ColorDrawable(Color.TRANSPARENT));

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
        }

        if(findViewById(R.id.lecturers_edit_relative_layout).getVisibility()==View.VISIBLE) {
            hideEditField();
            return;
        }

        findViewById(R.id.lecturers_relative_layout1).setForeground(new ColorDrawable(Color.BLACK));
        findViewById(R.id.lecturers_relative_layout1).getForeground().setAlpha(180);

        findViewById(R.id.included_exit_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.included_exit_layout).setClickable(true);

        Button button = findViewById(R.id.exit_reject_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.lecturers_relative_layout1).setForeground(new ColorDrawable(Color.TRANSPARENT));

                findViewById(R.id.included_exit_layout).setVisibility(View.INVISIBLE);
                findViewById(R.id.included_exit_layout).setClickable(false);
            }
        });

        button = findViewById(R.id.exit_confirm_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeApplication();
            }
        });
    }

    private void closeApplication() {
        finishAffinity();
        System.exit(0);
    }

    @Override
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
            case R.id.nav_lecturers:
                intent = new Intent(LecturersActivity.this, LecturersActivity.class);
                break;
            case R.id.nav_maps:
                intent = new Intent(LecturersActivity.this, MapsActivity.class);
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
        //TODO: delete from database
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

