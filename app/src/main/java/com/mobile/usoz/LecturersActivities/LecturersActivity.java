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
import com.mobile.usoz.Administrator.Administrator;
import com.mobile.usoz.Administrator.AdministratorCallback;
import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.CollectiveMethods.CollectiveMethods;
import com.mobile.usoz.CollectiveMethods.CollectiveMethodsCallback;
import com.mobile.usoz.Maps.MapsActivity;
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
    private BroadcastReceiver networkChangeReceiver;

    private LecturersModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lecturers);

        findViewById(R.id.included_exit_layout).setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        model = new LecturersModel();

        setupEditField();

        hideEditField();

        setupNavigation();

        if (CollectiveMethods.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)) && model.lectutersCollection == null) {
            downloadLecturers();
        }

        setupReciever();
    }

    /**
     * metoda ustawiajaca pole edycji dla administratora
     */

    private void setupEditField() {
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
                if (name.equals("") || university.equals("") || surname.equals("")) {
                    Toast.makeText(LecturersActivity.this, "Musisz wypełnić wszystkie pola", Toast.LENGTH_SHORT).show();
                } else {
                    model.lectutersCollection.add(new Lecturer(name, surname, university));
                    addLecturer(name, surname, university);
                    nameTextView.setText("");
                    surnameTextView.setText("");
                    universityTextView.setText("");
                    hideEditField();
                }
            }
        });
    }

    /**
     * ustawianie paska nawigacji
     */

    private void setupNavigation() {
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

    /**
     * ustawianie recievera na nasluchiwanie zmiany polaczenia internetowego
     */

    private void setupReciever() {
        networkChangeReceiver = CollectiveMethods.setupReciever(new CollectiveMethodsCallback() {
            @Override
            public void onDownload(boolean download) {
                if (download) {
                    downloadLecturers();
                }
            }
        }, (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE), model.lectutersCollection, LecturersActivity.this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(networkChangeReceiver, intentFilter);
    }

    /**
     * rejestracja reciever'a z zamiarem sledzenia zmiany polaczenia internetowego
     */

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (Exception e) {}
    }

    /** metoda pobierajaca informacje o wykladowcach z bazy danych
     */

    public void downloadLecturers() {
        model.lectutersCollection = new LinkedList<>();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference("Lecturers").child("lecturersCollection");
        storageReference.getBytes(100*1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                model.lectutersCollection = SerializationUtils.deserialize(bytes);
                for (Lecturer l:
                        model.lectutersCollection) {
                    addLecturer(l.getFirstName(), l.getSurname(), l.getUniversity());
                }
                try {
                    unregisterReceiver(networkChangeReceiver);
                } catch (Exception e) {}
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(com.mobile.usoz.LecturersActivities.LecturersActivity.this, "Nie udało się pobrać listy prowadzących", Toast.LENGTH_LONG).show();
            }
        });
    }

    /** aktualizacja pojedynczej oceny wprowadzonej przez uzytkownika o identyfikatorze "UID" na wykladowcy "lecturer"
     */

    public static synchronized void updateGrade(final String UID, final String lecturer, final double grade1, final Context context) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("Lecturers").document(lecturer);

        /** uruchamianie transakcji wprowadzajacej pojedynczy rekord do bazy danych,
         *  jesli rekord istnieje, transakcja konczy sie niepowodzeniem
         */
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(documentReference);

                Map<String, Number> map;

                if(snapshot.exists()) {
                    map = (Map<String, Number>) snapshot.get("grades");

                    if(map == null) {
                        map = new HashMap<String, Number>();
                        map.put(UID, grade1);
                    } else {
                        Number n = map.get(UID);

                        if(n!=null) {
                            if(n.doubleValue()!=0) {
                                Toast.makeText(context, "Niestety oceniłeś już wcześniej tego prowadzącego. Jeżeli uważasz, że to błąd aplikacji skontaktuj się z nami", Toast.LENGTH_LONG).show();
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
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Twoja ocena została dodana", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Błąd podczas dodawania Twojej oceny do bazy danych! Spróbuj ponownie za chwilę", Toast.LENGTH_LONG).show();
            }
        });
    }

    /** metoda dodajaca przycisk do menu wykladowcow
     *  przycisk jest odpowiednio utworzonym layoutem
     */

    private void addLecturer(String name, String surname, String university) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayoutParams.setMargins(4,3,3,4);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setBackgroundResource(R.drawable.linear_layout_border_1);

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


        /** ustawianie listenera dla danego przycisku
         *  i przechodzenie do strony wykladowcy
         */

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tv1 = (TextView) linearLayout.getChildAt(0);

                final String lecturerName =  tv1.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference documentReference = db.collection("Lecturers").document(lecturerName);

                model.grade = 0;
                model.gradeUID = 0;
                model.gradesMapSize = 0;

                /** pobieranie z firebase mapy ocen danego wykladowcy
                 */

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                Map<String, Number> gradesMap = new HashMap<>();

                                gradesMap = (Map<String, Number>) document.get("grades");

                                model.grade = 0;
                                model.gradeUID = 0;
                                model.gradesMapSize = 0;

                                if(gradesMap!=null) {
                                    model.gradesMapSize = gradesMap.size();

                                    if(model.gradesMapSize!=0) {

                                        if(gradesMap.containsKey(mAuth.getUid())) {
                                            model.gradeUID = gradesMap.get(mAuth.getUid()).doubleValue();
                                        }

                                        Collection<Number> collection = gradesMap.values();

                                        for(Number n : collection) {
                                            model.grade += n.doubleValue();
                                        }
                                        model.grade /= gradesMap.size();
                                    }
                                }
                            }
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    /** przechodzenie do strony wykladowcy
                     */

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent intent = new Intent(LecturersActivity.this, LecturerPageActivity.class);

                        Lecturer lecturer;

                        for(int i=0; i<model.lectutersCollection.size(); i++) {
                            lecturer = model.lectutersCollection.get(i);
                            if((lecturer.getFirstName() + " " + lecturer.getSurname()).equals(lecturerName)) {
                                intent.putExtra("serialized_lecturer", lecturer);
                                intent.putExtra("lecturer_grade", model.grade);
                                intent.putExtra("grade_UID", model.gradeUID);
                                intent.putExtra("gradesMapSize", model.gradesMapSize);

                                if(CollectiveMethods.isNetworkAvailableForToast((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE), LecturersActivity.this)) {
                                    startActivityForResult(intent,1);
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(com.mobile.usoz.LecturersActivities.LecturersActivity.this, "Nie udało się pobrać ocen wybranego prowadzącego", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        LinearLayout linearLayout1 = findViewById(R.id.lecturers_linear_layout);
        linearLayout1.addView(linearLayout);
    }

    /** metoda ustawiajaca pole edycji dla administratora na widoczne
     */

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();

        Administrator.isAdministrator(new AdministratorCallback() {
            @Override
            public void onCallback(boolean isAdministrator) {
                if(isAdministrator) {
                    inflater.inflate(R.menu.lecturers_edit_menu, menu);
                }
            }
        }, mAuth.getUid());

        return super.onCreateOptionsMenu(menu);
    }

    /** metoda obslugujaca menu edycji administratora
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item!=null) {
            switch (item.getItemId()) {
                case (R.id.lecturers_settings_add):
                    showEditField();
                    break;
                case (R.id.lecturers_settings_send):
                    sendDataToFirebase();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /** metoda wysylajaca dane o wykladowcach do firebase
     */

    private void sendDataToFirebase() {
        try {
            if(CollectiveMethods.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                byte[] myBytes = SerializationUtils.serialize(model.lectutersCollection);
                firebaseStorage = FirebaseStorage.getInstance();
                storageReference = firebaseStorage.getReference("Lecturers").child("lecturersCollection");
                storageReference.putBytes(myBytes);
                for(Lecturer l : model.lectutersCollection) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = db.collection("Lecturers").document(l.getName());

                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                Map<String, Number> map;

                                if (!documentSnapshot.exists()) {
                                    map = new HashMap<String, Number>();

                                    Map<String, Map<String, Number>> tmpMap = new HashMap<>();

                                    tmpMap.put("grades", map);

                                    db.collection("Lecturers").document(l.getName()).set(tmpMap);
                                }
                            }
                        }
                    });
                }
                Toast.makeText(com.mobile.usoz.LecturersActivities.LecturersActivity.this, "Zmiany zostały wysłane", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(com.mobile.usoz.LecturersActivities.LecturersActivity.this, "Błąd podczas wysyłania zmian", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(com.mobile.usoz.LecturersActivities.LecturersActivity.this, "Błąd podczas wysyłania zmian", Toast.LENGTH_LONG).show();
        }
    }

    /** metoda wyswietlajaca komunikat o wyjsciu z aplikacji
     *  oraz ukrywajaca pole edycji dla administratora w przypadku jego otwarcia
     */

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

        //nie wychodz z aplikacji
        Button button = findViewById(R.id.exit_reject_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.lecturers_relative_layout1).setForeground(new ColorDrawable(Color.TRANSPARENT));

                findViewById(R.id.included_exit_layout).setVisibility(View.INVISIBLE);
                findViewById(R.id.included_exit_layout).setClickable(false);
            }
        });

        //wyjdz z aplikacji
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

    /** metoda uruchamiana po wyjsciu z LecturerPageActivity
     *  aktualizuje dane wykladowcy badz usuwa go z listy wykladowcow
     */

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

                    for (int i=0; i<model.lectutersCollection.size(); i++) {
                        l = model.lectutersCollection.get(i);
                        if(l.getFirstName().equals(name) && l.getSurname().equals(surname)) {

                            l.setUniversity(lecturer.getUniversity());
                            l.setLectures(lecturer.getLectures());

                            LinearLayout linearLayout1 = findViewById(R.id.lecturers_linear_layout);

                            for (int j=i; j<model.lectutersCollection.size(); j++) {
                                linearLayout1.removeViewAt(i);
                            }

                            for (int j=i; j<model.lectutersCollection.size(); j++) {
                                l = model.lectutersCollection.get(j);
                                addLecturer(l.getFirstName(), l.getSurname(), l.getUniversity());
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /** metoda obslugujaca przyciski w nawigacji
     */

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
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
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

    /** metoda ustawiajaca pole edycji dla administratora na niewidoczne
     */

    private void hideEditField() {
        findViewById(R.id.lecturers_edit_relative_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.lecturers_edit_relative_layout).setClickable(false);

        findViewById(R.id.lecturers_relative_layout1).setForeground(new ColorDrawable(Color.TRANSPARENT));

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.lecturers_frame_layout).getWindowToken(), 0);
    }

    /** metoda tworzaca menu edycji dla administratora
     */

    private void logOut() {
        //FIREBASE LOG OUT
        mAuth.signOut();

        // FB LOG OUT
        LoginManager.getInstance().logOut();
        UpdateUI();
    }

    private void UpdateUI() {
        Toast.makeText(LecturersActivity.this, getResources().getString(R.string.you_re_logged_out), Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(LecturersActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }

    /** metoda usuwa wykładowcę z kolekcji
     */

    private int deleteLecturer(String name, String surname) {
        //TODO: delete from database
        Lecturer l;

        for (int i=0; i<model.lectutersCollection.size(); i++) {
            l = model.lectutersCollection.get(i);

            if(l.getFirstName().equals(name) && l.getSurname().equals(surname)) {
                model.lectutersCollection.remove(i);
                return i;
            }
        }
        return -1;
    }
}

