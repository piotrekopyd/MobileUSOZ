package com.mobile.usoz.Maps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.usoz.Administrator.Administrator;
import com.mobile.usoz.Administrator.AdministratorCallback;
import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.CollectiveMethods.CollectiveMethods;
import com.mobile.usoz.CollectiveMethods.CollectiveMethodsCallback;
import com.mobile.usoz.LecturersActivities.LecturersActivity;
import com.mobile.usoz.R;
import com.mobile.usoz.UserAccount.LogInActivity;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import org.apache.commons.lang3.SerializationUtils;

import java.util.LinkedList;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private GoogleMap googleMap;

    private EditText titleText;
    private EditText snippetText;
    private Button saveButton;
    private Button deleteButton;

    private Spinner spinner;
    private MapsModel model;
    private BroadcastReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        findViewById(R.id.included_exit_layout).setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        model = new MapsModel();

        setupEditField();
        hideEditTextAndButtons();
        setupSpinner();

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (NullPointerException e) {
            Toast.makeText(com.mobile.usoz.Maps.MapsActivity.this, "Wystąpił błąd podczas ładowania mapy. Spróbuj ponownie za chwilę", Toast.LENGTH_LONG).show();
        }

        setupNavigation();

        if(CollectiveMethods.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)) && model.myMarkersCollection==null) {
            downloadMarkers();
        }

        setupReciever();
    }

    /** ustawianie pola edycji dla administratora
     */

    private void setupEditField() {
        titleText = findViewById(R.id.map_marker_title);
        snippetText = findViewById(R.id.map_marker_snippet);
        saveButton = findViewById(R.id.map_marker_save);
        deleteButton = findViewById(R.id.map_marker_delete);
    }

    /** ustawianie paska nawigacji
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
                hideEditTextAndButtons();
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_maps);
    }

    /** ustawianie recievera na nasluchiwanie zmiany polaczenia internetowego
     */

    private void setupReciever() {
        networkChangeReceiver = CollectiveMethods.setupReciever(new CollectiveMethodsCallback() {
            @Override
            public void onDownload(boolean download) {
                if(download) {
                    downloadMarkers();
                }
            }
        }, (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE), model.myMarkersCollection);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void setupSpinner() {
        spinner = findViewById(R.id.map_color_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(MapsActivity.this,
                R.layout.map_spinner, getResources().getTextArray(R.array.map_spinner_colors)) {
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();

        Administrator.isAdministrator(new AdministratorCallback() {
            @Override
            public void onCallback(boolean isAdministrator) {
                if(isAdministrator) {
                    inflater.inflate(R.menu.map_settings_menu, menu);
                } else {
                    inflater.inflate(R.menu.map_settings_user_menu, menu);
                }
            }
        }, mAuth.getUid());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item != null) {
            switch (item.getItemId()) {
                case (R.id.map_settings_hide):
                    hideEditTextAndButtons();
                    googleMap.clear();
                    break;
                case (R.id.map_settings_show):
                    for (MyMarker e :
                            model.myMarkersCollection) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(e.getLatitude(), e.getLongitude()));
                        markerOptions.title(e.getTitle());
                        markerOptions.snippet(e.getSnippet());
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(e.getColor()));
                        googleMap.addMarker(markerOptions);
                    }
                    break;
                case (R.id.map_settings_send):
                    try { //UPLOAD MARKERS TO FIREBASE
                        if(CollectiveMethods.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))) {
                            byte[] myBytes = SerializationUtils.serialize(model.myMarkersCollection);
                            firebaseStorage = FirebaseStorage.getInstance();
                            storageReference = firebaseStorage.getReference("Markers").child("myMarkers");
                            storageReference.putBytes(myBytes);
                            Toast.makeText(com.mobile.usoz.Maps.MapsActivity.this, "Zmiany zostały wysłane", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(com.mobile.usoz.Maps.MapsActivity.this, "Wystąpił błąd podczas zapisywania zmian do bazy danych!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(com.mobile.usoz.Maps.MapsActivity.this, "Wystąpił błąd podczas zapisywania zmian do bazy danych!", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        /** jesli uzytkownik jest administratorem, ustawiam dla niego listenery na mapie, oraz na markerach
        */

        Administrator.isAdministrator(new AdministratorCallback() {
            @Override
            public void onCallback(boolean isAdministrator) {
                if(isAdministrator) {
                    setupMapListeners();
                }
            }
        }, mAuth.getUid());

        /** ustawianie widoku mapy na krakow
         */

        CameraPosition cracow = new CameraPosition.Builder().target(new LatLng(50.06,  19.944)).zoom(12.5f).bearing(0).tilt(0).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cracow), 10, null);
    }

    private void setupMapListeners() {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MyMarker myMarker = new MyMarker(latLng.latitude, latLng.longitude,"", "", 0);
                model.myMarkersCollection.addLast(myMarker);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(myMarker.getMarkerOptions());
                Toast.makeText(MapsActivity.this, latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideEditTextAndButtons();
                googleMap.clear();
                for (MyMarker e:
                        model.myMarkersCollection) {
                    googleMap.addMarker(e.getMarkerOptions());
                }
            }
        });

        /** wyswietlanie pola edycji po kliknieciu na marker
         * */

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final com.google.android.gms.maps.model.Marker marker) {
                marker.showInfoWindow();
                showEditTextAndButton();
                titleText.setText(marker.getTitle());
                snippetText.setText(marker.getSnippet());
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LatLng latLng = marker.getPosition();

                        String t = titleText.getText().toString();
                        String s = snippetText.getText().toString();

                        for (int i=0; i<model.myMarkersCollection.size(); i++) {
                            MyMarker ll = model.myMarkersCollection.get(i);
                            if(ll.getPosition().equals(latLng)) {
                                model.myMarkersCollection.remove(i);
                                break;
                            }
                        }
                        MyMarker myMarker = new MyMarker(latLng.latitude, latLng
                                .longitude, t, s, 0);

                        marker.setTitle(t);
                        marker.setSnippet(s);
                        marker.hideInfoWindow();
                        hideEditTextAndButtons();
                        marker.showInfoWindow();
                        switch (spinner.getSelectedItemPosition()) {
                            case 0:
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                myMarker.setColor(BitmapDescriptorFactory.HUE_RED);
                                break;
                            case 1:
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                myMarker.setColor(BitmapDescriptorFactory.HUE_BLUE);
                                break;
                            case 2: //BROWN
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(26));
                                myMarker.setColor(26);
                                break;
                            case 3:
                                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                myMarker.setColor(BitmapDescriptorFactory.HUE_GREEN);
                                break;
                        }
                        model.myMarkersCollection.add(myMarker);

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude))
                                .zoom(14.5f)
                                .bearing(0)
                                .tilt(0)
                                .build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
                    }
                });

                /** obsluga przycisku usuwajacego marker z mapy
                 */

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LatLng latLng = marker.getPosition();

                        for (int i=0; i<model.myMarkersCollection.size(); i++) {
                            MyMarker ll = model.myMarkersCollection.get(i);
                            if(ll.getPosition().equals(latLng)) {
                                model.myMarkersCollection.remove(i);
                                break;
                            }
                        }
                        marker.remove();
                        hideEditTextAndButtons();
                    }
                });
                return true;
            }
        });
    }

    /** pobieranie listy markerow z firebase
     */

    private void downloadMarkers() {
        model.myMarkersCollection = new LinkedList<>();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Markers").child("myMarkers");
        storageReference.getBytes(100*1028*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                model.myMarkersCollection = SerializationUtils.deserialize(bytes);
                for (MyMarker e:
                        model.myMarkersCollection) {
                    googleMap.addMarker(e.getMarkerOptions());
                }
                unregisterReceiver(networkChangeReceiver);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(com.mobile.usoz.Maps.MapsActivity.this, "Wystąpił błąd podczas ładowania mapy. Spróbuj ponownie za chwilę", Toast.LENGTH_LONG).show();
            }
        });

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

        if(findViewById(R.id.maps_edit_relative_layout).getVisibility()==View.VISIBLE) {
            hideEditTextAndButtons();
            return;
        }

        findViewById(R.id.maps_relative_layout).setForeground(new ColorDrawable(Color.BLACK));
        findViewById(R.id.maps_relative_layout).getForeground().setAlpha(180);

        findViewById(R.id.included_exit_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.included_exit_layout).setClickable(true);

        //nie wychodz z aplikacji
        Button button = findViewById(R.id.exit_reject_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.maps_relative_layout).setForeground(new ColorDrawable(Color.TRANSPARENT));

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

    /** metoda obslugujaca przyciski w nawigacji
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                intent = new Intent(MapsActivity.this, UserProfileAcitivity.class);
                break;
            case R.id.nav_calendar:
                intent = new Intent(MapsActivity.this, CalendarActivity.class);
                break;
            case R.id.nav_lecturers:
                intent = new Intent(MapsActivity.this, LecturersActivity.class);
                break;
            case R.id.nav_maps:
                intent = new Intent(MapsActivity.this, MapsActivity.class);
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

    /** metoda ustawiajaca pole edycji dla administratora na widoczne
     */

    private void showEditTextAndButton() {
        findViewById(R.id.maps_edit_relative_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.maps_edit_relative_layout).setClickable(true);
        findViewById(R.id.maps_edit_relative_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEditTextAndButtons();
            }
        });

        findViewById(R.id.maps_relative_layout).setForeground(new ColorDrawable(Color.BLACK));
        findViewById(R.id.maps_relative_layout).getForeground().setAlpha(180);
    }

    /** metoda ustawiajaca pole edycji dla administratora na niewidoczne
     */

    private void hideEditTextAndButtons() {
        findViewById(R.id.maps_edit_relative_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.maps_edit_relative_layout).setClickable(false);

        findViewById(R.id.maps_relative_layout).setForeground(new ColorDrawable(Color.TRANSPARENT));

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.map_frame_layout).getWindowToken(), 0);
    }

    private void logOut() {
        //FIREBASE LOG OUT
        mAuth.signOut();

        // FB LOG OUT
        LoginManager.getInstance().logOut();
        UpdateUI();
    }

    private void UpdateUI() {
        Toast.makeText(MapsActivity.this, getResources().getString(R.string.you_re_logged_out), Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(MapsActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
