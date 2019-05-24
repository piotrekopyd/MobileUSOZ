package com.mobile.usoz.Administrator;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Administrator {

    //Metoda sprawdza czy uzytkownik o identyfikatorze UID jest administratorem

    public static void isAdministrator(final AdministratorCallback administratorCallback, final String UID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Administrator").document("Admins").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<String> adminCollection = (ArrayList<String>) document.get("adminArray");
                                administratorCallback.onCallback(adminCollection.contains(UID));
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                administratorCallback.onCallback(false);
            }
        });
    }
}
