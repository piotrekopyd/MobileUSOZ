package com.mobile.usoz.DatabaseManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


/* Main parent class. Every child class extends this class. */
public class DatabaseManager {
    /* Internals:
        mAuth - Current firebase session
        user - Store user data
        db - reference to Firestore (database)
    */
    protected FirebaseAuth mAuth;
    protected FirebaseUser user;
    protected FirebaseFirestore db ;


    private void setup(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }
    // Init
    DatabaseManager(){
        setup();
    }

}
