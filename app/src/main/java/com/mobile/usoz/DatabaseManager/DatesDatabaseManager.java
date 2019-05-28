package com.mobile.usoz.DatabaseManager;

import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mobile.usoz.Calendar.Calendar.DatesActivity;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.Protocols.DatesDatabaseManagerInterface;

import java.util.ArrayList;

public class DatesDatabaseManager extends DatabaseManager implements NotesDatabaseKeyValues {

    public void retrieveDatesFromFirebase(String month, final DatesDatabaseManagerInterface callback) {
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //Przypisanie do modelu pobranej listy dat
                            ArrayList<String> list = (ArrayList<String>) document.get(KEY_NOTE);
                            if (list.isEmpty()) {
                                //Toast.makeText(DatesActivity., "Folder is empty", Toast.LENGTH_SHORT).show();
                                callback.prepareArray(null);
                            } else {
                                for(int i=0; i < list.size(); i++) {
                                    System.out.println(list.get(i));
                                }
                                callback.prepareArray(list);
                            }
                        } else {
                            callback.prepareArray(null);
                            //Toast.makeText(DatesActivity.this, "Folder is empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                        //retrieveEventsFromFirebase();
                         callback.prepareArray(null);
                        }
                });
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                 @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    retrieveEventsFromFirebase();
//                    }
//                 });
    }

    public void retrieveEventsFromFirebase(String month, final DatesDatabaseManagerInterface callback) {

        /** pobiera ogolne eventy z firebase
         */

        db.collection(NOTES_COLLECTION_PATH).document(EVENTS_COLLECTION_PATH).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            ArrayList<String> list = (ArrayList<String>)document.get(KEY_NOTE);
                            if(!list.isEmpty()) {
                                //Toast.makeText(DatesActivity.this,"Folder is empty", Toast.LENGTH_SHORT).show();
                                callback.prepareArray(list);
                            } else {
                                callback.prepareArray(null);
                            }
                        } else {
                            //Toast.makeText(DatesActivity.this,"Folder is empty", Toast.LENGTH_SHORT).show();
                            callback.prepareArray(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //setupRecyclerView();
                callback.prepareArray(null);
            }
        });
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                setupRecyclerView();
//            }
//        });
    }
}
