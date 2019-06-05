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

    //
    public void retrieveDatesFromFirebase(String month, final DatesDatabaseManagerInterface callback) {
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //fetching list of dates in particular month
                            ArrayList<String> list = (ArrayList<String>) document.get(KEY_NOTE);
                            // if list is empty return null. If not pass list to interface function
                            if (list.isEmpty()) {
                                callback.prepareArray(null);
                            } else {
                                callback.prepareArray(list);
                            }
                        } else {
                            callback.prepareArray(null);
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
                                callback.prepareArray(list);
                            } else {
                                callback.prepareArray(null);
                            }
                        } else {
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
    }
}
