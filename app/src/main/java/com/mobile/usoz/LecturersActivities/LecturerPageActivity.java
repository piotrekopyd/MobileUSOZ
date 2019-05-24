package com.mobile.usoz.LecturersActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mobile.usoz.Administrator.Administrator;
import com.mobile.usoz.Administrator.AdministratorCallback;
import com.mobile.usoz.R;

public class LecturerPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private Spinner spinner;

    private TextView universityTextView;
    private TextView lecturesTextView;
    private TextView textViewRate;
    private Button saveButton;

    private LecturerPageModel model;

    /**Metoda wywoływana przy tworzeniu Activity, tworzy model danych oraz UI
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_page);
        findViewById(R.id.lecturer_page_confirm_relative_layout).setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        model = new LecturerPageModel();

        Intent intent = getIntent();
        model.lecturer = (Lecturer) intent.getSerializableExtra("serialized_lecturer");
        model.grade = intent.getDoubleExtra("lecturer_grade", 0);
        model.gradeUID = intent.getDoubleExtra("grade_UID", 0);
        model.gradesMapSize = intent.getIntExtra("gradesMapSize", 0);

        setupEditField();

        hideEditField();

        setupLecturerDescription();

        setupSpinner();

        setupRateButton();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(model.lecturer.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /** Metoda przypisujaca funkcje oceniania wykładowcy do przycisku "rateButton"
     *  funkcja wykorzystuje mechanizm transakcj aby zapobiec sytuacji w ktorej uzytkownik ocenia wykladowce z kilku roznych urzadzen w tym samym czasie
     */

    private void setupRateButton() {
        model.chosenGrade = model.currentGrade = model.gradeUID;
        if(model.gradeUID == 0) {
            /** jesli wykladowca nie posiada oceny, ustawiam listener dla "rateButton" aby umożliwic uzytkownikowi ocenę wykładowcy
             */

            spinner.setSelection(0);
            final Button rateButton = findViewById(R.id.rateButton);

            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (spinner.getSelectedItemPosition()) {
                        case 0:
                            model.currentGrade = 0;
                            break;
                        case 1:
                            model.currentGrade = 2;
                            break;
                        case 2:
                            model.currentGrade = 3;
                            break;
                        case 3:
                            model.currentGrade = 3.5;
                            break;
                        case 4:
                            model.currentGrade = 4;
                            break;
                        case 5:
                            model.currentGrade = 4.5;
                            break;
                        case 6:
                            model.currentGrade = 5;
                            break;
                    }

                    if(model.currentGrade == 0) return;

                    /** wątek jest odpowiedzialny za wysłanie oceny do bazy danych
                     */

                    Thread thread = new Thread("updateGrade() Thread") {
                        @Override
                        public void run() {
                            LecturersActivity.updateGrade(mAuth.getUid(), model.lecturer.getName(), model.currentGrade);
                            super.run();
                        }
                    };
                    thread.start();

                    spinner = findViewById(R.id.spinnerLecturerGrade);

                    if(model.currentGrade==2) {
                        spinner.setSelection(1);
                    } else {
                        spinner.setSelection((int) (model.currentGrade*2 - 4));
                    }

                    spinner.setEnabled(false);
                    spinner.setClickable(false);

                    model.chosenGrade = model.currentGrade;

                    model.grade *= model.gradesMapSize;
                    model.grade += model.chosenGrade;
                    model.gradesMapSize++;
                    model.grade /= model.gradesMapSize;

                    textViewRate.setText(String.format("%.2f", model.grade));
                    rateButton.setClickable(false);
                }
            });
        } else {
            if(model.gradeUID==2) {
                spinner.setSelection(1);
            } else {
                spinner.setSelection((int) (model.gradeUID*2 - 4));
            }
            spinner.setEnabled(false);
        }
    }

    /** Metoda uzupelniająca pole z informacjami odnosnie wykladowcy,
     *  jesli wykladowca posiada ocene, to wpisuje ją do "textViewRate", wpp. wpisuje "--"
     */

    private void setupLecturerDescription() {
        TextView textView1 = findViewById(R.id.textViewLecturerName);
        TextView textView2 = findViewById(R.id.textViewUniversity);
        String text = model.lecturer.getFirstName() + " " + model.lecturer.getSurname();
        textView1.setText(text);
        textView2.setText(model.lecturer.getUniversity());
        String[] strArray = model.lecturer.getLectures();

        if(strArray!=null) {
            setLectures(strArray);
        }

        textViewRate = findViewById(R.id.textViewRate);

        if(model.grade==0) {
            textViewRate.setText("--");
        } else {
            textViewRate.setText(String.format("%.2f", model.grade));
        }
    }

    /** Metoda ustawiająca pole edycji dla administratorow
     */

    private void setupEditField() {
        universityTextView = findViewById(R.id.lecturer_page_text_university);
        lecturesTextView = findViewById(R.id.lecturer_page_text_lectures);
        saveButton = findViewById(R.id.lecturer_page_button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.lecturer.setUniversity(universityTextView.getText().toString());
                TextView tv = findViewById(R.id.textViewUniversity);
                String[] strArray = lecturesTextView.getText().toString().split("\n");
                model.lecturer.setLectures(strArray);
                setLectures(strArray);
                hideEditField();
            }
        });
    }

    /** Metoda ustawiająca spinner pozwalajacy wybrac ocene sposrod listy dostepnych ocen
     */

    private void setupSpinner() {
        spinner = findViewById(R.id.spinnerLecturerGrade);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(LecturerPageActivity.this,
                R.layout.map_spinner, getResources().getTextArray(R.array.lecturers_grades)) {


            //ustawianie koloru wybranej oceny na czarny
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.BLACK);
                return tv;
            }

            //akcentowanie wybranej oceny na kolor czarny
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.GRAY);
                if(position == model.mSelectedIndex) {
                    tv.setTextColor(Color.BLACK);
                }
                return tv;
            }
        };

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                model.mSelectedIndex = i;
                if(model.mSelectedIndex==0) {
                    model.chosenGrade = 0;
                } else if (model.mSelectedIndex==1) {
                    model.chosenGrade = 2;
                } else {
                    model.chosenGrade = ((double)model.mSelectedIndex + 4) / 2;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /** Metoda ustawiająca menu edycji dla administratorow
     */

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();

        Administrator.isAdministrator(new AdministratorCallback() {
            @Override
            public void onCallback(boolean isAdministrator) {
                if(isAdministrator) {
                    inflater.inflate(R.menu.lecturer_page_menu, menu);
                }
            }
        }, mAuth.getUid());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.lecturer_page_edit_relative_layout).getVisibility()==View.VISIBLE) {
            hideEditField();
            return;
        }

        /** jesli uzytkownik nie zatwierdzil wybranej oceny przyciskiem "rateButton", wyswietlany jest komunikat o podjeciu decyzji
         *  czy uzytkownik chce odrzucic, zaakceptowac, badz zmienic ocene
         */

        if(model.chosenGrade==model.currentGrade) {

            getIntent().putExtra("updateLecturerOnBackPressed", true);
            getIntent().putExtra("lecturer1", model.lecturer);

            setResult(RESULT_OK, getIntent());

            super.onBackPressed();
        } else {
            findViewById(R.id.lecturer_page_confirm_relative_layout).setClickable(true);
            findViewById(R.id.lecturer_page_confirm_relative_layout).setVisibility(View.VISIBLE);

            findViewById(R.id.lecturer_page_relative_layout).setForeground(new ColorDrawable(Color.BLACK));
            findViewById(R.id.lecturer_page_relative_layout).getForeground().setAlpha(180);

            Button button = findViewById(R.id.lecturer_page_confirm_frame_layout_accept_button);

            //akceptacja oceny
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Thread thread = new Thread("updateGrade() Thread") {
                        @Override
                        public void run() {
                            LecturersActivity.updateGrade(mAuth.getUid(), model.lecturer.getName(), model.chosenGrade);
                            super.run();
                        }
                    };
                    thread.start();

                    model.currentGrade = model.chosenGrade;
                    onBackPressed();
                }
            });

            //odrzucenie oceny
            button = findViewById(R.id.lecturer_page_confirm_frame_layout_reject_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.currentGrade = model.chosenGrade;
                    onBackPressed();
                }
            });

            //zmiana oceny
            button = findViewById(R.id.lecturer_page_confirm_frame_layout_cancel_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    findViewById(R.id.lecturer_page_confirm_relative_layout).setClickable(false);
                    findViewById(R.id.lecturer_page_confirm_relative_layout).setVisibility(View.INVISIBLE);

                    findViewById(R.id.lecturer_page_relative_layout).setForeground(new ColorDrawable(Color.TRANSPARENT));
                }
            });
        }
    }

    /** obsluga przyciskow w menu edycji dla administratora
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return false;
            case (R.id.lecturer_page_delete):
                Intent intent1 = new Intent();
                intent1.putExtra("deleteLecturer", true);
                intent1.putExtra("lecturerName", model.lecturer.getFirstName());
                intent1.putExtra("lecturerSurname", model.lecturer.getSurname());
                setResult(RESULT_OK, intent1);
                this.finish();
            case (R.id.lecturer_page_edit):
                showEditField();
                universityTextView.setText(model.lecturer.getUniversity());
                if(model.lecturer.getLectures()!=null) {
                    setLecturerPageTextLectures(model.lecturer.getLectures());
                }
                break;
            case (R.id.lecturer_page_save):
                Intent intent2 = new Intent();
                intent2.putExtra("updateLecturer", true);
                intent2.putExtra("lecturer", model.lecturer);
                setResult(RESULT_OK, intent2);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /** metoda wypelniajaca pole z informacja o prowadzonych przedmiotach
     */

    private void setLecturerPageTextLectures(String[] array) {
        lecturesTextView.setText("");

        for(int i=0; i<array.length; i++) {
            lecturesTextView.append(array[i]);

            if(i!=(array.length-1)) {
                lecturesTextView.append("\n");
            }
        }
    }

    /** metoda ustawiajaca pole z informacja o prowadzonych przedmiotach po dokonaniu edycji przez administratora
     */

    private void setLectures(String[] array) {
        TextView tv = findViewById(R.id.textViewLectures);
        tv.setText("");

        for(int i=0; i<array.length; i++) {
            tv.append("* "+ array[i]);
            if(i!=(array.length-1)) {
                tv.append("\n");
            }
        }
    }

    /** metoda ustawiajaca pole edycji dla administratora na widoczne
     */

    private void showEditField() {
        findViewById(R.id.lecturer_page_edit_relative_layout).setClickable(true);
        findViewById(R.id.lecturer_page_edit_relative_layout).setVisibility(View.VISIBLE);

        findViewById(R.id.lecturer_page_relative_layout).setForeground(new ColorDrawable(Color.BLACK));
        findViewById(R.id.lecturer_page_relative_layout).getForeground().setAlpha(180);

        View view = findViewById(R.id.lecturer_page_edit_relative_layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEditField();
            }
        });
    }

    /** metoda ustawiajaca pole edycji dla administratora na niewidoczne
     */

    private void hideEditField() {
        findViewById(R.id.lecturer_page_edit_relative_layout).setClickable(false);
        findViewById(R.id.lecturer_page_edit_relative_layout).setVisibility(View.INVISIBLE);

        findViewById(R.id.lecturer_page_relative_layout).setForeground(new ColorDrawable(Color.TRANSPARENT));

        //ukrywanie klawiatury
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.lecturer_page_frame_layout).getWindowToken(), 0);
    }
}