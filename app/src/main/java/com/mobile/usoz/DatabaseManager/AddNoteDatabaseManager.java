package com.mobile.usoz.DatabaseManager;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.mobile.usoz.Calendar.Notes.AddNewNoteActivity;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;

import java.util.HashMap;
import java.util.Map;

// DB Manager of adding note to Database
public class AddNoteDatabaseManager extends DatabaseManager implements NotesDatabaseKeyValues {

    /* Saving new note*/
    public void saveNote(final String noteText, final String day, final String month){
        final Map<String, Object> note = new HashMap<>();
        note.put(KEY_NOTE, noteText);
        //Get document from firestore
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            // If there is any note in particular day we need to update date at this day.
                            // If not we set new value.
                            if(document.exists()) {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).update(KEY_NOTE, FieldValue.arrayUnion(noteText));
                            } else {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).set(note);
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).update(KEY_NOTE, FieldValue.arrayUnion(noteText));
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        // Now we have to add new date to our dates collection *if date not already exists*
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayUnion(day));
                            } else {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).set(note);
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayUnion(day));
                            }
                        }
                    }
                });
    }
}
