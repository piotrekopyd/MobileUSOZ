package com.mobile.usoz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.LinkedList;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener {

    //Layouts
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
    private Spinner spinner;
    private int mSelectedIndex = 0;
    private LinkedList<MyMarker> myMarkersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAuth = FirebaseAuth.getInstance();

        titleText = findViewById(R.id.map_marker_title);
        snippetText = findViewById(R.id.map_marker_snippet);
        saveButton = findViewById(R.id.map_marker_save);
        spinner = findViewById(R.id.map_color_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(MapsActivity.this,
                R.layout.map_spinner, getResources().getTextArray(R.array.colors)) {
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
                if(position == mSelectedIndex){
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

        hideEditTextAndButton();
        myMarkersCollection = new LinkedList<MyMarker>();

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (NullPointerException e) {}

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideEditTextAndButton();
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_maps);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.map_settings_hide) :
                hideEditTextAndButton();
                googleMap.clear();
                break;
            case (R.id.map_settings_show) :
                for (MyMarker e:
                        myMarkersCollection) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(e.latLng);
                    markerOptions.title(e.title);
                    markerOptions.snippet(e.snippet);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(e.color));
                    googleMap.addMarker(markerOptions);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MyMarker myMarker = new MyMarker(latLng,"", "", 0);
                myMarkersCollection.addLast(myMarker);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.addMarker(myMarker.getMarkerOptions());
                Toast.makeText(MapsActivity.this, latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideEditTextAndButton();
                googleMap.clear();
                for (MyMarker e:
                        myMarkersCollection) {
                    googleMap.addMarker(e.getMarkerOptions());
                }
            }
        });

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

                        for (int i=0; i<myMarkersCollection.size(); i++) {
                            MyMarker ll = myMarkersCollection.get(i);
                            if(ll.getPosition().equals(latLng)) {
                                myMarkersCollection.remove(i);
                                break;
                            }
                        }
                        MyMarker myMarker = new MyMarker(latLng, t, s, 0);

                        marker.setTitle(t);
                        marker.setSnippet(s);
                        marker.hideInfoWindow();
                        hideEditTextAndButton();
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
                        myMarkersCollection.add(myMarker);

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude))
                                .zoom(14.5f)
                                .bearing(0)
                                .tilt(0)
                                .build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
                    }
                });
                return true;
            }
        });

        CameraPosition cracow = new CameraPosition.Builder().target(new LatLng(50.06,  19.944))
                .zoom(12.5f)
                .bearing(0)
                .tilt(0)
                .build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cracow), 1100, null);


        /*storageReference = firebaseStorage.getInstance().getReference();
        StorageReference ref = storageReference.child("downloadTest.txt");

        try {
            final File file = File.createTempFile("downloadTest", "txt");
            ref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    List<String> list = new ArrayList<String>();
                    BufferedReader reader = null;

                    try {
                        reader = new BufferedReader(new FileReader(file));
                        String text = null;

                        while ((text = reader.readLine()) != null) {
                            list.add(text);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (reader != null) {
                                reader.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(list);
                    Toast.makeText(MapsActivity.this, "Success (:", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        } catch (Exception e) {

        }*/
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_calendar:
                intent = new Intent(MapsActivity.this, CalendarActivity.class);
                break;
            case R.id.nav_forum:
                intent = new Intent(MapsActivity.this, ForumActivity.class);
                break;
            case R.id.nav_lecturers:
                intent = new Intent(MapsActivity.this, LecturersActivity.class);
                break;
            case R.id.nav_maps:
                intent = new Intent(MapsActivity.this, MapsActivity.class);
                break;
            case R.id.nav_notes:
                intent = new Intent(MapsActivity.this, NotesActivity.class);
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

    private void showEditTextAndButton() {
        titleText.setVisibility(View.VISIBLE);
        snippetText.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
    }

    private void hideEditTextAndButton() {
        titleText.setText("");
        snippetText.setText("");
        titleText.setVisibility(View.INVISIBLE);
        snippetText.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
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
        Toast.makeText(MapsActivity.this, "You're logged out", Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(MapsActivity.this, LogInActivity.class);
        startActivity(loginIntent);
        finish();
    }
}

class MyMarker {
    public LatLng latLng;
    public String title;
    public String snippet;
    public float color;
    public MyMarker(LatLng l, String ti, String sn, float co) {
        latLng = new LatLng(l.latitude, l.longitude);
        title = ti;
        snippet = sn;
        color = co;
    }
    public MarkerOptions getMarkerOptions() {
        MarkerOptions markerOptions = new MarkerOptions();
        return markerOptions.position(latLng).title(title).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(color));
    }
    public LatLng getPosition() {
        return latLng;
    }
    public void setColor(float c) {
        color = c;
    }
}