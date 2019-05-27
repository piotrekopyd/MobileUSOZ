package com.mobile.usoz.DatabaseManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseManager {
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
